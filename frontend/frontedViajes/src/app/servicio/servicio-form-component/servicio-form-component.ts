import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ServiciosFactService } from '../../services/servicios-fact.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Servicio } from '../servicio.model';
import { MatDialog } from '@angular/material/dialog';
//import { DireccionDialogComponent } from '../../direccion-dialog.component/direccion-dialog.component';

@Component({
  selector: 'app-servicio-form-component',
  standalone: false,
  templateUrl: './servicio-form-component.html',
  styleUrl: './servicio-form-component.css',
})
export class ServicioFormComponent implements OnInit {
  servicioForm!: FormGroup; //aqui es donde esta todo el formulario , es la variable donde se guarda el formulario completo con cajones y demas
  isEditMode: boolean = false;

  servicioId: number | null = null; // ID del servicio a editar
  empresaContextId: number | null = null; // ID de la empresa a la que pertenece

  constructor(
    private fb: FormBuilder, //Para construir formularios
    private router: Router, //Para navegar entre páginas
    private route: ActivatedRoute, // Para leer los parámetros de la URL
    private serviciosFactService: ServiciosFactService,
    private snackBar: MatSnackBar, //para motrar notificaciones
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    // Buscamos el ID del servicio (para modo Editar)
    const idParam = this.route.snapshot.paramMap.get('id');
    // Buscamos el ID de la empresa (para modo Crear)
    const empresaIdParam = this.route.snapshot.paramMap.get('empresaId');

    // --- 2. CONSTRUIMOS EL FORMULARIO  ---
    this.servicioForm = this.fb.group({
      // -- Cajón 1: "Información Principal" --
      viaje: this.fb.group({
        // NO hay 'empresaId' aquí, lo tenemos en 'empresaContextId'
        tipoServicio: ['', Validators.required],
        fechaServicio: [new Date(), Validators.required],
        origen: ['', Validators.required],
        destino: ['', Validators.required],
        clienteFinal: [''],
      }),

      // -- Cajón 2: "Logística y Costes" --
      logisticaCostes: this.fb.group({
        conductor: [''],
        matriculaVehiculo: [''],
        km: [
          null,
          [Validators.required, Validators.min(0), Validators.max(1000000)],
        ], // Límite de 1 millón
        precioKm: [
          null,
          [Validators.required, Validators.min(0), Validators.max(99)],
        ], // Límite de 99
        dieta: [false], // Límite de 100 millones
        precioDieta: [null, [Validators.min(0), Validators.max(99999999)]],
        horasEspera: [null, [Validators.min(0), Validators.max(99999999)]],
        importeEspera: [null, [Validators.min(0), Validators.max(99999999)]],
      }),

      // -- Cajón 3: "Documentación y Notas" --
      documentacion: this.fb.group({
        albaran: [''],
        orden: [0],
        observaciones: [''],
      }),
    });

    if (idParam) {
      // --- MODO EDITAR ---  URL  /servicios/editar/123
      this.isEditMode = true;
      this.servicioId = +idParam;
      this.cargarDatosServicio(this.servicioId);
    } else if (empresaIdParam) {
      // --- MODO CREAR ---
      this.isEditMode = false;
      this.empresaContextId = +empresaIdParam;

      // 1. ¿HAY DATOS PARA DUPLICAR?
      const duplicadoJson = localStorage.getItem('servicioParaDuplicar');

      if (duplicadoJson) {
        // A) SI HAY: Rellenamos el formulario con los datos copiados
        const data = JSON.parse(duplicadoJson);

        // Limpiamos para la próxima vez
        localStorage.removeItem('servicioParaDuplicar');

        this.servicioForm.patchValue({
          viaje: {
            tipoServicio: data.tipoServicio,
            fechaServicio: new Date(), // Ponemos fecha de HOY
            origen: data.origen,
            destino: data.destino,
            clienteFinal: data.clienteFinal,
          },
          logisticaCostes: {
            conductor: data.conductor,
            matriculaVehiculo: data.matriculaVehiculo,
            km: data.km,
            precioKm: data.precioKm,
            dieta: data.dieta,
            precioDieta: data.precioDieta,
            horasEspera: data.horasEspera,
            importeEspera: data.importeEspera,
          },
          documentacion: {
            albaran: '', // El albarán suele cambiar, lo dejamos vacío
            orden: data.orden,
            observaciones: data.observaciones,
          }
        });

        this.snackBar.open('Datos duplicados. ¡Revisa la fecha!', 'OK', { duration: 3000 });

      } else {
        // B) NO HAY: Cargamos precios por defecto (Comportamiento normal)
        this.cargarPreciosPorDefecto(this.empresaContextId);
      }
    } else {
      // Error: No tenemos contexto
      console.error(
        'Error: No se encontró :id para editar ni :empresaId para crear.'
      );
      this.snackBar.open('Error de carga: Contexto no encontrado.', 'Cerrar', {
        duration: 3000,
      });

      this.navegarDeVuelta();
    }
  }

/**
 * para cargar los precios que hemos metido en la empresa
 * @param empresaId
 */
cargarPreciosPorDefecto(empresaId: number) {
    this.serviciosFactService.getEmpresaPorId(empresaId).subscribe({
      next: (empresa) => {
        // Si la empresa trae precios, los ponemos en el formulario
        if (empresa) {
          this.servicioForm.patchValue({
            logisticaCostes: {
              // Usamos el operador || 0 por si vienen nulos
              precioKm: empresa.precioKmDefecto || 0,
              precioDieta: empresa.precioDietaDefecto || 0,
              importeEspera: empresa.precioHoraEsperaDefecto || 0 // Ojo: en tu form se llama importeEspera al precio hora
            }
          });
          // Avisamos discretamente
          if (empresa.precioKmDefecto && empresa.precioKmDefecto > 0) {
            this.snackBar.open('Precios de empresa cargados', '', { duration: 2000 });
          }
        }
      },
      error: (err) => console.error('No se pudieron cargar precios de empresa', err)
    });
  }



