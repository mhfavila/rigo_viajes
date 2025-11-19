import { Injectable } from '@angular/core';
import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private router: Router) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    //  Excluir login (no añadir token)
    if (request.url.includes('/api/auth/login')) {
      return next.handle(request);
    }

    //  Obtener token del sessionStorage
    const token = sessionStorage.getItem('token');

    //  Si existe token, clonamos la petición y añadimos el header
    if (token) {
      console.log('✅ Interceptor: Token encontrado. Añadiendo header Authorization...');
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
    }else{
      console.error('❌ Interceptor: NO se encontró ningún token en sessionStorage con la clave "token"');
      console.warn('Claves disponibles en sessionStorage:', Object.keys(sessionStorage));
    }

    //  Manejamos errores de autenticación
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        // Si el error es 401 (no autorizado), redirigimos al login
        if (error.status === 401) {
          console.warn('Token expirado o inválido. Redirigiendo al login...');
          sessionStorage.removeItem('token');
          this.router.navigate(['/login']);
        }

        // Si el error es 403 (sin permisos)
        if (error.status === 403) {
          console.error('No tienes permisos para acceder a este recurso');
          // Puedes mostrar un mensaje al usuario aquí
        }

        // Propagamos el error para que los servicios puedan manejarlo
        return throwError(() => error);
      })
    );
  }
}
