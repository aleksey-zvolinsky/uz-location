import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocationResponse, LocationResponse } from '../location-response.model';
import { LocationResponseService } from '../service/location-response.service';

@Injectable({ providedIn: 'root' })
export class LocationResponseRoutingResolveService implements Resolve<ILocationResponse> {
  constructor(protected service: LocationResponseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocationResponse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((locationResponse: HttpResponse<LocationResponse>) => {
          if (locationResponse.body) {
            return of(locationResponse.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LocationResponse());
  }
}
