import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ILocationRequest, LocationRequest } from '../location-request.model';
import { LocationRequestService } from '../service/location-request.service';
import { ILocationResponse } from 'app/entities/location-response/location-response.model';
import { LocationResponseService } from 'app/entities/location-response/service/location-response.service';
import { ITank } from 'app/entities/tank/tank.model';
import { TankService } from 'app/entities/tank/service/tank.service';

@Component({
  selector: 'jhi-location-request-update',
  templateUrl: './location-request-update.component.html',
})
export class LocationRequestUpdateComponent implements OnInit {
  isSaving = false;

  locationResponsesCollection: ILocationResponse[] = [];
  tanksSharedCollection: ITank[] = [];

  editForm = this.fb.group({
    id: [],
    requestDatetime: [],
    tankNumbers: [],
    locationResponse: [],
    tank: [],
  });

  constructor(
    protected locationRequestService: LocationRequestService,
    protected locationResponseService: LocationResponseService,
    protected tankService: TankService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locationRequest }) => {
      if (locationRequest.id === undefined) {
        const today = dayjs().startOf('day');
        locationRequest.requestDatetime = today;
      }

      this.updateForm(locationRequest);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const locationRequest = this.createFromForm();
    if (locationRequest.id !== undefined) {
      this.subscribeToSaveResponse(this.locationRequestService.update(locationRequest));
    } else {
      this.subscribeToSaveResponse(this.locationRequestService.create(locationRequest));
    }
  }

  trackLocationResponseById(index: number, item: ILocationResponse): number {
    return item.id!;
  }

  trackTankById(index: number, item: ITank): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocationRequest>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(locationRequest: ILocationRequest): void {
    this.editForm.patchValue({
      id: locationRequest.id,
      requestDatetime: locationRequest.requestDatetime ? locationRequest.requestDatetime.format(DATE_TIME_FORMAT) : null,
      tankNumbers: locationRequest.tankNumbers,
      locationResponse: locationRequest.locationResponse,
      tank: locationRequest.tank,
    });

    this.locationResponsesCollection = this.locationResponseService.addLocationResponseToCollectionIfMissing(
      this.locationResponsesCollection,
      locationRequest.locationResponse
    );
    this.tanksSharedCollection = this.tankService.addTankToCollectionIfMissing(this.tanksSharedCollection, locationRequest.tank);
  }

  protected loadRelationshipsOptions(): void {
    this.locationResponseService
      .query({ filter: 'locationrequest-is-null' })
      .pipe(map((res: HttpResponse<ILocationResponse[]>) => res.body ?? []))
      .pipe(
        map((locationResponses: ILocationResponse[]) =>
          this.locationResponseService.addLocationResponseToCollectionIfMissing(
            locationResponses,
            this.editForm.get('locationResponse')!.value
          )
        )
      )
      .subscribe((locationResponses: ILocationResponse[]) => (this.locationResponsesCollection = locationResponses));

    this.tankService
      .query()
      .pipe(map((res: HttpResponse<ITank[]>) => res.body ?? []))
      .pipe(map((tanks: ITank[]) => this.tankService.addTankToCollectionIfMissing(tanks, this.editForm.get('tank')!.value)))
      .subscribe((tanks: ITank[]) => (this.tanksSharedCollection = tanks));
  }

  protected createFromForm(): ILocationRequest {
    return {
      ...new LocationRequest(),
      id: this.editForm.get(['id'])!.value,
      requestDatetime: this.editForm.get(['requestDatetime'])!.value
        ? dayjs(this.editForm.get(['requestDatetime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      tankNumbers: this.editForm.get(['tankNumbers'])!.value,
      locationResponse: this.editForm.get(['locationResponse'])!.value,
      tank: this.editForm.get(['tank'])!.value,
    };
  }
}
