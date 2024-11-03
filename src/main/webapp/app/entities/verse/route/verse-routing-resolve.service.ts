import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVerse } from '../verse.model';
import { VerseService } from '../service/verse.service';

const verseResolve = (route: ActivatedRouteSnapshot): Observable<null | IVerse> => {
  const id = route.params.id;
  if (id) {
    return inject(VerseService)
      .find(id)
      .pipe(
        mergeMap((verse: HttpResponse<IVerse>) => {
          if (verse.body) {
            return of(verse.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default verseResolve;
