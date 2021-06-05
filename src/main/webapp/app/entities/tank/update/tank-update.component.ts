import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITank, Tank } from '../tank.model';
import { TankService } from '../service/tank.service';

@Component({
  selector: 'jhi-tank-update',
  templateUrl: './tank-update.component.html',
})
export class TankUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tankNumber: [null, [Validators.required]],
    ownerName: [null, [Validators.required]],
    clientName: [null, [Validators.required]],
  });

  constructor(protected tankService: TankService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tank }) => {
      this.updateForm(tank);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tank = this.createFromForm();
    if (tank.id !== undefined) {
      this.subscribeToSaveResponse(this.tankService.update(tank));
    } else {
      this.subscribeToSaveResponse(this.tankService.create(tank));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITank>>): void {
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

  protected updateForm(tank: ITank): void {
    this.editForm.patchValue({
      id: tank.id,
      tankNumber: tank.tankNumber,
      ownerName: tank.ownerName,
      clientName: tank.clientName,
    });
  }

  protected createFromForm(): ITank {
    return {
      ...new Tank(),
      id: this.editForm.get(['id'])!.value,
      tankNumber: this.editForm.get(['tankNumber'])!.value,
      ownerName: this.editForm.get(['ownerName'])!.value,
      clientName: this.editForm.get(['clientName'])!.value,
    };
  }
}
