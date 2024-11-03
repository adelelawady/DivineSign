import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISplend, NewSplend } from '../splend.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISplend for edit and NewSplendFormGroupInput for create.
 */
type SplendFormGroupInput = ISplend | PartialWithRequiredKeyOf<NewSplend>;

type SplendFormDefaults = Pick<NewSplend, 'id' | 'verified' | 'likedUsers' | 'dislikedSplends'>;

type SplendFormGroupContent = {
  id: FormControl<ISplend['id'] | NewSplend['id']>;
  title: FormControl<ISplend['title']>;
  content: FormControl<ISplend['content']>;
  likes: FormControl<ISplend['likes']>;
  dislikes: FormControl<ISplend['dislikes']>;
  verified: FormControl<ISplend['verified']>;
  category: FormControl<ISplend['category']>;
  verses: FormControl<ISplend['verses']>;
  likedUsers: FormControl<ISplend['likedUsers']>;
  dislikedSplends: FormControl<ISplend['dislikedSplends']>;
};

export type SplendFormGroup = FormGroup<SplendFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SplendFormService {
  createSplendFormGroup(splend: SplendFormGroupInput = { id: null }): SplendFormGroup {
    const splendRawValue = {
      ...this.getFormDefaults(),
      ...splend,
    };
    return new FormGroup<SplendFormGroupContent>({
      id: new FormControl(
        { value: splendRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(splendRawValue.title, {
        validators: [Validators.required],
      }),
      content: new FormControl(splendRawValue.content, {
        validators: [Validators.required],
      }),
      likes: new FormControl(splendRawValue.likes, {
        validators: [Validators.required],
      }),
      dislikes: new FormControl(splendRawValue.dislikes, {
        validators: [Validators.required],
      }),
      verified: new FormControl(splendRawValue.verified, {
        validators: [Validators.required],
      }),
      category: new FormControl(splendRawValue.category),
      verses: new FormControl(splendRawValue.verses),
      likedUsers: new FormControl(splendRawValue.likedUsers ?? []),
      dislikedSplends: new FormControl(splendRawValue.dislikedSplends ?? []),
    });
  }

  getSplend(form: SplendFormGroup): ISplend | NewSplend {
    return form.getRawValue() as ISplend | NewSplend;
  }

  resetForm(form: SplendFormGroup, splend: SplendFormGroupInput): void {
    const splendRawValue = { ...this.getFormDefaults(), ...splend };
    form.reset(
      {
        ...splendRawValue,
        id: { value: splendRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SplendFormDefaults {
    return {
      id: null,
      verified: false,
      likedUsers: [],
      dislikedSplends: [],
    };
  }
}
