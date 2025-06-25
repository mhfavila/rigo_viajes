import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ViajesService {
  private apiUrl = 'http://localhost:8080/api/viajes';

  constructor(private http: HttpClient,private authService: AuthService) { }

  getViajes(): Observable<any> {
    const token = this.authService.obtenerToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<any[]>(this.apiUrl, { headers });
  }
}
