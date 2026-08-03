import { alarms,collection,equipment,hourly,kpi,lines,products,systemHealth } from '../data/mockData';
const wait=<T>(value:T)=>new Promise<T>(resolve=>setTimeout(()=>resolve(structuredClone(value)),180));
export const monitoringApi={getDashboard:()=>wait({kpi,lines,hourly,alarms}),getEquipment:()=>wait(equipment),getProducts:()=>wait(products),getCollectionHealth:()=>wait({collection,systemHealth})};
