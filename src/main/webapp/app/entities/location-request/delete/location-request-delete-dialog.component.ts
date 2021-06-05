import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocationRequest } from '../location-request.model';
import { LocationRequestService } from '../service/location-request.service';

@Component({
  templateUrl: './location-request-delete-dialog.component.html',
})
export class LocationRequestDeleteDialogComponent {
  locationRequest?: ILocationRequest;

  constructor(protected locationRequestService: LocationRequestService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.locationRequestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
