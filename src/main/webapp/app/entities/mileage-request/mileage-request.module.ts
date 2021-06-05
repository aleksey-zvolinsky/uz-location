import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MileageRequestComponent } from './list/mileage-request.component';
import { MileageRequestDetailComponent } from './detail/mileage-request-detail.component';
import { MileageRequestUpdateComponent } from './update/mileage-request-update.component';
import { MileageRequestDeleteDialogComponent } from './delete/mileage-request-delete-dialog.component';
import { MileageRequestRoutingModule } from './route/mileage-request-routing.module';

@NgModule({
  imports: [SharedModule, MileageRequestRoutingModule],
  declarations: [
    MileageRequestComponent,
    MileageRequestDetailComponent,
    MileageRequestUpdateComponent,
    MileageRequestDeleteDialogComponent,
  ],
  entryComponents: [MileageRequestDeleteDialogComponent],
})
export class MileageRequestModule {}
