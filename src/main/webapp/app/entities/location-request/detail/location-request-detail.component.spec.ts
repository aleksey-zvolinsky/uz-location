import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LocationRequestDetailComponent } from './location-request-detail.component';

describe('Component Tests', () => {
  describe('LocationRequest Management Detail Component', () => {
    let comp: LocationRequestDetailComponent;
    let fixture: ComponentFixture<LocationRequestDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LocationRequestDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ locationRequest: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LocationRequestDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LocationRequestDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load locationRequest on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.locationRequest).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
