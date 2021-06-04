import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMileageResponse } from '../mileage-response.model';
import { MileageResponseService } from '../service/mileage-response.service';
import { MileageResponseDeleteDialogComponent } from '../delete/mileage-response-delete-dialog.component';

@Component({
  selector: 'jhi-mileage-response',
  templateUrl: './mileage-response.component.html',
})
export class MileageResponseComponent implements OnInit {
  mileageResponses?: IMileageResponse[];
  isLoading = false;

  constructor(protected mileageResponseService: MileageResponseService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.mileageResponseService.query().subscribe(
      (res: HttpResponse<IMileageResponse[]>) => {
        this.isLoading = false;
        this.mileageResponses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMileageResponse): number {
    return item.id!;
  }

  delete(mileageResponse: IMileageResponse): void {
    const modalRef = this.modalService.open(MileageResponseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mileageResponse = mileageResponse;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
