import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMileageResponse } from '../mileage-response.model';

@Component({
  selector: 'jhi-mileage-response-detail',
  templateUrl: './mileage-response-detail.component.html',
})
export class MileageResponseDetailComponent implements OnInit {
  mileageResponse: IMileageResponse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mileageResponse }) => {
      this.mileageResponse = mileageResponse;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
