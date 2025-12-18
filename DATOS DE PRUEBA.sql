-- ============================================================
-- 1. CREACIÓN DEL USUARIO
-- ============================================================
INSERT INTO usuarios (id, nombre, password, email) 
VALUES (1, 'Juan Dueño', 'pass1234', 'juan@email.com');

-- (Opcional) Roles si tienes la tabla creada
-- INSERT INTO usuario_roles (usuario_id, rol) VALUES (1, 'ADMIN');


-- ============================================================
-- 2. CREACIÓN DE LAS 2 EMPRESAS
-- Nota: Observa cómo la dirección se aplana en columnas 'direccion_xxx'
-- ============================================================

-- Empresa 1: Transportes Norte
INSERT INTO empresas (id, nombre, cif, telefono, email, iban, usuario_id, 
                      direccion_calle, direccion_numero, direccion_ciudad, direccion_cp, direccion_provincia, direccion_pais)
VALUES (1, 'Transportes Norte S.L.', 'B11111111', '600111222', 'norte@transporte.com', 'ES1234567890123456789012', 1,
        'Polígono Industrial A', '10', 'Bilbao', '48001', 'Vizcaya', 'España');

-- Empresa 2: Logística Sur
INSERT INTO empresas (id, nombre, cif, telefono, email, iban, usuario_id, 
                      direccion_calle, direccion_numero, direccion_ciudad, direccion_cp, direccion_provincia, direccion_pais)
VALUES (2, 'Logística Sur S.A.', 'A22222222', '600333444', 'sur@logistica.com', 'ES9876543210987654321098', 1,
        'Av. de la Palmera', '22', 'Sevilla', '41001', 'Sevilla', 'España');


-- ============================================================
-- 3. CREACIÓN DE LAS FACTURAS (3 por empresa)
-- ============================================================

-- Facturas para Empresa 1
INSERT INTO facturas (id, numero_factura, fecha_emision, empresa_id, usuario_id, total_bruto, porcentaje_iva, importe_iva, porcentaje_irpf, importe_irpf, total_factura, cuenta_bancaria, forma_pago, observaciones, estado)
VALUES 
(1, 'F2023-001', '2023-10-01', 1, 1, 1000.00, 21.00, 210.00, 15.00, 150.00, 1060.00, 'ES123...', 'Transferencia', 'Primera quincena', 'COBRADA'),
(2, 'F2023-002', '2023-10-15', 1, 1, 1500.00, 21.00, 315.00, 15.00, 225.00, 1590.00, 'ES123...', 'Transferencia', '', 'ENVIADA'),
(3, 'F2023-003', '2023-10-30', 1, 1, 800.00, 21.00, 168.00, 0.00, 0.00, 968.00, 'ES123...', 'Pagare', 'Vencimiento 60 días', 'BORRADOR');

-- Facturas para Empresa 2
INSERT INTO facturas (id, numero_factura, fecha_emision, empresa_id, usuario_id, total_bruto, porcentaje_iva, importe_iva, porcentaje_irpf, importe_irpf, total_factura, cuenta_bancaria, forma_pago, observaciones, estado)
VALUES 
(4, 'S2023-A01', '2023-10-05', 2, 1, 2000.00, 21.00, 420.00, 0.00, 0.00, 2420.00, 'ES987...', 'Confirming', '', 'COBRADA'),
(5, 'S2023-A02', '2023-10-20', 2, 1, 1200.00, 21.00, 252.00, 7.00, 84.00, 1368.00, 'ES987...', 'Transferencia', '', 'ENVIADA'),
(6, 'S2023-A03', '2023-11-01', 2, 1, 500.00, 21.00, 105.00, 0.00, 0.00, 605.00, 'ES987...', 'Contado', '', 'BORRADOR');


-- ============================================================
-- 4. CREACIÓN DE LOS SERVICIOS (10 por empresa)
-- Nota: Asignamos 'origen_xxx' y 'destino_xxx'
-- ============================================================

-- --- SERVICIOS EMPRESA 1 (IDs 1-10) ---

-- 3 Servicios para la Factura 1
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden,
                       origen_calle, origen_ciudad, origen_cp, origen_pais, destino_calle, destino_ciudad, destino_cp, destino_pais) VALUES
