import { FacturaService } from './../../services/factura.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';


import { Factura } from '../factura.model';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ConfirmDialogComponent } from '../../confirmacionDialogo/confirm-dialog.component';





@Component({
  selector: 'app-facturas',
  standalone: false,
  templateUrl: './facturas.component.html',
  styleUrls: ['./facturas.component.css'],
})
export class FacturasComponent implements OnInit {
  empresaId!: number;
  facturas:Factura[] = [];

  constructor(
      private route: ActivatedRoute,
      private facturaService: FacturaService,
      private dialog: MatDialog,
      private snackBar: MatSnackBar
    ) {}

  ngOnInit(): void {
    this.cargarFacturas();
  }


  cargarFacturas(){
    const id = this.route.snapshot.paramMap.get('empresaId');
    console.log('Empresa ID:', this.empresaId);

    if (id) {
      this.empresaId = +id;
      console.log('Empresa ID:', this.empresaId);

    // Usamos .subscribe para actualizar la tabla
      this.facturaService.getFacturaByEmpresa(this.empresaId).subscribe({
        next: (f) => this.facturas = f,
        error: (err) => console.error('Error cargando facturas:', err)
      });
    } else {
      console.warn('No se encontró empresaId en la ruta');
    }
  }


descargarFacturaPdf(facturaId: number) {
    this.facturaService.descargarPdf(facturaId).subscribe((data) => {
      const blob = new Blob([data], { type: 'application/pdf' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `factura_${facturaId}.pdf`;
      a.click();
    });
  }



  eliminarFactura(factura: Factura) {
    //comprobamos que la factura tenga id por si por alguna razon rara no tiene id
    if (!factura.id) {
      console.error('Error: La factura no tiene ID válido.');
      return;
    }


    // 1. Abrimos el diálogo de confirmación que ya tienes creado

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: {
        message: `¿Estás seguro de eliminar la factura ${factura.numeroFactura}? Los servicios quedarán pendientes de facturar de nuevo.`
      }
    });

    // 2. Esperamos la respuesta
    dialogRef.afterClosed().subscribe(confirmado => {
      if (confirmado) {
        // 3. Si confirma, llamamos al backend
        this.facturaService.eliminarFactura(factura.id!).subscribe({
          next: () => {
            this.mostrarNotificacion('Factura eliminada correctamente', 'success');
            // Recargamos la lista para que desaparezca de la tabla
            this.cargarFacturas();
          },
          error: (err) => {
            console.error('Error eliminando factura:', err);
            this.mostrarNotificacion('Error al eliminar la factura', 'error');
          }
        });
      }
    });
  }

  // Método auxiliar para mostrar mensajes bonitos abajo
  mostrarNotificacion(mensaje: string, tipo: 'success' | 'error') {
    this.snackBar.open(mensaje, 'Cerrar', {
      duration: 3000,
      panelClass: tipo === 'success' ? ['mat-toolbar', 'mat-primary'] : ['mat-toolbar', 'mat-warn'],
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    });
  }





}
