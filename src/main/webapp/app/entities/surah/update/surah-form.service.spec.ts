import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../surah.test-samples';

import { SurahFormService } from './surah-form.service';

describe('Surah Form Service', () => {
  let service: SurahFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SurahFormService);
  });

  describe('Service methods', () => {
    describe('createSurahFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSurahFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            transliteration: expect.any(Object),
            type: expect.any(Object),
            totalVerses: expect.any(Object),
          }),
        );
      });

      it('passing ISurah should create a new form with FormGroup', () => {
        const formGroup = service.createSurahFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            transliteration: expect.any(Object),
            type: expect.any(Object),
            totalVerses: expect.any(Object),
          }),
        );
      });
    });

    describe('getSurah', () => {
      it('should return NewSurah for default Surah initial value', () => {
        const formGroup = service.createSurahFormGroup(sampleWithNewData);

        const surah = service.getSurah(formGroup) as any;

        expect(surah).toMatchObject(sampleWithNewData);
      });

      it('should return NewSurah for empty Surah initial value', () => {
        const formGroup = service.createSurahFormGroup();

        const surah = service.getSurah(formGroup) as any;

        expect(surah).toMatchObject({});
      });

      it('should return ISurah', () => {
        const formGroup = service.createSurahFormGroup(sampleWithRequiredData);

        const surah = service.getSurah(formGroup) as any;

        expect(surah).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISurah should not enable id FormControl', () => {
        const formGroup = service.createSurahFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSurah should disable id FormControl', () => {
        const formGroup = service.createSurahFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
