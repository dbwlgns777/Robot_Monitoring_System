import {useEffect,useState,type FormEvent} from 'react';
import {Save,UserRound} from 'lucide-react';
import {authApi} from '../api/authApi';
import {Card,PageHeader} from '../components/Common';
import type {FactoryOption,UserProfile} from '../types/domain';

export function ProfilePage()
{
 const [profile,setProfile]=useState<UserProfile>();
 const [factories,setFactories]=useState<FactoryOption[]>([]);
 const [error,setError]=useState('');
 const [saved,setSaved]=useState(false);
 const [passwordError,setPasswordError]=useState('');
 const [passwordSaved,setPasswordSaved]=useState(false);
 useEffect(()=>{void Promise.all([authApi.profile(),authApi.factories()]).then(([user,factoryRows])=>{setProfile(user);setFactories(factoryRows)}).catch(e=>setError(e instanceof Error?e.message:'내 정보를 불러오지 못했습니다.'))},[]);
 async function submit(event:FormEvent<HTMLFormElement>)
 {
  event.preventDefault();
  const data=new FormData(event.currentTarget);
  setError('');setSaved(false);
  try
  {
   const updated=await authApi.updateProfile({name:String(data.get('name')),factoryId:Number(data.get('factoryId'))||undefined,department:String(data.get('department')),position:String(data.get('position')),phone:String(data.get('phone')),email:String(data.get('email'))});
   setProfile(updated);
   const storage=localStorage.getItem('prima-user')?localStorage:sessionStorage;
   const stored=JSON.parse(storage.getItem('prima-user')??'{}');
   storage.setItem('prima-user',JSON.stringify({...stored,name:updated.name}));
   window.dispatchEvent(new Event('prima-user-updated'));
   setSaved(true);
  }
  catch(e){setError(e instanceof Error?e.message:'내 정보를 저장하지 못했습니다.')}
 }
 async function changePassword(event:FormEvent<HTMLFormElement>)
 {
  event.preventDefault();
  const form=event.currentTarget,data=new FormData(form);
  setPasswordError('');setPasswordSaved(false);
  try
  {
   await authApi.changePassword(String(data.get('currentPassword')),String(data.get('newPassword')),String(data.get('newPasswordConfirm')));
   form.reset();setPasswordSaved(true);
  }
  catch(e){setPasswordError(e instanceof Error?e.message:'비밀번호를 변경하지 못했습니다.')}
 }
 if(!profile)return <div className="state-box">{error||'내 정보를 불러오는 중입니다.'}</div>;
 return <><PageHeader title="마이 페이지" description="가입 시 입력한 정보를 확인하고 수정합니다."/>{error&&<div className="form-error">{error}</div>}{saved&&<div className="form-success">내 정보가 저장되었습니다.</div>}<Card><form className="profile-form" onSubmit={submit}><div className="profile-avatar"><UserRound/><div><b>{profile.name}</b><span>{profile.username}</span></div></div><div className="profile-fields"><label><span>사용자 아이디</span><input value={profile.username} disabled/></label><label><span>이름</span><input name="name" required defaultValue={profile.name}/></label><label><span>소속 공장</span><select name="factoryId" defaultValue={profile.factoryId}>{factories.map(factory=><option key={factory.id} value={factory.id}>{factory.factoryName}</option>)}</select></label><label><span>부서</span><input name="department" defaultValue={profile.department}/></label><label><span>직책</span><input name="position" defaultValue={profile.position}/></label><label><span>연락처</span><input name="phone" defaultValue={profile.phone}/></label><label className="profile-field-wide"><span>이메일</span><input name="email" type="email" required defaultValue={profile.email}/></label></div><div className="profile-actions"><button className="primary-small profile-save"><Save/>개인정보 저장</button></div></form></Card><Card title="비밀번호 변경" caption="현재 비밀번호를 확인한 후 새 비밀번호로 변경합니다.">{passwordError&&<div className="form-error">{passwordError}</div>}{passwordSaved&&<div className="form-success">비밀번호가 변경되었습니다.</div>}<form className="password-form" onSubmit={changePassword}><label><span>현재 비밀번호</span><input name="currentPassword" type="password" required autoComplete="current-password"/></label><label><span>새 비밀번호</span><input name="newPassword" type="password" required minLength={8} autoComplete="new-password"/></label><label><span>새 비밀번호 확인</span><input name="newPasswordConfirm" type="password" required minLength={8} autoComplete="new-password"/></label><button className="primary-small"><Save/>비밀번호 변경</button></form></Card></>
}
