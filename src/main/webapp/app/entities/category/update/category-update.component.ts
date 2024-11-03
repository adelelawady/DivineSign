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
import { IValidationModel } from 'app/entities/validation-model/validation-model.model';
import { ValidationModelService } from 'app/entities/validation-model/service/validation-model.service';
import { CategoryService } from '../service/category.service';
import { ICategory } from '../category.model';
import { CategoryFormGroup, CategoryFormService } from './category-form.service';

@Component({
  standalone: true,
  selector: 'jhi-category-update',
  templateUrl: './category-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CategoryUpdateComponent implements OnInit {
  isSaving = false;
  category: ICategory | null = null;

  validationModelsSharedCollection: IValidationModel[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected categoryService = inject(CategoryService);
  protected categoryFormService = inject(CategoryFormService);
  protected validationModelService = inject(ValidationModelService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CategoryFormGroup = this.categoryFormService.createCategoryFormGroup();

  compareValidationModel = (o1: IValidationModel | null, o2: IValidationModel | null): boolean =>
    this.validationModelService.compareValidationModel(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ category }) => {
      this.category = category;
      if (category) {
        this.updateForm(category);
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
    const category = this.categoryFormService.getCategory(this.editForm);
    if (category.id !== null) {
      this.subscribeToSaveResponse(this.categoryService.update(category));
    } else {
      this.subscribeToSaveResponse(this.categoryService.create(category));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategory>>): void {
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

  protected updateForm(category: ICategory): void {
    this.category = category;
    this.categoryFormService.resetForm(this.editForm, category);

    this.validationModelsSharedCollection = this.validationModelService.addValidationModelToCollectionIfMissing<IValidationModel>(
      this.validationModelsSharedCollection,
      category.validationMethod,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.validationModelService
      .query()
      .pipe(map((res: HttpResponse<IValidationModel[]>) => res.body ?? []))
      .pipe(
        map((validationModels: IValidationModel[]) =>
          this.validationModelService.addValidationModelToCollectionIfMissing<IValidationModel>(
            validationModels,
            this.category?.validationMethod,
          ),
        ),
      )
      .subscribe((validationModels: IValidationModel[]) => (this.validationModelsSharedCollection = validationModels));
  }
}
