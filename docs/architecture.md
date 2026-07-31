# Architecture

PRIMA FACTORY 360 — Unified Smart Factory Monitoring & Process Optimization Platform.

```text
Device Server Simulator (8081) -- 1 s transaction --> MySQL Community 8 (3306)
MySQL current/telemetry/events/summaries --> Spring Boot + MyBatis Backend (8080)
Backend -- REST snapshot + STOMP topics --> React/Vite Frontend (5173)
Frontend -- STOMP unavailable --> 1 s REST polling
```

The simulator is the only random-data producer. Backend queries `equipment_current_state` in one batch and never uses `ORDER BY RAND()`. `EquipmentDataCollector` exposes collection only; future MC Protocol and Modbus implementations must remain READ-only. UTC is stored in DB/API and rendered for `Asia/Seoul` by the client.
