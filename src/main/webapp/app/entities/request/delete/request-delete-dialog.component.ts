import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRequest } from '../request.model';
import { RequestService } from '../service/request.service';

@Component({
  standalone: true,
  templateUrl: './request-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RequestDeleteDialogComponent {
  request?: IRequest;

  protected requestService = inject(RequestService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.requestService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
