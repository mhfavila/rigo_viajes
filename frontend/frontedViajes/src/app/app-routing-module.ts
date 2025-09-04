import { ViajesComponent } from './viajes/viajes.component';
import { EmpresasComponent } from './empresas/empresas.component';
import { NgModule, Component } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';



import { RegistroComponent } from './registro/registro.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [{ path: 'login', component: LoginComponent },
  { path: 'empresas', component: EmpresasComponent /*, canActivate: [AuthGuard]*/ },
//  { path: 'viajes', component: ViajesComponent /*, canActivate: [AuthGuard]*/ },
  { path: 'viajes/:empresaId', component: ViajesComponent },

  { path: 'registro', component: RegistroComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
