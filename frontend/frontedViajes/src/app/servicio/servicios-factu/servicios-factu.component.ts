import { EmpresaService } from './../../services/empresa.service';
import { Empresa } from './../../empresa/empresa.model';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { ServiciosFactService } from '../../services/servicios-fact.service';
import { Servicio } from '../servicio.model';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../confirmacionDialogo/confirm-dialog.component';
import { DatosFacturaDialogComponent } from '../../factura/datos-factura-dialog.component/datos-factura-dialog.component';
import { Factura } from '../../factura/factura.model';
import { FacturaService } from '../../services/factura.service';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-servicios-factu.component',
  standalone: false,
  templateUrl: './servicios-factu.component.html',
  styleUrl: './servicios-factu.component.css',
})
export class ServiciosFactuComponent implements OnInit {
  servicios: Servicio[] = []; // array vacío, inicializado
  empresaId!: number;
  totalFacturas!: number;
  idUsuario!: number;

  constructor(
    private dialog: MatDialog,
    private serviciosFactService: ServiciosFactService,
    private snackBar: MatSnackBar,
    private router: Router,
    private route: ActivatedRoute,
    private facturaService: FacturaService,
    private cdr: ChangeDetectorRef,
    private empresaService: EmpresaService
  ) {}
  ngOnInit(): void {
    this.cargarServicios();
  }
  cargarServicios(): void {
    const id = this.route.snapshot.paramMap.get('empresaId');
    if (id) {
      this.empresaId = +id;
      console.log('Empresa ID:', this.empresaId);

      //saco el usuario de la empresa para tenerlo
      //lo primero de todo voy a cargar el id del usuario para tenerlo
      this.empresaService.getEmpresaPorId(this.empresaId).subscribe({
        next: (data: Empresa) => {
          if (data.usuarioId) {
            this.idUsuario = data.usuarioId;
            console.log("**USUARIO ID"+this.idUsuario);
          } else {
            console.warn('Esta empresa no tiene un usuario asignado'); //en principio es imposible que sea asi pero por si acaso
          }
        },
      });
      console.log(this.idUsuario);
//continuamos con cargar los servicios de la empresa
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
  /**
   * se genera la factura apartir de los servicios selecionados
   * @returns
   */
  generarFacturaDeSeleccionados(): void {
    //const seleccionados = this.servicios.filter((s) => s.seleccionado);
    // recojo los servicios marcados
    const serviciosParaFacturar = this.servicios.filter((s) => s.seleccionado);
    if (serviciosParaFacturar.length === 0) {
      console.warn('No hay servicios seleccionados para facturar.');

      alert('Por favor, seleccione al menos un servicio.');
      return;
    }
    //comprobamos que todos los servicos sean de la misma empresa
    const primeraEmpresaId = serviciosParaFacturar[0].empresaId;
    const todosSonMismaEmpresa = serviciosParaFacturar.every(
      (s) => s.empresaId === primeraEmpresaId
    );
    if (!todosSonMismaEmpresa) {
      console.error(
        'Error: No se pueden facturar servicios de distintas empresas en una sola factura.'
      );
      alert('Error: Todos los servicios deben pertenecer a la misma empresa.');
      return;
    }

    //calculos
    // Sumamos el 'importeServicio' de todos los servicios seleccionados
    const totalBruto = serviciosParaFacturar.reduce(
      (acumulado, servicio) => acumulado + servicio.importeServicio,
      0
    );

    // abrimos el dialogo
    const dialogRef = this.dialog.open(DatosFacturaDialogComponent, {
      width: '300px',
      data: { iva: 21, irpf: 15 },//estos datos salen por defecto en el dialogo
    });
    // esperamos a que se cierre el dialogo
    dialogRef.afterClosed().subscribe((result) => {
      if (!result) {
        console.log('Cancelado por el usuario');
        return;
      }

      const porcentajeIvaUsuario = result.iva;
      const porcentajeIrpfUsuario = result.irpf;

      //llamamos al metodo para crear al factura y le pasamos los parametros
      this.crearFacturaConDatos(
        serviciosParaFacturar,
        primeraEmpresaId,
        porcentajeIvaUsuario,
        porcentajeIrpfUsuario
      );
    });
  }
  /**
   * metodo para crear la factura
   * @param servicios
   * @param empresaId
   * @param ivaPorcentaje
   * @param irpfPorcentaje
   */

  private crearFacturaConDatos(
    servicios: Servicio[],
    empresaId: number,
    ivaPorcentaje: number,
    irpfPorcentaje: number
  ) {
    // 1. Calculamos todo lo que NO depende de la API
    const totalBruto = servicios.reduce((acc, s) => acc + s.importeServicio, 0);
    const importeIva = totalBruto * (ivaPorcentaje / 100);
    const importeIrpf = totalBruto * (irpfPorcentaje / 100);
    const totalFactura = totalBruto + importeIva - importeIrpf;

    const usuarioId = this.idUsuario;
    const idsDeServicios = servicios.map((s) => s.id);

    // 2. PRIMERO obtenemos el total de facturas
    this.facturaService.getFacturaByEmpresa(empresaId).subscribe({
      next: (data) => {
        // 3. AHORA SÍ tenemos el dato correcto
        this.totalFacturas = data.length;
        console.log('Cantidad de facturas existentes:', this.totalFacturas);

        const numero = this.totalFacturas + 1;

        // 4. Construimos el objeto factura AQUÍ DENTRO
        const nuevaFactura: Factura = {
          numeroFactura:
            'F-' + empresaId + '-' + numero.toString().padStart(4, '0'),
          fechaEmision: new Date().toISOString(),
          empresaId: empresaId,
          usuarioId: usuarioId,
          totalBruto: totalBruto,
          porcentajeIva: ivaPorcentaje,
          importeIva: importeIva,
          porcentajeIrpf: irpfPorcentaje,
          importeIrpf: importeIrpf,
          totalFactura: totalFactura,
          estado: 'BORRADOR',
          serviciosIds: idsDeServicios,
        };

        // 5. Y llamamos a la API de creación AQUÍ DENTRO
        this.facturaService.crearFactura(nuevaFactura).subscribe({
          next: (res) => {
            // ----- ¡AÑADE ESTOS LOGS AQUÍ! -----
            console.log('--- 1. RESPUESTA DEL BACKEND RECIBIDA ---');
            console.log('facturaCreada:', res);
            console.log('IDs que se enviaron:', res.serviciosIds);
            console.log('Factura creada exitosamente', res);
            if (res && res.id) {
              console.log(
                '--- 2. GUARDIA SUPERADA. Llamando a actualizar... ---'
              );
              console.log('Se usará el ID de factura:', nuevaFactura.id);
              this.actualizarServiciosLocales(idsDeServicios, res.id);
            } else {
              console.error(
                'La facturaCreada no tiene ID (o es 0, null, undefined). No se llamará a actualizarServiciosLocales.'
              );
            }
            // this.cargarServicios
          },
          error: (err) => {
            console.error('Error al CREAR la factura', err);
          },
        });
      },
      error: (err) => {
        console.error('Error al OBTENER el número de facturas', err);
      },
    });
  }

  private actualizarServiciosLocales(
    idsFacturados: number[],
    nuevaFacturaId: number
  ) {
    this.servicios = this.servicios.map((servicio) => {
      if (idsFacturados.includes(servicio.id)) {
        return { ...servicio, facturaId: nuevaFacturaId, seleccionado: false };
      }
      return servicio;
    });
    this.cdr.markForCheck();
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

  eliminarServicio(servicioId: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: '¿Estás seguro de que deseas eliminar este servicio?' },
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
