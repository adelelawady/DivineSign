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

  protected resourceAPIUrl = this.applicationConfigService.getEndpointFor('api/public');

  getPublicSplends(): Observable<EntityResponseType> {
    return this.http.get<any>(`${this.resourceAPIUrl}/splends`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<any[]>(`${this.resourceAPIUrl}/splends`, { params: options, observe: 'response' });
  }
}
