import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISurah, NewSurah } from '../surah.model';

export type PartialUpdateSurah = Partial<ISurah> & Pick<ISurah, 'id'>;

export type EntityResponseType = HttpResponse<ISurah>;
export type EntityArrayResponseType = HttpResponse<ISurah[]>;

@Injectable({ providedIn: 'root' })
export class SurahService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/surahs');

  create(surah: NewSurah): Observable<EntityResponseType> {
    return this.http.post<ISurah>(this.resourceUrl, surah, { observe: 'response' });
  }

  update(surah: ISurah): Observable<EntityResponseType> {
    return this.http.put<ISurah>(`${this.resourceUrl}/${this.getSurahIdentifier(surah)}`, surah, { observe: 'response' });
  }

  partialUpdate(surah: PartialUpdateSurah): Observable<EntityResponseType> {
    return this.http.patch<ISurah>(`${this.resourceUrl}/${this.getSurahIdentifier(surah)}`, surah, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ISurah>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISurah[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSurahIdentifier(surah: Pick<ISurah, 'id'>): string {
    return surah.id;
  }

  compareSurah(o1: Pick<ISurah, 'id'> | null, o2: Pick<ISurah, 'id'> | null): boolean {
    return o1 && o2 ? this.getSurahIdentifier(o1) === this.getSurahIdentifier(o2) : o1 === o2;
  }

  addSurahToCollectionIfMissing<Type extends Pick<ISurah, 'id'>>(
    surahCollection: Type[],
    ...surahsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const surahs: Type[] = surahsToCheck.filter(isPresent);
    if (surahs.length > 0) {
      const surahCollectionIdentifiers = surahCollection.map(surahItem => this.getSurahIdentifier(surahItem));
      const surahsToAdd = surahs.filter(surahItem => {
        const surahIdentifier = this.getSurahIdentifier(surahItem);
        if (surahCollectionIdentifiers.includes(surahIdentifier)) {
          return false;
        }
        surahCollectionIdentifiers.push(surahIdentifier);
        return true;
      });
      return [...surahsToAdd, ...surahCollection];
    }
    return surahCollection;
  }
}
