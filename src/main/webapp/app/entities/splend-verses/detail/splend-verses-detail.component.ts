import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISplendVerses } from '../splend-verses.model';

@Component({
  standalone: true,
  selector: 'jhi-splend-verses-detail',
  templateUrl: './splend-verses-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SplendVersesDetailComponent {
  splendVerses = input<ISplendVerses | null>(null);

  previousState(): void {
    window.history.back();
  }
}
