ALTER TABLE system_user
    ADD COLUMN factory_id BIGINT NULL AFTER password_hash,
    ADD COLUMN department VARCHAR(100) NULL AFTER factory_id,
    ADD COLUMN position VARCHAR(100) NULL AFTER department,
    ADD COLUMN phone VARCHAR(30) NULL AFTER position,
    ADD COLUMN email VARCHAR(150) NULL AFTER phone,
    ADD CONSTRAINT fk_system_user_factory FOREIGN KEY(factory_id) REFERENCES factory(id);

UPDATE role SET role_name='관리자' WHERE role_code='ROLE_ADMIN';

INSERT INTO role(id, role_code, role_name)
VALUES(5, 'ROLE_USER', '일반 유저')
ON DUPLICATE KEY UPDATE role_name=VALUES(role_name);

INSERT IGNORE INTO user_role(user_id, role_id)
SELECT u.id, r.id
  FROM system_user u
  JOIN role r ON r.role_code='ROLE_USER'
 WHERE NOT EXISTS (
       SELECT 1 FROM user_role existing
       JOIN role current_role ON current_role.id=existing.role_id
       WHERE existing.user_id=u.id AND current_role.role_code='ROLE_ADMIN'
 );

DELETE ur FROM user_role ur
JOIN role r ON r.id=ur.role_id
WHERE r.role_code NOT IN ('ROLE_ADMIN','ROLE_USER');

DELETE rp FROM role_permission rp
JOIN role r ON r.id=rp.role_id
WHERE r.role_code NOT IN ('ROLE_ADMIN','ROLE_USER');

DELETE FROM role WHERE role_code NOT IN ('ROLE_ADMIN','ROLE_USER');
