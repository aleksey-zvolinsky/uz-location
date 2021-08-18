import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TankDetailComponent } from './tank-detail.component';

describe('Component Tests', () => {
  describe('Tank Management Detail Component', () => {
    let comp: TankDetailComponent;
    let fixture: ComponentFixture<TankDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TankDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tank: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TankDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TankDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tank on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tank).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
