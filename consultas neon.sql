select * from usuarios;

SELECT * from empresas where usuario_id = 4;

select * from servicios WHERE empresa_id = 2;



--para borrar toda la base de datos ,despues hay que volver a ejecutar el render(backend)
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO public;
GRANT ALL ON SCHEMA public TO neondb_owner; 
-- (Nota: a veces el usuario se llama diferente, pero con las dos primeras líneas suele bastar en Neon)