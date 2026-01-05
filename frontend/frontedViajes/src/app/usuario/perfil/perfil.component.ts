
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuarioService } from '../../services/usuario.service';
import { AuthService } from '../../services/auth.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-perfil',
   standalone: false,
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit {

  perfilForm!: FormGroup;
  userId: number | null = null;
  loading = true;

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private authService: AuthService, // Para obtener el ID del token
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    // 1. Obtener ID del usuario logueado desde el token
    this.userId = this.authService.getUsuarioId();

    this.initForm();

    if (this.userId) {
      this.cargarDatos();
    } else {
      console.error('No se pudo obtener el ID del usuario');
      this.loading = false;
    }
  }

  initForm(): void {
    this.perfilForm = this.fb.group({
      id: [null],
      nombre: [{value: '', disabled: true}], // Solo lectura
      email: [{value: '', disabled: true}],  // Solo lectura

      // --- DATOS FISCALES ---
      // Acepta DNI (12345678Z) o CIF (B12345678)
      nif: ['', [Validators.pattern(/^[A-Z0-9]{8,10}$/)]],
      telefono: ['', Validators.pattern(/^[0-9+\-\s]{9,20}$/)],
      cuentaBancaria: [''],
      imagenUrl: [''],

      // --- DIRECCIÓN (Grupo Anidado) ---
      direccion: this.fb.group({
        calle: ['', Validators.required],
        numero: ['', Validators.required],
        codigoPostal: ['', Validators.required],
        ciudad: ['', Validators.required],
        provincia: ['', Validators.required],
        pais: ['España', Validators.required]
      })
    });
  }

  cargarDatos() {
    if (!this.userId) return;

    this.usuarioService.getUsuario(this.userId).subscribe({
      next: (usuario) => {
        // Rellenamos el formulario con los datos que vienen del backend
        this.perfilForm.patchValue(usuario);

        // Si la dirección venía nula del backend, patchValue no falla,
        // pero los campos seguirán vacíos.
        this.loading = false;
      },
      error: (err) => {
        console.error('Error cargando perfil', err);
        this.mostrarNotificacion('Error al cargar los datos', 'error');
        this.loading = false;
      }
    });
  }

  guardar() {
    if (this.perfilForm.invalid) {
      this.perfilForm.markAllAsTouched();
      return;
    }

    if (!this.userId) return;

    // getRawValue incluye los campos deshabilitados (nombre, email)
    const datosParaEnviar = this.perfilForm.getRawValue();

    // Aseguramos NIF en mayúsculas
    if (datosParaEnviar.nif) {
      datosParaEnviar.nif = datosParaEnviar.nif.toUpperCase();
    }

    this.usuarioService.updateUsuario(this.userId, datosParaEnviar).subscribe({
      next: (res) => {
        this.mostrarNotificacion('Perfil actualizado correctamente', 'success');
      },
      error: (err) => {
        console.error(err);
        this.mostrarNotificacion('Error al guardar cambios', 'error');
      }
    });
  }

  mostrarNotificacion(mensaje: string, tipo: 'success' | 'error') {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: tipo === 'success' ? ['mat-toolbar', 'mat-primary'] : ['mat-toolbar', 'mat-warn']
    });
  }
}
