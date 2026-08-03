import type {AssignableRole,ManagedUser,RegistrationForm,RegistrationRequest} from '../types/domain';
import {request} from '../services/apiClient';
export interface AuthenticatedUser{id:number;username:string;name:string;roles:string[]}
interface CsrfData{token:string;headerName:string}
async function adminMutation<T>(path:string,method:'POST'|'PUT',body?:unknown):Promise<T>{const csrf=await request<CsrfData>('/auth/csrf');return request<T>(path,{method,headers:{[csrf.headerName]:csrf.token},body:body===undefined?undefined:JSON.stringify(body)})}
export const authApi={
 login:(username:string,password:string)=>request<AuthenticatedUser>('/auth/login',{method:'POST',body:JSON.stringify({username,password})}),
 register:(form:RegistrationForm)=>request<{status:'PENDING'}>('/auth/signup',{method:'POST',body:JSON.stringify(form)}),
 logout:()=>request('/auth/logout',{method:'POST'}),
 pendingRegistrations:()=>request<RegistrationRequest[]>('/admin/registration-requests'),
 assignableRoles:()=>request<AssignableRole[]>('/admin/users/roles'),
 users:()=>request<ManagedUser[]>('/admin/users'),
 approveRegistration:(id:number,roleCode:string)=>adminMutation<{id:number;status:'APPROVED';userId:number;roleCode:string}>(`/admin/registration-requests/${id}/approve`,'POST',{roleCode}),
 rejectRegistration:(id:number)=>adminMutation<{id:number;status:'REJECTED'}>(`/admin/registration-requests/${id}/reject`,'POST'),
 updateUserRole:(id:number,roleCode:string)=>adminMutation<{userId:number;roleCode:string}>(`/admin/users/${id}/role`,'PUT',{roleCode})
};
