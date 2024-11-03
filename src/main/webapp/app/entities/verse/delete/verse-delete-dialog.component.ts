import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVerse } from '../verse.model';
import { VerseService } from '../service/verse.service';

@Component({
  standalone: true,
  templateUrl: './verse-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VerseDeleteDialogComponent {
  verse?: IVerse;

  protected verseService = inject(VerseService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.verseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
