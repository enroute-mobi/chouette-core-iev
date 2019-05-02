--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.13
-- Dumped by pg_dump version 10.4 (Ubuntu 10.4-2.pgdg18.04+1)

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

--
-- Name: export_messages; Type: TABLE; Schema: public; Owner: chouette
--

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



--
-- Name: export_messages_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE public.export_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: export_messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE public.export_messages_id_seq OWNED BY public.export_messages.id;


--
-- Name: export_resources; Type: TABLE; Schema: public; Owner: chouette
--

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



--
-- Name: export_resources_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE public.export_resources_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: export_resources_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE public.export_resources_id_seq OWNED BY public.export_resources.id;


--
-- Name: exports; Type: TABLE; Schema: public; Owner: chouette
--

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



--
-- Name: exports_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE public.exports_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: exports_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE public.exports_id_seq OWNED BY public.exports.id;


--
-- Name: export_messages id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.export_messages ALTER COLUMN id SET DEFAULT nextval('public.export_messages_id_seq'::regclass);


--
-- Name: export_resources id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.export_resources ALTER COLUMN id SET DEFAULT nextval('public.export_resources_id_seq'::regclass);


--
-- Name: exports id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.exports ALTER COLUMN id SET DEFAULT nextval('public.exports_id_seq'::regclass);


--
-- Name: export_messages export_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.export_messages
    ADD CONSTRAINT export_messages_pkey PRIMARY KEY (id);


--
-- Name: export_resources export_resources_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.export_resources
    ADD CONSTRAINT export_resources_pkey PRIMARY KEY (id);


--
-- Name: exports exports_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.exports
    ADD CONSTRAINT exports_pkey PRIMARY KEY (id);


--
-- Name: index_export_messages_on_export_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_export_messages_on_export_id ON public.export_messages USING btree (export_id);


--
-- Name: index_export_messages_on_resource_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_export_messages_on_resource_id ON public.export_messages USING btree (resource_id);


--
-- Name: index_export_resources_on_export_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_export_resources_on_export_id ON public.export_resources USING btree (export_id);


--
-- Name: index_exports_on_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_exports_on_referential_id ON public.exports USING btree (referential_id);


--
-- Name: index_exports_on_workbench_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_exports_on_workbench_id ON public.exports USING btree (workbench_id);


--
-- PostgreSQL database dump complete
--

