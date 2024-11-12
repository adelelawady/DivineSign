/* eslint-disable */

import { Component, OnInit, OnDestroy } from '@angular/core';
import { ModalService } from 'app/services/modal.service';
import SharedModule from 'app/shared/shared.module';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [SharedModule],
  template: `
    <div *ngIf="isVisible" class="modal-overlay">
      <div class="modal-backdrop" (click)="close()"></div>
      <div class="modal-container">
        <div class="modal-header">
          <h2>{{ title }}</h2>
          <button class="close-button" (click)="close()">×</button>
        </div>
        <div class="modal-content">
          <div [innerHTML]="content"></div>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .modal-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 1000;
      }

      .modal-backdrop {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(0, 0, 0, 0.5);
      }

      .modal-container {
        position: relative;
        background: white;
        border-radius: 8px;
        padding: 20px;
        max-width: 90%;
        width: 600px;
        max-height: 90vh;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        z-index: 1001;
      }

      .modal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 1px solid #eee;
      }

      .close-button {
        background: none;
        border: none;
        font-size: 24px;
        cursor: pointer;
        padding: 0 8px;
      }

      .modal-content {
        overflow-y: auto;
        max-height: calc(90vh - 100px);
      }
    `,
  ],
})
export class ModalFormComponent implements OnInit, OnDestroy {
  isVisible = true;
  content = '';
  title = '';
  private destroy$ = new Subject<void>();

  constructor(private modalService: ModalService) {}

  ngOnInit(): void {
    this.modalService.isModalVisible$.pipe(takeUntil(this.destroy$)).subscribe((isVisible: boolean) => (this.isVisible = isVisible));

    this.modalService.modalContent$.pipe(takeUntil(this.destroy$)).subscribe((content: string) => (this.content = content));

    this.modalService.modalTitle$.pipe(takeUntil(this.destroy$)).subscribe((title: string) => (this.title = title));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  close(): void {
    this.modalService.hideModal();
  }
}
