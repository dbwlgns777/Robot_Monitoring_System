# PRIMA FACTORY 360

**Unified Smart Factory Monitoring & Process Optimization Platform** — React, Spring Boot, Java Device Server Simulator, MyBatis 및 MySQL Community로 구성된 READ 전용 full-stack 프로토타입입니다.

## 모듈과 데이터 흐름

- `frontend/` — 기존 10개 React 화면, REST snapshot, STOMP 갱신, 1초 REST fallback
- `backend/` — Java 21 Spring Boot REST/session security/STOMP/MyBatis/Flyway (8080)
- `device-server/` — 독립 Java 21 Simulator; 설비별 1초 telemetry/current-state/health 저장 (8081)
- `common-domain/` — Java 상태 enum과 우선순위 판정
- `database/` — MySQL 8 전체 dump와 초기화 안내

```text
Device Simulator → MySQL telemetry/current/events/health
MySQL → Backend REST snapshot + 1초 STOMP batch → React
                                   STOMP 장애 → 1초 REST polling
```

실제 PLC/로봇 연결, WRITE 명령, 원격 기동·정지·값 변경 기능은 없습니다.

## 개발 환경

Java 21, Gradle 8.14+, Node.js 20+/npm 10+, Docker Compose 또는 MySQL Community 8.x가 필요합니다. 비밀값을 포함하지 않은 `.env.example`을 `.env`로 복사하고 개발용 비밀번호를 로컬에서 변경하십시오. Backend와 Device Server는 저장소 루트의 `.env`에서 `DB_USER`와 `DB_PASSWORD`를 읽으며, 실제 비밀번호는 Git에 커밋하지 않습니다.

### Docker MySQL과 전체 동시 실행

```bash
cp .env.example .env
npm --prefix frontend install
./scripts/dev-start.sh
```

Windows PowerShell:

```powershell
Copy-Item .env.example .env
npm.cmd --prefix frontend install
.\scripts\dev-start.ps1
```

종료는 `./scripts/dev-stop.sh` 또는 `.\scripts\dev-stop.ps1`이며, PID 파일로 이 프로젝트가 시작한 프로세스만 종료합니다.

### 개별 실행

```bash
docker compose up -d mysql
./gradlew :backend:bootRun
./gradlew :device-server:bootRun
npm --prefix frontend run dev
```

IntelliJ에서 Gradle 프로젝트로 연 후 공유 설정 `Backend`, `Device Server`, `Frontend`를 개별 실행하거나 compound 설정 `PRIMA FACTORY 360`을 실행할 수 있습니다.

### IntelliJ에서 “관련 Gradle 프로젝트가 연결되어 있지 않음”이 표시될 때

`gradlew` 파일 자체를 프로젝트로 열지 말고 저장소 루트의 `settings.gradle.kts` 또는 `build.gradle.kts`를 Gradle 프로젝트로 연결해야 합니다.

1. IntelliJ에서 `File > Open`을 선택하고 **저장소 루트 폴더**를 엽니다.
2. 오른쪽 Gradle 도구창에서 `+ (Link Gradle Project)`를 누르고 루트 `settings.gradle.kts`를 선택합니다.
3. `Settings > Build Tools > Gradle`에서 `Distribution`을 **Wrapper**, `Gradle JVM`을 **Java 21**로 설정합니다.
4. Gradle 도구창에서 `Reload All Gradle Projects`를 누릅니다.
5. 기존에 잘못 연결된 프로젝트가 있으면 Gradle 도구창에서 해당 항목만 `Unlink`한 뒤 루트 프로젝트를 다시 연결합니다.

저장소에는 표준 Gradle Wrapper 스크립트와 설정이 포함되어 있으므로 전역 Gradle 설치가 없어도 됩니다. PR 시스템의 바이너리 파일 제한을 피하기 위해 Wrapper JAR는 텍스트 payload인 `gradle-wrapper.jar.b64`로 보관하며, `gradlew` 또는 `gradlew.bat` 첫 실행 시 로컬 JAR로 자동 복원됩니다. 생성된 JAR는 Git에서 제외됩니다. 최초 연결 시 Gradle 배포파일과 Spring 의존성을 내려받으므로 인터넷 연결이 필요합니다.

## 주소

| Service | URL |
|---|---|
| Frontend | http://localhost:5173 |
| Backend | http://localhost:8080 |
| Swagger | http://localhost:8080/swagger-ui.html |
| Backend health | http://localhost:8080/actuator/health |
| Device health | http://localhost:8081/actuator/health |
| MySQL | localhost:3306 / `prima_factory_360` |

## DB 초기화

```bash
mysql -u root -p < database/dump/prima_factory_360_full.sql
```

이 개발 dump는 **오직 `prima_factory_360` schema만** drop/recreate하며 전체 테이블, key/index와 상대시간 demo data를 포함합니다. 빈 DB에서는 Backend local profile의 Flyway가 같은 schema와 repeatable demo seed를 생성합니다.

개발 전용 계정은 `admin` / `password`입니다. 운영 profile에서는 dev migration을 사용하지 말고 계정을 별도로 생성해야 합니다.

## Frontend data source

기본값은 `VITE_DATA_SOURCE=api`이며 API 오류를 Mock으로 숨기지 않습니다. 독립 UI 확인에만 `.env`에서 `VITE_DATA_SOURCE=mock`을 사용합니다. Vite proxy가 `/api/v1`과 `/ws`를 8080으로 전달해 session cookie가 유지됩니다.

## 검증

```bash
./gradlew clean test bootJar
npm --prefix frontend ci
npm --prefix frontend run test -- --run
npm --prefix frontend run build
./scripts/smoke-test.sh
git diff --check
```

DB 상세는 `docs/database-schema.md`, API와 STOMP 주소는 `docs/api-contract.md`, 안전 불변조건은 `docs/read-only-safety.md`를 참조하십시오.
