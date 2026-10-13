import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Mengenabgabe } from './mengenabgabe';

describe('Mengenabgabe', () => {
  let httpTestingController: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Mengenabgabe],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should map the form fields to the POST request', () => {
    const fixture = TestBed.createComponent(Mengenabgabe);
    fixture.detectChanges();

    httpTestingController
      .expectOne('http://localhost:8080/geraetearten')
      .flush([{ nummer: 16, name: 'Geräteart', gruppe: 'Gruppe', kategorie: 'Kategorie' }]);

    const component = fixture.componentInstance;
    component.form.setValue({
      menge: 125.5,
      einheit: 2,
      zeitraum: new Date(2026, 8, 1),
      geraeteartnummer: 16,
      registrierungsnummer: '12345678',
      wahrheitserklaerung: true,
    });
    fixture.detectChanges();
    fixture.nativeElement.querySelector('form').dispatchEvent(
      new Event('submit', { bubbles: true, cancelable: true }),
    );

    const request = httpTestingController.expectOne('http://localhost:8080/mengenabgaben');
    expect(request.request.method).toBe('POST');
    expect(request.request.body).toEqual({
      menge: 125.5,
      einheit: 2,
      jahr: 2026,
      monat: 9,
      geraeteartnummer: 16,
      registrierungsnummer: 12345678,
      wahrheitserklaerung: true,
    });
    request.flush({
      id: 1,
      menge: 125.5,
      einheit: 2,
      jahr: 2026,
      monat: 9,
      geraeteartnummer: 16,
      registrierungsnummer: 12345678,
      wahrheitserklaerung: true,
    });

    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain(
      'Die Mengenabgabe wurde erfolgreich gespeichert.',
    );
  });

  it('should disable submission until the form is valid', () => {
    const fixture = TestBed.createComponent(Mengenabgabe);
    fixture.detectChanges();
    const submitButton = fixture.nativeElement.querySelector(
      'button[type="submit"]',
    ) as HTMLButtonElement;

    expect(submitButton.disabled).toBe(true);

    httpTestingController
      .expectOne('http://localhost:8080/geraetearten')
      .flush([{ nummer: 16, name: 'Geräteart', gruppe: 'Gruppe', kategorie: 'Kategorie' }]);
    fixture.componentInstance.form.setValue({
      menge: 125.5,
      einheit: 2,
      zeitraum: new Date(2026, 8, 1),
      geraeteartnummer: 16,
      registrierungsnummer: '12345678',
      wahrheitserklaerung: true,
    });
    fixture.detectChanges();

    expect(submitButton.disabled).toBe(false);

    fixture.componentInstance.form.controls.registrierungsnummer.setValue('123');
    fixture.detectChanges();

    expect(submitButton.disabled).toBe(true);
  });

  it('should strip non-digits and limit the registration number to eight digits', () => {
    const fixture = TestBed.createComponent(Mengenabgabe);
    fixture.detectChanges();
    httpTestingController
      .expectOne('http://localhost:8080/geraetearten')
      .flush([]);
    const input = fixture.nativeElement.querySelector(
      'input[formControlName="registrierungsnummer"]',
    ) as HTMLInputElement;
    input.value = '12ab3456789';
    input.dispatchEvent(new Event('input', { bubbles: true }));

    expect(input.value).toBe('12345678');
    expect(
      fixture.componentInstance.form.controls.registrierungsnummer.value,
    ).toBe('12345678');
  });

  it('should display the backend Problem Details title and detail on submission failure', () => {
    const fixture = TestBed.createComponent(Mengenabgabe);
    fixture.detectChanges();
    httpTestingController
      .expectOne('http://localhost:8080/geraetearten')
      .flush([{ nummer: 16, name: 'Geräteart', gruppe: 'Gruppe', kategorie: 'Kategorie' }]);

    fixture.componentInstance.form.setValue({
      menge: 125.5,
      einheit: 2,
      zeitraum: new Date(2026, 8, 1),
      geraeteartnummer: 16,
      registrierungsnummer: '12345678',
      wahrheitserklaerung: true,
    });
    fixture.detectChanges();
    fixture.nativeElement.querySelector('form').dispatchEvent(
      new Event('submit', { bubbles: true, cancelable: true }),
    );

    httpTestingController.expectOne('http://localhost:8080/mengenabgaben').flush(
      {
        detail:
          'Fehler bei der Übermittlung an den EAR-SOAP-Service: Maßeinheit "Stück" für diesen Mitteilungstyp nicht zulässig',
        instance: '/mengenabgaben',
        status: 502,
        title: 'EAR-Service nicht erreichbar',
      },
      { status: 502, statusText: 'Bad Gateway' },
    );
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('EAR-Service nicht erreichbar');
    expect(fixture.nativeElement.textContent).toContain(
      'Maßeinheit "Stück" für diesen Mitteilungstyp nicht zulässig',
    );
    expect(fixture.nativeElement.textContent).toContain('HTTP 502');
  });
});
