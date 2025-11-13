import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';

/**
 * Componente genérico para diálogos de confirmación.
 * Recibe un mensaje a través de MAT_DIALOG_DATA.
 * Devuelve 'true' si se confirma, 'false' si se cancela.
 */
@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrl: './confirm-dialog.component.css',
  standalone: true, // Lo hacemos Standalone para importarlo fácilmente
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule
  ]
})
export class ConfirmDialogComponent {

  // Injectamos MatDialogRef para poder cerrar este diálogo
  // Injectamos MAT_DIALOG_DATA para recibir el mensaje a mostrar
  constructor(
    public dialogRef: MatDialogRef<ConfirmDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { message: string }
  ) {}

  /**
   * Cierra el diálogo devolviendo 'false' (Cancelado)
   */
  onCancel(): void {
    this.dialogRef.close(false);
  }

  /**
   * Cierra el diálogo devolviendo 'true' (Confirmado)
   */
  onConfirm(): void {
    this.dialogRef.close(true);
  }
}
