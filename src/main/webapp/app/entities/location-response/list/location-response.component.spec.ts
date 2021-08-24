import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LocationResponseService } from '../service/location-response.service';

import { LocationResponseComponent } from './location-response.component';

describe('Component Tests', () => {
  describe('LocationResponse Management Component', () => {
    let comp: LocationResponseComponent;
    let fixture: ComponentFixture<LocationResponseComponent>;
    let service: LocationResponseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LocationResponseComponent],
      })
        .overrideTemplate(LocationResponseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocationResponseComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LocationResponseService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
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
      expect(comp.locationResponses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
