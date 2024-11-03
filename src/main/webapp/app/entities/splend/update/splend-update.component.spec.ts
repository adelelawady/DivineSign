import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ISplendVerses } from 'app/entities/splend-verses/splend-verses.model';
import { SplendVersesService } from 'app/entities/splend-verses/service/splend-verses.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ISplend } from '../splend.model';
import { SplendService } from '../service/splend.service';
import { SplendFormService } from './splend-form.service';

import { SplendUpdateComponent } from './splend-update.component';

describe('Splend Management Update Component', () => {
  let comp: SplendUpdateComponent;
  let fixture: ComponentFixture<SplendUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let splendFormService: SplendFormService;
  let splendService: SplendService;
  let categoryService: CategoryService;
  let splendVersesService: SplendVersesService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SplendUpdateComponent],
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
      .overrideTemplate(SplendUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SplendUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    splendFormService = TestBed.inject(SplendFormService);
    splendService = TestBed.inject(SplendService);
    categoryService = TestBed.inject(CategoryService);
    splendVersesService = TestBed.inject(SplendVersesService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Category query and add missing value', () => {
      const splend: ISplend = { id: 'CBA' };
      const category: ICategory = { id: 'e7792b01-66fe-414a-b99e-e14bc3cb9e3c' };
      splend.category = category;

      const categoryCollection: ICategory[] = [{ id: '46d0f9ab-414f-4d05-b8a5-5b9ca166c7d7' }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ splend });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        categoryCollection,
        ...additionalCategories.map(expect.objectContaining),
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call SplendVerses query and add missing value', () => {
      const splend: ISplend = { id: 'CBA' };
      const verses: ISplendVerses = { id: '8912f26b-392d-4101-9cf3-6a0181530338' };
      splend.verses = verses;

      const splendVersesCollection: ISplendVerses[] = [{ id: 'd927a15c-d46d-4707-8277-b524fd34c458' }];
      jest.spyOn(splendVersesService, 'query').mockReturnValue(of(new HttpResponse({ body: splendVersesCollection })));
      const additionalSplendVerses = [verses];
      const expectedCollection: ISplendVerses[] = [...additionalSplendVerses, ...splendVersesCollection];
      jest.spyOn(splendVersesService, 'addSplendVersesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ splend });
      comp.ngOnInit();

      expect(splendVersesService.query).toHaveBeenCalled();
      expect(splendVersesService.addSplendVersesToCollectionIfMissing).toHaveBeenCalledWith(
        splendVersesCollection,
        ...additionalSplendVerses.map(expect.objectContaining),
      );
      expect(comp.splendVersesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const splend: ISplend = { id: 'CBA' };
      const likedUsers: IUser[] = [{ id: 'b2a10924-6941-43b5-b838-e81eac3f84fc' }];
      splend.likedUsers = likedUsers;
      const dislikedSplends: IUser[] = [{ id: 'a5d5c63c-aa5c-4730-be37-c3341187fd02' }];
      splend.dislikedSplends = dislikedSplends;

      const userCollection: IUser[] = [{ id: '2153c0b4-346f-4326-b156-450b131cd1bd' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [...likedUsers, ...dislikedSplends];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ splend });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const splend: ISplend = { id: 'CBA' };
      const category: ICategory = { id: '4824f259-e2af-4000-857c-ce2bc6a5b280' };
      splend.category = category;
      const verses: ISplendVerses = { id: '15755095-4933-4b59-8d8f-5b8621bd4596' };
      splend.verses = verses;
      const likedUsers: IUser = { id: '7d01676b-2f61-4224-aa99-91a1a7fd7200' };
      splend.likedUsers = [likedUsers];
      const dislikedSplend: IUser = { id: 'e648e65a-8aef-4720-a91b-108d8e6f4582' };
      splend.dislikedSplends = [dislikedSplend];

      activatedRoute.data = of({ splend });
      comp.ngOnInit();

      expect(comp.categoriesSharedCollection).toContain(category);
      expect(comp.splendVersesSharedCollection).toContain(verses);
      expect(comp.usersSharedCollection).toContain(likedUsers);
      expect(comp.usersSharedCollection).toContain(dislikedSplend);
      expect(comp.splend).toEqual(splend);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISplend>>();
      const splend = { id: 'ABC' };
      jest.spyOn(splendFormService, 'getSplend').mockReturnValue(splend);
      jest.spyOn(splendService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ splend });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: splend }));
      saveSubject.complete();

      // THEN
      expect(splendFormService.getSplend).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(splendService.update).toHaveBeenCalledWith(expect.objectContaining(splend));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISplend>>();
      const splend = { id: 'ABC' };
      jest.spyOn(splendFormService, 'getSplend').mockReturnValue({ id: null });
      jest.spyOn(splendService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ splend: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: splend }));
      saveSubject.complete();

      // THEN
      expect(splendFormService.getSplend).toHaveBeenCalled();
      expect(splendService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISplend>>();
      const splend = { id: 'ABC' };
      jest.spyOn(splendService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ splend });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(splendService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategory', () => {
      it('Should forward to categoryService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(categoryService, 'compareCategory');
        comp.compareCategory(entity, entity2);
        expect(categoryService.compareCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSplendVerses', () => {
      it('Should forward to splendVersesService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(splendVersesService, 'compareSplendVerses');
        comp.compareSplendVerses(entity, entity2);
        expect(splendVersesService.compareSplendVerses).toHaveBeenCalledWith(entity, entity2);
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
