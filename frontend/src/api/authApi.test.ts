import {afterEach,describe,expect,it,vi} from 'vitest';
import {authApi} from './authApi';

afterEach(()=>vi.unstubAllGlobals());

describe('administrator registration approval API',()=>{
 it('calls the protected approval endpoint with the session cookie',async()=>{
  const fetchMock=vi.fn().mockResolvedValue({ok:true,status:200,json:async()=>({success:true,data:{id:7,status:'APPROVED',userId:12}})});
  vi.stubGlobal('fetch',fetchMock);

  await authApi.approveRegistration(7,'ROLE_MANAGER');

  expect(fetchMock).toHaveBeenCalledWith('/api/v1/admin/registration-requests/7/approve',expect.objectContaining({method:'POST',credentials:'include',body:JSON.stringify({roleCode:'ROLE_MANAGER'})}));
 });
});
