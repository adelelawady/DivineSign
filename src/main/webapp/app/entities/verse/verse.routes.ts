import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import VerseResolve from './route/verse-routing-resolve.service';

const verseRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/verse.component').then(m => m.VerseComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/verse-detail.component').then(m => m.VerseDetailComponent),
    resolve: {
      verse: VerseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/verse-update.component').then(m => m.VerseUpdateComponent),
    resolve: {
      verse: VerseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/verse-update.component').then(m => m.VerseUpdateComponent),
    resolve: {
      verse: VerseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default verseRoute;
