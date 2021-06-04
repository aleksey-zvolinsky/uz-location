import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMileageRequest, MileageRequest } from '../mileage-request.model';

import { MileageRequestService } from './mileage-request.service';

describe('Service Tests', () => {
  describe('MileageRequest Service', () => {
    let service: MileageRequestService;
    let httpMock: HttpTestingController;
    let elemDefault: IMileageRequest;
    let expectedResult: IMileageRequest | IMileageRequest[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MileageRequestService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        requestDatetime: currentDate,
        tankNumbers: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            requestDatetime: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a MileageRequest', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            requestDatetime: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            requestDatetime: currentDate,
          },
          returnedFromService
        );

        service.create(new MileageRequest()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MileageRequest', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            requestDatetime: currentDate.format(DATE_FORMAT),
            tankNumbers: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            requestDatetime: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MileageRequest', () => {
        const patchObject = Object.assign(
          {
            requestDatetime: currentDate.format(DATE_FORMAT),
          },
          new MileageRequest()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            requestDatetime: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MileageRequest', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            requestDatetime: currentDate.format(DATE_FORMAT),
            tankNumbers: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            requestDatetime: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MileageRequest', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMileageRequestToCollectionIfMissing', () => {
        it('should add a MileageRequest to an empty array', () => {
          const mileageRequest: IMileageRequest = { id: 123 };
          expectedResult = service.addMileageRequestToCollectionIfMissing([], mileageRequest);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mileageRequest);
        });

        it('should not add a MileageRequest to an array that contains it', () => {
          const mileageRequest: IMileageRequest = { id: 123 };
          const mileageRequestCollection: IMileageRequest[] = [
            {
              ...mileageRequest,
            },
            { id: 456 },
          ];
          expectedResult = service.addMileageRequestToCollectionIfMissing(mileageRequestCollection, mileageRequest);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MileageRequest to an array that doesn't contain it", () => {
          const mileageRequest: IMileageRequest = { id: 123 };
          const mileageRequestCollection: IMileageRequest[] = [{ id: 456 }];
          expectedResult = service.addMileageRequestToCollectionIfMissing(mileageRequestCollection, mileageRequest);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mileageRequest);
        });

        it('should add only unique MileageRequest to an array', () => {
          const mileageRequestArray: IMileageRequest[] = [{ id: 123 }, { id: 456 }, { id: 23581 }];
          const mileageRequestCollection: IMileageRequest[] = [{ id: 123 }];
          expectedResult = service.addMileageRequestToCollectionIfMissing(mileageRequestCollection, ...mileageRequestArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mileageRequest: IMileageRequest = { id: 123 };
          const mileageRequest2: IMileageRequest = { id: 456 };
          expectedResult = service.addMileageRequestToCollectionIfMissing([], mileageRequest, mileageRequest2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mileageRequest);
          expect(expectedResult).toContain(mileageRequest2);
        });

        it('should accept null and undefined values', () => {
          const mileageRequest: IMileageRequest = { id: 123 };
          expectedResult = service.addMileageRequestToCollectionIfMissing([], null, mileageRequest, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mileageRequest);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
