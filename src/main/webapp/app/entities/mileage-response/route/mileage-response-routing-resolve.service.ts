import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMileageResponse, MileageResponse } from '../mileage-response.model';
import { MileageResponseService } from '../service/mileage-response.service';

@Injectable({ providedIn: 'root' })
export class MileageResponseRoutingResolveService implements Resolve<IMileageResponse> {
  constructor(protected service: MileageResponseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMileageResponse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mileageResponse: HttpResponse<MileageResponse>) => {
          if (mileageResponse.body) {
            return of(mileageResponse.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MileageResponse());
  }
}
