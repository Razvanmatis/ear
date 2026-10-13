import { Routes } from '@angular/router';
import { authenticatedGuard } from './auth/auth.guard';

export const routes: Routes = [
  {
    path: 'mengenabgabe',
    canActivate: [authenticatedGuard],
    loadComponent: () =>
      import('./pages/mengenabgabe/mengenabgabe').then(
        ({ Mengenabgabe }) => Mengenabgabe,
      ),
  },
  {
    path: 'mengenabgabenanzeige',
    loadComponent: () =>
      import('./pages/mengenabgabenanzeige/mengenabgabenanzeige').then(
        ({ Mengenabgabenanzeige }) => Mengenabgabenanzeige,
      ),
  },
];
