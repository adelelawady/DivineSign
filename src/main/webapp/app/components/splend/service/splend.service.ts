/* eslint-disable */

import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';

export type PartialUpdateSplend = Partial<any> & Pick<any, 'id'>;

export type EntityResponseType = HttpResponse<any>;
export type EntityArrayResponseType = HttpResponse<any[]>;

@Injectable({ providedIn: 'root' })
export class SplendService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceAPIUrl = this.applicationConfigService.getEndpointFor('api/splend');
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/splends');
  protected resourceVersesUrl = this.applicationConfigService.getEndpointFor('api/verse');

  create(splend: any): Observable<EntityResponseType> {
    return this.http.post<any>(`${this.resourceAPIUrl}/create`, splend, { observe: 'response' });
  }

  createSplendVariable(splendId: any, createSplendVariablePayload: any): Observable<EntityResponseType> {
    return this.http.post<any>(`${this.resourceAPIUrl}/${splendId}/variables`, createSplendVariablePayload, { observe: 'response' });
  }

  getSplendVariables(splendId: any): Observable<EntityResponseType> {
    return this.http.get<any>(`${this.resourceAPIUrl}/${splendId}/variables`, { observe: 'response' });
  }

  likeSplend(splendId: any): Observable<EntityResponseType> {
    return this.http.get<any>(`${this.resourceAPIUrl}/${splendId}/like`, { observe: 'response' });
  }

  addSplendVariablesName(splendId: any, variableId: string, varPayload: any): Observable<EntityResponseType> {
    return this.http.post<any>(`${this.resourceAPIUrl}/${splendId}/variables/${variableId}`, varPayload, { observe: 'response' });
  }

  deleteSplendVariablesName(splendId: any, variableId: string, varName: any): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceAPIUrl}/${splendId}/variables/${variableId}?name=${varName}`, { observe: 'response' });
  }

  deleteSplendVariable(splendId: any, variableId: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceAPIUrl}/${splendId}/variables?id=${variableId}`, { observe: 'response' });
  }

  queryVersesByWord(verseSearch: any): Observable<EntityResponseType> {
    return this.http.post<any>(`${this.resourceVersesUrl}/search`, verseSearch, { observe: 'response' });
  }

  findById(id: string): Observable<EntityResponseType> {
    return this.http.get<any>(`${this.resourceAPIUrl}/${id}`, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<any[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSplendComments(splendId: any): Observable<EntityResponseType> {
    return this.http.get<any>(`${this.resourceAPIUrl}/${splendId}/comments`, { observe: 'response' });
  }

  createSplendComment(splendId: any, comment: any): Observable<EntityResponseType> {
    return this.http.post<any>(`${this.resourceAPIUrl}/${splendId}/comments`, comment, { observe: 'response' });
  }

  getSplendVariableVerses(splendId: any, variableId: any): Observable<EntityResponseType> {
    return this.http.get<any>(`${this.resourceAPIUrl}/${splendId}/variables/${variableId}/verses`, { observe: 'response' });
  }
}
