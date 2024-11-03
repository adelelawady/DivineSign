import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IVerse } from '../verse.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../verse.test-samples';

import { VerseService } from './verse.service';

const requireRestSample: IVerse = {
  ...sampleWithRequiredData,
};

describe('Verse Service', () => {
  let service: VerseService;
  let httpMock: HttpTestingController;
  let expectedResult: IVerse | IVerse[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(VerseService);
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

    it('should create a Verse', () => {
      const verse = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(verse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Verse', () => {
      const verse = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(verse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Verse', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Verse', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Verse', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVerseToCollectionIfMissing', () => {
      it('should add a Verse to an empty array', () => {
        const verse: IVerse = sampleWithRequiredData;
        expectedResult = service.addVerseToCollectionIfMissing([], verse);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(verse);
      });

      it('should not add a Verse to an array that contains it', () => {
        const verse: IVerse = sampleWithRequiredData;
        const verseCollection: IVerse[] = [
          {
            ...verse,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVerseToCollectionIfMissing(verseCollection, verse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Verse to an array that doesn't contain it", () => {
        const verse: IVerse = sampleWithRequiredData;
        const verseCollection: IVerse[] = [sampleWithPartialData];
        expectedResult = service.addVerseToCollectionIfMissing(verseCollection, verse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(verse);
      });

      it('should add only unique Verse to an array', () => {
        const verseArray: IVerse[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const verseCollection: IVerse[] = [sampleWithRequiredData];
        expectedResult = service.addVerseToCollectionIfMissing(verseCollection, ...verseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const verse: IVerse = sampleWithRequiredData;
        const verse2: IVerse = sampleWithPartialData;
        expectedResult = service.addVerseToCollectionIfMissing([], verse, verse2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(verse);
        expect(expectedResult).toContain(verse2);
      });

      it('should accept null and undefined values', () => {
        const verse: IVerse = sampleWithRequiredData;
        expectedResult = service.addVerseToCollectionIfMissing([], null, verse, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(verse);
      });

      it('should return initial array if no Verse is added', () => {
        const verseCollection: IVerse[] = [sampleWithRequiredData];
        expectedResult = service.addVerseToCollectionIfMissing(verseCollection, undefined, null);
        expect(expectedResult).toEqual(verseCollection);
      });
    });

    describe('compareVerse', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVerse(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareVerse(entity1, entity2);
        const compareResult2 = service.compareVerse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareVerse(entity1, entity2);
        const compareResult2 = service.compareVerse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareVerse(entity1, entity2);
        const compareResult2 = service.compareVerse(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
