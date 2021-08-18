import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMileageRequest } from '../mileage-request.model';
import { MileageRequestService } from '../service/mileage-request.service';

@Component({
  templateUrl: './mileage-request-delete-dialog.component.html',
})
export class MileageRequestDeleteDialogComponent {
  mileageRequest?: IMileageRequest;

  constructor(protected mileageRequestService: MileageRequestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mileageRequestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
