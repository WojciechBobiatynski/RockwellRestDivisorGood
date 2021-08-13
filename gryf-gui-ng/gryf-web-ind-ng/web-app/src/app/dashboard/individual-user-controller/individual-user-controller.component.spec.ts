import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndividualUserControllerComponent } from './individual-user-controller.component';

describe('IndividualUserControllerComponent', () => {
  let component: IndividualUserControllerComponent;
  let fixture: ComponentFixture<IndividualUserControllerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IndividualUserControllerComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IndividualUserControllerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
