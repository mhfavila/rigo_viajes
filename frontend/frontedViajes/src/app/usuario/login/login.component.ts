import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  nombre = '';
  password = '';
  errorMessage = '';

  // Variables para la animación de carga
  isLoading = false;
  showSlowServerMessage = false;
  // Variable privada para guardar el temporizador
  private slowServerTimer: any;

  constructor(private authService: AuthService, private router: Router) {}
  ngOnInit(): void {
    // Intentamos despertar al servidor en cuanto el usuario entra
    this.authService.wakeUpServer();
  }

  onSubmit(): void {
    const credentials = { nombre: this.nombre, password: this.password };

    // A) Antes de la petición: Activamos carga y limpiamos errores
    this.isLoading = true;
    this.errorMessage = '';
    this.showSlowServerMessage = false;

    // B) Iniciamos el contador: Si tarda más de 3 segundos, mostramos aviso
    this.slowServerTimer = setTimeout(() => {
      this.showSlowServerMessage = true;
    }, 3000);

    this.authService.login(credentials).subscribe({
      next: (response) => {
        this.authService.guardarToken(response.token);
        console.log('Token recibido:', response.token);
        //this.router.navigate(['/empresas'], { replaceUrl: true });//para que no vuelva al login sino que lo remplaza en el historial por el empresas
       // C) ÉXITO: Detenemos carga (aunque al navegar ya no se verá)
        this.detenerCarga();
        this.router.navigate(['/empresas']);
      },
      error: (error) => {
        console.error('Error al iniciar sesión:', error);
        this.errorMessage = 'Usuario o contraseña incorrectos';
        // D) ERROR: Muy importante detener la carga para que el usuario pueda intentar de nuevo
        this.detenerCarga();
      }
    });
  }
  // Método auxiliar para limpiar estados
  private detenerCarga() {
    this.isLoading = false;
    this.showSlowServerMessage = false;
    clearTimeout(this.slowServerTimer); // Cancelamos el aviso si ya respondió
  }

  irARegistro(): void {
  this.router.navigate(['/registro']);
}

}
