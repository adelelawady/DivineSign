import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import SurahResolve from './route/surah-routing-resolve.service';

const surahRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/surah.component').then(m => m.SurahComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/surah-detail.component').then(m => m.SurahDetailComponent),
    resolve: {
      surah: SurahResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/surah-update.component').then(m => m.SurahUpdateComponent),
    resolve: {
      surah: SurahResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/surah-update.component').then(m => m.SurahUpdateComponent),
    resolve: {
      surah: SurahResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default surahRoute;
