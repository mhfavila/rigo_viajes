import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DatosFacturaDialogComponent } from './datos-factura-dialog.component';

describe('DatosFacturaDialogComponent', () => {
  let component: DatosFacturaDialogComponent;
  let fixture: ComponentFixture<DatosFacturaDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DatosFacturaDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DatosFacturaDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
