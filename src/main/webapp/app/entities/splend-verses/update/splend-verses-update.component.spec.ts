import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IValidationModel } from 'app/entities/validation-model/validation-model.model';
import { ValidationModelService } from 'app/entities/validation-model/service/validation-model.service';
import { SplendVersesService } from '../service/splend-verses.service';
import { ISplendVerses } from '../splend-verses.model';
import { SplendVersesFormService } from './splend-verses-form.service';

import { SplendVersesUpdateComponent } from './splend-verses-update.component';

describe('SplendVerses Management Update Component', () => {
  let comp: SplendVersesUpdateComponent;
  let fixture: ComponentFixture<SplendVersesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let splendVersesFormService: SplendVersesFormService;
  let splendVersesService: SplendVersesService;
  let validationModelService: ValidationModelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SplendVersesUpdateComponent],
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
      .overrideTemplate(SplendVersesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SplendVersesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    splendVersesFormService = TestBed.inject(SplendVersesFormService);
    splendVersesService = TestBed.inject(SplendVersesService);
    validationModelService = TestBed.inject(ValidationModelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ValidationModel query and add missing value', () => {
      const splendVerses: ISplendVerses = { id: 'CBA' };
      const validationMethod: IValidationModel = { id: '2a024d88-5cce-440f-be39-bc0e507f8042' };
      splendVerses.validationMethod = validationMethod;

      const validationModelCollection: IValidationModel[] = [{ id: '8d2ae42a-b322-458c-837c-0b73638ea9c6' }];
      jest.spyOn(validationModelService, 'query').mockReturnValue(of(new HttpResponse({ body: validationModelCollection })));
      const additionalValidationModels = [validationMethod];
      const expectedCollection: IValidationModel[] = [...additionalValidationModels, ...validationModelCollection];
      jest.spyOn(validationModelService, 'addValidationModelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ splendVerses });
      comp.ngOnInit();

      expect(validationModelService.query).toHaveBeenCalled();
      expect(validationModelService.addValidationModelToCollectionIfMissing).toHaveBeenCalledWith(
        validationModelCollection,
        ...additionalValidationModels.map(expect.objectContaining),
      );
      expect(comp.validationModelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const splendVerses: ISplendVerses = { id: 'CBA' };
      const validationMethod: IValidationModel = { id: '72d69933-b3aa-4b4a-bbfd-0ac442e1f34d' };
      splendVerses.validationMethod = validationMethod;

      activatedRoute.data = of({ splendVerses });
      comp.ngOnInit();

      expect(comp.validationModelsSharedCollection).toContain(validationMethod);
      expect(comp.splendVerses).toEqual(splendVerses);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISplendVerses>>();
      const splendVerses = { id: 'ABC' };
      jest.spyOn(splendVersesFormService, 'getSplendVerses').mockReturnValue(splendVerses);
      jest.spyOn(splendVersesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ splendVerses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: splendVerses }));
      saveSubject.complete();

      // THEN
      expect(splendVersesFormService.getSplendVerses).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(splendVersesService.update).toHaveBeenCalledWith(expect.objectContaining(splendVerses));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISplendVerses>>();
      const splendVerses = { id: 'ABC' };
      jest.spyOn(splendVersesFormService, 'getSplendVerses').mockReturnValue({ id: null });
      jest.spyOn(splendVersesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ splendVerses: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: splendVerses }));
      saveSubject.complete();

      // THEN
      expect(splendVersesFormService.getSplendVerses).toHaveBeenCalled();
      expect(splendVersesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISplendVerses>>();
      const splendVerses = { id: 'ABC' };
      jest.spyOn(splendVersesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ splendVerses });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(splendVersesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareValidationModel', () => {
      it('Should forward to validationModelService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(validationModelService, 'compareValidationModel');
        comp.compareValidationModel(entity, entity2);
        expect(validationModelService.compareValidationModel).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
