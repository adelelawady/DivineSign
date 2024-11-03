import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IValidationModel, NewValidationModel } from '../validation-model.model';

export type PartialUpdateValidationModel = Partial<IValidationModel> & Pick<IValidationModel, 'id'>;

export type EntityResponseType = HttpResponse<IValidationModel>;
export type EntityArrayResponseType = HttpResponse<IValidationModel[]>;

@Injectable({ providedIn: 'root' })
export class ValidationModelService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/validation-models');

  create(validationModel: NewValidationModel): Observable<EntityResponseType> {
    return this.http.post<IValidationModel>(this.resourceUrl, validationModel, { observe: 'response' });
  }

  update(validationModel: IValidationModel): Observable<EntityResponseType> {
    return this.http.put<IValidationModel>(`${this.resourceUrl}/${this.getValidationModelIdentifier(validationModel)}`, validationModel, {
      observe: 'response',
    });
  }

  partialUpdate(validationModel: PartialUpdateValidationModel): Observable<EntityResponseType> {
    return this.http.patch<IValidationModel>(`${this.resourceUrl}/${this.getValidationModelIdentifier(validationModel)}`, validationModel, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IValidationModel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IValidationModel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getValidationModelIdentifier(validationModel: Pick<IValidationModel, 'id'>): string {
    return validationModel.id;
  }

  compareValidationModel(o1: Pick<IValidationModel, 'id'> | null, o2: Pick<IValidationModel, 'id'> | null): boolean {
    return o1 && o2 ? this.getValidationModelIdentifier(o1) === this.getValidationModelIdentifier(o2) : o1 === o2;
  }

  addValidationModelToCollectionIfMissing<Type extends Pick<IValidationModel, 'id'>>(
    validationModelCollection: Type[],
    ...validationModelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const validationModels: Type[] = validationModelsToCheck.filter(isPresent);
    if (validationModels.length > 0) {
      const validationModelCollectionIdentifiers = validationModelCollection.map(validationModelItem =>
        this.getValidationModelIdentifier(validationModelItem),
      );
      const validationModelsToAdd = validationModels.filter(validationModelItem => {
        const validationModelIdentifier = this.getValidationModelIdentifier(validationModelItem);
        if (validationModelCollectionIdentifiers.includes(validationModelIdentifier)) {
          return false;
        }
        validationModelCollectionIdentifiers.push(validationModelIdentifier);
        return true;
      });
      return [...validationModelsToAdd, ...validationModelCollection];
    }
    return validationModelCollection;
  }
}