(1, 1, 1, 'Nacional', '2023-10-01', 'Paco', '1234-BBB', 500, 1.00, 500.00, true, 50.00, 0, 0, 'ALB-001', 'Mercadona', 'Sin incidencias', 1, 'C/ A', 'Madrid', '28001', 'España', 'C/ B', 'Barcelona', '08001', 'España'),
(2, 1, 1, 'Nacional', '2023-10-02', 'Paco', '1234-BBB', 300, 1.00, 300.00, false, 0.00, 1, 20.00, 'ALB-002', 'Carrefour', 'Espera en carga', 2, 'C/ B', 'Barcelona', '08001', 'España', 'C/ C', 'Valencia', '46001', 'España'),
(3, 1, 1, 'Local', '2023-10-03', 'Luis', '5678-CCC', 200, 1.00, 200.00, false, 0.00, 0, 0, 'ALB-003', 'Amazon', '', 3, 'C/ C', 'Valencia', '46001', 'España', 'C/ D', 'Alicante', '03001', 'España');

-- 3 Servicios para la Factura 2
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden,
                       origen_calle, origen_ciudad, origen_cp, origen_pais, destino_calle, destino_ciudad, destino_cp, destino_pais) VALUES
(4, 2, 1, 'Internacional', '2023-10-10', 'Paco', '1234-BBB', 1000, 1.20, 1200.00, true, 80.00, 0, 0, 'ALB-004', 'Michelin', 'Ruta Francia', 1, 'Irun', 'Gipuzkoa', '20300', 'España', 'Rue Paris', 'Paris', '75000', 'Francia'),
(5, 2, 1, 'Nacional', '2023-10-12', 'Luis', '5678-CCC', 200, 1.00, 200.00, false, 0.00, 0, 0, 'ALB-005', 'Inditex', '', 2, 'C/ E', 'Coruña', '15001', 'España', 'C/ F', 'Lugo', '27001', 'España'),
(6, 2, 1, 'Local', '2023-10-13', 'Luis', '5678-CCC', 100, 1.00, 100.00, false, 0.00, 0, 0, 'ALB-006', 'Zara', '', 3, 'C/ F', 'Lugo', '27001', 'España', 'C/ E', 'Coruña', '15001', 'España');

-- 2 Servicios para la Factura 3
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden,
                       origen_calle, origen_ciudad, origen_cp, origen_pais, destino_calle, destino_ciudad, destino_cp, destino_pais) VALUES
(7, 3, 1, 'Nacional', '2023-10-25', 'Ana', '9090-DDD', 400, 1.00, 400.00, true, 45.00, 0, 0, 'ALB-007', 'IKEA', '', 1, 'C/ G', 'Bilbao', '48001', 'España', 'C/ A', 'Madrid', '28001', 'España'),
(8, 3, 1, 'Nacional', '2023-10-26', 'Ana', '9090-DDD', 400, 1.00, 400.00, false, 0.00, 0, 0, 'ALB-008', 'IKEA', 'Retorno', 2, 'C/ A', 'Madrid', '28001', 'España', 'C/ G', 'Bilbao', '48001', 'España');

-- 2 Servicios SIN FACTURAR (Pendientes) - factura_id es NULL
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden,
                       origen_calle, origen_ciudad, origen_cp, origen_pais, destino_calle, destino_ciudad, destino_cp, destino_pais) VALUES
(9, NULL, 1, 'Urgente', '2023-11-01', 'Paco', '1234-BBB', 150, 2.00, 300.00, false, 0.00, 0, 0, 'ALB-009', 'DHL', 'Entrega urgente', 1, 'Aeropuerto', 'Bilbao', '48150', 'España', 'Centro', 'Santander', '39001', 'España'),
(10, NULL, 1, 'Local', '2023-11-02', 'Ana', '9090-DDD', 50, 1.50, 75.00, false, 0.00, 0, 0, 'ALB-010', 'Local', '', 2, 'Centro', 'Santander', '39001', 'España', 'Puerto', 'Santander', '39004', 'España');


-- --- SERVICIOS EMPRESA 2 (IDs 11-20) ---

