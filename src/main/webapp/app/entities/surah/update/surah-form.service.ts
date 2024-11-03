import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISurah, NewSurah } from '../surah.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISurah for edit and NewSurahFormGroupInput for create.
 */
type SurahFormGroupInput = ISurah | PartialWithRequiredKeyOf<NewSurah>;

type SurahFormDefaults = Pick<NewSurah, 'id'>;

type SurahFormGroupContent = {
  id: FormControl<ISurah['id'] | NewSurah['id']>;
  name: FormControl<ISurah['name']>;
  transliteration: FormControl<ISurah['transliteration']>;
  type: FormControl<ISurah['type']>;
  totalVerses: FormControl<ISurah['totalVerses']>;
};

export type SurahFormGroup = FormGroup<SurahFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SurahFormService {
  createSurahFormGroup(surah: SurahFormGroupInput = { id: null }): SurahFormGroup {
    const surahRawValue = {
      ...this.getFormDefaults(),
      ...surah,
    };
    return new FormGroup<SurahFormGroupContent>({
      id: new FormControl(
        { value: surahRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(surahRawValue.name),
      transliteration: new FormControl(surahRawValue.transliteration),
      type: new FormControl(surahRawValue.type),
      totalVerses: new FormControl(surahRawValue.totalVerses),
    });
  }

  getSurah(form: SurahFormGroup): ISurah | NewSurah {
    return form.getRawValue() as ISurah | NewSurah;
  }

  resetForm(form: SurahFormGroup, surah: SurahFormGroupInput): void {
    const surahRawValue = { ...this.getFormDefaults(), ...surah };
    form.reset(
      {
        ...surahRawValue,
        id: { value: surahRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SurahFormDefaults {
    return {
      id: null,
    };
  }
}
