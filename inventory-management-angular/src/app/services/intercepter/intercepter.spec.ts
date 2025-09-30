import { TestBed } from '@angular/core/testing';

import { Intercepter } from './intercepter';

describe('Intercepter', () => {
  let service: Intercepter;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Intercepter);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
