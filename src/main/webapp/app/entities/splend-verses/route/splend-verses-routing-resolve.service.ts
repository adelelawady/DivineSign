import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISplendVerses } from '../splend-verses.model';
import { SplendVersesService } from '../service/splend-verses.service';

const splendVersesResolve = (route: ActivatedRouteSnapshot): Observable<null | ISplendVerses> => {
  const id = route.params.id;
  if (id) {
    return inject(SplendVersesService)
      .find(id)
      .pipe(
        mergeMap((splendVerses: HttpResponse<ISplendVerses>) => {
          if (splendVerses.body) {
            return of(splendVerses.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default splendVersesResolve;
