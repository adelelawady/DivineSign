import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRequest, NewRequest } from '../request.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRequest for edit and NewRequestFormGroupInput for create.
 */
type RequestFormGroupInput = IRequest | PartialWithRequiredKeyOf<NewRequest>;

type RequestFormDefaults = Pick<NewRequest, 'id' | 'verified' | 'deleted'>;

type RequestFormGroupContent = {
  id: FormControl<IRequest['id'] | NewRequest['id']>;
  title: FormControl<IRequest['title']>;
  content: FormControl<IRequest['content']>;
  verified: FormControl<IRequest['verified']>;
  deleted: FormControl<IRequest['deleted']>;
  status: FormControl<IRequest['status']>;
  splend: FormControl<IRequest['splend']>;
  user: FormControl<IRequest['user']>;
};

export type RequestFormGroup = FormGroup<RequestFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RequestFormService {
  createRequestFormGroup(request: RequestFormGroupInput = { id: null }): RequestFormGroup {
    const requestRawValue = {
      ...this.getFormDefaults(),
      ...request,
    };
    return new FormGroup<RequestFormGroupContent>({
      id: new FormControl(
        { value: requestRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(requestRawValue.title, {
        validators: [Validators.required],
      }),
      content: new FormControl(requestRawValue.content, {
        validators: [Validators.required],
      }),
      verified: new FormControl(requestRawValue.verified, {
        validators: [Validators.required],
      }),
      deleted: new FormControl(requestRawValue.deleted),
      status: new FormControl(requestRawValue.status),
      splend: new FormControl(requestRawValue.splend),
      user: new FormControl(requestRawValue.user),
    });
  }

  getRequest(form: RequestFormGroup): IRequest | NewRequest {
    return form.getRawValue() as IRequest | NewRequest;
  }

  resetForm(form: RequestFormGroup, request: RequestFormGroupInput): void {
    const requestRawValue = { ...this.getFormDefaults(), ...request };
    form.reset(
      {
        ...requestRawValue,
        id: { value: requestRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RequestFormDefaults {
    return {
      id: null,
      verified: false,
      deleted: false,
    };
  }
}
