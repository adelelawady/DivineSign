import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IVerse, NewVerse } from '../verse.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVerse for edit and NewVerseFormGroupInput for create.
 */
type VerseFormGroupInput = IVerse | PartialWithRequiredKeyOf<NewVerse>;

type VerseFormDefaults = Pick<NewVerse, 'id'>;

type VerseFormGroupContent = {
  id: FormControl<IVerse['id'] | NewVerse['id']>;
  verse: FormControl<IVerse['verse']>;
  diacriticVerse: FormControl<IVerse['diacriticVerse']>;
  surah: FormControl<IVerse['surah']>;
  splendVerses: FormControl<IVerse['splendVerses']>;
};

export type VerseFormGroup = FormGroup<VerseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VerseFormService {
  createVerseFormGroup(verse: VerseFormGroupInput = { id: null }): VerseFormGroup {
    const verseRawValue = {
      ...this.getFormDefaults(),
      ...verse,
    };
    return new FormGroup<VerseFormGroupContent>({
      id: new FormControl(
        { value: verseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      verse: new FormControl(verseRawValue.verse),
      diacriticVerse: new FormControl(verseRawValue.diacriticVerse),
      surah: new FormControl(verseRawValue.surah),
      splendVerses: new FormControl(verseRawValue.splendVerses),
    });
  }

  getVerse(form: VerseFormGroup): IVerse | NewVerse {
    return form.getRawValue() as IVerse | NewVerse;
  }

  resetForm(form: VerseFormGroup, verse: VerseFormGroupInput): void {
    const verseRawValue = { ...this.getFormDefaults(), ...verse };
    form.reset(
      {
        ...verseRawValue,
        id: { value: verseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): VerseFormDefaults {
    return {
      id: null,
    };
  }
}
