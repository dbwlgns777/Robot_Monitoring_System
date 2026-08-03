export const achievementRate=(actual:number,target:number)=>target?actual/target*100:0;
export const uptimeRate=(runningMinutes:number,plannedMinutes:number)=>plannedMinutes?runningMinutes/plannedMinutes*100:0;
export const estimatedLoss=(unplannedMinutes:number,hourlyRate:number)=>unplannedMinutes/60*hourlyRate;
export const formatNumber=(n:number)=>new Intl.NumberFormat('ko-KR',{maximumFractionDigits:1}).format(n);
