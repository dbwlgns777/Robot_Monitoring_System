# PRIMA FACTORY 360

**Unified Smart Factory Monitoring & Process Optimization Platform** — React, Spring Boot, Java Device Server Simulator, MyBatis 및 MySQL Community로 구성된 READ 전용 full-stack 프로토타입입니다.

## 모듈과 데이터 흐름

- `frontend/` — 기존 10개 React 화면, REST snapshot, STOMP 갱신, 1초 REST fallback
- `backend/` — Java 17 Spring Boot REST/session security/STOMP/MyBatis/Flyway (8080)
- `device-server/` — 독립 Java 17 Simulator; 설비별 1초 telemetry/current-state/health 저장 (8081)
- `common-domain/` — Java 상태 enum과 우선순위 판정
- `database/` — MySQL 8 전체 dump와 초기화 안내

```text
Device Simulator → MySQL telemetry/current/events/health
MySQL → Backend REST snapshot + 1초 STOMP batch → React
                                   STOMP 장애 → 1초 REST polling
```

실제 PLC/로봇 연결, WRITE 명령, 원격 기동·정지·값 변경 기능은 없습니다.

## 개발 환경

Temurin Java 17 이상, Gradle 8.14+, Node.js 20+/npm 10+, Docker Compose 또는 MySQL Community 8.x가 필요합니다. 비밀값을 포함하지 않은 `.env.example`을 `.env`로 복사하고 개발용 비밀번호를 로컬에서 변경하십시오. Backend와 Device Server는 저장소 루트의 `.env`에서 `DB_USER`와 `DB_PASSWORD`를 읽으며, 실제 비밀번호는 Git에 커밋하지 않습니다.

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

IntelliJ에서 Gradle 프로젝트로 연 후 공유 Gradle 설정 `Backend`, `Device Server`, `Frontend`를 개별 실행하거나 compound 설정 `PRIMA FACTORY 360`을 실행할 수 있습니다. 서버 공유 설정은 IDE의 모듈 classpath 추론에 의존하지 않고 각각 `:backend:bootRun`, `:device-server:bootRun`을 실행합니다.

`clean test bootJar` 성공 후에는 MySQL을 먼저 실행하고 루트 `.env`를 준비한 다음 IntelliJ 상단 실행 구성에서 `Backend`를 선택해 Run할 수 있습니다. 공유 Run Configuration은 루트 `.env`를 읽을 수 있도록 Working directory를 `$PROJECT_DIR$`로 고정합니다.

1. MySQL을 시작하고 `prima_factory_360` schema를 dump 또는 Flyway로 준비합니다.
2. `.env.example`을 루트 `.env`로 복사하고 `DB_USER`, `DB_PASSWORD`를 로컬 값으로 설정합니다.
3. IntelliJ의 `Backend` 구성을 실행한 뒤 `http://localhost:8080/actuator/health`가 `UP`인지 확인합니다.
4. `Device Server` 구성을 실행한 뒤 `http://localhost:8081/actuator/health`를 확인합니다.
5. `Frontend` 구성을 실행하고 `http://localhost:5173`에 접속합니다.

Backend 시작 중 `SQL State: 28000`, `Error Code: 1045`, `Access denied for user 'root'@'localhost'`가 발생하면 Spring/MyBatis bean 문제가 아니라 MySQL 인증 실패입니다. 먼저 애플리케이션과 같은 host/port/user로 직접 로그인한 뒤, 성공한 로컬 비밀번호를 루트 `.env`의 `DB_PASSWORD`에 설정합니다.

```powershell
mysql -h localhost -P 3306 -u root -p -e "SELECT USER(), CURRENT_USER(), VERSION();"
```

`.env`에는 실제 비밀번호를 Git에 커밋하지 않고 `DB_USER`, `DB_PASSWORD`, `DB_URL`만 로컬로 설정합니다. Docker MySQL 볼륨이 이미 생성된 뒤 `MYSQL_ROOT_PASSWORD`만 변경해도 기존 root 비밀번호는 자동 변경되지 않으므로, 기존 볼륨의 실제 비밀번호를 사용하거나 MySQL에서 안전하게 계정 비밀번호를 변경해야 합니다.

