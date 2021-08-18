import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MileageRequestDetailComponent } from './mileage-request-detail.component';

describe('Component Tests', () => {
  describe('MileageRequest Management Detail Component', () => {
    let comp: MileageRequestDetailComponent;
    let fixture: ComponentFixture<MileageRequestDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [MileageRequestDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ mileageRequest: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(MileageRequestDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MileageRequestDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mileageRequest on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mileageRequest).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
