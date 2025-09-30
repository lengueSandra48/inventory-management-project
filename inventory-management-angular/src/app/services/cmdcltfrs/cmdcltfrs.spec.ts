import { TestBed } from '@angular/core/testing';

import { Cmdcltfrs } from './cmdcltfrs';

describe('Cmdcltfrs', () => {
  let service: Cmdcltfrs;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Cmdcltfrs);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
