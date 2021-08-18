import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ILocationResponse, LocationResponse } from '../location-response.model';

import { LocationResponseService } from './location-response.service';

describe('Service Tests', () => {
  describe('LocationResponse Service', () => {
    let service: LocationResponseService;
    let httpMock: HttpTestingController;
    let elemDefault: ILocationResponse;
    let expectedResult: ILocationResponse | ILocationResponse[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LocationResponseService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        responseDatetime: currentDate,
        tankNumber: 'AAAAAAA',
        tankType: 'AAAAAAA',
        cargoId: 'AAAAAAA',
        cargoName: 'AAAAAAA',
        weight: 'AAAAAAA',
        receiverId: 'AAAAAAA',
        tankIndex: 'AAAAAAA',
        locationStationId: 'AAAAAAA',
        locationStationName: 'AAAAAAA',
        locationDatetime: 'AAAAAAA',
        locationOperation: 'AAAAAAA',
        stateFromStationId: 'AAAAAAA',
        stateFromStationName: 'AAAAAAA',
        stateToStationId: 'AAAAAAA',
        stateToStationName: 'AAAAAAA',
        stateSendDatetime: 'AAAAAAA',
        stateSenderId: 'AAAAAAA',
        planedServiceDatetime: 'AAAAAAA',
        tankOwner: 'AAAAAAA',
        tankModel: 'AAAAAAA',
        defectRegion: 'AAAAAAA',
        defectStation: 'AAAAAAA',
        defectDatetime: 'AAAAAAA',
        defectDetails: 'AAAAAAA',
        repairRegion: 'AAAAAAA',
        repairStation: 'AAAAAAA',
        repairDatetime: 'AAAAAAA',
        updateDatetime: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            responseDatetime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LocationResponse', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            responseDatetime: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            responseDatetime: currentDate,
          },
          returnedFromService
        );

        service.create(new LocationResponse()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LocationResponse', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            responseDatetime: currentDate.format(DATE_TIME_FORMAT),
            tankNumber: 'BBBBBB',
            tankType: 'BBBBBB',
            cargoId: 'BBBBBB',
            cargoName: 'BBBBBB',
            weight: 'BBBBBB',
            receiverId: 'BBBBBB',
            tankIndex: 'BBBBBB',
            locationStationId: 'BBBBBB',
            locationStationName: 'BBBBBB',
            locationDatetime: 'BBBBBB',
            locationOperation: 'BBBBBB',
            stateFromStationId: 'BBBBBB',
            stateFromStationName: 'BBBBBB',
            stateToStationId: 'BBBBBB',
            stateToStationName: 'BBBBBB',
            stateSendDatetime: 'BBBBBB',
            stateSenderId: 'BBBBBB',
            planedServiceDatetime: 'BBBBBB',
            tankOwner: 'BBBBBB',
            tankModel: 'BBBBBB',
            defectRegion: 'BBBBBB',
            defectStation: 'BBBBBB',
            defectDatetime: 'BBBBBB',
            defectDetails: 'BBBBBB',
            repairRegion: 'BBBBBB',
            repairStation: 'BBBBBB',
            repairDatetime: 'BBBBBB',
            updateDatetime: 'BBBBBB',
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

      it('should partial update a LocationResponse', () => {
        const patchObject = Object.assign(
          {
            responseDatetime: currentDate.format(DATE_TIME_FORMAT),
            tankNumber: 'BBBBBB',
            cargoName: 'BBBBBB',
            weight: 'BBBBBB',
            tankIndex: 'BBBBBB',
            locationStationId: 'BBBBBB',
            locationStationName: 'BBBBBB',
            locationDatetime: 'BBBBBB',
            stateToStationName: 'BBBBBB',
            stateSenderId: 'BBBBBB',
            planedServiceDatetime: 'BBBBBB',
            tankOwner: 'BBBBBB',
            defectRegion: 'BBBBBB',
            defectDatetime: 'BBBBBB',
            defectDetails: 'BBBBBB',
            repairRegion: 'BBBBBB',
            repairStation: 'BBBBBB',
            repairDatetime: 'BBBBBB',
            updateDatetime: 'BBBBBB',
          },
          new LocationResponse()
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

      it('should return a list of LocationResponse', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            responseDatetime: currentDate.format(DATE_TIME_FORMAT),
            tankNumber: 'BBBBBB',
            tankType: 'BBBBBB',
            cargoId: 'BBBBBB',
            cargoName: 'BBBBBB',
            weight: 'BBBBBB',
            receiverId: 'BBBBBB',
            tankIndex: 'BBBBBB',
            locationStationId: 'BBBBBB',
            locationStationName: 'BBBBBB',
            locationDatetime: 'BBBBBB',
            locationOperation: 'BBBBBB',
            stateFromStationId: 'BBBBBB',
            stateFromStationName: 'BBBBBB',
            stateToStationId: 'BBBBBB',
            stateToStationName: 'BBBBBB',
            stateSendDatetime: 'BBBBBB',
            stateSenderId: 'BBBBBB',
            planedServiceDatetime: 'BBBBBB',
            tankOwner: 'BBBBBB',
            tankModel: 'BBBBBB',
            defectRegion: 'BBBBBB',
            defectStation: 'BBBBBB',
            defectDatetime: 'BBBBBB',
            defectDetails: 'BBBBBB',
            repairRegion: 'BBBBBB',
            repairStation: 'BBBBBB',
            repairDatetime: 'BBBBBB',
            updateDatetime: 'BBBBBB',
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

      it('should delete a LocationResponse', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLocationResponseToCollectionIfMissing', () => {
        it('should add a LocationResponse to an empty array', () => {
          const locationResponse: ILocationResponse = { id: 123 };
          expectedResult = service.addLocationResponseToCollectionIfMissing([], locationResponse);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(locationResponse);
        });

        it('should not add a LocationResponse to an array that contains it', () => {
          const locationResponse: ILocationResponse = { id: 123 };
          const locationResponseCollection: ILocationResponse[] = [
            {
              ...locationResponse,
            },
            { id: 456 },
          ];
          expectedResult = service.addLocationResponseToCollectionIfMissing(locationResponseCollection, locationResponse);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a LocationResponse to an array that doesn't contain it", () => {
          const locationResponse: ILocationResponse = { id: 123 };
          const locationResponseCollection: ILocationResponse[] = [{ id: 456 }];
          expectedResult = service.addLocationResponseToCollectionIfMissing(locationResponseCollection, locationResponse);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(locationResponse);
        });

        it('should add only unique LocationResponse to an array', () => {
          const locationResponseArray: ILocationResponse[] = [{ id: 123 }, { id: 456 }, { id: 80316 }];
          const locationResponseCollection: ILocationResponse[] = [{ id: 123 }];
          expectedResult = service.addLocationResponseToCollectionIfMissing(locationResponseCollection, ...locationResponseArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const locationResponse: ILocationResponse = { id: 123 };
          const locationResponse2: ILocationResponse = { id: 456 };
          expectedResult = service.addLocationResponseToCollectionIfMissing([], locationResponse, locationResponse2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(locationResponse);
          expect(expectedResult).toContain(locationResponse2);
        });

        it('should accept null and undefined values', () => {
          const locationResponse: ILocationResponse = { id: 123 };
          expectedResult = service.addLocationResponseToCollectionIfMissing([], null, locationResponse, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(locationResponse);
        });

        it('should return initial array if no LocationResponse is added', () => {
          const locationResponseCollection: ILocationResponse[] = [{ id: 123 }];
          expectedResult = service.addLocationResponseToCollectionIfMissing(locationResponseCollection, undefined, null);
          expect(expectedResult).toEqual(locationResponseCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
