import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
// Asegúrate de que la ruta al environment sea la correcta según tu estructura de carpetas
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // Construimos la URL base usando la variable de entorno.
  // En local será: http://localhost:8080/api/auth
  // En prod será: https://rigo-viajes.onrender.com/api/auth
  private authUrl = environment.apiUrl + '/auth';


  constructor(private http: HttpClient, private router: Router) { }

  // --- LOGIN ---
  login(credentials: { nombre: string; password: string }): Observable<any> {
    console.log(this.authUrl);
    return this.http.post(`${this.authUrl}/login`, credentials);
  }

  // --- REGISTRO ---
  registrar(usuario: { nombre: string, password: string, email: string }): Observable<{ mensaje: string }> {
    return this.http.post<{ mensaje: string }>(`${this.authUrl}/register`, usuario);
  }

  // --- GESTIÓN DE TOKENS (SessionStorage) ---
  guardarToken(token: string) {
    sessionStorage.setItem('token', token);
    // console.log('Token guardado:', token); // Descomentar solo para depurar
  }

  obtenerToken(): string | null {
    return sessionStorage.getItem('token');
  }

  // --- ESTADO DEL USUARIO ---
  // Unificado: Devuelve true si hay token, false si no.
  isLoggedIn(): boolean {
    const token = this.obtenerToken();
    return token !== null && token !== '';
  }

  // Método alias por si lo usas con este nombre en otros componentes
  estaLogueado(): boolean {
    return this.isLoggedIn();
  }

  // --- LOGOUT COMPLETO ---
  logout(): void {
    // 1. Avisar al backend para invalidar el token (Blacklist)
    this.http.post(`${this.authUrl}/logout`, {}).subscribe({
      next: () => {
        console.log('Backend notificado del logout correctamente');
      },
      error: (err) => {
        console.warn('Aviso: El backend no respondió al logout (quizás el token ya expiró), pero cerramos sesión local igual.', err);
      }
    });

    // 2. Limpieza local (CRÍTICO)
    // Usamos sessionStorage porque es lo que usas en guardarToken
    sessionStorage.removeItem('token');
    sessionStorage.clear(); // Por si guardas algo más (como usuarioId o rol)

    // 3. Redirigir al login
    this.router.navigate(['/login']);
  }

  // --- UTILIDADES DEL TOKEN ---
  getUsuarioId(): number | null {
    const token = this.obtenerToken();
    if (!token) return null;
    try {
      // Decodifica la parte del payload del JWT
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.usuarioId;
    } catch (error) {
      console.error('Error al decodificar el token:', error);
      return null;
    }
  }
}
