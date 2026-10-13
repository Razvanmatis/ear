import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import {
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
  MatDateFormats,
} from '@angular/material/core';
import { MatDatepicker, MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { GeraeteartenService } from '../../generated/api/api/geraetearten.service';
import { MengenabgabenService } from '../../generated/api/api/mengenabgaben.service';
import { Geraeteart } from '../../generated/api/model/geraeteart';
import { MengenabgabeRequest } from '../../generated/api/model/mengenabgabeRequest';

const MONTH_YEAR_DATE_FORMATS: MatDateFormats = {
  parse: {
    dateInput: { month: 'long', year: 'numeric' },
  },
  display: {
    dateInput: { month: 'long', year: 'numeric' },
    monthYearLabel: { year: 'numeric', month: 'short' },
    dateA11yLabel: { year: 'numeric', month: 'long', day: 'numeric' },
    monthYearA11yLabel: { year: 'numeric', month: 'long' },
  },
};

const integerValidator: ValidatorFn = ({ value }) =>
  value === null || Number.isInteger(value) ? null : { integer: true };

type MengenabgabeForm = FormGroup<{
  menge: FormControl<number | null>;
  einheit: FormControl<MengenabgabeRequest['einheit'] | null>;
  zeitraum: FormControl<Date | null>;
  geraeteartnummer: FormControl<number | null>;
  registrierungsnummer: FormControl<string | null>;
  wahrheitserklaerung: FormControl<boolean>;
}>;

@Component({
  imports: [
    MatButtonModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatNativeDateModule,
    MatSelectModule,
    ReactiveFormsModule,
  ],
  selector: 'app-mengenabgabe',
  templateUrl: './mengenabgabe.html',
  styleUrl: './mengenabgabe.css',
  providers: [
    { provide: MAT_DATE_FORMATS, useValue: MONTH_YEAR_DATE_FORMATS },
    { provide: MAT_DATE_LOCALE, useValue: 'de-DE' },
  ],
})
export class Mengenabgabe implements OnInit {
  private readonly geraeteartenService = inject(GeraeteartenService);
  private readonly mengenabgabenService = inject(MengenabgabenService);

  protected readonly geraetearten = signal<Geraeteart[]>([]);
  protected readonly isLoadingGeraetearten = signal(true);
  protected readonly isSubmitting = signal(false);
  protected readonly successMessage = signal('');
  protected readonly errorMessage = signal('');
  protected readonly geraeteartenError = signal('');

  protected readonly einheiten = [
    { wert: 1, bezeichnung: 'Einheit 1' },
    { wert: 2, bezeichnung: 'Einheit 2' },
    { wert: 3, bezeichnung: 'Einheit 3' },
  ] as const;

  readonly form: MengenabgabeForm = new FormGroup({
    menge: new FormControl<number | null>(null, [
      Validators.required,
      Validators.min(0),
    ]),
    einheit: new FormControl<MengenabgabeRequest['einheit'] | null>(
      null,
      Validators.required,
    ),
    zeitraum: new FormControl<Date | null>(null, Validators.required),
    geraeteartnummer: new FormControl<number | null>(null, [
      Validators.required,
      integerValidator,
    ]),
    registrierungsnummer: new FormControl<string | null>(null, [
      Validators.required,
      Validators.pattern(/^\d+$/),
      Validators.minLength(8),
      Validators.maxLength(8)
    ]),
    wahrheitserklaerung: new FormControl(false, {
      nonNullable: true,
      validators: Validators.requiredTrue,
    }),
  });

  ngOnInit(): void {
    this.loadGeraetearten();
  }

  protected loadGeraetearten(): void {
    this.isLoadingGeraetearten.set(true);
    this.geraeteartenError.set('');
    this.geraeteartenService.getGeraetearten().subscribe({
      next: (geraetearten) => {
        this.geraetearten.set(geraetearten);
        this.isLoadingGeraetearten.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.geraeteartenError.set(this.getRequestErrorMessage(error));
        this.isLoadingGeraetearten.set(false);
      },
    });
  }

  protected setMonthAndYear(monthAndYear: Date, datepicker: MatDatepicker<Date>): void {
    const selectedMonth = new Date(
      monthAndYear.getFullYear(),
      monthAndYear.getMonth(),
      1,
    );
    this.form.controls.zeitraum.setValue(selectedMonth);
    this.form.controls.zeitraum.markAsTouched();
    datepicker.close();
  }

  protected keepRegistrierungsnummerNumeric(event: Event): void {
    const input = event.target;
    if (!(input instanceof HTMLInputElement)) {
      return;
    }

    const numericValue = input.value.replace(/\D/g, '').slice(0, 8);
    if (numericValue !== input.value) {
      input.value = numericValue;
      this.form.controls.registrierungsnummer.setValue(numericValue);
    }
  }

  protected submit(): void {
    this.successMessage.set('');
    this.errorMessage.set('');

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const {
      menge,
      einheit,
      zeitraum,
      geraeteartnummer,
      registrierungsnummer,
      wahrheitserklaerung,
    } = this.form.getRawValue();

    if (
      menge === null ||
      einheit === null ||
      zeitraum === null ||
      geraeteartnummer === null ||
      registrierungsnummer === null
    ) {
      return;
    }

    const request: MengenabgabeRequest = {
      menge,
      einheit,
      jahr: zeitraum.getFullYear(),
      monat: zeitraum.getMonth() + 1,
      geraeteartnummer,
      registrierungsnummer: +registrierungsnummer,
      wahrheitserklaerung,
    };

    this.isSubmitting.set(true);
    this.mengenabgabenService.createMengenabgabe(request).subscribe({
      next: () => {
        this.successMessage.set('Die Mengenabgabe wurde erfolgreich gespeichert.');
        this.form.reset({
          menge: null,
          einheit: null,
          zeitraum: null,
          geraeteartnummer: null,
          registrierungsnummer: null,
          wahrheitserklaerung: false,
        });
        this.isSubmitting.set(false);
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage.set(this.getRequestErrorMessage(error));
        this.isSubmitting.set(false);
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

    return `Die Anfrage ist fehlgeschlagen (HTTP ${error.status}). Bitte überprüfen Sie Ihre Eingaben und versuchen Sie es erneut.`;
  }

  private getProblemDetail(
    body: unknown,
  ): { detail?: string; title?: string } | undefined {
    if (typeof body !== 'object' || body === null) {
      return undefined;
    }

    const detail = 'detail' in body && typeof body.detail === 'string'
      ? body.detail
      : undefined;
    const title = 'title' in body && typeof body.title === 'string'
      ? body.title
      : undefined;

    return { detail, title };
  }
}
