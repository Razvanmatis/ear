import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { API_BASE_URL, AuthService } from './auth.service';

export const basicAuthInterceptor: HttpInterceptorFn = (request, next) => {
  const authorizationHeader = inject(AuthService).getAuthorizationHeader();
  const apiBaseUrl = inject(API_BASE_URL);

  if (!authorizationHeader || !request.url.startsWith(apiBaseUrl)) {
    return next(request);
  }

  return next(
    request.clone({
      setHeaders: { Authorization: authorizationHeader },
    }),
  );
};
