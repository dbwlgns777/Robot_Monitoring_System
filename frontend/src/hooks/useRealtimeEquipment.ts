import {useEffect,useState} from 'react';
import type {Equipment} from '../types/domain';
import {monitoringProvider} from '../services/provider';
import {StompRealtimeClient} from '../services/stompRealtimeClient';
import {mapEquipmentRows} from '../services/restApiProvider';

const REALTIME_INTERVAL_MS=1000;

export function useRealtimeEquipment()
{
 const [data,setData]=useState<Equipment[]>();
 const [error,setError]=useState('');
 useEffect(()=>
 {
  const controller=new AbortController();
  let active=true;
  const load=()=>monitoringProvider.getEquipment(controller.signal)
   .then(rows=>{setData(rows);setError('');})
   .catch(exception=>
   {
    if(!controller.signal.aborted)
     setError(exception instanceof Error?exception.message:'데이터 조회 실패');
   });
  void load();
  if(import.meta.env.VITE_DATA_SOURCE!=='mock')
  {
   let polling:number|undefined=window.setInterval(load,REALTIME_INTERVAL_MS);
   const websocket=new StompRealtimeClient<Record<string,unknown>[]>();
   const disconnect=websocket.connect(
    '/topic/equipment-status',
    rows=>{setData(mapEquipmentRows(rows));setError('');},
    connected=>
    {
     if(!active)return;
     if(connected&&polling!==undefined)
     {
      clearInterval(polling);
      polling=undefined;
     }
     else if(!connected&&polling===undefined)
     {
      polling=window.setInterval(load,REALTIME_INTERVAL_MS);
     }
    });
   return()=>
   {
    active=false;
    controller.abort();
    disconnect();
    if(polling!==undefined)clearInterval(polling);
   };
  }
  const polling=window.setInterval(load,REALTIME_INTERVAL_MS);
  return()=>
  {
   active=false;
   controller.abort();
   clearInterval(polling);
  };
 },[]);
 return{data,error,loading:!data&&!error};
}
