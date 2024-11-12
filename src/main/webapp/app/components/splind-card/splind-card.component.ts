/* eslint-disable */
import { Component, inject, Input, OnInit, signal } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { SplindCommentComponent } from '../splind-comment/splind-comment.component';
import { Editor, Toolbar } from 'ngx-editor';
import { SplendService } from '../splend/service/splend.service';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { Subject, takeUntil } from 'rxjs';

@Component({
  selector: 'jhi-splind-card',
  standalone: true,
  imports: [SharedModule, SplindCommentComponent],
  templateUrl: './splind-card.component.html',
  styleUrl: './splind-card.component.scss',
})
export class SplindCardComponent implements OnInit {
  @Input() splend: any | undefined;
  account = signal<any | null>(null);
  editor: Editor = new Editor();
  isLoading = true;
  showComments = false;
  isModelLoaded = false;
  commnetInput = '';
  commentParentId = '';
  showVerses = false;
  verses: any[] = [];
  @Input() showEdit = true;
  mainSelectedVariable: any = {};
  public splendService = inject(SplendService);
  private accountService = inject(AccountService);
  private readonly destroy$ = new Subject<void>();

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => this.account.set(account));
  }
  comments: any[] = [];
  submitComment(): void {
    let comment: any = {
      content: this.commnetInput,
      likes: 0,
    };

    if (this.commentParentId && this.commentParentId !== '') {
      comment.parentComment = this.commentParentId;
    }

    this.splendService.createSplendComment(this.splend.id, comment).subscribe(comm => {
      this.getSplendComments();
      this.isModelLoaded = false;
      this.commnetInput = '';
      this.commentParentId = '';
    });
  }
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  getAndShowSplendVariableVerses(variable: any, mainVariable: any): void {
    this.mainSelectedVariable = mainVariable;
    this.isLoading = true;
    this.splendService.getSplendVariableVerses(this.splend.id, variable.veriableId).subscribe(
      verse => {
        this.verses = verse.body;
        this.isLoading = false;
      },
      () => {
        this.isLoading = false;
      },
    );
  }

  highlightText(text: string, searchWord: string): string {
    if (this.mainSelectedVariable && this.mainSelectedVariable.word && this.mainSelectedVariable.regex) {
      return this.highlightTextRegex(text, this.mainSelectedVariable.regex);
    }
    text = text.replace(searchWord, `<span class="highlight">${searchWord}</span>`);
    return text;
  }

  highlightTextRegex(value: string, search: string): string {
    if (!search) return value;
    const regex = new RegExp(`(${search})`, 'g');
    return value.replace(regex, `<span class="highlight">$1</span>`);
  }

  highlightTextRegexRed(value: string, search: string): string {
    if (!search) return value;
    const regex = new RegExp(`(${search})`, 'g');
    return value.replace(regex, `<span class="highlightRed">$1</span>`);
  }

  getSplendComments(): void {
    this.splendService.getSplendComments(this.splend.id).subscribe(commentsFound => {
      this.comments = commentsFound.body;
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

  likeSplend(): void {
    if (this.splend) {
      this.splendService.likeSplend(this.splend.id).subscribe(spl => {
        this.splend = spl.body;
      });
    }
  }
}
