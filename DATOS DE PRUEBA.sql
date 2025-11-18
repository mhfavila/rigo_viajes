-- =====================================================
-- CREACIÓN DE DATOS DE PRUEBA
-- 2 usuarios -> 4 empresas -> 5 viajes, 10 servicios, 3 facturas por empresa
-- =====================================================

-- ====== USUARIOS ======
INSERT INTO usuarios (id, nombre, password, email)
VALUES 
(101, 'usuario1', 'pass1', 'usuario1@example.com'),
(102, 'usuario2', 'pass2', 'usuario2@example.com');

-- ====== EMPRESAS ======
INSERT INTO empresas (id, nombre, cif, direccion, telefono, email, iban, usuario_id)
VALUES
(201, 'Transporte Norte S.L.', 'B12345678', 'Calle Mayor 10, Valladolid', '600111222', 'empresa1@example.com', 'ES9121000418450200051332', 101),
(202, 'Logística Este S.A.', 'B87654321', 'Avenida Castilla 22, Burgos', '600333444', 'empresa2@example.com', 'ES9121000418450200051444', 101),
(203, 'Transportes del Sur S.L.', 'B11223344', 'Calle Real 5, Sevilla', '600555666', 'empresa3@example.com', 'ES9121000418450200051555', 102),
(204, 'Servicios Atlántico S.L.', 'B99887766', 'Avenida Galicia 8, A Coruña', '600777888', 'empresa4@example.com', 'ES9121000418450200051666', 102);

-- ====== VIAJES ======
-- 5 viajes por empresa
INSERT INTO viajes (id, origen, destino, fecha, distancia, precio_km, importe_total, empresa_id) VALUES
(301, 'Valladolid', 'Madrid', '2025-10-01', 190, 0.80, 152.00, 201),
(302, 'Madrid', 'Salamanca', '2025-10-03', 210, 0.80, 168.00, 201),
(303, 'Burgos', 'León', '2025-10-05', 230, 0.75, 172.50, 201),
(304, 'Zamora', 'Soria', '2025-10-08', 250, 0.70, 175.00, 201),
(305, 'Ávila', 'Segovia', '2025-10-10', 120, 0.85, 102.00, 201),

(306, 'Burgos', 'Bilbao', '2025-10-02', 160, 0.90, 144.00, 202),
(307, 'Bilbao', 'Vitoria', '2025-10-04', 90, 0.95, 85.50, 202),
(308, 'Vitoria', 'Pamplona', '2025-10-06', 130, 0.80, 104.00, 202),
(309, 'Pamplona', 'Logroño', '2025-10-08', 150, 0.85, 127.50, 202),
(310, 'Logroño', 'Zaragoza', '2025-10-10', 180, 0.80, 144.00, 202),

(311, 'Sevilla', 'Granada', '2025-10-01', 250, 0.70, 175.00, 203),
(312, 'Granada', 'Málaga', '2025-10-03', 130, 0.80, 104.00, 203),
(313, 'Córdoba', 'Jaén', '2025-10-05', 110, 0.85, 93.50, 203),
(314, 'Sevilla', 'Huelva', '2025-10-07', 100, 0.90, 90.00, 203),
(315, 'Málaga', 'Cádiz', '2025-10-09', 200, 0.75, 150.00, 203),

(316, 'A Coruña', 'Lugo', '2025-10-02', 120, 0.80, 96.00, 204),
(317, 'Lugo', 'Ourense', '2025-10-04', 140, 0.85, 119.00, 204),
(318, 'Ourense', 'Pontevedra', '2025-10-06', 150, 0.80, 120.00, 204),
(319, 'Pontevedra', 'Ferrol', '2025-10-08', 160, 0.75, 120.00, 204),
(320, 'Ferrol', 'Santiago', '2025-10-10', 180, 0.70, 126.00, 204);

-- ====== SERVICIOS ======
-- 10 servicios por empresa
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, origen, destino, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden)
VALUES
-- Empresa 201
-- Fila 401: dieta era 0 -> FALSE
(401, NULL, 201, 'Transporte', '2025-10-01', 'Valladolid', 'Madrid', 'Juan Pérez', '1234ABC', 190, 0.80, 152.00, FALSE, 0, 0, 0, 'A001', 'Cliente A', 'Sin incidencias', 1),

-- Fila 402: dieta era 10 -> TRUE
(402, NULL, 201, 'Logística', '2025-10-02', 'Madrid', 'León', 'María López', '5678DEF', 250, 0.75, 187.50, TRUE, 5, 1, 10, 'A002', 'Cliente B', 'Entrega rápida', 2),

