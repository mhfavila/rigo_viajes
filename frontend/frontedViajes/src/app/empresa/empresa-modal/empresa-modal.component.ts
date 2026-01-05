import { EmpresaService } from '../../services/empresa.service';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EmpresaModalData } from '../empresa-modal/empresa.interfaces';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

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
  selectedTabIndex: number = 0;//Variable para controlar el índice de la pestaña activa (0 = General, 1 = Dirección)
  constructor(
    private fb: FormBuilder,
    private empresaService: EmpresaService,
    public dialogRef: MatDialogRef<EmpresaModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: EmpresaModalData
  ) {
    this.isEditMode = this.data.isEditMode;
  }

  ngOnInit(): void {

    const cifPattern = /^[A-Z0-9]{8,10}$/;
    this.empresaForm = this.fb.group({
      nombre: [this.data.nombre || '', Validators.required],

      //cif: [this.data.cif || '', [Validators.required, cifValidator()]],
      cif: [this.data?.cif || '', [Validators.required, Validators.pattern(cifPattern)]],
      direccion: this.fb.group({
        calle: [this.data.direccion?.calle || '', Validators.required],
        numero: [this.data.direccion?.numero || '', Validators.required],
        codigoPostal: [this.data.direccion?.codigoPostal || '', Validators.required],
        ciudad: [this.data.direccion?.ciudad || '', Validators.required],
        provincia: [this.data.direccion?.provincia || '', Validators.required],
        pais: [this.data.direccion?.pais || 'España', Validators.required]
      }),

      telefono: [
        this.data.telefono || '',
        [
          Validators.required,
          Validators.pattern(/^[0-9]{9}$/),
        ],
      ],
      email: [this.data.email || '', [Validators.required, Validators.email]],
      //iban:[this.data.iban || '', [Validators.required]],
      iban: [this.data.iban || '', [Validators.required, ibanValidator()]],
      // Inicializamos a 0 si es null para evitar errores visuales
      precioKmDefecto: [this.data?.precioKmDefecto || 0, [Validators.min(0)]],
      precioHoraEsperaDefecto: [this.data?.precioHoraEsperaDefecto || 0, [Validators.min(0)]],
      precioDietaDefecto: [this.data?.precioDietaDefecto || 0, [Validators.min(0)]],
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
    // CAMBIO 2: Validar todo antes de guardar
    // Si el usuario está en la pestaña 1 pero el error está en la pestaña 2,
    // esto marcará la pestaña en rojo.

      this.empresaForm.markAllAsTouched();
      // 2. Comprobamos si el formulario es inválido
    if (this.empresaForm.invalid) {

      // LÓGICA INTELIGENTE:
      // Si el grupo 'direccion' tiene errores, forzamos ir a la pestaña 1
      if (this.empresaForm.get('direccion')?.invalid) {
        this.selectedTabIndex = 1;
      }
      // Si el error no es de dirección (es de nombre, cif, etc.), volvemos a la pestaña 0
      else {
        this.selectedTabIndex = 0;
      }

      // Detenemos la ejecución, no guardamos nada
      return;
    }



    const empresaData = this.empresaForm.getRawValue();
    empresaData.cif = empresaData.cif.toUpperCase();


    if (this.isEditMode) {
      this.empresaService.editarEmpresa(this.data.id!, empresaData).subscribe({
        next: (resp) => this.dialogRef.close(true),
        error: (err) => console.error('Error editando la empresa', err),
      });
    } else {
      this.empresaService.crearEmpresa(empresaData).subscribe({
        next: (resp) => this.dialogRef.close(true),
        error: (err) => console.error('Error creando la empresa', err),
      });
    }
  }
  // Método para el botón 'Cancelar'.
  cancelar() {
    this.dialogRef.close(false);
  }
}

// --- VALIDATOR 1: CIF ESPAÑOL (Simplificado para Regex) ---
export function cifValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (!value) return null; // Si está vacío, dejamos que el Validators.required se encargue

    // Regex básica para CIF/NIF: Letra inicial + números + letra/número final
    // Ejemplo: B12345678
    const cifRegex = /^[A-HJ-NP-SUVW][0-9]{7}[0-9A-J]$/i;

    // Si NO cumple el regex, devolvemos el error 'cifInvalido'
    return cifRegex.test(value) ? null : { cifInvalido: true };
  };
}

// --- VALIDATOR 2: IBAN (Sin espacios y longitud exacta) ---
export function ibanValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    let value = control.value;
    if (!value) return null;

    // 1. Comprobamos si tiene espacios
    if (value.toString().includes(' ')) {
      return { tieneEspacios: true };
    }

    // 2. Comprobamos longitud (IBAN español son 24 caracteres: ES + 2 dígitos control + 20 cuenta)
    if (value.length !== 24) {
      return { longitudIncorrecta: true };
    }

    // 3. Comprobamos que empiece por ES (opcional, pero recomendado)
    if (!value.toString().toUpperCase().startsWith('ES')) {
      return { noEsEspanol: true };
    }

    return null; // Todo correcto
  };
}
