/* eslint-disable */
import { Component, EventEmitter, Input, Output } from '@angular/core';
import SharedModule from 'app/shared/shared.module';
import { NzTagModule } from 'ng-zorro-antd/tag';

@Component({
  selector: 'jhi-tags-input',
  standalone: true,
  imports: [SharedModule, NzTagModule],
  templateUrl: './tags-input.component.html',
  styleUrl: './tags-input.component.scss',
})
export class TagsInputComponent {
  @Input() tags: string[] = []; // Property to hold the list of tags
  @Output() tagsChange = new EventEmitter<string[]>(); // Event to notify changes in the tags

  newTag: string = '';

  inputVisible = false;
  inputValue = '';

  // Show input field for new tag
  showInput(): void {
    this.inputVisible = true;
  }

  addTag(): void {
    if (this.newTag.trim() && !this.tags.includes(this.newTag.trim())) {
      this.tags = [...this.tags, this.newTag.trim()]; // Update the tags array
      this.tagsChange.emit(this.tags); // Emit the updated array
      this.newTag = '';
      this.inputValue = '';
      this.inputVisible = false;
    }
  }

  removeTag(tag: string): void {
    this.tags = this.tags.filter(t => t !== tag); // Remove the tag
    this.tagsChange.emit(this.tags); // Emit the updated array
  }
}
