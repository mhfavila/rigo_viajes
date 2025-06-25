import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
   private apiUrl = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient) { }

  login(credentials: { nombre: string; password: string }): Observable<any> {
    return this.http.post(this.apiUrl, credentials);
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
}
