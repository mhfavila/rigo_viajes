import { ViajesComponent } from './viaje/viajes/viajes.component';
import { EmpresasComponent } from './empresa/empresas/empresas.component';
import { NgModule, Component } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './usuario/login/login.component';

import { FacturasComponent } from './factura/facturas/facturas.component';



import { RegistroComponent } from './usuario/registro/registro.component';
import { AuthGuard } from './guards/auth.guard';
import { ServiciosFactuComponent  } from './servicio/servicios-factu/servicios-factu.component';
import {ServicioDetalleComponent} from './servicio/servicio-detalle/servicio-detalle.component';

const routes: Routes = [{ path: 'login', component: LoginComponent },
  { path: 'empresas', component: EmpresasComponent /*, canActivate: [AuthGuard]*/ },
//  { path: 'viajes', component: ViajesComponent /*, canActivate: [AuthGuard]*/ },
  { path: 'viajes/:empresaId', component: ViajesComponent },
  { path: 'empresas/:empresaId/servicios', component: ServiciosFactuComponent  },
  { path: 'servicio/:id', component: ServicioDetalleComponent },
  { path: 'registro', component: RegistroComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
 { path: 'facturas', component: FacturasComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
