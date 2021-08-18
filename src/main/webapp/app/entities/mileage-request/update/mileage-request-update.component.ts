import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMileageRequest, MileageRequest } from '../mileage-request.model';
import { MileageRequestService } from '../service/mileage-request.service';
import { IMileageResponse } from 'app/entities/mileage-response/mileage-response.model';
import { MileageResponseService } from 'app/entities/mileage-response/service/mileage-response.service';
import { ITank } from 'app/entities/tank/tank.model';
import { TankService } from 'app/entities/tank/service/tank.service';

@Component({
  selector: 'jhi-mileage-request-update',
  templateUrl: './mileage-request-update.component.html',
})
export class MileageRequestUpdateComponent implements OnInit {
  isSaving = false;

  mileageResponsesCollection: IMileageResponse[] = [];
  tanksSharedCollection: ITank[] = [];

  editForm = this.fb.group({
    id: [],
    requestDatetime: [],
    tankNumbers: [],
    mileageResponse: [],
    tank: [],
  });

  constructor(
    protected mileageRequestService: MileageRequestService,
    protected mileageResponseService: MileageResponseService,
    protected tankService: TankService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mileageRequest }) => {
      this.updateForm(mileageRequest);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mileageRequest = this.createFromForm();
    if (mileageRequest.id !== undefined) {
      this.subscribeToSaveResponse(this.mileageRequestService.update(mileageRequest));
    } else {
      this.subscribeToSaveResponse(this.mileageRequestService.create(mileageRequest));
    }
  }

  trackMileageResponseById(index: number, item: IMileageResponse): number {
    return item.id!;
  }

  trackTankById(index: number, item: ITank): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMileageRequest>>): void {
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

  protected updateForm(mileageRequest: IMileageRequest): void {
    this.editForm.patchValue({
      id: mileageRequest.id,
      requestDatetime: mileageRequest.requestDatetime,
      tankNumbers: mileageRequest.tankNumbers,
      mileageResponse: mileageRequest.mileageResponse,
      tank: mileageRequest.tank,
    });

    this.mileageResponsesCollection = this.mileageResponseService.addMileageResponseToCollectionIfMissing(
      this.mileageResponsesCollection,
      mileageRequest.mileageResponse
    );
    this.tanksSharedCollection = this.tankService.addTankToCollectionIfMissing(this.tanksSharedCollection, mileageRequest.tank);
  }

  protected loadRelationshipsOptions(): void {
    this.mileageResponseService
      .query({ 'mileageRequestId.specified': 'false' })
      .pipe(map((res: HttpResponse<IMileageResponse[]>) => res.body ?? []))
      .pipe(
        map((mileageResponses: IMileageResponse[]) =>
          this.mileageResponseService.addMileageResponseToCollectionIfMissing(mileageResponses, this.editForm.get('mileageResponse')!.value)
        )
      )
      .subscribe((mileageResponses: IMileageResponse[]) => (this.mileageResponsesCollection = mileageResponses));

    this.tankService
      .query()
      .pipe(map((res: HttpResponse<ITank[]>) => res.body ?? []))
      .pipe(map((tanks: ITank[]) => this.tankService.addTankToCollectionIfMissing(tanks, this.editForm.get('tank')!.value)))
      .subscribe((tanks: ITank[]) => (this.tanksSharedCollection = tanks));
  }

  protected createFromForm(): IMileageRequest {
    return {
      ...new MileageRequest(),
      id: this.editForm.get(['id'])!.value,
      requestDatetime: this.editForm.get(['requestDatetime'])!.value,
      tankNumbers: this.editForm.get(['tankNumbers'])!.value,
      mileageResponse: this.editForm.get(['mileageResponse'])!.value,
      tank: this.editForm.get(['tank'])!.value,
    };
  }
}
