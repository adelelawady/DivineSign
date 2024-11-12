/* eslint-disable */
import { AfterViewInit, Component, ElementRef, EventEmitter, inject, Input, Output, Renderer2 } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { CommonModule } from '@angular/common';
import { SplendService } from '../splend/service/splend.service';

@Component({
  selector: 'jhi-splind-comment',
  standalone: true,
  imports: [SharedModule, CommonModule],
  templateUrl: './splind-comment.component.html',
  styleUrl: './splind-comment.component.scss',
})
export class SplindCommentComponent implements AfterViewInit {
  constructor(
    private renderer: Renderer2,
    private el: ElementRef,
  ) {}

  ngAfterViewInit(): void {}
  @Input() comment: any | undefined;
  @Input() splend: any | undefined;
  @Input() level = 0; // Track the nesting level
  @Output() commnetAdded = new EventEmitter<any>(); // EventEmitter instance

  showReply = true;
  replyText = 'this is replay';
  showComments = false;
  isModelLoaded = false;
  commnetInput = '';
  commentParentId = '';
  public splendService = inject(SplendService);

  likeComment(comment: any): void {
    //
  }

  toggleReply() {
    this.showReply = !this.showReply;
  }

  addReply(comment: any): void {
    console.log('addReply', comment);
    const newReply = {
      author: 'Your Name',
      date: new Date(),
      content: this.replyText,
      likes: 0,
      replies: [],
    };
    comment.replies.push(newReply);
    //this.replyText = '';
    this.showReply = false;
  }

  emit(): void {
    this.commnetAdded.emit(null); // Emit the event with data
  }

  getNestingLevels() {
    return new Array(this.level);
  }

  toDate(date: Date | string | null, format: Intl.DateTimeFormatOptions = { dateStyle: 'short', timeStyle: 'short' }): string {
    if (!date) return ''; // Handle null or undefined dates

    const dateObj = typeof date === 'string' ? new Date(date) : date;
    return new Intl.DateTimeFormat('en-US', format).format(dateObj);
  }

  submitComment(): void {
    let comment: any = {
      content: this.commnetInput,
    };

    if (this.commentParentId && this.commentParentId !== '') {
      comment.parentComment = this.commentParentId;
    }

    this.splendService.createSplendComment(this.splend.id, comment).subscribe(comm => {
      this.isModelLoaded = false;
      this.comment.comments.push(comm.body);
      this.commnetAdded.emit(null); // Emit the event with data
      this.commnetInput = '';
      this.commentParentId = '';
    });
  }

  addCommnet(parentId: any): void {
    if (parentId && parentId !== '' && parentId !== null) {
      this.commentParentId = parentId;
    }
    this.isModelLoaded = true;
  }
  handleCancel(): void {
    this.isModelLoaded = false;
  }
}
