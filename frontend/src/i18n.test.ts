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
  expect(translateText('비밀번호 변경','en')).toBe('Change Password');
  expect(translateText('조회','en')).toBe('Search');
  expect(translateText('신규 설비','en')).toBe('Add Equipment');
  expect(translateText('Excel 일괄등록','en')).toBe('Bulk Excel Import');
  expect(translateText('전체 라인','en')).toBe('All Lines');
  expect(translateText('전체 상태','en')).toBe('All Statuses');
  expect(translateText('승인 시 선택한 권한으로 사용자 계정이 생성됩니다.','en')).toBe('A user account will be created with the role selected at approval.');
  expect(translateText('승인 대기 0건','en')).toBe('0 Pending Approvals');
  expect(translateText('신청 권한과 실제 부여 권한을 비교한 후 승인하세요.','en')).toBe('Compare the requested role with the role to be assigned before approval.');
  expect(translateText('수정','en')).toBe('Edit');
 });
 it('preserves Korean when Korean is selected',()=>
 {
  expect(translateText('개인정보 저장','ko')).toBe('개인정보 저장');
 });
});
