import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpresaDetalleDialog } from './empresa-detalle-dialog';

describe('EmpresaDetalleDialog', () => {
  let component: EmpresaDetalleDialog;
  let fixture: ComponentFixture<EmpresaDetalleDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EmpresaDetalleDialog]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmpresaDetalleDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