-- Fila 403: dieta era 0 -> FALSE
(403, NULL, 201, 'Transporte', '2025-10-03', 'León', 'Soria', 'Carlos Ruiz', '9999GGG', 300, 0.70, 210.00, FALSE, 0, 0, 0, 'A003', 'Cliente C', '', 3),

-- Fila 404: dieta era 10 -> TRUE
(404, NULL, 201, 'Especial', '2025-10-04', 'Soria', 'Ávila', 'Laura Díaz', '4321HJK', 280, 0.75, 210.00, TRUE, 5, 0, 0, 'A004', 'Cliente D', '', 4),

-- Fila 405: dieta era 0 -> FALSE
(405, NULL, 201, 'Transporte', '2025-10-05', 'Ávila', 'Salamanca', 'Pablo Gómez', '3456KLM', 200, 0.80, 160.00, FALSE, 0, 0, 0, 'A005', 'Cliente E', '', 5),

-- Fila 406: dieta era 0 -> FALSE
(406, NULL, 201, 'Transporte', '2025-10-06', 'Salamanca', 'Burgos', 'Pedro López', '1234JKL', 220, 0.75, 165.00, FALSE, 0, 0, 0, 'A006', 'Cliente F', '', 6),

-- Fila 407: dieta era 0 -> FALSE
(407, NULL, 201, 'Transporte', '2025-10-07', 'Burgos', 'Madrid', 'Sergio Díaz', '5555LLL', 240, 0.80, 192.00, FALSE, 0, 0, 0, 'A007', 'Cliente G', '', 7),

-- Fila 408: dieta era 5 -> TRUE
(408, NULL, 201, 'Logística', '2025-10-08', 'Madrid', 'Toledo', 'Raúl Gómez', '6543AAA', 180, 0.85, 153.00, TRUE, 5, 0, 0, 'A008', 'Cliente H', '', 8),

-- Fila 409: dieta era 0 -> FALSE
(409, NULL, 201, 'Especial', '2025-10-09', 'Toledo', 'Cuenca', 'José Ruiz', '7777PPP', 200, 0.80, 160.00, FALSE, 0, 0, 0, 'A009', 'Cliente I', '', 9),

-- Fila 410: dieta era 0 -> FALSE
(410, NULL, 201, 'Transporte', '2025-10-10', 'Cuenca', 'Valencia', 'Luis García', '8888TTT', 300, 0.75, 225.00, FALSE, 0, 0, 0, 'A010', 'Cliente J', '', 10);
-- Empresa 202
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, origen, destino, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden)
SELECT 410 + g.id AS id, NULL, 202, s.tipo_servicio, s.fecha_servicio, s.origen, s.destino, s.conductor, s.matricula_vehiculo, s.km, s.precio_km, s.importe_servicio, s.dieta, s.precio_dieta, s.horas_espera, s.importe_espera, 
       CONCAT('B', LPAD(g.id::text,3,'0')), s.cliente_final, s.observaciones, s.orden
FROM (SELECT * FROM servicios WHERE empresa_id = 201) s
JOIN generate_series(1,10) g(id) ON s.id = 400 + g.id;

-- Empresa 203
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, origen, destino, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden)
SELECT 420 + g.id AS id, NULL, 203, s.tipo_servicio, s.fecha_servicio, s.origen, s.destino, s.conductor, s.matricula_vehiculo, s.km, s.precio_km, s.importe_servicio, s.dieta, s.precio_dieta, s.horas_espera, s.importe_espera, 
       CONCAT('C', LPAD(g.id::text,3,'0')), s.cliente_final, s.observaciones, s.orden
FROM (SELECT * FROM servicios WHERE empresa_id = 201) s
JOIN generate_series(1,10) g(id) ON s.id = 400 + g.id;

-- Empresa 204
INSERT INTO servicios (id, factura_id, empresa_id, tipo_servicio, fecha_servicio, origen, destino, conductor, matricula_vehiculo, km, precio_km, importe_servicio, dieta, precio_dieta, horas_espera, importe_espera, albaran, cliente_final, observaciones, orden)
SELECT 430 + g.id AS id, NULL, 204, s.tipo_servicio, s.fecha_servicio, s.origen, s.destino, s.conductor, s.matricula_vehiculo, s.km, s.precio_km, s.importe_servicio, s.dieta, s.precio_dieta, s.horas_espera, s.importe_espera, 
       CONCAT('D', LPAD(g.id::text,3,'0')), s.cliente_final, s.observaciones, s.orden