  //Carga los datos de un servicio en modo Editar y rellena el formulario anidado.

  cargarDatosServicio(id: number): void {
    this.serviciosFactService.getServicioPorId(id).subscribe({
      next: (data) => {
        if (data.empresaId) {
          this.empresaContextId = data.empresaId;
        } else {
          console.error('El servicio cargado no tiene un ID de empresa.');
        }

        // Rellenamos el formulario anidado
        this.servicioForm.patchValue({
          viaje: {
            tipoServicio: data.tipoServicio,
            fechaServicio: data.fechaServicio,
            origen: data.origen,
            destino: data.destino,
            clienteFinal: data.clienteFinal,
          },
          logisticaCostes: {
            conductor: data.conductor,
            matriculaVehiculo: data.matriculaVehiculo,
            km: data.km,
            precioKm: data.precioKm,
            dieta: data.dieta,
            precioDieta: data.precioDieta,
            horasEspera: data.horasEspera,
            importeEspera: data.importeEspera,
          },
          documentacion: {
            albaran: data.albaran,
            orden: data.orden,
            observaciones: data.observaciones,
          },
        });
      },
      error: (err) => {
        console.error('Error al cargar el servicio:', err);
        this.snackBar.open('Error al cargar el servicio. ¿Existe?', 'Cerrar', {
          duration: 3000,
        });
      },
    });
  }

