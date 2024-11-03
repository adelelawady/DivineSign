import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISurah } from 'app/entities/surah/surah.model';
import { SurahService } from 'app/entities/surah/service/surah.service';
import { ISplendVerses } from 'app/entities/splend-verses/splend-verses.model';
import { SplendVersesService } from 'app/entities/splend-verses/service/splend-verses.service';
import { IVerse } from '../verse.model';
import { VerseService } from '../service/verse.service';
import { VerseFormService } from './verse-form.service';

import { VerseUpdateComponent } from './verse-update.component';

describe('Verse Management Update Component', () => {
  let comp: VerseUpdateComponent;
  let fixture: ComponentFixture<VerseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let verseFormService: VerseFormService;
  let verseService: VerseService;
  let surahService: SurahService;
  let splendVersesService: SplendVersesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [VerseUpdateComponent],
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
      .overrideTemplate(VerseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VerseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    verseFormService = TestBed.inject(VerseFormService);
    verseService = TestBed.inject(VerseService);
    surahService = TestBed.inject(SurahService);
    splendVersesService = TestBed.inject(SplendVersesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Surah query and add missing value', () => {
      const verse: IVerse = { id: 'CBA' };
      const surah: ISurah = { id: 'a2263624-abe9-488f-8d74-a3851dfc1f90' };
      verse.surah = surah;

      const surahCollection: ISurah[] = [{ id: '877c9f85-e3d1-4cb4-8622-49307ec47375' }];
      jest.spyOn(surahService, 'query').mockReturnValue(of(new HttpResponse({ body: surahCollection })));
      const additionalSurahs = [surah];
      const expectedCollection: ISurah[] = [...additionalSurahs, ...surahCollection];
      jest.spyOn(surahService, 'addSurahToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ verse });
      comp.ngOnInit();

      expect(surahService.query).toHaveBeenCalled();
      expect(surahService.addSurahToCollectionIfMissing).toHaveBeenCalledWith(
        surahCollection,
        ...additionalSurahs.map(expect.objectContaining),
      );
      expect(comp.surahsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SplendVerses query and add missing value', () => {
      const verse: IVerse = { id: 'CBA' };
      const splendVerses: ISplendVerses = { id: '9c82d352-cb81-4a7f-91b7-916b3f302817' };
      verse.splendVerses = splendVerses;

      const splendVersesCollection: ISplendVerses[] = [{ id: 'dcd1dae2-2543-4c63-af53-26bebbc4ba26' }];
      jest.spyOn(splendVersesService, 'query').mockReturnValue(of(new HttpResponse({ body: splendVersesCollection })));
      const additionalSplendVerses = [splendVerses];
      const expectedCollection: ISplendVerses[] = [...additionalSplendVerses, ...splendVersesCollection];
      jest.spyOn(splendVersesService, 'addSplendVersesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ verse });
      comp.ngOnInit();

      expect(splendVersesService.query).toHaveBeenCalled();
      expect(splendVersesService.addSplendVersesToCollectionIfMissing).toHaveBeenCalledWith(
        splendVersesCollection,
        ...additionalSplendVerses.map(expect.objectContaining),
      );
      expect(comp.splendVersesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const verse: IVerse = { id: 'CBA' };
      const surah: ISurah = { id: '9e38a64e-a457-4401-9182-d23bf56e47d9' };
      verse.surah = surah;
      const splendVerses: ISplendVerses = { id: 'fccba9ee-347a-436b-9308-5f90365e2d97' };
      verse.splendVerses = splendVerses;

      activatedRoute.data = of({ verse });
      comp.ngOnInit();

      expect(comp.surahsSharedCollection).toContain(surah);
      expect(comp.splendVersesSharedCollection).toContain(splendVerses);
      expect(comp.verse).toEqual(verse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVerse>>();
      const verse = { id: 'ABC' };
      jest.spyOn(verseFormService, 'getVerse').mockReturnValue(verse);
      jest.spyOn(verseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ verse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: verse }));
      saveSubject.complete();

      // THEN
      expect(verseFormService.getVerse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(verseService.update).toHaveBeenCalledWith(expect.objectContaining(verse));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVerse>>();
      const verse = { id: 'ABC' };
      jest.spyOn(verseFormService, 'getVerse').mockReturnValue({ id: null });
      jest.spyOn(verseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ verse: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: verse }));
      saveSubject.complete();

      // THEN
      expect(verseFormService.getVerse).toHaveBeenCalled();
      expect(verseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVerse>>();
      const verse = { id: 'ABC' };
      jest.spyOn(verseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ verse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(verseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSurah', () => {
      it('Should forward to surahService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(surahService, 'compareSurah');
        comp.compareSurah(entity, entity2);
        expect(surahService.compareSurah).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSplendVerses', () => {
      it('Should forward to splendVersesService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(splendVersesService, 'compareSplendVerses');
        comp.compareSplendVerses(entity, entity2);
        expect(splendVersesService.compareSplendVerses).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
