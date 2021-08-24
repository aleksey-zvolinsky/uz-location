import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LocationResponseComponent } from '../list/location-response.component';
import { LocationResponseDetailComponent } from '../detail/location-response-detail.component';
import { LocationResponseUpdateComponent } from '../update/location-response-update.component';
import { LocationResponseRoutingResolveService } from './location-response-routing-resolve.service';

const locationResponseRoute: Routes = [
  {
    path: '',
    component: LocationResponseComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LocationResponseDetailComponent,
    resolve: {
      locationResponse: LocationResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LocationResponseUpdateComponent,
    resolve: {
      locationResponse: LocationResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LocationResponseUpdateComponent,
    resolve: {
      locationResponse: LocationResponseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(locationResponseRoute)],
  exports: [RouterModule],
})
export class LocationResponseRoutingModule {}
