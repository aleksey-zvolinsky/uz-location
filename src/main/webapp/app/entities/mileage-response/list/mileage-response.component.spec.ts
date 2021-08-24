import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { MileageResponseService } from '../service/mileage-response.service';

import { MileageResponseComponent } from './mileage-response.component';

describe('Component Tests', () => {
  describe('MileageResponse Management Component', () => {
    let comp: MileageResponseComponent;
    let fixture: ComponentFixture<MileageResponseComponent>;
    let service: MileageResponseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MileageResponseComponent],
      })
        .overrideTemplate(MileageResponseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MileageResponseComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(MileageResponseService);

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
      expect(comp.mileageResponses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
