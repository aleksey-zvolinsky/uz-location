import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILocationResponse } from '../location-response.model';

@Component({
  selector: 'jhi-location-response-detail',
  templateUrl: './location-response-detail.component.html',
})
export class LocationResponseDetailComponent implements OnInit {
  locationResponse: ILocationResponse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locationResponse }) => {
      this.locationResponse = locationResponse;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
