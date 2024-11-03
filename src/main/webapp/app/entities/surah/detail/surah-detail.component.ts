import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISurah } from '../surah.model';

@Component({
  standalone: true,
  selector: 'jhi-surah-detail',
  templateUrl: './surah-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SurahDetailComponent {
  surah = input<ISurah | null>(null);

  previousState(): void {
    window.history.back();
  }
}
