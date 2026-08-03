# Permanent repository rules
- All PLC and robot communication is strictly READ-only. Never implement WRITE commands or remote control.
- Never add start, stop, set-value, or equipment-control APIs/UI. Database INSERT/UPDATE is permitted.
- Ports: frontend 5173, backend 8080, device-server 8081, MySQL 3306.
- Run: `./gradlew :backend:bootRun`, `./gradlew :device-server:bootRun`, `npm --prefix frontend run dev`.
- Verify: `./gradlew clean test bootJar`, `npm --prefix frontend run test -- --run`, `npm --prefix frontend run build`, `git diff --check`.
