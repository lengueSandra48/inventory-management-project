import { TestBed } from '@angular/core/testing';

import { Cltfrs } from './cltfrs';

describe('Cltfrs', () => {
  let service: Cltfrs;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Cltfrs);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
