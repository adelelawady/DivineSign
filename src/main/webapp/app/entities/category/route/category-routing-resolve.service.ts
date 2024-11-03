import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategory } from '../category.model';
import { CategoryService } from '../service/category.service';

const categoryResolve = (route: ActivatedRouteSnapshot): Observable<null | ICategory> => {
  const id = route.params.id;
  if (id) {
    return inject(CategoryService)
      .find(id)
      .pipe(
        mergeMap((category: HttpResponse<ICategory>) => {
          if (category.body) {
            return of(category.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default categoryResolve;
