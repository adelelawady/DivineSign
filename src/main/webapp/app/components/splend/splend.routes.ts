/* eslint-disable */

import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import splendResolve from './route/splend-routing-resolve.service';

const splendRoute: Routes = [
  {
    path: 'new',
    loadComponent: () => import('./create/splend-create.component').then(m => m.SplendCreateComponent),
    resolve: {
      splend: splendResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/manage',
    loadComponent: () => import('./manage/splend-manage.component').then(m => m.SplendManageComponent),
    resolve: {
      splend: splendResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default splendRoute;
