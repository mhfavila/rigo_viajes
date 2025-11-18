
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

// Interfaz sencilla para los datos
export interface DatosFacturaInput {
  iva: number;
  irpf: number;
}

@Component({
  selector: 'app-datos-factura-dialog.component',
  standalone: false,
  templateUrl: './datos-factura-dialog.component.html',
  styleUrl: './datos-factura-dialog.component.css',
})
export class DatosFacturaDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<DatosFacturaDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DatosFacturaInput
  ) {}

  cancelar(): void {
    this.dialogRef.close(); // Cierra sin devolver nada
  }

  aceptar(): void {
    this.dialogRef.close(this.data); // Cierra y devuelve los datos modificados
  }

}
