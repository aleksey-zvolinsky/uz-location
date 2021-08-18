import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MileageResponseComponent } from './list/mileage-response.component';
import { MileageResponseDetailComponent } from './detail/mileage-response-detail.component';
import { MileageResponseUpdateComponent } from './update/mileage-response-update.component';
import { MileageResponseDeleteDialogComponent } from './delete/mileage-response-delete-dialog.component';
import { MileageResponseRoutingModule } from './route/mileage-response-routing.module';

@NgModule({
  imports: [SharedModule, MileageResponseRoutingModule],
  declarations: [
    MileageResponseComponent,
    MileageResponseDetailComponent,
    MileageResponseUpdateComponent,
    MileageResponseDeleteDialogComponent,
  ],
  entryComponents: [MileageResponseDeleteDialogComponent],
})
export class MileageResponseModule {}
