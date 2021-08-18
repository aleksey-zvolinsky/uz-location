jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITank, Tank } from '../tank.model';
import { TankService } from '../service/tank.service';

import { TankRoutingResolveService } from './tank-routing-resolve.service';

describe('Service Tests', () => {
  describe('Tank routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TankRoutingResolveService;
    let service: TankService;
    let resultTank: ITank | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TankRoutingResolveService);
      service = TestBed.inject(TankService);
      resultTank = undefined;
    });

    describe('resolve', () => {
      it('should return ITank returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTank = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTank).toEqual({ id: 123 });
      });

      it('should return new ITank if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTank = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTank).toEqual(new Tank());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Tank })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTank = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTank).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
