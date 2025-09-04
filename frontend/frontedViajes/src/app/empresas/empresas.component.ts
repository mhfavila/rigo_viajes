import { Component, OnInit } from '@angular/core';
import { EmpresaService } from '../servicios/empresa.service';
import { AuthService } from '../servicios/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { EmpresaDetalleDialog } from '../empresa-detalle-dialog/empresa-detalle-dialog';
import { Router } from '@angular/router';
import { EmpresaModalComponent } from '../empresa-modal/empresa-modal.component';


@Component({
  selector: 'app-empresas',
  standalone: false,
  templateUrl: './empresas.component.html',
  styleUrl: './empresas.component.css',
})
export class EmpresasComponent implements OnInit {
  empresas: any[] = [];

  constructor(
    private empresaService: EmpresaService,
    private authService: AuthService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
   this.cargarEmpresas();
  }
  cargarEmpresas() {
     const usuarioId = this.authService.getUsuarioId();
    console.log('Usuario ID en frontend:', usuarioId);

    if (usuarioId) {
      this.empresaService.getEmpresasDeUsuario(usuarioId).subscribe({
        next: (data) => {
          console.log('Respuesta del backend:', data);
          this.empresas = data;
        },
        error: (err) => console.error('Error cargando empresas:', err),
      });
    } else {
      console.warn(
        'No se encontró usuarioId, no se hace la llamada al backend'
      );
    }
  }




  verEmpresa(empresa: any) {
    this.dialog.open(EmpresaDetalleDialog, {
      width: '400px',
      data: empresa,
    });
  }

  verViajes(empresa: any) {
    console.log('Navegando a viajes de la empresa con id:', empresa.id);
    this.router.navigate(['/viajes', empresa.id]);
  }
  abrirModal() {
  const dialogRef = this.dialog.open(EmpresaModalComponent, {
    width: '400px',
    data: {
      usuarioId: this.authService.getUsuarioId()
    }
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result) {
      this.cargarEmpresas() // recarga la lista después de crear la empresa
    }
  });
}
}
