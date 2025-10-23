import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Viaje {
  id: number;
  destino: string;
  distancia: number;
  precioKm: number;
  fecha: string;
  empresaId: number;
}

@Injectable({
  providedIn: 'root',
})
export class ViajesServices {
  private baseUrl = 'http://localhost:8080/api/viajes';

  constructor(private http: HttpClient) {}

  getViajesByEmpresa(empresaId: number): Observable<Viaje[]> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.get<Viaje[]>(`${this.baseUrl}/empresa/${empresaId}`, {//se anade a la url el /empresa/id
      headers,
    });
  }
}
