import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { App } from './app';
import { routes } from './app.routes';
import { Mengenabgabe } from './pages/mengenabgabe/mengenabgabe';
import { Mengenabgabenanzeige } from './pages/mengenabgabenanzeige/mengenabgabenanzeige';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [provideRouter(routes)],
    })
      .compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should provide only the two requested routes', () => {
    expect(routes.map(({ path }) => path)).toEqual([
      'mengenabgabe',
      'mengenabgabenanzeige',
    ]);
  });

  it('should navigate to the Mengenabgabe route', async () => {
    const harness = await RouterTestingHarness.create();

    await harness.navigateByUrl('/mengenabgabe', Mengenabgabe);

    expect(harness.routeNativeElement?.querySelector('h1')?.textContent)
      .toBe('Mengenabgabe');
  });

  it('should navigate to the Mengenabgabenanzeige route', async () => {
    const harness = await RouterTestingHarness.create();

    await harness.navigateByUrl('/mengenabgabenanzeige', Mengenabgabenanzeige);

    expect(harness.routeNativeElement?.querySelector('h1')?.textContent)
      .toBe('Mengenabgabenanzeige');
  });
});
