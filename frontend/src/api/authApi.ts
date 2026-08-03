import type {RegistrationForm,RegistrationRequest} from '../types/domain';
import {request} from '../services/apiClient';
export interface AuthenticatedUser{id:number;username:string;name:string;roles:string[]}
export const authApi={
 login:(username:string,password:string)=>request<AuthenticatedUser>('/auth/login',{method:'POST',body:JSON.stringify({username,password})}),
 register:(form:RegistrationForm)=>request<{status:'PENDING'}>('/auth/signup',{method:'POST',body:JSON.stringify(form)}),
 logout:()=>request('/auth/logout',{method:'POST'}),
 pendingRegistrations:()=>request<RegistrationRequest[]>('/admin/registration-requests'),
 approveRegistration:(id:number)=>request<{id:number;status:'APPROVED';userId:number}>(`/admin/registration-requests/${id}/approve`,{method:'POST'}),
 rejectRegistration:(id:number)=>request<{id:number;status:'REJECTED'}>(`/admin/registration-requests/${id}/reject`,{method:'POST'})
};
