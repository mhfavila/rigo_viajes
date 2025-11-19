import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Empresa } from '../empresa/empresa.model';

@Injectable({
  providedIn: 'root',
})
export class EmpresaService {
  private apiUrl = 'http://localhost:8080/api/usuarios/empresas/';

  private apiUrlEmpresa = 'http://localhost:8080/api/empresas';

  constructor(private http: HttpClient) {}

  getEmpresasDeUsuario(usuarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}${usuarioId}`);
  }

  crearEmpresa(empresa: Empresa): Observable<Empresa> {
    return this.http.post<Empresa>(this.apiUrlEmpresa, empresa);
  }

  editarEmpresa(id: number, empresa: Empresa): Observable<Empresa> {
    return this.http.put<Empresa>(`${this.apiUrlEmpresa}/${id}`, empresa);
  }

  eliminarEmpresa(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrlEmpresa}/${id}`);
  }

  getEmpresaPorId(empresaId: number) {
    return this.http.get<Empresa>(
      `http://localhost:8080/api/empresas/${empresaId}`
    );
  }
}
