SET statement_timeout = 0;
SET lock_timeout = 0;
-- SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
-- SET row_security = off;
SET search_path = public, pg_catalog;
SET default_tablespace = '';
SET default_with_oids = false;
CREATE TABLE compliance_check_blocks (
    id bigint NOT NULL,
    name character varying,
    condition_attributes shared_extensions.hstore,
    compliance_check_set_id bigint,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);
ALTER TABLE compliance_check_blocks OWNER TO chouette;
CREATE SEQUENCE compliance_check_blocks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE compliance_check_blocks_id_seq OWNER TO chouette;
ALTER SEQUENCE compliance_check_blocks_id_seq OWNED BY compliance_check_blocks.id;
CREATE TABLE compliance_check_messages (
    id bigint NOT NULL,
    compliance_check_id bigint,
    compliance_check_resource_id bigint,
    message_key character varying,
    message_attributes shared_extensions.hstore,
    resource_attributes shared_extensions.hstore,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    status character varying,
    compliance_check_set_id bigint
);
ALTER TABLE compliance_check_messages OWNER TO chouette;
CREATE SEQUENCE compliance_check_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE compliance_check_messages_id_seq OWNER TO chouette;
ALTER SEQUENCE compliance_check_messages_id_seq OWNED BY compliance_check_messages.id;
CREATE TABLE compliance_check_resources (
    id bigint NOT NULL,
    status character varying,
    name character varying,
    resource_type character varying,
    reference character varying,
    metrics shared_extensions.hstore,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    compliance_check_set_id bigint
);
ALTER TABLE compliance_check_resources OWNER TO chouette;
CREATE SEQUENCE compliance_check_resources_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE compliance_check_resources_id_seq OWNER TO chouette;
ALTER SEQUENCE compliance_check_resources_id_seq OWNED BY compliance_check_resources.id;
CREATE TABLE compliance_check_sets (
    id bigint NOT NULL,
    referential_id bigint,
    compliance_control_set_id bigint,
    workbench_id bigint,
    status character varying,
    parent_id bigint,
    parent_type character varying,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    current_step_id character varying,
    current_step_progress double precision,
    name character varying,
    started_at timestamp without time zone,
    ended_at timestamp without time zone,
    notified_parent_at timestamp without time zone
);
ALTER TABLE compliance_check_sets OWNER TO chouette;
CREATE SEQUENCE compliance_check_sets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE compliance_check_sets_id_seq OWNER TO chouette;
ALTER SEQUENCE compliance_check_sets_id_seq OWNED BY compliance_check_sets.id;
CREATE TABLE compliance_checks (
    id bigint NOT NULL,
    compliance_check_set_id bigint,
    compliance_check_block_id bigint,
    type character varying,
    control_attributes json,
    name character varying,
    code character varying,
    criticity character varying,
    comment text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    origin_code character varying
);
ALTER TABLE compliance_checks OWNER TO chouette;
CREATE SEQUENCE compliance_checks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE compliance_checks_id_seq OWNER TO chouette;
ALTER SEQUENCE compliance_checks_id_seq OWNED BY compliance_checks.id;
CREATE TABLE compliance_control_blocks (
    id bigint NOT NULL,
    name character varying,
    condition_attributes shared_extensions.hstore,
    compliance_control_set_id bigint,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);
ALTER TABLE compliance_control_blocks OWNER TO chouette;
CREATE SEQUENCE compliance_control_blocks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE compliance_control_blocks_id_seq OWNER TO chouette;
ALTER SEQUENCE compliance_control_blocks_id_seq OWNED BY compliance_control_blocks.id;
CREATE TABLE compliance_control_sets (
    id bigint NOT NULL,
    name character varying,
    organisation_id bigint,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);
