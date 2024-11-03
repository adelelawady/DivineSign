import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IRequest } from '../request.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../request.test-samples';

import { RequestService } from './request.service';

const requireRestSample: IRequest = {
  ...sampleWithRequiredData,
};

describe('Request Service', () => {
  let service: RequestService;
  let httpMock: HttpTestingController;
  let expectedResult: IRequest | IRequest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(RequestService);
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

    it('should create a Request', () => {
      const request = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(request).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Request', () => {
      const request = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(request).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Request', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Request', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Request', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRequestToCollectionIfMissing', () => {
      it('should add a Request to an empty array', () => {
        const request: IRequest = sampleWithRequiredData;
        expectedResult = service.addRequestToCollectionIfMissing([], request);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(request);
      });

      it('should not add a Request to an array that contains it', () => {
        const request: IRequest = sampleWithRequiredData;
        const requestCollection: IRequest[] = [
          {
            ...request,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRequestToCollectionIfMissing(requestCollection, request);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Request to an array that doesn't contain it", () => {
        const request: IRequest = sampleWithRequiredData;
        const requestCollection: IRequest[] = [sampleWithPartialData];
        expectedResult = service.addRequestToCollectionIfMissing(requestCollection, request);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(request);
      });

      it('should add only unique Request to an array', () => {
        const requestArray: IRequest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const requestCollection: IRequest[] = [sampleWithRequiredData];
        expectedResult = service.addRequestToCollectionIfMissing(requestCollection, ...requestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const request: IRequest = sampleWithRequiredData;
        const request2: IRequest = sampleWithPartialData;
        expectedResult = service.addRequestToCollectionIfMissing([], request, request2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(request);
        expect(expectedResult).toContain(request2);
      });

      it('should accept null and undefined values', () => {
        const request: IRequest = sampleWithRequiredData;
        expectedResult = service.addRequestToCollectionIfMissing([], null, request, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(request);
      });

      it('should return initial array if no Request is added', () => {
        const requestCollection: IRequest[] = [sampleWithRequiredData];
        expectedResult = service.addRequestToCollectionIfMissing(requestCollection, undefined, null);
        expect(expectedResult).toEqual(requestCollection);
      });
    });

    describe('compareRequest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRequest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareRequest(entity1, entity2);
        const compareResult2 = service.compareRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareRequest(entity1, entity2);
        const compareResult2 = service.compareRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareRequest(entity1, entity2);
        const compareResult2 = service.compareRequest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
