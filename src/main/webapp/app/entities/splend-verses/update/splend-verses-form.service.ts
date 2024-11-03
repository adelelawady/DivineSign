import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISplendVerses, NewSplendVerses } from '../splend-verses.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISplendVerses for edit and NewSplendVersesFormGroupInput for create.
 */
type SplendVersesFormGroupInput = ISplendVerses | PartialWithRequiredKeyOf<NewSplendVerses>;

type SplendVersesFormDefaults = Pick<NewSplendVerses, 'id'>;

type SplendVersesFormGroupContent = {
  id: FormControl<ISplendVerses['id'] | NewSplendVerses['id']>;
  word: FormControl<ISplendVerses['word']>;
  number: FormControl<ISplendVerses['number']>;
  condition: FormControl<ISplendVerses['condition']>;
  validationMethod: FormControl<ISplendVerses['validationMethod']>;
};

export type SplendVersesFormGroup = FormGroup<SplendVersesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SplendVersesFormService {
  createSplendVersesFormGroup(splendVerses: SplendVersesFormGroupInput = { id: null }): SplendVersesFormGroup {
    const splendVersesRawValue = {
      ...this.getFormDefaults(),
      ...splendVerses,
    };
    return new FormGroup<SplendVersesFormGroupContent>({
      id: new FormControl(
        { value: splendVersesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      word: new FormControl(splendVersesRawValue.word),
      number: new FormControl(splendVersesRawValue.number),
      condition: new FormControl(splendVersesRawValue.condition),
      validationMethod: new FormControl(splendVersesRawValue.validationMethod),
    });
  }

  getSplendVerses(form: SplendVersesFormGroup): ISplendVerses | NewSplendVerses {
    return form.getRawValue() as ISplendVerses | NewSplendVerses;
  }

  resetForm(form: SplendVersesFormGroup, splendVerses: SplendVersesFormGroupInput): void {
    const splendVersesRawValue = { ...this.getFormDefaults(), ...splendVerses };
    form.reset(
      {
        ...splendVersesRawValue,
        id: { value: splendVersesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SplendVersesFormDefaults {
    return {
      id: null,
    };
  }
}
