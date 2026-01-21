
import { EmpresasComponent } from './empresa/empresas/empresas.component';
import { NgModule, Component } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './usuario/login/login.component';

import { FacturasComponent } from './factura/facturas/facturas.component';

import { RegistroComponent } from './usuario/registro/registro.component';
import { AuthGuard } from './guards/auth.guard';
import { ServiciosFactuComponent } from './servicio/servicios-factu/servicios-factu.component';
import { ServicioDetalleComponent } from './servicio/servicio-detalle/servicio-detalle.component';
import { ServicioFormComponent } from './servicio/servicio-form-component/servicio-form-component';
import { PerfilComponent } from './usuario/perfil/perfil.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  {
    path: 'empresas',
    //canActivate: [AuthGuard] esto sirve para que sino hay token o esta mal te envie al login en vez de quedarse en la pantalla sin informacion
    component: EmpresasComponent , canActivate: [AuthGuard],
  },
  //  { path: 'viajes', component: ViajesComponent /*, canActivate: [AuthGuard]*/ },

  { path: 'empresas/:empresaId/servicios', component: ServiciosFactuComponent , canActivate: [AuthGuard]},
  { path: 'servicio/:id', component: ServicioDetalleComponent , canActivate: [AuthGuard]},
  { path: 'registro', component: RegistroComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'facturas/:empresaId', component: FacturasComponent, canActivate: [AuthGuard] },
  //Ruta para CREAR un nuevo servicio
  { path: 'empresas/:empresaId/servicios/nuevo',component: ServicioFormComponent, canActivate: [AuthGuard]},
  //Ruta para EDITAR un servicio existente
  { path: 'servicios/editar/:id',component: ServicioFormComponent, canActivate: [AuthGuard]},

  { path: 'perfil', component: PerfilComponent, canActivate: [AuthGuard] },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
