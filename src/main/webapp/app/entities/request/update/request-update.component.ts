import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ISplend } from 'app/entities/splend/splend.model';
import { SplendService } from 'app/entities/splend/service/splend.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { RequestStatus } from 'app/entities/enumerations/request-status.model';
import { RequestService } from '../service/request.service';
import { IRequest } from '../request.model';
import { RequestFormGroup, RequestFormService } from './request-form.service';

@Component({
  standalone: true,
  selector: 'jhi-request-update',
  templateUrl: './request-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RequestUpdateComponent implements OnInit {
  isSaving = false;
  request: IRequest | null = null;
  requestStatusValues = Object.keys(RequestStatus);

  splendsSharedCollection: ISplend[] = [];
  usersSharedCollection: IUser[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected requestService = inject(RequestService);
  protected requestFormService = inject(RequestFormService);
  protected splendService = inject(SplendService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RequestFormGroup = this.requestFormService.createRequestFormGroup();

  compareSplend = (o1: ISplend | null, o2: ISplend | null): boolean => this.splendService.compareSplend(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ request }) => {
      this.request = request;
      if (request) {
        this.updateForm(request);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('divineSignApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const request = this.requestFormService.getRequest(this.editForm);
    if (request.id !== null) {
      this.subscribeToSaveResponse(this.requestService.update(request));
    } else {
      this.subscribeToSaveResponse(this.requestService.create(request));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRequest>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(request: IRequest): void {
    this.request = request;
    this.requestFormService.resetForm(this.editForm, request);

    this.splendsSharedCollection = this.splendService.addSplendToCollectionIfMissing<ISplend>(this.splendsSharedCollection, request.splend);
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, request.user);
  }

  protected loadRelationshipsOptions(): void {
    this.splendService
      .query()
      .pipe(map((res: HttpResponse<ISplend[]>) => res.body ?? []))
      .pipe(map((splends: ISplend[]) => this.splendService.addSplendToCollectionIfMissing<ISplend>(splends, this.request?.splend)))
      .subscribe((splends: ISplend[]) => (this.splendsSharedCollection = splends));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.request?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
