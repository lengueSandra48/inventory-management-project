import { TestBed } from '@angular/core/testing';

import { ApplicationGuard } from './application-guard';

describe('ApplicationGuard', () => {
  let service: ApplicationGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApplicationGuard);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
