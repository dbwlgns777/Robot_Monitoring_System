import type {Alarm,CollectionHealth,Equipment,HourlyProduction,Kpi,Product,ProductionLine,SystemHealth} from '../types/domain';
export interface DashboardData{kpi:Kpi;lines:ProductionLine[];hourly:HourlyProduction[];alarms:Alarm[]}
export interface MonitoringProvider{getDashboard(signal?:AbortSignal):Promise<DashboardData>;getEquipment(signal?:AbortSignal):Promise<Equipment[]>;getProducts(signal?:AbortSignal):Promise<Product[]>;getCollectionHealth(signal?:AbortSignal):Promise<{collection:CollectionHealth[];systemHealth:SystemHealth}>;}
