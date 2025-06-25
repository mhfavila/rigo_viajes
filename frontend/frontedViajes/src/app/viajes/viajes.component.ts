import { Component, OnInit } from '@angular/core';
import { ViajesService } from '../servicios/viajes.service';


@Component({
  selector: 'app-viajes',
  standalone: false,
  templateUrl: './viajes.component.html',
  styleUrls: ['./viajes.css']
})
export class ViajesComponent implements OnInit {
  viajes: any[] = [];

  constructor(private viajesService: ViajesService) {}

  ngOnInit(): void {
    this.viajesService.getViajes().subscribe((data: any[]) => {
      console.log('Datos recibidos:', data);
      this.viajes = data;
    },error=>{
      console.error('Error al obtener viajes:', error);
    });
  }
}
