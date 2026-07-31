package com.prima.factory.mapper;
import java.util.*; import org.apache.ibatis.annotations.*;
@Mapper public interface MonitoringMapper {
 @Select("""SELECT e.id,e.equipment_code code,e.equipment_name name,e.line_id lineId,e.equipment_type type,s.display_status status,s.operating_status operatingStatus,s.communication_status communicationStatus,s.status_started_at statusStartedAt,p.product_name product,COALESCE(pp.target_quantity,0) target,s.production_count actual,s.cycle_time cycleTime,s.current_alarm alarm,s.collected_at lastReceived,s.response_ms responseMs,e.is_active enabled,e.manufacturer,e.model,c.ip_address ip,c.protocol FROM equipment e JOIN equipment_current_state s ON s.equipment_id=e.id LEFT JOIN equipment_connection c ON c.equipment_id=e.id LEFT JOIN production_plan pp ON pp.line_id=e.line_id AND pp.production_date=CURRENT_DATE AND pp.status='IN_PROGRESS' LEFT JOIN product p ON p.id=pp.product_id WHERE e.is_active=1 ORDER BY e.line_id,e.process_order""") List<Map<String,Object>> currentEquipment();
 @Select("SELECT * FROM collection_health ORDER BY equipment_id") List<Map<String,Object>> collectionHealth();
 @Select("SELECT * FROM product WHERE is_active=1 ORDER BY product_code") List<Map<String,Object>> products();
 @Select("SELECT * FROM production_line WHERE is_active=1 ORDER BY display_order") List<Map<String,Object>> lines();
 @Select("SELECT * FROM factory WHERE is_active=1") List<Map<String,Object>> factories();
 @Select("SELECT * FROM hourly_production_summary WHERE summary_hour>=CURRENT_DATE ORDER BY summary_hour") List<Map<String,Object>> productionAnalytics();
 @Select("SELECT * FROM downtime_event WHERE started_at>=CURRENT_DATE ORDER BY estimated_loss_quantity DESC") List<Map<String,Object>> downtimeAnalytics();
 @Select("SELECT * FROM alarm_event WHERE occurred_at>=CURRENT_DATE ORDER BY occurred_at DESC") List<Map<String,Object>> alarms();
 @Select("SELECT * FROM maintenance_history ORDER BY scheduled_at DESC LIMIT 50") List<Map<String,Object>> maintenance();
 @Select("SELECT * FROM equipment WHERE id=#{id}") Map<String,Object> equipment(long id);
 @Select("SELECT * FROM robot_telemetry WHERE equipment_id=#{id} ORDER BY collected_at DESC LIMIT #{limit}") List<Map<String,Object>> trend(long id,int limit);
 @Insert("INSERT INTO equipment(line_id,equipment_code,equipment_name,equipment_type,manufacturer,model,process_order,is_active) VALUES(#{lineId},#{code},#{name},#{type},#{manufacturer},#{model},#{processOrder},1)") @Options(useGeneratedKeys=true,keyProperty="id") int insertEquipment(Map<String,Object> body);
 @Update("UPDATE equipment SET equipment_name=#{name},manufacturer=#{manufacturer},model=#{model},updated_at=CURRENT_TIMESTAMP(3) WHERE id=#{id}") int updateEquipment(long id,Map<String,Object> body);
 @Update("UPDATE equipment SET is_active=0,updated_at=CURRENT_TIMESTAMP(3) WHERE id=#{id}") int deactivateEquipment(long id);
 @Insert("INSERT INTO product(product_code,product_name,vehicle_model,customer,unit,is_active) VALUES(#{code},#{name},#{vehicle},#{customer},'EA',1)") @Options(useGeneratedKeys=true,keyProperty="id") int insertProduct(Map<String,Object> body);
 @Update("UPDATE product SET product_name=#{name},vehicle_model=#{vehicle},customer=#{customer},updated_at=CURRENT_TIMESTAMP(3) WHERE id=#{id}") int updateProduct(long id,Map<String,Object> body);
 @Update("UPDATE product SET is_active=0,updated_at=CURRENT_TIMESTAMP(3) WHERE id=#{id}") int deactivateProduct(long id);
}