FROM (SELECT * FROM servicios WHERE empresa_id = 201) s
JOIN generate_series(1,10) g(id) ON s.id = 400 + g.id;


-- ====== FACTURAS ======
INSERT INTO facturas (id, numero_factura, fecha_emision, empresa_id, usuario_id, total_bruto, porcentaje_iva, importe_iva, porcentaje_irpf, importe_irpf, total_factura, cuenta_bancaria, forma_pago, observaciones, estado)
VALUES
-- Empresa 201
(501, 'F-201-001', '2025-10-10', 201, 101, 500.00, 21.00, 105.00, 0, 0, 605.00, 'ES9121000418450200051332', 'TRANSFERENCIA', 'Factura emitida automáticamente', 'COBRADA'),
(502, 'F-201-002', '2025-10-15', 201, 101, 350.00, 21.00, 73.50, 0, 0, 423.50, 'ES9121000418450200051332', 'TRANSFERENCIA', '', 'BORRADOR'),
(503, 'F-201-003', '2025-10-20', 201, 101, 420.00, 21.00, 88.20, 0, 0, 508.20, 'ES9121000418450200051332', 'TRANSFERENCIA', '', 'ENVIADA'),
-- Empresa 202
(504, 'F-202-001', '2025-10-10', 202, 101, 480.00, 21.00, 100.80, 0, 0, 580.80, 'ES9121000418450200051444', 'TRANSFERENCIA', '', 'COBRADA'),
(505, 'F-202-002', '2025-10-15', 202, 101, 370.00, 21.00, 77.70, 0, 0, 447.70, 'ES9121000418450200051444', 'TRANSFERENCIA', '', 'BORRADOR'),
(506, 'F-202-003', '2025-10-20', 202, 101, 420.00, 21.00, 88.20, 0, 0, 508.20, 'ES9121000418450200051444', 'TRANSFERENCIA', '', 'ENVIADA'),
-- Empresa 203
(507, 'F-203-001', '2025-10-10', 203, 102, 450.00, 21.00, 94.50, 0, 0, 544.50, 'ES9121000418450200051555', 'TRANSFERENCIA', '', 'COBRADA'),
(508, 'F-203-002', '2025-10-15', 203, 102, 320.00, 21.00, 67.20, 0, 0, 387.20, 'ES9121000418450200051555', 'TRANSFERENCIA', '', 'BORRADOR'),
(509, 'F-203-003', '2025-10-20', 203, 102, 410.00, 21.00, 86.10, 0, 0, 496.10, 'ES9121000418450200051555', 'TRANSFERENCIA', '', 'ENVIADA'),
-- Empresa 204
(510, 'F-204-001', '2025-10-10', 204, 102, 480.00, 21.00, 100.80, 0, 0, 580.80, 'ES9121000418450200051666', 'TRANSFERENCIA', '', 'COBRADA'),
(511, 'F-204-002', '2025-10-15', 204, 102, 350.00, 21.00, 73.50, 0, 0, 423.50, 'ES9121000418450200051666', 'TRANSFERENCIA', '', 'BORRADOR'),
(512, 'F-204-003', '2025-10-20', 204, 102, 420.00, 21.00, 88.20, 0, 0, 508.20, 'ES9121000418450200051666', 'TRANSFERENCIA', '', 'ENVIADA');

-- ====== ASIGNACIÓN DE SERVICIOS A FACTURAS ======
-- Empresa 201
UPDATE servicios SET factura_id = 501 WHERE id IN (401,402,403);
UPDATE servicios SET factura_id = 502 WHERE id IN (404,405);
UPDATE servicios SET factura_id = 503 WHERE id IN (406,407,408);
-- Empresa 202
UPDATE servicios SET factura_id = 504 WHERE id IN (411,412,413);
UPDATE servicios SET factura_id = 505 WHERE id IN (414,415);
UPDATE servicios SET factura_id = 506 WHERE id IN (416,417,418);
-- Empresa 203
UPDATE servicios SET factura_id = 507 WHERE id IN (421,422,423);
UPDATE servicios SET factura_id = 508 WHERE id IN (424,425);
UPDATE servicios SET factura_id = 509 WHERE id IN (426,427,428);
-- Empresa 204
UPDATE servicios SET factura_id = 510 WHERE id IN (431,432,433);
UPDATE servicios SET factura_id = 511 WHERE id IN (434,435);
UPDATE servicios SET factura_id = 512 WHERE id IN (436,437,438);