직접 만든 Application 구성에서 실행 명령에 `-classpath`가 없고 `ClassNotFoundException: com.prima.factory.ZES_PrimaFactoryBackendApplication`이 발생하면 해당 구성이 `backend.main` 모듈 classpath를 사용하지 않는 것입니다. 그 구성을 사용하지 말고 공유 `Backend` Gradle 구성을 선택하십시오. IntelliJ Community Edition에서도 Gradle Run Configuration은 사용할 수 있습니다.

### IntelliJ에서 “관련 Gradle 프로젝트가 연결되어 있지 않음”이 표시될 때

`gradlew` 파일 자체를 프로젝트로 열지 말고 저장소 루트의 `settings.gradle.kts` 또는 `build.gradle.kts`를 Gradle 프로젝트로 연결해야 합니다.

1. IntelliJ에서 `File > Open`을 선택하고 **저장소 루트 폴더**를 엽니다.
2. 오른쪽 Gradle 도구창에서 `+ (Link Gradle Project)`를 누르고 루트 `settings.gradle.kts`를 선택합니다.
3. `Settings > Build Tools > Gradle`에서 `Distribution`을 **Wrapper**, `Gradle JVM`을 **Temurin 17**로 설정합니다.
4. Gradle 도구창에서 `Reload All Gradle Projects`를 누릅니다.
5. 기존에 잘못 연결된 프로젝트가 있으면 Gradle 도구창에서 해당 항목만 `Unlink`한 뒤 루트 프로젝트를 다시 연결합니다.

`backend/build.gradle.kts` 편집기 상단의 `Gradle 프로젝트 연결`을 바로 누르면 Backend 폴더만 별도 프로젝트로 연결될 수 있으므로 사용하지 않습니다. 반드시 오른쪽 `Gradle` 도구창의 `+` 버튼에서 **저장소 루트의 `settings.gradle.kts`**를 선택하십시오. 연결 후 Gradle 도구창에는 하나의 `prima-factory-360` 루트 아래 `backend`, `common-domain`, `device-server`가 표시되어야 합니다.

`Task 'prepareKotlinBuildScriptModel' not found in project ':backend'`는 `backend/build.gradle.kts`를 독립 프로젝트로 잘못 연결했을 때 발생합니다. Gradle 도구창에서 잘못 연결된 `backend` 항목을 `Unlink Gradle Project`로 제거한 후, `+` 버튼으로 루트 `settings.gradle.kts`만 다시 연결하십시오. 편집기의 `스크립트 구성 로드`는 올바른 루트 연결이 끝난 뒤에 사용합니다.

왼쪽 `Project` 파일 트리에서 `backend` 폴더를 우클릭하면 `Unlink Gradle Project`가 나오지 않습니다. 그 메뉴의 `모듈 제거`도 선택하지 마십시오. `Unlink`는 오른쪽 `Gradle` 도구창에 연결된 Gradle 프로젝트 항목을 우클릭할 때만 표시됩니다. 오른쪽 Gradle 창이 비어 있으면 제거할 연결이 없는 상태이므로 `+` 버튼을 눌러 루트 `settings.gradle.kts`를 바로 연결하면 됩니다.

연결 메뉴가 보이지 않거나 이전 IDE 모델이 남아 있으면 IntelliJ를 닫고 `File > Open`에서 저장소 루트의 `settings.gradle.kts`를 선택한 뒤 `Open as Project`를 선택합니다. `.idea` 폴더나 `backend` 하위 폴더를 별도 프로젝트로 열지 않습니다.

저장소에는 표준 Gradle Wrapper 스크립트와 설정이 포함되어 있으므로 전역 Gradle 설치가 없어도 됩니다. PR 시스템의 바이너리 파일 제한을 피하기 위해 Wrapper JAR는 텍스트 payload인 `gradle-wrapper.jar.b64`로 보관하며, `gradlew` 또는 `gradlew.bat` 첫 실행 시 로컬 JAR로 자동 복원됩니다. 생성된 JAR는 Git에서 제외됩니다. 최초 연결 시 Gradle 배포파일과 Spring 의존성을 내려받으므로 인터넷 연결이 필요합니다.

