import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILocationResponse, LocationResponse } from '../location-response.model';
import { LocationResponseService } from '../service/location-response.service';

@Component({
  selector: 'jhi-location-response-update',
  templateUrl: './location-response-update.component.html',
})
export class LocationResponseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    responseDatetime: [],
    tankNumber: [],
    tankType: [],
    cargoId: [],
    cargoName: [],
    weight: [],
    receiverId: [],
    tankIndex: [],
    locationStationId: [],
    locationStationName: [],
    locationDatetime: [],
    locationOperation: [],
    stateFromStationId: [],
    stateFromStationName: [],
    stateToStationId: [],
    stateToStationName: [],
    stateSendDatetime: [],
    stateSenderId: [],
    planedServiceDatetime: [],
    mileageCurrent: [],
    mileageDatetime: [],
    mileageRemain: [],
    mileageUpdateDatetime: [],
    tankOwner: [],
    tankModel: [],
    defectRegion: [],
    defectStation: [],
    defectDatetime: [],
    defectDetails: [],
    repairRegion: [],
    repairStation: [],
    repairDatatime: [],
  });

  constructor(
    protected locationResponseService: LocationResponseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ locationResponse }) => {
      this.updateForm(locationResponse);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const locationResponse = this.createFromForm();
    if (locationResponse.id !== undefined) {
      this.subscribeToSaveResponse(this.locationResponseService.update(locationResponse));
    } else {
      this.subscribeToSaveResponse(this.locationResponseService.create(locationResponse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocationResponse>>): void {
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

  protected updateForm(locationResponse: ILocationResponse): void {
    this.editForm.patchValue({
      id: locationResponse.id,
      responseDatetime: locationResponse.responseDatetime,
      tankNumber: locationResponse.tankNumber,
      tankType: locationResponse.tankType,
      cargoId: locationResponse.cargoId,
      cargoName: locationResponse.cargoName,
      weight: locationResponse.weight,
      receiverId: locationResponse.receiverId,
      tankIndex: locationResponse.tankIndex,
      locationStationId: locationResponse.locationStationId,
      locationStationName: locationResponse.locationStationName,
      locationDatetime: locationResponse.locationDatetime,
      locationOperation: locationResponse.locationOperation,
      stateFromStationId: locationResponse.stateFromStationId,
      stateFromStationName: locationResponse.stateFromStationName,
      stateToStationId: locationResponse.stateToStationId,
      stateToStationName: locationResponse.stateToStationName,
      stateSendDatetime: locationResponse.stateSendDatetime,
      stateSenderId: locationResponse.stateSenderId,
      planedServiceDatetime: locationResponse.planedServiceDatetime,
      mileageCurrent: locationResponse.mileageCurrent,
      mileageDatetime: locationResponse.mileageDatetime,
      mileageRemain: locationResponse.mileageRemain,
      mileageUpdateDatetime: locationResponse.mileageUpdateDatetime,
      tankOwner: locationResponse.tankOwner,
      tankModel: locationResponse.tankModel,
      defectRegion: locationResponse.defectRegion,
      defectStation: locationResponse.defectStation,
      defectDatetime: locationResponse.defectDatetime,
      defectDetails: locationResponse.defectDetails,
      repairRegion: locationResponse.repairRegion,
      repairStation: locationResponse.repairStation,
      repairDatatime: locationResponse.repairDatatime,
    });
  }

  protected createFromForm(): ILocationResponse {
    return {
      ...new LocationResponse(),
      id: this.editForm.get(['id'])!.value,
      responseDatetime: this.editForm.get(['responseDatetime'])!.value,
      tankNumber: this.editForm.get(['tankNumber'])!.value,
      tankType: this.editForm.get(['tankType'])!.value,
      cargoId: this.editForm.get(['cargoId'])!.value,
      cargoName: this.editForm.get(['cargoName'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      receiverId: this.editForm.get(['receiverId'])!.value,
      tankIndex: this.editForm.get(['tankIndex'])!.value,
      locationStationId: this.editForm.get(['locationStationId'])!.value,
      locationStationName: this.editForm.get(['locationStationName'])!.value,
      locationDatetime: this.editForm.get(['locationDatetime'])!.value,
      locationOperation: this.editForm.get(['locationOperation'])!.value,
      stateFromStationId: this.editForm.get(['stateFromStationId'])!.value,
      stateFromStationName: this.editForm.get(['stateFromStationName'])!.value,
      stateToStationId: this.editForm.get(['stateToStationId'])!.value,
      stateToStationName: this.editForm.get(['stateToStationName'])!.value,
      stateSendDatetime: this.editForm.get(['stateSendDatetime'])!.value,
      stateSenderId: this.editForm.get(['stateSenderId'])!.value,
      planedServiceDatetime: this.editForm.get(['planedServiceDatetime'])!.value,
      mileageCurrent: this.editForm.get(['mileageCurrent'])!.value,
      mileageDatetime: this.editForm.get(['mileageDatetime'])!.value,
      mileageRemain: this.editForm.get(['mileageRemain'])!.value,
      mileageUpdateDatetime: this.editForm.get(['mileageUpdateDatetime'])!.value,
      tankOwner: this.editForm.get(['tankOwner'])!.value,
      tankModel: this.editForm.get(['tankModel'])!.value,
      defectRegion: this.editForm.get(['defectRegion'])!.value,
      defectStation: this.editForm.get(['defectStation'])!.value,
      defectDatetime: this.editForm.get(['defectDatetime'])!.value,
      defectDetails: this.editForm.get(['defectDetails'])!.value,
      repairRegion: this.editForm.get(['repairRegion'])!.value,
      repairStation: this.editForm.get(['repairStation'])!.value,
      repairDatatime: this.editForm.get(['repairDatatime'])!.value,
    };
  }
}
