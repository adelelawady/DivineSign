import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISurah } from '../surah.model';
import { SurahService } from '../service/surah.service';

@Component({
  standalone: true,
  templateUrl: './surah-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SurahDeleteDialogComponent {
  surah?: ISurah;

  protected surahService = inject(SurahService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.surahService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
