import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IVerse } from '../verse.model';

@Component({
  standalone: true,
  selector: 'jhi-verse-detail',
  templateUrl: './verse-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class VerseDetailComponent {
  verse = input<IVerse | null>(null);

  previousState(): void {
    window.history.back();
  }
}
