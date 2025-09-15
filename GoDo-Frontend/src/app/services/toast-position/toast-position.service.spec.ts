import { TestBed } from '@angular/core/testing';
import { ToastPositionService } from '../toast-position/toast-position.service';

describe('ToastPositionService', () => {
  let service: ToastPositionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ToastPositionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
