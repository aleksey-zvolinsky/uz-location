import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMileageRequest, MileageRequest } from '../mileage-request.model';
import { MileageRequestService } from '../service/mileage-request.service';

@Injectable({ providedIn: 'root' })
export class MileageRequestRoutingResolveService implements Resolve<IMileageRequest> {
  constructor(protected service: MileageRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMileageRequest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mileageRequest: HttpResponse<MileageRequest>) => {
          if (mileageRequest.body) {
            return of(mileageRequest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MileageRequest());
  }
}
