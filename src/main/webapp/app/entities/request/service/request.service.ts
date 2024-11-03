import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRequest, NewRequest } from '../request.model';

export type PartialUpdateRequest = Partial<IRequest> & Pick<IRequest, 'id'>;

export type EntityResponseType = HttpResponse<IRequest>;
export type EntityArrayResponseType = HttpResponse<IRequest[]>;

@Injectable({ providedIn: 'root' })
export class RequestService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/requests');

  create(request: NewRequest): Observable<EntityResponseType> {
    return this.http.post<IRequest>(this.resourceUrl, request, { observe: 'response' });
  }

  update(request: IRequest): Observable<EntityResponseType> {
    return this.http.put<IRequest>(`${this.resourceUrl}/${this.getRequestIdentifier(request)}`, request, { observe: 'response' });
  }

  partialUpdate(request: PartialUpdateRequest): Observable<EntityResponseType> {
    return this.http.patch<IRequest>(`${this.resourceUrl}/${this.getRequestIdentifier(request)}`, request, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRequest[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRequestIdentifier(request: Pick<IRequest, 'id'>): string {
    return request.id;
  }

  compareRequest(o1: Pick<IRequest, 'id'> | null, o2: Pick<IRequest, 'id'> | null): boolean {
    return o1 && o2 ? this.getRequestIdentifier(o1) === this.getRequestIdentifier(o2) : o1 === o2;
  }

  addRequestToCollectionIfMissing<Type extends Pick<IRequest, 'id'>>(
    requestCollection: Type[],
    ...requestsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const requests: Type[] = requestsToCheck.filter(isPresent);
    if (requests.length > 0) {
      const requestCollectionIdentifiers = requestCollection.map(requestItem => this.getRequestIdentifier(requestItem));
      const requestsToAdd = requests.filter(requestItem => {
        const requestIdentifier = this.getRequestIdentifier(requestItem);
        if (requestCollectionIdentifiers.includes(requestIdentifier)) {
          return false;
        }
        requestCollectionIdentifiers.push(requestIdentifier);
        return true;
      });
      return [...requestsToAdd, ...requestCollection];
    }
    return requestCollection;
  }
}
