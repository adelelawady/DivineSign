import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRequest } from '../request.model';
import { RequestService } from '../service/request.service';

const requestResolve = (route: ActivatedRouteSnapshot): Observable<null | IRequest> => {
  const id = route.params.id;
  if (id) {
    return inject(RequestService)
      .find(id)
      .pipe(
        mergeMap((request: HttpResponse<IRequest>) => {
          if (request.body) {
            return of(request.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default requestResolve;
