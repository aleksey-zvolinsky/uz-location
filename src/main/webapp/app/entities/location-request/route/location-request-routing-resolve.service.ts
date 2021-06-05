import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocationRequest, LocationRequest } from '../location-request.model';
import { LocationRequestService } from '../service/location-request.service';

@Injectable({ providedIn: 'root' })
export class LocationRequestRoutingResolveService implements Resolve<ILocationRequest> {
  constructor(protected service: LocationRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocationRequest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((locationRequest: HttpResponse<LocationRequest>) => {
          if (locationRequest.body) {
            return of(locationRequest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LocationRequest());
  }
}
