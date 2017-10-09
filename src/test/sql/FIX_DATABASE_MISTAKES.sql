SET search_path = public, pg_catalog;

--ALTER TABLE compliance_checks DROP COLUMN control_attributes;
--ALTER TABLE compliance_checks ADD COLUMN control_attributes shared_extensions.hstore;

--ALTER TABLE compliance_check_sets ADD COLUMN started_at timestamp without time zone;
--ALTER TABLE compliance_check_sets ADD COLUMN ended_at timestamp without time zone;
--ALTER TABLE compliance_check_sets ADD COLUMN current_step_id character varying;
--ALTER TABLE compliance_check_sets ADD COLUMN current_step_progress double precision;
--ALTER TABLE compliance_check_sets ADD COLUMN name character varying;

ALTER TABLE compliance_checks DROP COLUMN criticity;
ALTER TABLE compliance_checks ADD COLUMN criticity character varying;