import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMileageResponse, MileageResponse } from '../mileage-response.model';
import { MileageResponseService } from '../service/mileage-response.service';

@Component({
  selector: 'jhi-mileage-response-update',
  templateUrl: './mileage-response-update.component.html',
})
export class MileageResponseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    responseDatetime: [],
    tankNumber: [],
    mileageCurrent: [],
    mileageDatetime: [],
    mileageRemain: [],
    mileageUpdateDatetime: [],
  });

  constructor(
    protected mileageResponseService: MileageResponseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mileageResponse }) => {
      this.updateForm(mileageResponse);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mileageResponse = this.createFromForm();
    if (mileageResponse.id !== undefined) {
      this.subscribeToSaveResponse(this.mileageResponseService.update(mileageResponse));
    } else {
      this.subscribeToSaveResponse(this.mileageResponseService.create(mileageResponse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMileageResponse>>): void {
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

  protected updateForm(mileageResponse: IMileageResponse): void {
    this.editForm.patchValue({
      id: mileageResponse.id,
      responseDatetime: mileageResponse.responseDatetime,
      tankNumber: mileageResponse.tankNumber,
      mileageCurrent: mileageResponse.mileageCurrent,
      mileageDatetime: mileageResponse.mileageDatetime,
      mileageRemain: mileageResponse.mileageRemain,
      mileageUpdateDatetime: mileageResponse.mileageUpdateDatetime,
    });
  }

  protected createFromForm(): IMileageResponse {
    return {
      ...new MileageResponse(),
      id: this.editForm.get(['id'])!.value,
      responseDatetime: this.editForm.get(['responseDatetime'])!.value,
      tankNumber: this.editForm.get(['tankNumber'])!.value,
      mileageCurrent: this.editForm.get(['mileageCurrent'])!.value,
      mileageDatetime: this.editForm.get(['mileageDatetime'])!.value,
      mileageRemain: this.editForm.get(['mileageRemain'])!.value,
      mileageUpdateDatetime: this.editForm.get(['mileageUpdateDatetime'])!.value,
    };
  }
}
