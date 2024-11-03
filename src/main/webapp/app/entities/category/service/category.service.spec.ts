import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICategory } from '../category.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../category.test-samples';

import { CategoryService } from './category.service';

const requireRestSample: ICategory = {
  ...sampleWithRequiredData,
};

describe('Category Service', () => {
  let service: CategoryService;
  let httpMock: HttpTestingController;
  let expectedResult: ICategory | ICategory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CategoryService);
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

    it('should create a Category', () => {
      const category = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(category).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Category', () => {
      const category = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(category).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Category', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Category', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Category', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCategoryToCollectionIfMissing', () => {
      it('should add a Category to an empty array', () => {
        const category: ICategory = sampleWithRequiredData;
        expectedResult = service.addCategoryToCollectionIfMissing([], category);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(category);
      });

      it('should not add a Category to an array that contains it', () => {
        const category: ICategory = sampleWithRequiredData;
        const categoryCollection: ICategory[] = [
          {
            ...category,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCategoryToCollectionIfMissing(categoryCollection, category);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Category to an array that doesn't contain it", () => {
        const category: ICategory = sampleWithRequiredData;
        const categoryCollection: ICategory[] = [sampleWithPartialData];
        expectedResult = service.addCategoryToCollectionIfMissing(categoryCollection, category);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(category);
      });

      it('should add only unique Category to an array', () => {
        const categoryArray: ICategory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const categoryCollection: ICategory[] = [sampleWithRequiredData];
        expectedResult = service.addCategoryToCollectionIfMissing(categoryCollection, ...categoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const category: ICategory = sampleWithRequiredData;
        const category2: ICategory = sampleWithPartialData;
        expectedResult = service.addCategoryToCollectionIfMissing([], category, category2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(category);
        expect(expectedResult).toContain(category2);
      });

      it('should accept null and undefined values', () => {
        const category: ICategory = sampleWithRequiredData;
        expectedResult = service.addCategoryToCollectionIfMissing([], null, category, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(category);
      });

      it('should return initial array if no Category is added', () => {
        const categoryCollection: ICategory[] = [sampleWithRequiredData];
        expectedResult = service.addCategoryToCollectionIfMissing(categoryCollection, undefined, null);
        expect(expectedResult).toEqual(categoryCollection);
      });
    });

    describe('compareCategory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCategory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareCategory(entity1, entity2);
        const compareResult2 = service.compareCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareCategory(entity1, entity2);
        const compareResult2 = service.compareCategory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareCategory(entity1, entity2);
        const compareResult2 = service.compareCategory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
