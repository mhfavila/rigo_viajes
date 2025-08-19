select * from usuarios;
SELECT * from usuario_roles;
select * from empresas;
select * from viajes;

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