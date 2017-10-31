--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.14
-- Dumped by pg_dump version 9.6.5

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
-- Name: compliance_check_blocks; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE compliance_check_blocks (
    id bigint NOT NULL,
    name character varying,
    condition_attributes shared_extensions.hstore,
    compliance_check_set_id integer,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);


ALTER TABLE compliance_check_blocks OWNER TO chouette;

--
-- Name: compliance_check_blocks_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE compliance_check_blocks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE compliance_check_blocks_id_seq OWNER TO chouette;

--
-- Name: compliance_check_blocks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE compliance_check_blocks_id_seq OWNED BY compliance_check_blocks.id;


--
-- Name: compliance_check_messages; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE compliance_check_messages (
    id bigint NOT NULL,
    compliance_check_id integer,
    compliance_check_resource_id integer,
    message_key character varying,
    message_attributes shared_extensions.hstore,
    resource_attributes shared_extensions.hstore,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    status character varying,
    compliance_check_set_id integer
);


ALTER TABLE compliance_check_messages OWNER TO chouette;

--
-- Name: compliance_check_messages_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE compliance_check_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE compliance_check_messages_id_seq OWNER TO chouette;

--
-- Name: compliance_check_messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE compliance_check_messages_id_seq OWNED BY compliance_check_messages.id;


--
-- Name: compliance_check_resources; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE compliance_check_resources (
    id bigint NOT NULL,
    status character varying,
    name character varying,
    type character varying,
    reference character varying,
    metrics shared_extensions.hstore,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    compliance_check_set_id integer
);


ALTER TABLE compliance_check_resources OWNER TO chouette;

--
-- Name: compliance_check_resources_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE compliance_check_resources_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE compliance_check_resources_id_seq OWNER TO chouette;

--
-- Name: compliance_check_resources_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE compliance_check_resources_id_seq OWNED BY compliance_check_resources.id;


--
-- Name: compliance_check_sets; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE compliance_check_sets (
    id bigint NOT NULL,
    referential_id integer,
    compliance_control_set_id integer,
    workbench_id integer,
    creator character varying,
    status character varying,
    parent_id integer,
    parent_type character varying,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    current_step_id character varying,
    current_step_progress double precision,
    name character varying,
    started_at timestamp without time zone,
    ended_at timestamp without time zone
);


ALTER TABLE compliance_check_sets OWNER TO chouette;

--
-- Name: compliance_check_sets_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE compliance_check_sets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE compliance_check_sets_id_seq OWNER TO chouette;

--
-- Name: compliance_check_sets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE compliance_check_sets_id_seq OWNED BY compliance_check_sets.id;


--
-- Name: compliance_checks; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE compliance_checks (
    id bigint NOT NULL,
    compliance_check_set_id integer,
    compliance_check_block_id integer,
    type character varying,
    control_attributes shared_extensions.hstore,
    name character varying,
    code character varying,
    criticity character varying,
    comment text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    origin_code character varying
);


ALTER TABLE compliance_checks OWNER TO chouette;

--
-- Name: compliance_checks_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE compliance_checks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE compliance_checks_id_seq OWNER TO chouette;

--
-- Name: compliance_checks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE compliance_checks_id_seq OWNED BY compliance_checks.id;


--
-- Name: compliance_control_blocks; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE compliance_control_blocks (
    id bigint NOT NULL,
    name character varying,
    condition_attributes shared_extensions.hstore,
    compliance_control_set_id integer,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);


ALTER TABLE compliance_control_blocks OWNER TO chouette;

--
-- Name: compliance_control_blocks_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE compliance_control_blocks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE compliance_control_blocks_id_seq OWNER TO chouette;

--
-- Name: compliance_control_blocks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE compliance_control_blocks_id_seq OWNED BY compliance_control_blocks.id;


--
-- Name: compliance_control_sets; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE compliance_control_sets (
    id bigint NOT NULL,
    name character varying,
    organisation_id integer,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);


ALTER TABLE compliance_control_sets OWNER TO chouette;

--
-- Name: compliance_control_sets_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE compliance_control_sets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE compliance_control_sets_id_seq OWNER TO chouette;

--
-- Name: compliance_control_sets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE compliance_control_sets_id_seq OWNED BY compliance_control_sets.id;


--
-- Name: compliance_controls; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE compliance_controls (
    id bigint NOT NULL,
    compliance_control_set_id integer,
    type character varying,
    control_attributes shared_extensions.hstore,
    name character varying,
    code character varying,
    criticity character varying,
    comment text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    origin_code character varying,
    compliance_control_block_id integer
);


