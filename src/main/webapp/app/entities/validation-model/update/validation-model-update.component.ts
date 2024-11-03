import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IValidationModel } from '../validation-model.model';
import { ValidationModelService } from '../service/validation-model.service';
import { ValidationModelFormGroup, ValidationModelFormService } from './validation-model-form.service';

@Component({
  standalone: true,
  selector: 'jhi-validation-model-update',
  templateUrl: './validation-model-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ValidationModelUpdateComponent implements OnInit {
  isSaving = false;
  validationModel: IValidationModel | null = null;

  protected validationModelService = inject(ValidationModelService);
  protected validationModelFormService = inject(ValidationModelFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ValidationModelFormGroup = this.validationModelFormService.createValidationModelFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ validationModel }) => {
      this.validationModel = validationModel;
      if (validationModel) {
        this.updateForm(validationModel);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const validationModel = this.validationModelFormService.getValidationModel(this.editForm);
    if (validationModel.id !== null) {
      this.subscribeToSaveResponse(this.validationModelService.update(validationModel));
    } else {
      this.subscribeToSaveResponse(this.validationModelService.create(validationModel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IValidationModel>>): void {
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

  protected updateForm(validationModel: IValidationModel): void {
    this.validationModel = validationModel;
    this.validationModelFormService.resetForm(this.editForm, validationModel);
  }
}
