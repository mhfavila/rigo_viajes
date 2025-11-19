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


//Ya no hay const token, ni headers, ni tercer argumento en el get/post lo hace todo el interceptor
  //metodo para sacar los servicios de una empresa
  getServiciosByEmpresa(empresaId: number): Observable<Servicio[]> {

    return this.http.get<Servicio[]>(`${this.apiUrl}/servicios/empresa/${empresaId}`);
  }


  //metodo para sacar un servicio segun su id
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