ALTER TABLE compliance_controls OWNER TO chouette;

--
-- Name: compliance_controls_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE compliance_controls_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE compliance_controls_id_seq OWNER TO chouette;

--
-- Name: compliance_controls_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE compliance_controls_id_seq OWNED BY compliance_controls.id;


--
-- Name: compliance_check_blocks id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_blocks ALTER COLUMN id SET DEFAULT nextval('compliance_check_blocks_id_seq'::regclass);


--
-- Name: compliance_check_messages id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_messages ALTER COLUMN id SET DEFAULT nextval('compliance_check_messages_id_seq'::regclass);


--
-- Name: compliance_check_resources id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_resources ALTER COLUMN id SET DEFAULT nextval('compliance_check_resources_id_seq'::regclass);


--
-- Name: compliance_check_sets id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_sets ALTER COLUMN id SET DEFAULT nextval('compliance_check_sets_id_seq'::regclass);


--
-- Name: compliance_checks id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_checks ALTER COLUMN id SET DEFAULT nextval('compliance_checks_id_seq'::regclass);


--
-- Name: compliance_control_blocks id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_control_blocks ALTER COLUMN id SET DEFAULT nextval('compliance_control_blocks_id_seq'::regclass);


--
-- Name: compliance_control_sets id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_control_sets ALTER COLUMN id SET DEFAULT nextval('compliance_control_sets_id_seq'::regclass);


--
-- Name: compliance_controls id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_controls ALTER COLUMN id SET DEFAULT nextval('compliance_controls_id_seq'::regclass);


--
-- Name: compliance_check_blocks compliance_check_blocks_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_blocks
    ADD CONSTRAINT compliance_check_blocks_pkey PRIMARY KEY (id);


--
-- Name: compliance_check_messages compliance_check_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_messages
    ADD CONSTRAINT compliance_check_messages_pkey PRIMARY KEY (id);


--
-- Name: compliance_check_resources compliance_check_resources_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_resources
    ADD CONSTRAINT compliance_check_resources_pkey PRIMARY KEY (id);


--
-- Name: compliance_check_sets compliance_check_sets_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_sets
    ADD CONSTRAINT compliance_check_sets_pkey PRIMARY KEY (id);


--
-- Name: compliance_checks compliance_checks_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_checks
    ADD CONSTRAINT compliance_checks_pkey PRIMARY KEY (id);


--
-- Name: compliance_control_blocks compliance_control_blocks_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_control_blocks
    ADD CONSTRAINT compliance_control_blocks_pkey PRIMARY KEY (id);


--
-- Name: compliance_control_sets compliance_control_sets_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_control_sets
    ADD CONSTRAINT compliance_control_sets_pkey PRIMARY KEY (id);


--
-- Name: compliance_controls compliance_controls_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_controls
    ADD CONSTRAINT compliance_controls_pkey PRIMARY KEY (id);


--
-- Name: index_compliance_check_blocks_on_compliance_check_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_blocks_on_compliance_check_set_id ON compliance_check_blocks USING btree (compliance_check_set_id);


--
-- Name: index_compliance_check_messages_on_compliance_check_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_messages_on_compliance_check_id ON compliance_check_messages USING btree (compliance_check_id);


--
-- Name: index_compliance_check_messages_on_compliance_check_resource_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_messages_on_compliance_check_resource_id ON compliance_check_messages USING btree (compliance_check_resource_id);


--
-- Name: index_compliance_check_messages_on_compliance_check_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_messages_on_compliance_check_set_id ON compliance_check_messages USING btree (compliance_check_set_id);


--
-- Name: index_compliance_check_resources_on_compliance_check_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_resources_on_compliance_check_set_id ON compliance_check_resources USING btree (compliance_check_set_id);


--
-- Name: index_compliance_check_sets_on_compliance_control_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_sets_on_compliance_control_set_id ON compliance_check_sets USING btree (compliance_control_set_id);


--
-- Name: index_compliance_check_sets_on_parent_type_and_parent_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_sets_on_parent_type_and_parent_id ON compliance_check_sets USING btree (parent_type, parent_id);


--
-- Name: index_compliance_check_sets_on_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_sets_on_referential_id ON compliance_check_sets USING btree (referential_id);


--
-- Name: index_compliance_check_sets_on_workbench_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_check_sets_on_workbench_id ON compliance_check_sets USING btree (workbench_id);


--
-- Name: index_compliance_checks_on_compliance_check_block_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_checks_on_compliance_check_block_id ON compliance_checks USING btree (compliance_check_block_id);


