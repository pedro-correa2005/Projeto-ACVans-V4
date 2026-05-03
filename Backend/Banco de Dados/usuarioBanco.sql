-- 1. Criar usuário da aplicação
CREATE USER 'acvans_user'@'localhost' IDENTIFIED BY "";

-- 2. Dar permissões no banco específico
GRANT SELECT, INSERT, UPDATE, DELETE 
ON bdacvans.* 
TO 'acvans_user'@'localhost';

-- 3. Aplicar mudanças
FLUSH PRIVILEGES;