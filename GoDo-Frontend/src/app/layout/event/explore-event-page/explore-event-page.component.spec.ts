import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExploreEventPageComponent } from './explore-event-page.component';

describe('ExploreEventPageComponent', () => {
  let component: ExploreEventPageComponent;
  let fixture: ComponentFixture<ExploreEventPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExploreEventPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExploreEventPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
