# Database initialization

Flyway is the schema source of truth (`backend/src/main/resources/db/migration`). Local demo data is a repeatable migration under `db/devmigration`. All timestamps are stored as UTC `DATETIME(3)`; frontend displays Asia/Seoul.

Full development reset/import (drops only `prima_factory_360`):
```bash
mysql -u root -p < database/dump/prima_factory_360_full.sql
```
Docker Compose imports the same dump only when its named volume is first created. Physical DELETE is not used by APIs; `is_active` performs retirement.
