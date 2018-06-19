SET statement_timeout = 0;
SET lock_timeout = 0;
-- SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
-- SET row_security = off;
SET default_tablespace = '';
SET default_with_oids = false;
CREATE TABLE public.export_messages (
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
ALTER TABLE public.export_messages OWNER TO chouette;
CREATE SEQUENCE public.export_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.export_messages_id_seq OWNER TO chouette;
ALTER SEQUENCE public.export_messages_id_seq OWNED BY public.export_messages.id;
CREATE TABLE public.export_resources (
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
ALTER TABLE public.export_resources OWNER TO chouette;
CREATE SEQUENCE public.export_resources_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.export_resources_id_seq OWNER TO chouette;
ALTER SEQUENCE public.export_resources_id_seq OWNED BY public.export_resources.id;
CREATE TABLE public.exports (
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
ALTER TABLE public.exports OWNER TO chouette;
CREATE SEQUENCE public.exports_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.exports_id_seq OWNER TO chouette;
ALTER SEQUENCE public.exports_id_seq OWNED BY public.exports.id;
ALTER TABLE ONLY public.export_messages ALTER COLUMN id SET DEFAULT nextval('public.export_messages_id_seq'::regclass);
ALTER TABLE ONLY public.export_resources ALTER COLUMN id SET DEFAULT nextval('public.export_resources_id_seq'::regclass);
ALTER TABLE ONLY public.exports ALTER COLUMN id SET DEFAULT nextval('public.exports_id_seq'::regclass);
ALTER TABLE ONLY public.export_messages    ADD CONSTRAINT export_messages_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.export_resources    ADD CONSTRAINT export_resources_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.exports    ADD CONSTRAINT exports_pkey PRIMARY KEY (id);
CREATE INDEX index_export_messages_on_export_id ON public.export_messages USING btree (export_id);
CREATE INDEX index_export_messages_on_resource_id ON public.export_messages USING btree (resource_id);
CREATE INDEX index_export_resources_on_export_id ON public.export_resources USING btree (export_id);
CREATE INDEX index_exports_on_referential_id ON public.exports USING btree (referential_id);
CREATE INDEX index_exports_on_workbench_id ON public.exports USING btree (workbench_id);
