import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITank } from '../tank.model';
import { TankService } from '../service/tank.service';

@Component({
  templateUrl: './tank-delete-dialog.component.html',
})
export class TankDeleteDialogComponent {
  tank?: ITank;

  constructor(protected tankService: TankService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tankService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
