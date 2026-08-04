#!/usr/bin/env bash
set -euo pipefail
curl -fsS http://localhost:8080/actuator/health
curl -fsS http://localhost:8081/actuator/health
curl -fsS -c /tmp/prima-cookie -H 'Content-Type: application/json' -d '{"username":"admin","password":"password"}' http://localhost:8080/api/v1/auth/login
curl -fsS -b /tmp/prima-cookie http://localhost:8080/api/v1/realtime/equipment
rm -f /tmp/prima-cookie