  /**
   * Se llama al enviar el formulario (Crear o Editar).
   */
  guardar(): void {
    if (!this.servicioForm.valid) {
      this.servicioForm.markAllAsTouched(); // Muestra errores si los hay
      this.snackBar.open('Revise los campos requeridos.', 'Cerrar', {
        duration: 2000,
      });
      return;
    }

    // Comprobación de seguridad
    if (!this.empresaContextId) {
      console.error('Error fatal: No hay contexto de empresa para guardar.');
      return;
    }

    // --- 1. CONSTRUIMOS EL OBJETO (PAYLOAD) ---
    const formValue = this.servicioForm.getRawValue();

    // 2. Aplanamos la estructura de los "cajones" basicamente jusnta todo para poder pasarle json a la api
    const payload: any = {
      ...formValue.viaje,
      ...formValue.logisticaCostes,
      ...formValue.documentacion,
    };

    // 3. Añadimos el ID de la empresa (del contexto)

    payload.empresaId = this.empresaContextId;

    // Evita que al editar se reste un día por la zona horaria UTC
    // ---------------------------------------------------------
    if (payload.fechaServicio) {
        const fecha = new Date(payload.fechaServicio);
        // Ajustamos la zona horaria restando el offset para compensar
        const fechaLocal = new Date(fecha.getTime() - (fecha.getTimezoneOffset() * 60000));
        // Guardamos solo la fecha en formato string YYYY-MM-DD
        payload.fechaServicio = fechaLocal.toISOString().split('T')[0];
    }

    // 4. Calculamos el campo 'importeServicio'
    // (Usamos || 0 para evitar NaN si los campos están vacíos)
    // payload.importeServicio =
    //  (payload.km || 0) * (payload.precioKm || 0);//aqui se calcula el importe a;adir los tiempos de espera y la dieta

    //(payload.dieta ? (payload.precioDieta || 0) : 0); // Si dieta es true, suma precio, si no, suma 0

    // 1. Aseguramos que los valores sean números para evitar errores de texto
    const km = Number(payload.km) || 0;
    const precioKm = Number(payload.precioKm) || 0;
    const precioDieta = Number(payload.precioDieta) || 0;
    const precioEspera = Number(payload.importeEspera) || 0;
    const horasEspera = Number(payload.horasEspera) || 0;
    console.log('precio dieta------------' + payload.precioDieta);


    let total = 0;

    if (km < 100) {
      // REGLA: Menos de 100km son 50€ (Servicio Mínimo)
      total = 50.00;
      //guardamos en observaciones que fue servicio mínimo

       if (!payload.observaciones) payload.observaciones = '';
       payload.observaciones += ' (Aplicado Servicio Mínimo 50€)';
       console.log("Aplicado Servicio Mínimo 50€");
    } else {
      // Cálculo normal
      total = km * precioKm;
    }
    // 2. Calculamos el base
   // let total = km * precioKm;
    let importeHorasEspera = precioEspera * horasEspera;

    // 3. Comprobamos la dieta
    // Quitamos el "=== true" para que funcione si viene como 1, "true" o true
    if (payload.dieta) {
      total += precioDieta;
    }
    total += importeHorasEspera;
    // 4. Asignamos
    payload.importeServicio = total;

    // --- 5. ENVÍO A LA API ---
    if (this.isEditMode) {
      // --- Lógica de Editar ---
      this.serviciosFactService
        .editarServicio(this.servicioId!, payload)
        .subscribe({
          next: () => {
            this.snackBar.open('Servicio actualizado con éxito.', 'OK', {
              duration: 2000,
            });
            this.navegarDeVuelta(); // Volvemos a la lista
          },
          error: (err) =>
            console.error('Error al actualizar el servicio:', err),
        });
    } else {
      // --- Lógica de Crear ---
      this.serviciosFactService.crearServicio(payload).subscribe({
        next: () => {
          this.snackBar.open('Servicio creado con éxito.', 'OK', {
            duration: 2000,
          });
          this.navegarDeVuelta(); // Volvemos a la lista
        },
        error: (err) => console.error('Error al crear el servicio:', err),
      });
    }
  }

  /**
   * Navega de vuelta a la lista de servicios de la empresa.
   */
  navegarDeVuelta(): void {
    window.close();
  }

  /**
   * Se llama con el botón "Cancelar"
   */
  cancelar(): void {
    this.navegarDeVuelta();
  }
  //metodo para poder meter las direciones del origen y del destino
  /*abrirDialogoDireccion(campo: 'origen' | 'destino') {
    const control = this.servicioForm.get('viaje.' + campo);
    const valorActual = control?.value;

    const dialogRef = this.dialog.open(DireccionDialogComponent, {
      width: '600px',
      data: {
        titulo:
          campo === 'origen' ? 'Dirección de Origen' : 'Dirección de Destino',
        direccion: valorActual, // Pasamos lo que haya (o null)
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        // Si el usuario guardó, actualizamos el formulario con el objeto completo
        control?.setValue(result);
        control?.markAsDirty(); // Marcamos como modificado
      }
    });
  }*/

  // Función auxiliar para mostrar texto bonito en el input (Readonly)
  getDireccionTexto(campo: 'origen' | 'destino'): string {
    const val = this.servicioForm.get('viaje.' + campo)?.value;
    if (!val) return '';
    // Si es un objeto (nueva estructura)
    if (typeof val === 'object') {
      return `${val.calle} ${val.numero}, ${val.ciudad}`;
    }
    // Si viene del backend antiguo como string simple (fallback)
    return val;
  }
}
