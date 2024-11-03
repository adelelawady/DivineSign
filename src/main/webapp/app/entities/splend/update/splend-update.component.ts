import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { ISplendVerses } from 'app/entities/splend-verses/splend-verses.model';
import { SplendVersesService } from 'app/entities/splend-verses/service/splend-verses.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { SplendService } from '../service/splend.service';
import { ISplend } from '../splend.model';
import { SplendFormGroup, SplendFormService } from './splend-form.service';

@Component({
  standalone: true,
  selector: 'jhi-splend-update',
  templateUrl: './splend-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SplendUpdateComponent implements OnInit {
  isSaving = false;
  splend: ISplend | null = null;

  categoriesSharedCollection: ICategory[] = [];
  splendVersesSharedCollection: ISplendVerses[] = [];
  usersSharedCollection: IUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected splendService = inject(SplendService);
  protected splendFormService = inject(SplendFormService);
  protected categoryService = inject(CategoryService);
  protected splendVersesService = inject(SplendVersesService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SplendFormGroup = this.splendFormService.createSplendFormGroup();

  compareCategory = (o1: ICategory | null, o2: ICategory | null): boolean => this.categoryService.compareCategory(o1, o2);

  compareSplendVerses = (o1: ISplendVerses | null, o2: ISplendVerses | null): boolean =>
    this.splendVersesService.compareSplendVerses(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ splend }) => {
      this.splend = splend;
      if (splend) {
        this.updateForm(splend);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('divineSignApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const splend = this.splendFormService.getSplend(this.editForm);
    if (splend.id !== null) {
      this.subscribeToSaveResponse(this.splendService.update(splend));
    } else {
      this.subscribeToSaveResponse(this.splendService.create(splend));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISplend>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(splend: ISplend): void {
    this.splend = splend;
    this.splendFormService.resetForm(this.editForm, splend);

    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing<ICategory>(
      this.categoriesSharedCollection,
      splend.category,
    );
    this.splendVersesSharedCollection = this.splendVersesService.addSplendVersesToCollectionIfMissing<ISplendVerses>(
      this.splendVersesSharedCollection,
      splend.verses,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
      this.usersSharedCollection,
      ...(splend.likedUsers ?? []),
      ...(splend.dislikedSplends ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, this.splend?.category),
        ),
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.splendVersesService
      .query()
      .pipe(map((res: HttpResponse<ISplendVerses[]>) => res.body ?? []))
      .pipe(
        map((splendVerses: ISplendVerses[]) =>
          this.splendVersesService.addSplendVersesToCollectionIfMissing<ISplendVerses>(splendVerses, this.splend?.verses),
        ),
      )
      .subscribe((splendVerses: ISplendVerses[]) => (this.splendVersesSharedCollection = splendVerses));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(
            users,
            ...(this.splend?.likedUsers ?? []),
            ...(this.splend?.dislikedSplends ?? []),
          ),
        ),
      )
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
