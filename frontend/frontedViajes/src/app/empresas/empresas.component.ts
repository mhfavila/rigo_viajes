import { Component, OnInit } from '@angular/core';
import { EmpresaService } from '../servicios/empresa.service';
import { AuthService } from '../servicios/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { EmpresaDetalleDialog } from '../empresa-detalle-dialog/empresa-detalle-dialog';

@Component({
  selector: 'app-empresas',
  standalone: false,
  templateUrl: './empresas.component.html',
  styleUrl: './empresas.component.css'
})
export class EmpresasComponent implements OnInit {
  empresas: any[] = [];

  constructor(
    private empresaService: EmpresaService,
    private authService: AuthService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    const usuarioId = this.authService.getUsuarioId();
    console.log('Usuario ID en frontend:', usuarioId);

    if (usuarioId) {
      this.empresaService.getEmpresasDeUsuario(usuarioId).subscribe({
        next: (data) => {
          console.log('Respuesta del backend:', data);
          this.empresas = data;
        },
        error: (err) => console.error('Error cargando empresas:', err)
      });
    } else {
      console.warn('No se encontró usuarioId, no se hace la llamada al backend');
    }
  }

  verEmpresa(empresa: any) {
    this.dialog.open(EmpresaDetalleDialog, {
      width: '400px',
      data: empresa
    });
  }
}
