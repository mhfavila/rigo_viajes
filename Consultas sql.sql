select * from usuarios;
SELECT * from usuario_roles;
select * from empresas;
select * from viajes;

select * from facturas;
SELECT * from servicios ;

----para ver los servicios que tiene una factura
SELECT *
FROM servicios
WHERE factura_id = 10;


select * from servicios WHERE servicios.empresa_id = 201;

select * from facturas WHERE id = 10;
select * from usuarios WHERE id = 7;

select * from empresas WHERE id = 19;

SELECT *
FROM servicios
WHERE empresa_id = 19;



SELECT 
    u.id AS usuario_id,
    u.nombre AS usuario_nombre,
    u.email,
    u.password,
    STRING_AGG(ur.rol, ', ') AS roles
FROM usuarios u
LEFT JOIN usuario_roles ur 
    ON u.id = ur.usuario_id
GROUP BY u.id, u.nombre, u.email, u.password;



DROP SCHEMA public CASCADE;
CREATE SCHEMA public;





SELECT e.id AS empresa_id,
       e.nombre AS empresa_nombre,
       v.id AS viaje_id,
       v.destino AS viaje_destino,
       v.fecha AS viaje_fecha,
       v.precio_km AS viaje_precio
FROM empresas e
JOIN usuarios u ON e.usuario_id = u.id
LEFT JOIN viajes v ON v.empresa_id = e.id
WHERE u.id = 2  -- aquí reemplaza con el ID del usuario
ORDER BY e.id, v.id;



SELECT u.id AS usuario_id,
       u.nombre AS usuario_nombre,
       e.id AS empresa_id,
       e.nombre AS empresa_nombre,
       v.id AS viaje_id,
       v.destino AS viaje_destino,
       v.fecha AS viaje_fecha,
       v.precio_km AS viaje_precio
FROM usuarios u
LEFT JOIN empresas e ON e.usuario_id = u.id
LEFT JOIN viajes v ON v.empresa_id = e.id
ORDER BY u.id, e.id, v.id;


select * from empresas where usuario_id = 1;

SELECT * 
FROM usuarios u
JOIN empresas e ON e.usuario_id = u.id
WHERE e.nombre LIKE '%LambdaDesign%';





SELECT u.id, u.nombre, e.id AS empresa_id, e.nombre AS nombre_empresa
FROM usuarios u
JOIN empresas e ON e.usuario_id = u.id
WHERE e.id = 1;


select * from empresas e join usuarios ON usuarios.id = e.usuario_id where u.id =1;

SELECT * FROM empresas e WHERE e.usuario_id = 1;

-----------------

--ver los servicio sde la empresa 201

SELECT 
    s.id AS servicio_id,
    s.tipo_servicio,
    s.fecha_servicio,
    s.origen,
    s.destino,
    s.conductor,
    s.matricula_vehiculo,
    s.km,
    s.precio_km,
    s.importe_servicio,
    s.cliente_final,
    s.observaciones
FROM servicios s
WHERE s.empresa_id = 201
ORDER BY s.id;
------------------------
--consulta para ver las facturas de una empresa
SELECT *
FROM facturas
WHERE empresa_id = 201
ORDER BY fecha_emision DESC;
-----------------------
--consulta para ver las empresasa que no tienen servicios
SELECT 
    e.id AS empresa_id,
    e.nombre AS empresa_nombre,
    e.cif,
    e.direccion,
    e.telefono,
    e.email
FROM empresas e
LEFT JOIN servicios s ON s.empresa_id = e.id
WHERE s.id IS NULL
ORDER BY e.id;

-----------------

--Quiero una consulta para sacar los servicios de una factura


SELECT s.*
FROM servicios s
JOIN facturas f ON s.factura_id = f.id
WHERE f.numero_factura = 'F-0NaN';

-------------------------

--++++++++++++++++
--BORRAR TABLAS> +
--++++++++++++++++

DROP TABLE IF EXISTS viajes CASCADE;
DROP TABLE IF EXISTS empresas CASCADE;
DROP TABLE IF EXISTS usuario_roles CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS servicios CASCADE;
DROP TABLE IF EXISTS facturas CASCADE;




-- Insertamos datos de prueba en la tabla 'servicios'
INSERT INTO servicios (
    factura_id, 
    empresa_id, 
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
    precio_dieta, 
    horas_espera, 
    importe_espera, 
    albaran, 
    cliente_final, 
    observaciones, 
    orden
) VALUES 
(
    NULL,                           -- factura_id (NULL = no facturado)
    201,                            -- empresa_id (Tu ID requerido)
    'Nacional',                     -- tipo_servicio
    '2025-10-15',                   -- fecha_servicio
    'Madrid',                       -- origen
    'Barcelona',                    -- destino
    'Juan Pérez',                   -- conductor
    '1234 ABC',                     -- matricula_vehiculo
    620,                            -- km
    1.30,                           -- precio_km
    806.00,                         -- importe_servicio (620 * 1.30)
    1.0,                            -- dieta (1 dieta)
    50.00,                          -- precio_dieta
    0.0,                            -- horas_espera
    0.0,                            -- importe_espera
    'ALB-2025-001',                 -- albaran
    'Cliente A',                    -- cliente_final
    'Carga estándar. Entrega en 24h.', -- observaciones
    1                               -- orden
),
(
    NULL,                              -- factura_id (Asumimos que la Factura 1 existe)
    201,                            -- empresa_id
    'Internacional',                -- tipo_servicio
    '2025-10-20',                   -- fecha_servicio
    'Valencia',                     -- origen
    'París (Francia)',              -- destino
    'Ana Gómez',                    -- conductor
    '5678 XYZ',                     -- matricula_vehiculo
    1500,                           -- km
    1.50,                           -- precio_km
    2250.00,                        -- importe_servicio (1500 * 1.50)
    2.0,                            -- dieta (2 dietas)
    65.00,                          -- precio_dieta
    3.5,                            -- horas_espera
    105.00,                         -- importe_espera (ej: 3.5h * 30€/h)
    'ALB-2025-002',                 -- albaran
    'Cliente B (Internacional)',    -- cliente_final
    'Carga refrigerada. 3.5 horas de espera en aduana.', -- observaciones
    2                               -- orden
),
(
    NULL,                           -- factura_id (no facturado)
    201,                            -- empresa_id
    'Regional',                     -- tipo_servicio
    '2025-10-22',                   -- fecha_servicio
    'Sevilla',                      -- origen
    'Málaga',                       -- destino
    'Carlos Ruiz',                  -- conductor
    '9012 DEF',                     -- matricula_vehiculo
    200,                            -- km
    1.10,                           -- precio_km
    220.00,                         -- importe_servicio (200 * 1.10)
    0.0,                            -- dieta
    0.0,                            -- precio_dieta
    0.0,                            -- horas_espera
    0.0,                            -- importe_espera
    'ALB-2025-003',                 -- albaran
    'Cliente C (Local)',            -- cliente_final
    NULL,                           -- observaciones
    3                               -- orden
);