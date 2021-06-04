import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMileageResponse } from '../mileage-response.model';
import { MileageResponseService } from '../service/mileage-response.service';

@Component({
  templateUrl: './mileage-response-delete-dialog.component.html',
})
export class MileageResponseDeleteDialogComponent {
  mileageResponse?: IMileageResponse;

  constructor(protected mileageResponseService: MileageResponseService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mileageResponseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
