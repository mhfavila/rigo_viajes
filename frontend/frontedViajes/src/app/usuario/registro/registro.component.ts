import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; // Importante
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registro',
  standalone: false,
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent implements OnInit {

  registroForm!: FormGroup; // Usamos FormGroup
  errorMessage: string = '';
  successMessage: string = '';
  isLoading: boolean = false;

  constructor(
    private fb: FormBuilder, // Inyectamos FormBuilder
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Definimos las reglas del formulario
    this.registroForm = this.fb.group({
      nombre: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]], // Valida formato email
      password: ['', [Validators.required, Validators.minLength(6)]], // Valida min 6 caracteres
      rol: ['USER', Validators.required] // Por defecto USER
    });
  }

  onSubmit(): void {
    // Si el formulario no es válido (ej: pass corta), no enviamos nada
    if (this.registroForm.invalid) {
      this.registroForm.markAllAsTouched(); // Marca todo rojo para que el usuario lo vea
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const usuarioData = this.registroForm.value;

    this.authService.registrar(usuarioData).subscribe({
      next: (res) => {
        this.isLoading = false;
        this.successMessage = '¡Usuario registrado con éxito! Redirigiendo...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error registro:', err);

        // LÓGICA PARA CAPTURAR "EMAIL YA EXISTE"
        if (err.status === 400) {
          if (typeof err.error === 'string') {
            this.errorMessage = err.error; // Backend devuelve texto plano
          } else if (err.error && err.error.message) {
            this.errorMessage = err.error.message; // Backend devuelve JSON
          } else {
            this.errorMessage = 'Datos inválidos o correo duplicado.';
          }
        } else {
          this.errorMessage = 'Error de conexión. Inténtalo más tarde.';
        }
      }
    });
  }
}
