import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISplend } from '../splend.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../splend.test-samples';

import { SplendService } from './splend.service';

const requireRestSample: ISplend = {
  ...sampleWithRequiredData,
};

describe('Splend Service', () => {
  let service: SplendService;
  let httpMock: HttpTestingController;
  let expectedResult: ISplend | ISplend[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SplendService);
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

    it('should create a Splend', () => {
      const splend = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(splend).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Splend', () => {
      const splend = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(splend).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Splend', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Splend', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Splend', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSplendToCollectionIfMissing', () => {
      it('should add a Splend to an empty array', () => {
        const splend: ISplend = sampleWithRequiredData;
        expectedResult = service.addSplendToCollectionIfMissing([], splend);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(splend);
      });

      it('should not add a Splend to an array that contains it', () => {
        const splend: ISplend = sampleWithRequiredData;
        const splendCollection: ISplend[] = [
          {
            ...splend,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSplendToCollectionIfMissing(splendCollection, splend);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Splend to an array that doesn't contain it", () => {
        const splend: ISplend = sampleWithRequiredData;
        const splendCollection: ISplend[] = [sampleWithPartialData];
        expectedResult = service.addSplendToCollectionIfMissing(splendCollection, splend);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(splend);
      });

      it('should add only unique Splend to an array', () => {
        const splendArray: ISplend[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const splendCollection: ISplend[] = [sampleWithRequiredData];
        expectedResult = service.addSplendToCollectionIfMissing(splendCollection, ...splendArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const splend: ISplend = sampleWithRequiredData;
        const splend2: ISplend = sampleWithPartialData;
        expectedResult = service.addSplendToCollectionIfMissing([], splend, splend2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(splend);
        expect(expectedResult).toContain(splend2);
      });

      it('should accept null and undefined values', () => {
        const splend: ISplend = sampleWithRequiredData;
        expectedResult = service.addSplendToCollectionIfMissing([], null, splend, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(splend);
      });

      it('should return initial array if no Splend is added', () => {
        const splendCollection: ISplend[] = [sampleWithRequiredData];
        expectedResult = service.addSplendToCollectionIfMissing(splendCollection, undefined, null);
        expect(expectedResult).toEqual(splendCollection);
      });
    });

    describe('compareSplend', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSplend(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareSplend(entity1, entity2);
        const compareResult2 = service.compareSplend(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareSplend(entity1, entity2);
        const compareResult2 = service.compareSplend(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareSplend(entity1, entity2);
        const compareResult2 = service.compareSplend(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
