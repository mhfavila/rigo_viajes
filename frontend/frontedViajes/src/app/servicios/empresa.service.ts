import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {
  private apiUrl = 'http://localhost:8080/api/usuarios/empresas/';

constructor(private http: HttpClient) {}

  getEmpresasDeUsuario(usuarioId: number): Observable<any[]> {
  const token = localStorage.getItem('token');
  const headers = { Authorization: `Bearer ${token}` };

  console.log('Llamando a:', `${this.apiUrl}${usuarioId}`);
  console.log('Headers:', headers);

  return this.http.get<any[]>(`${this.apiUrl}${usuarioId}`, { headers });
}
}
