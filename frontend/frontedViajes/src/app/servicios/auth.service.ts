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

   // Método para registrar nuevo usuario
  registrar(usuario: { nombre: string, password: string, email: string }): Observable<{ mensaje: string }> {
    return this.http.post<{ mensaje: string }>(this.registerUrl, usuario);
  }
}

