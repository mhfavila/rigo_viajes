import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { ViajesComponent } from './viajes/viajes.component';

import { HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './login/login.component';
import { FormsModule } from '@angular/forms';
import { RegistroComponent } from './registro/registro.component';
import { EmpresasComponent } from './empresas/empresas.component';
import { EmpresaDetalleDialog } from './empresa-detalle-dialog/empresa-detalle-dialog';
import { MatDialogContent } from "@angular/material/dialog";
import { MatDialogActions } from "@angular/material/dialog";

import { MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
@NgModule({
  declarations: [
    App,
    ViajesComponent,
    LoginComponent,

    RegistroComponent,
    EmpresasComponent,
    EmpresaDetalleDialog,
  ],
  imports: [
    MatTableModule,
    MatDialogModule,
    MatButtonModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    MatDialogContent,
    MatDialogActions
],
  providers: [
    provideBrowserGlobalErrorListeners()
  ],
  bootstrap: [App]
})
export class AppModule { }
