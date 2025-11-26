import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servicio } from './../servicio/servicio.model';
import { Empresa } from '../../app/empresa/empresa.model';
import { Factura } from '../../app/factura/factura.model';

@Injectable({ providedIn: 'root' })
export class ServiciosFactService {

  private apiUrl: string;

  constructor(private http: HttpClient) {
    if (window.location.hostname === 'localhost') {
      this.apiUrl = 'http://localhost:8080/api';
    } else {
      this.apiUrl = 'https://rigo-viajes.onrender.com/api';
    }
  }

  getServiciosByEmpresa(empresaId: number): Observable<Servicio[]> {
    return this.http.get<Servicio[]>(`${this.apiUrl}/servicios/empresa/${empresaId}`);
  }

  getServicioPorId(servicioId: number): Observable<Servicio> {
    return this.http.get<Servicio>(`${this.apiUrl}/servicios/${servicioId}`);
  }

  crearServicio(servicio: Servicio): Observable<Servicio> {
    return this.http.post<Servicio>(`${this.apiUrl}/servicios`, servicio);
  }

  editarServicio(id: number, servicio: Servicio): Observable<Servicio> {
    return this.http.put<Servicio>(`${this.apiUrl}/servicios/${id}`, servicio);
  }

  eliminarServicio(servicioId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/servicios/${servicioId}`);
  }

  getEmpresaPorId(id: number): Observable<Empresa> {
    return this.http.get<Empresa>(`${this.apiUrl}/empresas/${id}`);
  }

  getFacturaPorId(id: number): Observable<Factura> {
    return this.http.get<Factura>(`${this.apiUrl}/facturas/${id}`);
  }
}
