import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import RequestResolve from './route/request-routing-resolve.service';

const requestRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/request.component').then(m => m.RequestComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/request-detail.component').then(m => m.RequestDetailComponent),
    resolve: {
      request: RequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/request-update.component').then(m => m.RequestUpdateComponent),
    resolve: {
      request: RequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/request-update.component').then(m => m.RequestUpdateComponent),
    resolve: {
      request: RequestResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default requestRoute;
