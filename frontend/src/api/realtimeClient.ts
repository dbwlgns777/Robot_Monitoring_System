export interface RealtimeClient { connect(onRefresh:()=>void):()=>void; }
export const mockRealtimeClient:RealtimeClient={connect(onRefresh){const id=window.setInterval(onRefresh,10_000);return()=>clearInterval(id);}};
// Spring WebSocket/STOMP 적용 시 동일 인터페이스 구현체만 교체합니다.
