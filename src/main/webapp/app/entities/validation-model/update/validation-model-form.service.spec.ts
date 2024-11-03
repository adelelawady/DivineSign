import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../validation-model.test-samples';

import { ValidationModelFormService } from './validation-model-form.service';

describe('ValidationModel Form Service', () => {
  let service: ValidationModelFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ValidationModelFormService);
  });

  describe('Service methods', () => {
    describe('createValidationModelFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createValidationModelFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
          }),
        );
      });

      it('passing IValidationModel should create a new form with FormGroup', () => {
        const formGroup = service.createValidationModelFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
          }),
        );
      });
    });

    describe('getValidationModel', () => {
      it('should return NewValidationModel for default ValidationModel initial value', () => {
        const formGroup = service.createValidationModelFormGroup(sampleWithNewData);

        const validationModel = service.getValidationModel(formGroup) as any;

        expect(validationModel).toMatchObject(sampleWithNewData);
      });

      it('should return NewValidationModel for empty ValidationModel initial value', () => {
        const formGroup = service.createValidationModelFormGroup();

        const validationModel = service.getValidationModel(formGroup) as any;

        expect(validationModel).toMatchObject({});
      });

      it('should return IValidationModel', () => {
        const formGroup = service.createValidationModelFormGroup(sampleWithRequiredData);

        const validationModel = service.getValidationModel(formGroup) as any;

        expect(validationModel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IValidationModel should not enable id FormControl', () => {
        const formGroup = service.createValidationModelFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewValidationModel should disable id FormControl', () => {
        const formGroup = service.createValidationModelFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
