import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISplendVerses, NewSplendVerses } from '../splend-verses.model';

export type PartialUpdateSplendVerses = Partial<ISplendVerses> & Pick<ISplendVerses, 'id'>;

export type EntityResponseType = HttpResponse<ISplendVerses>;
export type EntityArrayResponseType = HttpResponse<ISplendVerses[]>;

@Injectable({ providedIn: 'root' })
export class SplendVersesService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/splend-verses');

  create(splendVerses: NewSplendVerses): Observable<EntityResponseType> {
    return this.http.post<ISplendVerses>(this.resourceUrl, splendVerses, { observe: 'response' });
  }

  update(splendVerses: ISplendVerses): Observable<EntityResponseType> {
    return this.http.put<ISplendVerses>(`${this.resourceUrl}/${this.getSplendVersesIdentifier(splendVerses)}`, splendVerses, {
      observe: 'response',
    });
  }

  partialUpdate(splendVerses: PartialUpdateSplendVerses): Observable<EntityResponseType> {
    return this.http.patch<ISplendVerses>(`${this.resourceUrl}/${this.getSplendVersesIdentifier(splendVerses)}`, splendVerses, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ISplendVerses>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISplendVerses[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSplendVersesIdentifier(splendVerses: Pick<ISplendVerses, 'id'>): string {
    return splendVerses.id;
  }

  compareSplendVerses(o1: Pick<ISplendVerses, 'id'> | null, o2: Pick<ISplendVerses, 'id'> | null): boolean {
    return o1 && o2 ? this.getSplendVersesIdentifier(o1) === this.getSplendVersesIdentifier(o2) : o1 === o2;
  }

  addSplendVersesToCollectionIfMissing<Type extends Pick<ISplendVerses, 'id'>>(
    splendVersesCollection: Type[],
    ...splendVersesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const splendVerses: Type[] = splendVersesToCheck.filter(isPresent);
    if (splendVerses.length > 0) {
      const splendVersesCollectionIdentifiers = splendVersesCollection.map(splendVersesItem =>
        this.getSplendVersesIdentifier(splendVersesItem),
      );
      const splendVersesToAdd = splendVerses.filter(splendVersesItem => {
        const splendVersesIdentifier = this.getSplendVersesIdentifier(splendVersesItem);
        if (splendVersesCollectionIdentifiers.includes(splendVersesIdentifier)) {
          return false;
        }
        splendVersesCollectionIdentifiers.push(splendVersesIdentifier);
        return true;
      });
      return [...splendVersesToAdd, ...splendVersesCollection];
    }
    return splendVersesCollection;
  }
}
