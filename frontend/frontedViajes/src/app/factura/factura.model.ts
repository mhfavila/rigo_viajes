import { Servicio } from "../servicio/servicio.model";

export interface Factura{
  id: number;
  numeroFactura: string;
  fechaEmision: string; // En formato ISO (por ejemplo, '2025-10-24')
  empresaId: number; // Referencia al ID de la empresa
  usuarioId: number; // Referencia al ID del usuario

  totalBruto: number;
  porcentajeIva: number;
  importeIva: number;
  porcentajeIrpf?: number;
  importeIrpf?: number;
  totalFactura: number;

  cuentaBancaria?: string;
  formaPago?: string;
  observaciones?: string;
  estado?: string; // "BORRADOR", "ENVIADA", "COBRADA"

  servicios?: Servicio[]; // Relación con servicios (opcional)

}
