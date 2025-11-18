import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Empresa } from '../empresa/empresa.model';



@Injectable({
  providedIn: 'root',
})
export class EmpresaService {
  private apiUrl = 'http://localhost:8080/api/usuarios/empresas/';

  private apiUrlEmpresa ='http://localhost:8080/api/empresas';


  constructor(private http: HttpClient) {}

  getEmpresasDeUsuario(usuarioId: number): Observable<any[]> {
    const token = sessionStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };

    console.log('Llamando a:', `${this.apiUrl}${usuarioId}`);
    console.log('Headers:', headers);

    return this.http.get<any[]>(`${this.apiUrl}${usuarioId}`, { headers });
  }

  crearEmpresa(empresa: Empresa): Observable<Empresa> {

    const token = sessionStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };



    return this.http.post<Empresa>(this.apiUrlEmpresa, empresa,{ headers });
  }
//  Editar empresa
  editarEmpresa(id: number, empresa: Empresa): Observable<Empresa> {
    const token = sessionStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };

    return this.http.put<Empresa>(`${this.apiUrlEmpresa}/${id}`, empresa, { headers });
  }
  //  Eliminar empresa
eliminarEmpresa(id: number): Observable<any> {
  const token = sessionStorage.getItem('token');
  const headers = { Authorization: `Bearer ${token}` };

  return this.http.delete(`${this.apiUrlEmpresa}/${id}`, { headers });
}


//metodo para en la factura sacar la empresa
getEmpresaPorId(empresaId: number) {
  const token = sessionStorage.getItem('token');
  const headers = { Authorization: `Bearer ${token}` };
  return this.http.get<Empresa>(`http://localhost:8080/api/empresas/${empresaId}`, { headers });
}

}
