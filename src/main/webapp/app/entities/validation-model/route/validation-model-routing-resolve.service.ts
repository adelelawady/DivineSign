import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IValidationModel } from '../validation-model.model';
import { ValidationModelService } from '../service/validation-model.service';

const validationModelResolve = (route: ActivatedRouteSnapshot): Observable<null | IValidationModel> => {
  const id = route.params.id;
  if (id) {
    return inject(ValidationModelService)
      .find(id)
      .pipe(
        mergeMap((validationModel: HttpResponse<IValidationModel>) => {
          if (validationModel.body) {
            return of(validationModel.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default validationModelResolve;
