import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISurah } from '../surah.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../surah.test-samples';

import { SurahService } from './surah.service';

const requireRestSample: ISurah = {
  ...sampleWithRequiredData,
};

describe('Surah Service', () => {
  let service: SurahService;
  let httpMock: HttpTestingController;
  let expectedResult: ISurah | ISurah[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SurahService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Surah', () => {
      const surah = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(surah).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Surah', () => {
      const surah = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(surah).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Surah', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Surah', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Surah', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSurahToCollectionIfMissing', () => {
      it('should add a Surah to an empty array', () => {
        const surah: ISurah = sampleWithRequiredData;
        expectedResult = service.addSurahToCollectionIfMissing([], surah);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(surah);
      });

      it('should not add a Surah to an array that contains it', () => {
        const surah: ISurah = sampleWithRequiredData;
        const surahCollection: ISurah[] = [
          {
            ...surah,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSurahToCollectionIfMissing(surahCollection, surah);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Surah to an array that doesn't contain it", () => {
        const surah: ISurah = sampleWithRequiredData;
        const surahCollection: ISurah[] = [sampleWithPartialData];
        expectedResult = service.addSurahToCollectionIfMissing(surahCollection, surah);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(surah);
      });

      it('should add only unique Surah to an array', () => {
        const surahArray: ISurah[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const surahCollection: ISurah[] = [sampleWithRequiredData];
        expectedResult = service.addSurahToCollectionIfMissing(surahCollection, ...surahArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const surah: ISurah = sampleWithRequiredData;
        const surah2: ISurah = sampleWithPartialData;
        expectedResult = service.addSurahToCollectionIfMissing([], surah, surah2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(surah);
        expect(expectedResult).toContain(surah2);
      });

      it('should accept null and undefined values', () => {
        const surah: ISurah = sampleWithRequiredData;
        expectedResult = service.addSurahToCollectionIfMissing([], null, surah, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(surah);
      });

      it('should return initial array if no Surah is added', () => {
        const surahCollection: ISurah[] = [sampleWithRequiredData];
        expectedResult = service.addSurahToCollectionIfMissing(surahCollection, undefined, null);
        expect(expectedResult).toEqual(surahCollection);
      });
    });

    describe('compareSurah', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSurah(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareSurah(entity1, entity2);
        const compareResult2 = service.compareSurah(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareSurah(entity1, entity2);
        const compareResult2 = service.compareSurah(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareSurah(entity1, entity2);
        const compareResult2 = service.compareSurah(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
