jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LocationResponseService } from '../service/location-response.service';
import { ILocationResponse, LocationResponse } from '../location-response.model';

import { LocationResponseUpdateComponent } from './location-response-update.component';

describe('Component Tests', () => {
  describe('LocationResponse Management Update Component', () => {
    let comp: LocationResponseUpdateComponent;
    let fixture: ComponentFixture<LocationResponseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let locationResponseService: LocationResponseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LocationResponseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LocationResponseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocationResponseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      locationResponseService = TestBed.inject(LocationResponseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const locationResponse: ILocationResponse = { id: 456 };

        activatedRoute.data = of({ locationResponse });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(locationResponse));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const locationResponse = { id: 123 };
        spyOn(locationResponseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ locationResponse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: locationResponse }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(locationResponseService.update).toHaveBeenCalledWith(locationResponse);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const locationResponse = new LocationResponse();
        spyOn(locationResponseService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ locationResponse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: locationResponse }));
        saveSubject.complete();

        // THEN
        expect(locationResponseService.create).toHaveBeenCalledWith(locationResponse);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const locationResponse = { id: 123 };
        spyOn(locationResponseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ locationResponse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(locationResponseService.update).toHaveBeenCalledWith(locationResponse);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
