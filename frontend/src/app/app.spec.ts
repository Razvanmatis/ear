import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { App } from './app';
import { routes } from './app.routes';
import { Mengenabgabe } from './pages/mengenabgabe/mengenabgabe';
import { Mengenabgabenanzeige } from './pages/mengenabgabenanzeige/mengenabgabenanzeige';
import { AuthService, API_BASE_URL } from './auth/auth.service';
import { provideApi } from './generated/api/provide-api';

describe('App', () => {
  let httpTestingController: HttpTestingController;
  let authService: AuthService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        provideRouter(routes),
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: API_BASE_URL, useValue: 'http://localhost:8080' },
        provideApi('http://localhost:8080'),
      ],
    })
      .compileComponents();
    httpTestingController = TestBed.inject(HttpTestingController);
    authService = TestBed.inject(AuthService);
    authService.logout();
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should disable the Mengenabgabe navigation until login succeeds', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const mengenabgabeLink = fixture.nativeElement.querySelector(
      'button[aria-label="Mengenabgabe"]',
    ) as HTMLButtonElement;
    const loginHint = fixture.nativeElement.querySelector(
      '#mengenabgabe-login-hint',
    ) as HTMLElement;

    expect(mengenabgabeLink.disabled).toBe(true);
    expect(loginHint.textContent).toContain('Bitte zuerst anmelden');

    authService.login('user', 'password').subscribe();
    httpTestingController
      .expectOne('http://localhost:8080/auth/validate')
      .flush(null, { status: 204, statusText: 'No Content' });
    fixture.detectChanges();

    expect(mengenabgabeLink.disabled).toBe(false);
    expect(fixture.nativeElement.querySelector('#mengenabgabe-login-hint'))
      .toBeNull();
  });

  it('should provide only the two requested routes', () => {
    expect(routes.map(({ path }) => path)).toEqual([
      'mengenabgabe',
      'mengenabgabenanzeige',
    ]);
  });

  it('should navigate to the Mengenabgabe route', async () => {
    authService.login('user', 'password').subscribe();
    httpTestingController
      .expectOne('http://localhost:8080/auth/validate')
      .flush(null, { status: 204, statusText: 'No Content' });
    const harness = await RouterTestingHarness.create();

    await harness.navigateByUrl('/mengenabgabe', Mengenabgabe);
    httpTestingController
      .expectOne('http://localhost:8080/geraetearten')
      .flush([]);

    expect(harness.routeNativeElement?.querySelector('h1')?.textContent)
      .toBe('Mengenabgabe');
  });

  it('should navigate to the Mengenabgabenanzeige route', async () => {
    const harness = await RouterTestingHarness.create();

    await harness.navigateByUrl('/mengenabgabenanzeige', Mengenabgabenanzeige);
    httpTestingController
      .expectOne((request) => request.url === 'http://localhost:8080/mengenabgaben')
      .flush({
        content: [],
        page: 0,
        size: 10,
        totalElements: 0,
        totalPages: 0,
      });

    expect(harness.routeNativeElement?.querySelector('h1')?.textContent)
      .toBe('Mengenabgabenanzeige');
  });

  it('should redirect to the public list when Mengenabgabe is visited without login', async () => {
    const harness = await RouterTestingHarness.create();

    await harness.navigateByUrl('/mengenabgabe');
    httpTestingController
      .expectOne((request) => request.url === 'http://localhost:8080/mengenabgaben')
      .flush({
        content: [],
        page: 0,
        size: 10,
        totalElements: 0,
        totalPages: 0,
      });

    expect(harness.routeNativeElement?.querySelector('h1')?.textContent)
      .toBe('Mengenabgabenanzeige');
  });
});
