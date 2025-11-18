import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servicio } from './../servicio/servicio.model'; // Asegúrate que la ruta es correcta
import { Empresa } from '../../app/empresa/empresa.model';
import { Factura } from '../../app/factura/factura.model';

@Injectable({
  providedIn: 'root',
})
export class ServiciosFactService {


  // Esta es ahora la URL base de TODA tu API, no solo de servicios asi puedo llamar a mas endpoints
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}



  //metodo para sacar los servicios de una empresa
  getServiciosByEmpresa(empresaId: number): Observable<Servicio[]> {
    const token = sessionStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };

    return this.http.get<Servicio[]>(`${this.apiUrl}/servicios/empresa/${empresaId}`, {
      headers,
    });
  }

  //metodo para sacar un servicio segun su id
  getServicioPorId(servicioId: number): Observable<Servicio> {
    const token = sessionStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };

    return this.http.get<Servicio>(`${this.apiUrl}/servicios/${servicioId}`, {
      headers,
    });
  }

  //crear servicio
  crearServicio(servicio: Servicio): Observable<Servicio> {
    const token = sessionStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };

    return this.http.post<Servicio>(`${this.apiUrl}/servicios`, servicio, { headers });
  }

  //editar servicio
  editarServicio(id: number, servicio: Servicio): Observable<Servicio> {
    const token = sessionStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };

    return this.http.put<Servicio>(`${this.apiUrl}/servicios/${id}`, servicio, {
      headers,
    });
  }

  //eliminar servicio
  eliminarServicio(servicioId: number): Observable<any> {
    const token = sessionStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };

    return this.http.delete(`${this.apiUrl}/servicios/${servicioId}`, { headers });
  }



  getEmpresaPorId(id: number): Observable<Empresa> {
    const token = sessionStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };

    // 3. Ruta corregida: apunta a '/empresas'
    return this.http.get<Empresa>(`${this.apiUrl}/empresas/${id}`, { headers });
  }

  getFacturaPorId(id: number): Observable<Factura> {
    const token = sessionStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };

    // 3. Ruta corregida: apunta a '/facturas'
    return this.http.get<Factura>(`${this.apiUrl}/facturas/${id}`, { headers });
  }
}
