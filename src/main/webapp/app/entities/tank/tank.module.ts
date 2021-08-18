import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TankComponent } from './list/tank.component';
import { TankDetailComponent } from './detail/tank-detail.component';
import { TankUpdateComponent } from './update/tank-update.component';
import { TankDeleteDialogComponent } from './delete/tank-delete-dialog.component';
import { TankRoutingModule } from './route/tank-routing.module';

@NgModule({
  imports: [SharedModule, TankRoutingModule],
  declarations: [TankComponent, TankDetailComponent, TankUpdateComponent, TankDeleteDialogComponent],
  entryComponents: [TankDeleteDialogComponent],
})
export class TankModule {}
