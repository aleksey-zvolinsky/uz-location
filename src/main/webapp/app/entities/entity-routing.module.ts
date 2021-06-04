import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'tank',
        data: { pageTitle: 'uzLocationApp.tank.home.title' },
        loadChildren: () => import('./tank/tank.module').then(m => m.TankModule),
      },
      {
        path: 'location-request',
        data: { pageTitle: 'uzLocationApp.locationRequest.home.title' },
        loadChildren: () => import('./location-request/location-request.module').then(m => m.LocationRequestModule),
      },
      {
        path: 'location-response',
        data: { pageTitle: 'uzLocationApp.locationResponse.home.title' },
        loadChildren: () => import('./location-response/location-response.module').then(m => m.LocationResponseModule),
      },
      {
        path: 'mileage-request',
        data: { pageTitle: 'uzLocationApp.mileageRequest.home.title' },
        loadChildren: () => import('./mileage-request/mileage-request.module').then(m => m.MileageRequestModule),
      },
      {
        path: 'mileage-response',
        data: { pageTitle: 'uzLocationApp.mileageResponse.home.title' },
        loadChildren: () => import('./mileage-response/mileage-response.module').then(m => m.MileageResponseModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
