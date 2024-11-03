import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import SplendVersesResolve from './route/splend-verses-routing-resolve.service';

const splendVersesRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/splend-verses.component').then(m => m.SplendVersesComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/splend-verses-detail.component').then(m => m.SplendVersesDetailComponent),
    resolve: {
      splendVerses: SplendVersesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/splend-verses-update.component').then(m => m.SplendVersesUpdateComponent),
    resolve: {
      splendVerses: SplendVersesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/splend-verses-update.component').then(m => m.SplendVersesUpdateComponent),
    resolve: {
      splendVerses: SplendVersesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default splendVersesRoute;
