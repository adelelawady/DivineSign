import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IValidationModel } from '../validation-model.model';

@Component({
  standalone: true,
  selector: 'jhi-validation-model-detail',
  templateUrl: './validation-model-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ValidationModelDetailComponent {
  validationModel = input<IValidationModel | null>(null);

  previousState(): void {
    window.history.back();
  }
}
