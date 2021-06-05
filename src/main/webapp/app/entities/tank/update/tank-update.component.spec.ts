jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TankService } from '../service/tank.service';
import { ITank, Tank } from '../tank.model';

import { TankUpdateComponent } from './tank-update.component';

describe('Component Tests', () => {
  describe('Tank Management Update Component', () => {
    let comp: TankUpdateComponent;
    let fixture: ComponentFixture<TankUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tankService: TankService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TankUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TankUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TankUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tankService = TestBed.inject(TankService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tank: ITank = { id: 456 };

        activatedRoute.data = of({ tank });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tank));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tank = { id: 123 };
        spyOn(tankService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tank });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tank }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tankService.update).toHaveBeenCalledWith(tank);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tank = new Tank();
        spyOn(tankService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tank });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tank }));
        saveSubject.complete();

        // THEN
        expect(tankService.create).toHaveBeenCalledWith(tank);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tank = { id: 123 };
        spyOn(tankService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tank });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tankService.update).toHaveBeenCalledWith(tank);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
