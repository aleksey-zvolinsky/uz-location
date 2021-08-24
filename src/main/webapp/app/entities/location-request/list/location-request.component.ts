import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocationRequest } from '../location-request.model';
import { LocationRequestService } from '../service/location-request.service';
import { LocationRequestDeleteDialogComponent } from '../delete/location-request-delete-dialog.component';

@Component({
  selector: 'jhi-location-request',
  templateUrl: './location-request.component.html',
})
export class LocationRequestComponent implements OnInit {
  locationRequests?: ILocationRequest[];
  isLoading = false;

  constructor(protected locationRequestService: LocationRequestService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.locationRequestService.query().subscribe(
      (res: HttpResponse<ILocationRequest[]>) => {
        this.isLoading = false;
        this.locationRequests = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILocationRequest): number {
    return item.id!;
  }

  delete(locationRequest: ILocationRequest): void {
    const modalRef = this.modalService.open(LocationRequestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.locationRequest = locationRequest;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
