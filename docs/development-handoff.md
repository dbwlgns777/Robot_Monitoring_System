# Development handoff

This branch preserves the complete PRIMA FACTORY 360 full-stack prototype from commit `1a1c686` and adds this handoff record without rewriting the existing history.

## Preserved modules

- `frontend`: React/Vite monitoring UI on port 5173
- `backend`: Spring Boot REST/STOMP application on port 8080
- `device-server`: Spring Boot simulator on port 8081
- `common-domain`: shared Java 17 status model
- `database`: MySQL 8 schema, Flyway migrations, and development dump on port 3306

## Safety invariant

PLC and robot integrations remain strictly READ-only. No equipment start, stop, set-value, WRITE command, or remote-control API/UI is permitted. Database telemetry INSERT/UPDATE operations are allowed.

## Verification

```bash
./gradlew clean test bootJar
npm --prefix frontend run test -- --run
npm --prefix frontend run build
git diff --check
```

## Local startup

```bash
./gradlew :backend:bootRun
./gradlew :device-server:bootRun
npm --prefix frontend run dev
```
