import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMileageRequest } from '../mileage-request.model';

@Component({
  selector: 'jhi-mileage-request-detail',
  templateUrl: './mileage-request-detail.component.html',
})
export class MileageRequestDetailComponent implements OnInit {
  mileageRequest: IMileageRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mileageRequest }) => {
      this.mileageRequest = mileageRequest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
