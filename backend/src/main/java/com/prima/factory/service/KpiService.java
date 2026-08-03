package com.prima.factory.service;
import java.util.*; import org.springframework.stereotype.Service;
@Service public class KpiService {
 public double achievement(long actual,long target){return target<=0?0:(double)actual/target*100;}
 public double hourly(long count,double runningHours){return runningHours<=0?0:count/runningHours;}
 public double uptime(long running,long planned){return planned<=0?0:(double)running/planned*100;}
 public double loss(long minutes,double hourlyRate){return minutes/60d*hourlyRate;}
 public Double mtbf(long runningMinutes,long failures){return failures<=0?null:(double)runningMinutes/failures;}
 public Double mttr(long recoveryMinutes,long recoveries){return recoveries<=0?null:(double)recoveryMinutes/recoveries;}
 public Map<String,Object> quality(boolean linked,long defects,long total,double availability,double performance){if(!linked)return Map.of("qualityDataLinked",false,"ppm",Optional.empty(),"oee",Optional.empty()); double quality=total<=0?0:1-(double)defects/total; return Map.of("qualityDataLinked",true,"ppm",total<=0?0:(double)defects/total*1_000_000,"oee",availability*performance*quality*100);}
}
