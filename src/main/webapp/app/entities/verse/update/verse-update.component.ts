import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISurah } from 'app/entities/surah/surah.model';
import { SurahService } from 'app/entities/surah/service/surah.service';
import { ISplendVerses } from 'app/entities/splend-verses/splend-verses.model';
import { SplendVersesService } from 'app/entities/splend-verses/service/splend-verses.service';
import { VerseService } from '../service/verse.service';
import { IVerse } from '../verse.model';
import { VerseFormGroup, VerseFormService } from './verse-form.service';

@Component({
  standalone: true,
  selector: 'jhi-verse-update',
  templateUrl: './verse-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VerseUpdateComponent implements OnInit {
  isSaving = false;
  verse: IVerse | null = null;

  surahsSharedCollection: ISurah[] = [];
  splendVersesSharedCollection: ISplendVerses[] = [];

  protected verseService = inject(VerseService);
  protected verseFormService = inject(VerseFormService);
  protected surahService = inject(SurahService);
  protected splendVersesService = inject(SplendVersesService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VerseFormGroup = this.verseFormService.createVerseFormGroup();

  compareSurah = (o1: ISurah | null, o2: ISurah | null): boolean => this.surahService.compareSurah(o1, o2);

  compareSplendVerses = (o1: ISplendVerses | null, o2: ISplendVerses | null): boolean =>
    this.splendVersesService.compareSplendVerses(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ verse }) => {
      this.verse = verse;
      if (verse) {
        this.updateForm(verse);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const verse = this.verseFormService.getVerse(this.editForm);
    if (verse.id !== null) {
      this.subscribeToSaveResponse(this.verseService.update(verse));
    } else {
      this.subscribeToSaveResponse(this.verseService.create(verse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVerse>>): void {
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

  protected updateForm(verse: IVerse): void {
    this.verse = verse;
    this.verseFormService.resetForm(this.editForm, verse);

    this.surahsSharedCollection = this.surahService.addSurahToCollectionIfMissing<ISurah>(this.surahsSharedCollection, verse.surah);
    this.splendVersesSharedCollection = this.splendVersesService.addSplendVersesToCollectionIfMissing<ISplendVerses>(
      this.splendVersesSharedCollection,
      verse.splendVerses,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.surahService
      .query()
      .pipe(map((res: HttpResponse<ISurah[]>) => res.body ?? []))
      .pipe(map((surahs: ISurah[]) => this.surahService.addSurahToCollectionIfMissing<ISurah>(surahs, this.verse?.surah)))
      .subscribe((surahs: ISurah[]) => (this.surahsSharedCollection = surahs));

    this.splendVersesService
      .query()
      .pipe(map((res: HttpResponse<ISplendVerses[]>) => res.body ?? []))
      .pipe(
        map((splendVerses: ISplendVerses[]) =>
          this.splendVersesService.addSplendVersesToCollectionIfMissing<ISplendVerses>(splendVerses, this.verse?.splendVerses),
        ),
      )
      .subscribe((splendVerses: ISplendVerses[]) => (this.splendVersesSharedCollection = splendVerses));
  }
}
