interface Envelope<T>{success:boolean;data:T;message?:string}
export class ApiError extends Error{constructor(message:string,public status:number){super(message)}}
const base=import.meta.env.VITE_API_BASE_URL??'/api/v1';
export async function request<T>(path:string,init:RequestInit={},signal?:AbortSignal):Promise<T>{const response=await fetch(`${base}${path}`,{...init,signal,credentials:'include',headers:{'Content-Type':'application/json',...init.headers}});let body:Envelope<T>|undefined;try{body=await response.json() as Envelope<T>}catch{throw new ApiError('서버 응답 형식이 올바르지 않습니다.',response.status)}if(!response.ok||!body.success)throw new ApiError(body.message??'API 요청에 실패했습니다.',response.status);return body.data;}
