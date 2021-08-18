import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MileageResponseDetailComponent } from './mileage-response-detail.component';

describe('Component Tests', () => {
  describe('MileageResponse Management Detail Component', () => {
    let comp: MileageResponseDetailComponent;
    let fixture: ComponentFixture<MileageResponseDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MileageResponseDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mileageResponse: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MileageResponseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MileageResponseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mileageResponse on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mileageResponse).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
