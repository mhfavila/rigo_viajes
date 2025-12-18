export interface Direccion {
  calle: string;
  numero: string;
  codigoPostal: string;
  ciudad: string;
  provincia: string;
  pais: string;
}


export interface Empresa {
 // id?: number;        // opcional al crear
  nombre: string;
  cif: string;
  //direccion: string;
  direccion: Direccion;
  telefono: string;
  email: string;
  iban: string;
  usuarioId?: number;  // referencia al usuario propietario

}
