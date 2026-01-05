import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registro',
  standalone: false,
  templateUrl: './registro.component.html',
  styleUrl: './registro.component.css'
})
export class RegistroComponent {
  nombre = '';
  password = '';
  email = '';
  rol= '';
  mensaje = '';
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    const nuevoUsuario = { nombre: this.nombre, password: this.password, email: this.email,rol: this.rol };

    this.authService.registrar(nuevoUsuario).subscribe({
      next: (res) => {
        this.mensaje = 'Usuario creado correctamente';
        setTimeout(() => this.router.navigate(['/login']), 1000); // redirige al login
      },
      error: (err) => {
        this.error = 'Error al registrar usuario';
      }
    });

}
}
