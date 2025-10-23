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

--++++++++++++++++
--BORRAR TABLAS> +
--++++++++++++++++

DROP TABLE IF EXISTS viajes CASCADE;
DROP TABLE IF EXISTS empresas CASCADE;
DROP TABLE IF EXISTS usuario_roles CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;
DROP TABLE IF EXISTS servicios CASCADE;
DROP TABLE IF EXISTS facturas CASCADE;