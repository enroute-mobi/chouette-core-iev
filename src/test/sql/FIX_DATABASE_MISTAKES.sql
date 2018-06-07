SET search_path = public, pg_catalog;

ALTER TABLE public.compliance_checks
    ADD COLUMN iev_enabled_check boolean;