> Git Pull 직후 `gradle/wrapper/gradle-wrapper.jar`가 없는 것은 정상입니다. 먼저 `gradle-wrapper.jar.b64`가 있는지 확인한 다음 `gradlew.bat`을 한 번 실행하면 JAR가 생성됩니다.

```powershell
Get-Item .\gradle\wrapper\gradle-wrapper.jar.b64
.\gradlew.bat --version
Get-Item .\gradle\wrapper\gradle-wrapper.jar
```

두 번째 명령은 JAR를 복원한 뒤 Gradle 8.14.4를 내려받습니다. 회사 방화벽이나 프록시가 다운로드를 차단하면 JAR는 생성되어도 Gradle 다운로드 오류가 별도로 표시될 수 있습니다.

`gradlew.bat --version` 결과의 `Launcher JVM`과 `Daemon JVM`은 Java 17 이상이면 됩니다. 이 프로젝트는 별도의 JDK 21 Toolchain을 요구하지 않으며 모든 Java 모듈을 Java 17 bytecode로 컴파일합니다.

```powershell
# 실제 설치된 Temurin 17 폴더로 변경하세요.
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\gradlew.bat --stop
java -version
.\gradlew.bat --version
.\gradlew.bat javaCompatibility
.\gradlew.bat clean test bootJar
```

IntelliJ에서도 `Settings > Build Tools > Gradle > Gradle JVM`을 같은 Temurin 17로 지정한 뒤 Gradle 프로젝트를 다시 로드해야 합니다. `javaCompatibility`는 `backend`, `common-domain`, `device-server` 모두 `source=17, target=17, release=17`을 출력합니다.

### `javaCompatibility` 작업을 찾지 못하는 경우

`Task 'javaCompatibility' not found`는 Java 또는 Gradle 버전 문제가 아니라, 현재 PowerShell에서 실행 중인 checkout의 루트 `build.gradle.kts`가 아직 Java 17 변경 전 버전이라는 뜻입니다. PR이 병합되기 전에는 기본 브랜치에서 `git pull`만 실행해도 PR 커밋이 내려오지 않습니다. PR을 먼저 병합하거나 Java 17 변경이 포함된 PR 브랜치를 checkout한 뒤 아래처럼 확인하십시오.

```powershell
git status --short --branch
git log -1 --oneline
Select-String -Path .\build.gradle.kts -Pattern "javaCompatibility|VERSION_17|JavaLanguageVersion"
```

정상 checkout에서는 최신 커밋에 `2b02d1a` 이후 변경이 포함되고, 검색 결과에 `javaCompatibility`와 `VERSION_17`이 표시되며 `JavaLanguageVersion.of(21)`은 표시되지 않습니다. IntelliJ에서 연 프로젝트와 PowerShell 경로가 같은지도 확인하십시오.

### `git pull`이 로컬 Gradle 파일 변경 때문에 중단되는 경우

로컬 변경을 삭제하지 말고 먼저 임시 보관한 후 pull합니다. 아래 명령은 `build.gradle.kts`만 stash하므로 다른 작업 파일에는 영향을 주지 않습니다.

```powershell
git diff -- .\build.gradle.kts
git stash push -m "local Java build setting before pull" -- .\build.gradle.kts
git pull
git log -3 --oneline
Select-String -Path .\build.gradle.kts -Pattern "javaCompatibility|VERSION_17|JavaLanguageVersion"
git stash show -p "stash@{0}"
```

pull된 파일에 이미 `VERSION_17`과 `javaCompatibility`가 있으면 로컬 stash는 같은 목적의 이전 수정일 가능성이 큽니다. `git stash show -p`로 내용을 확인한 뒤 불필요한 경우에만 `git stash drop "stash@{0}"`으로 제거하십시오. 로컬 변경에 별도의 필요한 내용이 있으면 `git stash pop`을 무조건 실행하지 말고 해당 부분만 새 파일에 수동 반영해 충돌을 피하십시오.

