import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SurahService } from '../service/surah.service';
import { ISurah } from '../surah.model';
import { SurahFormService } from './surah-form.service';

import { SurahUpdateComponent } from './surah-update.component';

describe('Surah Management Update Component', () => {
  let comp: SurahUpdateComponent;
  let fixture: ComponentFixture<SurahUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let surahFormService: SurahFormService;
  let surahService: SurahService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SurahUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SurahUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SurahUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    surahFormService = TestBed.inject(SurahFormService);
    surahService = TestBed.inject(SurahService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const surah: ISurah = { id: 'CBA' };

      activatedRoute.data = of({ surah });
      comp.ngOnInit();

      expect(comp.surah).toEqual(surah);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISurah>>();
      const surah = { id: 'ABC' };
      jest.spyOn(surahFormService, 'getSurah').mockReturnValue(surah);
      jest.spyOn(surahService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surah });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: surah }));
      saveSubject.complete();

      // THEN
      expect(surahFormService.getSurah).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(surahService.update).toHaveBeenCalledWith(expect.objectContaining(surah));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISurah>>();
      const surah = { id: 'ABC' };
      jest.spyOn(surahFormService, 'getSurah').mockReturnValue({ id: null });
      jest.spyOn(surahService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surah: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: surah }));
      saveSubject.complete();

      // THEN
      expect(surahFormService.getSurah).toHaveBeenCalled();
      expect(surahService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISurah>>();
      const surah = { id: 'ABC' };
      jest.spyOn(surahService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ surah });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(surahService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
