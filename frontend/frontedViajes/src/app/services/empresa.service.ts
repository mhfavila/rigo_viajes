import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Empresa } from '../empresa/empresa.model';

@Injectable({ providedIn: 'root' })
export class EmpresaService {

  private baseUrl: string;

  constructor(private http: HttpClient) {
    if (window.location.hostname === 'localhost') {
      this.baseUrl = 'http://localhost:8080/api';
    } else {
      this.baseUrl = 'https://rigo-viajes.onrender.com/api';
    }
  }

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

  getEmpresaPorId(empresaId: number): Observable<Empresa> {
    return this.http.get<Empresa>(`${this.baseUrl}/empresas/${empresaId}`);
  }
}
