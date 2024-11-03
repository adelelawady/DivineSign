import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISurah } from '../surah.model';
import { SurahService } from '../service/surah.service';

const surahResolve = (route: ActivatedRouteSnapshot): Observable<null | ISurah> => {
  const id = route.params.id;
  if (id) {
    return inject(SurahService)
      .find(id)
      .pipe(
        mergeMap((surah: HttpResponse<ISurah>) => {
          if (surah.body) {
            return of(surah.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default surahResolve;
