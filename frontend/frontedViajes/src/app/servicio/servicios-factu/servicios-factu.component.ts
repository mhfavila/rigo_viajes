import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { ServiciosFactService } from '../../services/servicios-fact.service';
import { Servicio } from '../servicio.model';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../confirmacionDialogo/confirm-dialog.component';

@Component({
  selector: 'app-servicios-factu.component',
  standalone: false,
  templateUrl: './servicios-factu.component.html',
  styleUrl: './servicios-factu.component.css',
})
export class ServiciosFactuComponent implements OnInit {

  servicios: Servicio[] = []; // array vacío, inicializado
  empresaId!: number;


  constructor(

    private dialog: MatDialog,
    private serviciosFactService: ServiciosFactService,
    private snackBar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute
  ) {}
  ngOnInit(): void {
    this.cargarServicios();
  }
  cargarServicios(): void {
    const id = this.route.snapshot.paramMap.get('empresaId');
    if (id) {
      this.empresaId = +id;
      console.log('Empresa ID:', this.empresaId);

      this.serviciosFactService
        .getServiciosByEmpresa(this.empresaId)
        .subscribe({
          next: (data) => {
            console.log('Servicios obtenidos del backend:', data);
            this.servicios = data;
          },
          error: (err) => {
            console.error('Error al cargar los servicios:', err);
          },
        });
    } else {
      console.warn('No se encontró empresaId en la ruta');
    }
  }

  //metodo encargado de abrir los servicios en otra ventana
  abrirServicio(servicioId: number, event: Event) {
    event.preventDefault(); //para evitar que se recarge la pagian
    const url = `/servicio/${servicioId}`;
    const width = 800; // ancho de la ventana
    const height = 600; // alto de la ventana
    const left = window.screen.width / 2 - width / 2; // centrada horizontalmente
    const top = window.screen.height / 2 - height / 2; // centrada verticalmente

    window.open(
      url,
      '_blank',
      `width=${width},height=${height},top=${top},left=${left},resizable=yes,scrollbars=yes`
    );
  }
  //muestra los servicios que tienen marcado el check ,luego se modificara para a;adir esos servicios a las facturas
  mostrarSeleccionados(): void {
    const seleccionados = this.servicios.filter((s) => s.seleccionado);
    console.log('Servicios seleccionados:', seleccionados);
  }

  /**
   * Abre una nueva ventana emergente con el formulario para crear un servicio,
   * pasando el ID de la empresa actual en la URL.
   */
  anadirServicios(): void {
    // 1. Comprobamos que tenemos el ID de la empresa (cargado en ngOnInit)
    if (!this.empresaId) {
      console.error('Error: No se ha cargado el ID de la empresa.');
      this.snackBar.open(
        'Error: No se pudo identificar la empresa. Recargue.',
        'Cerrar',
        { duration: 3000 }
      );
      return;
    }

    // 2. Construimos la URL que espera el ServicioFormComponent
    // Esta es la ruta que definimos en el app-routing.module.ts
    const url = `/empresas/${this.empresaId}/servicios/nuevo`;

    const width = 800;
    const height = 600;
    const left = window.screen.width / 2 - width / 2;
    const top = window.screen.height / 2 - height / 2;

    const newWindow = window.open(
      url,
      '_blank', // Abre en una nueva pestaña/ventana
      `width=${width},height=${height},top=${top},left=${left},resizable=yes,scrollbars=yes`
    );

    // 2. Creamos el "oyente" (listener) para que cuando se cierre la ventana se recargue
    if (newWindow) {
      const checkInterval = setInterval(() => {
        // 3. Comprueba si el "hijo" ha llamado a 'window.close()'
        if (newWindow.closed) {
          clearInterval(checkInterval); // Deja de escuchar
          console.log('Ventana de servicio cerrada. Recargando lista...');
          // 4. ¡ACCIÓN! Recarga los servicios
          this.cargarServicios();
        }
      }, 500); // Comprueba cada medio segundo
    } else {
      this.snackBar.open(
        'El popup fue bloqueado. Habilite los popups para esta web.',
        'Cerrar',
        { duration: 3000 }
      );
    }
  }

  editarServicio(servicioId: number) {



    // 1. Comprobamos que tenemos el ID de la empresa (cargado en ngOnInit)
    if (!this.empresaId) {
      console.error('Error: No se ha cargado el ID de la empresa.');
      this.snackBar.open(
        'Error: No se pudo identificar la empresa. Recargue.',
        'Cerrar',
        { duration: 3000 }
      );
      return;
    }

    // 2. Construimos la URL que espera el ServicioFormComponent
    // Esta es la ruta que definimos en el app-routing.module.ts
    const url = `/servicios/editar/${servicioId}`;

    const width = 800;
    const height = 600;
    const left = window.screen.width / 2 - width / 2;
    const top = window.screen.height / 2 - height / 2;

    const newWindow = window.open(
      url,
      '_blank', // Abre en una nueva pestaña/ventana
      `width=${width},height=${height},top=${top},left=${left},resizable=yes,scrollbars=yes`
    );

    // 2. Creamos el "oyente" (listener) para que cuando se cierre la ventana se recargue
    if (newWindow) {
      const checkInterval = setInterval(() => {
        // 3. Comprueba si el "hijo" ha llamado a 'window.close()'
        if (newWindow.closed) {
          clearInterval(checkInterval); // Deja de escuchar
          console.log('Ventana de servicio cerrada. Recargando lista...');
          // 4. ¡ACCIÓN! Recarga los servicios
          this.cargarServicios();
        }
      }, 500); // Comprueba cada medio segundo
    } else {
      this.snackBar.open(
        'El popup fue bloqueado. Habilite los popups para esta web.',
        'Cerrar',
        { duration: 3000 }
      );
    }




  }


  eliminarServicio(servicioId: number):void {

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: '¿Estás seguro de que deseas eliminar este servicio?' }
    });
    // 2. Nos suscribimos al resultado del diálogo
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {

      // 3. Si el usuario hizo clic en "Confirmar" (confirmed es true)
      if (confirmed) {

        // 4. Llamamos al servicio para eliminar
        this.serviciosFactService.eliminarServicio(servicioId).subscribe({
          next: () => {
            // 5. Si todo va bien, mostramos un mensaje y recargamos la lista
            this.snackBar.open('Servicio eliminado correctamente', 'Cerrar', {
              duration: 3000,
            });
            this.cargarServicios(); // ¡Importante para refrescar!
          },
          error: (err) => {
            // 6. Si hay un error, lo mostramos
            console.error('Error al eliminar el servicio:', err);
            this.snackBar.open('Error al eliminar el servicio', 'Cerrar', {
              duration: 3000,
            });
          },
        });
      }
      // Si 'confirmed' es false, no hacemos nada (el usuario canceló)
    });

}
}
