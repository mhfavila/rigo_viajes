import { EmpresaService } from '../../services/empresa.service';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {EmpresaModalData} from '../empresa-modal/empresa.interfaces';



@Component({
  selector: 'app-empresa-modal',
   // 'standalone: false' indica que este componente es parte de un NgModule y debe ser declarado en el array 'declarations' de un módulo
  standalone: false,
  templateUrl: './empresa-modal.component.html',
  styleUrl: './empresa-modal.component.css',
})
export class EmpresaModalComponent implements OnInit {
  empresaForm!: FormGroup;
  // Bandera (flag) para controlar el estado del modal: 'true' para Editar, 'false' para Crear.
  isEditMode: boolean = false;

  constructor(
    private fb: FormBuilder,
    private empresaService: EmpresaService,
    public dialogRef: MatDialogRef<EmpresaModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: EmpresaModalData
  ) {

    this.isEditMode = this.data.isEditMode;
  }


  ngOnInit(): void {
    this.empresaForm = this.fb.group({
      nombre: [this.data.nombre || '', Validators.required],
      cif: [this.data.cif || '', Validators.required],
      direccion: [this.data.direccion || '', Validators.required],
      telefono: [this.data.telefono || '', Validators.required],
      email: [this.data.email || '', [Validators.required, Validators.email]],
      iban:[this.data.iban || '', [Validators.required]],
      usuarioId: [
        { value: this.data.usuarioId, disabled: true }, // 'usuarioId' siempre vendrá en 'data'
        Validators.required,
      ],
    });

    // --- Lógica Adicional: Filtro de Teléfono en tiempo real ---
    // Nos suscribimos a los cambios de valor del campo 'telefono'.
    this.empresaForm.get('telefono')?.valueChanges.subscribe((val) => {
      if (val != null) {
        const numeros = val.replace(/[^0-9]/g, '');
        if (val !== numeros) {
          this.empresaForm
            .get('telefono')
            ?.setValue(numeros, { emitEvent: false });
        }
      }
    });
  }

// Método que se llama al pulsar el botón 'Guardar'.
  guardar() {
    if (this.empresaForm.valid) {
      const empresaData = this.empresaForm.getRawValue();

      if (this.isEditMode) {
    // --- MODO EDITAR ---
        this.empresaService.editarEmpresa(this.data.id!, empresaData).subscribe({
          next: (resp) => this.dialogRef.close(true),
          error: (err) => console.error('Error editando la empresa', err), // Opcional: Mostrar un snackbar de error al usuario
        });
      } else {
        // --- MODO CREAR ---
        this.empresaService.crearEmpresa(empresaData).subscribe({
          next: (resp) => this.dialogRef.close(true),
          error: (err) => console.error('Error creando la empresa', err),
        });
      }
    }
  }
// Método para el botón 'Cancelar'.
  cancelar() {
    this.dialogRef.close(false);
  }
}
