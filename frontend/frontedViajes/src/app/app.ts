import { Component } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  standalone: false,
  styleUrl: './app.css'
})
export class App {
  protected title = 'frontedViajes';

  constructor(public authService: AuthService, private router: Router) {}

  // Método para el botón de Salir
  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  // Helper para el HTML (opcional, si authService.isLoggedIn es public)
  estaLogueado(): boolean {
    return this.authService.isLoggedIn();
  }
}
