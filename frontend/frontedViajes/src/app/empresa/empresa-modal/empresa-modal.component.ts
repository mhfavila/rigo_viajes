import { EmpresaService } from '../../services/empresa.service';
import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-empresa-modal',
  standalone: false,
  templateUrl: './empresa-modal.component.html',
  styleUrl: './empresa-modal.component.css',
})
export class EmpresaModalComponent {
  empresaForm: FormGroup;
  isEditMode: boolean = false;
  constructor(
    private fb: FormBuilder,
    private empresaService: EmpresaService,
    public dialogRef: MatDialogRef<EmpresaModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.isEditMode = !!data?.isEditMode;
    // Inicializamos el formulario con todos los campos
    this.empresaForm = this.fb.group({
      nombre: [data?.nombre || '', Validators.required],
      cif: [data?.cif || '', Validators.required],
      direccion: [data?.direccion || '', Validators.required],
      telefono: [data?.telefono || '', Validators.required],
      email: [data?.email || '', [Validators.required, Validators.email]],
      usuarioId: [
        { value: data?.usuarioId || 0, disabled: true },
        Validators.required,
      ],
    });

    // Suscribirse a cambios para filtrar números
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

  // Método para enviar los datos al backend y cerrar el modal
  guardar() {
    if (this.empresaForm.valid) {
      // getRawValue() incluye los campos deshabilitados
      const empresaData = this.empresaForm.getRawValue();
      console.log('isEditMode:', this.isEditMode);
      console.log('Data.id recibido:', this.data.id);
      console.log('Payload que voy a enviar:', empresaData);

      if (this.isEditMode) {
        console.log('Llamando a EDITAR empresa con id:', this.data.id);
        this.empresaService.editarEmpresa(this.data.id, empresaData).subscribe({
           next: (resp) => {
          console.log(' Respuesta EDITAR empresa:', resp);
          this.dialogRef.close(true);
        },

          error: (err) => console.error('Error editando la empresa', err),
        });
      } else {
         console.log('Llamando a CREAR empresa');
        this.empresaService.crearEmpresa(empresaData).subscribe({
          next: (resp) => {
          console.log('✅ Respuesta CREAR empresa:', resp);
          this.dialogRef.close(true);
        },
          error: (err) => console.error('Error creando la empresa', err),
        });
      }
    }
  }
  // Método para cerrar el modal sin guardar
  cancelar() {
    this.dialogRef.close(false);
  }
}
