jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILocationRequest, LocationRequest } from '../location-request.model';
import { LocationRequestService } from '../service/location-request.service';

import { LocationRequestRoutingResolveService } from './location-request-routing-resolve.service';

describe('Service Tests', () => {
  describe('LocationRequest routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LocationRequestRoutingResolveService;
    let service: LocationRequestService;
    let resultLocationRequest: ILocationRequest | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LocationRequestRoutingResolveService);
      service = TestBed.inject(LocationRequestService);
      resultLocationRequest = undefined;
    });

    describe('resolve', () => {
      it('should return ILocationRequest returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocationRequest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocationRequest).toEqual({ id: 123 });
      });

      it('should return new ILocationRequest if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocationRequest = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLocationRequest).toEqual(new LocationRequest());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocationRequest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocationRequest).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
