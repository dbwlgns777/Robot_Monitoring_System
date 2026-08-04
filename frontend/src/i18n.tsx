import {createContext,useContext,useEffect,useState,type ReactNode} from 'react';

export type Language='ko'|'en';
const LanguageContext=createContext<{language:Language;setLanguage:(language:Language)=>void}>({language:'ko',setLanguage:()=>{}});

const EN:Record<string,string>={
 '운영 모니터링':'OPERATIONS','통합 대시보드':'Dashboard','실시간 현황':'Realtime Status','성과 분석':'ANALYTICS','생산실적 분석':'Production Analysis','가동·비가동':'Uptime / Downtime','알람·정비':'Alarms / Maintenance','시스템 관리':'SYSTEM','설비 등록':'Equipment','제품 등록':'Products','데이터 수집':'Data Collection','가입 승인':'Signup Approvals','사용자 리스트':'User List',
 '접속 공장':'Connected Factory','화성 제1공장':'Hwaseong Plant 1','로그아웃':'Logout','실시간 연결 정상':'Realtime connected','마지막 갱신':'Last update','관리자':'Administrator','일반 유저':'General User','마이 페이지':'My Page','모드 변경':'Dark mode','언어':'Language','한국어':'한국어',
 '통합 운영 대시보드':'Integrated Operations Dashboard','화성 제1공장 · 2026년 7월 31일 금요일 · 주간조':'Hwaseong Plant 1 · Friday, July 31, 2026 · Day shift','일간 리포트':'Daily Report','전체 화면':'Fullscreen','대표자 핵심 요약':'Executive Summary','정상 가동':'Running','대기·주의':'Idle / Warning','정지·계획':'Stopped / Planned','고장·알람':'Fault / Alarm','금일 목표수량':'Today Target','금일 생산실적':'Today Output','생산 달성률':'Achievement','현재 시간당 생산':'Current Hourly Output','평균 사이클타임':'Average Cycle Time','금일 비가동':'Today Downtime','추정 생산손실':'Estimated Production Loss','종료 예상실적':'Forecast Output','라인별 생산 현황':'Production by Line','목표 달성 가능성과 이상 상태를 우선 표시':'Prioritizes target feasibility and abnormal conditions','즉시 조치 필요':'Immediate Action','장기 정지 및 통신 이상':'Extended stops and communication faults','상세':'Details','시간당 생산량':'Hourly Output','실적 / 목표 720개':'Actual / Target 720 units','반복 알람 TOP 3':'Recurring Alarms TOP 3','금일 발생 기준':'Based on today’s occurrences','목표 달성':'Target met','목표 미달':'Below target','목표 대비 -340개':'340 units below target','어제 대비 +4.8%':'+4.8% vs. yesterday',
 '공장·라인 실시간 현황':'Factory & Line Realtime Status','설비 상태를 선택하면 상세 운전 정보를 확인할 수 있습니다.':'Select equipment to view detailed operating data.','공통 설비 상태':'Equipment Status','READ 연결 정상':'READ connection normal','연결 정상':'Connected','연결 끊김':'Disconnected','대기':'Idle','계획 정지':'Planned Stop','수동 모드':'Manual Mode','티칭 모드':'Teaching Mode','로봇 고장':'Robot Fault','프레스 고장':'Press Fault','통신 장애':'Communication Fault','안전 정지':'Safety Stop','사용':'Enabled',
 '가동·비가동 분석':'Uptime / Downtime Analysis','정지 원인과 추정 생산손실을 우선 분석합니다.':'Prioritizes stop causes and estimated production loss.','전체 가동률':'Overall Uptime','금일 생산량':'Today Output','금일 알람':'Today Alarms','평균 복구시간':'Average Recovery Time','평균 복구시간 MTTR':'Mean Time to Repair (MTTR)','생산손실 원인':'Production Loss Causes','가장 자주 멈춘 설비':'Most Frequently Stopped Equipment','추정 손실수량 기준':'Based on estimated loss quantity','정지 판정 안내':'Stop Classification Guide','실제 설비 정지와 데이터 통신 중단을 명확히 구분합니다.':'Clearly distinguishes equipment stops from data communication outages.','비계획 정지':'Unplanned Stop','미분류 정지':'Unclassified Stop','장기 대기':'Extended Idle','후공정 대기':'Downstream Wait','소재 대기':'Material Wait','소재 공급 대기':'Material Supply Wait','순간 정지':'Brief Stop','속도 저하':'Speed Loss','금형 교체':'Die Change',
 '목표 대비 생산성과 시간별 추이를 분석합니다.':'Analyzes productivity against targets and hourly trends.','목표 달성률':'Target Achievement','생산 지연수량':'Delayed Quantity','예상 완료시각':'Estimated Completion','시간별 생산 실적':'Hourly Production','점선: 시간당 목표 720개':'Dotted line: hourly target 720 units','라인별 생산 기여도':'Production Contribution by Line','최근 7일 생산량':'Production in Last 7 Days','품질 KPI':'Quality KPI','병목공정 분석':'Bottleneck Analysis','규칙 기반 점검 권고':'Rule-based Inspection Recommendation',
 '알람·정비 분석':'Alarm / Maintenance Analysis','반복 고장과 예방정비 대상을 한눈에 확인합니다.':'View recurring failures and preventive maintenance targets at a glance.','현재 미해제 알람':'Current Open Alarms','반복 알람':'Recurring Alarms','예방정비 예정':'Preventive Maintenance Due','반복 알람 순위':'Recurring Alarm Ranking','최근 시스템 오류':'Recent System Errors',
 '설비 등록 관리':'Equipment Management','공장·라인·설비와 READ 전용 통신정보를 관리합니다.':'Manage factories, lines, equipment, and READ-only communication settings.','신규 설비 등록':'Add Equipment','설비코드 또는 설비명 검색':'Search equipment code or name','설비별 통신 연결':'Equipment Connections','설비정보 수정':'Edit Equipment','제품 등록 관리':'Product Management','제품별·라인별 표준 생산조건을 관리합니다.':'Manage standard production conditions by product and line.','신규 제품 등록':'Add Product','제품코드 또는 제품명 검색':'Search product code or name','제품정보 수정':'Edit Product',
 '데이터 수집 상태':'Data Collection Status','전체 수집 성공률':'Overall Collection Success','평균 응답시간':'Average Response Time','누락 데이터':'Missing Data','데이터 오류':'Data Errors','Java 수집 서비스':'Java Collection Service','정상 실행 중':'Running Normally','기본 수집주기 1초':'Default collection interval: 1 second','마지막 데이터 수신 지연':'Last Data Reception Delay','서버 자원':'Server Resources','수집 기준 설정':'Collection Settings',
 '가입 승인 관리':'Signup Approval Management','시스템 관리자가 가입 신청을 검토하고 실제 부여할 권한을 선택합니다.':'Administrators review signup requests and select the role to grant.','가입 신청을 조회하지 못했습니다.':'Could not load signup requests.','가입 신청을 처리하지 못했습니다.':'Could not process the signup request.','부여할 권한을 선택해 주세요.':'Select a role to grant.','선택한 권한으로 가입 신청을 승인하시겠습니까?':'Approve this signup request with the selected role?','이 가입 신청을 반려하시겠습니까?':'Reject this signup request?','신청자':'Applicant','소속':'Organization','신청 권한':'Requested Role','부여할 권한':'Assigned Role','신청 일시':'Requested At','처리':'Action','승인':'Approve','반려':'Reject','새로고침':'Refresh',
 '가입 승인된 사용자 목록을 확인하고 관리자 또는 일반 유저 권한을 변경합니다.':'View approved users and change their role to Administrator or General User.','사용자 목록을 조회하지 못했습니다.':'Could not load the user list.','사용자 권한을 변경하지 못했습니다.':'Could not change the user role.','가입 사용자':'Registered Users','계정 상태':'Account Status','현재 권한':'Current Role','변경 권한':'New Role','가입 일시':'Joined At','저장':'Save','정상':'Active','잠김/비활성':'Locked / Inactive','권한 없음':'No Role','관리자 전용':'Administrators Only','현재 로그인한 관리자 자신의 권한은 안전을 위해 변경할 수 없습니다.':'For safety, you cannot change your own administrator role.',
 '가입 시 입력한 정보를 확인하고 수정합니다.':'Review and update the information entered during signup.','사용자 아이디':'Username','이름':'Name','소속 공장':'Factory','부서':'Department','직책':'Position','연락처':'Phone','이메일':'Email','개인정보 저장':'Save Profile','내 정보를 불러오는 중입니다.':'Loading your profile…','내 정보를 불러오지 못했습니다.':'Could not load your profile.','내 정보를 저장하지 못했습니다.':'Could not save your profile.','내 정보가 저장되었습니다.':'Your profile has been saved.',
 '로그인':'Sign In','로그인 확인 중...':'Signing in…','로그인 실패':'Sign-in failed','아이디':'Username','비밀번호':'Password','아이디를 입력하세요':'Enter your username','비밀번호를 입력하세요':'Enter your password','로그인 유지':'Keep me signed in','비밀번호 찾기':'Forgot password','가입 승인 요청':'Request Access','회원가입':'Sign Up','비밀번호 확인':'Confirm Password','비밀번호 재입력':'Re-enter password','영문, 숫자 조합':'Letters and numbers','8자 이상':'At least 8 characters','가입 신청 저장 중...':'Submitting request…','가입 신청 저장에 실패했습니다.':'Could not submit the signup request.','READ ONLY':'READ ONLY','설비 제어 기능 없음':'No equipment controls','메뉴 닫기':'Close menu',
 '가동률':'Uptime','개':'units','개/h':'units/h','건':'events','대':'machines','분':'min','초':'sec','실시간':'Realtime','통신 연결':'Connection','수신 지연':'Reception Delay','사용자':'User','월':'Mon','화':'Tue','수':'Wed','목':'Thu','금':'Fri','토':'Sat','일':'Sun','로봇 위치 불일치':'Robot Position Mismatch','서보 부하율 상승':'Increased Servo Load','소재 감지 지연':'Material Detection Delay','제어기 통신 응답 없음':'No Controller Communication Response','이름 입력':'Enter your name','미분류 정지 3건 확인':'Review 3 Unclassified Stops','조회':'Search','조회 기간':'Date Range','생산라인':'Production Line','전체 라인':'All Lines','전체 제품':'All Products','전체 상태':'All Statuses','Excel 일괄등록':'Bulk Excel Import','Excel 양식':'Excel Template','일괄등록':'Bulk Import','신규 설비':'Add Equipment','신규 제품':'Add Product','승인 시 선택한 권한으로 사용자 계정이 생성됩니다.':'A user account will be created with the role selected at approval.','신청 권한과 실제 부여 권한을 비교한 후 승인하세요.':'Compare the requested role with the role to be assigned before approval.','가입 신청을 불러오는 중입니다.':'Loading signup requests…','처리할 가입 신청이 없습니다.':'There are no signup requests to process.','새로운 가입 승인 요청이 접수되면 이 화면에 표시됩니다.':'New signup approval requests will appear here.','모니터링 READ 전용':'READ-only Monitoring','통신 테스트는 데이터 읽기만 수행하며 WRITE 명령은 지원하지 않습니다.':'Communication tests only read data; WRITE commands are not supported.','사용 중':'In Use','중지':'Disabled','관리':'Manage','설비':'Equipment','라인':'Line','유형 / 제조사':'Type / Manufacturer','통신방식':'Protocol','IP 주소':'IP Address','현재상태':'Current Status','설비코드':'Equipment Code','설비명':'Equipment Name','설비유형':'Equipment Type','제조사':'Manufacturer','모델명':'Model','통신정보':'Communication Settings','포트번호':'Port','수집주기(ms)':'Collection Interval (ms)','READ 통신 테스트':'Test READ Connection','제품코드 / 제품명':'Product Code / Name','차종·고객사':'Vehicle / Customer','적용라인':'Assigned Line','표준 CT':'Standard CT','시간당 생산':'Hourly Output','기본 목표':'Default Target','금형번호':'Die Number','제품코드':'Product Code','제품명':'Product Name','차종':'Vehicle','고객사':'Customer','표준 사이클타임(초)':'Standard Cycle Time (sec)','기본 목표수량':'Default Target Quantity','실적':'Actual','달성률':'Achievement','표준/실제 CT':'Standard / Actual CT','품질 데이터 미연동':'Quality Data Not Connected','불량수량, PPM, OEE를 임의로 계산하지 않습니다.':'Defect quantity, PPM, and OEE are not estimated.','현재 상태 지속시간':'Current Status Duration','실시간 운전 정보':'Realtime Operating Data','현재 제품':'Current Product','생산 실적':'Production Output','최근 사이클타임':'Latest Cycle Time','로봇 부하율':'Robot Load','서보 상태':'Servo Status','현재 프로그램':'Current Program','통신 상태':'Communication Status','최근 1시간 사이클':'Last Hour Cycle','조회 전용 화면입니다. 설비 제어 기능은 제공되지 않습니다.':'This is a read-only screen. Equipment controls are not provided.','로그인 상태를 확인하는 중입니다.':'Checking sign-in status…','동적 수집 태그':'Dynamic Collected Tags','데이터 종류를 고정하지 않고 수집된 키와 값을 그대로 표시합니다.':'Displays collected keys and values without fixing the data types.','수집된 동적 태그가 없습니다.':'No dynamic tags have been collected.','비밀번호 변경':'Change Password','현재 비밀번호를 확인한 후 새 비밀번호로 변경합니다.':'Verify your current password, then set a new password.','현재 비밀번호':'Current Password','새 비밀번호':'New Password','새 비밀번호 확인':'Confirm New Password','비밀번호가 변경되었습니다.':'Your password has been changed.','비밀번호를 변경하지 못했습니다.':'Could not change the password.','로그인으로 돌아가기':'Back to Sign In','수정':'Edit','신규 등록':'Add New','등록':'Add','검색':'Search','닫기':'Close','취소':'Cancel','확인':'Confirm','데이터 조회 실패':'Failed to load data','서버 응답 형식이 올바르지 않습니다.':'Invalid server response format.','API 요청에 실패했습니다.':'API request failed.'
};

