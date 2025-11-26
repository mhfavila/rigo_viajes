import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Empresa } from '../empresa/empresa.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class EmpresaService {

  private baseUrl = environment.apiUrl;
  //private apiUrl = 'http://localhost:8080/api/usuarios/empresas/';

  //private apiUrlEmpresa = 'http://localhost:8080/api/empresas';

  constructor(private http: HttpClient) {}

  getEmpresasDeUsuario(usuarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/usuarios/empresas/${usuarioId}`);
  }

  crearEmpresa(empresa: Empresa): Observable<Empresa> {
    return this.http.post<Empresa>(`${this.baseUrl}/empresas`, empresa);
  }

  editarEmpresa(id: number, empresa: Empresa): Observable<Empresa> {
    return this.http.put<Empresa>(`${this.baseUrl}/empresas/${id}`, empresa);

  }

  eliminarEmpresa(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/empresas/${id}`);
  }

  getEmpresaPorId(empresaId: number) {

      return this.http.get<Empresa>(`${this.baseUrl}/empresas/${empresaId}`)

  }
}
