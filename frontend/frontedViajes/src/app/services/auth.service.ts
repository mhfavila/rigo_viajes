import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authUrl: string; // No le damos valor aquí

  constructor(private http: HttpClient, private router: Router) {
    // --- DETECCIÓN AUTOMÁTICA DE ENTORNO ---
    const hostname = window.location.hostname;

    if (hostname === 'localhost' || hostname === '127.0.0.1') {
      // Estoy en mi PC
      this.authUrl = 'http://localhost:8080/api/auth';
    } else {
      // Estoy en Vercel (Producción)
      this.authUrl = 'https://rigo-viajes.onrender.com/api/auth';
    }
    console.log('AuthService conectando a:', this.authUrl);
  }

  login(credentials: { nombre: string; password: string }): Observable<any> {
    return this.http.post(`${this.authUrl}/login`, credentials);
  }

  registrar(usuario: { nombre: string, password: string, email: string }): Observable<{ mensaje: string }> {
    return this.http.post<{ mensaje: string }>(`${this.authUrl}/register`, usuario);
  }

  // ... MANTÉN EL RESTO DE TUS MÉTODOS IGUAL (guardarToken, logout, etc.) ...
  guardarToken(token: string) { sessionStorage.setItem('token', token); }
  obtenerToken(): string | null { return sessionStorage.getItem('token'); }
  isLoggedIn(): boolean { return !!this.obtenerToken(); }
  estaLogueado(): boolean { return this.isLoggedIn(); }

  logout(): void {
    this.http.post(`${this.authUrl}/logout`, {}).subscribe({
      next: () => console.log('Logout OK'),
      error: (e) => console.warn('Logout local', e)
    });
    sessionStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  getUsuarioId(): number | null {
     const token = this.obtenerToken();
     if (!token) return null;
     try { return JSON.parse(atob(token.split('.')[1])).usuarioId; }
     catch (e) { return null; }
  }
}
