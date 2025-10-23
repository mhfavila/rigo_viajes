import { Component,OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { ServiciosFactService, Servicio } from '../../services/servicios-fact.service';

@Component({
  selector: 'app-servicios-factu.component',
  standalone: false,
  templateUrl: './servicios-factu.component.html',
  styleUrl: './servicios-factu.component.css'
})
export class ServiciosFactuComponent implements OnInit {

  servicios: Servicio[] = []; // array vacío, inicializado
  empresaId!: number;

  constructor(
    //private empresaService: EmpresaService,
    //private authService: AuthService,
    //private dialog: MatDialog,
    private serviciosFactService: ServiciosFactService,
     private snackBar: MatSnackBar,
     private router: Router,
     private route: ActivatedRoute,
  ) {}  ngOnInit(): void {
    this.cargarServicios();

  }
  cargarServicios():void {

    const id = this.route.snapshot.paramMap.get('empresaId');
    if (id) {
      this.empresaId = +id;
      console.log('Empresa ID:', this.empresaId);

      this.serviciosFactService.getServiciosByEmpresa(this.empresaId).subscribe({
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
    event.preventDefault();//para evitar que se recarge la pagian
  const url = `/servicio/${servicioId}`;
  const width = 800;   // ancho de la ventana
  const height = 600;  // alto de la ventana
  const left = (window.screen.width / 2) - (width / 2);  // centrada horizontalmente
  const top = (window.screen.height / 2) - (height / 2); // centrada verticalmente

  window.open(
    url,
    '_blank',
    `width=${width},height=${height},top=${top},left=${left},resizable=yes,scrollbars=yes`
  );
}

}


