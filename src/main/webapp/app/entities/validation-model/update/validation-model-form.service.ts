import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IValidationModel, NewValidationModel } from '../validation-model.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IValidationModel for edit and NewValidationModelFormGroupInput for create.
 */
type ValidationModelFormGroupInput = IValidationModel | PartialWithRequiredKeyOf<NewValidationModel>;

type ValidationModelFormDefaults = Pick<NewValidationModel, 'id'>;

type ValidationModelFormGroupContent = {
  id: FormControl<IValidationModel['id'] | NewValidationModel['id']>;
  name: FormControl<IValidationModel['name']>;
  type: FormControl<IValidationModel['type']>;
};

export type ValidationModelFormGroup = FormGroup<ValidationModelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ValidationModelFormService {
  createValidationModelFormGroup(validationModel: ValidationModelFormGroupInput = { id: null }): ValidationModelFormGroup {
    const validationModelRawValue = {
      ...this.getFormDefaults(),
      ...validationModel,
    };
    return new FormGroup<ValidationModelFormGroupContent>({
      id: new FormControl(
        { value: validationModelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(validationModelRawValue.name),
      type: new FormControl(validationModelRawValue.type),
    });
  }

  getValidationModel(form: ValidationModelFormGroup): IValidationModel | NewValidationModel {
    return form.getRawValue() as IValidationModel | NewValidationModel;
  }

  resetForm(form: ValidationModelFormGroup, validationModel: ValidationModelFormGroupInput): void {
    const validationModelRawValue = { ...this.getFormDefaults(), ...validationModel };
    form.reset(
      {
        ...validationModelRawValue,
        id: { value: validationModelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ValidationModelFormDefaults {
    return {
      id: null,
    };
  }
}
