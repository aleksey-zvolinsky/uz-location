import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocationResponse } from '../location-response.model';
import { LocationResponseService } from '../service/location-response.service';

@Component({
  templateUrl: './location-response-delete-dialog.component.html',
})
export class LocationResponseDeleteDialogComponent {
  locationResponse?: ILocationResponse;

  constructor(protected locationResponseService: LocationResponseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.locationResponseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
