import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../verse.test-samples';

import { VerseFormService } from './verse-form.service';

describe('Verse Form Service', () => {
  let service: VerseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VerseFormService);
  });

  describe('Service methods', () => {
    describe('createVerseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVerseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            verse: expect.any(Object),
            diacriticVerse: expect.any(Object),
            surah: expect.any(Object),
            splendVerses: expect.any(Object),
          }),
        );
      });

      it('passing IVerse should create a new form with FormGroup', () => {
        const formGroup = service.createVerseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            verse: expect.any(Object),
            diacriticVerse: expect.any(Object),
            surah: expect.any(Object),
            splendVerses: expect.any(Object),
          }),
        );
      });
    });

    describe('getVerse', () => {
      it('should return NewVerse for default Verse initial value', () => {
        const formGroup = service.createVerseFormGroup(sampleWithNewData);

        const verse = service.getVerse(formGroup) as any;

        expect(verse).toMatchObject(sampleWithNewData);
      });

      it('should return NewVerse for empty Verse initial value', () => {
        const formGroup = service.createVerseFormGroup();

        const verse = service.getVerse(formGroup) as any;

        expect(verse).toMatchObject({});
      });

      it('should return IVerse', () => {
        const formGroup = service.createVerseFormGroup(sampleWithRequiredData);

        const verse = service.getVerse(formGroup) as any;

        expect(verse).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVerse should not enable id FormControl', () => {
        const formGroup = service.createVerseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVerse should disable id FormControl', () => {
        const formGroup = service.createVerseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
