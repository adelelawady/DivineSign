import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISplend, NewSplend } from '../splend.model';

export type PartialUpdateSplend = Partial<ISplend> & Pick<ISplend, 'id'>;

export type EntityResponseType = HttpResponse<ISplend>;
export type EntityArrayResponseType = HttpResponse<ISplend[]>;

@Injectable({ providedIn: 'root' })
export class SplendService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/splends');

  create(splend: NewSplend): Observable<EntityResponseType> {
    return this.http.post<ISplend>(this.resourceUrl, splend, { observe: 'response' });
  }

  update(splend: ISplend): Observable<EntityResponseType> {
    return this.http.put<ISplend>(`${this.resourceUrl}/${this.getSplendIdentifier(splend)}`, splend, { observe: 'response' });
  }

  partialUpdate(splend: PartialUpdateSplend): Observable<EntityResponseType> {
    return this.http.patch<ISplend>(`${this.resourceUrl}/${this.getSplendIdentifier(splend)}`, splend, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ISplend>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISplend[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSplendIdentifier(splend: Pick<ISplend, 'id'>): string {
    return splend.id;
  }

  compareSplend(o1: Pick<ISplend, 'id'> | null, o2: Pick<ISplend, 'id'> | null): boolean {
    return o1 && o2 ? this.getSplendIdentifier(o1) === this.getSplendIdentifier(o2) : o1 === o2;
  }

  addSplendToCollectionIfMissing<Type extends Pick<ISplend, 'id'>>(
    splendCollection: Type[],
    ...splendsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const splends: Type[] = splendsToCheck.filter(isPresent);
    if (splends.length > 0) {
      const splendCollectionIdentifiers = splendCollection.map(splendItem => this.getSplendIdentifier(splendItem));
      const splendsToAdd = splends.filter(splendItem => {
        const splendIdentifier = this.getSplendIdentifier(splendItem);
        if (splendCollectionIdentifiers.includes(splendIdentifier)) {
          return false;
        }
        splendCollectionIdentifiers.push(splendIdentifier);
        return true;
      });
      return [...splendsToAdd, ...splendCollection];
    }
    return splendCollection;
  }
}
