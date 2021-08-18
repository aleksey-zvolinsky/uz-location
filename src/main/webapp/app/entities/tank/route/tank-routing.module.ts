import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TankComponent } from '../list/tank.component';
import { TankDetailComponent } from '../detail/tank-detail.component';
import { TankUpdateComponent } from '../update/tank-update.component';
import { TankRoutingResolveService } from './tank-routing-resolve.service';

const tankRoute: Routes = [
  {
    path: '',
    component: TankComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TankDetailComponent,
    resolve: {
      tank: TankRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TankUpdateComponent,
    resolve: {
      tank: TankRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TankUpdateComponent,
    resolve: {
      tank: TankRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tankRoute)],
  exports: [RouterModule],
})
export class TankRoutingModule {}
