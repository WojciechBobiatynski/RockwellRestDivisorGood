import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndividualUserServiceComponent } from './individual-user-service.component';

describe('IndividualUserServiceComponent', () => {
  let component: IndividualUserServiceComponent;
  let fixture: ComponentFixture<IndividualUserServiceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IndividualUserServiceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(IndividualUserServiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
