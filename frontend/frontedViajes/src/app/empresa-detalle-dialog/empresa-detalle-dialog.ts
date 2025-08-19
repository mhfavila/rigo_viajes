import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-empresa-detalle-dialog',
  standalone: false,
  templateUrl: './empresa-detalle-dialog.html',
  styleUrl: './empresa-detalle-dialog.css'
})
export class EmpresaDetalleDialog {

   constructor(
    public dialogRef: MatDialogRef<EmpresaDetalleDialog>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  cerrar(): void {
    this.dialogRef.close();
  }
}
