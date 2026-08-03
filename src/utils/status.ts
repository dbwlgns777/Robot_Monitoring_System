import type { EquipmentStatus } from '../types/domain';
export const statusMeta: Record<EquipmentStatus,{label:string;color:string;icon:string}> = {
 RUNNING:{label:'정상 가동',color:'var(--green)',icon:'●'}, IDLE:{label:'대기·주의',color:'var(--yellow)',icon:'▲'},
 ALARM:{label:'고장·알람',color:'var(--red)',icon:'!'}, PLANNED_STOP:{label:'계획 정지',color:'var(--gray)',icon:'■'},
 DISCONNECTED:{label:'통신 장애',color:'var(--black)',icon:'×'}, MANUAL:{label:'수동·티칭',color:'var(--blue)',icon:'◆'} };
