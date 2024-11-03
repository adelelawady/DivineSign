import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IValidationModel } from 'app/entities/validation-model/validation-model.model';
import { ValidationModelService } from 'app/entities/validation-model/service/validation-model.service';
import { CategoryService } from '../service/category.service';
import { ICategory } from '../category.model';
import { CategoryFormService } from './category-form.service';

import { CategoryUpdateComponent } from './category-update.component';

describe('Category Management Update Component', () => {
  let comp: CategoryUpdateComponent;
  let fixture: ComponentFixture<CategoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let categoryFormService: CategoryFormService;
  let categoryService: CategoryService;
  let validationModelService: ValidationModelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CategoryUpdateComponent],
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
      .overrideTemplate(CategoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categoryFormService = TestBed.inject(CategoryFormService);
    categoryService = TestBed.inject(CategoryService);
    validationModelService = TestBed.inject(ValidationModelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ValidationModel query and add missing value', () => {
      const category: ICategory = { id: 'CBA' };
      const validationMethod: IValidationModel = { id: 'eb96f333-1170-4060-aafb-66499e0add0d' };
      category.validationMethod = validationMethod;

      const validationModelCollection: IValidationModel[] = [{ id: '77acbecb-7397-4898-aa17-c40d206dfaca' }];
      jest.spyOn(validationModelService, 'query').mockReturnValue(of(new HttpResponse({ body: validationModelCollection })));
      const additionalValidationModels = [validationMethod];
      const expectedCollection: IValidationModel[] = [...additionalValidationModels, ...validationModelCollection];
      jest.spyOn(validationModelService, 'addValidationModelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ category });
      comp.ngOnInit();

      expect(validationModelService.query).toHaveBeenCalled();
      expect(validationModelService.addValidationModelToCollectionIfMissing).toHaveBeenCalledWith(
        validationModelCollection,
        ...additionalValidationModels.map(expect.objectContaining),
      );
      expect(comp.validationModelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const category: ICategory = { id: 'CBA' };
      const validationMethod: IValidationModel = { id: '80e3bbc7-cc42-4519-a711-a9804364940b' };
      category.validationMethod = validationMethod;

      activatedRoute.data = of({ category });
      comp.ngOnInit();

      expect(comp.validationModelsSharedCollection).toContain(validationMethod);
      expect(comp.category).toEqual(category);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategory>>();
      const category = { id: 'ABC' };
      jest.spyOn(categoryFormService, 'getCategory').mockReturnValue(category);
      jest.spyOn(categoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ category });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: category }));
      saveSubject.complete();

      // THEN
      expect(categoryFormService.getCategory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(categoryService.update).toHaveBeenCalledWith(expect.objectContaining(category));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategory>>();
      const category = { id: 'ABC' };
      jest.spyOn(categoryFormService, 'getCategory').mockReturnValue({ id: null });
      jest.spyOn(categoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ category: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: category }));
      saveSubject.complete();

      // THEN
      expect(categoryFormService.getCategory).toHaveBeenCalled();
      expect(categoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICategory>>();
      const category = { id: 'ABC' };
      jest.spyOn(categoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ category });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categoryService.update).toHaveBeenCalled();
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
