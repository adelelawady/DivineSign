import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICategory } from '../category.model';
import { CategoryService } from '../service/category.service';

@Component({
  standalone: true,
  templateUrl: './category-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CategoryDeleteDialogComponent {
  category?: ICategory;

  protected categoryService = inject(CategoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.categoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
