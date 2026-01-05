
export interface Direccion {
  calle: string;
  numero: string;
  codigoPostal: string;
  ciudad: string;
  provincia: string;
  pais: string;
}


export interface EmpresaModalData {
  isEditMode: boolean;
  usuarioId: number; // Lo pasas en "Crear"
  id?: number;         // Lo pasas en "Editar" (como parte de la empresa)
  nombre?: string;
  cif?: string;
  direccion?: Direccion;
  telefono?: string;
  email?: string;
  iban?: string;
  precioKmDefecto?: number;
  precioHoraEsperaDefecto?: number;
  precioDietaDefecto?: number;

}
