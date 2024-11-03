import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISplend } from '../splend.model';
import { SplendService } from '../service/splend.service';

@Component({
  standalone: true,
  templateUrl: './splend-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SplendDeleteDialogComponent {
  splend?: ISplend;

  protected splendService = inject(SplendService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.splendService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
