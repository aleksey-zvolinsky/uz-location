import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocationRequestComponent } from '../list/location-request.component';
import { LocationRequestDetailComponent } from '../detail/location-request-detail.component';
import { LocationRequestUpdateComponent } from '../update/location-request-update.component';
import { LocationRequestRoutingResolveService } from './location-request-routing-resolve.service';

const locationRequestRoute: Routes = [
  {
    path: '',
    component: LocationRequestComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocationRequestDetailComponent,
    resolve: {
      locationRequest: LocationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocationRequestUpdateComponent,
    resolve: {
      locationRequest: LocationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocationRequestUpdateComponent,
    resolve: {
      locationRequest: LocationRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(locationRequestRoute)],
  exports: [RouterModule],
})
export class LocationRequestRoutingModule {}
