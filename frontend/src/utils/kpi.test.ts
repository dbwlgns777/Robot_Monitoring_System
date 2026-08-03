import {describe,expect,it} from 'vitest'; import {achievementRate,estimatedLoss,uptimeRate} from './kpi';
describe('KPI calculation',()=>{it('생산 달성률',()=>expect(achievementRate(800,1000)).toBe(80));it('가동률',()=>expect(uptimeRate(420,480)).toBe(87.5));it('추정 생산손실',()=>expect(estimatedLoss(30,240)).toBe(120));});
