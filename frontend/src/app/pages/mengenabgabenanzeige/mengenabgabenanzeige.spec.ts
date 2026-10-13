import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { Mengenabgabenanzeige } from './mengenabgabenanzeige';

describe('Mengenabgabenanzeige', () => {
  let httpTestingController: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Mengenabgabenanzeige],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should request and display a sorted page of Mengenabgaben', () => {
    const fixture = TestBed.createComponent(Mengenabgabenanzeige);
    fixture.detectChanges();

    const request = httpTestingController.expectOne(
      (req) =>
        req.url === 'http://localhost:8080/mengenabgaben' &&
        req.params.get('page') === '0' &&
        req.params.get('size') === '10',
    );
    expect(request.request.method).toBe('GET');
    expect(request.request.params.getAll('sort')).toEqual([
      'jahr%2Cdesc',
      'monat%2Cdesc',
    ]);
    request.flush({
      content: [
        {
          id: 7,
          menge: 125.5,
          einheit: 2,
          jahr: 2026,
          monat: 9,
          geraeteartnummer: 16,
          registrierungsnummer: 12345678,
          wahrheitserklaerung: true,
        },
      ],
      page: 0,
      size: 10,
      totalElements: 1,
      totalPages: 1,
    });

    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('09/2026');
    expect(fixture.nativeElement.textContent).toContain('125.5');
    expect(fixture.nativeElement.textContent).toContain('12345678');
    expect(fixture.nativeElement.textContent).toContain('Einträge gesamt');
  });

  it('should reload the first page with the selected column sort', () => {
    const fixture = TestBed.createComponent(Mengenabgabenanzeige);
    fixture.detectChanges();
    httpTestingController
      .expectOne(
        (req) =>
          req.url === 'http://localhost:8080/mengenabgaben' &&
          req.params.get('page') === '0',
      )
      .flush({
        content: [],
        page: 0,
        size: 10,
        totalElements: 0,
        totalPages: 0,
      });
    fixture.detectChanges();

    const sortHeader = fixture.nativeElement.querySelector(
      '[mat-sort-header="menge"]',
    ) as HTMLElement;
    sortHeader.click();
    fixture.detectChanges();

    const request = httpTestingController.expectOne(
      (req) =>
        req.url === 'http://localhost:8080/mengenabgaben' &&
        req.params.get('page') === '0',
    );
    expect(request.request.params.getAll('sort')).toEqual(['menge%2Casc']);
    request.flush({
      content: [],
      page: 0,
      size: 10,
      totalElements: 0,
      totalPages: 0,
    });
  });

  it('should request the next page when the paginator advances', () => {
    const fixture = TestBed.createComponent(Mengenabgabenanzeige);
    fixture.detectChanges();

    httpTestingController
      .expectOne((req) => req.url === 'http://localhost:8080/mengenabgaben')
      .flush({
        content: [],
        page: 0,
        size: 10,
        totalElements: 21,
        totalPages: 3,
      });
    fixture.detectChanges();

    const nextPageButton = fixture.nativeElement.querySelector(
      'button[aria-label="Next page"]',
    ) as HTMLButtonElement;
    nextPageButton.click();

    const request = httpTestingController.expectOne(
      (req) =>
        req.url === 'http://localhost:8080/mengenabgaben' &&
        req.params.get('page') === '1' &&
        req.params.get('size') === '10',
    );
    request.flush({
      content: [],
      page: 1,
      size: 10,
      totalElements: 21,
      totalPages: 3,
    });
  });

  it('should show the backend problem detail when loading fails', () => {
    const fixture = TestBed.createComponent(Mengenabgabenanzeige);
    fixture.detectChanges();

    httpTestingController.expectOne(
      (req) => req.url === 'http://localhost:8080/mengenabgaben',
    ).flush(
      {
        detail: 'Fehler beim Laden der Abgaben',
        instance: '/mengenabgaben',
        status: 502,
        title: 'Backend nicht erreichbar',
      },
      { status: 502, statusText: 'Bad Gateway' },
    );
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('Backend nicht erreichbar');
    expect(fixture.nativeElement.textContent).toContain('Fehler beim Laden der Abgaben');
    expect(fixture.nativeElement.textContent).toContain('HTTP 502');
  });
});
