import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-direccion-dialog.component',
  standalone: false,
  templateUrl: './direccion-dialog.component.html',
  styleUrl: './direccion-dialog.component.css',
})
export class DireccionDialogComponent implements OnInit {
  form!: FormGroup;
  titulo: string = 'Dirección';
  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<DireccionDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any // Recibimos la dirección actual o null
  ) {
    this.titulo = data.titulo || 'Dirección';
  }
  ngOnInit(): void {
    // Inicializamos con los datos que nos pasen o vacíos
    const dir = this.data.direccion || {};

    this.form = this.fb.group({
      calle: [dir.calle || '', Validators.required],
      numero: [dir.numero || '', Validators.required],
      codigoPostal: [dir.codigoPostal || '', Validators.required],
      ciudad: [dir.ciudad || '', Validators.required],
      provincia: [dir.provincia || '', Validators.required],
      pais: [dir.pais || 'España', Validators.required],
    });
  }

  guardar() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value); // Devolvemos el objeto JSON
    } else {
      this.form.markAllAsTouched();
    }
  }

  cancelar() {
    this.dialogRef.close();
  }
}
