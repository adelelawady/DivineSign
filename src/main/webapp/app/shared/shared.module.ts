/* eslint-disable */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';

import FindLanguageFromKeyPipe from './language/find-language-from-key.pipe';
import TranslateDirective from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { NzTimelineModule } from 'ng-zorro-antd/timeline';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzAvatarModule } from 'ng-zorro-antd/avatar';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzTagModule } from 'ng-zorro-antd/tag';
import { NzPageHeaderModule } from 'ng-zorro-antd/page-header';
import { NzToolTipModule } from 'ng-zorro-antd/tooltip';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzBreadCrumbComponent, NzBreadCrumbModule } from 'ng-zorro-antd/breadcrumb';
import { NzCommentModule } from 'ng-zorro-antd/comment';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NgxEditorModule } from 'ngx-editor';

import { NzSpaceModule } from 'ng-zorro-antd/space';
import { NzNotificationModule } from 'ng-zorro-antd/notification';
import { NzListModule } from 'ng-zorro-antd/list';
import { NzDividerModule } from 'ng-zorro-antd/divider';
import { NzStatisticModule } from 'ng-zorro-antd/statistic';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { NzSelectModule } from 'ng-zorro-antd/select';

import { NzIconModule, NZ_ICONS } from 'ng-zorro-antd/icon';
import { PlusOutline } from '@ant-design/icons-angular/icons';
import { GravatarModule } from 'ngx-gravatar';

//statistic
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

/**
 * Application wide Module
 */
@NgModule({
  imports: [
    GravatarModule,
    NzIconModule,
    AlertComponent,
    AlertErrorComponent,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    NzBreadCrumbComponent,
  ],
  exports: [
    CommonModule,
    NgbModule,
    FormsModule,
    RouterModule,
    ReactiveFormsModule,
    NgxEditorModule,
    FontAwesomeModule,
    AlertComponent,
    AlertErrorComponent,
    TranslateModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,

    NzTimelineModule,
    NzCardModule,
    NzAvatarModule,
    GravatarModule,
    NzButtonModule,
    NzModalModule,
    NzCommentModule,
    NzSelectModule,
    NzIconModule,
    NzFormModule,
    NzInputModule,
    NzListModule,
    NzDividerModule,
    NzStatisticModule,
    NzTagModule,
    NzSpinModule,
    NzNotificationModule,
    NzPageHeaderModule,
    NzToolTipModule,
    NzLayoutModule,
    NzBreadCrumbModule,
    NzBreadCrumbComponent,
    NzSpaceModule,
  ],
  providers: [
    { provide: NZ_ICONS, useValue: [PlusOutline] }, // Add PlusOutline here
  ],
})
export default class SharedModule {}
