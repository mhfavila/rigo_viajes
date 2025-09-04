import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Empresa {
 // id?: number;        // opcional al crear
  nombre: string;
  cif: string;
  direccion: string;
  telefono: string;
  email: string;
  usuarioId?: number;  // referencia al usuario propietario

}

@Injectable({
  providedIn: 'root',
})
export class EmpresaService {
  private apiUrl = 'http://localhost:8080/api/usuarios/empresas/';

  private apiUrlEmpresa ='http://localhost:8080/api/empresas';

  constructor(private http: HttpClient) {}

  getEmpresasDeUsuario(usuarioId: number): Observable<any[]> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };

    console.log('Llamando a:', `${this.apiUrl}${usuarioId}`);
    console.log('Headers:', headers);

    return this.http.get<any[]>(`${this.apiUrl}${usuarioId}`, { headers });
  }

  crearEmpresa(empresa: Empresa): Observable<Empresa> {

    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };



    return this.http.post<Empresa>(this.apiUrlEmpresa, empresa,{ headers });
  }
}
