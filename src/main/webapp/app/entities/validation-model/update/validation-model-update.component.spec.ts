import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ValidationModelService } from '../service/validation-model.service';
import { IValidationModel } from '../validation-model.model';
import { ValidationModelFormService } from './validation-model-form.service';

import { ValidationModelUpdateComponent } from './validation-model-update.component';

describe('ValidationModel Management Update Component', () => {
  let comp: ValidationModelUpdateComponent;
  let fixture: ComponentFixture<ValidationModelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let validationModelFormService: ValidationModelFormService;
  let validationModelService: ValidationModelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ValidationModelUpdateComponent],
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
      .overrideTemplate(ValidationModelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ValidationModelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    validationModelFormService = TestBed.inject(ValidationModelFormService);
    validationModelService = TestBed.inject(ValidationModelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const validationModel: IValidationModel = { id: 'CBA' };

      activatedRoute.data = of({ validationModel });
      comp.ngOnInit();

      expect(comp.validationModel).toEqual(validationModel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IValidationModel>>();
      const validationModel = { id: 'ABC' };
      jest.spyOn(validationModelFormService, 'getValidationModel').mockReturnValue(validationModel);
      jest.spyOn(validationModelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ validationModel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: validationModel }));
      saveSubject.complete();

      // THEN
      expect(validationModelFormService.getValidationModel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(validationModelService.update).toHaveBeenCalledWith(expect.objectContaining(validationModel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IValidationModel>>();
      const validationModel = { id: 'ABC' };
      jest.spyOn(validationModelFormService, 'getValidationModel').mockReturnValue({ id: null });
      jest.spyOn(validationModelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ validationModel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: validationModel }));
      saveSubject.complete();

      // THEN
      expect(validationModelFormService.getValidationModel).toHaveBeenCalled();
      expect(validationModelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IValidationModel>>();
      const validationModel = { id: 'ABC' };
      jest.spyOn(validationModelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ validationModel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(validationModelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
