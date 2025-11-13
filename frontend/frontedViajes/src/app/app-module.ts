import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';

// Componentes
import { LoginComponent } from './usuario/login/login.component';
import { RegistroComponent } from './usuario/registro/registro.component';
import { EmpresasComponent } from './empresa/empresas/empresas.component';
import { EmpresaDetalleDialog } from './empresa/empresa-detalle-dialog/empresa-detalle-dialog';
import { EmpresaModalComponent } from './empresa/empresa-modal/empresa-modal.component';
import { ViajesComponent } from './viaje/viajes/viajes.component';
import { FacturasComponent } from './factura/facturas/facturas.component';
import { ServiciosFactuComponent } from './servicio/servicios-factu/servicios-factu.component';
import { ServicioDetalleComponent } from './servicio/servicio-detalle/servicio-detalle.component';

// Angular Material
import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { ServicioFormComponent } from './servicio/servicio-form-component/servicio-form-component';

import { MatTabsModule } from '@angular/material/tabs';

import { ConfirmDialogComponent } from './confirmacionDialogo/confirm-dialog.component';
@NgModule({
  declarations: [
    App,
    LoginComponent,
    RegistroComponent,
    EmpresasComponent,
    EmpresaDetalleDialog,
    EmpresaModalComponent,
    ViajesComponent,
    FacturasComponent,
    ServiciosFactuComponent,
    ServicioDetalleComponent,
    ServicioFormComponent,
  ],
  imports: [
    BrowserModule,
    CommonModule,
    RouterModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    // Material
    MatTableModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    MatIconModule,
    MatDatepickerModule,
    MatNativeDateModule,

    ReactiveFormsModule,
    MatTabsModule,

    ConfirmDialogComponent,
  ],
  providers: [],
  bootstrap: [App],
})
export class AppModule {}
