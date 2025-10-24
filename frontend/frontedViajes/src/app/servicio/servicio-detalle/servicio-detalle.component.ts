import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ServiciosFactService } from '../../services/servicios-fact.service';
import { Servicio } from '../servicio.model';

@Component({
  selector: 'app-servicio-detalle',
  standalone: false,
  templateUrl: './servicio-detalle.component.html',
  styleUrl: './servicio-detalle.component.css'
})
export class ServicioDetalleComponent implements OnInit{

servicio: Servicio | null = null;
constructor(
    private route: ActivatedRoute,
    private servicioService: ServiciosFactService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.servicioService.getServicioPorId(id).subscribe(data => {
      this.servicio = data;
    });
  }

}
