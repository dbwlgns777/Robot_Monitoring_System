import {request} from './apiClient';
import type {MonitoringProvider} from './apiProvider';
import type {Alarm,CollectionHealth,Equipment,HourlyProduction,Product,ProductionLine} from '../types/domain';

const status=(value:string):Equipment['status']=>({WAITING:'WAITING',ROBOT_FAULT:'ROBOT_FAULT',PRESS_FAULT:'PRESS_FAULT',COMMUNICATION_LOSS:'COMMUNICATION_LOSS'}[value]??value) as Equipment['status'];
const dynamicTags=(value:unknown):Record<string,unknown>=>
 value!==null&&typeof value==='object'&&!Array.isArray(value)?value as Record<string,unknown>:{};

export function mapEquipmentRows(rows:Record<string,unknown>[]):Equipment[]
{
 return rows.map(row=>({
  ...row,id:String(row.id),lineId:`line-${row.lineId}`,status:status(String(row.status)),
  statusDuration:'실시간',product:String(row.product??'-'),target:Number(row.target??0),
  actual:Number(row.actual??0),cycleTime:Number(row.cycleTime??0),
  lastReceived:String(row.lastReceived??''),responseMs:Number(row.responseMs??0),
  enabled:Boolean(row.enabled),dynamicTags:dynamicTags(row.dynamicTags)
 }) as Equipment);
}

export const restApiProvider:MonitoringProvider={
 async getEquipment(signal){return mapEquipmentRows(await request<Record<string,unknown>[]>('/realtime/equipment',{},signal))},
 async getProducts(signal){const rows=await request<Record<string,unknown>[]>('/products',{},signal);return rows.map(r=>({id:String(r.id),code:String(r.productCode),name:String(r.productName),vehicle:String(r.vehicleModel??''),customer:String(r.customer??''),line:'-',cycleTime:0,hourlyRate:0,target:0,mold:'-',enabled:Boolean(r.isActive)}) as Product)},
 async getDashboard(signal){const [equipment,hourRows,alarmRows]=await Promise.all([this.getEquipment(signal),request<Record<string,unknown>[]>('/analytics/production',{},signal),request<Record<string,unknown>[]>('/analytics/alarms',{},signal)]);const grouped=[1,2,3].map(n=>{const eq=equipment.filter(e=>e.lineId===`line-${n}`),first=eq[0];return{id:`line-${n}`,name:`${n}라인`,product:first?.product??'-',status:first?.status??'UNCLASSIFIED_STOP',target:first?.target??0,actual:first?.actual??0,expected:first?.actual??0,uptime:0,equipmentIds:eq.map(e=>e.id)} as ProductionLine});const target=grouped.reduce((a,x)=>a+x.target,0),actual=grouped.reduce((a,x)=>a+x.actual,0);const kpi={running:equipment.filter(e=>e.status==='RUNNING').length,idle:equipment.filter(e=>e.status==='WAITING').length,stopped:equipment.filter(e=>['PLANNED_STOP','COMMUNICATION_LOSS'].includes(e.status)).length,alarms:equipment.filter(e=>e.status.endsWith('FAULT')).length,target,actual,achievement:target?actual/target*100:0,hourly:0,avgCycle:equipment.reduce((a,e)=>a+e.cycleTime,0)/(equipment.length||1),downtime:0,estimatedLoss:0,forecast:actual,yesterdayDelta:0,weeklyUptimeDelta:0};const hourly=hourRows.map(x=>({hour:String(x.summaryHour??''),target:Number(x.targetQuantity??0),actual:Number(x.productionQuantity??0)} as HourlyProduction));const alarms=alarmRows.map(x=>({id:String(x.id),equipmentCode:String(x.equipmentId),line:'',code:String(x.alarmCode),message:String(x.alarmMessage),occurredAt:String(x.occurredAt),duration:Number(x.durationSeconds??0)/60,count:Number(x.occurrenceCount??1),severity:String(x.severity)==='CRITICAL'?'critical':'warning',cleared:Boolean(x.clearedAt)} as Alarm));return{kpi,lines:grouped,hourly,alarms};},
 async getCollectionHealth(signal){const rows=await request<Record<string,unknown>[]>('/system/collection-health',{},signal);const collection=rows.map(r=>({equipmentCode:String(r.equipmentId),state:String(r.dataQuality)==='BAD'?'COMMUNICATION_LOSS':'CONNECTED',lastReceived:String(r.lastReceivedAt),successRate:Number(r.successRate),failures:Number(r.consecutiveFailures),responseMs:Number(r.averageResponseMs),missing:Number(r.missingCount),reconnects:Number(r.reconnectCount)} as CollectionHealth));return{collection,systemHealth:{collector:true,api:true,websocket:true,dbUsage:0,diskFree:0,cpu:0,memory:0,lastBackup:'',backupSuccess:true}};}
};
