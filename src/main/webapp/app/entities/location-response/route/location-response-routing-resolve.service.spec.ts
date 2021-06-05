jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ILocationResponse, LocationResponse } from '../location-response.model';
import { LocationResponseService } from '../service/location-response.service';

import { LocationResponseRoutingResolveService } from './location-response-routing-resolve.service';

describe('Service Tests', () => {
  describe('LocationResponse routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: LocationResponseRoutingResolveService;
    let service: LocationResponseService;
    let resultLocationResponse: ILocationResponse | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(LocationResponseRoutingResolveService);
      service = TestBed.inject(LocationResponseService);
      resultLocationResponse = undefined;
    });

    describe('resolve', () => {
      it('should return ILocationResponse returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocationResponse = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocationResponse).toEqual({ id: 123 });
      });

      it('should return new ILocationResponse if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocationResponse = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultLocationResponse).toEqual(new LocationResponse());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultLocationResponse = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultLocationResponse).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
