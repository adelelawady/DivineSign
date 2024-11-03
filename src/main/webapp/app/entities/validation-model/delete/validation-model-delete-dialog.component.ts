import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IValidationModel } from '../validation-model.model';
import { ValidationModelService } from '../service/validation-model.service';

@Component({
  standalone: true,
  templateUrl: './validation-model-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ValidationModelDeleteDialogComponent {
  validationModel?: IValidationModel;

  protected validationModelService = inject(ValidationModelService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.validationModelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
