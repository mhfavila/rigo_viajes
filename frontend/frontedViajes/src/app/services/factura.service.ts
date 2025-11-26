import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servicio } from './../servicio/servicio.model';
import { Factura } from '../factura/factura.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class FacturaService {
  private baseUrl = environment.apiUrl + '/facturas';
  constructor(private http: HttpClient) {}

  // Obtener facturas por empresa
  getFacturaByEmpresa(empresaId: number): Observable<Factura[]> {
    return this.http.get<Factura[]>(`${this.baseUrl}/empresa/${empresaId}`);
  }

  //metodo para descargar la factura
  descargarPdf(id: number): Observable<Blob> {// En este caso, MANTENEMOS responseType: 'blob', pero quitamos headers
    return this.http.get(`${this.baseUrl}/${id}/pdf`, {
      responseType: 'blob', // Esto sí se queda porque es configuración de respuesta, no de Auth
    });
  }

  crearFactura(factura: Factura): Observable<Factura> {
    return this.http.post<Factura>(this.baseUrl, factura);
  }
}
