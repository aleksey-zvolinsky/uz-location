import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TankService } from '../service/tank.service';

import { TankComponent } from './tank.component';

describe('Component Tests', () => {
  describe('Tank Management Component', () => {
    let comp: TankComponent;
    let fixture: ComponentFixture<TankComponent>;
    let service: TankService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TankComponent],
      })
        .overrideTemplate(TankComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TankComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TankService);

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
      expect(comp.tanks?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
