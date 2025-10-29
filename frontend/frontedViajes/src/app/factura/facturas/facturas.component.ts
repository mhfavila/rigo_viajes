import { FacturaService } from './../../services/factura.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {  EmpresaService } from '../../services/empresa.service';
import {Empresa} from '../../empresa/empresa.model';
import { Factura } from '../factura.model';




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
      private facturaService: FacturaService
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

    this.facturaService.getFacturaByEmpresa(this.empresaId).subscribe(f=> this.facturas =f);
    }else {
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



}
