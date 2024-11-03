import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../splend-verses.test-samples';

import { SplendVersesFormService } from './splend-verses-form.service';

describe('SplendVerses Form Service', () => {
  let service: SplendVersesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SplendVersesFormService);
  });

  describe('Service methods', () => {
    describe('createSplendVersesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSplendVersesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            word: expect.any(Object),
            number: expect.any(Object),
            condition: expect.any(Object),
            validationMethod: expect.any(Object),
          }),
        );
      });

      it('passing ISplendVerses should create a new form with FormGroup', () => {
        const formGroup = service.createSplendVersesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            word: expect.any(Object),
            number: expect.any(Object),
            condition: expect.any(Object),
            validationMethod: expect.any(Object),
          }),
        );
      });
    });

    describe('getSplendVerses', () => {
      it('should return NewSplendVerses for default SplendVerses initial value', () => {
        const formGroup = service.createSplendVersesFormGroup(sampleWithNewData);

        const splendVerses = service.getSplendVerses(formGroup) as any;

        expect(splendVerses).toMatchObject(sampleWithNewData);
      });

      it('should return NewSplendVerses for empty SplendVerses initial value', () => {
        const formGroup = service.createSplendVersesFormGroup();

        const splendVerses = service.getSplendVerses(formGroup) as any;

        expect(splendVerses).toMatchObject({});
      });

      it('should return ISplendVerses', () => {
        const formGroup = service.createSplendVersesFormGroup(sampleWithRequiredData);

        const splendVerses = service.getSplendVerses(formGroup) as any;

        expect(splendVerses).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISplendVerses should not enable id FormControl', () => {
        const formGroup = service.createSplendVersesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSplendVerses should disable id FormControl', () => {
        const formGroup = service.createSplendVersesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
