#!/usr/bin/env bash
set -euo pipefail
ROOT=$(cd "$(dirname "$0")/.." && pwd); RUN="$ROOT/.dev-run"
for f in "$RUN"/*.pid; do [ -f "$f" ] || continue; pid=$(cat "$f"); kill "$pid" 2>/dev/null || true; rm -f "$f"; done
docker compose -f "$ROOT/docker-compose.yml" stop mysql
