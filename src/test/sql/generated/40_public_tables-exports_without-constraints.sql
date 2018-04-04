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
CREATE TABLE export_messages (
    id bigint NOT NULL,
    criticity character varying,
    message_key character varying,
    message_attributes shared_extensions.hstore,
    export_id bigint,
    resource_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    resource_attributes shared_extensions.hstore
);
ALTER TABLE export_messages OWNER TO chouette;
CREATE SEQUENCE export_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE export_messages_id_seq OWNER TO chouette;
ALTER SEQUENCE export_messages_id_seq OWNED BY export_messages.id;
CREATE TABLE export_resources (
    id bigint NOT NULL,
    export_id bigint,
    status character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    resource_type character varying,
    reference character varying,
    name character varying,
    metrics shared_extensions.hstore
);
ALTER TABLE export_resources OWNER TO chouette;
CREATE SEQUENCE export_resources_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE export_resources_id_seq OWNER TO chouette;
ALTER SEQUENCE export_resources_id_seq OWNED BY export_resources.id;
CREATE TABLE exports (
    id bigint NOT NULL,
    status character varying,
    current_step_id character varying,
    current_step_progress double precision,
    workbench_id bigint,
    referential_id bigint,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    file character varying,
    started_at timestamp without time zone,
    ended_at timestamp without time zone,
    token_upload character varying,
    type character varying,
    parent_id bigint,
    parent_type character varying,
    notified_parent_at timestamp without time zone,
    current_step integer DEFAULT 0,
    total_steps integer DEFAULT 0,
    creator character varying,
    options shared_extensions.hstore
);
ALTER TABLE exports OWNER TO chouette;
CREATE SEQUENCE exports_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE exports_id_seq OWNER TO chouette;
ALTER SEQUENCE exports_id_seq OWNED BY exports.id;
ALTER TABLE ONLY export_messages ALTER COLUMN id SET DEFAULT nextval('export_messages_id_seq'::regclass);
ALTER TABLE ONLY export_resources ALTER COLUMN id SET DEFAULT nextval('export_resources_id_seq'::regclass);
ALTER TABLE ONLY exports ALTER COLUMN id SET DEFAULT nextval('exports_id_seq'::regclass);
ALTER TABLE ONLY export_messages    ADD CONSTRAINT export_messages_pkey PRIMARY KEY (id);
ALTER TABLE ONLY export_resources    ADD CONSTRAINT export_resources_pkey PRIMARY KEY (id);
ALTER TABLE ONLY exports    ADD CONSTRAINT exports_pkey PRIMARY KEY (id);
CREATE INDEX index_export_messages_on_export_id ON export_messages USING btree (export_id);
CREATE INDEX index_export_messages_on_resource_id ON export_messages USING btree (resource_id);
CREATE INDEX index_export_resources_on_export_id ON export_resources USING btree (export_id);
CREATE INDEX index_exports_on_referential_id ON exports USING btree (referential_id);
CREATE INDEX index_exports_on_workbench_id ON exports USING btree (workbench_id);
