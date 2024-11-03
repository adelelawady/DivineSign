import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'divineSignApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'category',
    data: { pageTitle: 'divineSignApp.category.home.title' },
    loadChildren: () => import('./category/category.routes'),
  },
  {
    path: 'comment',
    data: { pageTitle: 'divineSignApp.comment.home.title' },
    loadChildren: () => import('./comment/comment.routes'),
  },
  {
    path: 'request',
    data: { pageTitle: 'divineSignApp.request.home.title' },
    loadChildren: () => import('./request/request.routes'),
  },
  {
    path: 'splend',
    data: { pageTitle: 'divineSignApp.splend.home.title' },
    loadChildren: () => import('./splend/splend.routes'),
  },
  {
    path: 'splend-verses',
    data: { pageTitle: 'divineSignApp.splendVerses.home.title' },
    loadChildren: () => import('./splend-verses/splend-verses.routes'),
  },
  {
    path: 'surah',
    data: { pageTitle: 'divineSignApp.surah.home.title' },
    loadChildren: () => import('./surah/surah.routes'),
  },
  {
    path: 'tag',
    data: { pageTitle: 'divineSignApp.tag.home.title' },
    loadChildren: () => import('./tag/tag.routes'),
  },
  {
    path: 'validation-model',
    data: { pageTitle: 'divineSignApp.validationModel.home.title' },
    loadChildren: () => import('./validation-model/validation-model.routes'),
  },
  {
    path: 'verse',
    data: { pageTitle: 'divineSignApp.verse.home.title' },
    loadChildren: () => import('./verse/verse.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