-- 4 Servicios para Factura 4
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden,
                       origen_calle, origen_ciudad, origen_cp, origen_pais, destino_calle, destino_ciudad, destino_cp, destino_pais) VALUES
(11, 4, 2, 'Ruta Sur', '2023-10-01', 'Jose', '1111-HJK', 500, 1.00, 500.00, true, 40.00, 0, 0, 'SUR-001', 'Lidl', '', 1, 'Sevilla', 'Sevilla', '41001', 'España', 'Málaga', 'Málaga', '29001', 'España'),
(12, 4, 2, 'Ruta Sur', '2023-10-02', 'Jose', '1111-HJK', 500, 1.00, 500.00, true, 40.00, 0, 0, 'SUR-002', 'Lidl', '', 2, 'Málaga', 'Málaga', '29001', 'España', 'Almería', 'Almería', '04001', 'España'),
(13, 4, 2, 'Ruta Sur', '2023-10-03', 'Jose', '1111-HJK', 500, 1.00, 500.00, true, 40.00, 0, 0, 'SUR-003', 'Dia', '', 3, 'Almería', 'Almería', '04001', 'España', 'Granada', 'Granada', '18001', 'España'),
(14, 4, 2, 'Ruta Sur', '2023-10-04', 'Jose', '1111-HJK', 500, 1.00, 500.00, true, 40.00, 0, 0, 'SUR-004', 'Dia', '', 4, 'Granada', 'Granada', '18001', 'España', 'Sevilla', 'Sevilla', '41001', 'España');

-- 3 Servicios para Factura 5
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden,
                       origen_calle, origen_ciudad, origen_cp, origen_pais, destino_calle, destino_ciudad, destino_cp, destino_pais) VALUES
(15, 5, 2, 'Especial', '2023-10-15', 'Marta', '2222-LMN', 400, 1.00, 400.00, false, 0.00, 2, 50.00, 'SUR-005', 'Repsol', 'Carga Peligrosa', 1, 'Huelva', 'Huelva', '21001', 'España', 'Cádiz', 'Cádiz', '11001', 'España'),
(16, 5, 2, 'Especial', '2023-10-16', 'Marta', '2222-LMN', 400, 1.00, 400.00, false, 0.00, 0, 0, 'SUR-006', 'Repsol', '', 2, 'Cádiz', 'Cádiz', '11001', 'España', 'Córdoba', 'Córdoba', '14001', 'España'),
(17, 5, 2, 'Especial', '2023-10-17', 'Marta', '2222-LMN', 400, 1.00, 400.00, false, 0.00, 0, 0, 'SUR-007', 'Repsol', '', 3, 'Córdoba', 'Córdoba', '14001', 'España', 'Sevilla', 'Sevilla', '41001', 'España');

-- 1 Servicio para Factura 6
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden,
                       origen_calle, origen_ciudad, origen_cp, origen_pais, destino_calle, destino_ciudad, destino_cp, destino_pais) VALUES
(18, 6, 2, 'Corto', '2023-11-01', 'Jose', '1111-HJK', 500, 1.00, 500.00, false, 0.00, 0, 0, 'SUR-008', 'Local', '', 1, 'Sevilla', 'Sevilla', '41001', 'España', 'Dos Hermanas', 'Sevilla', '41700', 'España');

-- 2 Servicios SIN FACTURAR (Pendientes) - factura_id es NULL
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden,
                       origen_calle, origen_ciudad, origen_cp, origen_pais, destino_calle, destino_ciudad, destino_cp, destino_pais) VALUES
(19, NULL, 2, 'Portugal', '2023-11-05', 'Carlos', '3333-XYZ', 600, 1.20, 720.00, true, 60.00, 0, 0, 'SUR-009', 'Continente', 'Viaje a Faro', 1, 'Huelva', 'Huelva', '21001', 'España', 'Faro', 'Algarve', '8000', 'Portugal'),
(20, NULL, 2, 'Retorno', '2023-11-06', 'Carlos', '3333-XYZ', 600, 1.20, 720.00, false, 0.00, 0, 0, 'SUR-010', 'Continente', '', 2, 'Faro', 'Algarve', '8000', 'Portugal', 'Huelva', 'Huelva', '21001', 'España');