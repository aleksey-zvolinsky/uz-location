jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LocationRequestService } from '../service/location-request.service';
import { ILocationRequest, LocationRequest } from '../location-request.model';
import { ILocationResponse } from 'app/entities/location-response/location-response.model';
import { LocationResponseService } from 'app/entities/location-response/service/location-response.service';
import { ITank } from 'app/entities/tank/tank.model';
import { TankService } from 'app/entities/tank/service/tank.service';

import { LocationRequestUpdateComponent } from './location-request-update.component';

describe('Component Tests', () => {
  describe('LocationRequest Management Update Component', () => {
    let comp: LocationRequestUpdateComponent;
    let fixture: ComponentFixture<LocationRequestUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let locationRequestService: LocationRequestService;
    let locationResponseService: LocationResponseService;
    let tankService: TankService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LocationRequestUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LocationRequestUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocationRequestUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      locationRequestService = TestBed.inject(LocationRequestService);
      locationResponseService = TestBed.inject(LocationResponseService);
      tankService = TestBed.inject(TankService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call locationResponse query and add missing value', () => {
        const locationRequest: ILocationRequest = { id: 456 };
        const locationResponse: ILocationResponse = { id: 57690 };
        locationRequest.locationResponse = locationResponse;

        const locationResponseCollection: ILocationResponse[] = [{ id: 92240 }];
        jest.spyOn(locationResponseService, 'query').mockReturnValue(of(new HttpResponse({ body: locationResponseCollection })));
        const expectedCollection: ILocationResponse[] = [locationResponse, ...locationResponseCollection];
        jest.spyOn(locationResponseService, 'addLocationResponseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ locationRequest });
        comp.ngOnInit();

        expect(locationResponseService.query).toHaveBeenCalled();
        expect(locationResponseService.addLocationResponseToCollectionIfMissing).toHaveBeenCalledWith(
          locationResponseCollection,
          locationResponse
        );
        expect(comp.locationResponsesCollection).toEqual(expectedCollection);
      });

      it('Should call Tank query and add missing value', () => {
        const locationRequest: ILocationRequest = { id: 456 };
        const tank: ITank = { id: 11746 };
        locationRequest.tank = tank;

        const tankCollection: ITank[] = [{ id: 12891 }];
        jest.spyOn(tankService, 'query').mockReturnValue(of(new HttpResponse({ body: tankCollection })));
        const additionalTanks = [tank];
        const expectedCollection: ITank[] = [...additionalTanks, ...tankCollection];
        jest.spyOn(tankService, 'addTankToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ locationRequest });
        comp.ngOnInit();

        expect(tankService.query).toHaveBeenCalled();
        expect(tankService.addTankToCollectionIfMissing).toHaveBeenCalledWith(tankCollection, ...additionalTanks);
        expect(comp.tanksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const locationRequest: ILocationRequest = { id: 456 };
        const locationResponse: ILocationResponse = { id: 35622 };
        locationRequest.locationResponse = locationResponse;
        const tank: ITank = { id: 16754 };
        locationRequest.tank = tank;

        activatedRoute.data = of({ locationRequest });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(locationRequest));
        expect(comp.locationResponsesCollection).toContain(locationResponse);
        expect(comp.tanksSharedCollection).toContain(tank);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LocationRequest>>();
        const locationRequest = { id: 123 };
        jest.spyOn(locationRequestService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ locationRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: locationRequest }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(locationRequestService.update).toHaveBeenCalledWith(locationRequest);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LocationRequest>>();
        const locationRequest = new LocationRequest();
        jest.spyOn(locationRequestService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ locationRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: locationRequest }));
        saveSubject.complete();

        // THEN
        expect(locationRequestService.create).toHaveBeenCalledWith(locationRequest);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LocationRequest>>();
        const locationRequest = { id: 123 };
        jest.spyOn(locationRequestService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ locationRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(locationRequestService.update).toHaveBeenCalledWith(locationRequest);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLocationResponseById', () => {
        it('Should return tracked LocationResponse primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLocationResponseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTankById', () => {
        it('Should return tracked Tank primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTankById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
