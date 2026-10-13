import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AuthService } from './auth/auth.service';
import { LoginDialog } from './auth/login-dialog';

@Component({
  imports: [
    MatButtonModule,
    MatDialogModule,
    MatIconModule,
    MatListModule,
    MatTooltipModule,
    MatToolbarModule,
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
  ],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App {
  protected readonly authService = inject(AuthService);
  protected readonly sidebarExpanded = signal(true);
  private readonly dialog = inject(MatDialog);
  private readonly router = inject(Router);

  protected toggleSidebar(): void {
    this.sidebarExpanded.update((expanded) => !expanded);
  }

  protected openLoginDialog(): void {
    this.dialog.open(LoginDialog, {
      width: 'min(28rem, calc(100vw - 2rem))',
      maxWidth: 'calc(100vw - 2rem)',
      autoFocus: 'first-tabbable',
    });
  }

  protected logout(): void {
    this.authService.logout();
    if (this.router.url.split(/[?#]/, 1)[0] === '/mengenabgabe') {
      void this.router.navigateByUrl('/mengenabgabenanzeige');
    }
  }
}
