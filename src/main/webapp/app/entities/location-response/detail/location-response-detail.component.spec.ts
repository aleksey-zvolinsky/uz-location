import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LocationResponseDetailComponent } from './location-response-detail.component';

describe('Component Tests', () => {
  describe('LocationResponse Management Detail Component', () => {
    let comp: LocationResponseDetailComponent;
    let fixture: ComponentFixture<LocationResponseDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [LocationResponseDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ locationResponse: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(LocationResponseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LocationResponseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load locationResponse on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.locationResponse).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
