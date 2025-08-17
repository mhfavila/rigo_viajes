import { Component, OnInit } from '@angular/core';
import { EmpresaService } from '../servicios/empresa.service';
import { AuthService } from '../servicios/auth.service';

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
    private authService: AuthService
  ) {}

   ngOnInit(): void {
      const usuarioId = this.authService.getUsuarioId();  // método que debes añadir
    if (usuarioId) {
      this.empresaService.getEmpresasDeUsuario(usuarioId).subscribe({
        next: (data) => this.empresas = data,
        error: (err) => console.error('Error cargando empresas:', err)
      });
    }
  }
}
