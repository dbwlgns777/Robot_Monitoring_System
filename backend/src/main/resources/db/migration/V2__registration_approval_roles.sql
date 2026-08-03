INSERT INTO role(id, role_code, role_name)
VALUES(4, 'ROLE_MAINTENANCE', '설비·정비 담당자')
ON DUPLICATE KEY UPDATE
 role_code = VALUES(role_code),
 role_name = VALUES(role_name);

INSERT IGNORE INTO role_permission(role_id, permission_id)
SELECT r.id, p.id
  FROM role r
  JOIN permission p ON p.permission_code = 'MONITOR_READ'
 WHERE r.role_code = 'ROLE_MAINTENANCE';
