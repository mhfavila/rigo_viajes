
import { Component, OnInit } from '@angular/core';
import { EmpresaService } from '../../services/empresa.service';
import { AuthService } from '../../services/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { EmpresaDetalleDialog } from '../../empresa/empresa-detalle-dialog/empresa-detalle-dialog';
import { Router } from '@angular/router';
import { EmpresaModalComponent } from '../empresa-modal/empresa-modal.component';
import { MatSnackBar } from '@angular/material/snack-bar';


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
     private snackBar: MatSnackBar,
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

  verServicios(empresa: any) {
    console.log('Navegando a servicios de la empresa con id:', empresa.id);
    this.router.navigate(['empresas', empresa.id, 'servicios']);
  }
  crearEmpresa() {
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
editarEmpresa(empresa: any) {
  const dialogRef = this.dialog.open(EmpresaModalComponent, {
    width: '400px',
    data: { ...empresa,isEditMode:true } // pasamos los datos de la empresa
  });

  dialogRef.afterClosed().subscribe(result => {
    if (result) {
      this.cargarEmpresas(); // refresca la lista si se editó correctamente
    }
  });
}

// Método para eliminar empresa
eliminarEmpresa(empresa: any) {
  const confirmDelete = confirm(`¿Seguro que quieres eliminar la empresa "${empresa.nombre}"?`);
  if (confirmDelete) {
    this.empresaService.eliminarEmpresa(empresa.id).subscribe({
      next: () => {
        console.log('Empresa eliminada correctamente');
        this.cargarEmpresas(); // refresca la lista
        this.snackBar.open('Empresa eliminada', 'Cerrar', { duration: 3000 });
      },
      error: (err) => {
        console.error('Error eliminando la empresa', err);
        this.snackBar.open('Error al eliminar la empresa', 'Cerrar', { duration: 3000 });
      }
    });
  }
}



}
