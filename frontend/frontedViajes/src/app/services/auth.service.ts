import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
   //private apiUrl = 'http://localhost:8080/api/auth/login';
   // URLs de los endpoints
  private loginUrl = 'http://localhost:8080/api/auth/login';
  private registerUrl = 'http://localhost:8080/api/auth/register';
  private logoutUrl ='http://localhost:8080/api/auth/logout';

  constructor(private http: HttpClient,private router: Router) { }

  login(credentials: { nombre: string; password: string }): Observable<any> {
    return this.http.post(this.loginUrl, credentials);
  }

 guardarToken(token: string) {
  sessionStorage.setItem('token', token);
  console.log('Token guardado:', sessionStorage.getItem('token')); // Verifica aquí
}

obtenerToken(): string | null {
  return sessionStorage.getItem('token');
}

  cerrarSesion(): void {
    sessionStorage.removeItem('token');
  }
 //para comprobar si esta loggeado
  isLoggedIn(): boolean {
    const token = this.obtenerToken();
    return token !== null && token !== '';
  }

  // Saber si hay usuario logueado
  estaLogueado(): boolean {
    return !!this.obtenerToken(); // true si hay token
  }
   // Método para registrar nuevo usuario
  registrar(usuario: { nombre: string, password: string, email: string }): Observable<{ mensaje: string }> {
    return this.http.post<{ mensaje: string }>(this.registerUrl, usuario);
  }

 // 🔑 Nuevo método para obtener el usuarioId desde el token
  getUsuarioId(): number | null {
  const token = this.obtenerToken();
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.usuarioId; // ahora existe
  } catch (error) {
    console.error('Error al decodificar el token:', error);
    return null;
  }
}

// NUEVO MÉTODO LOGOUT
  logout(): void {
    // 1. Avisar al backend para invalidar el token (Blacklist)
    // No necesitamos enviar el token manual en el body si tienes un Interceptor
    // que lo mete en la cabecera automáticamente.
    this.http.post(`${this.logoutUrl}`, {}).subscribe({
      next: () => {
        console.log('Backend notificado del logout');
      },
      error: (err) => {
        console.warn('Error avisando al backend, pero cerramos sesión local igual', err);
      }
    });

    // 2. Limpieza local (CRÍTICO)
    localStorage.removeItem('token'); // O sessionStorage.removeItem('token')

    // 3. Redirigir al login
    this.router.navigate(['/login']);
  }



}

