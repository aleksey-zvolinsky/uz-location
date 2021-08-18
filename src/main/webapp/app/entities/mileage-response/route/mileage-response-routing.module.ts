import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MileageResponseComponent } from '../list/mileage-response.component';
import { MileageResponseDetailComponent } from '../detail/mileage-response-detail.component';
import { MileageResponseUpdateComponent } from '../update/mileage-response-update.component';
import { MileageResponseRoutingResolveService } from './mileage-response-routing-resolve.service';

const mileageResponseRoute: Routes = [
  {
    path: '',
    component: MileageResponseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MileageResponseDetailComponent,
    resolve: {
      mileageResponse: MileageResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MileageResponseUpdateComponent,
    resolve: {
      mileageResponse: MileageResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MileageResponseUpdateComponent,
    resolve: {
      mileageResponse: MileageResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mileageResponseRoute)],
  exports: [RouterModule],
})
export class MileageResponseRoutingModule {}
