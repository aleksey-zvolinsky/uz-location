import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITank } from '../tank.model';

@Component({
  selector: 'jhi-tank-detail',
  templateUrl: './tank-detail.component.html',
})
export class TankDetailComponent implements OnInit {
  tank: ITank | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tank }) => {
      this.tank = tank;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
