import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MileageRequestComponent } from '../list/mileage-request.component';
import { MileageRequestDetailComponent } from '../detail/mileage-request-detail.component';
import { MileageRequestUpdateComponent } from '../update/mileage-request-update.component';
import { MileageRequestRoutingResolveService } from './mileage-request-routing-resolve.service';

const mileageRequestRoute: Routes = [
  {
    path: '',
    component: MileageRequestComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MileageRequestDetailComponent,
    resolve: {
      mileageRequest: MileageRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MileageRequestUpdateComponent,
    resolve: {
      mileageRequest: MileageRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MileageRequestUpdateComponent,
    resolve: {
      mileageRequest: MileageRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mileageRequestRoute)],
  exports: [RouterModule],
})
export class MileageRequestRoutingModule {}
