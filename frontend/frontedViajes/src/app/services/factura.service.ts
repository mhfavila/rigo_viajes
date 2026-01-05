import { Factura } from './../factura/factura.model';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({ providedIn: 'root' })
export class FacturaService {

  private baseUrl: string;

  constructor(private http: HttpClient) {
    const host = window.location.hostname;
    // Usamos la URL base + /facturas
    const apiBase = (host === 'localhost')
      ? 'http://localhost:8080/api'
      : 'https://rigo-viajes.onrender.com/api';

    this.baseUrl = `${apiBase}/facturas`;
  }

  getFacturaByEmpresa(empresaId: number): Observable<Factura[]> {
    return this.http.get<Factura[]>(`${this.baseUrl}/empresa/${empresaId}`);
  }

  descargarPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${id}/pdf`, { responseType: 'blob' });
  }

  crearFactura(factura: Factura): Observable<Factura> {
    return this.http.post<Factura>(this.baseUrl, factura);
  }


  eliminarFactura(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${id}`);
  }
}
