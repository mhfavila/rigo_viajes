import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
  UrlTree
} from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    // 🔍 Verificar si el usuario está autenticado
    if (this.authService.estaLogueado()) {
      console.log('✅ Usuario autenticado, acceso permitido a:', state.url);
      return true;
    }

    // 🚫 Usuario no autenticado
    console.warn('❌ Acceso denegado. Redirigiendo al login...');
    console.warn('Intentó acceder a:', state.url);

    // Guardar la URL a la que intentó acceder para redirigir después del login
    // Puedes usar esto para redirigir al usuario después de loguearse
    // localStorage.setItem('redirectUrl', state.url);

    // Redirigir al login
    return this.router.createUrlTree(['/login'], {
      queryParams: { returnUrl: state.url } // opcional: pasar la URL de retorno
    });
  }
}
