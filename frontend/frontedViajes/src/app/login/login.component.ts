import { Component } from '@angular/core';
import { AuthService } from '../servicios/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  nombre = '';
  password = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    const credentials = { nombre: this.nombre, password: this.password };

    this.authService.login(credentials).subscribe({
      next: (response) => {
        this.authService.guardarToken(response.token);
        console.log('Token recibido:', response.token);
        this.router.navigate(['/viajes']);  // Redirige al listado de viajes o donde quieras
      },
      error: (error) => {
        console.error('Error al iniciar sesión:', error);
        this.errorMessage = 'Usuario o contraseña incorrectos';
      }
    });
  }

  irARegistro(): void {
  this.router.navigate(['/registro']);
}

}
