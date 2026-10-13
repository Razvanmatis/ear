import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSortModule, Sort } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MengenabgabenService } from '../../generated/api/api/mengenabgaben.service';
import { Mengenabgabe } from '../../generated/api/model/mengenabgabe';

@Component({
  imports: [
    CommonModule,
    MatButtonModule,
    MatIconModule,
    MatPaginatorModule,
    MatProgressSpinnerModule,
    MatSortModule,
    MatTableModule,
  ],
  selector: 'app-mengenabgabenanzeige',
  templateUrl: './mengenabgabenanzeige.html',
  styleUrl: './mengenabgabenanzeige.css',
})
export class Mengenabgabenanzeige implements OnInit {
  private readonly mengenabgabenService = inject(MengenabgabenService);

  protected readonly mengenabgaben = signal<Mengenabgabe[]>([]);
  protected readonly totalElements = signal(0);
  protected readonly pageIndex = signal(0);
  protected readonly pageSize = signal(10);
  protected readonly isLoading = signal(true);
  protected readonly errorMessage = signal('');
  protected readonly sortActive = signal('jahr');
  protected readonly sortDirection = signal<'asc' | 'desc' | ''>('desc');
  protected readonly displayedColumns = [
    'id',
    'zeitraum',
    'menge',
    'einheit',
    'geraeteartnummer',
    'registrierungsnummer',
    'wahrheitserklaerung',
  ];

  ngOnInit(): void {
    this.loadMengenabgaben();
  }

  protected onSortChange(sort: Sort): void {
    this.sortActive.set(sort.active);
    this.sortDirection.set(sort.direction);
    this.pageIndex.set(0);
    this.loadMengenabgaben();
  }

  protected onPageChange(event: PageEvent): void {
    this.pageIndex.set(event.pageIndex);
    this.pageSize.set(event.pageSize);
    this.loadMengenabgaben();
  }

  protected retry(): void {
    this.loadMengenabgaben();
  }

  protected formatZeitraum(mengenabgabe: Mengenabgabe): string {
    const month = String(mengenabgabe.monat).padStart(2, '0');
    return `${month}/${mengenabgabe.jahr}`;
  }

  protected geraeteartName(nummer: number): string {
    return String(nummer);
  }

  protected einheitName(einheit: number): string {
    return `Einheit ${einheit}`;
  }

  private loadMengenabgaben(): void {
    this.isLoading.set(true);
    this.errorMessage.set('');

    const sort =
      this.sortDirection() && this.sortActive() === 'jahr'
        ? [
            `jahr,${this.sortDirection()}`,
            `monat,${this.sortDirection()}`,
          ]
        : this.sortDirection()
          ? [`${this.sortActive()},${this.sortDirection()}`]
          : [];

    this.mengenabgabenService
      .getMengenabgaben(this.pageIndex(), this.pageSize(), sort)
      .subscribe({
        next: (response) => {
          this.mengenabgaben.set(response.content);
          this.totalElements.set(response.totalElements);
          this.pageIndex.set(response.page);
          this.pageSize.set(response.size);
          this.isLoading.set(false);
        },
        error: (error: HttpErrorResponse) => {
          this.errorMessage.set(this.getRequestErrorMessage(error));
          this.isLoading.set(false);
        },
      });
  }

  private getRequestErrorMessage(error: HttpErrorResponse): string {
    if (error.status === 0) {
      return 'Der Server ist derzeit nicht erreichbar. Bitte versuchen Sie es später erneut.';
    }

    const problem = this.getProblemDetail(error.error);
    if (problem?.detail) {
      const title = problem.title ? `${problem.title}: ` : '';
      return `${title}${problem.detail} (HTTP ${error.status})`;
    }

    return `Die Mengenabgaben konnten nicht geladen werden (HTTP ${error.status}). Bitte versuchen Sie es erneut.`;
  }

  private getProblemDetail(
    body: unknown,
  ): { detail?: string; title?: string } | undefined {
    if (typeof body !== 'object' || body === null) {
      return undefined;
    }

    const detail =
      'detail' in body && typeof body.detail === 'string'
        ? body.detail
        : undefined;
    const title =
      'title' in body && typeof body.title === 'string'
        ? body.title
        : undefined;

    return { detail, title };
  }
}
