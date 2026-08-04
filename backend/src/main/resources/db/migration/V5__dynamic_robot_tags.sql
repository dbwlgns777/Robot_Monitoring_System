-- Flexible payload for robot tags whose names and value types are not known yet.
-- Frequently queried, stable monitoring values remain in typed columns.
ALTER TABLE robot_telemetry
    ADD COLUMN dynamic_tags JSON NULL AFTER total_runtime_minutes;

ALTER TABLE equipment_current_state
    ADD COLUMN dynamic_tags JSON NULL AFTER current_alarm;
