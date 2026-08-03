import type { RegistrationForm } from '../types/domain';
const sleep=()=>new Promise(r=>setTimeout(r,350));
export const authApi={async login(username:string,password:string){await sleep();if(!username||!password)throw new Error('아이디와 비밀번호를 입력하세요.');return {name:'김대표',role:'대표자'};},async register(_form:RegistrationForm){await sleep();return {status:'PENDING' as const};}};