const textOriginals=new WeakMap<Text,string>();
const attributeOriginals=new WeakMap<Element,Map<string,string>>();
export function translateText(value:string,language:Language)
{
 if(language==='ko')return value;
 const trimmed=value.trim();
 if(EN[trimmed])return value.replace(trimmed,EN[trimmed]);
 const templates:[RegExp,(match:RegExpMatchArray)=>string][]=[
  [/^승인 대기 (\d+)건$/m,match=>`${match[1]} Pending Approvals`],
  [/^가입 사용자 (\d+)명$/m,match=>`${match[1]} Registered Users`],
  [/^총 (\d+)대$/m,match=>`${match[1]} Equipment`],
  [/^총 (\d+)개 제품$/m,match=>`${match[1]} Products`],
  [/^(\d+)라인$/m,match=>`Line ${match[1]}`]
 ];
 for(const [pattern,replace] of templates){const match=trimmed.match(pattern);if(match)return value.replace(trimmed,replace(match))}
 return value;
}
function translate(root:Node,language:Language)
{
 const nodes:Node[]=[root];
 if(root.nodeType!==Node.TEXT_NODE){const walker=document.createTreeWalker(root,NodeFilter.SHOW_TEXT);let current:Node|null;while((current=walker.nextNode()))nodes.push(current)}
 for(const node of nodes)
 {
  if(node.nodeType!==Node.TEXT_NODE)continue;
  const text=node as Text;if(!textOriginals.has(text))textOriginals.set(text,text.data);
  const next=translateText(textOriginals.get(text)??text.data,language);if(text.data!==next)text.data=next;
 }
 const elements:Element[]=[];
 if(root instanceof Element)elements.push(root,...Array.from(root.querySelectorAll('*')));
 for(const element of elements)
 {
  let originals=attributeOriginals.get(element);if(!originals){originals=new Map();attributeOriginals.set(element,originals)}
  for(const name of ['placeholder','title','aria-label']){const value=element.getAttribute(name);if(value!==null&&!originals.has(name))originals.set(name,value);const original=originals.get(name);if(original!==undefined)element.setAttribute(name,translateText(original,language))}
 }
}

export function LanguageProvider({children}:{children:ReactNode})
{
 const [language,setLanguageState]=useState<Language>(()=>localStorage.getItem('prima-language')==='en'?'en':'ko');
 function setLanguage(next:Language){localStorage.setItem('prima-language',next);document.documentElement.lang=next;setLanguageState(next)}
 useEffect(()=>{document.documentElement.lang=language;translate(document.body,language);const observer=new MutationObserver(records=>records.forEach(record=>{if(record.type==='characterData')translate(record.target,language);record.addedNodes.forEach(node=>translate(node,language))}));observer.observe(document.body,{childList:true,characterData:true,subtree:true});return()=>observer.disconnect()},[language]);
 return <LanguageContext.Provider value={{language,setLanguage}}>{children}</LanguageContext.Provider>
}
export const useLanguage=()=>useContext(LanguageContext);
