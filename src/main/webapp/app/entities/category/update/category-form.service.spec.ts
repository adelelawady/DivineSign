import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../category.test-samples';

import { CategoryFormService } from './category-form.service';

describe('Category Form Service', () => {
  let service: CategoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CategoryFormService);
  });

  describe('Service methods', () => {
    describe('createCategoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCategoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            validationMethod: expect.any(Object),
          }),
        );
      });

      it('passing ICategory should create a new form with FormGroup', () => {
        const formGroup = service.createCategoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            validationMethod: expect.any(Object),
          }),
        );
      });
    });

    describe('getCategory', () => {
      it('should return NewCategory for default Category initial value', () => {
        const formGroup = service.createCategoryFormGroup(sampleWithNewData);

        const category = service.getCategory(formGroup) as any;

        expect(category).toMatchObject(sampleWithNewData);
      });

      it('should return NewCategory for empty Category initial value', () => {
        const formGroup = service.createCategoryFormGroup();

        const category = service.getCategory(formGroup) as any;

        expect(category).toMatchObject({});
      });

      it('should return ICategory', () => {
        const formGroup = service.createCategoryFormGroup(sampleWithRequiredData);

        const category = service.getCategory(formGroup) as any;

        expect(category).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICategory should not enable id FormControl', () => {
        const formGroup = service.createCategoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCategory should disable id FormControl', () => {
        const formGroup = service.createCategoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
