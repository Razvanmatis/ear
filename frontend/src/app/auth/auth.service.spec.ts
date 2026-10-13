import {
  HttpClient,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { basicAuthInterceptor } from './basic-auth.interceptor';
import { API_BASE_URL, AuthService } from './auth.service';

describe('AuthService', () => {
  let authService: AuthService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptors([basicAuthInterceptor])),
        provideHttpClientTesting(),
        { provide: API_BASE_URL, useValue: 'http://localhost:8080' },
      ],
    });
    authService = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should only authenticate after the backend validates the credentials', () => {
    let loginSucceeded = false;
    authService.login('testuser', 'secret').subscribe(() => {
      loginSucceeded = true;
    });

    const validationRequest = httpTestingController.expectOne(
      'http://localhost:8080/auth/validate',
    );
    expect(validationRequest.request.headers.get('Authorization')).toBe(
      `Basic ${btoa('testuser:secret')}`,
    );
    expect(authService.isAuthenticated()).toBe(false);
    validationRequest.flush(null, { status: 204, statusText: 'No Content' });

    expect(loginSucceeded).toBe(true);
    expect(authService.isAuthenticated()).toBe(true);
  });

  it('should remain logged out when the backend rejects the credentials', () => {
    let loginFailed = false;
    authService.login('testuser', 'wrong-password').subscribe({
      error: () => {
        loginFailed = true;
      },
    });

    httpTestingController
      .expectOne('http://localhost:8080/auth/validate')
      .flush(
        { title: 'Unauthorized' },
        { status: 401, statusText: 'Unauthorized' },
      );

    expect(loginFailed).toBe(true);
    expect(authService.isAuthenticated()).toBe(false);
  });

  it('should send the Authorization header to the API until logout', () => {
    authService.login('testuser', 'secret').subscribe();
    httpTestingController
      .expectOne('http://localhost:8080/auth/validate')
      .flush(null, { status: 204, statusText: 'No Content' });

    const http = TestBed.inject(HttpClient);
    http.get('http://localhost:8080/resource').subscribe();
    const authenticatedRequest = httpTestingController.expectOne(
      'http://localhost:8080/resource',
    );
    expect(authenticatedRequest.request.headers.get('Authorization')).toBe(
      `Basic ${btoa('testuser:secret')}`,
    );
    authenticatedRequest.flush({});

    authService.logout();
    http.get('http://localhost:8080/resource').subscribe();
    const loggedOutRequest = httpTestingController.expectOne(
      'http://localhost:8080/resource',
    );
    expect(loggedOutRequest.request.headers.has('Authorization')).toBe(false);
    loggedOutRequest.flush({});
    expect(authService.isAuthenticated()).toBe(false);
  });
});
