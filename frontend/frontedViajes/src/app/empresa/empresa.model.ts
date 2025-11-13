export interface Empresa {
 // id?: number;        // opcional al crear
  nombre: string;
  cif: string;
  direccion: string;
  telefono: string;
  email: string;
  iban: string;
  usuarioId?: number;  // referencia al usuario propietario

}
