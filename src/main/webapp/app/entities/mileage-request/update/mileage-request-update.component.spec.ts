jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MileageRequestService } from '../service/mileage-request.service';
import { IMileageRequest, MileageRequest } from '../mileage-request.model';
import { IMileageResponse } from 'app/entities/mileage-response/mileage-response.model';
import { MileageResponseService } from 'app/entities/mileage-response/service/mileage-response.service';
import { ITank } from 'app/entities/tank/tank.model';
import { TankService } from 'app/entities/tank/service/tank.service';

import { MileageRequestUpdateComponent } from './mileage-request-update.component';

describe('Component Tests', () => {
  describe('MileageRequest Management Update Component', () => {
    let comp: MileageRequestUpdateComponent;
    let fixture: ComponentFixture<MileageRequestUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mileageRequestService: MileageRequestService;
    let mileageResponseService: MileageResponseService;
    let tankService: TankService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MileageRequestUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MileageRequestUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MileageRequestUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mileageRequestService = TestBed.inject(MileageRequestService);
      mileageResponseService = TestBed.inject(MileageResponseService);
      tankService = TestBed.inject(TankService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call mileageResponse query and add missing value', () => {
        const mileageRequest: IMileageRequest = { id: 456 };
        const mileageResponse: IMileageResponse = { id: 81086 };
        mileageRequest.mileageResponse = mileageResponse;

        const mileageResponseCollection: IMileageResponse[] = [{ id: 61161 }];
        jest.spyOn(mileageResponseService, 'query').mockReturnValue(of(new HttpResponse({ body: mileageResponseCollection })));
        const expectedCollection: IMileageResponse[] = [mileageResponse, ...mileageResponseCollection];
        jest.spyOn(mileageResponseService, 'addMileageResponseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ mileageRequest });
        comp.ngOnInit();

        expect(mileageResponseService.query).toHaveBeenCalled();
        expect(mileageResponseService.addMileageResponseToCollectionIfMissing).toHaveBeenCalledWith(
          mileageResponseCollection,
          mileageResponse
        );
        expect(comp.mileageResponsesCollection).toEqual(expectedCollection);
      });

      it('Should call Tank query and add missing value', () => {
        const mileageRequest: IMileageRequest = { id: 456 };
        const tank: ITank = { id: 72391 };
        mileageRequest.tank = tank;

        const tankCollection: ITank[] = [{ id: 76120 }];
        jest.spyOn(tankService, 'query').mockReturnValue(of(new HttpResponse({ body: tankCollection })));
        const additionalTanks = [tank];
        const expectedCollection: ITank[] = [...additionalTanks, ...tankCollection];
        jest.spyOn(tankService, 'addTankToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ mileageRequest });
        comp.ngOnInit();

        expect(tankService.query).toHaveBeenCalled();
        expect(tankService.addTankToCollectionIfMissing).toHaveBeenCalledWith(tankCollection, ...additionalTanks);
        expect(comp.tanksSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const mileageRequest: IMileageRequest = { id: 456 };
        const mileageResponse: IMileageResponse = { id: 45481 };
        mileageRequest.mileageResponse = mileageResponse;
        const tank: ITank = { id: 8315 };
        mileageRequest.tank = tank;

        activatedRoute.data = of({ mileageRequest });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mileageRequest));
        expect(comp.mileageResponsesCollection).toContain(mileageResponse);
        expect(comp.tanksSharedCollection).toContain(tank);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MileageRequest>>();
        const mileageRequest = { id: 123 };
        jest.spyOn(mileageRequestService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mileageRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mileageRequest }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mileageRequestService.update).toHaveBeenCalledWith(mileageRequest);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MileageRequest>>();
        const mileageRequest = new MileageRequest();
        jest.spyOn(mileageRequestService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mileageRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mileageRequest }));
        saveSubject.complete();

        // THEN
        expect(mileageRequestService.create).toHaveBeenCalledWith(mileageRequest);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MileageRequest>>();
        const mileageRequest = { id: 123 };
        jest.spyOn(mileageRequestService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mileageRequest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mileageRequestService.update).toHaveBeenCalledWith(mileageRequest);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMileageResponseById', () => {
        it('Should return tracked MileageResponse primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMileageResponseById(0, entity);
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
