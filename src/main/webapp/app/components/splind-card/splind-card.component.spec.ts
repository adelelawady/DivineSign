import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SplindCardComponent } from './splind-card.component';

describe('SplindCardComponent', () => {
  let component: SplindCardComponent;
  let fixture: ComponentFixture<SplindCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SplindCardComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(SplindCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
