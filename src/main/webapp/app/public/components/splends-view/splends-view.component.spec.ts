import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SplendsViewComponent } from './splends-view.component';

describe('SplendsViewComponent', () => {
  let component: SplendsViewComponent;
  let fixture: ComponentFixture<SplendsViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SplendsViewComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SplendsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
