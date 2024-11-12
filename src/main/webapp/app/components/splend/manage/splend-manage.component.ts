/* eslint-disable */

import { Component, OnDestroy, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { debounceTime, distinctUntilChanged, finalize, map } from 'rxjs/operators';
import { Editor, Toolbar } from 'ngx-editor';

import SharedModule from 'app/shared/shared.module';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { SplendService } from '../service/splend.service';
import { TagsInputComponent } from 'app/components/tags-input/tags-input.component';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { SplindCardComponent } from 'app/components/splind-card/splind-card.component';

@Component({
  standalone: true,
  selector: 'jhi-splend-manage',
  styleUrl: './splend-manage.component.scss',
  templateUrl: './splend-manage.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, TagsInputComponent, SplindCardComponent],
  styles: `
    :host ::ng-deep .NgxEditor__Content {
      height: 200px;
    }
  `,
})
export class SplendManageComponent implements OnInit, OnDestroy {
  isSaving = false;
  splend: any | null = null;
  editor: Editor = new Editor();
  html = '';
  title = '';
  tags: string[] = [];
  usersSharedCollection: IUser[] = [];
  versesSeachResultsModel: any;
  isSearchVersesDisabled = false;
  searchControl = new FormControl('');
  regexSearchControl = new FormControl('');
  isLoading = false;
  splendVariables: any[] = [];

  userInput: string = '';
  inputType: string = '';
  isModelLoaded: boolean = false; // Controls form visibility
  createVariableId = null;

  toolbar: Toolbar = [
    // default value
    ['bold', 'italic'],
    ['underline', 'strike'],
    ['code', 'blockquote'],
    ['ordered_list', 'bullet_list'],
    [{ heading: ['h1', 'h2', 'h3', 'h4', 'h5', 'h6'] }],
    ['link', 'image'],
    // or, set options for link:
    //[{ link: { showOpenInNewTab: false } }, 'image'],
    ['text_color', 'background_color'],
    ['align_left', 'align_center', 'align_right', 'align_justify'],
    ['horizontal_rule', 'format_clear', 'indent', 'outdent'],
    ['superscript', 'subscript'],
    ['undo', 'redo'],
  ];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected splendService = inject(SplendService);
  protected categoryService = inject(CategoryService);

  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);
  protected notification = inject(NzNotificationService);
  // eslint-disable-next-line @typescript-eslint/member-ordering

  ngOnInit(): void {
    this.editor = new Editor();
    this.activatedRoute.data.subscribe(({ splend }) => {
      this.splend = splend;
      if (splend) {
        this.getSplendVariable();
      }
    });

    this.searchControl.valueChanges
      .pipe(
        debounceTime(500), // Wait 300ms after the last keystroke
        distinctUntilChanged(), // Only emit if the value has changed
      )
      .subscribe((value: any) => {
        if (value.length < 2) {
          this.isLoading = false;
          return;
        }
        this.isSearchVersesDisabled = true;
        this.searchVerses(value);
      });
  }
  // make sure to destory the editor
  ngOnDestroy(): void {
    this.editor?.destroy();
  }

  searchVerses(event: any): void {
    this.isLoading = true;
    this.isSearchVersesDisabled = true;
    const searchValue = event;

    let regexSearchValue = this.regexSearchControl.value;
    if (searchValue && searchValue !== '') {
      if (!regexSearchValue || regexSearchValue === '') {
        this.regexSearchControl.setValue(searchValue);
        regexSearchValue = searchValue;
      }

      const searchVerseModel = {
        wordQuery: searchValue,
        regexQuery: regexSearchValue,
        applySeachQuries: true,
      };

      this.splendService.queryVersesByWord(searchVerseModel).subscribe(verses => {
        this.versesSeachResultsModel = verses.body;
        this.versesSeachResultsModel.word = searchValue;
        this.isSearchVersesDisabled = false;
        this.isLoading = false;
      });
    } else {
      this.isSearchVersesDisabled = false;
      this.isLoading = false;
    }
  }

  getSplendVariable(): void {
    this.splendService.getSplendVariables(this.splend.id).subscribe(_splendVariables => {
      this.splendVariables = _splendVariables.body;
    });
  }

  createSplendVariable(hasVariable: boolean): void {
    if (
      !(this.searchControl.value && this.searchControl.value !== '') ||
      !(this.regexSearchControl.value && this.regexSearchControl.value !== '')
    ) {
      this.notification.error(
        'Created New Splend Variable',
        `Splend '${this.splend.title}' : Failed To Create Variable Word And Regex Must Not Be Empty`,
        { nzPlacement: 'topLeft' },
      );
    }

    const splendId = this.splend.id;
    let splendVariablePayload: any = {
      search: {
        wordQuery: this.searchControl.value,
        regexQuery: this.regexSearchControl.value,
      },
    };

    if (hasVariable) {
      if (this.userInput && this.inputType && this.userInput !== '' && this.inputType !== '') {
        splendVariablePayload.variableName = this.userInput;
        splendVariablePayload.variableValue = this.inputType;
      }
    }

    this.splendService.createSplendVariable(splendId, splendVariablePayload).subscribe(payload => {
      this.getSplend();
      this.getSplendVariable();

      this.notification.success(
        'Created New Splend variable',
        `Splend '${payload.body.word}' was created successfully [${payload.body.id}]`,
        { nzPlacement: 'topLeft' },
      );
    });
  }

  highlightText(text: string, searchWord: string): string {
    if (this.regexSearchControl.value && this.searchControl.value && this.searchControl.value !== this.regexSearchControl.value) {
      return this.highlightTextRegex(text, this.regexSearchControl.value);
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

  previousState(): void {
    window.history.back();
  }

  save(): void {
    let splend: any = {};
    splend.title = this.title;
    splend.content = this.html;
    splend.tags = this.tags;

    this.splendService.create(splend).subscribe(splendCreated => {
      console.log('splend Created At ', splendCreated);
      this.onSaveSuccess(splendCreated);
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(null),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(splend: any): void {
    this.notification.success('Created New Splend', `Splend '${splend.title}' was created successfully`, { nzPlacement: 'topLeft' });

    // TODO go to someware after splend created
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  // Method to handle form submission
  submitInput() {
    if (this.userInput) {
      if (this.createVariableId === null) {
        this.handleCancel();
      } else {
        const varPayload = {
          name: this.userInput,
          value: this.inputType,
        };
        this.splendService.addSplendVariablesName(this.splend.id, this.createVariableId, varPayload).subscribe(() => {
          this.getSplend();
          this.getSplendVariable();
          this.createVariableId = null;
          this.handleCancel();
          this.inputType = '';
          this.userInput = '';
        });
      }
    } else {
      alert('Input is empty!');
    }
  }

  getSplend(): void {
    this.splendService.findById(this.splend.id).subscribe(splendFound => {
      this.splend = splendFound.body;
    });
  }

  deleteSplendVar(varId: string) {
    this.splendService.deleteSplendVariable(this.splend.id, varId).subscribe(() => {
      this.getSplend();
      this.getSplendVariable();
    });
  }

  deleteSplendVarName(varId: string, varName: string) {
    this.splendService.deleteSplendVariablesName(this.splend.id, varId, varName).subscribe(() => {
      this.getSplend();
      this.getSplendVariable();
    });
  }
  handleCancel(): void {
    this.isModelLoaded = false; // Close the modal on cancel
  }
}
