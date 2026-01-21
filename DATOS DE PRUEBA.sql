-- 1. LIMPIEZA TOTAL (Resetea todo)
TRUNCATE TABLE servicios, facturas, empresas, usuario_roles, usuarios RESTART IDENTITY CASCADE;

-- 2. USUARIOS
-- Contraseña '123456' encriptada
INSERT INTO usuarios (
    id, 
    nombre, 
    email, 
    password, 
    nif, 
    telefono, 
    cuenta_bancaria, 
    imagen_url,
    direccion_calle, direccion_ciudad, direccion_provincia, direccion_pais, direccion_cp
)
VALUES 
(100, 'Favila Montero', 'favilamontero@gmail.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlNBxBFve4ZlLa', '12345678Z', '600111111', 'ES9100000000000000001234', 'https://ui-avatars.com/api/?name=Favila+Montero&background=random', 'Calle Principal 1', 'Oviedo', 'Asturias', 'España', '33001'),
(200, 'Usuario Prueba 2', 'usuario2@test.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlNBxBFve4ZlLa', '87654321X', '600222222', 'ES9100000000000000005678', 'https://ui-avatars.com/api/?name=Usuario+Dos&background=random', 'Av. Secundaria 2', 'Madrid', 'Madrid', 'España', '28001');

-- Roles
INSERT INTO usuario_roles (usuario_id, rol) VALUES (100, 'USER'), (200, 'USER');

-- 3. EMPRESAS (5 por usuario)
INSERT INTO empresas (
    id, 
    nombre, 
    email, 
    cif, 
    iban, 
    telefono, 
    usuario_id,
    precio_km_defecto, 
    precio_hora_espera_defecto,
    direccion_calle, direccion_ciudad, direccion_pais
)
SELECT 
    gs + (CASE WHEN u_id = 100 THEN 1000 ELSE 2000 END), 
    'Empresa ' || gs || (CASE WHEN u_id = 100 THEN ' (Favila)' ELSE ' (User2)' END),
    'contacto_' || gs || '_' || u_id || '@empresa.com',
    (CASE WHEN u_id = 100 THEN 'B100000' || gs ELSE 'A200000' || gs END),
    'ES00' || u_id || '0000000000' || gs,
    '900' || u_id || '00' || gs,
    u_id,
    0.65, 
    25.00,
    'Polígono ' || gs, 'Ciudad Test', 'España'
FROM generate_series(1, 5) AS gs, (VALUES (100), (200)) AS t(u_id);

-- 4. FACTURAS (1 por empresa)
-- OJO: Aquí uso tus nombres de columnas REALES: total_bruto, total_factura, etc.
INSERT INTO facturas (
    id, 
    numero_factura, 
    fecha_emision, 
    total_bruto,      -- Correspondiente a @Column(name = "total_bruto")
    porcentaje_iva,   -- Correspondiente a @Column(name = "porcentaje_iva")
    importe_iva,      -- Correspondiente a @Column(name = "importe_iva")
    porcentaje_irpf,  -- Correspondiente a @Column(name = "porcentaje_irpf")
    importe_irpf,     -- Correspondiente a @Column(name = "importe_irpf")
    total_factura,    -- Correspondiente a @Column(name = "total_factura")
    empresa_id,
    usuario_id,
    estado
)
SELECT 
    id + 9000, 
    'FAC-2026-' || id, 
    CURRENT_DATE, 
    300.00,  -- total_bruto
    21.00,   -- porcentaje_iva
    63.00,   -- importe_iva
    15.00,   -- porcentaje_irpf
    45.00,   -- importe_irpf
    318.00,  -- total_factura
    id,      -- empresa_id
    usuario_id, -- usuario_id (NECESARIO según tu Factura.java)
    'EMITIDA'
FROM empresas;

-- 5. SERVICIOS (10 por empresa)
-- Servicios 1, 2 y 3 -> Asignados a la factura creada.
-- Servicios 4 al 10 -> Sin factura (NULL).
INSERT INTO servicios (
    empresa_id, 
    factura_id, 
    tipo_servicio, 
    fecha_servicio, 
    origen, 
    destino, 
    conductor, 
    matricula_vehiculo, 
    km, 
    precio_km, 
    importe_servicio, 
    dieta, 
    cliente_final, 
    observaciones
)
SELECT 
    e.id, 
    CASE 
        WHEN s_num <= 3 THEN e.id + 9000 -- Asigna factura a los 3 primeros
        ELSE NULL                        -- Deja libres el resto
    END,
    'Transporte Carga',
    CURRENT_DATE - (s_num || ' days')::interval,
    'Origen ' || s_num,
    'Destino ' || s_num,
    'Conductor ' || s_num,
    '1234-BBB',
    100, 
    0.65, 
    65.00, -- importe_servicio
    false, 
    'Cliente Final ' || s_num,
    'Observación servicio ' || s_num
FROM empresas e
CROSS JOIN generate_series(1, 10) AS s_num;

-- 6. RESETEAR CONTADORES (Para que no falle al crear nuevos desde la web)
SELECT setval('usuarios_id_seq', (SELECT MAX(id) FROM usuarios) + 1);
SELECT setval('empresas_id_seq', (SELECT MAX(id) FROM empresas) + 1);
SELECT setval('facturas_id_seq', (SELECT MAX(id) FROM facturas) + 1);
SELECT setval('servicios_id_seq', (SELECT MAX(id) FROM servicios) + 1);