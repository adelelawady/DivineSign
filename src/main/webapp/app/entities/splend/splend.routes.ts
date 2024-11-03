import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import SplendResolve from './route/splend-routing-resolve.service';

const splendRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/splend.component').then(m => m.SplendComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/splend-detail.component').then(m => m.SplendDetailComponent),
    resolve: {
      splend: SplendResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/splend-update.component').then(m => m.SplendUpdateComponent),
    resolve: {
      splend: SplendResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/splend-update.component').then(m => m.SplendUpdateComponent),
    resolve: {
      splend: SplendResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default splendRoute;
