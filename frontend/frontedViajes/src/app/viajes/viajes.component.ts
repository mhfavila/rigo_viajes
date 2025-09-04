import { Component,OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ViajesServices, Viaje } from '../servicios/viajes.service';

@Component({
  selector: 'app-viajes',
  standalone: false,
  templateUrl: './viajes.component.html',
  styleUrl: './viajes.component.css'
})
export class ViajesComponent implements OnInit{
 empresaId!: number;
  viajes: Viaje[] = [];

  constructor(
    private route: ActivatedRoute,
    private viajesServices: ViajesServices
  ) {}

  ngOnInit(): void {
    this.empresaId = +this.route.snapshot.paramMap.get('empresaId')!;
    console.log('empresa id',this.empresaId);
    this.viajesServices.getViajesByEmpresa(this.empresaId)
      .subscribe(v => this.viajes = v);
  }
}
