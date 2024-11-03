import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IValidationModel } from '../validation-model.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../validation-model.test-samples';

import { ValidationModelService } from './validation-model.service';

const requireRestSample: IValidationModel = {
  ...sampleWithRequiredData,
};

describe('ValidationModel Service', () => {
  let service: ValidationModelService;
  let httpMock: HttpTestingController;
  let expectedResult: IValidationModel | IValidationModel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ValidationModelService);
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

    it('should create a ValidationModel', () => {
      const validationModel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(validationModel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ValidationModel', () => {
      const validationModel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(validationModel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ValidationModel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ValidationModel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ValidationModel', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addValidationModelToCollectionIfMissing', () => {
      it('should add a ValidationModel to an empty array', () => {
        const validationModel: IValidationModel = sampleWithRequiredData;
        expectedResult = service.addValidationModelToCollectionIfMissing([], validationModel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(validationModel);
      });

      it('should not add a ValidationModel to an array that contains it', () => {
        const validationModel: IValidationModel = sampleWithRequiredData;
        const validationModelCollection: IValidationModel[] = [
          {
            ...validationModel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addValidationModelToCollectionIfMissing(validationModelCollection, validationModel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ValidationModel to an array that doesn't contain it", () => {
        const validationModel: IValidationModel = sampleWithRequiredData;
        const validationModelCollection: IValidationModel[] = [sampleWithPartialData];
        expectedResult = service.addValidationModelToCollectionIfMissing(validationModelCollection, validationModel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(validationModel);
      });

      it('should add only unique ValidationModel to an array', () => {
        const validationModelArray: IValidationModel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const validationModelCollection: IValidationModel[] = [sampleWithRequiredData];
        expectedResult = service.addValidationModelToCollectionIfMissing(validationModelCollection, ...validationModelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const validationModel: IValidationModel = sampleWithRequiredData;
        const validationModel2: IValidationModel = sampleWithPartialData;
        expectedResult = service.addValidationModelToCollectionIfMissing([], validationModel, validationModel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(validationModel);
        expect(expectedResult).toContain(validationModel2);
      });

      it('should accept null and undefined values', () => {
        const validationModel: IValidationModel = sampleWithRequiredData;
        expectedResult = service.addValidationModelToCollectionIfMissing([], null, validationModel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(validationModel);
      });

      it('should return initial array if no ValidationModel is added', () => {
        const validationModelCollection: IValidationModel[] = [sampleWithRequiredData];
        expectedResult = service.addValidationModelToCollectionIfMissing(validationModelCollection, undefined, null);
        expect(expectedResult).toEqual(validationModelCollection);
      });
    });

    describe('compareValidationModel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareValidationModel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareValidationModel(entity1, entity2);
        const compareResult2 = service.compareValidationModel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareValidationModel(entity1, entity2);
        const compareResult2 = service.compareValidationModel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareValidationModel(entity1, entity2);
        const compareResult2 = service.compareValidationModel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
