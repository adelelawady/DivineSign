import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISplend } from 'app/entities/splend/splend.model';
import { SplendService } from 'app/entities/splend/service/splend.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IRequest } from '../request.model';
import { RequestService } from '../service/request.service';
import { RequestFormService } from './request-form.service';

import { RequestUpdateComponent } from './request-update.component';

describe('Request Management Update Component', () => {
  let comp: RequestUpdateComponent;
  let fixture: ComponentFixture<RequestUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let requestFormService: RequestFormService;
  let requestService: RequestService;
  let splendService: SplendService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RequestUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(RequestUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RequestUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    requestFormService = TestBed.inject(RequestFormService);
    requestService = TestBed.inject(RequestService);
    splendService = TestBed.inject(SplendService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Splend query and add missing value', () => {
      const request: IRequest = { id: 'CBA' };
      const splend: ISplend = { id: 'dba228b8-41de-4222-97f8-64088f9889d9' };
      request.splend = splend;

      const splendCollection: ISplend[] = [{ id: '0ba33d75-e3e5-46be-9ccd-aa317ab20cbd' }];
      jest.spyOn(splendService, 'query').mockReturnValue(of(new HttpResponse({ body: splendCollection })));
      const additionalSplends = [splend];
      const expectedCollection: ISplend[] = [...additionalSplends, ...splendCollection];
      jest.spyOn(splendService, 'addSplendToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ request });
      comp.ngOnInit();

      expect(splendService.query).toHaveBeenCalled();
      expect(splendService.addSplendToCollectionIfMissing).toHaveBeenCalledWith(
        splendCollection,
        ...additionalSplends.map(expect.objectContaining),
      );
      expect(comp.splendsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const request: IRequest = { id: 'CBA' };
      const user: IUser = { id: '565c6d01-fbf5-472b-b78b-b4f6d23ee636' };
      request.user = user;

      const userCollection: IUser[] = [{ id: 'fa39798a-4633-4139-87e8-86f6bd969488' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ request });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const request: IRequest = { id: 'CBA' };
      const splend: ISplend = { id: 'a5769f9a-435d-4cc9-89f7-0afeccc9547c' };
      request.splend = splend;
      const user: IUser = { id: '8e90f324-3032-4413-a5f1-7a4b7e823189' };
      request.user = user;

      activatedRoute.data = of({ request });
      comp.ngOnInit();

      expect(comp.splendsSharedCollection).toContain(splend);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.request).toEqual(request);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequest>>();
      const request = { id: 'ABC' };
      jest.spyOn(requestFormService, 'getRequest').mockReturnValue(request);
      jest.spyOn(requestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ request });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: request }));
      saveSubject.complete();

      // THEN
      expect(requestFormService.getRequest).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(requestService.update).toHaveBeenCalledWith(expect.objectContaining(request));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequest>>();
      const request = { id: 'ABC' };
      jest.spyOn(requestFormService, 'getRequest').mockReturnValue({ id: null });
      jest.spyOn(requestService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ request: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: request }));
      saveSubject.complete();

      // THEN
      expect(requestFormService.getRequest).toHaveBeenCalled();
      expect(requestService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRequest>>();
      const request = { id: 'ABC' };
      jest.spyOn(requestService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ request });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(requestService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSplend', () => {
      it('Should forward to splendService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(splendService, 'compareSplend');
        comp.compareSplend(entity, entity2);
        expect(splendService.compareSplend).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
