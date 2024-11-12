import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SplindCommentComponent } from './splind-comment.component';

describe('SplindCommentComponent', () => {
  let component: SplindCommentComponent;
  let fixture: ComponentFixture<SplindCommentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SplindCommentComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SplindCommentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
