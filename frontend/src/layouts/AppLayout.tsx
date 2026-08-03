import {useEffect,useRef,useState} from 'react';
import {authApi,type AuthenticatedUser} from '../api/authApi';
import {NavLink,Outlet,useNavigate} from 'react-router-dom';
import {Activity,AlarmClock,BarChart3,Box,ChevronDown,ChevronLeft,ChevronUp,Database,Factory,Gauge,Languages,LayoutDashboard,LogOut,Menu,Moon,Settings2,ShieldCheck,Sun,UserCheck,UserRound,Users,X} from 'lucide-react';
import {useLanguage} from '../i18n';

const groups=[{label:'운영 모니터링',items:[['/dashboard','통합 대시보드',LayoutDashboard],['/realtime','실시간 현황',Factory]]},{label:'성과 분석',items:[['/production','생산실적 분석',BarChart3],['/downtime','가동·비가동',Activity],['/alarms','알람·정비',AlarmClock]]},{label:'시스템 관리',items:[['/equipment','설비 등록',Settings2],['/products','제품 등록',Box],['/collection','데이터 수집',Database],['/approvals','가입 승인',UserCheck],['/users','사용자 리스트',Users]]}] as const;

function readUser():AuthenticatedUser
{
 try{return JSON.parse(localStorage.getItem('prima-user')??sessionStorage.getItem('prima-user')??'{}') as AuthenticatedUser}catch{return {id:0,username:'',name:'사용자',roles:[]}}
}

export function AppLayout()
{
 const [open,setOpen]=useState(false),[accountOpen,setAccountOpen]=useState(false),[languageOpen,setLanguageOpen]=useState(false),[user,setUser]=useState(readUser),[theme,setTheme]=useState(()=>localStorage.getItem('prima-theme')??'light');
 const accountRef=useRef<HTMLDivElement>(null),nav=useNavigate(),{language,setLanguage}=useLanguage();
 const admin=user.roles?.includes('ROLE_ADMIN')??false;
 useEffect(()=>{document.documentElement.dataset.theme=theme;localStorage.setItem('prima-theme',theme)},[theme]);
 useEffect(()=>{const refresh=()=>setUser(readUser());window.addEventListener('prima-user-updated',refresh);return()=>window.removeEventListener('prima-user-updated',refresh)},[]);
 useEffect(()=>{const close=(event:MouseEvent)=>{if(!accountRef.current?.contains(event.target as Node)){setAccountOpen(false);setLanguageOpen(false)}};document.addEventListener('mousedown',close);return()=>document.removeEventListener('mousedown',close)},[]);
 function logout(){localStorage.removeItem('prima-auth');localStorage.removeItem('prima-user');localStorage.removeItem('prima-remember');sessionStorage.removeItem('prima-auth');sessionStorage.removeItem('prima-user');void authApi.logout().finally(()=>nav('/login'))}
 return <div className="app-shell"><aside className={open?'sidebar open':'sidebar'}><div className="brand"><div className="brand-mark"><Gauge/></div><div><b>PRIMA</b><span>FACTORY 360</span></div><button className="mobile-close" onClick={()=>setOpen(false)}><X/></button></div><div className="plant-chip"><i/><div><span>접속 공장</span><b>화성 제1공장</b></div><ChevronLeft/></div><nav>{groups.map(group=><div className="nav-group" key={group.label}><small>{group.label}</small>{group.items.filter(([to])=>!['/approvals','/users'].includes(to)||admin).map(([to,label,Icon])=><NavLink key={to} to={to} onClick={()=>setOpen(false)}><Icon/>{label}</NavLink>)}</div>)}</nav><div className="readonly"><ShieldCheck/><div><b>READ ONLY</b><span>설비 제어 기능 없음</span></div></div><button className="logout" onClick={logout}><LogOut/>로그아웃</button></aside>{open&&<button className="scrim" aria-label="메뉴 닫기" onClick={()=>setOpen(false)}/>}<div className="main-wrap"><div className="topbar"><button className="menu-btn" onClick={()=>setOpen(true)}><Menu/></button><div className="live"><i/> 실시간 연결 정상 <span>마지막 갱신 09:42:30</span></div><div className="account-wrap" ref={accountRef}><button className="user" onClick={()=>setAccountOpen(value=>!value)}><div>{user.name?.trim().charAt(0)||'U'}</div><span><b>{user.name||user.username}</b><small>{admin?'관리자':'일반 유저'}</small></span>{accountOpen?<ChevronUp/>:<ChevronDown/>}</button>{accountOpen&&<div className="account-menu"><NavLink to="/profile" onClick={()=>setAccountOpen(false)}><UserRound/>마이 페이지</NavLink><div className="account-action"><span><Moon/>모드 변경</span><button className={theme==='dark'?'theme-toggle active':'theme-toggle'} aria-label="모드 변경" aria-pressed={theme==='dark'} onClick={()=>setTheme(value=>value==='dark'?'light':'dark')}><i/></button></div><button className="account-action language-action" onClick={()=>setLanguageOpen(value=>!value)}><span><Languages/>언어</span><small>{language==='ko'?'한국어':'English'}</small></button>{languageOpen&&<div className="language-popup"><button className={language==='ko'?'selected':''} onClick={()=>{setLanguage('ko');setLanguageOpen(false)}}>한국어</button><button className={language==='en'?'selected':''} onClick={()=>{setLanguage('en');setLanguageOpen(false)}}>English</button></div>}<button className="menu-logout" onClick={logout}><LogOut/>로그아웃</button></div>}</div></div><main><Outlet/></main></div></div>
}
