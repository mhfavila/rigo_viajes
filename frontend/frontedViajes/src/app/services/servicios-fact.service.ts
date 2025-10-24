import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Servicio } from './../servicio/servicio.model';


@Injectable({
  providedIn: 'root',
})
export class ServiciosFactService {
  private baseUrl = 'http://localhost:8080/api/servicios';

  constructor(private http: HttpClient) {}
  //metodo para sacar los servicios de una empresa
  getServiciosByEmpresa(empresaId: number): Observable<Servicio[]> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.get<Servicio[]>(`${this.baseUrl}/empresa/${empresaId}`, {
      //se anade a la url el /empresa/id
      headers,
    });
  }
//metodo para sacar un servicio segun su id
  getServicioPorId(servicioId: number): Observable<Servicio> {
    const token = localStorage.getItem('token');
    const headers = { Authorization: `Bearer ${token}` };
    return this.http.get<Servicio>(`${this.baseUrl}/${servicioId}`, {
      headers,
    });
  }
}
