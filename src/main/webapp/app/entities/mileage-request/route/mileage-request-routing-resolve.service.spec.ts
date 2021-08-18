jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMileageRequest, MileageRequest } from '../mileage-request.model';
import { MileageRequestService } from '../service/mileage-request.service';

import { MileageRequestRoutingResolveService } from './mileage-request-routing-resolve.service';

describe('Service Tests', () => {
  describe('MileageRequest routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MileageRequestRoutingResolveService;
    let service: MileageRequestService;
    let resultMileageRequest: IMileageRequest | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MileageRequestRoutingResolveService);
      service = TestBed.inject(MileageRequestService);
      resultMileageRequest = undefined;
    });

    describe('resolve', () => {
      it('should return IMileageRequest returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMileageRequest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMileageRequest).toEqual({ id: 123 });
      });

      it('should return new IMileageRequest if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMileageRequest = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMileageRequest).toEqual(new MileageRequest());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as MileageRequest })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMileageRequest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMileageRequest).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
