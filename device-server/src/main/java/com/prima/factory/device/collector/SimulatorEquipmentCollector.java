package com.prima.factory.device.collector;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prima.factory.device.simulator.RandomSource;
import com.prima.factory.device.simulator.SeededRandomSource;
import com.prima.factory.device.simulator.TelemetryState;
import com.prima.factory.domain.CommunicationStatus;
import com.prima.factory.domain.EquipmentStatus;

@Component
public class SimulatorEquipmentCollector implements EquipmentDataCollector
{
    private final JdbcTemplate db;
    private final Clock clock;
    private final RandomSource random;
    private final ObjectMapper objectMapper;
    private final ConcurrentMap<Long, TelemetryState> states = new ConcurrentHashMap<>();

    @Autowired
    public SimulatorEquipmentCollector(
        JdbcTemplate db,
        ObjectMapper objectMapper,
        @Value("${simulator.random-seed:360}") long seed)
    {
        this(db, Clock.systemUTC(), new SeededRandomSource(seed), objectMapper);
    }

    SimulatorEquipmentCollector(
        JdbcTemplate db, Clock clock, RandomSource random, ObjectMapper objectMapper)
    {
        this.db = db;
        this.clock = clock;
        this.random = random;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void collect(long equipmentId)
    {
        TelemetryState previous = states.computeIfAbsent(equipmentId, this::load);
        TelemetryState next = previous.next(random);
        states.put(equipmentId, next);
        LocalDateTime now = LocalDateTime.now(clock);
        String displayStatus = next.communication() == CommunicationStatus.DISCONNECTED
            ? "COMMUNICATION_LOSS" : next.operating().name();
        String dynamicTags = dynamicTags(equipmentId, next, now);

        db.update("""
            INSERT INTO robot_telemetry(
                equipment_id,line_id,collected_at,source_timestamp,collection_success,data_quality,
                last_good_received_at,response_ms,operating_mode,operating_status,cycle_count,
                cycle_time,motor_current,load_rate,temperature,dynamic_tags)
            SELECT id,line_id,?,?,?,?,?,?,?,?,?,?,?,?,?,CAST(? AS JSON)
              FROM equipment WHERE id=? AND equipment_type='ROBOT'
            """, now, now, true, "GOOD", now, 20, "AUTO", next.operating().name(),
            next.count(), next.cycle(), next.current(), next.load(), next.temperature(),
            dynamicTags, equipmentId);

        db.update("""
            INSERT INTO press_telemetry(
                equipment_id,line_id,collected_at,source_timestamp,collection_success,data_quality,
                last_good_received_at,response_ms,operating_status,production_counter,spm,
                press_angle,load_ton,slide_position,temperature)
            SELECT id,line_id,?,?,?,?,?,?,?,?,?,?,?,?,?
              FROM equipment WHERE id=? AND equipment_type='PRESS'
            """, now, now, true, "GOOD", now, 20, next.operating().name(), next.count(),
            60 / next.cycle(), random.nextDouble() * 360, 120 + random.nextDouble() * 5,
            random.nextDouble() * 100, next.temperature(), equipmentId);

        db.update("""
            INSERT INTO equipment_current_state(
                equipment_id,operating_status,communication_status,display_status,production_count,
                cycle_time,load_rate,current_amp,temperature,dynamic_tags,response_ms,
                last_good_received_at,collected_at)
            VALUES(?,?,?,?,?,?,?,?,?,CAST(? AS JSON),?,?,?)
            ON DUPLICATE KEY UPDATE
                operating_status=VALUES(operating_status),
                communication_status=VALUES(communication_status),
                display_status=VALUES(display_status),
                production_count=GREATEST(production_count,VALUES(production_count)),
                cycle_time=VALUES(cycle_time),load_rate=VALUES(load_rate),
                current_amp=VALUES(current_amp),temperature=VALUES(temperature),
                dynamic_tags=VALUES(dynamic_tags),response_ms=VALUES(response_ms),
                last_good_received_at=VALUES(last_good_received_at),collected_at=VALUES(collected_at)
            """, equipmentId, next.operating().name(), next.communication().name(), displayStatus,
            next.count(), next.cycle(), next.load(), next.current(), next.temperature(),
            dynamicTags, 20, now, now);

        db.update("""
            INSERT INTO collection_health(
                equipment_id,last_received_at,last_good_received_at,success_rate,
                consecutive_failures,average_response_ms,missing_count,reconnect_count,
                data_quality,updated_at)
            VALUES(?,?,?,?,0,?,0,0,'GOOD',?)
            ON DUPLICATE KEY UPDATE last_received_at=VALUES(last_received_at),
                last_good_received_at=VALUES(last_good_received_at),
                success_rate=VALUES(success_rate),average_response_ms=VALUES(average_response_ms),
                data_quality='GOOD',updated_at=VALUES(updated_at)
            """, equipmentId, now, now, 99.9, 20, now);

        if (next.count() > previous.count())
        {
            db.update("""
                INSERT IGNORE INTO production_count_event(
                    equipment_id,source_event_key,event_at,quantity_delta,cumulative_count)
                VALUES(?,?,?,?,?)
                """, equipmentId, equipmentId + "-" + next.count(), now, 1, next.count());
        }
    }

    private String dynamicTags(long equipmentId, TelemetryState state, LocalDateTime collectedAt)
    {
        Map<String, Object> tags = new LinkedHashMap<>();
        tags.put("simulator.source", "SIMULATOR");
        tags.put("simulator.equipmentId", equipmentId);
        tags.put("simulator.sampleValue", Math.round(random.nextDouble() * 10000.0) / 100.0);
        tags.put("simulator.sampleFlag", random.nextDouble() >= 0.5);
        tags.put("simulator.collectedAt", collectedAt.toString());
        tags.put("simulator.sequence", state.count());
        try
        {
            return objectMapper.writeValueAsString(tags);
        }
        catch (JsonProcessingException exception)
        {
            throw new IllegalStateException("Failed to serialize simulator dynamic tags", exception);
        }
    }

    private TelemetryState load(long equipmentId)
    {
        Long count = db.queryForObject(
            "SELECT COALESCE(production_count,0) FROM equipment_current_state WHERE equipment_id=?",
            Long.class, equipmentId);
        return new TelemetryState(count == null ? 0 : count, 14, 55, 12, 42,
            EquipmentStatus.RUNNING, CommunicationStatus.CONNECTED);
    }
}
