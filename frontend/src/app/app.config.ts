import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
} from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { provideApi } from './generated/api/provide-api';
import { routes } from './app.routes';
import { API_BASE_URL } from './auth/auth.service';
import { basicAuthInterceptor } from './auth/basic-auth.interceptor';
import { environment } from './environments/environment';

const apiBaseUrl = environment.backend_url;

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    { provide: API_BASE_URL, useValue: apiBaseUrl },
    provideHttpClient(withInterceptors([basicAuthInterceptor])),
    provideApi(apiBaseUrl),
    provideRouter(routes),
  ],
};
