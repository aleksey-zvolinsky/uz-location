import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LocationRequestService } from '../service/location-request.service';

import { LocationRequestComponent } from './location-request.component';

describe('Component Tests', () => {
  describe('LocationRequest Management Component', () => {
    let comp: LocationRequestComponent;
    let fixture: ComponentFixture<LocationRequestComponent>;
    let service: LocationRequestService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LocationRequestComponent],
      })
        .overrideTemplate(LocationRequestComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocationRequestComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LocationRequestService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.locationRequests?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
