INSERT INTO role(id, role_code, role_name)
VALUES(3, 'ROLE_ADMIN', '시스템 관리자')
ON DUPLICATE KEY UPDATE
 role_code = VALUES(role_code),
 role_name = VALUES(role_name);

INSERT IGNORE INTO user_role(user_id, role_id)
SELECT u.id, r.id
  FROM system_user u
  JOIN role r ON r.role_code = 'ROLE_ADMIN'
 WHERE u.username = 'admin';
