import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Empresa, EmpresaService } from '../servicios/empresa.service';
import { Viaje, ViajesServices } from '../servicios/viajes.service';

// Extendemos la interfaz Viaje para añadir 'seleccionado'
interface ViajeSeleccionable extends Viaje {
  seleccionado: boolean;
}

@Component({
  selector: 'app-facturas',
  standalone: false,
  templateUrl: './facturas.component.html',
  styleUrls: ['./facturas.component.css'],
})
export class FacturasComponent implements OnInit {
  empresa: Empresa | undefined;

  // Ahora usamos la interfaz extendida
  viajes: ViajeSeleccionable[] = [];

  // Filtros
  filtros = {
    fechaDesde: '',
    fechaHasta: '',
    estado: '',
    destino: '',
  };

  // Checkbox “marcar todos”
  seleccionarTodos = false;

  constructor(
    private route: ActivatedRoute,
    private empresaService: EmpresaService,
    private viajeService: ViajesServices
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const empresaId = +params['empresaId'];
      if (empresaId) {
        // Cargar datos de la empresa
        this.empresaService.getEmpresaPorId(empresaId).subscribe((data) => {
          this.empresa = data;
        });

        // Cargar viajes de la empresa y añadir 'seleccionado'
        this.viajeService.getViajesByEmpresa(empresaId).subscribe((data) => {
          this.viajes = data.map((v) => ({ ...v, seleccionado: false }));
        });
      }
    });
  }

  // Filtrar viajes según filtros
  get viajesFiltrados(): ViajeSeleccionable[] {
    return this.viajes.filter((v) => {
      return (
        (!this.filtros.destino ||
          v.destino.toLowerCase().includes(this.filtros.destino.toLowerCase())) &&
        (!this.filtros.fechaDesde || v.fecha >= this.filtros.fechaDesde) &&
        (!this.filtros.fechaHasta || v.fecha <= this.filtros.fechaHasta)
      );
    });
  }

  // Marcar o desmarcar todos los viajes filtrados
  toggleTodos() {
    this.viajesFiltrados.forEach((v) => (v.seleccionado = this.seleccionarTodos));
  }

  // Generar PDF solo con los viajes seleccionados
  generarFacturaPDF() {
    const viajesSeleccionados = this.viajesFiltrados.filter((v) => v.seleccionado);

    import('jspdf').then((jsPDF) => {
      const doc = new jsPDF.default();
      doc.setFontSize(16);
      doc.text(`Factura de ${this.empresa?.nombre || ''}`, 10, 10);

      let y = 20;
      doc.setFontSize(12);
      doc.text(
        'ID   Fecha        Destino       Distancia   Precio/km   Importe',
        10,
        y
      );
      y += 10;

      if (viajesSeleccionados.length === 0) {
        doc.text('No se seleccionó ningún viaje.', 10, y);
      } else {
        let total = 0;
        viajesSeleccionados.forEach((v) => {
          const importe = v.distancia * v.precioKm;
          doc.text(
            `${v.id}   ${v.fecha}   ${v.destino}   ${v.distancia}km   ${v.precioKm}€   ${importe}€`,
            10,
            y
          );
          y += 10;
          total += importe;
        });

        doc.text(`Total: ${total}€`, 10, y + 10);
      }

      doc.save(`factura_${this.empresa?.nombre || ''}.pdf`);
    });
  }
}
