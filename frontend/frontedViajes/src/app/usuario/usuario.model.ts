export interface Direccion {
  calle: string;
  numero: string;
  codigoPostal: string;
  ciudad: string;
  provincia: string;
  pais: string;
}

export interface Usuario {
  id: number;
  nombre: string;
  email: string;
  password?: string; // Opcional, normalmente no se envía al editar perfil
  roles?: string[];


  nif?: string;
  telefono?: string;
  cuentaBancaria?: string;
  imagenUrl?: string; // Para el logo
  direccion?: Direccion;
}
