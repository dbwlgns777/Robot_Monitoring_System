package com.prima.factory.device.collector;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SimulatorScheduler
{
    private final JdbcTemplate db;
    private final EquipmentDataCollector collector;
    private final boolean enabled;

    public SimulatorScheduler(
        JdbcTemplate db,
        EquipmentDataCollector collector,
        @Value("${simulator.enabled:true}") boolean enabled)
    {
        this.db = db;
        this.collector = collector;
        this.enabled = enabled;
    }

    @Scheduled(fixedRateString = "${simulator.interval-ms:1000}")
    void tick()
    {
        if (!enabled)
        {
            return;
        }

        List<Long> equipmentIds = db.queryForList(
            "SELECT id FROM equipment WHERE is_active=1", Long.class);
        equipmentIds.parallelStream().forEach(this::collect);
        db.update("""
            INSERT INTO service_heartbeat(service_name,status,last_heartbeat_at,details)
            VALUES('DEVICE_SERVER','UP',CURRENT_TIMESTAMP(3),'simulator')
            ON DUPLICATE KEY UPDATE status='UP',last_heartbeat_at=CURRENT_TIMESTAMP(3)
            """);
    }

    private void collect(long equipmentId)
    {
        try
        {
            collector.collect(equipmentId);
        }
        catch (Exception exception)
        {
            db.update("""
                UPDATE collection_health
                   SET consecutive_failures=consecutive_failures+1,
                       data_quality='BAD',
                       updated_at=CURRENT_TIMESTAMP(3)
                 WHERE equipment_id=?
                """, equipmentId);
        }
    }
}
