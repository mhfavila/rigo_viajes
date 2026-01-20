-- ============================================================
-- 0. LIMPIEZA INICIAL (Borrón y cuenta nueva)
-- ============================================================
TRUNCATE TABLE servicios, facturas, viajes, empresas, usuario_roles, usuarios RESTART IDENTITY CASCADE;

-- ============================================================
-- 1. CREAR EL USUARIO (ID 1000, PASS '1234')
-- ============================================================
INSERT INTO usuarios (id, nombre, password, email, nif, cuenta_bancaria, telefono, direccion_calle, direccion_ciudad, direccion_pais)
VALUES (1000, 'usuario111', '$2a$10$VuH0FoCJNYLpbnQOHdt2WOICYvnxgqSMmmEt.Q5/jR3HIhkVGrJsO', 'usuario1@test.com', '12345678Z', 'ES1234567890123456789012', '600123456', 'Calle Test', 'Madrid', 'España');

-- Asignar Rol
INSERT INTO usuario_roles (usuario_id, rol) VALUES (1000, 'ROLE_USER');


-- ============================================================
-- 2. CREAR LAS DOS EMPRESAS (IDs 1000 y 2000)
-- ============================================================
INSERT INTO empresas (
    id, nombre, cif, telefono, email, usuario_id,
    direccion_calle, direccion_numero, direccion_cp, direccion_ciudad, direccion_provincia, direccion_pais,
    iban, precio_km_defecto, precio_hora_espera_defecto, precio_dieta_defecto
) VALUES
(1000, 'Transportes Rápidos S.L.', 'B12345678', '910000001', 'contacto@rapidos.com', 1000,
 'Calle Industria', '10', '28001', 'Madrid', 'Madrid', 'España', 'ES1234567890123456789012', 0.50, 10.00, 15.00),

(2000, 'Logística Global S.A.', 'A98765432', '930000001', 'info@global.com', 1000,
 'Avenida del Puerto', '55', '08001', 'Barcelona', 'Barcelona', 'España', 'ES9876543210987654321098', 0.60, 12.00, 20.00);


-- ============================================================
-- 3. CREAR LAS FACTURAS (Una para cada empresa)
-- ============================================================
-- Factura ID 500 para la Empresa 1000
INSERT INTO facturas (
    id, numero_factura, fecha_emision, empresa_id, usuario_id,
    total_bruto, porcentaje_iva, importe_iva, porcentaje_irpf, importe_irpf, total_factura,
    cuenta_bancaria, forma_pago, estado
) VALUES (
    500, 'F2025-001', '2025-01-30', 1000, 1000,
    1000.00, 21.00, 210.00, 15.00, 150.00, 1060.00,
    'ES1234567890123456789012', 'Transferencia', 'EMITIDA'
);

-- Factura ID 501 para la Empresa 2000
INSERT INTO facturas (
    id, numero_factura, fecha_emision, empresa_id, usuario_id,
    total_bruto, porcentaje_iva, importe_iva, porcentaje_irpf, importe_irpf, total_factura,
    cuenta_bancaria, forma_pago, estado
) VALUES (
    501, 'F2025-002', '2025-01-31', 2000, 1000,
    1500.00, 21.00, 315.00, 0.00, 0.00, 1815.00,
    'ES9876543210987654321098', 'Transferencia', 'EMITIDA'
);


-- ============================================================
-- 4. SERVICIOS EMPRESA 1000 (ADAPTADO A NUEVA ENTIDAD)
-- ============================================================

-- 10 Servicios FACTURADOS (vinculados a Factura 500)
INSERT INTO servicios (
    empresa_id, factura_id, tipo_servicio, fecha_servicio, 
    origen, destino, -- CORREGIDO: Texto simple
    km, precio_km, importe_servicio, conductor, matricula_vehiculo
)
SELECT
    1000, 500, 'Transporte Mercancía', CURRENT_DATE - (i || ' days')::INTERVAL, 
    'Madrid', 'Valencia', -- Valor texto simple
    100, 1.00, 100.00, 'Conductor A', '1234-BBB'
FROM generate_series(1, 10) AS i;

-- 5 Servicios SIN FACTURAR (factura_id NULL)
INSERT INTO servicios (
    empresa_id, factura_id, tipo_servicio, fecha_servicio, 
    origen, destino, -- CORREGIDO: Texto simple
    km, precio_km, importe_servicio, conductor, matricula_vehiculo
)
SELECT
    1000, NULL, 'Transporte Urgente', CURRENT_DATE + (i || ' days')::INTERVAL, 
    'Madrid', 'Sevilla', -- Valor texto simple
    200, 1.50, 300.00, 'Conductor B', '5678-CCC'
FROM generate_series(1, 5) AS i;


-- ============================================================
-- 5. SERVICIOS EMPRESA 2000 (ADAPTADO A NUEVA ENTIDAD)
-- ============================================================

-- 10 Servicios FACTURADOS (vinculados a Factura 501)
INSERT INTO servicios (
    empresa_id, factura_id, tipo_servicio, fecha_servicio, 
    origen, destino, -- CORREGIDO: Texto simple
    km, precio_km, importe_servicio, conductor, matricula_vehiculo
)
SELECT
    2000, 501, 'Mudanza', CURRENT_DATE - (i || ' days')::INTERVAL, 
    'Barcelona', 'Zaragoza', -- Valor texto simple
    150, 1.00, 150.00, 'Conductor C', '9012-DDD'
FROM generate_series(1, 10) AS i;

-- 5 Servicios SIN FACTURAR (factura_id NULL)
INSERT INTO servicios (
    empresa_id, factura_id, tipo_servicio, fecha_servicio, 
    origen, destino, -- CORREGIDO: Texto simple
    km, precio_km, importe_servicio, conductor, matricula_vehiculo
)
SELECT
    2000, NULL, 'Logística Interna', CURRENT_DATE + (i || ' days')::INTERVAL, 
    'Barcelona', 'Girona', -- Valor texto simple
    50, 2.00, 100.00, 'Conductor D', '3456-FFF'
FROM generate_series(1, 5) AS i;

-- Ajustar las secuencias para que los próximos IDs autogenerados no colisionen
SELECT setval('usuarios_id_seq', 2000, true);
SELECT setval('empresas_id_seq', 3000, true);
SELECT setval('facturas_id_seq', 1000, true);
SELECT setval('servicios_id_seq', 1000, true);