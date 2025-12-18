import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authUrl: string = `${environment.apiUrl}/auth`;

  constructor(private http: HttpClient, private router: Router) {
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
     try {
       const decoded: any = jwtDecode(token);
       return decoded.usuarioId;
     } catch (e) {
       console.error('Error al decodificar token', e);
       return null;
     }
  }

 wakeUpServer() {
  // Truco: Reemplazamos '/auth' por '/ping' para salir de la ruta de seguridad
  const pingUrl = this.authUrl.replace('/auth', '/ping');
  this.http.get(pingUrl).subscribe();
}
}
