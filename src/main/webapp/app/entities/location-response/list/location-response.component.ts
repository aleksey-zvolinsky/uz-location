import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocationResponse } from '../location-response.model';
import { LocationResponseService } from '../service/location-response.service';
import { LocationResponseDeleteDialogComponent } from '../delete/location-response-delete-dialog.component';

@Component({
  selector: 'jhi-location-response',
  templateUrl: './location-response.component.html',
})
export class LocationResponseComponent implements OnInit {
  locationResponses?: ILocationResponse[];
  isLoading = false;

  constructor(protected locationResponseService: LocationResponseService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.locationResponseService.query().subscribe(
      (res: HttpResponse<ILocationResponse[]>) => {
        this.isLoading = false;
        this.locationResponses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILocationResponse): number {
    return item.id!;
  }

  delete(locationResponse: ILocationResponse): void {
    const modalRef = this.modalService.open(LocationResponseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.locationResponse = locationResponse;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
