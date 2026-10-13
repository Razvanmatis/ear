import { HttpClient, HttpHeaders } from '@angular/common/http';
import {
  computed,
  inject,
  Injectable,
  InjectionToken,
  signal,
} from '@angular/core';
import { tap } from 'rxjs';

export const API_BASE_URL = new InjectionToken<string>('API_BASE_URL');

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly apiBaseUrl = inject(API_BASE_URL);
  private readonly authorizationHeader = signal<string | null>(null);

  readonly isAuthenticated = computed(
    () => this.authorizationHeader() !== null,
  );

  login(username: string, password: string) {
    const authorizationHeader = `Basic ${this.encodeCredentials(username, password)}`;

    return this.http
      .get<void>(`${this.apiBaseUrl}/auth/validate`, {
        headers: new HttpHeaders({ Authorization: authorizationHeader }),
      })
      .pipe(tap(() => this.authorizationHeader.set(authorizationHeader)));
  }

  logout(): void {
    this.authorizationHeader.set(null);
  }

  getAuthorizationHeader(): string | null {
    return this.authorizationHeader();
  }

  private encodeCredentials(username: string, password: string): string {
    const bytes = new TextEncoder().encode(`${username}:${password}`);
    const binary = Array.from(bytes, (byte) => String.fromCharCode(byte)).join('');
    return btoa(binary);
  }
}
