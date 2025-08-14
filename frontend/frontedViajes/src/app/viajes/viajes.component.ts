import { Component, OnInit } from '@angular/core';
import { ViajesService } from '../servicios/viajes.service';
import { AuthService } from '../servicios/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-viajes',
  standalone: false,
  templateUrl: './viajes.component.html',
  styleUrls: ['./viajes.css']
})
export class ViajesComponent implements OnInit {
  viajes: any[] = [];

  constructor(private viajesService: ViajesService,  private authService: AuthService,
    private router: Router) {}

  ngOnInit(): void {

    //  Comprobación de login al cargar el componente
    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/login'], { replaceUrl: true });
      return; // Salimos para que no haga la petición de viajes
    }
    this.viajesService.getViajes().subscribe((data: any[]) => {
      console.log('Datos recibidos:', data);
      this.viajes = data;
    },error=>{
      console.error('Error al obtener viajes:', error);
    });
  }
}
