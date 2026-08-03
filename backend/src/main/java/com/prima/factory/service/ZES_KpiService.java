package com.prima.factory.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ZES_KpiService
{
    public double ZES_achievement(long ZES_actual, long ZES_target)
    {
        return ZES_target <= 0 ? 0 : (double) ZES_actual / ZES_target * 100;
    }

    public double ZES_hourly(long ZES_count, double ZES_runningHours)
    {
        return ZES_runningHours <= 0 ? 0 : ZES_count / ZES_runningHours;
    }

    public double ZES_uptime(long ZES_running, long ZES_planned)
    {
        return ZES_planned <= 0 ? 0 : (double) ZES_running / ZES_planned * 100;
    }

    public double ZES_loss(long ZES_minutes, double ZES_hourlyRate)
    {
        return ZES_minutes / 60d * ZES_hourlyRate;
    }

    public Double ZES_mtbf(long ZES_runningMinutes, long ZES_failures)
    {
        return ZES_failures <= 0 ? null : (double) ZES_runningMinutes / ZES_failures;
    }

    public Double ZES_mttr(long ZES_recoveryMinutes, long ZES_recoveries)
    {
        return ZES_recoveries <= 0 ? null : (double) ZES_recoveryMinutes / ZES_recoveries;
    }

    public Map<String, Object> ZES_quality(
        boolean ZES_linked, long ZES_defects, long ZES_total,
        double ZES_availability, double ZES_performance)
    {
        if (!ZES_linked)
        {
            return Map.of("qualityDataLinked", false, "ppm", Optional.empty(), "oee", Optional.empty());
        }
        double ZES_quality = ZES_total <= 0 ? 0 : 1 - (double) ZES_defects / ZES_total;
        return Map.of(
            "qualityDataLinked", true,
            "ppm", ZES_total <= 0 ? 0 : (double) ZES_defects / ZES_total * 1_000_000,
            "oee", ZES_availability * ZES_performance * ZES_quality * 100);
    }
}
