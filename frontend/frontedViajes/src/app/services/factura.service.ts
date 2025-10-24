import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servicio } from './../servicio/servicio.model';
import { Factura } from '../factura/factura.model';






@Injectable({
  providedIn: 'root'
})
export class FacturaService {

  private baseUrl = 'http://localhost:8080/api/facturas';
  constructor(private http: HttpClient) {}

  getFacturaByEmpresa(empresaId: number): Observable<Factura[]> {
      const token = localStorage.getItem('token');
      const headers = { Authorization: `Bearer ${token}` };
      console.log(empresaId)
      return this.http.get<Factura[]>(`${this.baseUrl}/empresa/${empresaId}`, {//se anade a la url el /empresa/id
        headers,
      });
    }

}
