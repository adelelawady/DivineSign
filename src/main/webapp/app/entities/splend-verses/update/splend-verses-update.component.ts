import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IValidationModel } from 'app/entities/validation-model/validation-model.model';
import { ValidationModelService } from 'app/entities/validation-model/service/validation-model.service';
import { ISplendVerses } from '../splend-verses.model';
import { SplendVersesService } from '../service/splend-verses.service';
import { SplendVersesFormGroup, SplendVersesFormService } from './splend-verses-form.service';

@Component({
  standalone: true,
  selector: 'jhi-splend-verses-update',
  templateUrl: './splend-verses-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SplendVersesUpdateComponent implements OnInit {
  isSaving = false;
  splendVerses: ISplendVerses | null = null;

  validationModelsSharedCollection: IValidationModel[] = [];

  protected splendVersesService = inject(SplendVersesService);
  protected splendVersesFormService = inject(SplendVersesFormService);
  protected validationModelService = inject(ValidationModelService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SplendVersesFormGroup = this.splendVersesFormService.createSplendVersesFormGroup();

  compareValidationModel = (o1: IValidationModel | null, o2: IValidationModel | null): boolean =>
    this.validationModelService.compareValidationModel(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ splendVerses }) => {
      this.splendVerses = splendVerses;
      if (splendVerses) {
        this.updateForm(splendVerses);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const splendVerses = this.splendVersesFormService.getSplendVerses(this.editForm);
    if (splendVerses.id !== null) {
      this.subscribeToSaveResponse(this.splendVersesService.update(splendVerses));
    } else {
      this.subscribeToSaveResponse(this.splendVersesService.create(splendVerses));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISplendVerses>>): void {
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

  protected updateForm(splendVerses: ISplendVerses): void {
    this.splendVerses = splendVerses;
    this.splendVersesFormService.resetForm(this.editForm, splendVerses);

    this.validationModelsSharedCollection = this.validationModelService.addValidationModelToCollectionIfMissing<IValidationModel>(
      this.validationModelsSharedCollection,
      splendVerses.validationMethod,
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
            this.splendVerses?.validationMethod,
          ),
        ),
      )
      .subscribe((validationModels: IValidationModel[]) => (this.validationModelsSharedCollection = validationModels));
  }
}
