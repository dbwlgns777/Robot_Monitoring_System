#!/usr/bin/env bash
set -euo pipefail
ROOT=$(cd "$(dirname "$0")/.." && pwd); RUN="$ROOT/.dev-run"; mkdir -p "$RUN"
docker compose -f "$ROOT/docker-compose.yml" up -d mysql
start(){ name=$1; shift; (cd "$ROOT"; "$@" >"$RUN/$name.log" 2>&1 & echo $! >"$RUN/$name.pid"); }
start backend ./gradlew :backend:bootRun
start device ./gradlew :device-server:bootRun
start frontend npm --prefix frontend run dev
printf 'Frontend http://localhost:5173\nBackend http://localhost:8080\nDevice http://localhost:8081\nPID/log files: %s\n' "$RUN"
