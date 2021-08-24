import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITank } from '../tank.model';
import { TankService } from '../service/tank.service';
import { TankDeleteDialogComponent } from '../delete/tank-delete-dialog.component';

@Component({
  selector: 'jhi-tank',
  templateUrl: './tank.component.html',
})
export class TankComponent implements OnInit {
  tanks?: ITank[];
  isLoading = false;

  constructor(protected tankService: TankService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.tankService.query().subscribe(
      (res: HttpResponse<ITank[]>) => {
        this.isLoading = false;
        this.tanks = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITank): number {
    return item.id!;
  }

  delete(tank: ITank): void {
    const modalRef = this.modalService.open(TankDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tank = tank;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
