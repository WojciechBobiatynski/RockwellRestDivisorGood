import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SendPinModalComponent } from './send-pin-modal.component';

describe('SendPinModalComponent', () => {
  let component: SendPinModalComponent;
  let fixture: ComponentFixture<SendPinModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SendPinModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SendPinModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
