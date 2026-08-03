import {describe,expect,it} from 'vitest';
import {translateText} from './i18n';

describe('English UI translations',()=>
{
 it('translates labels inside monitoring and administration pages',()=>
 {
  expect(translateText('가동·비가동 분석','en')).toBe('Uptime / Downtime Analysis');
  expect(translateText('설비 등록 관리','en')).toBe('Equipment Management');
  expect(translateText('가입 승인 관리','en')).toBe('Signup Approval Management');
  expect(translateText('사용자 리스트','en')).toBe('User List');
 });
 it('preserves Korean when Korean is selected',()=>
 {
  expect(translateText('개인정보 저장','ko')).toBe('개인정보 저장');
 });
});
