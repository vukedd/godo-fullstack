import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationRequestManagementComponent } from './registration-request-management.component';

describe('RegistrationRequestManagementComponent', () => {
  let component: RegistrationRequestManagementComponent;
  let fixture: ComponentFixture<RegistrationRequestManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationRequestManagementComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationRequestManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
