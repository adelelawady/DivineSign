import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISplend } from '../splend.model';
import { SplendService } from '../service/splend.service';

const splendResolve = (route: ActivatedRouteSnapshot): Observable<null | ISplend> => {
  const id = route.params.id;
  if (id) {
    return inject(SplendService)
      .find(id)
      .pipe(
        mergeMap((splend: HttpResponse<ISplend>) => {
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
