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
CREATE TABLE public.import_messages (
    id bigint NOT NULL,
    criticity character varying,
    message_key character varying,
    message_attributes shared_extensions.hstore,
    import_id bigint,
    resource_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    resource_attributes shared_extensions.hstore
);
ALTER TABLE public.import_messages OWNER TO chouette;
CREATE SEQUENCE public.import_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.import_messages_id_seq OWNER TO chouette;
ALTER SEQUENCE public.import_messages_id_seq OWNED BY public.import_messages.id;
CREATE TABLE public.import_resources (
    id bigint NOT NULL,
    import_id bigint,
    status character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    resource_type character varying,
    reference character varying,
    name character varying,
    metrics shared_extensions.hstore,
    referential_id bigint
);
ALTER TABLE public.import_resources OWNER TO chouette;
CREATE SEQUENCE public.import_resources_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.import_resources_id_seq OWNER TO chouette;
ALTER SEQUENCE public.import_resources_id_seq OWNED BY public.import_resources.id;
CREATE TABLE public.imports (
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
    token_download character varying,
    type character varying,
    parent_id bigint,
    parent_type character varying,
    notified_parent_at timestamp without time zone,
    current_step integer DEFAULT 0,
    total_steps integer DEFAULT 0,
    creator character varying
);
ALTER TABLE public.imports OWNER TO chouette;
CREATE SEQUENCE public.imports_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.imports_id_seq OWNER TO chouette;
ALTER SEQUENCE public.imports_id_seq OWNED BY public.imports.id;
ALTER TABLE ONLY public.import_messages ALTER COLUMN id SET DEFAULT nextval('public.import_messages_id_seq'::regclass);
ALTER TABLE ONLY public.import_resources ALTER COLUMN id SET DEFAULT nextval('public.import_resources_id_seq'::regclass);
ALTER TABLE ONLY public.imports ALTER COLUMN id SET DEFAULT nextval('public.imports_id_seq'::regclass);
ALTER TABLE ONLY public.import_messages    ADD CONSTRAINT import_messages_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.import_resources    ADD CONSTRAINT import_resources_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.imports    ADD CONSTRAINT imports_pkey PRIMARY KEY (id);
CREATE INDEX index_import_messages_on_import_id ON public.import_messages USING btree (import_id);
CREATE INDEX index_import_messages_on_resource_id ON public.import_messages USING btree (resource_id);
CREATE INDEX index_import_resources_on_import_id ON public.import_resources USING btree (import_id);
CREATE INDEX index_import_resources_on_referential_id ON public.import_resources USING btree (referential_id);
CREATE INDEX index_imports_on_referential_id ON public.imports USING btree (referential_id);
CREATE INDEX index_imports_on_workbench_id ON public.imports USING btree (workbench_id);
