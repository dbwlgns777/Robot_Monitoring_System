import {afterEach,describe,expect,it,vi} from 'vitest';
import {authApi} from './authApi';

afterEach(()=>vi.unstubAllGlobals());

describe('administrator mutations',()=>{
 it('loads a CSRF token before approving a registration',async()=>{
  const fetchMock=vi.fn()
   .mockResolvedValueOnce({ok:true,status:200,json:async()=>({success:true,data:{token:'csrf-token',headerName:'X-CSRF-TOKEN'}})})
   .mockResolvedValueOnce({ok:true,status:200,json:async()=>({success:true,data:{id:7,status:'APPROVED',userId:12,roleCode:'ROLE_USER'}})});
  vi.stubGlobal('fetch',fetchMock);

  await authApi.approveRegistration(7,'ROLE_USER');

  expect(fetchMock).toHaveBeenNthCalledWith(2,'/api/v1/admin/registration-requests/7/approve',expect.objectContaining({method:'POST',credentials:'include',headers:expect.objectContaining({'X-CSRF-TOKEN':'csrf-token'}),body:JSON.stringify({roleCode:'ROLE_USER'})}));
 });
});