ALTER TABLE compliance_control_sets OWNER TO chouette;
CREATE SEQUENCE compliance_control_sets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE compliance_control_sets_id_seq OWNER TO chouette;
ALTER SEQUENCE compliance_control_sets_id_seq OWNED BY compliance_control_sets.id;
CREATE TABLE compliance_controls (
    id bigint NOT NULL,
    compliance_control_set_id bigint,
    type character varying,
    control_attributes json,
    name character varying,
    code character varying,
    criticity character varying,
    comment text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    origin_code character varying,
    compliance_control_block_id bigint
);
ALTER TABLE compliance_controls OWNER TO chouette;
CREATE SEQUENCE compliance_controls_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE compliance_controls_id_seq OWNER TO chouette;
ALTER SEQUENCE compliance_controls_id_seq OWNED BY compliance_controls.id;
ALTER TABLE ONLY compliance_check_blocks ALTER COLUMN id SET DEFAULT nextval('compliance_check_blocks_id_seq'::regclass);
ALTER TABLE ONLY compliance_check_messages ALTER COLUMN id SET DEFAULT nextval('compliance_check_messages_id_seq'::regclass);
ALTER TABLE ONLY compliance_check_resources ALTER COLUMN id SET DEFAULT nextval('compliance_check_resources_id_seq'::regclass);
ALTER TABLE ONLY compliance_check_sets ALTER COLUMN id SET DEFAULT nextval('compliance_check_sets_id_seq'::regclass);
ALTER TABLE ONLY compliance_checks ALTER COLUMN id SET DEFAULT nextval('compliance_checks_id_seq'::regclass);
ALTER TABLE ONLY compliance_control_blocks ALTER COLUMN id SET DEFAULT nextval('compliance_control_blocks_id_seq'::regclass);
ALTER TABLE ONLY compliance_control_sets ALTER COLUMN id SET DEFAULT nextval('compliance_control_sets_id_seq'::regclass);
ALTER TABLE ONLY compliance_controls ALTER COLUMN id SET DEFAULT nextval('compliance_controls_id_seq'::regclass);
ALTER TABLE ONLY compliance_check_blocks    ADD CONSTRAINT compliance_check_blocks_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_check_messages    ADD CONSTRAINT compliance_check_messages_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_check_resources    ADD CONSTRAINT compliance_check_resources_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_check_sets    ADD CONSTRAINT compliance_check_sets_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_checks    ADD CONSTRAINT compliance_checks_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_control_blocks    ADD CONSTRAINT compliance_control_blocks_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_control_sets    ADD CONSTRAINT compliance_control_sets_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_controls    ADD CONSTRAINT compliance_controls_pkey PRIMARY KEY (id);
CREATE INDEX index_compliance_check_blocks_on_compliance_check_set_id ON compliance_check_blocks USING btree (compliance_check_set_id);
CREATE INDEX index_compliance_check_messages_on_compliance_check_id ON compliance_check_messages USING btree (compliance_check_id);
CREATE INDEX index_compliance_check_messages_on_compliance_check_resource_id ON compliance_check_messages USING btree (compliance_check_resource_id);
CREATE INDEX index_compliance_check_messages_on_compliance_check_set_id ON compliance_check_messages USING btree (compliance_check_set_id);
CREATE INDEX index_compliance_check_resources_on_compliance_check_set_id ON compliance_check_resources USING btree (compliance_check_set_id);
CREATE INDEX index_compliance_check_sets_on_compliance_control_set_id ON compliance_check_sets USING btree (compliance_control_set_id);
CREATE INDEX index_compliance_check_sets_on_parent_type_and_parent_id ON compliance_check_sets USING btree (parent_type, parent_id);
CREATE INDEX index_compliance_check_sets_on_referential_id ON compliance_check_sets USING btree (referential_id);
CREATE INDEX index_compliance_check_sets_on_workbench_id ON compliance_check_sets USING btree (workbench_id);
CREATE INDEX index_compliance_checks_on_compliance_check_block_id ON compliance_checks USING btree (compliance_check_block_id);
CREATE INDEX index_compliance_checks_on_compliance_check_set_id ON compliance_checks USING btree (compliance_check_set_id);
CREATE INDEX index_compliance_control_blocks_on_compliance_control_set_id ON compliance_control_blocks USING btree (compliance_control_set_id);
CREATE INDEX index_compliance_control_sets_on_organisation_id ON compliance_control_sets USING btree (organisation_id);
CREATE UNIQUE INDEX index_compliance_controls_on_code_and_compliance_control_set_id ON compliance_controls USING btree (code, compliance_control_set_id);
CREATE INDEX index_compliance_controls_on_compliance_control_block_id ON compliance_controls USING btree (compliance_control_block_id);
CREATE INDEX index_compliance_controls_on_compliance_control_set_id ON compliance_controls USING btree (compliance_control_set_id);
