import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
// 1. IMPORTAMOS LAS HERRAMIENTAS DE FORMULARIO
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  // 2. SUSTITUIMOS LAS VARIABLES SUELTAS POR EL FORMGROUP
  loginForm!: FormGroup;

  errorMessage = '';

  // Variables para la animación de carga
  isLoading = false;
  showSlowServerMessage = false;
  private slowServerTimer: any;

  // 3. INYECTAMOS EL 'FormBuilder' (fb) EN EL CONSTRUCTOR
  constructor(
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // Intentamos despertar al servidor
    this.authService.wakeUpServer();

    // 4. INICIALIZAMOS EL FORMULARIO CON VALIDACIONES
    this.loginForm = this.fb.group({
      nombreUsuario: ['', [Validators.required, Validators.email]], // Valida que sea email
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    // 5. SI EL FORMULARIO NO ES VÁLIDO, NO HACEMOS NADA
    if (this.loginForm.invalid) {
      return;
    }

    // OBTENEMOS LOS VALORES DEL FORMULARIO
    const { nombreUsuario, password } = this.loginForm.value;

    // Preparamos el objeto para enviar (asegúrate de que el backend espera 'nombreUsuario' o 'nombre')
    // Si tu backend AuthRequest tiene el campo 'nombreUsuario', úsalo así.
    // Si sigue usando 'nombre', cámbialo a: { nombre: nombreUsuario, password: password }
    const credentials = { nombre:nombreUsuario, password:password };

    // A) Antes de la petición: Activamos carga y limpiamos errores
    this.isLoading = true;
    this.errorMessage = '';
    this.showSlowServerMessage = false;

    // B) Iniciamos el contador
    this.slowServerTimer = setTimeout(() => {
      this.showSlowServerMessage = true;
    }, 3000);

    this.authService.login(credentials).subscribe({
      next: (response) => {
        this.authService.guardarToken(response.token);
        console.log('Token recibido:', response.token);
        this.detenerCarga();
        this.router.navigate(['/empresas']);
      },
      error: (error) => {
        console.error('Error al iniciar sesión:', error);
        this.errorMessage = 'Correo o contraseña incorrectos';
        this.detenerCarga();
      }
    });
  }

  private detenerCarga() {
    this.isLoading = false;
    this.showSlowServerMessage = false;
    clearTimeout(this.slowServerTimer);
  }

  irARegistro(): void {
    this.router.navigate(['/registro']);
  }
}
