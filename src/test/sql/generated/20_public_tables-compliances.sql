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
-- Name: compliance_check_blocks; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE public.compliance_check_blocks (
    id bigint NOT NULL,
    name character varying,
    condition_attributes shared_extensions.hstore,
    compliance_check_set_id bigint,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);



--
-- Name: compliance_check_blocks_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE public.compliance_check_blocks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: compliance_check_blocks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE public.compliance_check_blocks_id_seq OWNED BY public.compliance_check_blocks.id;


--
-- Name: compliance_check_messages; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE public.compliance_check_messages (
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



--
-- Name: compliance_check_messages_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE public.compliance_check_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: compliance_check_messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE public.compliance_check_messages_id_seq OWNED BY public.compliance_check_messages.id;


--
-- Name: compliance_check_resources; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE public.compliance_check_resources (
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



--
-- Name: compliance_check_resources_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE public.compliance_check_resources_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: compliance_check_resources_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE public.compliance_check_resources_id_seq OWNED BY public.compliance_check_resources.id;


--
-- Name: compliance_check_sets; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE public.compliance_check_sets (
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
    notified_parent_at timestamp without time zone,
    metadata jsonb DEFAULT '{}'::jsonb
);



--
-- Name: compliance_check_sets_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE public.compliance_check_sets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: compliance_check_sets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE public.compliance_check_sets_id_seq OWNED BY public.compliance_check_sets.id;


--
-- Name: compliance_checks; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE public.compliance_checks (
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
    origin_code character varying,
    compliance_control_name character varying
);



--
-- Name: compliance_checks_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE public.compliance_checks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: compliance_checks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE public.compliance_checks_id_seq OWNED BY public.compliance_checks.id;


--
-- Name: compliance_check_blocks id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_blocks ALTER COLUMN id SET DEFAULT nextval('public.compliance_check_blocks_id_seq'::regclass);


--
-- Name: compliance_check_messages id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_messages ALTER COLUMN id SET DEFAULT nextval('public.compliance_check_messages_id_seq'::regclass);


--
-- Name: compliance_check_resources id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_resources ALTER COLUMN id SET DEFAULT nextval('public.compliance_check_resources_id_seq'::regclass);


--
-- Name: compliance_check_sets id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_sets ALTER COLUMN id SET DEFAULT nextval('public.compliance_check_sets_id_seq'::regclass);


--
-- Name: compliance_checks id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_checks ALTER COLUMN id SET DEFAULT nextval('public.compliance_checks_id_seq'::regclass);


--
-- Name: compliance_check_blocks compliance_check_blocks_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_blocks
    ADD CONSTRAINT compliance_check_blocks_pkey PRIMARY KEY (id);


--
-- Name: compliance_check_messages compliance_check_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_messages
    ADD CONSTRAINT compliance_check_messages_pkey PRIMARY KEY (id);


--
-- Name: compliance_check_resources compliance_check_resources_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_resources
    ADD CONSTRAINT compliance_check_resources_pkey PRIMARY KEY (id);


--
-- Name: compliance_check_sets compliance_check_sets_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_sets
    ADD CONSTRAINT compliance_check_sets_pkey PRIMARY KEY (id);


--
-- Name: compliance_checks compliance_checks_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_checks
    ADD CONSTRAINT compliance_checks_pkey PRIMARY KEY (id);


--
-- Name: index_compliance_check_blocks_on_compliance_check_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_blocks_on_compliance_check_set_id ON public.compliance_check_blocks USING btree (compliance_check_set_id);


--
-- Name: index_compliance_check_messages_on_compliance_check_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_messages_on_compliance_check_id ON public.compliance_check_messages USING btree (compliance_check_id);


--
-- Name: index_compliance_check_messages_on_compliance_check_resource_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_messages_on_compliance_check_resource_id ON public.compliance_check_messages USING btree (compliance_check_resource_id);


--
-- Name: index_compliance_check_messages_on_compliance_check_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_messages_on_compliance_check_set_id ON public.compliance_check_messages USING btree (compliance_check_set_id);


--
-- Name: index_compliance_check_resources_on_compliance_check_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_resources_on_compliance_check_set_id ON public.compliance_check_resources USING btree (compliance_check_set_id);


--
-- Name: index_compliance_check_sets_on_compliance_control_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_sets_on_compliance_control_set_id ON public.compliance_check_sets USING btree (compliance_control_set_id);


--
-- Name: index_compliance_check_sets_on_parent_type_and_parent_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_sets_on_parent_type_and_parent_id ON public.compliance_check_sets USING btree (parent_type, parent_id);


--
-- Name: index_compliance_check_sets_on_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_sets_on_referential_id ON public.compliance_check_sets USING btree (referential_id);


--
-- Name: index_compliance_check_sets_on_workbench_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_sets_on_workbench_id ON public.compliance_check_sets USING btree (workbench_id);


--
-- Name: index_compliance_checks_on_compliance_check_block_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_checks_on_compliance_check_block_id ON public.compliance_checks USING btree (compliance_check_block_id);


--
-- Name: index_compliance_checks_on_compliance_check_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_checks_on_compliance_check_set_id ON public.compliance_checks USING btree (compliance_check_set_id);


--
-- Name: compliance_check_messages fk_rails_1361178dd5; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_messages
    ADD CONSTRAINT fk_rails_1361178dd5 FOREIGN KEY (compliance_check_id) REFERENCES public.compliance_checks(id);


--
-- Name: compliance_checks fk_rails_2cbc8a0142; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_checks
    ADD CONSTRAINT fk_rails_2cbc8a0142 FOREIGN KEY (compliance_check_set_id) REFERENCES public.compliance_check_sets(id);


--
-- Name: compliance_check_resources fk_rails_45cd323eca; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_resources
    ADD CONSTRAINT fk_rails_45cd323eca FOREIGN KEY (compliance_check_set_id) REFERENCES public.compliance_check_sets(id);


--
-- Name: compliance_check_messages fk_rails_70bd95092e; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_messages
    ADD CONSTRAINT fk_rails_70bd95092e FOREIGN KEY (compliance_check_resource_id) REFERENCES public.compliance_check_resources(id);


--
-- Name: compliance_check_blocks fk_rails_7d7a89703f; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_blocks
    ADD CONSTRAINT fk_rails_7d7a89703f FOREIGN KEY (compliance_check_set_id) REFERENCES public.compliance_check_sets(id);


--
-- Name: compliance_check_sets fk_rails_d61509f1a9; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_sets
    ADD CONSTRAINT fk_rails_d61509f1a9 FOREIGN KEY (workbench_id) REFERENCES public.workbenches(id);


--
-- Name: compliance_checks fk_rails_df077b5b35; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_checks
    ADD CONSTRAINT fk_rails_df077b5b35 FOREIGN KEY (compliance_check_block_id) REFERENCES public.compliance_check_blocks(id);


--
-- Name: compliance_check_messages fk_rails_e1cf9ec59a; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY public.compliance_check_messages
    ADD CONSTRAINT fk_rails_e1cf9ec59a FOREIGN KEY (compliance_check_set_id) REFERENCES public.compliance_check_sets(id);


--
-- PostgreSQL database dump complete
--

