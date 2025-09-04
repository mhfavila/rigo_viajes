import { TestBed } from '@angular/core/testing';

import { ViajesServices } from './viajes.service';

describe('Viajes', () => {
  let service: ViajesServices;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ViajesServices);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
