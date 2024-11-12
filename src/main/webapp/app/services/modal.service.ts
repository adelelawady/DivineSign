/* eslint-disable */
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  private displayModal = new BehaviorSubject<boolean>(false);
  private content = new BehaviorSubject<string>('');
  private title = new BehaviorSubject<string>('');

  constructor() {}

  get isModalVisible$(): Observable<boolean> {
    return this.displayModal.asObservable();
  }

  get modalContent$(): Observable<string> {
    return this.content.asObservable();
  }

  get modalTitle$(): Observable<string> {
    return this.title.asObservable();
  }

  showModal(content: string, title: string = 'Content Preview'): void {
    this.content.next(content);
    this.title.next(title);
    this.displayModal.next(true);
    // document.body.style.overflow = 'hidden';
  }

  hideModal(): void {
    this.displayModal.next(false);
    document.body.style.overflow = 'auto';
  }

  showContentFromElement(elementId: string, title: string = 'Content Preview'): void {
    const element = document.getElementById(elementId);
    if (element) {
      this.showModal(element.innerHTML, title);
    } else {
      console.warn(`Element with ID '${elementId}' not found`);
    }
  }
}
