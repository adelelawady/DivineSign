/* eslint-disable */

import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { SplendService } from '../service/splend.service';

const splendResolve = (route: ActivatedRouteSnapshot): Observable<null | any> => {
  const id = route.params.id;
  if (id) {
    return inject(SplendService)
      .findById(id)
      .pipe(
        mergeMap((splend: HttpResponse<any>): any => {
          if (splend.body) {
            return of(splend.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default splendResolve;
