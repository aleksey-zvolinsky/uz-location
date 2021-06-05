import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMileageRequest } from '../mileage-request.model';
import { MileageRequestService } from '../service/mileage-request.service';
import { MileageRequestDeleteDialogComponent } from '../delete/mileage-request-delete-dialog.component';

@Component({
  selector: 'jhi-mileage-request',
  templateUrl: './mileage-request.component.html',
})
export class MileageRequestComponent implements OnInit {
  mileageRequests?: IMileageRequest[];
  isLoading = false;

  constructor(protected mileageRequestService: MileageRequestService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.mileageRequestService.query().subscribe(
      (res: HttpResponse<IMileageRequest[]>) => {
        this.isLoading = false;
        this.mileageRequests = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMileageRequest): number {
    return item.id!;
  }

  delete(mileageRequest: IMileageRequest): void {
    const modalRef = this.modalService.open(MileageRequestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mileageRequest = mileageRequest;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
