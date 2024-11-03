import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISurah } from '../surah.model';
import { SurahService } from '../service/surah.service';
import { SurahFormGroup, SurahFormService } from './surah-form.service';

@Component({
  standalone: true,
  selector: 'jhi-surah-update',
  templateUrl: './surah-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SurahUpdateComponent implements OnInit {
  isSaving = false;
  surah: ISurah | null = null;

  protected surahService = inject(SurahService);
  protected surahFormService = inject(SurahFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SurahFormGroup = this.surahFormService.createSurahFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ surah }) => {
      this.surah = surah;
      if (surah) {
        this.updateForm(surah);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const surah = this.surahFormService.getSurah(this.editForm);
    if (surah.id !== null) {
      this.subscribeToSaveResponse(this.surahService.update(surah));
    } else {
      this.subscribeToSaveResponse(this.surahService.create(surah));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISurah>>): void {
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

  protected updateForm(surah: ISurah): void {
    this.surah = surah;
    this.surahFormService.resetForm(this.editForm, surah);
  }
}
