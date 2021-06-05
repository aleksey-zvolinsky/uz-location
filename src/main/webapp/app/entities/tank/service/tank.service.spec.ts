import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITank, Tank } from '../tank.model';

import { TankService } from './tank.service';

describe('Service Tests', () => {
  describe('Tank Service', () => {
    let service: TankService;
    let httpMock: HttpTestingController;
    let elemDefault: ITank;
    let expectedResult: ITank | ITank[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TankService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        tankNumber: 'AAAAAAA',
        ownerName: 'AAAAAAA',
        clientName: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Tank', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Tank()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Tank', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tankNumber: 'BBBBBB',
            ownerName: 'BBBBBB',
            clientName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Tank', () => {
        const patchObject = Object.assign(
          {
            ownerName: 'BBBBBB',
          },
          new Tank()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Tank', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tankNumber: 'BBBBBB',
            ownerName: 'BBBBBB',
            clientName: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Tank', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTankToCollectionIfMissing', () => {
        it('should add a Tank to an empty array', () => {
          const tank: ITank = { id: 123 };
          expectedResult = service.addTankToCollectionIfMissing([], tank);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tank);
        });

        it('should not add a Tank to an array that contains it', () => {
          const tank: ITank = { id: 123 };
          const tankCollection: ITank[] = [
            {
              ...tank,
            },
            { id: 456 },
          ];
          expectedResult = service.addTankToCollectionIfMissing(tankCollection, tank);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Tank to an array that doesn't contain it", () => {
          const tank: ITank = { id: 123 };
          const tankCollection: ITank[] = [{ id: 456 }];
          expectedResult = service.addTankToCollectionIfMissing(tankCollection, tank);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tank);
        });

        it('should add only unique Tank to an array', () => {
          const tankArray: ITank[] = [{ id: 123 }, { id: 456 }, { id: 63059 }];
          const tankCollection: ITank[] = [{ id: 123 }];
          expectedResult = service.addTankToCollectionIfMissing(tankCollection, ...tankArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const tank: ITank = { id: 123 };
          const tank2: ITank = { id: 456 };
          expectedResult = service.addTankToCollectionIfMissing([], tank, tank2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(tank);
          expect(expectedResult).toContain(tank2);
        });

        it('should accept null and undefined values', () => {
          const tank: ITank = { id: 123 };
          expectedResult = service.addTankToCollectionIfMissing([], null, tank, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(tank);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
