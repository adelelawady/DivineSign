import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import ValidationModelResolve from './route/validation-model-routing-resolve.service';

const validationModelRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/validation-model.component').then(m => m.ValidationModelComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/validation-model-detail.component').then(m => m.ValidationModelDetailComponent),
    resolve: {
      validationModel: ValidationModelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/validation-model-update.component').then(m => m.ValidationModelUpdateComponent),
    resolve: {
      validationModel: ValidationModelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/validation-model-update.component').then(m => m.ValidationModelUpdateComponent),
    resolve: {
      validationModel: ValidationModelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default validationModelRoute;
