import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SplendVersesDetailComponent } from './splend-verses-detail.component';

describe('SplendVerses Management Detail Component', () => {
  let comp: SplendVersesDetailComponent;
  let fixture: ComponentFixture<SplendVersesDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SplendVersesDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./splend-verses-detail.component').then(m => m.SplendVersesDetailComponent),
              resolve: { splendVerses: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SplendVersesDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SplendVersesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load splendVerses on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SplendVersesDetailComponent);

      // THEN
      expect(instance.splendVerses()).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
