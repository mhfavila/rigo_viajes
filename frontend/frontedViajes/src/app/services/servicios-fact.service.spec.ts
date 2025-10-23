import { TestBed } from '@angular/core/testing';

import { ServiciosFactService } from './servicios-fact.service';

describe('ServiciosFactService', () => {
  let service: ServiciosFactService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiciosFactService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
