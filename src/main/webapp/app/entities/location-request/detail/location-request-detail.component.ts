import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILocationRequest } from '../location-request.model';

@Component({
  selector: 'jhi-location-request-detail',
  templateUrl: './location-request-detail.component.html',
})
export class LocationRequestDetailComponent implements OnInit {
  locationRequest: ILocationRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locationRequest }) => {
      this.locationRequest = locationRequest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
