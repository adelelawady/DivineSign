import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISplendVerses } from '../splend-verses.model';
import { SplendVersesService } from '../service/splend-verses.service';

@Component({
  standalone: true,
  templateUrl: './splend-verses-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SplendVersesDeleteDialogComponent {
  splendVerses?: ISplendVerses;

  protected splendVersesService = inject(SplendVersesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.splendVersesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
