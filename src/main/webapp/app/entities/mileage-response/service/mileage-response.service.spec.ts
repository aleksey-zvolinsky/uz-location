import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMileageResponse, MileageResponse } from '../mileage-response.model';

import { MileageResponseService } from './mileage-response.service';

describe('Service Tests', () => {
  describe('MileageResponse Service', () => {
    let service: MileageResponseService;
    let httpMock: HttpTestingController;
    let elemDefault: IMileageResponse;
    let expectedResult: IMileageResponse | IMileageResponse[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MileageResponseService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        responseDatetime: currentDate,
        tankNumber: 'AAAAAAA',
        mileageCurrent: 'AAAAAAA',
        mileageDatetime: 'AAAAAAA',
        mileageRemain: 'AAAAAAA',
        mileageUpdateDatetime: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            responseDatetime: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a MileageResponse', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            responseDatetime: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            responseDatetime: currentDate,
          },
          returnedFromService
        );

        service.create(new MileageResponse()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MileageResponse', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            responseDatetime: currentDate.format(DATE_FORMAT),
            tankNumber: 'BBBBBB',
            mileageCurrent: 'BBBBBB',
            mileageDatetime: 'BBBBBB',
            mileageRemain: 'BBBBBB',
            mileageUpdateDatetime: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            responseDatetime: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MileageResponse', () => {
        const patchObject = Object.assign(
          {
            mileageCurrent: 'BBBBBB',
            mileageUpdateDatetime: 'BBBBBB',
          },
          new MileageResponse()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            responseDatetime: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MileageResponse', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            responseDatetime: currentDate.format(DATE_FORMAT),
            tankNumber: 'BBBBBB',
            mileageCurrent: 'BBBBBB',
            mileageDatetime: 'BBBBBB',
            mileageRemain: 'BBBBBB',
            mileageUpdateDatetime: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            responseDatetime: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a MileageResponse', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMileageResponseToCollectionIfMissing', () => {
        it('should add a MileageResponse to an empty array', () => {
          const mileageResponse: IMileageResponse = { id: 123 };
          expectedResult = service.addMileageResponseToCollectionIfMissing([], mileageResponse);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mileageResponse);
        });

        it('should not add a MileageResponse to an array that contains it', () => {
          const mileageResponse: IMileageResponse = { id: 123 };
          const mileageResponseCollection: IMileageResponse[] = [
            {
              ...mileageResponse,
            },
            { id: 456 },
          ];
          expectedResult = service.addMileageResponseToCollectionIfMissing(mileageResponseCollection, mileageResponse);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MileageResponse to an array that doesn't contain it", () => {
          const mileageResponse: IMileageResponse = { id: 123 };
          const mileageResponseCollection: IMileageResponse[] = [{ id: 456 }];
          expectedResult = service.addMileageResponseToCollectionIfMissing(mileageResponseCollection, mileageResponse);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mileageResponse);
        });

        it('should add only unique MileageResponse to an array', () => {
          const mileageResponseArray: IMileageResponse[] = [{ id: 123 }, { id: 456 }, { id: 64274 }];
          const mileageResponseCollection: IMileageResponse[] = [{ id: 123 }];
          expectedResult = service.addMileageResponseToCollectionIfMissing(mileageResponseCollection, ...mileageResponseArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const mileageResponse: IMileageResponse = { id: 123 };
          const mileageResponse2: IMileageResponse = { id: 456 };
          expectedResult = service.addMileageResponseToCollectionIfMissing([], mileageResponse, mileageResponse2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(mileageResponse);
          expect(expectedResult).toContain(mileageResponse2);
        });

        it('should accept null and undefined values', () => {
          const mileageResponse: IMileageResponse = { id: 123 };
          expectedResult = service.addMileageResponseToCollectionIfMissing([], null, mileageResponse, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(mileageResponse);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
