export type EquipmentStatus = 'RUNNING'|'IDLE'|'ALARM'|'PLANNED_STOP'|'DISCONNECTED'|'MANUAL';
export type EquipmentType = 'ROBOT'|'PRESS';
export interface Equipment { id:string; code:string; name:string; lineId:string; type:EquipmentType; status:EquipmentStatus; statusDuration:string; product:string; target:number; actual:number; cycleTime:number; alarm?:string; lastReceived:string; responseMs:number; enabled:boolean; manufacturer:string; model:string; ip:string; protocol:string; }
export interface ProductionLine { id:string; name:string; product:string; status:EquipmentStatus; target:number; actual:number; expected:number; uptime:number; equipmentIds:string[]; }
export interface Kpi { running:number; idle:number; stopped:number; alarms:number; target:number; actual:number; achievement:number; hourly:number; avgCycle:number; downtime:number; estimatedLoss:number; forecast:number; yesterdayDelta:number; weeklyUptimeDelta:number; }
export interface HourlyProduction { hour:string; target:number; actual:number; }
export interface Alarm { id:string; equipmentCode:string; line:string; code:string; message:string; occurredAt:string; duration:number; count:number; severity:'critical'|'warning'; cleared:boolean; }
export interface DowntimeReason { reason:string; minutes:number; loss:number; count:number; color:string; }
export interface Product { id:string; code:string; name:string; vehicle:string; customer:string; line:string; cycleTime:number; hourlyRate:number; target:number; mold:string; enabled:boolean; }
export interface CollectionHealth { equipmentCode:string; state:'CONNECTED'|'DELAYED'|'DISCONNECTED'; lastReceived:string; successRate:number; failures:number; responseMs:number; missing:number; reconnects:number; }
export interface RegistrationForm { username:string; name:string; password:string; factory:string; department:string; position:string; phone:string; email:string; requestedRole:string; }
export interface SystemHealth { collector:boolean; api:boolean; websocket:boolean; dbUsage:number; diskFree:number; cpu:number; memory:number; lastBackup:string; backupSuccess:boolean; }
