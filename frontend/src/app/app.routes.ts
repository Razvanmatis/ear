import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: 'mengenabgabe',
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
