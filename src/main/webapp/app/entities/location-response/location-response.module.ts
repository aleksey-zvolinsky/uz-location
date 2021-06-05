import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { LocationResponseComponent } from './list/location-response.component';
import { LocationResponseDetailComponent } from './detail/location-response-detail.component';
import { LocationResponseUpdateComponent } from './update/location-response-update.component';
import { LocationResponseDeleteDialogComponent } from './delete/location-response-delete-dialog.component';
import { LocationResponseRoutingModule } from './route/location-response-routing.module';

@NgModule({
  imports: [SharedModule, LocationResponseRoutingModule],
  declarations: [
    LocationResponseComponent,
    LocationResponseDetailComponent,
    LocationResponseUpdateComponent,
    LocationResponseDeleteDialogComponent,
  ],
  entryComponents: [LocationResponseDeleteDialogComponent],
})
export class LocationResponseModule {}