오류에 `backend/build.gradle.kts`처럼 다른 파일이 표시되면 해당 경로만 같은 방식으로 보관합니다. 아래 절차는 Backend Gradle 파일 외의 사용자 변경을 건드리지 않습니다.

```powershell
git diff -- .\backend\build.gradle.kts
git stash push -m "local backend Gradle setting before pull" -- .\backend\build.gradle.kts
git pull
git stash show -p "stash@{0}"
```

pull된 `backend/build.gradle.kts`에 필요한 `junit-platform-launcher` 변경이 이미 있으면 stash를 다시 적용하지 않습니다. 확인 후 중복된 로컬 변경일 때만 `git stash drop "stash@{0}"`으로 제거합니다.

IntelliJ가 `.run/Backend.run.xml`을 로컬에서 수정해 pull이 중단된 경우에도 해당 파일만 보관합니다. 원격 최신 설정은 `:backend:bootRun`을 사용하므로 기존 Application 설정을 pull 후 다시 적용하지 않습니다.

```powershell
git diff -- .\.run\Backend.run.xml
git stash push -m "local IntelliJ Backend run setting before pull" -- .\.run\Backend.run.xml
git pull
Select-String -Path .\.run\Backend.run.xml -Pattern ":backend:bootRun|GradleRunConfiguration"
git stash show -p "stash@{0}"
```

검색 결과에 `:backend:bootRun`과 `GradleRunConfiguration`이 있으면 새 공유 설정이 적용된 것입니다. 로컬 stash가 이전 `SpringBootApplicationConfigurationType` 또는 classpath 없는 Application 설정뿐이라면 확인 후 `git stash drop "stash@{0}"`으로 제거합니다.

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

로그인 시 `Encoded password does not look like BCrypt` 경고가 발생하면 `system_user.password_hash`가 API에서 `passwordHash`로 매핑되는 최신 Backend인지 확인하십시오. 인증 실패는 500으로 감추지 않고 401/403 상태와 API 오류 응답으로 반환합니다.

개발 Seed의 BCrypt 값은 문서화된 `password`와 일치하도록 테스트합니다. 기존 DB에 잘못된 개발 hash가 있으면 local profile의 repeatable Flyway migration이 `admin` 계정의 개발 hash와 승인·잠금 상태를 갱신합니다. 운영 profile에서는 이 개발 migration을 사용하지 않습니다.

## Frontend data source

기본값은 `VITE_DATA_SOURCE=api`이며 API 오류를 Mock으로 숨기지 않습니다. 독립 UI 확인에만 `.env`에서 `VITE_DATA_SOURCE=mock`을 사용합니다. Vite proxy가 `/api/v1`과 `/ws`를 8080으로 전달해 session cookie가 유지됩니다.

이전 루트 Frontend 설치에서 남은 `src/`, `node_modules/`, 루트 `package*.json` 등은 현재 `frontend/` 모듈이 사용하지 않습니다. Git에 추적되지 않는 로컬 잔여 파일은 아래 스크립트로 제거하며, 활성 `frontend/` 폴더는 보존합니다.

```powershell
.\scripts\cleanup-legacy-root-frontend.ps1
```

로그인 성공 후 보호 API가 403을 반환하면 서버 세션에 Spring Security 인증정보가 저장되지 않은 구버전 Backend일 수 있습니다. 최신 로그인 API는 `SPRING_SECURITY_CONTEXT`와 `USER_ID`를 같은 HTTP session에 저장하며, 인증 만료·권한 오류도 JSON `ZES_ApiResponse` 형식으로 반환합니다.

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

### Backend 컴파일 실패 진단

`Task :backend:compileJava FAILED`와 `BUILD FAILED`만으로는 실제 Java 컴파일 원인을 알 수 없습니다. 그보다 앞에 출력된 `파일경로:줄번호: error:` 메시지가 필요합니다. Windows PowerShell에서는 다음 스크립트로 캐시를 사용하지 않고 Backend를 다시 컴파일하고 전체 로그를 `backend-compile.log`에 저장합니다.

