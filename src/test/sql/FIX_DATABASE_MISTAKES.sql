SET search_path = public, pg_catalog;

ALTER TABLE compliance_checks DROP COLUMN control_attributes;
ALTER TABLE compliance_checks ADD COLUMN control_attributes shared_extensions.hstore;
