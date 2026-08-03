package com.prima.factory.mapper;
import java.util.*; import org.apache.ibatis.annotations.*;
@Mapper
public interface ZES_MonitoringMapper
{
 @Select("""
     SELECT e.id,
            e.equipment_code code,
            e.equipment_name name,
            e.line_id lineId,
            e.equipment_type type,
            s.display_status status,
            s.operating_status operatingStatus,
            s.communication_status communicationStatus,
            s.status_started_at statusStartedAt,
            p.product_name product,
            COALESCE(pp.target_quantity, 0) target,
            s.production_count actual,
            s.cycle_time cycleTime,
            s.current_alarm alarm,
            s.collected_at lastReceived,
            s.response_ms responseMs,
            e.is_active enabled,
            e.manufacturer,
            e.model,
            c.ip_address ip,
            c.protocol
       FROM equipment e
       JOIN equipment_current_state s ON s.equipment_id = e.id
       LEFT JOIN equipment_connection c ON c.equipment_id = e.id
       LEFT JOIN production_plan pp
         ON pp.line_id = e.line_id
        AND pp.production_date = CURRENT_DATE
        AND pp.status = 'IN_PROGRESS'
       LEFT JOIN product p ON p.id = pp.product_id
      WHERE e.is_active = 1
      ORDER BY e.line_id, e.process_order
     """)
 List<Map<String,Object>> ZES_currentEquipment();
 @Select("SELECT * FROM collection_health ORDER BY equipment_id") List<Map<String,Object>> ZES_collectionHealth();
 @Select("SELECT * FROM product WHERE is_active=1 ORDER BY product_code") List<Map<String,Object>> ZES_products();
 @Select("SELECT * FROM production_line WHERE is_active=1 ORDER BY display_order") List<Map<String,Object>> ZES_lines();
 @Select("SELECT * FROM factory WHERE is_active=1") List<Map<String,Object>> ZES_factories();
 @Select("SELECT * FROM hourly_production_summary WHERE summary_hour>=CURRENT_DATE ORDER BY summary_hour") List<Map<String,Object>> ZES_productionAnalytics();
 @Select("SELECT * FROM downtime_event WHERE started_at>=CURRENT_DATE ORDER BY estimated_loss_quantity DESC") List<Map<String,Object>> ZES_downtimeAnalytics();
 @Select("SELECT * FROM alarm_event WHERE occurred_at>=CURRENT_DATE ORDER BY occurred_at DESC") List<Map<String,Object>> ZES_alarms();
 @Select("SELECT * FROM maintenance_history ORDER BY scheduled_at DESC LIMIT 50") List<Map<String,Object>> ZES_maintenance();
 @Select("SELECT * FROM equipment WHERE id=#{ZES_id}") Map<String,Object> ZES_equipment(@Param("ZES_id") long ZES_id);
 @Select("SELECT * FROM robot_telemetry WHERE equipment_id=#{ZES_id} ORDER BY collected_at DESC LIMIT #{ZES_limit}") List<Map<String,Object>> ZES_trend(@Param("ZES_id") long ZES_id, @Param("ZES_limit") int ZES_limit);
 @Insert("INSERT INTO equipment(line_id,equipment_code,equipment_name,equipment_type,manufacturer,model,process_order,is_active) VALUES(#{lineId},#{code},#{name},#{type},#{manufacturer},#{model},#{processOrder},1)") @Options(useGeneratedKeys=true,keyProperty="id") int ZES_insertEquipment(Map<String,Object> ZES_body);
 @Update("UPDATE equipment SET equipment_name=#{ZES_body.name},manufacturer=#{ZES_body.manufacturer},model=#{ZES_body.model},updated_at=CURRENT_TIMESTAMP(3) WHERE id=#{ZES_id}") int ZES_updateEquipment(@Param("ZES_id") long ZES_id, @Param("ZES_body") Map<String,Object> ZES_body);
 @Update("UPDATE equipment SET is_active=0,updated_at=CURRENT_TIMESTAMP(3) WHERE id=#{ZES_id}") int ZES_deactivateEquipment(@Param("ZES_id") long ZES_id);
 @Insert("INSERT INTO product(product_code,product_name,vehicle_model,customer,unit,is_active) VALUES(#{code},#{name},#{vehicle},#{customer},'EA',1)") @Options(useGeneratedKeys=true,keyProperty="id") int ZES_insertProduct(Map<String,Object> ZES_body);
 @Update("UPDATE product SET product_name=#{ZES_body.name},vehicle_model=#{ZES_body.vehicle},customer=#{ZES_body.customer},updated_at=CURRENT_TIMESTAMP(3) WHERE id=#{ZES_id}") int ZES_updateProduct(@Param("ZES_id") long ZES_id, @Param("ZES_body") Map<String,Object> ZES_body);
 @Update("UPDATE product SET is_active=0,updated_at=CURRENT_TIMESTAMP(3) WHERE id=#{ZES_id}") int ZES_deactivateProduct(@Param("ZES_id") long ZES_id);
}
