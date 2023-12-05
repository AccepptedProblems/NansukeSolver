import { TestBed } from '@angular/core/testing';

import { NansukeService } from './nansuke.service';

describe('NansukeService', () => {
  let service: NansukeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NansukeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
