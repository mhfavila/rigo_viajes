import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
   //private apiUrl = 'http://localhost:8080/api/auth/login';
   // URLs de los endpoints
  private loginUrl = 'http://localhost:8080/api/auth/login';
  private registerUrl = 'http://localhost:8080/api/auth/register';

  constructor(private http: HttpClient) { }

  login(credentials: { nombre: string; password: string }): Observable<any> {
    return this.http.post(this.loginUrl, credentials);
  }

  guardarToken(token: string): void {
    localStorage.setItem('jwtToken', token);
  }

   obtenerToken(): string | null {
    return localStorage.getItem('jwtToken');
  }

  cerrarSesion(): void {
    localStorage.removeItem('jwtToken');
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
      // Si el token es un JWT: separar las partes
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.usuarioId; // 👈 asegúrate de que tu backend ponga "usuarioId" en el payload
    } catch (error) {
      console.error('Error al decodificar el token:', error);
      return null;
    }
  }



}

