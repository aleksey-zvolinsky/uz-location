import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LocationRequestComponent } from './list/location-request.component';
import { LocationRequestDetailComponent } from './detail/location-request-detail.component';
import { LocationRequestUpdateComponent } from './update/location-request-update.component';
import { LocationRequestDeleteDialogComponent } from './delete/location-request-delete-dialog.component';
import { LocationRequestRoutingModule } from './route/location-request-routing.module';

@NgModule({
  imports: [SharedModule, LocationRequestRoutingModule],
  declarations: [
    LocationRequestComponent,
    LocationRequestDetailComponent,
    LocationRequestUpdateComponent,
    LocationRequestDeleteDialogComponent,
  ],
  entryComponents: [LocationRequestDeleteDialogComponent],
})
export class LocationRequestModule {}
