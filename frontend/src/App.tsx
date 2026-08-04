import {useEffect,useState} from 'react';
import {Navigate,Outlet,Route,Routes,useLocation} from 'react-router-dom';
import {authApi,type AuthenticatedUser} from './api/authApi';
import {AppLayout} from './layouts/AppLayout';
import {AlarmPage,DowntimePage,ProductionPage} from './pages/AnalysisPages';
import {LoginPage,SignupPage} from './pages/AuthPages';
import {DashboardPage} from './pages/DashboardPage';
import {CollectionPage,EquipmentPage,ProductPage} from './pages/ManagementPages';
import {RegistrationApprovalPage} from './pages/RegistrationApprovalPage';
import {UserAdministrationPage} from './pages/UserAdministrationPage';
import {RealtimePage} from './pages/RealtimePage';
import {ProfilePage} from './pages/ProfilePage';

function storeSessionUser(user:AuthenticatedUser)
{
 const storage=localStorage.getItem('prima-remember')==='true'?localStorage:sessionStorage;
 storage.setItem('prima-auth','true');storage.setItem('prima-user',JSON.stringify(user));
}
function clearSessionUser()
{
 localStorage.removeItem('prima-auth');localStorage.removeItem('prima-user');
 sessionStorage.removeItem('prima-auth');sessionStorage.removeItem('prima-user');
}
function RequireSession()
{
 const location=useLocation();
 const [state,setState]=useState<'checking'|'valid'|'invalid'>('checking');
 useEffect(()=>{let active=true;void authApi.me().then(user=>{if(active){storeSessionUser(user);setState('valid')}}).catch(()=>{if(active){clearSessionUser();setState('invalid')}});return()=>{active=false}},[]);
 if(state==='checking')return <div className="state-box"><span className="spinner"/>로그인 상태를 확인하는 중입니다.</div>;
 if(state==='invalid')return <Navigate to="/login" replace state={{from:location.pathname}}/>;
 return <Outlet/>;
}
function HomeRedirect(){return <Navigate to="/dashboard" replace/>}

export default function App()
{
 return <Routes><Route path="/login" element={<LoginPage/>}/><Route path="/signup" element={<SignupPage/>}/><Route element={<RequireSession/>}><Route element={<AppLayout/>}><Route path="/dashboard" element={<DashboardPage/>}/><Route path="/realtime" element={<RealtimePage/>}/><Route path="/production" element={<ProductionPage/>}/><Route path="/downtime" element={<DowntimePage/>}/><Route path="/alarms" element={<AlarmPage/>}/><Route path="/equipment" element={<EquipmentPage/>}/><Route path="/products" element={<ProductPage/>}/><Route path="/collection" element={<CollectionPage/>}/><Route path="/approvals" element={<RegistrationApprovalPage/>}/><Route path="/users" element={<UserAdministrationPage/>}/><Route path="/profile" element={<ProfilePage/>}/></Route></Route><Route path="/" element={<HomeRedirect/>}/><Route path="*" element={<HomeRedirect/>}/></Routes>
}
