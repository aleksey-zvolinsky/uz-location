import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ILocationRequest, LocationRequest } from '../location-request.model';

import { LocationRequestService } from './location-request.service';

describe('Service Tests', () => {
  describe('LocationRequest Service', () => {
    let service: LocationRequestService;
    let httpMock: HttpTestingController;
    let elemDefault: ILocationRequest;
    let expectedResult: ILocationRequest | ILocationRequest[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LocationRequestService);
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

      it('should create a LocationRequest', () => {
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

        service.create(new LocationRequest()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LocationRequest', () => {
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

      it('should partial update a LocationRequest', () => {
        const patchObject = Object.assign(
          {
            requestDatetime: currentDate.format(DATE_FORMAT),
          },
          new LocationRequest()
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

      it('should return a list of LocationRequest', () => {
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

      it('should delete a LocationRequest', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLocationRequestToCollectionIfMissing', () => {
        it('should add a LocationRequest to an empty array', () => {
          const locationRequest: ILocationRequest = { id: 123 };
          expectedResult = service.addLocationRequestToCollectionIfMissing([], locationRequest);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(locationRequest);
        });

        it('should not add a LocationRequest to an array that contains it', () => {
          const locationRequest: ILocationRequest = { id: 123 };
          const locationRequestCollection: ILocationRequest[] = [
            {
              ...locationRequest,
            },
            { id: 456 },
          ];
          expectedResult = service.addLocationRequestToCollectionIfMissing(locationRequestCollection, locationRequest);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LocationRequest to an array that doesn't contain it", () => {
          const locationRequest: ILocationRequest = { id: 123 };
          const locationRequestCollection: ILocationRequest[] = [{ id: 456 }];
          expectedResult = service.addLocationRequestToCollectionIfMissing(locationRequestCollection, locationRequest);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(locationRequest);
        });

        it('should add only unique LocationRequest to an array', () => {
          const locationRequestArray: ILocationRequest[] = [{ id: 123 }, { id: 456 }, { id: 83396 }];
          const locationRequestCollection: ILocationRequest[] = [{ id: 123 }];
          expectedResult = service.addLocationRequestToCollectionIfMissing(locationRequestCollection, ...locationRequestArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const locationRequest: ILocationRequest = { id: 123 };
          const locationRequest2: ILocationRequest = { id: 456 };
          expectedResult = service.addLocationRequestToCollectionIfMissing([], locationRequest, locationRequest2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(locationRequest);
          expect(expectedResult).toContain(locationRequest2);
        });

        it('should accept null and undefined values', () => {
          const locationRequest: ILocationRequest = { id: 123 };
          expectedResult = service.addLocationRequestToCollectionIfMissing([], null, locationRequest, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(locationRequest);
        });

        it('should return initial array if no LocationRequest is added', () => {
          const locationRequestCollection: ILocationRequest[] = [{ id: 123 }];
          expectedResult = service.addLocationRequestToCollectionIfMissing(locationRequestCollection, undefined, null);
          expect(expectedResult).toEqual(locationRequestCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