```powershell
.\scripts\diagnose-backend-build.ps1
Select-String -Path .\backend-compile.log -Pattern "error:|What went wrong" -Context 2,3
```

진단 로그는 로컬 생성 파일이며 Git에 커밋하지 마십시오. 문제를 보고할 때는 비밀번호나 환경변수 값이 아니라 첫 번째 `error:`의 파일 경로, 줄 번호, 오류 문장과 바로 위아래 코드만 제공하십시오.

Java 17 text block은 여는 `"""` 뒤에 줄바꿈이 반드시 필요합니다. `illegal text block open delimiter sequence, missing line terminator` 오류가 발생했던 `MonitoringMapper.currentEquipment()` SQL은 이 형식에 맞게 수정되어 있습니다.

### Common Domain 테스트 실패 진단

Gradle 테스트는 실패한 테스트명, assertion의 expected/actual 값, 전체 stack trace를 콘솔에 출력하도록 설정되어 있습니다. `:common-domain:test FAILED` 요약만 보이면 다음 명령으로 해당 모듈만 캐시 없이 다시 실행하십시오.

```powershell
.\gradlew.bat :common-domain:test --rerun-tasks --console=plain
Get-Content .\common-domain\build\test-results\test\TEST-*.xml
```

`Problems report`와 Gradle 9 deprecation 경고는 테스트 실패 원인이 아닙니다. 실제 원인은 콘솔의 `FAILED` 테스트명 아래 또는 `TEST-*.xml`의 `<failure>` 요소에 표시됩니다.

각 Java 모듈은 Gradle Test Executor가 JUnit 5 엔진을 안정적으로 시작하도록 `junit-platform-launcher`를 테스트 런타임 의존성으로 명시합니다. 테스트 클래스명 없이 `:common-domain:test FAILED`만 출력되던 경우 최신 의존성을 받은 뒤 위 명령을 다시 실행하십시오.

콘솔에 테스트명이 계속 나타나지 않으면 더 이상 Gradle 설정을 추측해서 변경하지 말고 다음 진단 스크립트를 실행하십시오. 이 스크립트는 전체 Gradle 로그를 보존하고 생성된 모든 JUnit XML에서 `<failure>`와 `<error>` 원인을 자동 추출합니다.

```powershell
.\scripts\diagnose-common-domain-tests.ps1
```

현재 모노레포의 루트 Gradle 프로젝트와 `backend`, `device-server`, `common-domain` 모듈 분리는 일반적인 멀티프로젝트 구조입니다. `common-domain:test` 실패는 폴더 구조가 아니라 테스트 실행 또는 테스트 결과 문제이며, XML 원인을 확인하기 전에는 모듈을 이동하거나 Gradle 구성을 반복 변경하지 않습니다.

### Backend controller/service naming rule

Backend HTTP controllers only validate/deserialise request arguments and delegate to a service. Business rules,
transaction boundaries, and mapper calls belong to `ZES_*Service` classes. New application-owned controller,
service, mapper, method, and variable identifiers use the `ZES_` prefix and Allman braces. Framework-defined
entry points and overridden framework method names (for example Java `main`) keep their required names.

Signup writes a `PENDING` row to `user_registration_request` in one transaction. The submitted factory name or
code must match an active row in `factory`; duplicate active users and duplicate pending requests are rejected.


### Backend class rename 후 중복 endpoint 오류

`AuthController`에서 `ZES_AuthController`로 이름을 변경한 뒤 기존 `AuthController.class`가
`backend/build/classes`에 남아 있으면 Spring이 두 컨트롤러를 함께 검색하여 `Ambiguous mapping`으로
시작에 실패합니다. 최신 `:backend:bootRun` 작업은 컴파일 후 구형 컴포넌트 class를 자동 삭제합니다.
이미 실행 중인 IntelliJ/Gradle 프로세스가 있으면 중지한 다음 최초 한 번은 다음과 같이 실행하십시오.

```powershell
.\gradlew.bat :backend:clean :backend:bootRun
```

이 명령은 소스나 데이터베이스를 삭제하지 않고 `backend/build`의 컴파일 산출물만 다시 생성합니다.
