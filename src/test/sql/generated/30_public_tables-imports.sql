--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.15
-- Dumped by pg_dump version 9.6.7

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

--
-- Name: import_messages; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE import_messages (
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


ALTER TABLE import_messages OWNER TO chouette;

--
-- Name: import_messages_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE import_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE import_messages_id_seq OWNER TO chouette;

--
-- Name: import_messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE import_messages_id_seq OWNED BY import_messages.id;


--
-- Name: import_resources; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE import_resources (
    id bigint NOT NULL,
    import_id bigint,
    status character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    resource_type character varying,
    reference character varying,
    name character varying,
    metrics shared_extensions.hstore
);


ALTER TABLE import_resources OWNER TO chouette;

--
-- Name: import_resources_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE import_resources_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE import_resources_id_seq OWNER TO chouette;

--
-- Name: import_resources_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE import_resources_id_seq OWNED BY import_resources.id;


--
-- Name: imports; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE imports (
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


ALTER TABLE imports OWNER TO chouette;

--
-- Name: imports_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE imports_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE imports_id_seq OWNER TO chouette;

--
-- Name: imports_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE imports_id_seq OWNED BY imports.id;


--
-- Name: import_messages id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY import_messages ALTER COLUMN id SET DEFAULT nextval('import_messages_id_seq'::regclass);


--
-- Name: import_resources id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY import_resources ALTER COLUMN id SET DEFAULT nextval('import_resources_id_seq'::regclass);


--
-- Name: imports id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY imports ALTER COLUMN id SET DEFAULT nextval('imports_id_seq'::regclass);


--
-- Name: import_messages import_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY import_messages
    ADD CONSTRAINT import_messages_pkey PRIMARY KEY (id);


--
-- Name: import_resources import_resources_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY import_resources
    ADD CONSTRAINT import_resources_pkey PRIMARY KEY (id);


--
-- Name: imports imports_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY imports
    ADD CONSTRAINT imports_pkey PRIMARY KEY (id);


--
-- Name: index_import_messages_on_import_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_import_messages_on_import_id ON import_messages USING btree (import_id);


--
-- Name: index_import_messages_on_resource_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_import_messages_on_resource_id ON import_messages USING btree (resource_id);


--
-- Name: index_import_resources_on_import_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_import_resources_on_import_id ON import_resources USING btree (import_id);


--
-- Name: index_imports_on_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_imports_on_referential_id ON imports USING btree (referential_id);


--
-- Name: index_imports_on_workbench_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_imports_on_workbench_id ON imports USING btree (workbench_id);


--
-- PostgreSQL database dump complete
--

