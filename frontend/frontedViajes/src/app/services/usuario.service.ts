import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../usuario/usuario.model';
import { AuthService } from './auth.service'; // Para saber la URL base

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  // Usamos una lógica similar a AuthService para la URL
  private baseUrl: string;

  constructor(private http: HttpClient) {
    const hostname = window.location.hostname;
    if (hostname === 'localhost' || hostname === '127.0.0.1') {
      this.baseUrl = 'http://localhost:8080/api/usuarios';
    } else {
      this.baseUrl = 'https://rigo-viajes.onrender.com/api/usuarios';
    }
  }

  // Obtener datos del usuario por ID
  getUsuario(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseUrl}/${id}`);
  }

  // Actualizar datos del usuario
  updateUsuario(id: number, usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/${id}`, usuario);
  }
}
