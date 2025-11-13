import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ServiciosFactService } from '../../services/servicios-fact.service';
import { Servicio } from '../servicio.model';
import { Empresa } from '../../empresa/empresa.model'; // <-- AÑADIR IMPORT
import { Factura } from '../../factura/factura.model'; // <-- AÑADIR IMPORT
import { switchMap, tap } from 'rxjs/operators'; // <-- Necesario para encadenar
import { forkJoin, of } from 'rxjs'; // <-- Necesario para cargas paralelas

@Component({
  selector: 'app-servicio-detalle',
  standalone: false,
  templateUrl: './servicio-detalle.component.html',
  styleUrls: ['./servicio-detalle.component.css'] // <-- Corregido de 'styleUrl' a 'styleUrls' (más seguro)
})
export class ServicioDetalleComponent implements OnInit {

  // Propiedades para cada objeto. NO signals.
  servicio: Servicio | null = null;
  empresa: Empresa | null = null;
  factura: Factura | null = null;

  constructor(
    private route: ActivatedRoute,
    private servicioService: ServiciosFactService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    // Usamos 'switchMap' para encadenar las llamadas
    this.servicioService.getServicioPorId(id).subscribe(servicioData => {

      // 1. Asignamos el servicio
      this.servicio = servicioData;

      // Asumimos que el servicio de datos tiene estos métodos
      // (Si no los tiene, necesitaríamos crearlos)
      const empresa$ = this.servicioService.getEmpresaPorId(servicioData.empresaId);

      // 'of(null)' se usa si no hay facturaId, para que forkJoin no falle
      const factura$ = servicioData.facturaId
        ? this.servicioService.getFacturaPorId(servicioData.facturaId)
        : of(null);

      // 2. Cargamos empresa y factura en paralelo
      forkJoin({
        empresa: empresa$,
        factura: factura$
      }).subscribe(({ empresa, factura }) => {
        this.empresa = empresa;
        this.factura = factura;
      });

    });
  }
}
