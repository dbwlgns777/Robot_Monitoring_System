import {describe,expect,it} from 'vitest';import {statusMeta} from './status';
describe('공통 설비 상태',()=>{it('모든 상태에 이름과 색상이 있다',()=>{for(const value of Object.values(statusMeta)){expect(value.label).toBeTruthy();expect(value.color).toMatch(/^var\(--/);}});it('통신장애가 검정 토큰을 사용한다',()=>expect(statusMeta.COMMUNICATION_LOSS.color).toBe('var(--black)'));});
