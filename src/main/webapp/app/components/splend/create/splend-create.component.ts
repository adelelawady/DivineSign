/* eslint-disable */

import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { Editor, Toolbar } from 'ngx-editor';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { SplendService } from '../service/splend.service';
import { TagsInputComponent } from 'app/components/tags-input/tags-input.component';
import { NzNotificationService } from 'ng-zorro-antd/notification';

@Component({
  standalone: true,
  selector: 'jhi-splend-create',
  templateUrl: './splend-create.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, TagsInputComponent],
  styles: ``,
})
export class SplendCreateComponent implements OnInit, OnDestroy {
  isSaving = false;
  splend: any | null = null;
  editor: Editor = new Editor();
  html = '';
  title = '';
  tags: string[] = [];
  usersSharedCollection: IUser[] = [];

  toolbar: Toolbar = [
    // default value
    ['bold', 'italic'],
    ['underline', 'strike'],
    ['code', 'blockquote'],
    ['ordered_list', 'bullet_list'],
    [{ heading: ['h1', 'h2', 'h3', 'h4', 'h5', 'h6'] }],
    ['link', 'image'],
    // or, set options for link:
    //[{ link: { showOpenInNewTab: false } }, 'image'],
    ['text_color', 'background_color'],
    ['align_left', 'align_center', 'align_right', 'align_justify'],
    ['horizontal_rule', 'format_clear', 'indent', 'outdent'],
    ['superscript', 'subscript'],
    ['undo', 'redo'],
  ];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected splendService = inject(SplendService);
  protected categoryService = inject(CategoryService);

  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);
  protected notification = inject(NzNotificationService);
  // eslint-disable-next-line @typescript-eslint/member-ordering

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.editor = new Editor();
    this.activatedRoute.data.subscribe(({ splend }) => {
      this.splend = splend;
    });
  }
  // make sure to destory the editor
  ngOnDestroy(): void {
    this.editor?.destroy();
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    let splend: any = {};
    splend.title = this.title;
    splend.content = this.html;
    splend.tags = this.tags;

    this.splendService.create(splend).subscribe((splendCreated: any) => {
      this.onSaveSuccess(splendCreated.body);
      const splendId = splendCreated.body.id;
      this.router.navigate(['/', 'user', 'splend', splendId, 'manage']);
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(null),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(splend: any): void {
    this.notification.success('Created New Splend', `Splend '${splend.title}' was created successfully`, { nzPlacement: 'topLeft' });

    // TODO go to someware after splend created
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
