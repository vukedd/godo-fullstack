import { TestBed } from '@angular/core/testing';

import { ManagesService } from './manages.service';

describe('ManagesService', () => {
  let service: ManagesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ManagesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
