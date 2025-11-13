export interface EmpresaModalData {
  isEditMode: boolean;
  usuarioId: number; // Lo pasas en "Crear"
  id?: number;         // Lo pasas en "Editar" (como parte de la empresa)
  nombre?: string;
  cif?: string;
  direccion?: string;
  telefono?: string;
  email?: string;
  iban?: string;

}