--
-- Name: index_compliance_checks_on_compliance_check_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_checks_on_compliance_check_set_id ON compliance_checks USING btree (compliance_check_set_id);


--
-- Name: index_compliance_control_blocks_on_compliance_control_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_control_blocks_on_compliance_control_set_id ON compliance_control_blocks USING btree (compliance_control_set_id);


--
-- Name: index_compliance_control_sets_on_organisation_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_control_sets_on_organisation_id ON compliance_control_sets USING btree (organisation_id);


--
-- Name: index_compliance_controls_on_code_and_compliance_control_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX index_compliance_controls_on_code_and_compliance_control_set_id ON compliance_controls USING btree (code, compliance_control_set_id);


--
-- Name: index_compliance_controls_on_compliance_control_block_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_controls_on_compliance_control_block_id ON compliance_controls USING btree (compliance_control_block_id);


--
-- Name: index_compliance_controls_on_compliance_control_set_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_compliance_controls_on_compliance_control_set_id ON compliance_controls USING btree (compliance_control_set_id);


--
-- Name: compliance_control_blocks fk_rails_0f26e226bd; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_control_blocks
    ADD CONSTRAINT fk_rails_0f26e226bd FOREIGN KEY (compliance_control_set_id) REFERENCES compliance_control_sets(id);


--
-- Name: compliance_check_messages fk_rails_1361178dd5; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_messages
    ADD CONSTRAINT fk_rails_1361178dd5 FOREIGN KEY (compliance_check_id) REFERENCES compliance_checks(id);


--
-- Name: compliance_checks fk_rails_2cbc8a0142; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_checks
    ADD CONSTRAINT fk_rails_2cbc8a0142 FOREIGN KEY (compliance_check_set_id) REFERENCES compliance_check_sets(id);


--
-- Name: compliance_check_sets fk_rails_4145f3761b; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_sets
    ADD CONSTRAINT fk_rails_4145f3761b FOREIGN KEY (referential_id) REFERENCES referentials(id);


--
-- Name: compliance_check_resources fk_rails_45cd323eca; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_resources
    ADD CONSTRAINT fk_rails_45cd323eca FOREIGN KEY (compliance_check_set_id) REFERENCES compliance_check_sets(id);


--
-- Name: compliance_check_messages fk_rails_70bd95092e; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_messages
    ADD CONSTRAINT fk_rails_70bd95092e FOREIGN KEY (compliance_check_resource_id) REFERENCES compliance_check_resources(id);


--
-- Name: compliance_check_blocks fk_rails_7d7a89703f; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_blocks
    ADD CONSTRAINT fk_rails_7d7a89703f FOREIGN KEY (compliance_check_set_id) REFERENCES compliance_check_sets(id);


--
-- Name: compliance_control_sets fk_rails_aa1e909966; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_control_sets
    ADD CONSTRAINT fk_rails_aa1e909966 FOREIGN KEY (organisation_id) REFERENCES organisations(id);


--
-- Name: compliance_controls fk_rails_c613154a10; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_controls
    ADD CONSTRAINT fk_rails_c613154a10 FOREIGN KEY (compliance_control_block_id) REFERENCES compliance_control_blocks(id);


--
-- Name: compliance_check_sets fk_rails_d227ba43d7; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_sets
    ADD CONSTRAINT fk_rails_d227ba43d7 FOREIGN KEY (compliance_control_set_id) REFERENCES compliance_control_sets(id);


--
-- Name: compliance_check_sets fk_rails_d61509f1a9; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_sets
    ADD CONSTRAINT fk_rails_d61509f1a9 FOREIGN KEY (workbench_id) REFERENCES workbenches(id);


--
-- Name: compliance_checks fk_rails_df077b5b35; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_checks
    ADD CONSTRAINT fk_rails_df077b5b35 FOREIGN KEY (compliance_check_block_id) REFERENCES compliance_check_blocks(id);


--
-- Name: compliance_check_messages fk_rails_e1cf9ec59a; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_check_messages
    ADD CONSTRAINT fk_rails_e1cf9ec59a FOREIGN KEY (compliance_check_set_id) REFERENCES compliance_check_sets(id);


--
-- Name: compliance_controls fk_rails_f402e905ef; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY compliance_controls
    ADD CONSTRAINT fk_rails_f402e905ef FOREIGN KEY (compliance_control_set_id) REFERENCES compliance_control_sets(id);


--
-- PostgreSQL database dump complete
--

