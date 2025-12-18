export interface Direccion {
  calle: string;
  numero: string;
  codigoPostal: string;
  ciudad: string;
  provincia: string;
  pais: string;
}

export interface Servicio {
  id: number;
  facturaId?: number;
  empresaId: number;
  tipoServicio: string;
  fechaServicio: string; // formato ISO: '2025-10-22'
  origen: Direccion;
  destino: Direccion;
  conductor?: string;
  matriculaVehiculo?: string;
  km: number;
  precioKm: number;
  importeServicio: number;
  dieta: boolean;
  precioDieta?: number;
  horasEspera?: number;
  importeEspera?: number;
  albaran?: string;
  clienteFinal?: string;
  observaciones?: string;
  orden?: number;
  seleccionado?: boolean;
}
