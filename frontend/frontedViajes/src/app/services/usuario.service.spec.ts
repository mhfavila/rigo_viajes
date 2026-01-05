import { TestBed } from '@angular/core/testing';

import { UsuarioService } from './usuario.service.js';

describe('UsuarioServiceTs', () => {
  let service: UsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsuarioService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
