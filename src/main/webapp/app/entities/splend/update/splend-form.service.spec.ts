import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../splend.test-samples';

import { SplendFormService } from './splend-form.service';

describe('Splend Form Service', () => {
  let service: SplendFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SplendFormService);
  });

  describe('Service methods', () => {
    describe('createSplendFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSplendFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            likes: expect.any(Object),
            dislikes: expect.any(Object),
            verified: expect.any(Object),
            category: expect.any(Object),
            verses: expect.any(Object),
            likedUsers: expect.any(Object),
            dislikedSplends: expect.any(Object),
          }),
        );
      });

      it('passing ISplend should create a new form with FormGroup', () => {
        const formGroup = service.createSplendFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            content: expect.any(Object),
            likes: expect.any(Object),
            dislikes: expect.any(Object),
            verified: expect.any(Object),
            category: expect.any(Object),
            verses: expect.any(Object),
            likedUsers: expect.any(Object),
            dislikedSplends: expect.any(Object),
          }),
        );
      });
    });

    describe('getSplend', () => {
      it('should return NewSplend for default Splend initial value', () => {
        const formGroup = service.createSplendFormGroup(sampleWithNewData);

        const splend = service.getSplend(formGroup) as any;

        expect(splend).toMatchObject(sampleWithNewData);
      });

      it('should return NewSplend for empty Splend initial value', () => {
        const formGroup = service.createSplendFormGroup();

        const splend = service.getSplend(formGroup) as any;

        expect(splend).toMatchObject({});
      });

      it('should return ISplend', () => {
        const formGroup = service.createSplendFormGroup(sampleWithRequiredData);

        const splend = service.getSplend(formGroup) as any;

        expect(splend).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISplend should not enable id FormControl', () => {
        const formGroup = service.createSplendFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSplend should disable id FormControl', () => {
        const formGroup = service.createSplendFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
