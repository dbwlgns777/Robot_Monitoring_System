# Database schema

## Registration approval lifecycle

`user_registration_request` stores signup requests as `PENDING`. Administrator approval copies the BCrypt
password hash into `system_user`, creates the requested `user_role`, and records `reviewed_by`, `reviewed_at`,
and `APPROVED` on the request in one transaction. Rejection records `REJECTED` without creating an account.
`V3__repair_admin_role.sql` also restores the `admin` to `ROLE_ADMIN` link when an older database is missing it.

All tables use InnoDB/utf8mb4. Exact executable definitions, nullability, defaults, keys and indexes are maintained in [`V1__baseline_schema.sql`](../backend/src/main/resources/db/migration/V1__baseline_schema.sql); this document is the table-level catalogue.

| Table | Key columns / types | Purpose |
|---|---|---|
| factory | id BIGINT PK, factory_code VARCHAR UQ, timezone VARCHAR, is_active BOOLEAN | Factory master |
| production_line | id PK, factory_id FK, line_code UQ | Line master |
| equipment | id PK, line_id FK, equipment_code UQ, equipment_type ENUM | Robot/press master |
| equipment_connection | equipment_id FK/UQ, protocol ENUM, is_read_only BOOLEAN | READ-only connection |
| equipment_tag_mapping | equipment_id FK, logical_tag_name/address/data_type | Tag mapping |
| product / product_line_standard | product_code UQ; product/line/equipment FKs | Product and standards |
| production_plan | work_order_number UQ, production_date, target_quantity | Work plan |
| robot_telemetry / press_telemetry | id PK, equipment_id + collected_at indexed, DATETIME(3) | Raw readings |
| equipment_current_state | equipment_id PK, operating_status, communication_status, display_status | Latest snapshot |
| equipment_state_event / downtime_event / alarm_event | equipment_id FK, start/end DATETIME(3), automatic/corrected values | Event history |
| production_count_event | source_event_key UQ, cumulative_count | Deduplicated count |
| hourly/daily_production_summary | line/time UQ, target/output/runtime/loss/cycle/uptime | Analytics |
| collection_health | equipment_id PK, receive times/success/failures/quality | Collection health |
| maintenance_history / maintenance_part_history | equipment_id FK, schedule/action/parts | Maintenance |
| system_user / registration / role / permission / joins | username UQ, BCrypt hash, approval/lock | Session RBAC |
| audit_log | user/action/before/after/IP/time | Audit |
| service_heartbeat / system_health_snapshot / setting / error / backup | service/time/status values | System health |
| report_history / robot_axis_telemetry | report metadata / axis values | Supporting data |

Every mutable master table includes `created_at` and `updated_at`; full column definitions and defaults are in the executable migration.
