import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISplendVerses } from '../splend-verses.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../splend-verses.test-samples';

import { SplendVersesService } from './splend-verses.service';

const requireRestSample: ISplendVerses = {
  ...sampleWithRequiredData,
};

describe('SplendVerses Service', () => {
  let service: SplendVersesService;
  let httpMock: HttpTestingController;
  let expectedResult: ISplendVerses | ISplendVerses[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SplendVersesService);
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

    it('should create a SplendVerses', () => {
      const splendVerses = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(splendVerses).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SplendVerses', () => {
      const splendVerses = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(splendVerses).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SplendVerses', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SplendVerses', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SplendVerses', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSplendVersesToCollectionIfMissing', () => {
      it('should add a SplendVerses to an empty array', () => {
        const splendVerses: ISplendVerses = sampleWithRequiredData;
        expectedResult = service.addSplendVersesToCollectionIfMissing([], splendVerses);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(splendVerses);
      });

      it('should not add a SplendVerses to an array that contains it', () => {
        const splendVerses: ISplendVerses = sampleWithRequiredData;
        const splendVersesCollection: ISplendVerses[] = [
          {
            ...splendVerses,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSplendVersesToCollectionIfMissing(splendVersesCollection, splendVerses);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SplendVerses to an array that doesn't contain it", () => {
        const splendVerses: ISplendVerses = sampleWithRequiredData;
        const splendVersesCollection: ISplendVerses[] = [sampleWithPartialData];
        expectedResult = service.addSplendVersesToCollectionIfMissing(splendVersesCollection, splendVerses);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(splendVerses);
      });

      it('should add only unique SplendVerses to an array', () => {
        const splendVersesArray: ISplendVerses[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const splendVersesCollection: ISplendVerses[] = [sampleWithRequiredData];
        expectedResult = service.addSplendVersesToCollectionIfMissing(splendVersesCollection, ...splendVersesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const splendVerses: ISplendVerses = sampleWithRequiredData;
        const splendVerses2: ISplendVerses = sampleWithPartialData;
        expectedResult = service.addSplendVersesToCollectionIfMissing([], splendVerses, splendVerses2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(splendVerses);
        expect(expectedResult).toContain(splendVerses2);
      });

      it('should accept null and undefined values', () => {
        const splendVerses: ISplendVerses = sampleWithRequiredData;
        expectedResult = service.addSplendVersesToCollectionIfMissing([], null, splendVerses, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(splendVerses);
      });

      it('should return initial array if no SplendVerses is added', () => {
        const splendVersesCollection: ISplendVerses[] = [sampleWithRequiredData];
        expectedResult = service.addSplendVersesToCollectionIfMissing(splendVersesCollection, undefined, null);
        expect(expectedResult).toEqual(splendVersesCollection);
      });
    });

    describe('compareSplendVerses', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSplendVerses(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareSplendVerses(entity1, entity2);
        const compareResult2 = service.compareSplendVerses(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareSplendVerses(entity1, entity2);
        const compareResult2 = service.compareSplendVerses(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareSplendVerses(entity1, entity2);
        const compareResult2 = service.compareSplendVerses(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
