export interface Empresa {
 // id?: number;        // opcional al crear
  nombre: string;
  cif: string;
  direccion: string;
  telefono: string;
  email: string;
  usuarioId?: number;  // referencia al usuario propietario

}
