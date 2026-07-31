# PRIMA PRESS 360 — RobotMonitoringSystem

프레스·자동화 로봇 통합 모니터링 시스템의 **1차 React 프론트엔드 프로토타입**입니다. 실제 PLC, 로봇, Java 백엔드, MySQL에는 연결하지 않으며 모든 화면은 현실적인 Mock Data를 사용합니다. 제어/WRITE 기능은 존재하지 않습니다.

## 구현 계획 및 구조

```text
src/
├── api/          # REST 교체 경계, 인증 API, 실시간 클라이언트 인터페이스
├── components/   # KPI, 상태 배지, 공통 카드/로딩/오류 UI
├── data/         # 3개 라인·12대 로봇 Mock Data Provider
├── hooks/        # 비동기 조회 상태 처리
├── layouts/      # 반응형 산업용 사이드바 레이아웃
├── pages/        # 인증, 대시보드, 실시간, 분석, 등록, 수집상태 화면
├── styles/       # 공통 토큰 및 PC/모바일 반응형 스타일
├── types/        # 모든 도메인 TypeScript interface/type
└── utils/        # 상태 표준화와 KPI 계산식
```

Mock Provider를 `Spring Boot REST API` 구현체로 교체하고 `RealtimeClient`를 Spring WebSocket/STOMP 구현체로 교체할 수 있도록 화면과 데이터 계층을 분리했습니다.

## 화면

1. 로그인
2. 회원가입(관리자 승인 흐름)
3. 설비 등록·수정·복사·사용중지
4. 제품 등록·수정·복사·사용중지
5. 대표자 통합 대시보드
6. 공장·라인 실시간 현황 및 설비 상세 패널
7. 생산실적 분석
8. 가동·비가동/병목 분석
9. 알람·정비 분석
10. 데이터 수집 상태

## 실행

요구사항: Node.js 20 이상, npm 10 이상

```bash
cp .env.example .env
npm install
npm run dev
```

브라우저에서 `http://localhost:5173`을 엽니다. 프로토타입 로그인 입력값은 화면에 미리 채워져 있으며, 프론트엔드에는 실제 계정정보가 저장되지 않습니다.

## 검증

```bash
npm test
npm run build
npm run preview -- --host 0.0.0.0
```

빌드 결과는 `dist/`에 생성되며 향후 Spring Boot의 정적 리소스로 배포할 수 있습니다.

## 안전·연동 원칙

- 실제 PLC/로봇 통신을 수행하지 않습니다.
- 원격 기동, 정지, 값 변경 및 PLC WRITE 기능을 구현하지 않습니다.
- 통신 테스트 UI는 READ 전용임을 명시합니다.
- 외부 운영 접속은 회사 VPN 사용을 전제로 합니다.
- OEE/PPM은 품질 데이터 미연동 상태에서 임의 산출하지 않습니다.
- 생산손실은 실제 불량이 아닌 `비계획 정지시간 × 표준 시간당 생산량`의 추정값으로 표시합니다.
