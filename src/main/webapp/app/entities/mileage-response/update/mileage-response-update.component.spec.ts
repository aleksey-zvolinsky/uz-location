jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MileageResponseService } from '../service/mileage-response.service';
import { IMileageResponse, MileageResponse } from '../mileage-response.model';

import { MileageResponseUpdateComponent } from './mileage-response-update.component';

describe('Component Tests', () => {
  describe('MileageResponse Management Update Component', () => {
    let comp: MileageResponseUpdateComponent;
    let fixture: ComponentFixture<MileageResponseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let mileageResponseService: MileageResponseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MileageResponseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MileageResponseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MileageResponseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      mileageResponseService = TestBed.inject(MileageResponseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const mileageResponse: IMileageResponse = { id: 456 };

        activatedRoute.data = of({ mileageResponse });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(mileageResponse));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MileageResponse>>();
        const mileageResponse = { id: 123 };
        jest.spyOn(mileageResponseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mileageResponse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mileageResponse }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(mileageResponseService.update).toHaveBeenCalledWith(mileageResponse);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MileageResponse>>();
        const mileageResponse = new MileageResponse();
        jest.spyOn(mileageResponseService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mileageResponse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: mileageResponse }));
        saveSubject.complete();

        // THEN
        expect(mileageResponseService.create).toHaveBeenCalledWith(mileageResponse);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<MileageResponse>>();
        const mileageResponse = { id: 123 };
        jest.spyOn(mileageResponseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ mileageResponse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(mileageResponseService.update).toHaveBeenCalledWith(mileageResponse);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
