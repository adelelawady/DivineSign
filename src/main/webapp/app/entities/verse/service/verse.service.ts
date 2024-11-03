import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVerse, NewVerse } from '../verse.model';

export type PartialUpdateVerse = Partial<IVerse> & Pick<IVerse, 'id'>;

export type EntityResponseType = HttpResponse<IVerse>;
export type EntityArrayResponseType = HttpResponse<IVerse[]>;

@Injectable({ providedIn: 'root' })
export class VerseService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/verses');

  create(verse: NewVerse): Observable<EntityResponseType> {
    return this.http.post<IVerse>(this.resourceUrl, verse, { observe: 'response' });
  }

  update(verse: IVerse): Observable<EntityResponseType> {
    return this.http.put<IVerse>(`${this.resourceUrl}/${this.getVerseIdentifier(verse)}`, verse, { observe: 'response' });
  }

  partialUpdate(verse: PartialUpdateVerse): Observable<EntityResponseType> {
    return this.http.patch<IVerse>(`${this.resourceUrl}/${this.getVerseIdentifier(verse)}`, verse, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IVerse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVerse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVerseIdentifier(verse: Pick<IVerse, 'id'>): string {
    return verse.id;
  }

  compareVerse(o1: Pick<IVerse, 'id'> | null, o2: Pick<IVerse, 'id'> | null): boolean {
    return o1 && o2 ? this.getVerseIdentifier(o1) === this.getVerseIdentifier(o2) : o1 === o2;
  }

  addVerseToCollectionIfMissing<Type extends Pick<IVerse, 'id'>>(
    verseCollection: Type[],
    ...versesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const verses: Type[] = versesToCheck.filter(isPresent);
    if (verses.length > 0) {
      const verseCollectionIdentifiers = verseCollection.map(verseItem => this.getVerseIdentifier(verseItem));
      const versesToAdd = verses.filter(verseItem => {
        const verseIdentifier = this.getVerseIdentifier(verseItem);
        if (verseCollectionIdentifiers.includes(verseIdentifier)) {
          return false;
        }
        verseCollectionIdentifiers.push(verseIdentifier);
        return true;
      });
      return [...versesToAdd, ...verseCollection];
    }
    return verseCollection;
  }
}
