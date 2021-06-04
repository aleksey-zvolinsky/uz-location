import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITank, Tank } from '../tank.model';
import { TankService } from '../service/tank.service';

@Injectable({ providedIn: 'root' })
export class TankRoutingResolveService implements Resolve<ITank> {
  constructor(protected service: TankService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITank> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tank: HttpResponse<Tank>) => {
          if (tank.body) {
            return of(tank.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Tank());
  }
}
