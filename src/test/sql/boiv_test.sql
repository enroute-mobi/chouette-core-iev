--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.11
-- Dumped by pg_dump version 9.1.11
-- Started on 2014-02-27 11:15:39 CET

-- authentification 127.0.0.1 trust
-- USAGE : psql -h 127.0.0.1 -U chouette  -d boiv_test -f boiv_test.sql'

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;


SET default_tablespace = '';

SET default_with_oids = false;

CREATE SCHEMA IF NOT EXISTS shared_extensions;
CREATE EXTENSION IF NOT EXISTS postgis SCHEMA shared_extensions;

-- public 
SET search_path = public, pg_catalog;

DROP TABLE IF EXISTS public.access_links CASCADE;
DROP TABLE IF EXISTS public.access_points CASCADE;
DROP TABLE IF EXISTS public.companies CASCADE;

DROP TABLE IF EXISTS public.compliance_check_blocks  CASCADE;
DROP TABLE IF EXISTS public.compliance_check_resources  CASCADE;
DROP TABLE IF EXISTS public.compliance_check_results  CASCADE;
DROP TABLE IF EXISTS public.compliance_check_sets  CASCADE;
DROP TABLE IF EXISTS public.compliance_checks  CASCADE;
DROP TABLE IF EXISTS public.compliance_control_blocks  CASCADE;
DROP TABLE IF EXISTS public.compliance_control_sets  CASCADE;
DROP TABLE IF EXISTS public.compliance_controls  CASCADE;

DROP TABLE IF EXISTS public.connection_links CASCADE;
DROP TABLE IF EXISTS public.group_of_lines CASCADE;
DROP TABLE IF EXISTS public.group_of_lines_lines CASCADE;
DROP TABLE IF EXISTS public.import_messages CASCADE;
DROP TABLE IF EXISTS public.import_resources CASCADE;
DROP TABLE IF EXISTS public.imports CASCADE;
DROP TABLE IF EXISTS public.line_referentials CASCADE;
DROP TABLE IF EXISTS public.lines CASCADE;
DROP TABLE IF EXISTS public.networks CASCADE;
DROP TABLE IF EXISTS public.organisations CASCADE;
DROP TABLE IF EXISTS public.referential_metadata CASCADE;
DROP TABLE IF EXISTS public.referentials CASCADE;
DROP TABLE IF EXISTS public.route_sections CASCADE;
DROP TABLE IF EXISTS public.stop_area_referentials CASCADE;
DROP TABLE IF EXISTS public.stop_areas CASCADE;
DROP TABLE IF EXISTS public.users CASCADE;
DROP TABLE IF EXISTS public.workbenches CASCADE;



CREATE TABLE access_links (
    id bigint NOT NULL,
    access_point_id bigint,
    stop_area_id bigint,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    name character varying(255),
    comment character varying(255),
    link_distance numeric(19,2),
    lift_availability boolean,
    mobility_restricted_suitability boolean,
    stairs_availability boolean,
    default_duration time without time zone,
    frequent_traveller_duration time without time zone,
    occasional_traveller_duration time without time zone,
    mobility_restricted_traveller_duration time without time zone,
    link_type character varying(255),
    int_user_needs integer,
    link_orientation character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE access_links OWNER TO chouette;
CREATE SEQUENCE access_links_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE access_links_id_seq OWNER TO chouette;
ALTER SEQUENCE access_links_id_seq OWNED BY access_links.id;

CREATE TABLE access_points (
    id bigint NOT NULL,
    objectid character varying(255),
    object_version bigint,
    creator_id character varying(255),
    name character varying(255),
    comment character varying(255),
    longitude numeric(19,16),
    latitude numeric(19,16),
    long_lat_type character varying(255),
    country_code character varying(255),
    street_name character varying(255),
    contained_in character varying(255),
    openning_time time without time zone,
    closing_time time without time zone,
    access_type character varying(255),
    lift_availability boolean,
    mobility_restricted_suitability boolean,
    stairs_availability boolean,
    stop_area_id bigint,
    zip_code character varying(255),
    city_name character varying(255),
    import_xml text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE access_points OWNER TO chouette;
CREATE SEQUENCE access_points_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE access_points_id_seq OWNER TO chouette;
ALTER SEQUENCE access_points_id_seq OWNED BY access_points.id;


CREATE TABLE companies (
    id bigint NOT NULL,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    name character varying(255),
    short_name character varying(255),
    organizational_unit character varying(255),
    operating_department_name character varying(255),
    code character varying(255),
    phone character varying(255),
    fax character varying(255),
    email character varying(255),
    registration_number character varying(255),
    url character varying(255),
    time_zone character varying(255),
    line_referential_id bigint,
    import_xml text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE companies OWNER TO chouette;
CREATE SEQUENCE companies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE companies_id_seq OWNER TO chouette;
ALTER SEQUENCE companies_id_seq OWNED BY companies.id;


--
-- Compliance
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



CREATE SEQUENCE compliance_check_blocks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE compliance_check_blocks_id_seq OWNER TO chouette;

ALTER SEQUENCE compliance_check_blocks_id_seq OWNED BY compliance_check_blocks.id;


CREATE TABLE compliance_check_resources (
    id bigint NOT NULL,
    status character varying,
    name character varying,
    type character varying,
    reference character varying,
    metrics shared_extensions.hstore,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
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

CREATE TABLE compliance_check_results (
    id bigint NOT NULL,
    compliance_check_id integer,
    compliance_check_resource_id integer,
    message_key character varying,
    message_attributes shared_extensions.hstore,
    resource_attributes shared_extensions.hstore,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);


ALTER TABLE compliance_check_results OWNER TO chouette;

CREATE SEQUENCE compliance_check_results_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE compliance_check_results_id_seq OWNER TO chouette;

ALTER SEQUENCE compliance_check_results_id_seq OWNED BY compliance_check_results.id;

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
    updated_at timestamp without time zone NOT NULL
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
    compliance_check_set_id integer,
    compliance_check_block_id integer,
    type character varying,
    control_attributes json,
    name character varying,
    code character varying,
    criticity integer,
    comment text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
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
    compliance_control_set_id integer,
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
    organisation_id integer,
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
    compliance_control_set_id integer,
    compliance_control_block_id integer,
    type character varying,
    control_attributes json,
    name character varying,
    code character varying,
    criticity character varying,
    comment text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
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






CREATE TABLE connection_links (
    id bigint NOT NULL,
    departure_id bigint,
    arrival_id bigint,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    name character varying(255),
    comment character varying(255),
    link_distance numeric(19,2),
    link_type character varying(255),
    default_duration time without time zone,
    frequent_traveller_duration time without time zone,
    occasional_traveller_duration time without time zone,
    mobility_restricted_traveller_duration time without time zone,
    mobility_restricted_suitability boolean,
    stairs_availability boolean,
    lift_availability boolean,
    int_user_needs integer,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE connection_links OWNER TO chouette;
CREATE SEQUENCE connection_links_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE connection_links_id_seq OWNER TO chouette;
ALTER SEQUENCE connection_links_id_seq OWNED BY connection_links.id;


CREATE TABLE group_of_lines (
    id bigint NOT NULL,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    name character varying(255),
    comment character varying(255),
    registration_number character varying(255),
    line_referential_id bigint,
    import_xml text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE group_of_lines OWNER TO chouette;
CREATE SEQUENCE group_of_lines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE group_of_lines_id_seq OWNER TO chouette;
ALTER SEQUENCE group_of_lines_id_seq OWNED BY group_of_lines.id;


CREATE TABLE group_of_lines_lines (
    group_of_line_id bigint,
    line_id bigint
);
ALTER TABLE group_of_lines_lines OWNER TO chouette;

CREATE TABLE import_messages (
    id bigint NOT NULL,
    criticity integer,
    message_key character varying(255),
    message_attributes shared_extensions.hstore,
    import_id bigint,
    resource_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    resource_attributes shared_extensions.hstore
);
ALTER TABLE import_messages OWNER TO chouette;
CREATE SEQUENCE import_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE import_messages_id_seq OWNER TO chouette;
ALTER SEQUENCE import_messages_id_seq OWNED BY import_messages.id;

CREATE TABLE import_resources (
    id bigint NOT NULL,
    import_id bigint,
    status character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    resource_type character varying(255),
    reference character varying(255),
    name character varying(255),
    metrics shared_extensions.hstore
);
ALTER TABLE import_resources OWNER TO chouette;
CREATE SEQUENCE import_resources_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE import_resources_id_seq OWNER TO chouette;
ALTER SEQUENCE import_resources_id_seq OWNED BY import_resources.id;

CREATE TABLE imports (
    id bigint NOT NULL,
    status character varying(255),
    current_step_id character varying(255),
    current_step_progress double precision,
    workbench_id bigint,
    referential_id bigint,
    name character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    file character varying(255),
    started_at timestamp without time zone,
    ended_at timestamp without time zone,
    token_download character varying(255),
    notified_parent_at timestamp without time zone,
    current_step integer DEFAULT 0,
    total_steps integer DEFAULT 0
);

ALTER TABLE imports OWNER TO chouette;
CREATE SEQUENCE imports_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE imports_id_seq OWNER TO chouette;
ALTER SEQUENCE imports_id_seq OWNED BY imports.id;



CREATE TABLE line_referentials (
    id bigint NOT NULL,
    name character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    sync_interval integer DEFAULT 1
);
ALTER TABLE line_referentials OWNER TO chouette;
CREATE SEQUENCE line_referentials_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE line_referentials_id_seq OWNER TO chouette;
ALTER SEQUENCE line_referentials_id_seq OWNED BY line_referentials.id;


CREATE TABLE lines (
    id bigint NOT NULL,
    network_id bigint,
    company_id bigint,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    name character varying(255),
    number character varying(255),
    published_name character varying(255),
    transport_mode character varying(255),
    registration_number character varying(255),
    comment character varying(255),
    mobility_restricted_suitability boolean,
    int_user_needs integer,
    flexible_service boolean,
    url character varying(255),
    color character varying(6),
    text_color character varying(6),
    stable_id character varying(255),
    line_referential_id bigint,
    deactivated boolean DEFAULT false,
    import_xml text,
    transport_submode character varying(255),
    secondary_company_ids bigint[],
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE lines OWNER TO chouette;
CREATE SEQUENCE lines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE lines_id_seq OWNER TO chouette;
ALTER SEQUENCE lines_id_seq OWNED BY lines.id;


CREATE TABLE networks (
    id bigint NOT NULL,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    version_date date,
    description character varying(255),
    name character varying(255),
    registration_number character varying(255),
    source_name character varying(255),
    source_type character varying(255),
    source_identifier character varying(255),
    comment character varying(255),
    import_xml text,
    line_referential_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE networks OWNER TO chouette;
CREATE SEQUENCE networks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE networks_id_seq OWNER TO chouette;
ALTER SEQUENCE networks_id_seq OWNED BY networks.id;


CREATE TABLE organisations (
    id bigint NOT NULL,
    name character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    data_format character varying(255) DEFAULT 'neptune'::character varying,
    code character varying(255),
    synced_at timestamp without time zone,
    sso_attributes shared_extensions.hstore
);
ALTER TABLE organisations OWNER TO chouette;
CREATE SEQUENCE organisations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE organisations_id_seq OWNER TO chouette;
ALTER SEQUENCE organisations_id_seq OWNED BY organisations.id;

CREATE TABLE referential_metadata (
    id bigint NOT NULL,
    referential_id bigint,
    line_ids bigint[],
    referential_source_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    periodes daterange[]
);
ALTER TABLE referential_metadata OWNER TO chouette;
CREATE SEQUENCE referential_metadata_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE referential_metadata_id_seq OWNER TO chouette;
ALTER SEQUENCE referential_metadata_id_seq OWNED BY referential_metadata.id;


CREATE TABLE referentials (
    id bigint NOT NULL,
    name character varying(255),
    slug character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    prefix character varying(255),
    projection_type character varying(255),
    time_zone character varying(255),
    bounds character varying(255),
    organisation_id bigint,
    geographical_bounds text,
    user_id bigint,
    user_name character varying(255),
    data_format character varying(255),
    line_referential_id bigint,
    stop_area_referential_id bigint,
    workbench_id bigint,
    archived_at timestamp without time zone,
    created_from_id bigint,
    ready boolean DEFAULT false
);
ALTER TABLE referentials OWNER TO chouette;
CREATE SEQUENCE referentials_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE referentials_id_seq OWNER TO chouette;
ALTER SEQUENCE referentials_id_seq OWNED BY referentials.id;

CREATE TABLE route_sections (
    id bigint NOT NULL,
    departure_id bigint,
    arrival_id bigint,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    input_geometry shared_extensions.geometry(LineString,4326),
    processed_geometry shared_extensions.geometry(LineString,4326),
    distance double precision,
    no_processing boolean,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE route_sections OWNER TO chouette;
CREATE SEQUENCE route_sections_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE route_sections_id_seq OWNER TO chouette;
ALTER SEQUENCE route_sections_id_seq OWNED BY route_sections.id;


CREATE TABLE stop_area_referentials (
    id bigint NOT NULL,
    name character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE stop_area_referentials OWNER TO chouette;
CREATE SEQUENCE stop_area_referentials_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE stop_area_referentials_id_seq OWNER TO chouette;
ALTER SEQUENCE stop_area_referentials_id_seq OWNED BY stop_area_referentials.id;



CREATE TABLE stop_areas (
    id bigint NOT NULL,
    parent_id bigint,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    name character varying(255),
    comment character varying(255),
    area_type character varying(255),
    registration_number character varying(255),
    nearest_topic_name character varying(255),
    fare_code integer,
    longitude numeric(19,16),
    latitude numeric(19,16),
    long_lat_type character varying(255),
    country_code character varying(255),
    street_name character varying(255),
    mobility_restricted_suitability boolean,
    stairs_availability boolean,
    lift_availability boolean,
    int_user_needs integer,
    zip_code character varying(255),
    city_name character varying(255),
    url character varying(255),
    time_zone character varying(255),
    stop_area_referential_id bigint,
    status character varying(255),
    import_xml text,
    deleted_at timestamp without time zone,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    stif_type character varying(255)
);
ALTER TABLE stop_areas OWNER TO chouette;
CREATE SEQUENCE stop_areas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE stop_areas_id_seq OWNER TO chouette;
ALTER SEQUENCE stop_areas_id_seq OWNED BY stop_areas.id;


CREATE TABLE users (
    id bigint NOT NULL,
    email character varying(255) DEFAULT ''::character varying NOT NULL,
    encrypted_password character varying(255) DEFAULT ''::character varying,
    reset_password_token character varying(255),
    reset_password_sent_at timestamp without time zone,
    remember_created_at timestamp without time zone,
    sign_in_count integer DEFAULT 0,
    current_sign_in_at timestamp without time zone,
    last_sign_in_at timestamp without time zone,
    current_sign_in_ip character varying(255),
    last_sign_in_ip character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    organisation_id bigint,
    name character varying(255),
    confirmation_token character varying(255),
    confirmed_at timestamp without time zone,
    confirmation_sent_at timestamp without time zone,
    unconfirmed_email character varying(255),
    failed_attempts integer DEFAULT 0,
    unlock_token character varying(255),
    locked_at timestamp without time zone,
    authentication_token character varying(255),
    invitation_token character varying(255),
    invitation_sent_at timestamp without time zone,
    invitation_accepted_at timestamp without time zone,
    invitation_limit integer,
    invited_by_id bigint,
    invited_by_type character varying(255),
    invitation_created_at timestamp without time zone,
    username character varying(255),
    synced_at timestamp without time zone,
    permissions character varying(255)[]
);
ALTER TABLE users OWNER TO chouette;
CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE users_id_seq OWNER TO chouette;
ALTER SEQUENCE users_id_seq OWNED BY users.id;


CREATE TABLE workbenches (
    id bigint NOT NULL,
    name character varying(255),
    organisation_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    line_referential_id bigint,
    stop_area_referential_id bigint
);
ALTER TABLE workbenches OWNER TO chouette;
CREATE SEQUENCE workbenches_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE workbenches_id_seq OWNER TO chouette;
ALTER SEQUENCE workbenches_id_seq OWNED BY workbenches.id;


-------------------------------------------------------------
-- set constraints

ALTER TABLE ONLY access_links ALTER COLUMN id SET DEFAULT nextval('access_links_id_seq'::regclass);
ALTER TABLE ONLY access_points ALTER COLUMN id SET DEFAULT nextval('access_points_id_seq'::regclass);
ALTER TABLE ONLY companies ALTER COLUMN id SET DEFAULT nextval('companies_id_seq'::regclass);

ALTER TABLE ONLY compliance_check_blocks ALTER COLUMN id SET DEFAULT nextval('compliance_check_blocks_id_seq'::regclass);
ALTER TABLE ONLY compliance_check_resources ALTER COLUMN id SET DEFAULT nextval('compliance_check_resources_id_seq'::regclass);
ALTER TABLE ONLY compliance_check_results ALTER COLUMN id SET DEFAULT nextval('compliance_check_results_id_seq'::regclass);
ALTER TABLE ONLY compliance_check_sets ALTER COLUMN id SET DEFAULT nextval('compliance_check_sets_id_seq'::regclass);
ALTER TABLE ONLY compliance_checks ALTER COLUMN id SET DEFAULT nextval('compliance_checks_id_seq'::regclass);
ALTER TABLE ONLY compliance_control_blocks ALTER COLUMN id SET DEFAULT nextval('compliance_control_blocks_id_seq'::regclass);
ALTER TABLE ONLY compliance_control_sets ALTER COLUMN id SET DEFAULT nextval('compliance_control_sets_id_seq'::regclass);
ALTER TABLE ONLY compliance_controls ALTER COLUMN id SET DEFAULT nextval('compliance_controls_id_seq'::regclass);

ALTER TABLE ONLY connection_links ALTER COLUMN id SET DEFAULT nextval('connection_links_id_seq'::regclass);
ALTER TABLE ONLY group_of_lines ALTER COLUMN id SET DEFAULT nextval('group_of_lines_id_seq'::regclass);
ALTER TABLE ONLY import_messages ALTER COLUMN id SET DEFAULT nextval('import_messages_id_seq'::regclass);
ALTER TABLE ONLY import_resources ALTER COLUMN id SET DEFAULT nextval('import_resources_id_seq'::regclass);
ALTER TABLE ONLY imports ALTER COLUMN id SET DEFAULT nextval('imports_id_seq'::regclass);
ALTER TABLE ONLY line_referentials ALTER COLUMN id SET DEFAULT nextval('line_referentials_id_seq'::regclass);
ALTER TABLE ONLY lines ALTER COLUMN id SET DEFAULT nextval('lines_id_seq'::regclass);
ALTER TABLE ONLY networks ALTER COLUMN id SET DEFAULT nextval('networks_id_seq'::regclass);
ALTER TABLE ONLY organisations ALTER COLUMN id SET DEFAULT nextval('organisations_id_seq'::regclass);
ALTER TABLE ONLY referential_metadata ALTER COLUMN id SET DEFAULT nextval('referential_metadata_id_seq'::regclass);
ALTER TABLE ONLY referentials ALTER COLUMN id SET DEFAULT nextval('referentials_id_seq'::regclass);
ALTER TABLE ONLY stop_area_referentials ALTER COLUMN id SET DEFAULT nextval('stop_area_referentials_id_seq'::regclass);
ALTER TABLE ONLY stop_areas ALTER COLUMN id SET DEFAULT nextval('stop_areas_id_seq'::regclass);
ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);
ALTER TABLE ONLY workbenches ALTER COLUMN id SET DEFAULT nextval('workbenches_id_seq'::regclass);


-----------------------------------------------------------
-- insert init data here !
-----------------------------------------------------------
SELECT pg_catalog.setval('access_links_id_seq', 1, false);
SELECT pg_catalog.setval('access_points_id_seq', 1, false);

INSERT INTO companies VALUES (1, 'STIF:CODIFLIGNE:Operator:011', 2, 'chouette', 'TRANSDEV IDF ECQUEVILLY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '<Operator version="any" id="STIF:CODIFLIGNE:Operator:011">
  <Name>TRANSDEV IDF ECQUEVILLY</Name>
</Operator>', '2017-02-10 09:39:20.66393', NULL);
INSERT INTO companies VALUES (2, 'STIF:CODIFLIGNE:Operator:212', 2, 'chouette', 'TRANSDEV IDF CONFLANS', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '<Operator version="any" id="STIF:CODIFLIGNE:Operator:212">
  <Name>TRANSDEV IDF CONFLANS</Name>
</Operator>', '2017-02-10 09:39:38.614316', '2017-02-10 09:39:38.614316');
INSERT INTO companies VALUES (3, 'STIF:CODIFLIGNE:Operator:005', 2, 'chouette', 'TRANSDEV IDF HOUDAN', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '<Operator version="any" id="STIF:CODIFLIGNE:Operator:005">
  <Name>TRANSDEV IDF HOUDAN</Name>
</Operator>', '2017-02-10 09:38:48.981666', '2017-02-10 09:38:48.981666');
INSERT INTO companies VALUES (4, 'STIF:CODIFLIGNE:Operator:013', 2, 'chouette', 'TRANSDEV IDF RAMBOUILLET', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, '<Operator version="any" id="STIF:CODIFLIGNE:Operator:013">
  <Name>TRANSDEV IDF RAMBOUILLET</Name>
</Operator>', '2017-02-10 09:39:31.131375', '2017-02-10 09:39:31.131375');


SELECT pg_catalog.setval('companies_id_seq', 4, true);
SELECT pg_catalog.setval('connection_links_id_seq', 1, false);
SELECT pg_catalog.setval('group_of_lines_id_seq', 1, false);
SELECT pg_catalog.setval('import_messages_id_seq', 1, false);
SELECT pg_catalog.setval('import_resources_id_seq', 1, false);
SELECT pg_catalog.setval('imports_id_seq', 1, false);

INSERT INTO line_referentials VALUES (1, 'CodifLigne', '2017-02-10 09:37:12.188078', '2017-02-10 09:37:12.188078', 1);

SELECT pg_catalog.setval('line_referentials_id_seq', 1, true);

INSERT INTO lines VALUES (1, 1, 1, 'STIF:CODIFLIGNE:Line:C00108', 5, 'chouette', '9', '9', NULL, 'bus', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, false, '<Line version="any" created="2014-07-16T00:00:00+02:00" changed="2014-07-16T00:00:00+02:00" status="active" id="STIF:CODIFLIGNE:Line:C00108">
  <keyList>
    <KeyValue>
      <!-- Ceci est l accessibilite de la ligne : 0 si false , 1 si true -->
      <Key>Accessibility</Key>
      <Value>0</Value>
    </KeyValue>
  </keyList>
  <Name>9</Name>
  <ShortName>9</ShortName>
  <TransportMode>bus</TransportMode>
  <TransportSubmode>
    <BusSubmode/>
  </TransportSubmode>
  <!-- Ceci est le code technique -->
  <PrivateCode>011011009</PrivateCode>
  <OperatorRef version="any" ref="STIF:CODIFLIGNE:Operator:011"/>
  <!-- Type of line null ou égal à seasonal -->
  <TypeOfLineRef version="any" ref="null"/>
  <Presentation>
    <infoLinks/>
  </Presentation>
</Line>', NULL, '{2,3}', '2017-02-10 09:45:19.680043', NULL);
INSERT INTO lines VALUES (2, 1, 1, 'STIF:CODIFLIGNE:Line:C00109', 5, 'chouette', '10', '10', NULL, 'bus', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, false, '<Line version="any" created="2014-07-16T00:00:00+02:00" changed="2014-07-16T00:00:00+02:00" status="active" id="STIF:CODIFLIGNE:Line:C00109">
  <keyList>
    <KeyValue>
      <!-- Ceci est l accessibilite de la ligne : 0 si false , 1 si true -->
      <Key>Accessibility</Key>
      <Value>0</Value>
    </KeyValue>
  </keyList>
  <Name>10</Name>
  <ShortName>10</ShortName>
  <TransportMode>bus</TransportMode>
  <TransportSubmode>
    <BusSubmode/>
  </TransportSubmode>
  <!-- Ceci est le code technique -->
  <PrivateCode>011011010</PrivateCode>
  <OperatorRef version="any" ref="STIF:CODIFLIGNE:Operator:011"/>
  <!-- Type of line null ou égal à seasonal -->
  <TypeOfLineRef version="any" ref="null"/>
  <Presentation>
    <infoLinks/>
  </Presentation>
</Line>', NULL, '{2,3}', '2017-02-10 09:45:19.683374', '2017-02-10 09:45:19.683374');
INSERT INTO lines VALUES (3, 1, 4, 'STIF:CODIFLIGNE:Line:C00163', 5, 'chouette', '01', '01', NULL, 'bus', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, false, '<Line version="any" created="2014-07-16T00:00:00+02:00" changed="2014-07-16T00:00:00+02:00" status="active" id="STIF:CODIFLIGNE:Line:C00163">
  <keyList>
    <KeyValue>
      <!-- Ceci est l accessibilite de la ligne : 0 si false , 1 si true -->
      <Key>Accessibility</Key>
      <Value>0</Value>
    </KeyValue>
  </keyList>
  <Name>01</Name>
  <ShortName>01</ShortName>
  <TransportMode>bus</TransportMode>
  <TransportSubmode>
    <BusSubmode/>
  </TransportSubmode>
  <!-- Ceci est le code technique -->
  <PrivateCode>013013001</PrivateCode>
  <OperatorRef version="any" ref="STIF:CODIFLIGNE:Operator:013"/>
  <!-- Type of line null ou égal à seasonal -->
  <TypeOfLineRef version="any" ref="null"/>
  <Presentation>
    <infoLinks/>
  </Presentation>
</Line>', NULL, NULL, '2017-02-10 09:45:25.652603', '2017-02-10 09:45:25.652603');
INSERT INTO lines VALUES (4, 1, 4, 'STIF:CODIFLIGNE:Line:C00164', 5, 'chouette', '03', '03', NULL, 'bus', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, false, '<Line version="any" created="2014-07-16T00:00:00+02:00" changed="2014-07-16T00:00:00+02:00" status="active" id="STIF:CODIFLIGNE:Line:C00164">
  <keyList>
    <KeyValue>
      <!-- Ceci est l accessibilite de la ligne : 0 si false , 1 si true -->
      <Key>Accessibility</Key>
      <Value>0</Value>
    </KeyValue>
  </keyList>
  <Name>03</Name>
  <ShortName>03</ShortName>
  <TransportMode>bus</TransportMode>
  <TransportSubmode>
    <BusSubmode/>
  </TransportSubmode>
  <!-- Ceci est le code technique -->
  <PrivateCode>013013003</PrivateCode>
  <OperatorRef version="any" ref="STIF:CODIFLIGNE:Operator:013"/>
  <!-- Type of line null ou égal à seasonal -->
  <TypeOfLineRef version="any" ref="null"/>
  <Presentation>
    <infoLinks/>
  </Presentation>
</Line>', NULL, NULL, '2017-02-10 09:45:25.656208', '2017-02-10 09:45:25.656208');
INSERT INTO lines VALUES (5, 1, 4, 'STIF:CODIFLIGNE:Line:C00165', 5, 'chouette', '04', '04', NULL, 'bus', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, false, '<Line version="any" created="2014-07-16T00:00:00+02:00" changed="2014-07-16T00:00:00+02:00" status="active" id="STIF:CODIFLIGNE:Line:C00165">
  <keyList>
    <KeyValue>
      <!-- Ceci est l accessibilite de la ligne : 0 si false , 1 si true -->
      <Key>Accessibility</Key>
      <Value>0</Value>
    </KeyValue>
  </keyList>
  <Name>04</Name>
  <ShortName>04</ShortName>
  <TransportMode>bus</TransportMode>
  <TransportSubmode>
    <BusSubmode/>
  </TransportSubmode>
  <!-- Ceci est le code technique -->
  <PrivateCode>013013004</PrivateCode>
  <OperatorRef version="any" ref="STIF:CODIFLIGNE:Operator:013"/>
  <!-- Type of line null ou égal à seasonal -->
  <TypeOfLineRef version="any" ref="null"/>
  <Presentation>
    <infoLinks/>
  </Presentation>
</Line>', NULL, NULL, '2017-02-10 09:45:25.659857', '2017-02-10 09:45:25.659857');
INSERT INTO lines VALUES (6, 1, 4, 'STIF:CODIFLIGNE:Line:C00166', 5, 'chouette', '05', '05', NULL, 'bus', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, false, '<Line version="any" created="2014-07-16T00:00:00+02:00" changed="2014-07-16T00:00:00+02:00" status="active" id="STIF:CODIFLIGNE:Line:C00166">
  <keyList>
    <KeyValue>
      <!-- Ceci est l accessibilite de la ligne : 0 si false , 1 si true -->
      <Key>Accessibility</Key>
      <Value>0</Value>
    </KeyValue>
  </keyList>
  <Name>05</Name>
  <ShortName>05</ShortName>
  <TransportMode>bus</TransportMode>
  <TransportSubmode>
    <BusSubmode/>
  </TransportSubmode>
  <!-- Ceci est le code technique -->
  <PrivateCode>013013005</PrivateCode>
  <OperatorRef version="any" ref="STIF:CODIFLIGNE:Operator:013"/>
  <!-- Type of line null ou égal à seasonal -->
  <TypeOfLineRef version="any" ref="null"/>
  <Presentation>
    <infoLinks/>
  </Presentation>
</Line>', NULL, NULL, '2017-02-10 09:45:25.663423', '2017-02-10 09:45:25.663423');
INSERT INTO lines VALUES (7, 1, 4, 'STIF:CODIFLIGNE:Line:C00168', 5, 'chouette', '08', '08', NULL, 'bus', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, false, '<Line version="any" created="2014-07-16T00:00:00+02:00" changed="2014-07-16T00:00:00+02:00" status="active" id="STIF:CODIFLIGNE:Line:C00168">
  <keyList>
    <KeyValue>
      <!-- Ceci est l accessibilite de la ligne : 0 si false , 1 si true -->
      <Key>Accessibility</Key>
      <Value>0</Value>
    </KeyValue>
  </keyList>
  <Name>08</Name>
  <ShortName>08</ShortName>
  <TransportMode>bus</TransportMode>
  <TransportSubmode>
    <BusSubmode/>
  </TransportSubmode>
  <!-- Ceci est le code technique -->
  <PrivateCode>013013008</PrivateCode>
  <OperatorRef version="any" ref="STIF:CODIFLIGNE:Operator:013"/>
  <!-- Type of line null ou égal à seasonal -->
  <TypeOfLineRef version="any" ref="null"/>
  <Presentation>
    <infoLinks/>
  </Presentation>
</Line>', NULL, NULL, '2017-02-10 09:45:25.758591', '2017-02-10 09:45:25.758591');
INSERT INTO lines VALUES (8, 1, 4, 'STIF:CODIFLIGNE:Line:C00171', 5, 'chouette', '11', '11', NULL, 'bus', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1, false, '<Line version="any" created="2014-07-16T00:00:00+02:00" changed="2014-07-16T00:00:00+02:00" status="active" id="STIF:CODIFLIGNE:Line:C00171">
  <keyList>
    <KeyValue>
      <!-- Ceci est l accessibilite de la ligne : 0 si false , 1 si true -->
      <Key>Accessibility</Key>
      <Value>0</Value>
    </KeyValue>
  </keyList>
  <Name>11</Name>
  <ShortName>11</ShortName>
  <TransportMode>bus</TransportMode>
  <TransportSubmode>
    <BusSubmode/>
  </TransportSubmode>
  <!-- Ceci est le code technique -->
  <PrivateCode>013013011</PrivateCode>
  <OperatorRef version="any" ref="STIF:CODIFLIGNE:Operator:013"/>
  <!-- Type of line null ou égal à seasonal -->
  <TypeOfLineRef version="any" ref="null"/>
  <Presentation>
    <infoLinks/>
  </Presentation>
</Line>', NULL, NULL, '2017-02-10 09:45:25.836009', '2017-02-10 09:45:25.836009');


SELECT pg_catalog.setval('lines_id_seq', 8, true);

INSERT INTO networks VALUES (1, 'STIF:CODIFLIGNE:PTNetwork:117', 2, 'chouette', NULL, NULL, 'Veolia Ecquevilly', NULL, NULL, NULL, NULL, NULL, '<Network version="any" changed="2009-12-02T00:00:00Z" id="STIF:CODIFLIGNE:PTNetwork:117">
  <Name>Veolia Ecquevilly</Name>
  <members>
    <LineRef ref="STIF:CODIFLIGNE:Line:C00108"/>
    <LineRef ref="STIF:CODIFLIGNE:Line:C00109"/>
  </members>
</Network>', 1, '2017-02-10 09:45:19.627071', NULL);

SELECT pg_catalog.setval('networks_id_seq', 1, true);

INSERT INTO organisations VALUES (1, 'STIF', '2017-02-10 09:37:12.285489', '2017-02-10 09:44:28.887246', 'neptune', 'STIF', '2017-02-10 09:44:28.720673', '"functional_scope"=>"[]"');
INSERT INTO organisations VALUES (2, 'Cityway', '2017-02-10 10:05:53.212327', '2017-02-10 10:05:53.212327', 'neptune', 'CITYWAY', '2017-02-10 10:05:53.21035', '"functional_scope"=>"[\"STIF:CODIFLIGNE:Line:C00108\"]"');

SELECT pg_catalog.setval('organisations_id_seq', 2, true);

INSERT INTO referential_metadata VALUES (1, 1, '{1,2,3,4,5,6,7,8}', NULL, '2017-02-10 10:06:41.901097', '2017-02-10 10:06:41.901097', '{"[2017-02-10,2017-12-11)"}');
INSERT INTO referential_metadata VALUES (2, 2, '{1,2,3,4,5,6,7,8}', NULL, '2017-02-10 10:06:41.901097', '2017-02-10 10:06:41.901097', '{"[2017-02-10,2017-12-11)"}');

SELECT pg_catalog.setval('referential_metadata_id_seq', 2, true);

INSERT INTO referentials VALUES (1, 'test', 'chouette_gui', '2017-02-10 10:06:27.034272', '2017-02-10 10:06:27.034272', 'test', '', 'Paris', 'SRID=4326;POLYGON((-5.2 42.25,-5.2 51.1,8.23 51.1,8.23 42.25,-5.2 42.25))', 2, NULL, 2, 'Etienne Michel', 'neptune', 1, 1, 1, NULL, NULL, true);
INSERT INTO referentials VALUES (2, 'checkPoints', 'iev_check_points', '2017-02-10 10:06:27.034272', '2017-02-10 10:06:27.034272', 'test', '', 'Paris', 'SRID=4326;POLYGON((-5.2 42.25,-5.2 51.1,8.23 51.1,8.23 42.25,-5.2 42.25))', 2, NULL, 2, 'Etienne Michel', 'neptune', 1, 1, 1, NULL, NULL, true);

SELECT pg_catalog.setval('referentials_id_seq', 2, true);

INSERT INTO stop_area_referentials VALUES (1, 'Reflex', '2017-02-10 09:37:12.116926', '2017-02-10 09:37:12.116926');

SELECT pg_catalog.setval('stop_area_referentials_id_seq', 1, true);


INSERT INTO stop_areas VALUES (2825, 42239, 'FR:78402:ZDE:32798:STIF', 1, 'chouette', 'LISERETTES', NULL, 'zder', NULL, NULL, NULL, 1.7955805718439200, 48.9523196160323000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32798" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32798:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009396:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>LISERETTES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611793.013 6873157.023</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32798">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32798">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.828119', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (2855, 42268, 'FR:78217:ZDE:32522:STIF', 1, 'chouette', 'LE CHATEAU', NULL, 'zder', NULL, NULL, NULL, 1.8126173239686100, 48.9533412676803000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32522" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32522:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009053:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>LE CHATEAU</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613042.332 6873251.726</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32522">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32522">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.767586', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (20009, 56991, 'FR:78217:ZDE:32526:STIF', 1, 'chouette', 'LE FOURNEAU', NULL, 'zder', NULL, NULL, NULL, 1.8090855692680200, 48.9424962532744000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32526" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32526:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009055:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>LE FOURNEAU</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612765.516 6872049.746</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32526">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32526">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.876238', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (20019, 57001, 'FR:78217:ZDE:32783:STIF', 1, 'chouette', 'BOIS DE L''AULNE', NULL, 'zder', NULL, NULL, NULL, 1.7942321823644500, 48.9427262657198000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32783" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32783:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009382:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>BOIS DE L''AULNE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611677.98 6872091.833</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32783">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32783">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.317781', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (20056, 57033, 'FR:78217:ZDE:32516:STIF', 1, 'chouette', 'ALLEE DE PINCELOUP', NULL, 'zder', NULL, NULL, NULL, 1.8084105849672000, 48.9439162501291000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32516" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32516:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009047:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>ALLEE DE PINCELOUP</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612718.46 6872208.383</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32516">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32516">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.463925', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (56620, NULL, 'FR:78217:LDA:64800:STIF', 1, 'chouette', 'Brouillard', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="64800" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:64800:STIF">
       <Name>Brouillard</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:64800">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:64800">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 09:59:06.133825', '2017-03-23 09:59:06.133825', NULL);
INSERT INTO stop_areas VALUES (111188, 147677, 'FR:78217:ZDE:50009035:STIF', 1, 'chouette', 'BOULEVARD DE BRUXELLES', NULL, 'zdep', NULL, NULL, NULL, 1.8380508823024100, 48.9703496294287000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="445780" created="2015-02-03T07:02:47.0Z" changed="2015-02-03T07:02:47.0Z" id="FR:78217:ZDE:50009035:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78029:ZDE:32505:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>BOULEVARD DE BRUXELLES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614932.648 6875115.24</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009035">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009035">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:55.792631', '2017-03-23 13:35:35.2967', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111190, 147679, 'FR:78217:ZDE:50009037:STIF', 1, 'chouette', 'BOULEVARD DE LA PAIX', NULL, 'zdep', NULL, NULL, NULL, 1.8359592073884300, 48.9698496280250000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="445795" created="2015-02-03T07:02:56.0Z" changed="2015-02-03T07:02:56.0Z" id="FR:78217:ZDE:50009037:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32507:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>BOULEVARD DE LA PAIX</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614778.706 6875061.898</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009037">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009037">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:55.883458', '2017-03-23 13:35:35.339891', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111193, 147682, 'FR:78217:ZDE:50009041:STIF', 1, 'chouette', 'PLACE MARECHAL JUIN', NULL, 'zdep', NULL, NULL, NULL, 1.8369392348705200, 48.9715229579612000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443423" created="2015-02-03T06:02:03.0Z" changed="2015-02-03T06:02:03.0Z" id="FR:78217:ZDE:50009041:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32514:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011180</Value>
        </KeyValue>
       </keyList>
       <Name>PLACE MARECHAL JUIN</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614853.191 6875246.906</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009041">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009041">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.052458', '2017-03-23 13:35:35.408496', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111195, 147684, 'FR:78217:ZDE:50009042:STIF', 1, 'chouette', 'LES DOLMENS', NULL, 'zdep', NULL, NULL, NULL, 1.8328142105013700, 48.9702879694420000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443388" created="2015-02-03T06:02:55.0Z" changed="2015-02-03T06:02:55.0Z" id="FR:78217:ZDE:50009042:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32510:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>LES DOLMENS</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614549.193 6875114.038</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009042">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009042">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.1419', '2017-03-23 13:35:35.453903', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111202, 147690, 'FR:78217:ZDE:50009047:STIF', 1, 'chouette', 'ALLEE DE PINCELOUP', NULL, 'zdep', NULL, NULL, NULL, 1.8084105849672000, 48.9439162501291000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443367" created="2015-02-03T06:02:50.0Z" changed="2015-02-03T06:02:50.0Z" id="FR:78217:ZDE:50009047:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32516:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>ALLEE DE PINCELOUP</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612718.46 6872208.383</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009047">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009047">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.497986', '2017-03-23 13:35:35.643903', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111204, 147691, 'FR:78217:ZDE:50009049:STIF', 1, 'chouette', 'AVENUE DE LA GARE', NULL, 'zdep', NULL, NULL, NULL, 1.8076239920404600, 48.9580212851215000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443374" created="2015-02-03T06:02:51.0Z" changed="2015-02-03T06:02:51.0Z" id="FR:78217:ZDE:50009049:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32518:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>AVENUE DE LA GARE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612684.533 6873777.619</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009049">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009049">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.598028', '2017-03-23 13:35:35.701831', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111205, 147692, 'FR:78217:ZDE:50009050:STIF', 1, 'chouette', 'CARREFOUR ST MARTIN', NULL, 'zdep', NULL, NULL, NULL, 1.8195940225965100, 48.9537812663945000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443380" created="2015-02-03T06:02:53.0Z" changed="2015-02-03T06:02:53.0Z" id="FR:78217:ZDE:50009050:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32519:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>CARREFOUR ST MARTIN</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613553.962 6873292.99</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009050">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009050">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.653088', '2017-03-23 13:35:35.733846', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111207, 147693, 'FR:78217:ZDE:50009052:STIF', 1, 'chouette', 'LE CHATEAU', NULL, 'zdep', NULL, NULL, NULL, 1.8133756580597400, 48.9533662680704000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443339" created="2015-02-03T06:02:43.0Z" changed="2015-02-03T06:02:43.0Z" id="FR:78217:ZDE:50009052:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32521:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>LE CHATEAU</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613097.906 6873253.671</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009052">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009052">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.753004', '2017-03-23 13:35:35.810086', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111209, 147694, 'FR:78217:ZDE:50009054:STIF', 1, 'chouette', 'CHEMIN NEUF', NULL, 'zdep', NULL, NULL, NULL, 1.8162940080552900, 48.9524979333468000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443347" created="2015-02-03T06:02:45.0Z" changed="2015-02-03T06:02:45.0Z" id="FR:78217:ZDE:50009054:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32523:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>CHEMIN NEUF</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613310.167 6873153.911</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009054">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009054">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.864881', '2017-03-23 13:35:35.864709', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111211, 147696, 'FR:78217:ZDE:50009057:STIF', 1, 'chouette', 'LA FALAISE', NULL, 'zdep', NULL, NULL, NULL, 1.8215623766055400, 48.9563379378776000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443318" created="2015-02-03T06:02:38.0Z" changed="2015-02-03T06:02:38.0Z" id="FR:78217:ZDE:50009057:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32527:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>LA FALAISE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613702.345 6873575.12</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009057">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009057">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.964578', '2017-03-23 13:35:35.921707', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111213, 147697, 'FR:78217:ZDE:50009059:STIF', 1, 'chouette', 'LES BICHES', NULL, 'zdep', NULL, NULL, NULL, 1.8118139867675400, 48.9526329360882000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443313" created="2015-02-03T06:02:37.0Z" changed="2015-02-03T06:02:37.0Z" id="FR:78217:ZDE:50009059:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32529:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>LES BICHES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612982.319 6873173.85</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009059">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009059">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:57.052144', '2017-03-23 13:35:35.966082', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111216, 147699, 'FR:78217:ZDE:50009065:STIF', 1, 'chouette', 'POTEAU', NULL, 'zdep', NULL, NULL, NULL, 1.8300191010731300, 48.9566546048684000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443287" created="2015-02-03T06:02:31.0Z" changed="2015-02-03T06:02:31.0Z" id="FR:78217:ZDE:50009065:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32533:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011512</Value>
        </KeyValue>
       </keyList>
       <Name>POTEAU</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614322.112 6873601.122</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009065">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009065">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:57.229441', '2017-03-23 13:35:36.021402', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111215, 147699, 'FR:78217:ZDE:50009066:STIF', 1, 'chouette', 'POTEAU', NULL, 'zdep', NULL, NULL, NULL, 1.8301257677224300, 48.9567112696982000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443289" created="2015-02-03T06:02:31.0Z" changed="2015-02-03T06:02:31.0Z" id="FR:78217:ZDE:50009066:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32532:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011512</Value>
        </KeyValue>
       </keyList>
       <Name>POTEAU</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614330.016 6873607.307</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009066">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009066">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:57.172239', '2017-03-23 13:35:36.035501', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (26194, 62311, 'FR:78402:ZDE:32790:STIF', 1, 'chouette', 'CHAUFFOUR', NULL, 'zder', NULL, NULL, NULL, 1.7987106032497500, 48.9544312871310000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32790" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32790:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009390:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CHAUFFOUR</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612025.799 6873388.332</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32790">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32790">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.550948', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (26241, 62360, 'FR:78217:ZDE:32528:STIF', 1, 'chouette', 'LA FALAISE', NULL, 'zder', NULL, NULL, NULL, 1.8215273921141900, 48.9563896027813000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32528" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32528:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009058:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>LA FALAISE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613699.869 6873580.903</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32528">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32528">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.974587', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (26263, 62386, 'FR:78217:ZDE:32530:STIF', 1, 'chouette', 'Mairie', NULL, 'zder', NULL, NULL, NULL, 1.8116042808075700, 48.9572862999147000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="819294" created="2014-12-29T03:12:51.0Z" changed="2016-11-07T04:11:55.0Z" id="FR:78217:ZDE:32530:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009061:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50000881:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>052052080</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612974.75 6873691.5</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32530">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32530">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:10:16.581352', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (26351, 62468, 'FR:78402:ZDE:13657:STIF', 1, 'chouette', 'Libération', NULL, 'zder', NULL, NULL, NULL, 1.8007240306059900, 48.9596196742322000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="795044" created="2014-12-29T03:12:51.0Z" changed="2016-08-29T10:08:40.0Z" id="FR:78402:ZDE:13657:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50000917:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009229:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011180</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>052052080</Value>
        </KeyValue>
       </keyList>
       <Name>Libération</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612182.0 6873963.0</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:13657">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:13657">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 11:50:48.818913', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (111382, 147800, 'FR:78402:ZDE:50009383:STIF', 1, 'chouette', 'BOIS DE L''AULNE', NULL, 'zdep', NULL, NULL, NULL, 1.7941171745678400, 48.9425629280426000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444528" created="2015-02-03T06:02:27.0Z" changed="2015-02-03T06:02:27.0Z" id="FR:78402:ZDE:50009383:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32782:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>BOIS DE L''AULNE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611669.279 6872073.8</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009383">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009383">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.295077', '2017-03-23 13:35:40.508681', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111384, 147801, 'FR:78217:ZDE:50009384:STIF', 1, 'chouette', 'BROUILLARD', NULL, 'zdep', NULL, NULL, NULL, 1.7875870622303600, 48.9263228997569000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444534" created="2015-02-03T06:02:30.0Z" changed="2015-02-03T06:02:30.0Z" id="FR:78217:ZDE:50009384:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32784:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>BROUILLARD</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611163.259 6870275.399</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009384">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009384">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.401813', '2017-03-23 13:35:40.538829', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111386, 147802, 'FR:78402:ZDE:50009388:STIF', 1, 'chouette', 'PLACE CDT GRIMBLOT', NULL, 'zdep', NULL, NULL, NULL, 1.7949872805994600, 48.9607563019472000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444550" created="2015-02-03T06:02:37.0Z" changed="2015-02-03T06:02:37.0Z" id="FR:78402:ZDE:50009388:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32788:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>PLACE CDT GRIMBLOT</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611763.883 6874095.78</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009388">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009388">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.493125', '2017-03-23 13:35:40.59622', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111388, 147803, 'FR:78402:ZDE:50009390:STIF', 1, 'chouette', 'CHAUFFOUR', NULL, 'zdep', NULL, NULL, NULL, 1.7987106032497500, 48.9544312871310000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444557" created="2015-02-03T06:02:39.0Z" changed="2015-02-03T06:02:39.0Z" id="FR:78402:ZDE:50009390:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32790:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CHAUFFOUR</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612025.799 6873388.332</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009390">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009390">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.582791', '2017-03-23 13:35:40.687607', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111390, 147804, 'FR:78402:ZDE:50009392:STIF', 1, 'chouette', 'CHEMIN DE L''EPINE', NULL, 'zdep', NULL, NULL, NULL, 1.7937005476704400, 48.9498796149550000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444565" created="2015-02-03T06:02:43.0Z" changed="2015-02-03T06:02:43.0Z" id="FR:78402:ZDE:50009392:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32792:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CHEMIN DE L''EPINE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611651.193 6872887.817</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009392">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009392">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.670781', '2017-03-23 13:35:40.74287', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111391, 147805, 'FR:78402:ZDE:50009393:STIF', 1, 'chouette', 'FONTAINE', NULL, 'zdep', NULL, NULL, NULL, 1.7892689237877700, 48.9609196302789000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444571" created="2015-02-03T06:02:45.0Z" changed="2015-02-03T06:02:45.0Z" id="FR:78402:ZDE:50009393:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32793:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>FONTAINE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611345.472 6874120.346</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009393">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009393">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.714276', '2017-03-23 13:35:40.790418', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111392, 147806, 'FR:78402:ZDE:50009394:STIF', 1, 'chouette', 'LA VILLENEUVE', NULL, 'zdep', NULL, NULL, NULL, 1.7938288733818300, 48.9474412752481000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444578" created="2015-02-03T06:02:48.0Z" changed="2015-02-03T06:02:48.0Z" id="FR:78402:ZDE:50009394:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32794:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>LA VILLENEUVE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611656.449 6872616.551</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009394">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009394">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.760859', '2017-03-23 13:35:40.853904', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111395, 147807, 'FR:78402:ZDE:50009397:STIF', 1, 'chouette', 'LISERETTES', NULL, 'zdep', NULL, NULL, NULL, 1.7955739011500000, 48.9522762814456000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444584" created="2015-02-03T06:02:51.0Z" changed="2015-02-03T06:02:51.0Z" id="FR:78402:ZDE:50009397:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32799:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>LISERETTES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611792.451 6873152.212</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009397">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009397">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.90421', '2017-03-23 13:35:40.906764', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (50870, 66829, 'FR:78217:ZDE:35752:STIF', 1, 'chouette', 'Gare d''Epône', NULL, 'zder', NULL, NULL, NULL, 1.8086383194034700, 48.9626079268849000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="806125" created="2014-12-29T12:12:00.0Z" changed="2016-10-12T10:10:03.0Z" id="FR:78217:ZDE:35752:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50010057:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>005005015</Value>
        </KeyValue>
       </keyList>
       <Name>Gare d''Epône</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612766.5 6874286.5</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:35752">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:35752">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 11:49:03.442182', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (31692, 66969, 'FR:78217:ZDE:32509:STIF', 1, 'chouette', 'BOUT DU MONDE', NULL, 'zder', NULL, NULL, NULL, 1.8298075100030400, 48.9683646251586000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32509" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32509:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009040:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>BOUT DU MONDE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614325.916 6874903.431</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32509">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32509">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:55.977016', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (42267, 42266, 'FR:78217:ZDL:53998:STIF', 1, 'chouette', 'Les Biches', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53998" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53998:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008593:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Les Biches</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53998">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53998">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32529:STIF" version="32529"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.986448', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (42239, 42238, 'FR:78402:ZDL:54103:STIF', 1, 'chouette', 'Liserettes', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="54103" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDL:54103:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:50008794:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Liserettes</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:54103">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:54103">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:32798:STIF" version="32798"/>
        <QuayRef ref="FR:78402:ZDE:32799:STIF" version="32799"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:29.609905', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (42268, 42266, 'FR:78217:ZDL:53994:STIF', 1, 'chouette', 'Le Château', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53994" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53994:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008588:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Le Château</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53994">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53994">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32521:STIF" version="32521"/>
        <QuayRef ref="FR:78217:ZDE:32522:STIF" version="32522"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.775742', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (62288, 62287, 'FR:78217:ZDL:53993:STIF', 1, 'chouette', 'Carrefour Saint Martin', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53993" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53993:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008587:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Carrefour Saint Martin</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53993">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53993">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32519:STIF" version="32519"/>
        <QuayRef ref="FR:78217:ZDE:32520:STIF" version="32520"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.730427', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (62366, 62365, 'FR:78217:ZDL:54000:STIF', 1, 'chouette', 'Poteau', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="54000" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:54000:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008597:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Poteau</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:54000">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:54000">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32532:STIF" version="32532"/>
        <QuayRef ref="FR:78217:ZDE:32533:STIF" version="32533"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:24.100334', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (62386, 62385, 'FR:78217:ZDL:43983:STIF', 1, 'chouette', 'Mairie', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="43983" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:43983:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50000635:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008594:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:43983">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:43983">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:13660:STIF" version="819300"/>
        <QuayRef ref="FR:78217:ZDE:32530:STIF" version="819294"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:11:32.338497', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (62495, 62494, 'FR:78402:ZDL:54098:STIF', 1, 'chouette', 'Place Commandant Grimblot', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="54098" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDL:54098:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:50008789:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Place Commandant Grimblot</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:54098">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:54098">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:32788:STIF" version="32788"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:29.376452', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (56712, NULL, 'FR:78217:LDA:64848:STIF', 1, 'chouette', 'Canada', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="64848" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:64848:STIF">
       <Name>Canada</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:64848">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:64848">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 09:59:10.276987', '2017-03-23 09:59:10.276987', NULL);
INSERT INTO stop_areas VALUES (56990, NULL, 'FR:78217:LDA:64996:STIF', 1, 'chouette', 'Le Fourneau', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="64996" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:64996:STIF">
       <Name>Le Fourneau</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:64996">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:64996">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 09:59:22.072541', '2017-03-23 09:59:22.072541', NULL);
INSERT INTO stop_areas VALUES (56991, 56990, 'FR:78217:ZDL:53996:STIF', 1, 'chouette', 'Le Fourneau', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53996" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53996:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008590:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Le Fourneau</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53996">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53996">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32526:STIF" version="32526"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.879337', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (57000, NULL, 'FR:78217:LDA:65001:STIF', 1, 'chouette', 'Bois de l''Aulnz', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65001" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65001:STIF">
       <Name>Bois de l''Aulnz</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65001">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65001">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 09:59:22.473283', '2017-03-23 09:59:22.473283', NULL);
INSERT INTO stop_areas VALUES (57032, NULL, 'FR:78217:LDA:65017:STIF', 1, 'chouette', 'Allée de Pinceloup', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65017" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65017:STIF">
       <Name>Allée de Pinceloup</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65017">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65017">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 09:59:23.80477', '2017-03-23 09:59:23.80477', NULL);
INSERT INTO stop_areas VALUES (57033, 57032, 'FR:78217:ZDL:53991:STIF', 1, 'chouette', 'Allée de Pinceloup', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53991" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53991:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008585:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Allée de Pinceloup</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53991">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53991">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32516:STIF" version="32516"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.631311', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (71575, 71574, 'FR:78217:ZDL:420191:STIF', 1, 'chouette', 'Emile Sergent', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="729345" created="2015-10-07T04:10:59.0Z" changed="2015-10-07T04:10:59.0Z" id="FR:78217:ZDL:420191:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008596:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Emile Sergent</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:420191">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:420191">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32525:STIF" version="32525"/>
        <QuayRef ref="FR:78217:ZDE:32524:STIF" version="729344"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:16:23.046721', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (94846, 141689, 'FR:78217:ZDE:50010057:STIF', 1, 'chouette', 'Gare d''Epône', NULL, 'zdep', NULL, NULL, NULL, 1.8086385749719200, 48.9626063106196000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:22" version="805429" created="2015-03-26T09:03:28.0Z" changed="2016-10-11T09:10:03.0Z" id="FR:78217:ZDE:50010057:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:35752:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>005005015</Value>
        </KeyValue>
       </keyList>
       <Name>Gare d''Epône</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612766.516 6874286.32</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50010057">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50010057">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:49:03.472911', '2017-03-23 13:27:37.291655', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (94851, 141692, 'FR:78217:ZDE:50094817:STIF', 1, 'chouette', 'Gare d''Epône Mézières', NULL, 'zdep', NULL, NULL, NULL, 1.8083199035563100, 48.9630722105784000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:81" version="743658" created="2015-03-30T02:03:38.0Z" changed="2016-01-05T09:01:45.0Z" id="FR:78217:ZDE:50094817:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:18305:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057057081</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057057082</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057318208</Value>
        </KeyValue>
       </keyList>
       <Name>Gare d''Epône Mézières</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612743.966 6874338.477</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50094817">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50094817">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:49:03.724617', '2017-03-23 13:27:37.36882', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (94852, 141693, 'FR:78217:ZDE:50097974:STIF', 1, 'chouette', 'GARE D''EPONE MEZIERES', NULL, 'zdep', NULL, NULL, NULL, 1.8086450611031900, 48.9633073081647000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:76" version="714158" created="2015-06-15T09:06:29.0Z" changed="2015-06-26T09:06:39.0Z" id="FR:78217:ZDE:50097974:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:418584:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800854541</Value>
        </KeyValue>
       </keyList>
       <Name>GARE D''EPONE MEZIERES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612768.167 6874364.259</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50097974">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50097974">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:49:03.78181', '2017-03-23 13:27:37.422616', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (62287, NULL, 'FR:78217:LDA:65163:STIF', 1, 'chouette', 'Carrefour Saint Martin', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65163" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65163:STIF">
       <Name>Carrefour Saint Martin</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65163">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65163">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:02:37.790111', '2017-03-23 10:02:37.790111', NULL);
INSERT INTO stop_areas VALUES (62310, NULL, 'FR:78402:LDA:65175:STIF', 1, 'chouette', 'Chauffour', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="65175" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:LDA:65175:STIF">
       <Name>Chauffour</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65175">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65175">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:02:38.43537', '2017-03-23 10:02:38.43537', NULL);
INSERT INTO stop_areas VALUES (62359, NULL, 'FR:78217:LDA:65202:STIF', 1, 'chouette', 'La Falaise', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65202" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65202:STIF">
       <Name>La Falaise</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65202">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65202">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:02:39.843201', '2017-03-23 10:02:39.843201', NULL);
INSERT INTO stop_areas VALUES (62365, NULL, 'FR:78217:LDA:65206:STIF', 1, 'chouette', 'Poteau', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65206" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65206:STIF">
       <Name>Poteau</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65206">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65206">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:02:40.001397', '2017-03-23 10:02:40.001397', NULL);
INSERT INTO stop_areas VALUES (62385, NULL, 'FR:78217:LDA:65217:STIF', 1, 'chouette', 'Mairie', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65217" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65217:STIF">
       <Name>Mairie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65217">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65217">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:02:40.534417', '2017-03-23 10:02:40.534417', NULL);
INSERT INTO stop_areas VALUES (62399, NULL, 'FR:78402:LDA:65223:STIF', 1, 'chouette', 'Avenue de la Gare', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="65223" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:LDA:65223:STIF">
       <Name>Avenue de la Gare</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65223">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65223">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:02:41.037394', '2017-03-23 10:02:41.037394', NULL);
INSERT INTO stop_areas VALUES (62467, NULL, 'FR:78402:LDA:65258:STIF', 1, 'chouette', 'Libération', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="65258" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:LDA:65258:STIF">
       <Name>Libération</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65258">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65258">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:02:42.880706', '2017-03-23 10:02:42.880706', NULL);
INSERT INTO stop_areas VALUES (62494, NULL, 'FR:78402:LDA:65272:STIF', 1, 'chouette', 'Place Commandant Grimblot', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="65272" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:LDA:65272:STIF">
       <Name>Place Commandant Grimblot</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65272">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65272">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:02:43.615513', '2017-03-23 10:02:43.615513', NULL);
INSERT INTO stop_areas VALUES (96984, 142461, 'FR:78402:ZDE:50000917:STIF', 1, 'chouette', 'Libération', NULL, 'zdep', NULL, NULL, NULL, 1.8007124759776900, 48.9596232459550000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:58" version="824622" created="2015-01-31T09:01:33.0Z" changed="2017-02-10T09:02:40.0Z" id="FR:78402:ZDE:50000917:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:13657:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>052052080</Value>
        </KeyValue>
       </keyList>
       <Name>Libération</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612181.16 6873963.41</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50000917">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50000917">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:50:48.852392', '2017-03-23 13:28:33.828163', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (96989, 142463, 'FR:78402:ZDE:50010290:STIF', 1, 'chouette', 'Libération', NULL, 'zdep', NULL, NULL, NULL, 1.8014149803144700, 48.9589973240387000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:22" version="805572" created="2015-03-26T09:03:10.0Z" changed="2016-10-11T09:10:07.0Z" id="FR:78402:ZDE:50010290:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:35371:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>005005015</Value>
        </KeyValue>
       </keyList>
       <Name>Libération</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612231.541 6873893.031</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50010290">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50010290">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:50:49.059844', '2017-03-23 13:28:33.901527', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (66732, 66731, 'FR:78402:ZDL:54101:STIF', 1, 'chouette', 'Fontaine', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="54101" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDL:54101:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:50008792:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Fontaine</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:54101">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:54101">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:32793:STIF" version="32793"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:29.521038', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (66731, NULL, 'FR:78402:LDA:65275:STIF', 1, 'chouette', 'Fontaine', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="65275" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:LDA:65275:STIF">
       <Name>Fontaine</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65275">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65275">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:04:38.548068', '2017-03-23 10:04:38.548068', NULL);
INSERT INTO stop_areas VALUES (66828, NULL, 'FR:78217:LDA:65326:STIF', 1, 'chouette', 'Épône Mézières', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65326" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65326:STIF">
       <Name>Épône Mézières</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65326">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65326">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>railStation</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:04:41.295779', '2017-03-23 10:04:41.295779', NULL);
INSERT INTO stop_areas VALUES (66829, 66828, 'FR:78217:ZDL:47882:STIF', 1, 'chouette', 'Épône Mézières', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace xmlns:gml="http://www.opengis.net/gml/3.2" version="47882" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:47882:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50009674:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008591:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78029:ZDL:50008577:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50094383:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50097107:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Épône Mézières</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:47882">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:47882">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <entrances>
        <StopPlaceEntrance version="1925" id="FR:78402:ADL:1925:STIF">
         <Name>Epone Mezieres - Chemin Des Ardiles</Name>
         <Centroid>
          <Location>
           <gml:pos srsName="EPSG:2154">612720.73 6874394.95</gml:pos>
          </Location>
         </Centroid>
         <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:1925">
          <Town>Mézières-sur-Seine</Town>
          <PostalRegion>78402</PostalRegion>
         </PostalAddress>
         <IsEntry>true</IsEntry>
         <IsExit>true</IsExit>
        </StopPlaceEntrance>
        <StopPlaceEntrance version="1924" id="FR:78217:ADL:1924:STIF">
         <Name>Epone Mezieres - Place De La Gare</Name>
         <Centroid>
          <Location>
           <gml:pos srsName="EPSG:2154">612735.77 6874354.486</gml:pos>
          </Location>
         </Centroid>
         <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:1924">
          <Town>Épône</Town>
          <PostalRegion>78217</PostalRegion>
         </PostalAddress>
         <IsEntry>true</IsEntry>
         <IsExit>true</IsExit>
        </StopPlaceEntrance>
        <StopPlaceEntrance version="1923" id="FR:78217:ADL:1923:STIF">
         <Name>Epone Mezieres - Place De La Gare</Name>
         <Centroid>
          <Location>
           <gml:pos srsName="EPSG:2154">612762.971 6874346.473</gml:pos>
          </Location>
         </Centroid>
         <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:1923">
          <Town>Épône</Town>
          <PostalRegion>78217</PostalRegion>
         </PostalAddress>
         <IsEntry>true</IsEntry>
         <IsExit>true</IsExit>
        </StopPlaceEntrance>
        <StopPlaceEntrance version="1922" id="FR:78217:ADL:1922:STIF">
         <Name>Epone Mezieres - Place De La Gare</Name>
         <Centroid>
          <Location>
           <gml:pos srsName="EPSG:2154">612739.362 6874355.806</gml:pos>
          </Location>
         </Centroid>
         <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:1922">
          <Town>Épône</Town>
          <PostalRegion>78217</PostalRegion>
         </PostalAddress>
         <IsEntry>true</IsEntry>
         <IsExit>true</IsExit>
        </StopPlaceEntrance>
        <StopPlaceEntrance version="1921" id="FR:78217:ADL:1921:STIF">
         <Name>Epone Mezieres - Place De La Gare</Name>
         <Centroid>
          <Location>
           <gml:pos srsName="EPSG:2154">612759.873 6874351.751</gml:pos>
          </Location>
         </Centroid>
         <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:1921">
          <Town>Épône</Town>
          <PostalRegion>78217</PostalRegion>
         </PostalAddress>
         <IsEntry>true</IsEntry>
         <IsExit>true</IsExit>
        </StopPlaceEntrance>
        <StopPlaceEntrance version="1920" id="FR:78217:ADL:1920:STIF">
         <Name>Epone Mezieres - Place De La Gare</Name>
         <Centroid>
          <Location>
           <gml:pos srsName="EPSG:2154">612755.727 6874357.07</gml:pos>
          </Location>
         </Centroid>
         <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:1920">
          <Town>Épône</Town>
          <PostalRegion>78217</PostalRegion>
         </PostalAddress>
         <IsEntry>true</IsEntry>
         <IsExit>true</IsExit>
        </StopPlaceEntrance>
        <StopPlaceEntrance version="1919" id="FR:78402:ADL:1919:STIF">
         <Name>Epone Mezieres - Chemin Des Ardiles</Name>
         <Centroid>
          <Location>
           <gml:pos srsName="EPSG:2154">612724.719 6874395.017</gml:pos>
          </Location>
         </Centroid>
         <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:1919">
          <Town>Mézières-sur-Seine</Town>
          <PostalRegion>78402</PostalRegion>
         </PostalAddress>
         <IsEntry>true</IsEntry>
         <IsExit>true</IsExit>
        </StopPlaceEntrance>
       </entrances>
       <StopPlaceType>railStation</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:35752:STIF" version="806125"/>
        <QuayRef ref="FR:78217:ZDE:41202:STIF" version="41202"/>
        <QuayRef ref="FR:78217:ZDE:32503:STIF" version="32503"/>
        <QuayRef ref="FR:78217:ZDE:18304:STIF" version="18304"/>
        <QuayRef ref="FR:78217:ZDE:18305:STIF" version="18305"/>
        <QuayRef ref="FR:78217:ZDE:418584:STIF" version="709210"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 12:36:02.732716', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (66974, NULL, 'FR:78217:LDA:65403:STIF', 1, 'chouette', 'Avenue d''Epone', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65403" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65403:STIF">
       <Name>Avenue d''Epone</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65403">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65403">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:04:45.34518', '2017-03-23 10:04:45.34518', NULL);
INSERT INTO stop_areas VALUES (66975, 66974, 'FR:78217:ZDL:53980:STIF', 1, 'chouette', 'Avenue d''Epone', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53980" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53980:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008572:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Avenue d''Epone</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53980">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53980">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32504:STIF" version="32504"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:14:33.825688', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (66968, NULL, 'FR:78217:LDA:65400:STIF', 1, 'chouette', 'Bout du Monde', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65400" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65400:STIF">
       <Name>Bout du Monde</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65400">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65400">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:04:45.206909', '2017-03-23 10:04:45.206909', NULL);
INSERT INTO stop_areas VALUES (67013, NULL, 'FR:78217:LDA:65425:STIF', 1, 'chouette', 'Boulevard de la Paix', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65425" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65425:STIF">
       <Name>Boulevard de la Paix</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65425">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65425">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:04:46.452947', '2017-03-23 10:04:46.452947', NULL);
INSERT INTO stop_areas VALUES (67023, NULL, 'FR:78217:LDA:65433:STIF', 1, 'chouette', 'Les Dolmens', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65433" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65433:STIF">
       <Name>Les Dolmens</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65433">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65433">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:04:46.708345', '2017-03-23 10:04:46.708345', NULL);
INSERT INTO stop_areas VALUES (67081, NULL, 'FR:78217:LDA:65463:STIF', 1, 'chouette', 'Place Maréchal Juin', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65463" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65463:STIF">
       <Name>Place Maréchal Juin</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65463">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65463">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:04:48.256811', '2017-03-23 10:04:48.256811', NULL);
INSERT INTO stop_areas VALUES (67135, 67134, 'FR:78217:ZDL:53988:STIF', 1, 'chouette', 'Mairie - Ecole', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53988" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53988:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008581:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie - Ecole</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53988">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53988">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32511:STIF" version="32511"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.434043', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (67134, NULL, 'FR:78217:LDA:65493:STIF', 1, 'chouette', 'Mairie - Ecole', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65493" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65493:STIF">
       <Name>Mairie - Ecole</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65493">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65493">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:04:49.729457', '2017-03-23 10:04:49.729457', NULL);
INSERT INTO stop_areas VALUES (67160, NULL, 'FR:78217:LDA:65507:STIF', 1, 'chouette', 'La Bergerie', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65507" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65507:STIF">
       <Name>La Bergerie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65507">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65507">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:04:50.399593', '2017-03-23 10:04:50.399593', NULL);
INSERT INTO stop_areas VALUES (71574, NULL, 'FR:78217:LDA:420192:STIF', 1, 'chouette', 'Emile Sergent', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="729355" created="2015-10-07T04:10:21.0Z" changed="2015-10-07T04:10:21.0Z" id="FR:78217:LDA:420192:STIF">
       <Name>Emile Sergent</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:420192">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:420192">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, '2017-03-23 10:20:12.580087', '2017-03-23 10:20:12.580087', NULL);
INSERT INTO stop_areas VALUES (42130, 42129, 'FR:78402:ZDL:54102:STIF', 1, 'chouette', 'La Villeneuve', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="54102" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDL:54102:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:50008793:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>La Villeneuve</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:54102">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:54102">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:32794:STIF" version="32794"/>
        <QuayRef ref="FR:78402:ZDE:32795:STIF" version="32795"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:29.565407', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (19555, 56621, 'FR:78217:ZDE:32784:STIF', 1, 'chouette', 'BROUILLARD', NULL, 'zder', NULL, NULL, NULL, 1.7875870622303600, 48.9263228997569000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32784" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32784:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009384:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>BROUILLARD</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611163.259 6870275.399</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32784">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32784">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.369123', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (19556, 56621, 'FR:78217:ZDE:32785:STIF', 1, 'chouette', 'BROUILLARD', NULL, 'zder', NULL, NULL, NULL, 1.7870004023971600, 48.9261295695634000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32785" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32785:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009385:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>BROUILLARD</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611119.946 6870254.563</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32785">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32785">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.41497', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (56621, 56620, 'FR:78217:ZDL:54096:STIF', 1, 'chouette', 'Brouillard', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="54096" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:54096:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008787:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Brouillard</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:54096">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:54096">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32784:STIF" version="32784"/>
        <QuayRef ref="FR:78217:ZDE:32785:STIF" version="32785"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:29.331637', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (57001, 57000, 'FR:78217:ZDL:54095:STIF', 1, 'chouette', 'Bois de l''Aulnz', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="54095" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:54095:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008786:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Bois de l''Aulnz</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:54095">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:54095">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:32782:STIF" version="32782"/>
        <QuayRef ref="FR:78217:ZDE:32783:STIF" version="32783"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:29.2878', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (66969, 66968, 'FR:78217:ZDL:53985:STIF', 1, 'chouette', 'Bout du Monde', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53985" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53985:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008578:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Bout du Monde</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53985">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53985">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32509:STIF" version="32509"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.253281', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (31738, 67014, 'FR:78217:ZDE:32507:STIF', 1, 'chouette', 'BOULEVARD DE LA PAIX', NULL, 'zder', NULL, NULL, NULL, 1.8359592073884300, 48.9698496280250000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32507" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32507:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009037:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>BOULEVARD DE LA PAIX</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614778.706 6875061.898</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32507">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32507">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:55.852444', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (67082, 67081, 'FR:78217:ZDL:53986:STIF', 1, 'chouette', 'Place Maréchal Juin', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53986" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53986:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008579:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50098328:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Place Maréchal Juin</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53986">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53986">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32514:STIF" version="32514"/>
        <QuayRef ref="FR:78217:ZDE:416465:STIF" version="666350"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.300159', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (116524, 150090, 'FR:78217:ZDE:50000882:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.8115991914771900, 48.9573350926121000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:58" version="824656" created="2015-01-31T09:01:45.0Z" changed="2017-02-10T09:02:49.0Z" id="FR:78217:ZDE:50000882:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:13660:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>052052080</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612974.459 6873696.931</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50000882">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50000882">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:10:16.547515', '2017-03-23 13:37:55.043103', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (116527, 150090, 'FR:78217:ZDE:50000881:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.8115859069711800, 48.9572990862766000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:58" version="824657" created="2015-01-31T09:01:46.0Z" changed="2017-02-10T09:02:49.0Z" id="FR:78217:ZDE:50000881:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32530:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>052052080</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612973.426 6873692.942</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50000881">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50000881">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:10:16.634084', '2017-03-23 13:37:55.068138', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (116526, 150091, 'FR:78217:ZDE:50009061:STIF', 1, 'chouette', 'MAIRIE', NULL, 'zdep', NULL, NULL, NULL, 1.8115373421136000, 48.9572979483809000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443331" created="2015-02-03T06:02:41.0Z" changed="2015-02-03T06:02:41.0Z" id="FR:78217:ZDE:50009061:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32530:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>MAIRIE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612969.868 6873692.869</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009061">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009061">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:10:16.60913', '2017-03-23 13:37:55.091879', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (116525, 150091, 'FR:78217:ZDE:50009060:STIF', 1, 'chouette', 'MAIRIE', NULL, 'zdep', NULL, NULL, NULL, 1.8114323433300500, 48.9574929456276000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443333" created="2015-02-03T06:02:41.0Z" changed="2015-02-03T06:02:41.0Z" id="FR:78217:ZDE:50009060:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:13660:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>MAIRIE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612962.506 6873714.667</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009060">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009060">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:10:16.565713', '2017-03-23 13:37:55.112686', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (2705, 42130, 'FR:78402:ZDE:32794:STIF', 1, 'chouette', 'LA VILLENEUVE', NULL, 'zder', NULL, NULL, NULL, 1.7938288733818300, 48.9474412752481000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32794" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32794:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009394:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>LA VILLENEUVE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611656.449 6872616.551</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32794">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32794">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.728191', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (2774, 42187, 'FR:78402:ZDE:32792:STIF', 1, 'chouette', 'CHEMIN DE L''EPINE', NULL, 'zder', NULL, NULL, NULL, 1.7937005478790100, 48.9498796059636000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32792" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32792:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009392:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CHEMIN DE L''EPINE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611651.193 6872887.816</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32792">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32792">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.639743', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (2826, 42239, 'FR:78402:ZDE:32799:STIF', 1, 'chouette', 'LISERETTES', NULL, 'zder', NULL, NULL, NULL, 1.7955739011500000, 48.9522762814456000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32799" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32799:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009397:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>LISERETTES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611792.451 6873152.212</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32799">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32799">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.873063', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (2854, 42268, 'FR:78217:ZDE:32521:STIF', 1, 'chouette', 'LE CHATEAU', NULL, 'zder', NULL, NULL, NULL, 1.8133756580597400, 48.9533662680704000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32521" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32521:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009052:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>LE CHATEAU</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613097.906 6873253.671</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32521">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32521">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.722058', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (19658, 56713, 'FR:78217:ZDE:32786:STIF', 1, 'chouette', 'CANADA', NULL, 'zder', NULL, NULL, NULL, 1.7903770996057600, 48.9298595765723000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32786" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32786:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009386:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CANADA</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611373.701 6870665.504</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32786">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32786">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:20:55.413049', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (2827, 42241, 'FR:78217:ZDE:32523:STIF', 1, 'chouette', 'CHEMIN NEUF', NULL, 'zder', NULL, NULL, NULL, 1.8162940080552900, 48.9524979333468000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32523" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32523:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009054:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>CHEMIN NEUF</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613310.167 6873153.911</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32523">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32523">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.833235', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (26245, 62366, 'FR:78217:ZDE:32533:STIF', 1, 'chouette', 'POTEAU', NULL, 'zder', NULL, NULL, NULL, 1.8300191012754500, 48.9566545958770000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32533" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32533:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009065:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011512</Value>
        </KeyValue>
       </keyList>
       <Name>POTEAU</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614322.112 6873601.121</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32533">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32533">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:57.190086', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (26173, 62288, 'FR:78217:ZDE:32520:STIF', 1, 'chouette', 'CARREFOUR ST MARTIN', NULL, 'zder', NULL, NULL, NULL, 1.8193123651273500, 48.9536762647935000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32520" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32520:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009051:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>CARREFOUR ST MARTIN</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613533.162 6873281.623</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32520">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32520">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.665713', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (26193, 62311, 'FR:78402:ZDE:32789:STIF', 1, 'chouette', 'CHAUFFOUR', NULL, 'zder', NULL, NULL, NULL, 1.7981472685123400, 48.9541279492270000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32789" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32789:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009389:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CHAUFFOUR</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611984.034 6873355.231</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32789">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32789">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.504504', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (42240, NULL, 'FR:78217:LDA:65144:STIF', 1, 'chouette', 'Chemin Neuf', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65144" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65144:STIF">
       <Name>Chemin Neuf</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65144">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65144">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, NULL, '2017-03-23 09:51:53.500172', NULL);
INSERT INTO stop_areas VALUES (26240, 62360, 'FR:78217:ZDE:32527:STIF', 1, 'chouette', 'LA FALAISE', NULL, 'zder', NULL, NULL, NULL, 1.8215623902590100, 48.9563379380118000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32527" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32527:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009057:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>LA FALAISE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613702.346 6873575.12</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32527">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32527">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.92403', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (26262, 62386, 'FR:78217:ZDE:13660:STIF', 1, 'chouette', 'Mairie', NULL, 'zder', NULL, NULL, NULL, 1.8115371679130100, 48.9573485884751000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="819300" created="2014-12-29T03:12:51.0Z" changed="2016-11-07T04:11:21.0Z" id="FR:78217:ZDE:13660:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50000882:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009060:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>052052080</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612969.94 6873698.5</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:13660">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:13660">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:10:16.515181', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (26352, 62468, 'FR:78402:ZDE:13658:STIF', 1, 'chouette', 'Libération', NULL, 'zder', NULL, NULL, NULL, 1.8008231196433900, 48.9597645596152000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="812704" created="2014-12-29T03:12:51.0Z" changed="2016-10-25T05:10:13.0Z" id="FR:78402:ZDE:13658:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50000918:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009230:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011180</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>052052080</Value>
        </KeyValue>
       </keyList>
       <Name>Libération</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612189.5 6873979.0</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:13658">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:13658">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 11:50:48.887048', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (26353, 62468, 'FR:78402:ZDE:35370:STIF', 1, 'chouette', 'Libération', NULL, 'zder', NULL, NULL, NULL, 1.8015371568591400, 48.9590607796269000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="35370" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:35370:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50010289:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>005005015</Value>
        </KeyValue>
       </keyList>
       <Name>Libération</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612240.594 6873899.951</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:35370">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:35370">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 11:50:48.966313', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (31817, 67082, 'FR:78217:ZDE:416465:STIF', 1, 'chouette', 'Elisab. Pl du Maréchal Juin', NULL, 'zder', NULL, NULL, NULL, 1.8368735974221300, 48.9714362201824000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="666350" created="2015-05-12T09:05:28.0Z" changed="2015-05-12T09:05:28.0Z" id="FR:78217:ZDE:416465:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50098303:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Elisab. Pl du Maréchal Juin</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614848.244 6875237.332</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:416465">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:416465">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.062969', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (31532, 66829, 'FR:78217:ZDE:18305:STIF', 1, 'chouette', 'Gare d''Epône Mézières', NULL, 'zder', NULL, NULL, NULL, 1.8083195437807300, 48.9630724138501000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="18305" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:18305:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50094817:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057318208</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057057082</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057057081</Value>
        </KeyValue>
       </keyList>
       <Name>Gare d''Epône Mézières</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612743.94 6874338.5</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:18305">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:18305">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 11:49:03.682688', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (31699, 66975, 'FR:78217:ZDE:32504:STIF', 1, 'chouette', 'AVENUE D''EPONE', NULL, 'zder', NULL, NULL, NULL, 1.8337208598697700, 48.9686746309032000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32504" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32504:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009034:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>AVENUE D''EPONE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614612.915 6874933.663</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32504">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32504">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:17:26.2704', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (31816, 67082, 'FR:78217:ZDE:32514:STIF', 1, 'chouette', 'PLACE MARECHAL JUIN', NULL, 'zder', NULL, NULL, NULL, 1.8369392346693400, 48.9715229669525000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32514" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32514:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009041:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011180</Value>
        </KeyValue>
       </keyList>
       <Name>PLACE MARECHAL JUIN</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614853.191 6875246.907</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32514">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32514">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.019656', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (31885, 67135, 'FR:78217:ZDE:32511:STIF', 1, 'chouette', 'MAIRIE / ECOLE', NULL, 'zder', NULL, NULL, NULL, 1.8369025767705700, 48.9732296340157000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32511" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32511:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009043:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>MAIRIE / ECOLE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614853.303 6875436.719</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32511">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32511">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.166768', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (37772, 71575, 'FR:78217:ZDE:32524:STIF', 1, 'chouette', 'EMILE SERGENT', NULL, 'zder', NULL, NULL, NULL, 1.8169827330564200, 48.9566739779480000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="729344" created="2014-12-29T12:12:00.0Z" changed="2015-10-07T04:10:33.0Z" id="FR:78217:ZDE:32524:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009063:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>EMILE SERGENT</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613367.56 6873617.5</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32524">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32524">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:21:28.104679', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (42060, NULL, 'FR:78217:LDA:65045:STIF', 1, 'chouette', 'Moulinà Vent', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65045" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65045:STIF">
       <Name>Moulin à Vent</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65045">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65045">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, NULL, '2017-03-23 09:51:49.166119', NULL);
INSERT INTO stop_areas VALUES (42129, NULL, 'FR:78402:LDA:65082:STIF', 1, 'chouette', 'La Villeneuve', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="65082" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:LDA:65082:STIF">
       <Name>La Villeneuve</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65082">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65082">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, NULL, '2017-03-23 09:51:50.83226', NULL);
INSERT INTO stop_areas VALUES (42187, 42186, 'FR:78402:ZDL:54100:STIF', 1, 'chouette', 'Chemin de l''Epine', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="54100" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDL:54100:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:50008791:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Chemin de l''Epine</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:54100">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:54100">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:32791:STIF" version="32791"/>
        <QuayRef ref="FR:78402:ZDE:32792:STIF" version="32792"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:29.477306', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (42186, NULL, 'FR:78402:LDA:65112:STIF', 1, 'chouette', 'Chemin de l''Epine', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="65112" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:LDA:65112:STIF">
       <Name>Chemin de l''Epine</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65112">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65112">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, NULL, '2017-03-23 09:51:52.166187', NULL);
INSERT INTO stop_areas VALUES (42238, NULL, 'FR:78402:LDA:65143:STIF', 1, 'chouette', 'Liserettes', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="65143" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:LDA:65143:STIF">
       <Name>Liserettes</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65143">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65143">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, NULL, '2017-03-23 09:51:53.444687', NULL);
INSERT INTO stop_areas VALUES (42266, NULL, 'FR:78217:LDA:65159:STIF', 1, 'chouette', 'Le Château / Les Biches', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65159" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65159:STIF">
       <Name>Le Château / Les Biches</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="LDA"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:65159">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:65159">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
      </StopPlace>', NULL, NULL, '2017-03-23 09:51:54.300039', NULL);
INSERT INTO stop_areas VALUES (31529, 66829, 'FR:78217:ZDE:41202:STIF', 1, 'chouette', 'GARE D''EPONE MEZIERES', NULL, 'zder', NULL, NULL, NULL, 1.8086450611031900, 48.9633073081647000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="41202" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:41202:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50024700:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800854541</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800854518</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800852051</Value>
        </KeyValue>
       </keyList>
       <Name>GARE D''EPONE MEZIERES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612768.167 6874364.259</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:41202">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:41202">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 11:49:03.485897', 'REFERENCE_OBJECT');
-- maybe useless
INSERT INTO stop_areas VALUES (94847, NULL, 'FR:78217:ZDE:50024700:STIF', 1, 'chouette', 'GARE D''EPONE MEZIERES', NULL, 'zdep', NULL, NULL, NULL, 1.8086450611031900, 48.9633073081647000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:1" version="412181" created="2015-01-26T03:01:48.0Z" changed="2015-01-26T03:01:48.0Z" id="FR:78217:ZDE:50024700:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:41202:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800852051</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800854518</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800854541</Value>
        </KeyValue>
       </keyList>
       <Name>GARE D''EPONE MEZIERES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612768.167 6874364.259</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50024700">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50024700">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:49:03.517741', '2017-03-23 11:49:03.517741', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (31530, 66829, 'FR:78217:ZDE:32503:STIF', 1, 'chouette', 'Annexe Mairie (Gare SNCF)', NULL, 'zder', NULL, NULL, NULL, 1.8085190228973600, 48.9626212928230000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32503" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32503:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009056:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78029:ZDE:50009039:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011180</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>Annexe Mairie (Gare SNCF)</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612757.788 6874288.118</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32503">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32503">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 11:49:03.538268', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (94848, 141690, 'FR:78217:ZDE:50009056:STIF', 1, 'chouette', 'GARE D''EPONE MEZIERES', NULL, 'zdep', NULL, NULL, NULL, 1.8085190226913000, 48.9626213018143000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443357" created="2015-02-03T06:02:47.0Z" changed="2015-02-03T06:02:47.0Z" id="FR:78217:ZDE:50009056:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32503:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>GARE D''EPONE MEZIERES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612757.788 6874288.119</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009056">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009056">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:49:03.578169', '2017-03-23 13:27:37.312739', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (31531, 66829, 'FR:78217:ZDE:18304:STIF', 1, 'chouette', 'Gare d''Epône Mézières', NULL, 'zder', NULL, NULL, NULL, 1.8083189204177400, 48.9631353611361000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="18304" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:18304:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50094816:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057057081</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057057082</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057318208</Value>
        </KeyValue>
       </keyList>
       <Name>Gare d''Epône Mézières</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612744.0 6874345.5</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:18304">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:18304">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 11:49:03.627176', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (94850, 141692, 'FR:78217:ZDE:50094816:STIF', 1, 'chouette', 'Gare d''Epône Mézières', NULL, 'zdep', NULL, NULL, NULL, 1.8083192537077100, 48.9631351216278000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:81" version="743659" created="2015-03-30T02:03:39.0Z" changed="2016-01-05T09:01:47.0Z" id="FR:78217:ZDE:50094816:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:18304:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057057082</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057057081</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>057318208</Value>
        </KeyValue>
       </keyList>
       <Name>Gare d''Epône Mézières</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612744.024 6874345.473</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50094816">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50094816">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:49:03.663073', '2017-03-23 13:27:37.389289', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (31533, 66829, 'FR:78217:ZDE:418584:STIF', 1, 'chouette', 'GARE D''EPONE MEZIERES', NULL, 'zder', NULL, NULL, NULL, 1.8086450611031900, 48.9633073081647000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="709210" created="2015-06-15T09:06:29.0Z" changed="2015-06-15T09:06:29.0Z" id="FR:78217:ZDE:418584:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50097974:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50097449:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800854541</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800854541</Value>
        </KeyValue>
       </keyList>
       <Name>GARE D''EPONE MEZIERES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612768.167 6874364.259</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:418584">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:418584">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 11:49:03.749121', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (94853, 141693, 'FR:78217:ZDE:50097449:STIF', 1, 'chouette', 'GARE D''EPONE MEZIERES', NULL, 'zdep', NULL, NULL, NULL, 1.8086450611031900, 48.9633073081647000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:76" version="714159" created="2015-06-15T09:06:36.0Z" changed="2015-06-26T09:06:40.0Z" id="FR:78217:ZDE:50097449:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:418584:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800854541</Value>
        </KeyValue>
       </keyList>
       <Name>GARE D''EPONE MEZIERES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612768.167 6874364.259</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50097449">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50097449">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:49:03.81267', '2017-03-23 13:27:37.444464', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (96986, 142461, 'FR:78402:ZDE:50000918:STIF', 1, 'chouette', 'Libération', NULL, 'zdep', NULL, NULL, NULL, 1.8008754114950900, 48.9597048628052000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:58" version="824621" created="2015-01-31T09:01:32.0Z" changed="2017-02-10T09:02:39.0Z" id="FR:78402:ZDE:50000918:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:13658:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>052052080</Value>
        </KeyValue>
       </keyList>
       <Name>Libération</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612193.228 6873972.304</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50000918">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50000918">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:50:48.922238', '2017-03-23 13:28:33.806378', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (96985, 142462, 'FR:78402:ZDE:50009229:STIF', 1, 'chouette', 'LIBERATION', NULL, 'zdep', NULL, NULL, NULL, 1.8007139685441900, 48.9596213003233000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443459" created="2015-02-03T06:02:13.0Z" changed="2015-02-03T06:02:13.0Z" id="FR:78402:ZDE:50009229:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:13657:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011180</Value>
        </KeyValue>
       </keyList>
       <Name>LIBERATION</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612181.266 6873963.192</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009229">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009229">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:50:48.874593', '2017-03-23 13:28:33.850197', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (96987, 142462, 'FR:78402:ZDE:50009230:STIF', 1, 'chouette', 'LIBERATION', NULL, 'zdep', NULL, NULL, NULL, 1.8007889762727600, 48.9597479670721000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443461" created="2015-02-03T06:02:13.0Z" changed="2015-02-03T06:02:13.0Z" id="FR:78402:ZDE:50009230:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:13658:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011180</Value>
        </KeyValue>
       </keyList>
       <Name>LIBERATION</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612186.972 6873977.193</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009230">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009230">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:50:48.952717', '2017-03-23 13:28:33.872662', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (26354, 62468, 'FR:78402:ZDE:35371:STIF', 1, 'chouette', 'Libération', NULL, 'zder', NULL, NULL, NULL, 1.8014149803144700, 48.9589973240387000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="35371" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:35371:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50010290:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>005005015</Value>
        </KeyValue>
       </keyList>
       <Name>Libération</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612231.541 6873893.031</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:35371">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:35371">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 11:50:49.023652', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (96988, 142463, 'FR:78402:ZDE:50010289:STIF', 1, 'chouette', 'Libération', NULL, 'zdep', NULL, NULL, NULL, 1.8015371568591400, 48.9590607796269000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:22" version="805573" created="2015-03-26T09:03:11.0Z" changed="2016-10-11T09:10:09.0Z" id="FR:78402:ZDE:50010289:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:35370:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>005005015</Value>
        </KeyValue>
       </keyList>
       <Name>Libération</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612240.594 6873899.951</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50010289">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50010289">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:50:48.999382', '2017-03-23 13:28:33.91767', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (31916, 67161, 'FR:78217:ZDE:32508:STIF', 1, 'chouette', 'LA BERGERIE', NULL, 'zder', NULL, NULL, NULL, 1.8388059189735000, 48.9739196359739000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32508" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32508:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78029:ZDE:50009038:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>LA BERGERIE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614993.759 6875511.393</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32508">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32508">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:55.908461', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (31746, 67024, 'FR:78217:ZDE:32510:STIF', 1, 'chouette', 'LES DOLMENS', NULL, 'zder', NULL, NULL, NULL, 1.8328141968442100, 48.9702879693091000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32510" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32510:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009042:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>LES DOLMENS</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614549.192 6875114.038</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32510">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32510">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.106211', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (111192, 147681, 'FR:78217:ZDE:50009040:STIF', 1, 'chouette', 'BOUT DU MONDE', NULL, 'zdep', NULL, NULL, NULL, 1.8298075098006400, 48.9683646341499000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443418" created="2015-02-03T06:02:02.0Z" changed="2015-02-03T06:02:02.0Z" id="FR:78217:ZDE:50009040:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32509:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>BOUT DU MONDE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614325.916 6874903.432</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009040">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009040">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.006059', '2017-03-23 13:35:35.384263', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111194, 147683, 'FR:78217:ZDE:50098303:STIF', 1, 'chouette', 'Elisab. Pl du Maréchal Juin', NULL, 'zdep', NULL, NULL, NULL, 1.8368735974221300, 48.9714362201824000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:79" version="666349" created="2015-05-12T09:05:28.0Z" changed="2015-05-12T09:05:28.0Z" id="FR:78217:ZDE:50098303:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:416465:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Elisab. Pl du Maréchal Juin</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614848.244 6875237.332</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50098303">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50098303">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.097518', '2017-03-23 13:35:35.424511', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111196, 147685, 'FR:78217:ZDE:50009043:STIF', 1, 'chouette', 'MAIRIE / ECOLE', NULL, 'zdep', NULL, NULL, NULL, 1.8369025765693800, 48.9732296430070000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443393" created="2015-02-03T06:02:56.0Z" changed="2015-02-03T06:02:56.0Z" id="FR:78217:ZDE:50009043:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32511:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>MAIRIE / ECOLE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614853.303 6875436.72</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009043">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009043">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.208646', '2017-03-23 13:35:35.486601', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (26276, 62400, 'FR:78402:ZDE:32517:STIF', 1, 'chouette', 'AVENUE DE LA GARE', NULL, 'zder', NULL, NULL, NULL, 1.8076656653974800, 48.9580496150535000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32517" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32517:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009048:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>AVENUE DE LA GARE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612687.632 6873780.723</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32517">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32517">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.521265', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (26277, 62400, 'FR:78402:ZDE:32518:STIF', 1, 'chouette', 'AVENUE DE LA GARE', NULL, 'zder', NULL, NULL, NULL, 1.8076239920404600, 48.9580212851215000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32518" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32518:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009049:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>AVENUE DE LA GARE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612684.533 6873777.619</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32518">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32518">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.565485', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (111203, 147691, 'FR:78217:ZDE:50009048:STIF', 1, 'chouette', 'AVENUE DE LA GARE', NULL, 'zdep', NULL, NULL, NULL, 1.8076656517435900, 48.9580496149177000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443372" created="2015-02-03T06:02:51.0Z" changed="2015-02-03T06:02:51.0Z" id="FR:78217:ZDE:50009048:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32517:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>AVENUE DE LA GARE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612687.631 6873780.723</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009048">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009048">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.553918', '2017-03-23 13:35:35.677727', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (26172, 62288, 'FR:78217:ZDE:32519:STIF', 1, 'chouette', 'CARREFOUR ST MARTIN', NULL, 'zder', NULL, NULL, NULL, 1.8195940225965100, 48.9537812663945000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32519" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32519:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009050:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>CARREFOUR ST MARTIN</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613553.962 6873292.99</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32519">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32519">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:56.622666', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (111206, 147692, 'FR:78217:ZDE:50009051:STIF', 1, 'chouette', 'CARREFOUR ST MARTIN', NULL, 'zdep', NULL, NULL, NULL, 1.8193123651273500, 48.9536762647935000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443382" created="2015-02-03T06:02:53.0Z" changed="2015-02-03T06:02:53.0Z" id="FR:78217:ZDE:50009051:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32520:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>CARREFOUR ST MARTIN</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613533.162 6873281.623</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009051">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009051">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.700142', '2017-03-23 13:35:35.781135', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111208, 147693, 'FR:78217:ZDE:50009053:STIF', 1, 'chouette', 'LE CHATEAU', NULL, 'zdep', NULL, NULL, NULL, 1.8126173237632900, 48.9533412766717000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443341" created="2015-02-03T06:02:43.0Z" changed="2015-02-03T06:02:43.0Z" id="FR:78217:ZDE:50009053:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32522:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>LE CHATEAU</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613042.332 6873251.727</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009053">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009053">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.810321', '2017-03-23 13:35:35.835552', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (2853, 42267, 'FR:78217:ZDE:32529:STIF', 1, 'chouette', 'LES BICHES', NULL, 'zder', NULL, NULL, NULL, 1.8118139867675400, 48.9526329360882000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32529" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32529:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009059:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>LES BICHES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612982.319 6873173.85</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32529">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32529">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:57.021688', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (111210, 147695, 'FR:78217:ZDE:50009055:STIF', 1, 'chouette', 'LE FOURNEAU', NULL, 'zdep', NULL, NULL, NULL, 1.8090855831237100, 48.9424962444185000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443352" created="2015-02-03T06:02:46.0Z" changed="2015-02-03T06:02:46.0Z" id="FR:78217:ZDE:50009055:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32526:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>LE FOURNEAU</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612765.517 6872049.745</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009055">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009055">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:56.905914', '2017-03-23 13:35:35.897663', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111212, 147696, 'FR:78217:ZDE:50009058:STIF', 1, 'chouette', 'LA FALAISE', NULL, 'zdep', NULL, NULL, NULL, 1.8215273784607000, 48.9563896026471000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443320" created="2015-02-03T06:02:38.0Z" changed="2015-02-03T06:02:38.0Z" id="FR:78217:ZDE:50009058:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32528:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>LA FALAISE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613699.868 6873580.903</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009058">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009058">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:57.008691', '2017-03-23 13:35:35.935952', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (2592, 42061, 'FR:78217:ZDE:32531:STIF', 1, 'chouette', 'MOULIN A VENT', NULL, 'zder', NULL, NULL, NULL, 1.8099622622031800, 48.9454845919617000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32531" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32531:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009062:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>MOULIN A VENT</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612834.737 6872381.055</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32531">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32531">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:57.068362', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (26244, 62366, 'FR:78217:ZDE:32532:STIF', 1, 'chouette', 'POTEAU', NULL, 'zder', NULL, NULL, NULL, 1.8301257677224300, 48.9567112696982000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32532" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32532:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009066:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011512</Value>
        </KeyValue>
       </keyList>
       <Name>POTEAU</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614330.016 6873607.307</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32532">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32532">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:05:57.134609', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (111214, 147698, 'FR:78217:ZDE:50009062:STIF', 1, 'chouette', 'MOULIN A VENT', NULL, 'zdep', NULL, NULL, NULL, 1.8099622622031800, 48.9454845919617000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443326" created="2015-02-03T06:02:40.0Z" changed="2015-02-03T06:02:40.0Z" id="FR:78217:ZDE:50009062:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32531:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>MOULIN A VENT</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612834.737 6872381.055</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009062">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009062">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:05:57.110603', '2017-03-23 13:35:35.997324', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (20018, 57001, 'FR:78402:ZDE:32782:STIF', 1, 'chouette', 'BOIS DE L''AULNE', NULL, 'zder', NULL, NULL, NULL, 1.7941171745678400, 48.9425629280426000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32782" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32782:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009383:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>BOIS DE L''AULNE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611669.279 6872073.8</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32782">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32782">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.2599', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (111383, 147800, 'FR:78217:ZDE:50009382:STIF', 1, 'chouette', 'BOIS DE L''AULNE', NULL, 'zdep', NULL, NULL, NULL, 1.7942321823644500, 48.9427262657198000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444526" created="2015-02-03T06:02:26.0Z" changed="2015-02-03T06:02:26.0Z" id="FR:78217:ZDE:50009382:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32783:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>BOIS DE L''AULNE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611677.98 6872091.833</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009382">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009382">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.352658', '2017-03-23 13:35:40.45913', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (26382, 62495, 'FR:78402:ZDE:32788:STIF', 1, 'chouette', 'PLACE CDT GRIMBLOT', NULL, 'zder', NULL, NULL, NULL, 1.7949872944624100, 48.9607562930931000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32788" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32788:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009388:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>PLACE CDT GRIMBLOT</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611763.884 6874095.779</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32788">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32788">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.461722', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (111385, 147801, 'FR:78217:ZDE:50009385:STIF', 1, 'chouette', 'BROUILLARD', NULL, 'zdep', NULL, NULL, NULL, 1.7870004023971600, 48.9261295695634000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444536" created="2015-02-03T06:02:30.0Z" changed="2015-02-03T06:02:30.0Z" id="FR:78217:ZDE:50009385:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32785:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>BROUILLARD</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611119.946 6870254.563</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009385">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009385">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.448573', '2017-03-23 13:35:40.563591', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111387, 147803, 'FR:78402:ZDE:50009389:STIF', 1, 'chouette', 'CHAUFFOUR', NULL, 'zdep', NULL, NULL, NULL, 1.7981472685123400, 48.9541279492270000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444555" created="2015-02-03T06:02:38.0Z" changed="2015-02-03T06:02:38.0Z" id="FR:78402:ZDE:50009389:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32789:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CHAUFFOUR</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611984.034 6873355.231</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009389">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009389">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.535656', '2017-03-23 13:35:40.643597', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (2773, 42187, 'FR:78402:ZDE:32791:STIF', 1, 'chouette', 'CHEMIN DE L''EPINE', NULL, 'zder', NULL, NULL, NULL, 1.7941822176487300, 48.9501596076067000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32791" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32791:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009391:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CHEMIN DE L''EPINE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611686.943 6872918.411</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32791">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32791">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.593626', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (31421, 66732, 'FR:78402:ZDE:32793:STIF', 1, 'chouette', 'FONTAINE', NULL, 'zder', NULL, NULL, NULL, 1.7892689374423600, 48.9609196304168000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32793" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32793:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009393:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>FONTAINE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611345.473 6874120.346</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32793">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32793">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.684842', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (111389, 147804, 'FR:78402:ZDE:50009391:STIF', 1, 'chouette', 'CHEMIN DE L''EPINE', NULL, 'zdep', NULL, NULL, NULL, 1.7941822176487300, 48.9501596076067000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444563" created="2015-02-03T06:02:42.0Z" changed="2015-02-03T06:02:42.0Z" id="FR:78402:ZDE:50009391:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32791:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CHEMIN DE L''EPINE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611686.943 6872918.411</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009391">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009391">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.627244', '2017-03-23 13:35:40.718223', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (2706, 42130, 'FR:78402:ZDE:32795:STIF', 1, 'chouette', 'LA VILLENEUVE', NULL, 'zder', NULL, NULL, NULL, 1.7936538795271800, 48.9478062696551000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32795" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDE:32795:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:50009395:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>LA VILLENEUVE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611644.253 6872657.331</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32795">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32795">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:06:05.773559', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (111393, 147806, 'FR:78402:ZDE:50009395:STIF', 1, 'chouette', 'LA VILLENEUVE', NULL, 'zdep', NULL, NULL, NULL, 1.7936538656674500, 48.9478062785091000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444576" created="2015-02-03T06:02:47.0Z" changed="2015-02-03T06:02:47.0Z" id="FR:78402:ZDE:50009395:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32795:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>LA VILLENEUVE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611644.252 6872657.332</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009395">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009395">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.806392', '2017-03-23 13:35:40.82817', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (111394, 147807, 'FR:78402:ZDE:50009396:STIF', 1, 'chouette', 'LISERETTES', NULL, 'zdep', NULL, NULL, NULL, 1.7955805720521800, 48.9523196070409000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444586" created="2015-02-03T06:02:51.0Z" changed="2015-02-03T06:02:51.0Z" id="FR:78402:ZDE:50009396:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDE:32798:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>LISERETTES</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611793.013 6873157.022</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009396">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009396">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:06:05.858089', '2017-03-23 13:35:40.93123', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (125584, 153497, 'FR:78217:ZDE:50009034:STIF', 1, 'chouette', 'AVENUE D''EPONE', NULL, 'zdep', NULL, NULL, NULL, 1.8337208462130300, 48.9686746307704000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="445775" created="2015-02-03T07:02:44.0Z" changed="2015-02-03T07:02:44.0Z" id="FR:78217:ZDE:50009034:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32504:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011042</Value>
        </KeyValue>
       </keyList>
       <Name>AVENUE D''EPONE</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614612.914 6874933.663</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009034">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009034">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:17:26.298622', '2017-03-23 12:17:26.298622', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (129289, NULL, 'FR:78217:ZDE:50009386:STIF', 1, 'chouette', 'CANADA', NULL, 'zdep', NULL, NULL, NULL, 1.7903770996057600, 48.9298595765723000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444542" created="2015-02-03T06:02:33.0Z" changed="2015-02-03T06:02:33.0Z" id="FR:78217:ZDE:50009386:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32786:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CANADA</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611373.701 6870665.504</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009386">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009386">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:20:55.457246', '2017-03-23 12:20:55.457246', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (19659, 56713, 'FR:78217:ZDE:32787:STIF', 1, 'chouette', 'CANADA', NULL, 'zder', NULL, NULL, NULL, 1.7902854280654100, 48.9289362426669000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32787" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32787:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009387:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CANADA</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611365.412 6870562.942</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32787">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32787">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:20:55.494089', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (129290, 155111, 'FR:78217:ZDE:50009387:STIF', 1, 'chouette', 'CANADA', NULL, 'zdep', NULL, NULL, NULL, 1.7902854146282800, 48.9289362335376000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444544" created="2015-02-03T06:02:34.0Z" changed="2015-02-03T06:02:34.0Z" id="FR:78217:ZDE:50009387:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32787:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>CANADA</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611365.411 6870562.941</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009387">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009387">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:20:55.544443', '2017-03-23 12:20:55.544443', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (37771, 71575, 'FR:78217:ZDE:32525:STIF', 1, 'chouette', 'EMILE SERGENT', NULL, 'zder', NULL, NULL, NULL, 1.8170356954507600, 48.9568029441520000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32525" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32525:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:50009064:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>EMILE SERGENT</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613371.653 6873631.782</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:32525">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:32525">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, NULL, '2017-03-23 12:21:28.046604', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (129791, 155354, 'FR:78217:ZDE:50009064:STIF', 1, 'chouette', 'EMILE SERGENT', NULL, 'zdep', NULL, NULL, NULL, 1.8170356954507600, 48.9568029441520000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443297" created="2015-02-03T06:02:33.0Z" changed="2015-02-03T06:02:33.0Z" id="FR:78217:ZDE:50009064:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32525:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011009</Value>
        </KeyValue>
       </keyList>
       <Name>EMILE SERGENT</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613371.653 6873631.782</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009064">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009064">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:21:28.07862', '2017-03-23 12:21:28.07862', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (129792, 155354, 'FR:78217:ZDE:50009063:STIF', 1, 'chouette', 'EMILE SERGENT', NULL, 'zdep', NULL, NULL, NULL, 1.8169827219037900, 48.9566756685959000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="717630" created="2015-02-03T06:02:33.0Z" changed="2015-07-17T09:07:43.0Z" id="FR:78217:ZDE:50009063:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDE:32524:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
        </KeyValue>
       </keyList>
       <Name>EMILE SERGENT</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613367.562 6873617.688</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009063">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009063">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:21:28.157954', '2017-03-23 12:21:28.157954', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (67024, 67023, 'FR:78217:ZDL:53987:STIF', 1, 'chouette', 'Les Dolmens', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53987" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53987:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008580:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Les Dolmens</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53987">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53987">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32510:STIF" version="32510"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.378825', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (141689, NULL, 'FR:78217:ZDL:50009674:STIF', 1, 'chouette', 'EPONE Gare SNCF', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:22" version="805430" created="2015-03-26T09:03:28.0Z" changed="2016-10-11T09:10:04.0Z" id="FR:78217:ZDL:50009674:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:47882:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>EPONE Gare SNCF</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009674">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009674">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50010057:STIF" version="805429"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:36:02.854669', '2017-03-23 12:36:02.854669', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (141690, NULL, 'FR:78217:ZDL:50008591:STIF', 1, 'chouette', 'GARE D''EPONE MEZIERES', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443359" created="2015-02-03T06:02:48.0Z" changed="2015-02-03T06:02:48.0Z" id="FR:78217:ZDL:50008591:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:47882:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>GARE D''EPONE MEZIERES</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008591">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008591">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009056:STIF" version="443357"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:36:02.883358', '2017-03-23 12:36:02.883358', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (141692, NULL, 'FR:78217:ZDL:50094383:STIF', 1, 'chouette', 'EPONE Gare', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:81" version="637617" created="2015-03-30T02:03:39.0Z" changed="2015-03-30T02:03:39.0Z" id="FR:78217:ZDL:50094383:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:47882:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>EPONE Gare</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50094383">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50094383">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50094817:STIF" version="743658"/>
        <QuayRef ref="FR:78217:ZDE:50094816:STIF" version="743659"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:36:02.979184', '2017-03-23 12:36:02.979184', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (141693, NULL, 'FR:78217:ZDL:50097107:STIF', 1, 'chouette', 'GARE D''EPONE MEZIERES', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:76" version="709215" created="2015-06-15T09:06:37.0Z" changed="2015-06-15T09:06:37.0Z" id="FR:78217:ZDL:50097107:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:47882:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>GARE D''EPONE MEZIERES</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50097107">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50097107">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50097974:STIF" version="714158"/>
        <QuayRef ref="FR:78217:ZDE:50097449:STIF" version="714159"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:36:03.047183', '2017-03-23 12:36:03.047183', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (62468, 62467, 'FR:78402:ZDL:43807:STIF', 1, 'chouette', 'Libération', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="43807" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDL:43807:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:50000654:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:50008702:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:50009813:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Libération</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:43807">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:43807">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:13657:STIF" version="795044"/>
        <QuayRef ref="FR:78402:ZDE:13658:STIF" version="812704"/>
        <QuayRef ref="FR:78402:ZDE:35370:STIF" version="35370"/>
        <QuayRef ref="FR:78402:ZDE:35371:STIF" version="35371"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 12:37:06.725921', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (142461, NULL, 'FR:78402:ZDL:50000654:STIF', 1, 'chouette', 'Libération', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:58" version="428397" created="2015-01-31T09:01:34.0Z" changed="2015-01-31T09:01:34.0Z" id="FR:78402:ZDL:50000654:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:43807:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Libération</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50000654">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50000654">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:50000918:STIF" version="824621"/>
        <QuayRef ref="FR:78402:ZDE:50000917:STIF" version="824622"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:37:06.767804', '2017-03-23 12:37:06.767804', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (142462, NULL, 'FR:78402:ZDL:50008702:STIF', 1, 'chouette', 'LIBERATION', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443463" created="2015-02-03T06:02:13.0Z" changed="2015-02-03T06:02:13.0Z" id="FR:78402:ZDL:50008702:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:43807:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>LIBERATION</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008702">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008702">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:50009229:STIF" version="443459"/>
        <QuayRef ref="FR:78402:ZDE:50009230:STIF" version="443461"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:37:06.790155', '2017-03-23 12:37:06.790155', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (142463, NULL, 'FR:78402:ZDL:50009813:STIF', 1, 'chouette', 'MEZIERES SUR SEINE Libération', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:22" version="632447" created="2015-03-26T09:03:11.0Z" changed="2015-03-26T09:03:11.0Z" id="FR:78402:ZDL:50009813:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:43807:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>MEZIERES SUR SEINE Libération</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50009813">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50009813">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:50010290:STIF" version="805572"/>
        <QuayRef ref="FR:78402:ZDE:50010289:STIF" version="805573"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:37:06.856562', '2017-03-23 12:37:06.856562', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (67161, 67160, 'FR:78217:ZDL:53984:STIF', 1, 'chouette', 'La Bergerie', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53984" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53984:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78029:ZDL:50008576:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>La Bergerie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53984">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53984">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32508:STIF" version="32508"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.210172', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (147694, NULL, 'FR:78217:ZDL:50008589:STIF', 1, 'chouette', 'CHEMIN NEUF', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443349" created="2015-02-03T06:02:45.0Z" changed="2015-02-03T06:02:45.0Z" id="FR:78217:ZDL:50008589:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53995:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>CHEMIN NEUF</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008589">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008589">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009054:STIF" version="443347"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.862072', '2017-03-23 13:09:23.862072', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147695, NULL, 'FR:78217:ZDL:50008590:STIF', 1, 'chouette', 'LE FOURNEAU', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443354" created="2015-02-03T06:02:46.0Z" changed="2015-02-03T06:02:46.0Z" id="FR:78217:ZDL:50008590:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53996:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>LE FOURNEAU</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008590">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008590">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009055:STIF" version="443352"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.92171', '2017-03-23 13:09:23.92171', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147681, NULL, 'FR:78217:ZDL:50008578:STIF', 1, 'chouette', 'BOUT DU MONDE', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443420" created="2015-02-03T06:02:02.0Z" changed="2015-02-03T06:02:02.0Z" id="FR:78217:ZDL:50008578:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53985:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>BOUT DU MONDE</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008578">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008578">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009040:STIF" version="443418"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.284977', '2017-03-23 13:09:23.284977', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147677, NULL, 'FR:78217:ZDL:50008573:STIF', 1, 'chouette', 'BOULEVARD DE BRUXELLES', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="445782" created="2015-02-03T07:02:49.0Z" changed="2015-02-03T07:02:49.0Z" id="FR:78217:ZDL:50008573:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78029:ZDL:53981:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>BOULEVARD DE BRUXELLES</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008573">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008573">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009035:STIF" version="445780"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.097245', '2017-03-23 13:09:23.097245', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (67014, 67013, 'FR:78217:ZDL:53983:STIF', 1, 'chouette', 'Boulevard de la Paix', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53983" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53983:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008575:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Boulevard de la Paix</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53983">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53983">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32507:STIF" version="32507"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.153891', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (147679, NULL, 'FR:78217:ZDL:50008575:STIF', 1, 'chouette', 'BOULEVARD DE LA PAIX', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="445797" created="2015-02-03T07:02:58.0Z" changed="2015-02-03T07:02:58.0Z" id="FR:78217:ZDL:50008575:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53983:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>BOULEVARD DE LA PAIX</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008575">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008575">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009037:STIF" version="445795"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.192973', '2017-03-23 13:09:23.192973', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147682, NULL, 'FR:78217:ZDL:50008579:STIF', 1, 'chouette', 'PLACE MARECHAL JUIN', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443425" created="2015-02-03T06:02:04.0Z" changed="2015-02-03T06:02:04.0Z" id="FR:78217:ZDL:50008579:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53986:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>PLACE MARECHAL JUIN</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008579">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008579">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009041:STIF" version="443423"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.33706', '2017-03-23 13:09:23.33706', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147683, NULL, 'FR:78217:ZDL:50098328:STIF', 1, 'chouette', 'Elisab. Pl du Maréchal Juin', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:79" version="666353" created="2015-05-12T09:05:29.0Z" changed="2015-05-12T09:05:29.0Z" id="FR:78217:ZDL:50098328:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53986:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Elisab. Pl du Maréchal Juin</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50098328">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50098328">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50098303:STIF" version="666349"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.360074', '2017-03-23 13:09:23.360074', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147684, NULL, 'FR:78217:ZDL:50008580:STIF', 1, 'chouette', 'LES DOLMENS', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443390" created="2015-02-03T06:02:55.0Z" changed="2015-02-03T06:02:55.0Z" id="FR:78217:ZDL:50008580:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53987:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>LES DOLMENS</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008580">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008580">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009042:STIF" version="443388"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.419063', '2017-03-23 13:09:23.419063', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147685, NULL, 'FR:78217:ZDL:50008581:STIF', 1, 'chouette', 'MAIRIE / ECOLE', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443395" created="2015-02-03T06:02:56.0Z" changed="2015-02-03T06:02:56.0Z" id="FR:78217:ZDL:50008581:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53988:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>MAIRIE / ECOLE</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008581">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008581">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009043:STIF" version="443393"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.462771', '2017-03-23 13:09:23.462771', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147690, NULL, 'FR:78217:ZDL:50008585:STIF', 1, 'chouette', 'ALLEE DE PINCELOUP', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443369" created="2015-02-03T06:02:50.0Z" changed="2015-02-03T06:02:50.0Z" id="FR:78217:ZDL:50008585:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53991:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>ALLEE DE PINCELOUP</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008585">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008585">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009047:STIF" version="443367"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.662263', '2017-03-23 13:09:23.662263', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (62400, 62399, 'FR:78402:ZDL:53992:STIF', 1, 'chouette', 'Avenue de la Gare', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="53992" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDL:53992:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008586:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Avenue de la Gare</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53992">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53992">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:32517:STIF" version="32517"/>
        <QuayRef ref="FR:78402:ZDE:32518:STIF" version="32518"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.677836', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (147691, NULL, 'FR:78217:ZDL:50008586:STIF', 1, 'chouette', 'AVENUE DE LA GARE', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443376" created="2015-02-03T06:02:51.0Z" changed="2015-02-03T06:02:51.0Z" id="FR:78217:ZDL:50008586:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:53992:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>AVENUE DE LA GARE</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008586">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008586">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009048:STIF" version="443372"/>
        <QuayRef ref="FR:78217:ZDE:50009049:STIF" version="443374"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.718946', '2017-03-23 13:09:23.718946', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147692, NULL, 'FR:78217:ZDL:50008587:STIF', 1, 'chouette', 'CARREFOUR ST MARTIN', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443384" created="2015-02-03T06:02:53.0Z" changed="2015-02-03T06:02:53.0Z" id="FR:78217:ZDL:50008587:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53993:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>CARREFOUR ST MARTIN</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008587">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008587">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009050:STIF" version="443380"/>
        <QuayRef ref="FR:78217:ZDE:50009051:STIF" version="443382"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.762596', '2017-03-23 13:09:23.762596', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147693, NULL, 'FR:78217:ZDL:50008588:STIF', 1, 'chouette', 'LE CHATEAU', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443343" created="2015-02-03T06:02:43.0Z" changed="2015-02-03T06:02:43.0Z" id="FR:78217:ZDL:50008588:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53994:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>LE CHATEAU</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008588">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008588">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009052:STIF" version="443339"/>
        <QuayRef ref="FR:78217:ZDE:50009053:STIF" version="443341"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.81686', '2017-03-23 13:09:23.81686', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (42241, 42240, 'FR:78217:ZDL:53995:STIF', 1, 'chouette', 'Chemin Neuf', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53995" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53995:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008589:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Chemin Neuf</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53995">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53995">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32523:STIF" version="32523"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.833318', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (62360, 62359, 'FR:78217:ZDL:53997:STIF', 1, 'chouette', 'La Falaise', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53997" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53997:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008592:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>La Falaise</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53997">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53997">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32527:STIF" version="32527"/>
        <QuayRef ref="FR:78217:ZDE:32528:STIF" version="32528"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:23.931439', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (147696, NULL, 'FR:78217:ZDL:50008592:STIF', 1, 'chouette', 'LA FALAISE', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443322" created="2015-02-03T06:02:38.0Z" changed="2015-02-03T06:02:38.0Z" id="FR:78217:ZDL:50008592:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53997:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>LA FALAISE</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008592">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008592">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009057:STIF" version="443318"/>
        <QuayRef ref="FR:78217:ZDE:50009058:STIF" version="443320"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:23.971922', '2017-03-23 13:09:23.971922', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147697, NULL, 'FR:78217:ZDL:50008593:STIF', 1, 'chouette', 'LES BICHES', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443315" created="2015-02-03T06:02:37.0Z" changed="2015-02-03T06:02:37.0Z" id="FR:78217:ZDL:50008593:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53998:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>LES BICHES</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008593">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008593">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009059:STIF" version="443313"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:24.02208', '2017-03-23 13:09:24.02208', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (42061, 42060, 'FR:78217:ZDL:53999:STIF', 1, 'chouette', 'Moulinà Vent', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53999" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53999:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008595:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Moulin à Vent</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:53999">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:53999">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32531:STIF" version="32531"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:24.044', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (147698, NULL, 'FR:78217:ZDL:50008595:STIF', 1, 'chouette', 'MOULIN A VENT', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443328" created="2015-02-03T06:02:40.0Z" changed="2015-02-03T06:02:40.0Z" id="FR:78217:ZDL:50008595:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53999:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>MOULIN A VENT</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008595">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008595">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009062:STIF" version="443326"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:24.085711', '2017-03-23 13:09:24.085711', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147699, NULL, 'FR:78217:ZDL:50008597:STIF', 1, 'chouette', 'POTEAU', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443291" created="2015-02-03T06:02:31.0Z" changed="2015-02-03T06:02:31.0Z" id="FR:78217:ZDL:50008597:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:54000:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>POTEAU</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008597">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008597">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009065:STIF" version="443287"/>
        <QuayRef ref="FR:78217:ZDE:50009066:STIF" version="443289"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:24.141993', '2017-03-23 13:09:24.141993', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147800, NULL, 'FR:78217:ZDL:50008786:STIF', 1, 'chouette', 'BOIS DE L''AULNE', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="444530" created="2015-02-03T06:02:28.0Z" changed="2015-02-03T06:02:28.0Z" id="FR:78217:ZDL:50008786:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:54095:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>BOIS DE L''AULNE</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008786">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008786">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009382:STIF" version="444526"/>
        <QuayRef ref="FR:78402:ZDE:50009383:STIF" version="444528"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:29.314353', '2017-03-23 13:09:29.314353', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147801, NULL, 'FR:78217:ZDL:50008787:STIF', 1, 'chouette', 'BROUILLARD', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="444538" created="2015-02-03T06:02:31.0Z" changed="2015-02-03T06:02:31.0Z" id="FR:78217:ZDL:50008787:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:54096:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>BROUILLARD</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008787">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008787">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009384:STIF" version="444534"/>
        <QuayRef ref="FR:78217:ZDE:50009385:STIF" version="444536"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:29.358291', '2017-03-23 13:09:29.358291', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147802, NULL, 'FR:78402:ZDL:50008789:STIF', 1, 'chouette', 'PLACE CDT GRIMBLOT', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="444552" created="2015-02-03T06:02:37.0Z" changed="2015-02-03T06:02:37.0Z" id="FR:78402:ZDL:50008789:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:54098:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>PLACE CDT GRIMBLOT</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008789">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008789">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:50009388:STIF" version="444550"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:29.404032', '2017-03-23 13:09:29.404032', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (62311, 62310, 'FR:78402:ZDL:54099:STIF', 1, 'chouette', 'Chauffour', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace version="54099" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78402:ZDL:54099:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:50008790:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Chauffour</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:54099">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:54099">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:32789:STIF" version="32789"/>
        <QuayRef ref="FR:78402:ZDE:32790:STIF" version="32790"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:09:29.433616', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (147803, NULL, 'FR:78402:ZDL:50008790:STIF', 1, 'chouette', 'CHAUFFOUR', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="444559" created="2015-02-03T06:02:40.0Z" changed="2015-02-03T06:02:40.0Z" id="FR:78402:ZDL:50008790:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:54099:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>CHAUFFOUR</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008790">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008790">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:50009389:STIF" version="444555"/>
        <QuayRef ref="FR:78402:ZDE:50009390:STIF" version="444557"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:29.459523', '2017-03-23 13:09:29.459523', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147804, NULL, 'FR:78402:ZDL:50008791:STIF', 1, 'chouette', 'CHEMIN DE L''EPINE', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="444567" created="2015-02-03T06:02:43.0Z" changed="2015-02-03T06:02:43.0Z" id="FR:78402:ZDL:50008791:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:54100:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>CHEMIN DE L''EPINE</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008791">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008791">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:50009391:STIF" version="444563"/>
        <QuayRef ref="FR:78402:ZDE:50009392:STIF" version="444565"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:29.506149', '2017-03-23 13:09:29.506149', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147805, NULL, 'FR:78402:ZDL:50008792:STIF', 1, 'chouette', 'FONTAINE', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="444573" created="2015-02-03T06:02:46.0Z" changed="2015-02-03T06:02:46.0Z" id="FR:78402:ZDL:50008792:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:54101:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>FONTAINE</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008792">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008792">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:50009393:STIF" version="444571"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:29.549049', '2017-03-23 13:09:29.549049', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147806, NULL, 'FR:78402:ZDL:50008793:STIF', 1, 'chouette', 'LA VILLENEUVE', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="444580" created="2015-02-03T06:02:49.0Z" changed="2015-02-03T06:02:49.0Z" id="FR:78402:ZDL:50008793:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:54102:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>LA VILLENEUVE</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008793">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008793">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:50009395:STIF" version="444576"/>
        <QuayRef ref="FR:78402:ZDE:50009394:STIF" version="444578"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:29.592797', '2017-03-23 13:09:29.592797', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (147807, NULL, 'FR:78402:ZDL:50008794:STIF', 1, 'chouette', 'LISERETTES', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78402', 'Mézières-sur-Seine', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="444588" created="2015-02-03T06:02:52.0Z" changed="2015-02-03T06:02:52.0Z" id="FR:78402:ZDL:50008794:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78402:ZDL:54103:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>LISERETTES</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008794">
        <Town>Mézières-sur-Seine</Town>
        <PostalRegion>78402</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008794">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78402:ZDE:50009397:STIF" version="444584"/>
        <QuayRef ref="FR:78402:ZDE:50009396:STIF" version="444586"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:09:29.637441', '2017-03-23 13:09:29.637441', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (150090, NULL, 'FR:78217:ZDL:50000635:STIF', 1, 'chouette', 'Mairie', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:58" version="428560" created="2015-01-31T09:01:47.0Z" changed="2015-01-31T09:01:47.0Z" id="FR:78217:ZDL:50000635:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:43983:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50000635">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50000635">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50000882:STIF" version="824656"/>
        <QuayRef ref="FR:78217:ZDE:50000881:STIF" version="824657"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:11:32.39979', '2017-03-23 13:11:32.39979', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (150091, NULL, 'FR:78217:ZDL:50008594:STIF', 1, 'chouette', 'MAIRIE', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443335" created="2015-02-03T06:02:41.0Z" changed="2015-02-03T06:02:41.0Z" id="FR:78217:ZDL:50008594:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:43983:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>MAIRIE</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008594">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008594">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009061:STIF" version="443331"/>
        <QuayRef ref="FR:78217:ZDE:50009060:STIF" version="443333"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:11:32.432633', '2017-03-23 13:11:32.432633', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (153497, NULL, 'FR:78217:ZDL:50008572:STIF', 1, 'chouette', 'AVENUE D''EPONE', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="445777" created="2015-02-03T07:02:46.0Z" changed="2015-02-03T07:02:46.0Z" id="FR:78217:ZDL:50008572:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:53980:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>AVENUE D''EPONE</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008572">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008572">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009034:STIF" version="445775"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:14:33.868862', '2017-03-23 13:14:33.868862', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (56713, 56712, 'FR:78217:ZDL:54097:STIF', 1, 'chouette', 'Canada', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="661331" created="2014-12-29T12:12:00.0Z" changed="2015-04-30T05:04:18.0Z" id="FR:78217:ZDL:54097:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LOCAL_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:50008788:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Canada</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:54097">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:54097">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32786:STIF" version="32786"/>
        <QuayRef ref="FR:78217:ZDE:32787:STIF" version="32787"/>
       </quays>
      </StopPlace>', NULL, NULL, '2017-03-23 13:16:08.780009', 'REFERENCE_OBJECT');
INSERT INTO stop_areas VALUES (155111, NULL, 'FR:78217:ZDL:50008788:STIF', 1, 'chouette', 'CANADA', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="444546" created="2015-02-03T06:02:35.0Z" changed="2015-02-03T06:02:35.0Z" id="FR:78217:ZDL:50008788:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:54097:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>CANADA</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008788">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008788">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009386:STIF" version="444542"/>
        <QuayRef ref="FR:78217:ZDE:50009387:STIF" version="444544"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:16:08.817373', '2017-03-23 13:16:08.817373', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (155354, NULL, 'FR:78217:ZDL:50008596:STIF', 1, 'chouette', 'EMILE SERGENT', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:21" version="443299" created="2015-02-03T06:02:33.0Z" changed="2015-02-03T06:02:33.0Z" id="FR:78217:ZDL:50008596:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78217:ZDL:420191:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>EMILE SERGENT</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50008596">
        <Town>Épône</Town>
        <PostalRegion>78217</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50008596">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:50009063:STIF" version="717630"/>
        <QuayRef ref="FR:78217:ZDE:50009064:STIF" version="443297"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:16:23.084787', '2017-03-23 13:16:23.084787', 'LOCAL_OBJECT');
      
      
-- Rambouillet  

-- 141019
INSERT INTO stop_areas VALUES (141019, NULL, 'FR:78517:ZDL:50111661:STIF', 1, 'chouette', 'RAMBOUILLET Garr Prairie', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:22" version="805978" created="2016-10-11T09:10:20.0Z" changed="2016-10-11T09:10:20.0Z" id="FR:78517:ZDL:50111661:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDL:43185:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>RAMBOUILLET Garr Prairie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50111661">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50111661">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78517:ZDE:50111663:STIF" version="805974"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:35:11.810166', '2017-03-23 12:35:11.810166', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (93531, 141019, 'FR:78517:ZDE:50111663:STIF', 1, 'chouette', 'RAMBOUILLET Gare Prairie', NULL, 'zdep', NULL, NULL, NULL, 1.8321858921295100, 48.6433370482632000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:22" version="805974" created="2016-10-11T09:10:04.0Z" changed="2016-10-11T09:10:04.0Z" id="FR:78517:ZDE:50111663:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDE:424802:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>005005060</Value>
        </KeyValue>
       </keyList>
       <Name>RAMBOUILLET Gare Prairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613965.563 6838764.946</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50111663">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50111663">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:47:52.859668', '2017-03-23 13:26:57.652355', 'LOCAL_OBJECT');
      
--       134332
INSERT INTO stop_areas VALUES (134332, NULL, 'FR:78497:ZDL:50015326:STIF', 1, 'chouette', 'Feuillettes', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682396" created="2015-05-19T05:05:54.0Z" changed="2015-05-19T05:05:54.0Z" id="FR:78497:ZDL:50015326:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDL:55012:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Feuillettes</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015326">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015326">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78497:ZDE:50016140:STIF" version="791825"/>
        <QuayRef ref="FR:78497:ZDE:50016141:STIF" version="791826"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:46.124258', '2017-03-23 12:26:46.124258', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (77234, 134332, 'FR:78497:ZDE:50016141:STIF', 1, 'chouette', 'Feuillettes', NULL, 'zdep', NULL, NULL, NULL, 1.7628821864459000, 48.6842415074444000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791826" created="2015-05-19T05:05:53.0Z" changed="2016-08-23T10:08:43.0Z" id="FR:78497:ZDE:50016141:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDE:31640:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013001</Value>
        </KeyValue>
       </keyList>
       <Name>Feuillettes</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">608931.517 6843389.748</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016141">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016141">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:51.061527', '2017-03-23 13:19:32.88684', 'LOCAL_OBJECT');

--      134330
INSERT INTO stop_areas VALUES (134330, NULL, 'FR:78497:ZDL:50015324:STIF', 1, 'chouette', 'Cerisaie', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682412" created="2015-05-19T05:05:02.0Z" changed="2015-05-19T05:05:02.0Z" id="FR:78497:ZDL:50015324:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDL:55010:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Cerisaie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015324">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015324">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78497:ZDE:50016136:STIF" version="791829"/>
        <QuayRef ref="FR:78497:ZDE:50016137:STIF" version="791830"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:45.948218', '2017-03-23 12:26:45.948218', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (77230, 134330, 'FR:78497:ZDE:50016136:STIF', 1, 'chouette', 'Cerisaie', NULL, 'zdep', NULL, NULL, NULL, 1.7651876163368700, 48.6766869744947000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791829" created="2015-05-19T05:05:00.0Z" changed="2016-08-23T10:08:47.0Z" id="FR:78497:ZDE:50016136:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDE:31636:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013001</Value>
        </KeyValue>
       </keyList>
       <Name>Cerisaie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">609088.08 6842547.297</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016136">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016136">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:50.86893', '2017-03-23 13:19:32.770089', 'LOCAL_OBJECT');

-- 134331
INSERT INTO stop_areas VALUES (134331, NULL, 'FR:78497:ZDL:50015325:STIF', 1, 'chouette', 'Cimetière', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682420" created="2015-05-19T05:05:06.0Z" changed="2015-05-19T05:05:06.0Z" id="FR:78497:ZDL:50015325:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDL:55011:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Cimetière</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015325">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015325">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78497:ZDE:50016138:STIF" version="791831"/>
        <QuayRef ref="FR:78497:ZDE:50016139:STIF" version="791832"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:46.046902', '2017-03-23 12:26:46.046902', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (77232, 134331, 'FR:78497:ZDE:50016139:STIF', 1, 'chouette', 'Cimetière', NULL, 'zdep', NULL, NULL, NULL, 1.7570581747502100, 48.6809266792408000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791832" created="2015-05-19T05:05:05.0Z" changed="2016-08-23T10:08:50.0Z" id="FR:78497:ZDE:50016139:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDE:31638:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013001</Value>
        </KeyValue>
       </keyList>
       <Name>Cimetière</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">608497.026 6843027.99</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016139">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016139">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:50.961463', '2017-03-23 13:19:32.842379', 'LOCAL_OBJECT');
-- 134333
INSERT INTO stop_areas VALUES (134333, NULL, 'FR:78497:ZDL:50015327:STIF', 1, 'chouette', 'Mairie', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682404" created="2015-05-19T05:05:58.0Z" changed="2015-05-19T05:05:58.0Z" id="FR:78497:ZDL:50015327:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDL:55013:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015327">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015327">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78497:ZDE:50016142:STIF" version="791827"/>
        <QuayRef ref="FR:78497:ZDE:50016143:STIF" version="791828"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:46.184083', '2017-03-23 12:26:46.184083', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (77237, 134333, 'FR:78497:ZDE:50016142:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.7537333180274800, 48.6761794180927000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791827" created="2015-05-19T05:05:56.0Z" changed="2016-08-23T10:08:45.0Z" id="FR:78497:ZDE:50016142:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDE:31643:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013001</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">608243.947 6842504.124</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016142">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016142">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:51.203986', '2017-03-23 13:19:32.918499', 'LOCAL_OBJECT');
      
-- 134333
INSERT INTO stop_areas VALUES (77236, 134333, 'FR:78497:ZDE:50016143:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.7533033868738600, 48.6760375221217000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791828" created="2015-05-19T05:05:57.0Z" changed="2016-08-23T10:08:46.0Z" id="FR:78497:ZDE:50016143:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDE:31642:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013001</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">608212.047 6842488.85</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016143">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016143">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:51.157822', '2017-03-23 13:19:32.944519', 'LOCAL_OBJECT');
      
--       134331
INSERT INTO stop_areas VALUES (77233, 134331, 'FR:78497:ZDE:50016138:STIF', 1, 'chouette', 'Cimetière', NULL, 'zdep', NULL, NULL, NULL, 1.7573635964898200, 48.6810720307394000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791831" created="2015-05-19T05:05:04.0Z" changed="2016-08-23T10:08:49.0Z" id="FR:78497:ZDE:50016138:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDE:31639:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013001</Value>
        </KeyValue>
       </keyList>
       <Name>Cimetière</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">608519.763 6843043.794</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016138">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016138">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:51.013935', '2017-03-23 13:19:32.816748', 'LOCAL_OBJECT');
      
-- 134330
INSERT INTO stop_areas VALUES (77231, 134330, 'FR:78497:ZDE:50016137:STIF', 1, 'chouette', 'Cerisaie', NULL, 'zdep', NULL, NULL, NULL, 1.7654056052053900, 48.6766392697890000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791830" created="2015-05-19T05:05:01.0Z" changed="2016-08-23T10:08:48.0Z" id="FR:78497:ZDE:50016137:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDE:31637:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013001</Value>
        </KeyValue>
       </keyList>
       <Name>Cerisaie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">609104.045 6842541.743</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016137">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016137">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:50.916728', '2017-03-23 13:19:32.785114', 'LOCAL_OBJECT');
-- 134332
INSERT INTO stop_areas VALUES (77235, 134332, 'FR:78497:ZDE:50016140:STIF', 1, 'chouette', 'Feuillettes', NULL, 'zdep', NULL, NULL, NULL, 1.7628209584199200, 48.6841242973463000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78497', 'Poigny-la-Forêt', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791825" created="2015-05-19T05:05:52.0Z" changed="2016-08-23T10:08:42.0Z" id="FR:78497:ZDE:50016140:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78497:ZDE:31641:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013001</Value>
        </KeyValue>
       </keyList>
       <Name>Feuillettes</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">608926.806 6843376.789</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016140">
        <Town>Poigny-la-Forêt</Town>
        <PostalRegion>78497</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016140">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:51.112195', '2017-03-23 13:19:32.872507', 'LOCAL_OBJECT');
-- 134368
INSERT INTO stop_areas VALUES (134368, NULL, 'FR:78569:ZDL:50015467:STIF', 1, 'chouette', 'Ville Lebrun', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78569', 'Sainte-Mesme', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="681176" created="2015-05-19T05:05:21.0Z" changed="2015-05-19T05:05:21.0Z" id="FR:78569:ZDL:50015467:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78569:ZDL:55049:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Ville Lebrun</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015467">
        <Town>Sainte-Mesme</Town>
        <PostalRegion>78569</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015467">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78569:ZDE:50016385:STIF" version="791633"/>
        <QuayRef ref="FR:78569:ZDE:50016384:STIF" version="791634"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:49.252944', '2017-03-23 12:26:49.252944', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (77306, 134368, 'FR:78569:ZDE:50016385:STIF', 1, 'chouette', 'Ville Lebrun', NULL, 'zdep', NULL, NULL, NULL, 1.9708101574504900, 48.5277924042932000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78569', 'Sainte-Mesme', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791633" created="2015-05-19T05:05:20.0Z" changed="2016-08-23T10:08:25.0Z" id="FR:78569:ZDE:50016385:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78569:ZDE:31704:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Ville Lebrun</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">624010.166 6825778.957</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016385">
        <Town>Sainte-Mesme</Town>
        <PostalRegion>78569</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016385">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:55.07004', '2017-03-23 13:19:40.784733', 'LOCAL_OBJECT');
-- 134365
INSERT INTO stop_areas VALUES (134365, NULL, 'FR:78569:ZDL:50015465:STIF', 1, 'chouette', 'Mairie', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78569', 'Sainte-Mesme', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="681168" created="2015-05-19T05:05:19.0Z" changed="2015-05-19T05:05:19.0Z" id="FR:78569:ZDL:50015465:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78569:ZDL:55046:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015465">
        <Town>Sainte-Mesme</Town>
        <PostalRegion>78569</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015465">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78569:ZDE:50016381:STIF" version="791631"/>
        <QuayRef ref="FR:78569:ZDE:50016380:STIF" version="791632"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:49.037056', '2017-03-23 12:26:49.037056', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (77300, 134365, 'FR:78569:ZDE:50016381:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.9608314665536100, 48.5309856657776000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78569', 'Sainte-Mesme', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791631" created="2015-05-19T05:05:18.0Z" changed="2016-08-23T10:08:24.0Z" id="FR:78569:ZDE:50016381:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78569:ZDE:31699:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">623278.107 6826143.552</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016381">
        <Town>Sainte-Mesme</Town>
        <PostalRegion>78569</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016381">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:54.791271', '2017-03-23 13:19:35.464111', 'LOCAL_OBJECT');
-- 134367
INSERT INTO stop_areas VALUES (134367, NULL, 'FR:78569:ZDL:50015464:STIF', 1, 'chouette', 'Denisy', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78569', 'Sainte-Mesme', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="681160" created="2015-05-19T05:05:16.0Z" changed="2015-05-19T05:05:16.0Z" id="FR:78569:ZDL:50015464:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78569:ZDL:55048:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Denisy</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015464">
        <Town>Sainte-Mesme</Town>
        <PostalRegion>78569</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015464">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78569:ZDE:50016379:STIF" version="681156"/>
        <QuayRef ref="FR:78569:ZDE:50016378:STIF" version="681158"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:49.213412', '2017-03-23 12:26:49.213412', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (77304, 134367, 'FR:78569:ZDE:50016379:STIF', 1, 'chouette', 'Denisy', NULL, 'zdep', NULL, NULL, NULL, 1.9387758033472900, 48.5424593525560000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78569', 'Sainte-Mesme', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="681156" created="2015-05-19T05:05:16.0Z" changed="2015-05-19T05:05:16.0Z" id="FR:78569:ZDE:50016379:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78569:ZDE:31702:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013003</Value>
        </KeyValue>
       </keyList>
       <Name>Denisy</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">621666.974 6827440.564</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016379">
        <Town>Sainte-Mesme</Town>
        <PostalRegion>78569</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016379">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:54.973043', '2017-03-23 13:19:40.41657', 'LOCAL_OBJECT');
-- 134338
INSERT INTO stop_areas VALUES (77246, 134338, 'FR:78499:ZDE:50016153:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.9115843650549900, 48.5532602646953000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78499', 'Ponthévrard', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="682012" created="2015-05-19T05:05:06.0Z" changed="2015-05-19T05:05:06.0Z" id="FR:78499:ZDE:50016153:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78499:ZDE:31652:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013026</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013003</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">619676.545 6828668.462</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016153">
        <Town>Ponthévrard</Town>
        <PostalRegion>78499</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016153">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:51.614037', '2017-03-23 13:19:33.188444', 'LOCAL_OBJECT');
-- 134339
INSERT INTO stop_areas VALUES (77248, 134339, 'FR:78499:ZDE:50016155:STIF', 1, 'chouette', 'Vallée Brun', NULL, 'zdep', NULL, NULL, NULL, 1.9078534670371400, 48.5557728860806000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78499', 'Ponthévrard', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791760" created="2015-05-19T05:05:03.0Z" changed="2016-08-23T10:08:22.0Z" id="FR:78499:ZDE:50016155:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78537:ZDE:31654:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013003</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013026</Value>
        </KeyValue>
       </keyList>
       <Name>Vallée Brun</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">619405.091 6828951.556</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016155">
        <Town>Ponthévrard</Town>
        <PostalRegion>78499</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016155">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:51.699663', '2017-03-23 13:19:33.236592', 'LOCAL_OBJECT');
-- 134339
INSERT INTO stop_areas VALUES (77249, 134339, 'FR:78499:ZDE:50016154:STIF', 1, 'chouette', 'Vallée Brun', NULL, 'zdep', NULL, NULL, NULL, 1.9078675294141900, 48.5557100135739000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78499', 'Ponthévrard', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="682006" created="2015-05-19T05:05:04.0Z" changed="2015-05-19T05:05:04.0Z" id="FR:78499:ZDE:50016154:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78499:ZDE:31655:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013026</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013003</Value>
        </KeyValue>
       </keyList>
       <Name>Vallée Brun</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">619406.032 6828944.553</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016154">
        <Town>Ponthévrard</Town>
        <PostalRegion>78499</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016154">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:51.745943', '2017-03-23 13:19:33.253128', 'LOCAL_OBJECT');
-- 134338
INSERT INTO stop_areas VALUES (77247, 134338, 'FR:78499:ZDE:50016152:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.9113658961638300, 48.5531373716927000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78499', 'Ponthévrard', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791761" created="2015-05-19T05:05:07.0Z" changed="2016-08-23T10:08:23.0Z" id="FR:78499:ZDE:50016152:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78499:ZDE:31653:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013003</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013026</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">619660.235 6828655.024</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016152">
        <Town>Ponthévrard</Town>
        <PostalRegion>78499</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016152">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:51.658661', '2017-03-23 13:19:33.207835', 'LOCAL_OBJECT');
-- 134367
INSERT INTO stop_areas VALUES (77303, 134367, 'FR:78569:ZDE:50016378:STIF', 1, 'chouette', 'Denisy', NULL, 'zdep', NULL, NULL, NULL, 1.9387220683448500, 48.5424052362841000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78569', 'Sainte-Mesme', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="681158" created="2015-05-19T05:05:16.0Z" changed="2015-05-19T05:05:16.0Z" id="FR:78569:ZDE:50016378:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78569:ZDE:31701:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013003</Value>
        </KeyValue>
       </keyList>
       <Name>Denisy</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">621662.927 6827434.602</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016378">
        <Town>Sainte-Mesme</Town>
        <PostalRegion>78569</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016378">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:54.925468', '2017-03-23 13:19:40.540916', 'LOCAL_OBJECT');
-- 134365
INSERT INTO stop_areas VALUES (77299, 134365, 'FR:78569:ZDE:50016380:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.9611994357089700, 48.5309742984147000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78569', 'Sainte-Mesme', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791632" created="2015-05-19T05:05:18.0Z" changed="2016-08-23T10:08:24.0Z" id="FR:78569:ZDE:50016380:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78569:ZDE:31698:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">623305.256 6826141.931</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016380">
        <Town>Sainte-Mesme</Town>
        <PostalRegion>78569</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016380">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:54.745791', '2017-03-23 13:19:35.961459', 'LOCAL_OBJECT');
      -- 134368
INSERT INTO stop_areas VALUES (77305, 134368, 'FR:78569:ZDE:50016384:STIF', 1, 'chouette', 'Ville Lebrun', NULL, 'zdep', NULL, NULL, NULL, 1.9709721525557100, 48.5278696020652000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78569', 'Sainte-Mesme', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791634" created="2015-05-19T05:05:20.0Z" changed="2016-08-23T10:08:25.0Z" id="FR:78569:ZDE:50016384:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78569:ZDE:31703:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Ville Lebrun</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">624022.238 6825787.382</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016384">
        <Town>Sainte-Mesme</Town>
        <PostalRegion>78569</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016384">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:55.023014', '2017-03-23 13:19:40.846872', 'LOCAL_OBJECT');
      -- 143139
INSERT INTO stop_areas VALUES (98795, 143139, 'FR:78517:ZDE:50076506:STIF', 1, 'chouette', 'Giroderie', NULL, 'zdep', NULL, NULL, NULL, 1.8484245917049600, 48.6446005841075000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:24" version="677652" created="2015-05-19T02:05:19.0Z" changed="2015-05-19T02:05:19.0Z" id="FR:78517:ZDE:50076506:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDE:4594:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039003</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039303</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039003</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039203</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039303</Value>
        </KeyValue>
       </keyList>
       <Name>Giroderie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">615163.853 6838887.834</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50076506">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50076506">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:52:09.891719', '2017-03-23 13:29:39.697142', 'LOCAL_OBJECT');
      -- 134232
INSERT INTO stop_areas VALUES (77033, 134232, 'FR:78164:ZDE:50015685:STIF', 1, 'chouette', 'Croix', NULL, 'zdep', NULL, NULL, NULL, 1.9046964337120800, 48.6196763102360000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78164', 'Clairefontaine-en-Yvelines', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="683684" created="2015-05-19T05:05:06.0Z" changed="2015-05-19T05:05:06.0Z" id="FR:78164:ZDE:50015685:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78164:ZDE:31934:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013029</Value>
        </KeyValue>
       </keyList>
       <Name>Croix</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">619270.672 6836058.203</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015685">
        <Town>Clairefontaine-en-Yvelines</Town>
        <PostalRegion>78164</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015685">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:41.809841', '2017-03-23 13:19:27.216729', 'LOCAL_OBJECT');
      -- 149199
INSERT INTO stop_areas VALUES (114340, 149199, 'FR:78164:ZDE:50015683:STIF', 1, 'chouette', 'Centre', NULL, 'zdep', NULL, NULL, NULL, 1.9086562541416600, 48.6127182088555000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78164', 'Clairefontaine-en-Yvelines', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792032" created="2015-05-19T05:05:00.0Z" changed="2016-08-23T10:08:57.0Z" id="FR:78164:ZDE:50015683:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78164:ZDE:31946:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013029</Value>
        </KeyValue>
       </keyList>
       <Name>Centre</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">619551.821 6835280.69</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015683">
        <Town>Clairefontaine-en-Yvelines</Town>
        <PostalRegion>78164</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015683">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:29.193565', '2017-03-23 13:36:59.734816', 'LOCAL_OBJECT');
      -- 134233
INSERT INTO stop_areas VALUES (77034, 134233, 'FR:78164:ZDE:50015687:STIF', 1, 'chouette', 'Montjoie', NULL, 'zdep', NULL, NULL, NULL, 1.9151927599455300, 48.6177364944117000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78164', 'Clairefontaine-en-Yvelines', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="683660" created="2015-05-19T05:05:49.0Z" changed="2015-05-19T05:05:49.0Z" id="FR:78164:ZDE:50015687:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78164:ZDE:31895:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Montjoie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">620041.292 6835831.89</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015687">
        <Town>Clairefontaine-en-Yvelines</Town>
        <PostalRegion>78164</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015687">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:41.863431', '2017-03-23 13:19:27.271853', 'LOCAL_OBJECT');
      -- 149197
INSERT INTO stop_areas VALUES (114336, 149197, 'FR:78125:ZDE:50015678:STIF', 1, 'chouette', 'Villeneuve', NULL, 'zdep', NULL, NULL, NULL, 1.9437545019195500, 48.6326668106157000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78125', 'La Celle-les-Bordes', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="683764" created="2015-05-19T05:05:00.0Z" changed="2015-05-19T05:05:00.0Z" id="FR:78125:ZDE:50015678:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78125:ZDE:31960:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Villeneuve</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">622168.589 6837463.045</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015678">
        <Town>La Celle-les-Bordes</Town>
        <PostalRegion>78125</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015678">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:27.537169', '2017-03-23 13:36:59.612736', 'LOCAL_OBJECT');
      -- 149196
INSERT INTO stop_areas VALUES (114334, 149196, 'FR:78125:ZDE:50015676:STIF', 1, 'chouette', 'Bois des Gaulles', NULL, 'zdep', NULL, NULL, NULL, 1.9570840825980200, 48.6402970484119000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78125', 'La Celle-les-Bordes', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="683772" created="2015-05-19T05:05:06.0Z" changed="2015-05-19T05:05:06.0Z" id="FR:78125:ZDE:50015676:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78125:ZDE:31961:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Bois des Gaulles</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">623161.95 6838298.198</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015676">
        <Town>La Celle-les-Bordes</Town>
        <PostalRegion>78125</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015676">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:27.441917', '2017-03-23 13:36:59.557159', 'LOCAL_OBJECT');
      -- 149194
INSERT INTO stop_areas VALUES (114331, 149194, 'FR:78125:ZDE:50015672:STIF', 1, 'chouette', 'Château', NULL, 'zdep', NULL, NULL, NULL, 1.9664965769673000, 48.6428434929102000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78125', 'La Celle-les-Bordes', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792045" created="2015-05-19T05:05:33.0Z" changed="2016-08-23T10:08:16.0Z" id="FR:78125:ZDE:50015672:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78125:ZDE:424188:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Château</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">623859.092 6838572.155</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015672">
        <Town>La Celle-les-Bordes</Town>
        <PostalRegion>78125</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015672">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:27.290267', '2017-03-23 13:36:59.445716', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (156195, NULL, 'FR:78120:ZDL:50015082:STIF', 1, 'chouette', 'Bullion Ronqueux', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78120', 'Bullion', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="683734" created="2015-05-19T05:05:40.0Z" changed="2015-05-19T05:05:40.0Z" id="FR:78120:ZDL:50015082:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78120:ZDL:57562:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Bullion Ronqueux</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015082">
        <Town>Bullion</Town>
        <PostalRegion>78120</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015082">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78120:ZDE:50015671:STIF" version="792046"/>
        <QuayRef ref="FR:78120:ZDE:50015670:STIF" version="792047"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:17:11.33077', '2017-03-23 13:17:11.33077', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (131451, 156195, 'FR:78120:ZDE:50015671:STIF', 1, 'chouette', 'Ronqueux', NULL, 'zdep', NULL, NULL, NULL, 1.9867288563947600, 48.6410984444406000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78120', 'Bullion', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792046" created="2015-05-19T05:05:37.0Z" changed="2016-08-23T10:08:18.0Z" id="FR:78120:ZDE:50015671:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78120:ZDE:31975:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Ronqueux</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">625347.085 6838358.848</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015671">
        <Town>Bullion</Town>
        <PostalRegion>78120</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015671">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:23:29.302182', '2017-03-23 12:23:29.302182', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (156193, NULL, 'FR:78120:ZDL:50015080:STIF', 1, 'chouette', 'Longchêne', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78120', 'Bullion', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="683750" created="2015-05-19T05:05:51.0Z" changed="2015-05-19T05:05:51.0Z" id="FR:78120:ZDL:50015080:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78120:ZDL:48655:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Longchêne</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015080">
        <Town>Bullion</Town>
        <PostalRegion>78120</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015080">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78120:ZDE:50015667:STIF" version="792049"/>
        <QuayRef ref="FR:78120:ZDE:50015666:STIF" version="792050"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:17:11.218807', '2017-03-23 13:17:11.218807', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (131448, 156193, 'FR:78120:ZDE:50015667:STIF', 1, 'chouette', 'Longchêne Place', NULL, 'zdep', NULL, NULL, NULL, 2.0022086604000400, 48.6384554259711000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78120', 'Bullion', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792049" created="2015-05-19T05:05:48.0Z" changed="2016-08-23T10:08:22.0Z" id="FR:78120:ZDE:50015667:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78120:ZDE:31935:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013029</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Longchêne Place</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">626483.788 6838050.513</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015667">
        <Town>Bullion</Town>
        <PostalRegion>78120</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015667">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:23:29.079222', '2017-03-23 12:23:29.079222', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (131446, 156193, 'FR:78120:ZDE:50015666:STIF', 1, 'chouette', 'Longchêne Place', NULL, 'zdep', NULL, NULL, NULL, 2.0021368943189800, 48.6383332579947000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78120', 'Bullion', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792050" created="2015-05-19T05:05:49.0Z" changed="2016-08-23T10:08:23.0Z" id="FR:78120:ZDE:50015666:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78120:ZDE:31924:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013029</Value>
        </KeyValue>
       </keyList>
       <Name>Longchêne Place</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">626478.329 6838036.999</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015666">
        <Town>Bullion</Town>
        <PostalRegion>78120</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015666">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:23:28.959433', '2017-03-23 12:23:28.959433', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (131450, 156195, 'FR:78120:ZDE:50015670:STIF', 1, 'chouette', 'Ronqueux', NULL, 'zdep', NULL, NULL, NULL, 1.9868653924334700, 48.6409640054408000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78120', 'Bullion', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792047" created="2015-05-19T05:05:38.0Z" changed="2016-08-23T10:08:19.0Z" id="FR:78120:ZDE:50015670:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78120:ZDE:31974:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Ronqueux</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">625356.952 6838343.774</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015670">
        <Town>Bullion</Town>
        <PostalRegion>78120</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015670">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:23:29.246306', '2017-03-23 12:23:29.246306', 'LOCAL_OBJECT');
      -- 149194
INSERT INTO stop_areas VALUES (114330, 149194, 'FR:78125:ZDE:50015673:STIF', 1, 'chouette', 'Château', NULL, 'zdep', NULL, NULL, NULL, 1.9663622945750800, 48.6426795158335000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78125', 'La Celle-les-Bordes', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792044" created="2015-05-19T05:05:32.0Z" changed="2016-08-23T10:08:15.0Z" id="FR:78125:ZDE:50015673:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78125:ZDE:31314:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Château</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">623848.961 6838554.056</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015673">
        <Town>La Celle-les-Bordes</Town>
        <PostalRegion>78125</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015673">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:27.238243', '2017-03-23 13:36:59.420194', 'LOCAL_OBJECT');
      -- 149196
INSERT INTO stop_areas VALUES (114335, 149196, 'FR:78125:ZDE:50015677:STIF', 1, 'chouette', 'Bois des Gaulles', NULL, 'zdep', NULL, NULL, NULL, 1.9568776858763200, 48.6402818199532000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78125', 'La Celle-les-Bordes', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792053" created="2015-05-19T05:05:04.0Z" changed="2016-08-23T10:08:28.0Z" id="FR:78125:ZDE:50015677:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78125:ZDE:31317:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Bois des Gaulles</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">623146.722 6838296.706</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015677">
        <Town>La Celle-les-Bordes</Town>
        <PostalRegion>78125</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015677">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:27.493677', '2017-03-23 13:36:59.531455', 'LOCAL_OBJECT');
      -- 149197
INSERT INTO stop_areas VALUES (114337, 149197, 'FR:78125:ZDE:50015679:STIF', 1, 'chouette', 'Villeneuve', NULL, 'zdep', NULL, NULL, NULL, 1.9436675596451700, 48.6327193395303000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78125', 'La Celle-les-Bordes', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792052" created="2015-05-19T05:05:59.0Z" changed="2016-08-23T10:08:26.0Z" id="FR:78125:ZDE:50015679:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78125:ZDE:31318:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Villeneuve</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">622162.261 6837468.97</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015679">
        <Town>La Celle-les-Bordes</Town>
        <PostalRegion>78125</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015679">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:27.578741', '2017-03-23 13:36:59.58858', 'LOCAL_OBJECT');
      -- 134233
INSERT INTO stop_areas VALUES (77035, 134233, 'FR:78164:ZDE:50015686:STIF', 1, 'chouette', 'Montjoie', NULL, 'zdep', NULL, NULL, NULL, 1.9154950897015000, 48.6178362779646000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78164', 'Clairefontaine-en-Yvelines', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792030" created="2015-05-19T05:05:48.0Z" changed="2016-08-23T10:08:52.0Z" id="FR:78164:ZDE:50015686:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78164:ZDE:31901:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Montjoie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">620063.727 6835842.676</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015686">
        <Town>Clairefontaine-en-Yvelines</Town>
        <PostalRegion>78164</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015686">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:41.900819', '2017-03-23 13:19:27.245978', 'LOCAL_OBJECT');
      -- 149199
INSERT INTO stop_areas VALUES (114341, 149199, 'FR:78164:ZDE:50015682:STIF', 1, 'chouette', 'Centre', NULL, 'zdep', NULL, NULL, NULL, 1.9088500486871000, 48.6125179626097000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78164', 'Clairefontaine-en-Yvelines', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="683674" created="2015-05-19T05:05:59.0Z" changed="2015-05-19T05:05:59.0Z" id="FR:78164:ZDE:50015682:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78164:ZDE:31321:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013029</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
       </keyList>
       <Name>Centre</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">619565.798 6835258.233</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015682">
        <Town>Clairefontaine-en-Yvelines</Town>
        <PostalRegion>78164</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015682">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:29.24673', '2017-03-23 13:36:59.710879', 'LOCAL_OBJECT');
      -- 134232
INSERT INTO stop_areas VALUES (77032, 134232, 'FR:78164:ZDE:50015684:STIF', 1, 'chouette', 'Croix', NULL, 'zdep', NULL, NULL, NULL, 1.9047861604737300, 48.6186698446081000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78164', 'Clairefontaine-en-Yvelines', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="683682" created="2015-05-19T05:05:05.0Z" changed="2015-05-19T05:05:05.0Z" id="FR:78164:ZDE:50015684:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78164:ZDE:31909:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013029</Value>
        </KeyValue>
       </keyList>
       <Name>Croix</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">619275.733 6835946.231</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015684">
        <Town>Clairefontaine-en-Yvelines</Town>
        <PostalRegion>78164</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015684">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:41.72119', '2017-03-23 13:19:27.190821', 'LOCAL_OBJECT');
      -- 143139
INSERT INTO stop_areas VALUES (98794, 143139, 'FR:78517:ZDE:50076505:STIF', 1, 'chouette', 'Giroderie', NULL, 'zdep', NULL, NULL, NULL, 1.8484364624986800, 48.6447804082950000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:24" version="677650" created="2015-05-19T02:05:18.0Z" changed="2015-05-19T02:05:18.0Z" id="FR:78517:ZDE:50076505:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDE:4593:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039303</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039303</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039003</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039203</Value>
        </KeyValue>
       </keyList>
       <Name>Giroderie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">615165.019 6838907.811</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50076505">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50076505">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:52:09.849393', '2017-03-23 13:29:39.642626', 'LOCAL_OBJECT');
      -- 134317
INSERT INTO stop_areas VALUES (77205, 134317, 'FR:78464:ZDE:50016099:STIF', 1, 'chouette', 'Guillemets', NULL, 'zdep', NULL, NULL, NULL, 1.8052608800689500, 48.6074151542779000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78464', 'Orcemont', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="682334" created="2015-05-19T05:05:25.0Z" changed="2015-05-19T05:05:25.0Z" id="FR:78464:ZDE:50016099:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78464:ZDE:31612:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
       </keyList>
       <Name>Guillemets</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611921.689 6834801.54</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016099">
        <Town>Orcemont</Town>
        <PostalRegion>78464</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016099">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:49.736253', '2017-03-23 13:19:32.062381', 'LOCAL_OBJECT');
      -- 134315
INSERT INTO stop_areas VALUES (77203, 134315, 'FR:78464:ZDE:50016096:STIF', 1, 'chouette', 'Centre', NULL, 'zdep', NULL, NULL, NULL, 1.8113757263957000, 48.5874560365627000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78464', 'Orcemont', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791797" created="2015-05-19T05:05:43.0Z" changed="2016-08-23T10:08:05.0Z" id="FR:78464:ZDE:50016096:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78464:ZDE:31610:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
       </keyList>
       <Name>Centre</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612339.053 6832576.128</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016096">
        <Town>Orcemont</Town>
        <PostalRegion>78464</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016096">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:49.646019', '2017-03-23 13:19:31.998178', 'LOCAL_OBJECT');
      
INSERT INTO stop_areas VALUES (156198, NULL, 'FR:78470:ZDL:50015308:STIF', 1, 'chouette', 'Les Coudraies', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78470', 'Orphin', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682309" created="2015-05-19T05:05:14.0Z" changed="2015-05-19T05:05:14.0Z" id="FR:78470:ZDL:50015308:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78470:ZDL:54999:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Les Coudraies</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015308">
        <Town>Orphin</Town>
        <PostalRegion>78470</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015308">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78470:ZDE:50016107:STIF" version="791803"/>
        <QuayRef ref="FR:78470:ZDE:50016108:STIF" version="791804"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:17:11.492129', '2017-03-23 13:17:11.492129', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (131456, 156198, 'FR:78470:ZDE:50016107:STIF', 1, 'chouette', 'Les Coudrayes', NULL, 'zdep', NULL, NULL, NULL, 1.7833544651190200, 48.5781439589585000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78470', 'Orphin', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791803" created="2015-05-19T05:05:12.0Z" changed="2016-08-23T10:08:15.0Z" id="FR:78470:ZDE:50016107:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78470:ZDE:31618:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
       </keyList>
       <Name>Les Coudrayes</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">610256.699 6831572.512</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016107">
        <Town>Orphin</Town>
        <PostalRegion>78470</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016107">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:23:29.787805', '2017-03-23 12:23:29.787805', 'LOCAL_OBJECT');
      -- 134322
INSERT INTO stop_areas VALUES (77215, 134322, 'FR:78470:ZDE:50016111:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.7792962365663400, 48.5781823017855000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78470', 'Orphin', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791800" created="2015-05-19T05:05:07.0Z" changed="2016-08-23T10:08:10.0Z" id="FR:78470:ZDE:50016111:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78470:ZDE:31622:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">609957.442 6831581.394</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016111">
        <Town>Orphin</Town>
        <PostalRegion>78470</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016111">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:50.181802', '2017-03-23 13:19:32.349285', 'LOCAL_OBJECT');
      -- 134322 
INSERT INTO stop_areas VALUES (77216, 134322, 'FR:78470:ZDE:50016114:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.7799520175159900, 48.5781599420635000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78470', 'Orphin', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791799" created="2015-05-19T05:05:48.0Z" changed="2016-08-23T10:08:09.0Z" id="FR:78470:ZDE:50016114:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78470:ZDE:31850:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">610005.772 6831578.161</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016114">
        <Town>Orphin</Town>
        <PostalRegion>78470</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016114">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:50.226467', '2017-03-23 13:19:32.374384', 'LOCAL_OBJECT');
      -- 
INSERT INTO stop_areas VALUES (131457, 156198, 'FR:78470:ZDE:50016108:STIF', 1, 'chouette', 'Les Coudrayes', NULL, 'zdep', NULL, NULL, NULL, 1.7848618101186100, 48.5778093828306000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78470', 'Orphin', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791804" created="2015-05-19T05:05:13.0Z" changed="2016-08-23T10:08:16.0Z" id="FR:78470:ZDE:50016108:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78470:ZDE:31619:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
       </keyList>
       <Name>Les Coudrayes</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">610367.304 6831533.61</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016108">
        <Town>Orphin</Town>
        <PostalRegion>78470</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016108">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:23:29.876632', '2017-03-23 12:23:29.876632', 'LOCAL_OBJECT');
      -- 134315
INSERT INTO stop_areas VALUES (77202, 134315, 'FR:78464:ZDE:50016097:STIF', 1, 'chouette', 'Centre', NULL, 'zdep', NULL, NULL, NULL, 1.8110570671769500, 48.5878260382743000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78464', 'Orcemont', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="682239" created="2015-05-19T05:05:43.0Z" changed="2015-05-19T05:05:43.0Z" id="FR:78464:ZDE:50016097:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78464:ZDE:31609:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
       </keyList>
       <Name>Centre</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612316.173 6832617.61</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016097">
        <Town>Orcemont</Town>
        <PostalRegion>78464</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016097">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:49.595935', '2017-03-23 13:19:31.972267', 'LOCAL_OBJECT');
      -- 134317
INSERT INTO stop_areas VALUES (77206, 134317, 'FR:78464:ZDE:50016100:STIF', 1, 'chouette', 'Guillemets', NULL, 'zdep', NULL, NULL, NULL, 1.8055587127468900, 48.6074424592190000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78464', 'Orcemont', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791811" created="2015-05-19T05:05:26.0Z" changed="2016-08-23T10:08:25.0Z" id="FR:78464:ZDE:50016100:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78464:ZDE:31613:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
       </keyList>
       <Name>Guillemets</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">611943.69 6834804.243</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016100">
        <Town>Orcemont</Town>
        <PostalRegion>78464</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016100">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:49.781355', '2017-03-23 13:19:32.08903', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (156300, NULL, 'FR:78269:ZDL:50111660:STIF', 1, 'chouette', 'RAMBOUILLET Arbouville', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78269', 'Gazeran', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:22" version="805991" created="2016-10-11T09:10:53.0Z" changed="2016-10-11T09:10:53.0Z" id="FR:78269:ZDL:50111660:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78269:ZDL:424804:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>RAMBOUILLET Arbouville</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50111660">
        <Town>Gazeran</Town>
        <PostalRegion>78269</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50111660">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78269:ZDE:50111664:STIF" version="805981"/>
        <QuayRef ref="FR:78269:ZDE:50111665:STIF" version="805989"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:17:17.693483', '2017-03-23 13:17:17.693483', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (131672, 156300, 'FR:78269:ZDE:50111665:STIF', 1, 'chouette', 'Arbouville', NULL, 'zdep', NULL, NULL, NULL, 1.8181509735314600, 48.6360761753006000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78269', 'Gazeran', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:22" version="805989" created="2016-10-11T09:10:47.0Z" changed="2016-10-11T09:10:47.0Z" id="FR:78269:ZDE:50111665:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78269:ZDE:424803:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>005005060</Value>
        </KeyValue>
       </keyList>
       <Name>Arbouville</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612919.587 6837973.199</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50111665">
        <Town>Gazeran</Town>
        <PostalRegion>78269</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50111665">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:23:49.131373', '2017-03-23 12:23:49.131373', 'LOCAL_OBJECT');
      -- 137572
INSERT INTO stop_areas VALUES (85387, 137572, 'FR:78517:ZDE:50076501:STIF', 1, 'chouette', 'Collège Racinay', NULL, 'zdep', NULL, NULL, NULL, 1.8231950380281900, 48.6348396466707000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:24" version="732666" created="2015-05-19T02:05:12.0Z" changed="2015-10-27T09:10:40.0Z" id="FR:78517:ZDE:50076501:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDE:17580:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039003</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039203</Value>
        </KeyValue>
       </keyList>
       <Name>Collège Racinay</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613289.165 6837830.194</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50076501">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50076501">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:41:41.295571', '2017-03-23 13:23:14.8419', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (156517, NULL, 'FR:78517:ZDL:50015444:STIF', 1, 'chouette', 'De Vivonne', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="791685" created="2015-05-19T05:05:01.0Z" changed="2016-08-23T10:08:13.0Z" id="FR:78517:ZDL:50015444:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDL:42688:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>De Vivonne</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015444">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015444">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78517:ZDE:50016341:STIF" version="791684"/>
        <QuayRef ref="FR:78517:ZDE:50112102:STIF" version="796818"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:17:29.71562', '2017-03-23 13:17:29.71562', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (132100, 156517, 'FR:78517:ZDE:50016341:STIF', 1, 'chouette', 'De Vivonne', NULL, 'zdep', NULL, NULL, NULL, 1.8486886950553000, 48.6481238695820000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791684" created="2015-05-19T05:05:00.0Z" changed="2016-08-23T10:08:12.0Z" id="FR:78517:ZDE:50016341:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDE:31292:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013001</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013100</Value>
        </KeyValue>
       </keyList>
       <Name>De Vivonne</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">615189.019 6839279.209</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016341">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016341">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:24:21.769979', '2017-03-23 12:24:21.769979', 'LOCAL_OBJECT');
      -- 142981
INSERT INTO stop_areas VALUES (98357, 142981, 'FR:78486:ZDE:50015940:STIF', 1, 'chouette', 'Gare du Perray en Yvelines', NULL, 'zdep', NULL, NULL, NULL, 1.8564583320880800, 48.6940943006237000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78486', 'Le Perray-en-Yvelines', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791903" created="2015-05-19T05:05:36.0Z" changed="2016-08-23T10:08:19.0Z" id="FR:78486:ZDE:50015940:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78486:ZDE:31791:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013059</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013019</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013039</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
       </keyList>
       <Name>Gare du Perray en Yvelines</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">615835.34 6844381.213</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015940">
        <Town>Le Perray-en-Yvelines</Town>
        <PostalRegion>78486</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015940">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:51:51.23602', '2017-03-23 13:29:17.539805', 'LOCAL_OBJECT');
      -- 136607
INSERT INTO stop_areas VALUES (136607, NULL, 'FR:78030:ZDE:50015569:STIF', 1, 'chouette', 'Carrières', NULL, 'zdep', NULL, NULL, NULL, 1.8681967809156600, 48.6933423252341000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78030', 'Auffargis', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792079" created="2015-05-19T05:05:59.0Z" changed="2016-08-23T10:08:14.0Z" id="FR:78030:ZDE:50015569:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78486:ZDE:31801:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
       </keyList>
       <Name>Carrières</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">616698.032 6844285.17</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015569">
        <Town>Auffargis</Town>
        <PostalRegion>78030</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015569">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:39:56.808113', '2017-03-23 13:22:12.952721', 'LOCAL_OBJECT');
      -- 149176
INSERT INTO stop_areas VALUES (114296, 149176, 'FR:78030:ZDE:50015577:STIF', 1, 'chouette', 'Route du Perray', NULL, 'zdep', NULL, NULL, NULL, 1.8833187287035400, 48.6975412000902000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78030', 'Auffargis', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792072" created="2015-05-19T05:05:36.0Z" changed="2016-08-23T10:08:03.0Z" id="FR:78030:ZDE:50015577:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78030:ZDE:31876:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013039</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013019</Value>
        </KeyValue>
       </keyList>
       <Name>Route du Perray</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">617817.551 6844736.105</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015577">
        <Town>Auffargis</Town>
        <PostalRegion>78030</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015577">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:25.547183', '2017-03-23 13:36:58.510134', 'LOCAL_OBJECT');
      -- 134287
INSERT INTO stop_areas VALUES (77146, 134287, 'FR:78486:ZDE:50015930:STIF', 1, 'chouette', 'Champ de Foire', NULL, 'zdep', NULL, NULL, NULL, 1.8563036835737800, 48.6969101834133000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78486', 'Le Perray-en-Yvelines', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791925" created="2015-05-19T05:05:07.0Z" changed="2016-08-23T10:08:54.0Z" id="FR:78486:ZDE:50015930:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78486:ZDE:31485:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013059</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013012</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013089</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013019</Value>
        </KeyValue>
       </keyList>
       <Name>Champ de Foire</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">615828.493 6844694.413</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015930">
        <Town>Le Perray-en-Yvelines</Town>
        <PostalRegion>78486</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015930">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:46.934909', '2017-03-23 13:19:30.383858', 'LOCAL_OBJECT');
      -- 134289
INSERT INTO stop_areas VALUES (77151, 134289, 'FR:78486:ZDE:50015937:STIF', 1, 'chouette', 'Fer Ouvré', NULL, 'zdep', NULL, NULL, NULL, 1.8597129957288300, 48.6997882306510000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78486', 'Le Perray-en-Yvelines', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791893" created="2015-05-19T05:05:11.0Z" changed="2016-08-23T10:08:06.0Z" id="FR:78486:ZDE:50015937:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78486:ZDE:31493:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013019</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
       </keyList>
       <Name>Fer Ouvré</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">616084.008 6845010.731</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015937">
        <Town>Le Perray-en-Yvelines</Town>
        <PostalRegion>78486</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015937">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:47.157865', '2017-03-23 13:19:30.494273', 'LOCAL_OBJECT');
      -- 142959
INSERT INTO stop_areas VALUES (98290, 142959, 'FR:78220:ZDE:50076444:STIF', 1, 'chouette', 'Gare des Essarts le Roi', NULL, 'zdep', NULL, NULL, NULL, 1.8911278224161900, 48.7220006562091000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78220', 'Les Essarts-le-Roi', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:24" version="677923" created="2015-05-19T02:05:26.0Z" changed="2015-05-19T02:05:26.0Z" id="FR:78220:ZDE:50076444:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78220:ZDE:4555:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039027</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>039039027</Value>
        </KeyValue>
       </keyList>
       <Name>Gare des Essarts le Roi</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">618430.413 6847447.157</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50076444">
        <Town>Les Essarts-le-Roi</Town>
        <PostalRegion>78220</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50076444">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:51:48.600083', '2017-03-23 13:29:16.127371', 'LOCAL_OBJECT');
      -- 134289
INSERT INTO stop_areas VALUES (77150, 134289, 'FR:78486:ZDE:50015936:STIF', 1, 'chouette', 'Fer Ouvré', NULL, 'zdep', NULL, NULL, NULL, 1.8595850221503300, 48.6996158250589000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78486', 'Le Perray-en-Yvelines', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791892" created="2015-05-19T05:05:10.0Z" changed="2016-08-23T10:08:05.0Z" id="FR:78486:ZDE:50015936:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78486:ZDE:31792:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013019</Value>
        </KeyValue>
       </keyList>
       <Name>Fer Ouvré</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">616074.314 6844991.701</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015936">
        <Town>Le Perray-en-Yvelines</Town>
        <PostalRegion>78486</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015936">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:47.111911', '2017-03-23 13:19:30.47032', 'LOCAL_OBJECT');
      -- 134287
INSERT INTO stop_areas VALUES (77147, 134287, 'FR:78486:ZDE:50015931:STIF', 1, 'chouette', 'Champ de Foire', NULL, 'zdep', NULL, NULL, NULL, 1.8564394899959600, 48.6971548203544000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78486', 'Le Perray-en-Yvelines', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791924" created="2015-05-19T05:05:06.0Z" changed="2016-08-23T10:08:53.0Z" id="FR:78486:ZDE:50015931:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78486:ZDE:31486:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013059</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013012</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013089</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013019</Value>
        </KeyValue>
       </keyList>
       <Name>Champ de Foire</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">615838.881 6844721.464</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015931">
        <Town>Le Perray-en-Yvelines</Town>
        <PostalRegion>78486</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015931">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:46.979448', '2017-03-23 13:19:30.367454', 'LOCAL_OBJECT');
      -- 149176
INSERT INTO stop_areas VALUES (114297, 149176, 'FR:78030:ZDE:50015576:STIF', 1, 'chouette', 'Route du Perray', NULL, 'zdep', NULL, NULL, NULL, 1.8829420141483000, 48.6974201122419000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78030', 'Auffargis', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792071" created="2015-05-19T05:05:34.0Z" changed="2016-08-23T10:08:02.0Z" id="FR:78030:ZDE:50015576:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78030:ZDE:31898:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013039</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013019</Value>
        </KeyValue>
       </keyList>
       <Name>Route du Perray</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">617789.638 6844723.036</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015576">
        <Town>Auffargis</Town>
        <PostalRegion>78030</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015576">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:25.602019', '2017-03-23 13:36:58.485207', 'LOCAL_OBJECT');
      -- 136607
INSERT INTO stop_areas VALUES (82959, 136607, 'FR:78030:ZDE:50015568:STIF', 1, 'chouette', 'Carrières', NULL, 'zdep', NULL, NULL, NULL, 1.8680868765064500, 48.6933941123016000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78030', 'Auffargis', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792078" created="2015-05-19T05:05:57.0Z" changed="2016-08-23T10:08:13.0Z" id="FR:78030:ZDE:50015568:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78486:ZDE:31800:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
       </keyList>
       <Name>Carrières</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">616690.026 6844291.043</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015568">
        <Town>Auffargis</Town>
        <PostalRegion>78030</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015568">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:39:56.749483', '2017-03-23 13:22:12.926767', 'LOCAL_OBJECT');
      -- 
INSERT INTO stop_areas VALUES (156516, NULL, 'FR:78517:ZDL:50109943:STIF', 1, 'chouette', 'Collège De Vivonne', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="791510" created="2016-08-23T10:08:13.0Z" changed="2016-08-23T10:08:13.0Z" id="FR:78517:ZDL:50109943:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDL:42688:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Collège De Vivonne</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50109943">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50109943">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78517:ZDE:50016340:STIF" version="791509"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:17:29.692451', '2017-03-23 13:17:29.692451', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (132099, 156516, 'FR:78517:ZDE:50016340:STIF', 1, 'chouette', 'Collège de Vivonne', NULL, 'zdep', NULL, NULL, NULL, 1.8484137774675400, 48.6479340735053000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791509" created="2015-05-19T05:05:00.0Z" changed="2016-08-23T10:08:13.0Z" id="FR:78517:ZDE:50016340:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDE:31278:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
       </keyList>
       <Name>Collège de Vivonne</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">615168.461 6839258.406</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016340">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016340">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:24:21.715039', '2017-03-23 12:24:21.715039', 'LOCAL_OBJECT');
      -- 137570
INSERT INTO stop_areas VALUES (85385, 137570, 'FR:78517:ZDE:50016206:STIF', 1, 'chouette', 'Collège Racinay', NULL, 'zdep', NULL, NULL, NULL, 1.8237401132170700, 48.6349797711267000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791607" created="2015-05-19T04:05:37.0Z" changed="2016-08-23T10:08:07.0Z" id="FR:78517:ZDE:50016206:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDE:31211:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013029</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013102</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013019</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013003</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013024</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013001</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013030</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013020</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
       </keyList>
       <Name>Collège Racinay</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613329.557 6837845.172</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016206">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016206">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:41:41.20784', '2017-03-23 13:23:14.79373', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (131671, 156300, 'FR:78269:ZDE:50111664:STIF', 1, 'chouette', 'Arbouville', NULL, 'zdep', NULL, NULL, NULL, 1.8181645450568300, 48.6360762379725000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78269', 'Gazeran', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:22" version="805981" created="2016-10-11T09:10:27.0Z" changed="2016-10-11T09:10:27.0Z" id="FR:78269:ZDE:50111664:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78269:ZDE:424803:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>005005060</Value>
        </KeyValue>
       </keyList>
       <Name>Arbouville</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">612920.587 6837973.191</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50111664">
        <Town>Gazeran</Town>
        <PostalRegion>78269</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50111664">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:23:49.070195', '2017-03-23 12:23:49.070195', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (122614, NULL, 'FR:91200:ZDE:50097522:STIF', 1, 'chouette', 'GARE DE DOURDAN LA FORET', NULL, 'zdep', NULL, NULL, NULL, 1.9958981545942500, 48.5354819333277000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '91200', 'Dourdan', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:76" version="714443" created="2015-06-15T10:06:08.0Z" changed="2015-06-26T09:06:35.0Z" id="FR:91200:ZDE:50097522:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:91200:ZDE:418737:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800803571</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800803571</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800803571</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800803571</Value>
        </KeyValue>
       </keyList>
       <Name>GARE DE DOURDAN LA FORET</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">625873.297 6826609.84</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50097522">
        <Town>Dourdan</Town>
        <PostalRegion>91200</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50097522">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:14:44.654642', '2017-03-23 12:14:44.654642', 'LOCAL_OBJECT');
      -- 140813
INSERT INTO stop_areas VALUES (92773, NULL, 'FR:91200:ZDE:50097554:STIF', 1, 'chouette', 'GARE DE DOURDAN', NULL, 'zdep', NULL, NULL, NULL, 2.0097168842685400, 48.5336151327653000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '91200', 'Dourdan', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:76" version="714445" created="2015-06-15T10:06:26.0Z" changed="2015-06-26T09:06:37.0Z" id="FR:91200:ZDE:50097554:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:91200:ZDE:418738:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800803571</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800803571</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>800803571</Value>
        </KeyValue>
       </keyList>
       <Name>GARE DE DOURDAN</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">626890.793 6826389.451</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50097554">
        <Town>Dourdan</Town>
        <PostalRegion>91200</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50097554">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:47:17.668565', '2017-03-23 13:26:41.286542', 'LOCAL_OBJECT');
-- 141017 not found
INSERT INTO stop_areas VALUES (93528, 141017, 'FR:78517:ZDE:50016296:STIF', 1, 'chouette', 'Gare de Rambouillet [Prairie]', NULL, 'zdep', NULL, NULL, NULL, 1.8322018603547400, 48.6432261599316000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791575" created="2015-05-19T04:05:02.0Z" changed="2016-08-23T10:08:47.0Z" id="FR:78517:ZDE:50016296:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDE:424216:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013029</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013008</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013030</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013102</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013001</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013010</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013024</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013100</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013003</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013019</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013004</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013101</Value>
        </KeyValue>
       </keyList>
       <Name>Gare de Rambouillet [Prairie]</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613966.557 6838752.602</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016296">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016296">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:47:52.702452', '2017-03-23 13:26:57.563139', 'LOCAL_OBJECT');
      -- 149165
INSERT INTO stop_areas VALUES (114274, 149165, 'FR:78003:ZDE:50015543:STIF', 1, 'chouette', 'Pierre Trouvé', NULL, 'zdep', NULL, NULL, NULL, 1.8308304736604600, 48.5168881682201000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78003', 'Ablis', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792094" created="2015-05-19T05:05:23.0Z" changed="2016-08-23T10:08:44.0Z" id="FR:78003:ZDE:50015543:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78003:ZDE:31922:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013025</Value>
        </KeyValue>
       </keyList>
       <Name>Pierre Trouvé</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613657.583 6824710.807</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015543">
        <Town>Ablis</Town>
        <PostalRegion>78003</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015543">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:24.458478', '2017-03-23 13:36:57.886307', 'LOCAL_OBJECT');
      -- 149163
INSERT INTO stop_areas VALUES (114270, 149163, 'FR:78003:ZDE:50015539:STIF', 1, 'chouette', 'La Paix', NULL, 'zdep', NULL, NULL, NULL, 1.8329734756083700, 48.5187916705726000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78003', 'Ablis', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792096" created="2015-05-19T05:05:37.0Z" changed="2016-08-23T10:08:48.0Z" id="FR:78003:ZDE:50015539:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78003:ZDE:31945:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013018</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
       </keyList>
       <Name>La Paix</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613818.958 6824920.042</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015539">
        <Town>Ablis</Town>
        <PostalRegion>78003</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015539">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:24.29851', '2017-03-23 13:36:57.786397', 'LOCAL_OBJECT');
      -- 149159
INSERT INTO stop_areas VALUES (114263, 149159, 'FR:78003:ZDE:50015524:STIF', 1, 'chouette', 'Champarts', NULL, 'zdep', NULL, NULL, NULL, 1.8383011150488100, 48.5188442153089000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78003', 'Ablis', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792068" created="2015-05-19T05:05:07.0Z" changed="2016-08-23T10:08:55.0Z" id="FR:78003:ZDE:50015524:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78003:ZDE:31875:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013018</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013023</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013025</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
       </keyList>
       <Name>Champarts</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614212.444 6824920.081</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015524">
        <Town>Ablis</Town>
        <PostalRegion>78003</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015524">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:23.988055', '2017-03-23 13:36:57.58801', 'LOCAL_OBJECT');
      -- 134325
INSERT INTO stop_areas VALUES (77221, 134325, 'FR:78472:ZDE:50016121:STIF', 1, 'chouette', 'Eglise', NULL, 'zdep', NULL, NULL, NULL, 1.8345281658981300, 48.4781883387925000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78472', 'Orsonville', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791816" created="2015-05-19T05:05:38.0Z" changed="2016-08-23T10:08:31.0Z" id="FR:78472:ZDE:50016121:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78472:ZDE:31628:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013018</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013026</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
       </keyList>
       <Name>Eglise</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613867.142 6820405.314</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016121">
        <Town>Orsonville</Town>
        <PostalRegion>78472</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016121">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:50.449069', '2017-03-23 13:19:32.533045', 'LOCAL_OBJECT');
      -- 134325
INSERT INTO stop_areas VALUES (77222, 134325, 'FR:78472:ZDE:50016122:STIF', 1, 'chouette', 'Eglise', NULL, 'zdep', NULL, NULL, NULL, 1.8348001130032000, 48.4782057265572000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78472', 'Orsonville', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="791815" created="2015-05-19T05:05:37.0Z" changed="2016-08-23T10:08:30.0Z" id="FR:78472:ZDE:50016122:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78472:ZDE:31629:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013026</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013018</Value>
        </KeyValue>
       </keyList>
       <Name>Eglise</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613887.267 6820406.95</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50016122">
        <Town>Orsonville</Town>
        <PostalRegion>78472</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50016122">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:35:50.494165', '2017-03-23 13:19:32.517134', 'LOCAL_OBJECT');
      -- 149159
INSERT INTO stop_areas VALUES (114262, 149159, 'FR:78003:ZDE:50015523:STIF', 1, 'chouette', 'Champarts', NULL, 'zdep', NULL, NULL, NULL, 1.8381785933933500, 48.5187831712476000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78003', 'Ablis', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792067" created="2015-05-19T05:05:06.0Z" changed="2016-08-23T10:08:54.0Z" id="FR:78003:ZDE:50015523:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78003:ZDE:31871:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013023</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013018</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013025</Value>
        </KeyValue>
       </keyList>
       <Name>Champarts</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">614203.297 6824913.429</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015523">
        <Town>Ablis</Town>
        <PostalRegion>78003</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015523">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:23.929218', '2017-03-23 13:36:57.562606', 'LOCAL_OBJECT');
      -- 149163
INSERT INTO stop_areas VALUES (114271, 149163, 'FR:78003:ZDE:50015538:STIF', 1, 'chouette', 'La Paix', NULL, 'zdep', NULL, NULL, NULL, 1.8330957536317000, 48.5187446935073000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78003', 'Ablis', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792095" created="2015-05-19T05:05:35.0Z" changed="2016-08-23T10:08:46.0Z" id="FR:78003:ZDE:50015538:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78003:ZDE:31208:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013018</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013023</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013025</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
       </keyList>
       <Name>La Paix</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613827.91 6824914.687</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015538">
        <Town>Ablis</Town>
        <PostalRegion>78003</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015538">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:24.33431', '2017-03-23 13:36:57.768545', 'LOCAL_OBJECT');
      -- 149165
INSERT INTO stop_areas VALUES (114275, 149165, 'FR:78003:ZDE:50015542:STIF', 1, 'chouette', 'Pierre Trouvé', NULL, 'zdep', NULL, NULL, NULL, 1.8307784146431700, 48.5168281693426000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78003', 'Ablis', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792093" created="2015-05-19T05:05:22.0Z" changed="2016-08-23T10:08:42.0Z" id="FR:78003:ZDE:50015542:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78003:ZDE:31931:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013025</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013005</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
       </keyList>
       <Name>Pierre Trouvé</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">613653.64 6824704.195</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015542">
        <Town>Ablis</Town>
        <PostalRegion>78003</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015542">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 12:08:24.49159', '2017-03-23 13:36:57.862738', 'LOCAL_OBJECT');
      -- 140550 not found
INSERT INTO stop_areas VALUES (92299, NULL, 'FR:28015:ZDE:50015578:STIF', 1, 'chouette', 'Eglise', NULL, 'zdep', NULL, NULL, NULL, 1.7744413676204800, 48.4622308100999000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '28015', 'Auneau', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792073" created="2015-05-19T05:05:41.0Z" changed="2016-08-23T10:08:05.0Z" id="FR:28015:ZDE:50015578:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:28015:ZDE:31822:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
       </keyList>
       <Name>Eglise</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">609399.314 6818698.938</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015578">
        <Town>Auneau</Town>
        <PostalRegion>28015</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015578">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:46:54.469232', '2017-03-23 13:26:29.474346', 'LOCAL_OBJECT');
      -- 140551 not found
INSERT INTO stop_areas VALUES (92300, NULL, 'FR:28015:ZDE:50015581:STIF', 1, 'chouette', 'Marceau', NULL, 'zdep', NULL, NULL, NULL, 1.7733064592784600, 48.4624969242007000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '28015', 'Auneau', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792070" created="2015-05-19T05:05:31.0Z" changed="2016-08-23T10:08:00.0Z" id="FR:28015:ZDE:50015581:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:28015:ZDE:31821:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
       </keyList>
       <Name>Marceau</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">609315.881 6818729.818</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015581">
        <Town>Auneau</Town>
        <PostalRegion>28015</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015581">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:46:54.519712', '2017-03-23 13:26:29.496194', 'LOCAL_OBJECT');
      -- 140548 not found
INSERT INTO stop_areas VALUES (92295, NULL, 'FR:28015:ZDE:50015586:STIF', 1, 'chouette', 'Gare d''Auneau', NULL, 'zdep', NULL, NULL, NULL, 1.7797136489079900, 48.4471456370079000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '28015', 'Auneau', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="805105" created="2015-05-19T05:05:38.0Z" changed="2016-10-05T09:10:07.0Z" id="FR:28015:ZDE:50015586:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:28015:ZDE:31908:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
       </keyList>
       <Name>Gare d''Auneau</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">609763.129 6817016.27</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015586">
        <Town>Auneau</Town>
        <PostalRegion>28015</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015586">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:46:54.266589', '2017-03-23 13:26:29.410848', 'LOCAL_OBJECT');
      -- 140548 not found
INSERT INTO stop_areas VALUES (92294, NULL, 'FR:28015:ZDE:50015587:STIF', 1, 'chouette', 'Gare d''Auneau', NULL, 'zdep', NULL, NULL, NULL, 1.7797234743253300, 48.4472967047516000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '28015', 'Auneau', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:25" version="792010" created="2015-05-19T05:05:37.0Z" changed="2016-08-23T10:08:17.0Z" id="FR:28015:ZDE:50015587:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:28015:ZDE:31848:STIF</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>013013011</Value>
        </KeyValue>
       </keyList>
       <Name>Gare d''Auneau</Name>
       <Centroid>
        <Location>
         <gml:pos srsName="EPSG:2154">609764.115 6817033.049</gml:pos>
        </Location>
       </Centroid>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015587">
        <Town>Auneau</Town>
        <PostalRegion>28015</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015587">
        <MobilityImpairedAccess>unknown</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-03-23 11:46:54.201882', '2017-03-23 13:26:29.392707', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (137570, NULL, 'FR:78517:ZDL:50015361:STIF', 1, 'chouette', 'Collège Racinay', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="681012" created="2015-05-19T04:05:37.0Z" changed="2015-05-19T04:05:37.0Z" id="FR:78517:ZDL:50015361:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDL:42722:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Collège Racinay</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015361">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015361">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78517:ZDE:50016206:STIF" version="791607"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:31:03.337688', '2017-03-23 12:31:03.337688', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (134338, NULL, 'FR:78499:ZDL:50015332:STIF', 1, 'chouette', 'Mairie', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78499', 'Ponthévrard', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682016" created="2015-05-19T05:05:08.0Z" changed="2015-05-19T05:05:08.0Z" id="FR:78499:ZDL:50015332:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78499:ZDL:55018:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015332">
        <Town>Ponthévrard</Town>
        <PostalRegion>78499</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015332">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78499:ZDE:50016153:STIF" version="682012"/>
        <QuayRef ref="FR:78499:ZDE:50016152:STIF" version="791761"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:46.591547', '2017-03-23 12:26:46.591547', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (134339, NULL, 'FR:78499:ZDL:50015333:STIF', 1, 'chouette', 'Vallée Brun', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78499', 'Ponthévrard', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682008" created="2015-05-19T05:05:04.0Z" changed="2015-05-19T05:05:04.0Z" id="FR:78499:ZDL:50015333:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78499:ZDL:55019:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Vallée Brun</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015333">
        <Town>Ponthévrard</Town>
        <PostalRegion>78499</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015333">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78499:ZDE:50016155:STIF" version="791760"/>
        <QuayRef ref="FR:78499:ZDE:50016154:STIF" version="682006"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:46.692587', '2017-03-23 12:26:46.692587', 'LOCAL_OBJECT');

INSERT INTO stop_areas VALUES (143139, NULL, 'FR:78517:ZDL:50075979:STIF', 1, 'chouette', 'Giroderie', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:24" version="677654" created="2015-05-19T02:05:20.0Z" changed="2015-05-19T02:05:20.0Z" id="FR:78517:ZDL:50075979:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDL:43295:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Giroderie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50075979">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50075979">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78517:ZDE:50076505:STIF" version="677650"/>
        <QuayRef ref="FR:78517:ZDE:50076506:STIF" version="677652"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:38:01.011078', '2017-03-23 12:38:01.011078', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (134232, NULL, 'FR:78164:ZDL:50015089:STIF', 1, 'chouette', 'Croix', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78164', 'Clairefontaine-en-Yvelines', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="683686" created="2015-05-19T05:05:07.0Z" changed="2015-05-19T05:05:07.0Z" id="FR:78164:ZDL:50015089:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78164:ZDL:54896:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Croix</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015089">
        <Town>Clairefontaine-en-Yvelines</Town>
        <PostalRegion>78164</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015089">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78164:ZDE:50015684:STIF" version="683682"/>
        <QuayRef ref="FR:78164:ZDE:50015685:STIF" version="683684"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:35.29419', '2017-03-23 12:26:35.29419', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (149199, NULL, 'FR:78164:ZDL:50015088:STIF', 1, 'chouette', 'Centre', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78164', 'Clairefontaine-en-Yvelines', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="683678" created="2015-05-19T05:05:02.0Z" changed="2015-05-19T05:05:02.0Z" id="FR:78164:ZDL:50015088:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78164:ZDL:54895:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Centre</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015088">
        <Town>Clairefontaine-en-Yvelines</Town>
        <PostalRegion>78164</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015088">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78164:ZDE:50015682:STIF" version="683674"/>
        <QuayRef ref="FR:78164:ZDE:50015683:STIF" version="792032"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:10:41.938029', '2017-03-23 13:10:41.938029', 'LOCAL_OBJECT');
-- -----------------------------------------------------------------------
INSERT INTO stop_areas VALUES (134233, NULL, 'FR:78164:ZDL:50015090:STIF', 1, 'chouette', 'Montjoie', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78164', 'Clairefontaine-en-Yvelines', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="683662" created="2015-05-19T05:05:50.0Z" changed="2015-05-19T05:05:50.0Z" id="FR:78164:ZDL:50015090:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78164:ZDL:54897:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Montjoie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015090">
        <Town>Clairefontaine-en-Yvelines</Town>
        <PostalRegion>78164</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015090">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78164:ZDE:50015686:STIF" version="792030"/>
        <QuayRef ref="FR:78164:ZDE:50015687:STIF" version="683660"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:35.354613', '2017-03-23 12:26:35.354613', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (134287, NULL, 'FR:78486:ZDL:50015215:STIF', 1, 'chouette', 'Champ de Foire', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78486', 'Le Perray-en-Yvelines', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682884" created="2015-05-19T05:05:08.0Z" changed="2015-05-19T05:05:08.0Z" id="FR:78486:ZDL:50015215:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78486:ZDL:54955:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Champ de Foire</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015215">
        <Town>Le Perray-en-Yvelines</Town>
        <PostalRegion>78486</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015215">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78486:ZDE:50015931:STIF" version="791924"/>
        <QuayRef ref="FR:78486:ZDE:50015930:STIF" version="791925"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:39.194217', '2017-03-23 12:26:39.194217', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (134289, NULL, 'FR:78486:ZDL:50015218:STIF', 1, 'chouette', 'Fer Ouvré', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78486', 'Le Perray-en-Yvelines', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682669" created="2015-05-19T05:05:12.0Z" changed="2015-05-19T05:05:12.0Z" id="FR:78486:ZDL:50015218:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78486:ZDL:54957:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Fer Ouvré</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015218">
        <Town>Le Perray-en-Yvelines</Town>
        <PostalRegion>78486</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015218">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78486:ZDE:50015936:STIF" version="791892"/>
        <QuayRef ref="FR:78486:ZDE:50015937:STIF" version="791893"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:39.361043', '2017-03-23 12:26:39.361043', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (134315, NULL, 'FR:78464:ZDL:50015302:STIF', 1, 'chouette', 'Centre', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78464', 'Orcemont', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682243" created="2015-05-19T05:05:44.0Z" changed="2015-05-19T05:05:44.0Z" id="FR:78464:ZDL:50015302:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78464:ZDL:54993:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Centre</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015302">
        <Town>Orcemont</Town>
        <PostalRegion>78464</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015302">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78464:ZDE:50016097:STIF" version="682239"/>
        <QuayRef ref="FR:78464:ZDE:50016096:STIF" version="791797"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:44.603195', '2017-03-23 12:26:44.603195', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (134317, NULL, 'FR:78464:ZDL:50015304:STIF', 1, 'chouette', 'Guillemets', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78464', 'Orcemont', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682338" created="2015-05-19T05:05:27.0Z" changed="2015-05-19T05:05:27.0Z" id="FR:78464:ZDL:50015304:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78464:ZDL:54995:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Guillemets</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015304">
        <Town>Orcemont</Town>
        <PostalRegion>78464</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015304">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78464:ZDE:50016099:STIF" version="682334"/>
        <QuayRef ref="FR:78464:ZDE:50016100:STIF" version="791811"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:44.752089', '2017-03-23 12:26:44.752089', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (134322, NULL, 'FR:78470:ZDL:50015310:STIF', 1, 'chouette', 'Mairie', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78470', 'Orphin', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682294" created="2015-05-19T05:05:07.0Z" changed="2015-05-19T05:05:07.0Z" id="FR:78470:ZDL:50015310:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78470:ZDL:55001:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Mairie</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015310">
        <Town>Orphin</Town>
        <PostalRegion>78470</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015310">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78470:ZDE:50016111:STIF" version="791800"/>
        <QuayRef ref="FR:78470:ZDE:50016114:STIF" version="791799"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:45.155592', '2017-03-23 12:26:45.155592', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (134325, NULL, 'FR:78472:ZDL:50015316:STIF', 1, 'chouette', 'Eglise', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78472', 'Orsonville', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="682362" created="2015-05-19T05:05:38.0Z" changed="2015-05-19T05:05:38.0Z" id="FR:78472:ZDL:50015316:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78472:ZDL:55005:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Eglise</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015316">
        <Town>Orsonville</Town>
        <PostalRegion>78472</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015316">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78472:ZDE:50016122:STIF" version="791815"/>
        <QuayRef ref="FR:78472:ZDE:50016121:STIF" version="791816"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:26:45.492294', '2017-03-23 12:26:45.492294', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (134572, NULL, 'FR:78466:ZDL:50001447:STIF', 1, 'chouette', '40 Sous', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78466', 'Orgeval', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:23" version="416729" created="2015-01-30T12:01:30.0Z" changed="2015-01-30T12:01:30.0Z" id="FR:78466:ZDL:50001447:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78466:ZDL:46774:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>40 Sous</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50001447">
        <Town>Orgeval</Town>
        <PostalRegion>78466</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50001447">
        <MobilityImpairedAccess>false</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78466:ZDE:50002019:STIF" version="824887"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:27:04.392023', '2017-03-23 12:27:04.392023', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (141017, NULL, 'FR:78517:ZDL:50015417:STIF', 1, 'chouette', 'Gare de Rambouillet [Prairie]', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="791580" created="2015-05-19T04:05:03.0Z" changed="2016-08-23T10:08:49.0Z" id="FR:78517:ZDL:50015417:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDL:43185:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Gare de Rambouillet [Prairie]</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015417">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015417">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78517:ZDE:50016296:STIF" version="791575"/>
        <QuayRef ref="FR:78517:ZDE:50015659:STIF" version="805097"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:35:11.738978', '2017-03-23 12:35:11.738978', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (142959, NULL, 'FR:78220:ZDL:50075942:STIF', 1, 'chouette', 'Gare des Essarts le Roi', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78220', 'Les Essarts-le-Roi', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:24" version="677925" created="2015-05-19T02:05:27.0Z" changed="2015-05-19T02:05:27.0Z" id="FR:78220:ZDL:50075942:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78220:ZDL:43229:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Gare des Essarts le Roi</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50075942">
        <Town>Les Essarts-le-Roi</Town>
        <PostalRegion>78220</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50075942">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78220:ZDE:50076443:STIF" version="677921"/>
        <QuayRef ref="FR:78220:ZDE:50076444:STIF" version="677923"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:37:46.540087', '2017-03-23 12:37:46.540087', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (142981, NULL, 'FR:78486:ZDL:50015220:STIF', 1, 'chouette', 'Gare Le Perray', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78486', 'Le Perray-en-Yvelines', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="791904" created="2015-05-19T05:05:37.0Z" changed="2016-08-23T10:08:20.0Z" id="FR:78486:ZDL:50015220:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78486:ZDL:43235:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Gare Le Perray</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015220">
        <Town>Le Perray-en-Yvelines</Town>
        <PostalRegion>78486</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015220">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78486:ZDE:50015940:STIF" version="791903"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:37:48.545766', '2017-03-23 12:37:48.545766', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (149159, NULL, 'FR:78003:ZDL:50015007:STIF', 1, 'chouette', 'Champarts', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78003', 'Ablis', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="683862" created="2015-05-19T05:05:09.0Z" changed="2015-05-19T05:05:09.0Z" id="FR:78003:ZDL:50015007:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78003:ZDL:54848:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Champarts</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015007">
        <Town>Ablis</Town>
        <PostalRegion>78003</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015007">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78003:ZDE:50015523:STIF" version="792067"/>
        <QuayRef ref="FR:78003:ZDE:50015524:STIF" version="792068"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:10:39.71555', '2017-03-23 13:10:39.71555', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (149163, NULL, 'FR:78003:ZDL:50015015:STIF', 1, 'chouette', 'La Paix', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78003', 'Ablis', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="684048" created="2015-05-19T05:05:38.0Z" changed="2015-05-19T05:05:38.0Z" id="FR:78003:ZDL:50015015:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78003:ZDL:54853:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>La Paix</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015015">
        <Town>Ablis</Town>
        <PostalRegion>78003</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015015">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78003:ZDE:50015538:STIF" version="792095"/>
        <QuayRef ref="FR:78003:ZDE:50015539:STIF" version="792096"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:10:39.951302', '2017-03-23 13:10:39.951302', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (149165, NULL, 'FR:78003:ZDL:50015017:STIF', 1, 'chouette', 'Pierre Trouvé', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78003', 'Ablis', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="684032" created="2015-05-19T05:05:25.0Z" changed="2015-05-19T05:05:25.0Z" id="FR:78003:ZDL:50015017:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78003:ZDL:54855:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Pierre Trouvé</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015017">
        <Town>Ablis</Town>
        <PostalRegion>78003</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015017">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78003:ZDE:50015542:STIF" version="792093"/>
        <QuayRef ref="FR:78003:ZDE:50015543:STIF" version="792094"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:10:40.077448', '2017-03-23 13:10:40.077448', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (149176, NULL, 'FR:78030:ZDL:50015034:STIF', 1, 'chouette', 'Route du Perray', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78030', 'Auffargis', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="683899" created="2015-05-19T05:05:38.0Z" changed="2015-05-19T05:05:38.0Z" id="FR:78030:ZDL:50015034:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78030:ZDL:54867:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Route du Perray</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015034">
        <Town>Auffargis</Town>
        <PostalRegion>78030</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015034">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78030:ZDE:50015576:STIF" version="792071"/>
        <QuayRef ref="FR:78030:ZDE:50015577:STIF" version="792072"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:10:40.666615', '2017-03-23 13:10:40.666615', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (149194, NULL, 'FR:78125:ZDL:50015083:STIF', 1, 'chouette', 'Château', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78125', 'La Celle-les-Bordes', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="683726" created="2015-05-19T05:05:34.0Z" changed="2015-05-19T05:05:34.0Z" id="FR:78125:ZDL:50015083:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78125:ZDL:54890:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Château</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015083">
        <Town>La Celle-les-Bordes</Town>
        <PostalRegion>78125</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015083">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78125:ZDE:50015673:STIF" version="792044"/>
        <QuayRef ref="FR:78125:ZDE:50015672:STIF" version="792045"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:10:41.658609', '2017-03-23 13:10:41.658609', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (149196, NULL, 'FR:78125:ZDL:50015085:STIF', 1, 'chouette', 'Bois des Gaulles', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78125', 'La Celle-les-Bordes', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="683774" created="2015-05-19T05:05:07.0Z" changed="2015-05-19T05:05:07.0Z" id="FR:78125:ZDL:50015085:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78125:ZDL:54892:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Bois des Gaulles</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015085">
        <Town>La Celle-les-Bordes</Town>
        <PostalRegion>78125</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015085">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78125:ZDE:50015677:STIF" version="792053"/>
        <QuayRef ref="FR:78125:ZDE:50015676:STIF" version="683772"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:10:41.771118', '2017-03-23 13:10:41.771118', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (149197, NULL, 'FR:78125:ZDL:50015086:STIF', 1, 'chouette', 'Villeneuve', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78125', 'La Celle-les-Bordes', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:25" version="683766" created="2015-05-19T05:05:02.0Z" changed="2015-05-19T05:05:02.0Z" id="FR:78125:ZDL:50015086:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78125:ZDL:54893:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Villeneuve</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50015086">
        <Town>La Celle-les-Bordes</Town>
        <PostalRegion>78125</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50015086">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78125:ZDE:50015679:STIF" version="792052"/>
        <QuayRef ref="FR:78125:ZDE:50015678:STIF" version="683764"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 13:10:41.826806', '2017-03-23 13:10:41.826806', 'LOCAL_OBJECT');
INSERT INTO stop_areas VALUES (137572, NULL, 'FR:78517:ZDL:50075977:STIF', 1, 'chouette', 'Collège Racinay', NULL, 'zdlp', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78517', 'Rambouillet', NULL, NULL, 1, NULL, '<StopPlace dataSourceRef="STIF-REFLEX:Operator:24" version="677641" created="2015-05-19T02:05:14.0Z" changed="2015-05-19T02:05:14.0Z" id="FR:78517:ZDL:50075977:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>LOCAL_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>REFERENCE_OBJECT_ID</Key>
         <Value>FR:78517:ZDL:42722:STIF</Value>
        </KeyValue>
       </keyList>
       <Name>Collège Racinay</Name>
       <placeTypes>
        <TypeOfPlaceRef ref="ZDL"/>
       </placeTypes>
       <PostalAddress version="any" id="STIF-REFLEX:PostalAddress:50075977">
        <Town>Rambouillet</Town>
        <PostalRegion>78517</PostalRegion>
       </PostalAddress>
       <AccessibilityAssessment version="any" id="STIF-REFLEX:AccessibilityAssessment:50075977">
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78517:ZDE:50076501:STIF" version="732666"/>
       </quays>
      </StopPlace>', NULL, '2017-03-23 12:31:03.387835', '2017-03-23 12:31:03.387835', 'LOCAL_OBJECT');


      
SELECT pg_catalog.setval('stop_areas_id_seq', 156879, true);


INSERT INTO users VALUES (1, 'admin@stif.info', '', NULL, NULL, NULL, 1, '2017-02-10 09:44:29.503632', '2017-02-10 09:44:29.503632', '173.18.0.1', '173.18.0.1', '2017-02-10 09:37:12.514985', '2017-02-10 09:44:29.506024', 2, 'stif admin', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'admin', NULL, '{routes.create,routes.edit,routes.destroy,journey_patterns.create,journey_patterns.edit,journey_patterns.destroy,vehicle_journeys.create,vehicle_journeys.edit,vehicle_journeys.destroy,time_tables.create,time_tables.edit,time_tables.destroy,footnotes.edit,footnotes.create,footnotes.destroy,routing_constraint_zones.create,routing_constraint_zones.edit,routing_constraint_zones.destroy,access_points.create,access_points.edit,access_points.destroy,access_links.create,access_links.edit,access_links.destroy,connection_links.create,connection_links.edit,connection_links.destroy,route_sections.create,route_sections.edit,route_sections.destroy}');
INSERT INTO users VALUES (2, 'metienne@cityway.fr', '', NULL, NULL, NULL, 1, '2017-02-10 10:05:53.285943', '2017-02-10 10:05:53.285943', '173.18.0.1', '173.18.0.1', '2017-02-10 10:05:53.2692', '2017-02-10 10:05:53.287299', 4, 'Etienne Michel', NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'metienne', NULL, '{routes.create,routes.edit,routes.destroy,journey_patterns.create,journey_patterns.edit,journey_patterns.destroy,vehicle_journeys.create,vehicle_journeys.edit,vehicle_journeys.destroy,time_tables.create,time_tables.edit,time_tables.destroy,footnotes.edit,footnotes.create,footnotes.destroy,routing_constraint_zones.create,routing_constraint_zones.edit,routing_constraint_zones.destroy,access_points.create,access_points.edit,access_points.destroy,access_links.create,access_links.edit,access_links.destroy,connection_links.create,connection_links.edit,connection_links.destroy,route_sections.create,route_sections.edit,route_sections.destroy}');

SELECT pg_catalog.setval('users_id_seq', 2, true);

INSERT INTO workbenches VALUES (1, 'Gestion de l offre', 2, '2017-02-10 09:37:12.360669', '2017-02-10 09:37:12.360669', 1, 1);

SELECT pg_catalog.setval('workbenches_id_seq', 1, true);


-- #################################################################
ALTER TABLE ONLY access_links
    ADD CONSTRAINT access_links_pkey PRIMARY KEY (id);
ALTER TABLE ONLY access_points
    ADD CONSTRAINT access_points_pkey PRIMARY KEY (id);
ALTER TABLE ONLY companies
    ADD CONSTRAINT companies_pkey PRIMARY KEY (id);

ALTER TABLE ONLY compliance_check_blocks
    ADD CONSTRAINT compliance_check_blocks_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_check_resources
    ADD CONSTRAINT compliance_check_resources_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_check_results
    ADD CONSTRAINT compliance_check_results_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_check_sets
    ADD CONSTRAINT compliance_check_sets_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_checks
    ADD CONSTRAINT compliance_checks_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_control_blocks
    ADD CONSTRAINT compliance_control_blocks_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_control_sets
    ADD CONSTRAINT compliance_control_sets_pkey PRIMARY KEY (id);
ALTER TABLE ONLY compliance_controls
    ADD CONSTRAINT compliance_controls_pkey PRIMARY KEY (id);   

    
ALTER TABLE ONLY connection_links
    ADD CONSTRAINT connection_links_pkey PRIMARY KEY (id);
ALTER TABLE ONLY group_of_lines
    ADD CONSTRAINT group_of_lines_pkey PRIMARY KEY (id);
ALTER TABLE ONLY import_messages
    ADD CONSTRAINT import_messages_pkey PRIMARY KEY (id);
ALTER TABLE ONLY import_resources
    ADD CONSTRAINT import_resources_pkey PRIMARY KEY (id);
ALTER TABLE ONLY imports
    ADD CONSTRAINT imports_pkey PRIMARY KEY (id);
ALTER TABLE ONLY line_referentials
    ADD CONSTRAINT line_referentials_pkey PRIMARY KEY (id);
ALTER TABLE ONLY lines
    ADD CONSTRAINT lines_pkey PRIMARY KEY (id);
ALTER TABLE ONLY networks
    ADD CONSTRAINT networks_pkey PRIMARY KEY (id);
ALTER TABLE ONLY organisations
    ADD CONSTRAINT organisations_pkey PRIMARY KEY (id);
ALTER TABLE ONLY referential_metadata
    ADD CONSTRAINT referential_metadata_pkey PRIMARY KEY (id);
ALTER TABLE ONLY referentials
    ADD CONSTRAINT referentials_pkey PRIMARY KEY (id);
ALTER TABLE ONLY stop_area_referentials
    ADD CONSTRAINT stop_area_referentials_pkey PRIMARY KEY (id);
ALTER TABLE ONLY stop_areas
    ADD CONSTRAINT stop_areas_pkey PRIMARY KEY (id);
ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
ALTER TABLE ONLY workbenches
    ADD CONSTRAINT workbenches_pkey PRIMARY KEY (id);

CREATE UNIQUE INDEX access_links_objectid_key ON access_links USING btree (objectid);
CREATE UNIQUE INDEX access_points_objectid_key ON access_points USING btree (objectid);
CREATE UNIQUE INDEX companies_objectid_key ON companies USING btree (objectid);
CREATE INDEX companies_registration_number_key ON companies USING btree (registration_number);
CREATE UNIQUE INDEX connection_links_objectid_key ON connection_links USING btree (objectid);
CREATE UNIQUE INDEX group_of_lines_objectid_key ON group_of_lines USING btree (objectid);
CREATE INDEX index_companies_on_line_referential_id ON companies USING btree (line_referential_id);

CREATE INDEX index_compliance_check_blocks_on_compliance_check_set_id ON compliance_check_blocks USING btree (compliance_check_set_id);
CREATE INDEX index_compliance_check_results_on_compliance_check_id ON compliance_check_results USING btree (compliance_check_id);
CREATE INDEX index_compliance_check_results_on_compliance_check_resource_id ON compliance_check_results USING btree (compliance_check_resource_id);
CREATE INDEX index_compliance_check_sets_on_compliance_control_set_id ON compliance_check_sets USING btree (compliance_control_set_id);
CREATE INDEX index_compliance_check_sets_on_parent_type_and_parent_id ON compliance_check_sets USING btree (parent_type, parent_id);
CREATE INDEX index_compliance_check_sets_on_referential_id ON compliance_check_sets USING btree (referential_id);
CREATE INDEX index_compliance_check_sets_on_workbench_id ON compliance_check_sets USING btree (workbench_id);
CREATE INDEX index_compliance_checks_on_compliance_check_block_id ON compliance_checks USING btree (compliance_check_block_id);
CREATE INDEX index_compliance_checks_on_compliance_check_set_id ON compliance_checks USING btree (compliance_check_set_id);
CREATE INDEX index_compliance_control_blocks_on_compliance_control_set_id ON compliance_control_blocks USING btree (compliance_control_set_id);
CREATE INDEX index_compliance_control_sets_on_organisation_id ON compliance_control_sets USING btree (organisation_id);
CREATE INDEX index_compliance_controls_on_compliance_control_block_id ON compliance_controls USING btree (compliance_control_block_id);
CREATE INDEX index_compliance_controls_on_compliance_control_set_id ON compliance_controls USING btree (compliance_control_set_id);

CREATE INDEX index_group_of_lines_on_line_referential_id ON group_of_lines USING btree (line_referential_id);
CREATE INDEX index_import_messages_on_import_id ON import_messages USING btree (import_id);
CREATE INDEX index_import_messages_on_resource_id ON import_messages USING btree (resource_id);
CREATE INDEX index_import_resources_on_import_id ON import_resources USING btree (import_id);
CREATE INDEX index_imports_on_referential_id ON imports USING btree (referential_id);
CREATE INDEX index_imports_on_workbench_id ON imports USING btree (workbench_id);
CREATE INDEX index_lines_on_line_referential_id ON lines USING btree (line_referential_id);
CREATE INDEX index_networks_on_line_referential_id ON networks USING btree (line_referential_id);
CREATE UNIQUE INDEX index_organisations_on_code ON organisations USING btree (code);
CREATE INDEX index_referential_metadata_on_line_ids ON referential_metadata USING gin (line_ids);
CREATE INDEX index_referential_metadata_on_referential_id ON referential_metadata USING btree (referential_id);
CREATE INDEX index_referential_metadata_on_referential_source_id ON referential_metadata USING btree (referential_source_id);
CREATE INDEX index_referentials_on_created_from_id ON referentials USING btree (created_from_id);
CREATE INDEX index_stop_areas_on_name ON stop_areas USING btree (name);
CREATE INDEX index_stop_areas_on_parent_id ON stop_areas USING btree (parent_id);
CREATE INDEX index_stop_areas_on_stop_area_referential_id ON stop_areas USING btree (stop_area_referential_id);
CREATE UNIQUE INDEX index_users_on_email ON users USING btree (email);
CREATE UNIQUE INDEX index_users_on_invitation_token ON users USING btree (invitation_token);
CREATE UNIQUE INDEX index_users_on_reset_password_token ON users USING btree (reset_password_token);
CREATE UNIQUE INDEX index_users_on_username ON users USING btree (username);
CREATE INDEX index_workbenches_on_line_referential_id ON workbenches USING btree (line_referential_id);
CREATE INDEX index_workbenches_on_organisation_id ON workbenches USING btree (organisation_id);
CREATE INDEX index_workbenches_on_stop_area_referential_id ON workbenches USING btree (stop_area_referential_id);
CREATE UNIQUE INDEX lines_objectid_key ON lines USING btree (objectid);
CREATE INDEX lines_registration_number_key ON lines USING btree (registration_number);
CREATE UNIQUE INDEX networks_objectid_key ON networks USING btree (objectid);
CREATE INDEX networks_registration_number_key ON networks USING btree (registration_number);
CREATE UNIQUE INDEX stop_areas_objectid_key ON stop_areas USING btree (objectid);

ALTER TABLE ONLY access_links
    ADD CONSTRAINT aclk_acpt_fkey FOREIGN KEY (access_point_id) REFERENCES access_points(id) ON DELETE CASCADE;
ALTER TABLE ONLY stop_areas
    ADD CONSTRAINT area_parent_fkey FOREIGN KEY (parent_id) REFERENCES stop_areas(id) ON DELETE SET NULL;
ALTER TABLE ONLY group_of_lines_lines
    ADD CONSTRAINT groupofline_group_fkey FOREIGN KEY (group_of_line_id) REFERENCES group_of_lines(id) ON DELETE CASCADE;

ALTER TABLE ONLY compliance_control_blocks
    ADD CONSTRAINT fk_rails_0f26e226bd FOREIGN KEY (compliance_control_set_id) REFERENCES compliance_control_sets(id);
ALTER TABLE ONLY compliance_check_results
    ADD CONSTRAINT fk_rails_1361178dd5 FOREIGN KEY (compliance_check_id) REFERENCES compliance_checks(id);
ALTER TABLE ONLY compliance_checks
    ADD CONSTRAINT fk_rails_2cbc8a0142 FOREIGN KEY (compliance_check_set_id) REFERENCES compliance_check_sets(id);
ALTER TABLE ONLY compliance_check_sets
    ADD CONSTRAINT fk_rails_4145f3761b FOREIGN KEY (referential_id) REFERENCES referentials(id);
ALTER TABLE ONLY compliance_check_results
    ADD CONSTRAINT fk_rails_70bd95092e FOREIGN KEY (compliance_check_resource_id) REFERENCES compliance_check_resources(id);
ALTER TABLE ONLY compliance_check_blocks
    ADD CONSTRAINT fk_rails_7d7a89703f FOREIGN KEY (compliance_check_set_id) REFERENCES compliance_check_sets(id);
ALTER TABLE ONLY compliance_control_sets
    ADD CONSTRAINT fk_rails_aa1e909966 FOREIGN KEY (organisation_id) REFERENCES organisations(id);
ALTER TABLE ONLY compliance_controls
    ADD CONSTRAINT fk_rails_c613154a10 FOREIGN KEY (compliance_control_block_id) REFERENCES compliance_control_blocks(id);
ALTER TABLE ONLY compliance_check_sets
    ADD CONSTRAINT fk_rails_d227ba43d7 FOREIGN KEY (compliance_control_set_id) REFERENCES compliance_control_sets(id);
ALTER TABLE ONLY compliance_check_sets
    ADD CONSTRAINT fk_rails_d61509f1a9 FOREIGN KEY (workbench_id) REFERENCES workbenches(id);
ALTER TABLE ONLY compliance_checks
    ADD CONSTRAINT fk_rails_df077b5b35 FOREIGN KEY (compliance_check_block_id) REFERENCES compliance_check_blocks(id);
ALTER TABLE ONLY compliance_controls
    ADD CONSTRAINT fk_rails_f402e905ef FOREIGN KEY (compliance_control_set_id) REFERENCES compliance_control_sets(id);

    

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;





  
  
-- référentiel de test 
  
DROP SCHEMA IF EXISTS chouette_gui CASCADE;

CREATE SCHEMA chouette_gui ;

SET search_path = chouette_gui, pg_catalog;

--

--
-- TOC entry 191 (class 1259 OID 938920)
-- Name: footnotes; Type: TABLE; Schema: chouette_gui; Owner: chouette; Tablespace: 
--

CREATE TABLE footnotes (
    id bigint NOT NULL,
    line_id bigint,
    code character varying(255),
    label character varying(255),
    checksum character varying(255),
    checksum_source text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE chouette_gui.footnotes OWNER TO chouette;
CREATE SEQUENCE footnotes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.footnotes_id_seq OWNER TO chouette;
ALTER SEQUENCE footnotes_id_seq OWNED BY footnotes.id;


CREATE TABLE footnotes_vehicle_journeys (
    vehicle_journey_id bigint,
    footnote_id bigint
);
ALTER TABLE chouette_gui.footnotes_vehicle_journeys OWNER TO chouette;



CREATE TABLE journey_frequencies (
    id bigint NOT NULL,
    vehicle_journey_id bigint,
    scheduled_headway_interval time without time zone NOT NULL,
    first_departure_time time without time zone NOT NULL,
    last_departure_time time without time zone,
    exact_time boolean DEFAULT false,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    timeband_id bigint
);
ALTER TABLE chouette_gui.journey_frequencies OWNER TO chouette;
CREATE SEQUENCE journey_frequencies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.journey_frequencies_id_seq OWNER TO chouette;
ALTER SEQUENCE journey_frequencies_id_seq OWNED BY journey_frequencies.id;



CREATE TABLE journey_pattern_sections (
    id bigint NOT NULL,
    journey_pattern_id bigint NOT NULL,
    route_section_id bigint NOT NULL,
    rank integer NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE chouette_gui.journey_pattern_sections OWNER TO chouette;
CREATE SEQUENCE journey_pattern_sections_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.journey_pattern_sections_id_seq OWNER TO chouette;
ALTER SEQUENCE journey_pattern_sections_id_seq OWNED BY journey_pattern_sections.id;



CREATE TABLE journey_patterns (
    id bigint NOT NULL,
    route_id bigint,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    name character varying(255),
    comment character varying(255),
    registration_number character varying(255),
    published_name character varying(255),
    departure_stop_point_id bigint,
    arrival_stop_point_id bigint,
    section_status integer DEFAULT 0 NOT NULL,
    checksum character varying(255),
    checksum_source text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE chouette_gui.journey_patterns OWNER TO chouette;
CREATE SEQUENCE journey_patterns_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.journey_patterns_id_seq OWNER TO chouette;
ALTER SEQUENCE journey_patterns_id_seq OWNED BY journey_patterns.id;



CREATE TABLE journey_patterns_stop_points (
    journey_pattern_id bigint,
    stop_point_id bigint
);
ALTER TABLE chouette_gui.journey_patterns_stop_points OWNER TO chouette;






CREATE TABLE routes (
    id bigint NOT NULL,
    line_id bigint,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    name character varying(255),
    comment character varying(255),
    opposite_route_id bigint,
    published_name character varying(255),
    number character varying(255),
    direction character varying(255),
    wayback character varying(255),
    checksum character varying(255),
    checksum_source text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE chouette_gui.routes OWNER TO chouette;
CREATE SEQUENCE routes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.routes_id_seq OWNER TO chouette;
ALTER SEQUENCE routes_id_seq OWNED BY routes.id;



CREATE TABLE routing_constraint_zones (
    id bigint NOT NULL,
    name character varying(255),
    stop_point_ids bigint[],
    route_id bigint,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    checksum character varying(255),
    checksum_source text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE chouette_gui.routing_constraint_zones OWNER TO chouette;
CREATE SEQUENCE routing_constraint_zones_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.routing_constraint_zones_id_seq OWNER TO chouette;
ALTER SEQUENCE routing_constraint_zones_id_seq OWNED BY routing_constraint_zones.id;



CREATE TABLE stop_points (
    id bigint NOT NULL,
    route_id bigint,
    stop_area_id bigint,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    "position" integer,
    for_boarding character varying(255),
    for_alighting character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE chouette_gui.stop_points OWNER TO chouette;
CREATE SEQUENCE stop_points_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.stop_points_id_seq OWNER TO chouette;
ALTER SEQUENCE stop_points_id_seq OWNED BY stop_points.id;



CREATE TABLE time_table_dates (
    time_table_id bigint NOT NULL,
    date date,
    "position" integer NOT NULL,
    id bigint NOT NULL,
    in_out boolean,
    checksum character varying(255),
    checksum_source text
);
ALTER TABLE chouette_gui.time_table_dates OWNER TO chouette;
CREATE SEQUENCE time_table_dates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.time_table_dates_id_seq OWNER TO chouette;
ALTER SEQUENCE time_table_dates_id_seq OWNED BY time_table_dates.id;



CREATE TABLE time_table_periods (
    time_table_id bigint NOT NULL,
    period_start date,
    period_end date,
    "position" integer NOT NULL,
    id bigint NOT NULL,
    checksum character varying(255),
    checksum_source text
);
ALTER TABLE chouette_gui.time_table_periods OWNER TO chouette;
CREATE SEQUENCE time_table_periods_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.time_table_periods_id_seq OWNER TO chouette;
ALTER SEQUENCE time_table_periods_id_seq OWNED BY time_table_periods.id;



CREATE TABLE time_tables (
    id bigint NOT NULL,
    objectid character varying(255) NOT NULL,
    object_version bigint DEFAULT 1,
    creator_id character varying(255),
    version character varying(255),
    comment character varying(255),
    int_day_types integer DEFAULT 0,
    start_date date,
    end_date date,
    calendar_id integer,
    checksum character varying(255),
    checksum_source text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE chouette_gui.time_tables OWNER TO chouette;
CREATE SEQUENCE time_tables_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.time_tables_id_seq OWNER TO chouette;
ALTER SEQUENCE time_tables_id_seq OWNED BY time_tables.id;




CREATE TABLE time_tables_vehicle_journeys (
    time_table_id bigint,
    vehicle_journey_id bigint
);
ALTER TABLE chouette_gui.time_tables_vehicle_journeys OWNER TO chouette;



CREATE TABLE timebands (
    id bigint NOT NULL,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    name character varying(255),
    start_time time without time zone NOT NULL,
    end_time time without time zone NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE chouette_gui.timebands OWNER TO chouette;
CREATE SEQUENCE timebands_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.timebands_id_seq OWNER TO chouette;
ALTER SEQUENCE timebands_id_seq OWNED BY timebands.id;



CREATE TABLE vehicle_journey_at_stops (
    id bigint NOT NULL,
    vehicle_journey_id bigint,
    stop_point_id bigint,
    connecting_service_id character varying(255),
    boarding_alighting_possibility character varying(255),
    arrival_time time without time zone,
    departure_time time without time zone,
    for_boarding character varying(255),
    for_alighting character varying(255),
    departure_day_offset int not null default 0,
    arrival_day_offset int not null default 0,
    checksum character varying(255),
    checksum_source text
);
ALTER TABLE chouette_gui.vehicle_journey_at_stops OWNER TO chouette;
CREATE SEQUENCE vehicle_journey_at_stops_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.vehicle_journey_at_stops_id_seq OWNER TO chouette;
ALTER SEQUENCE vehicle_journey_at_stops_id_seq OWNED BY vehicle_journey_at_stops.id;



CREATE TABLE vehicle_journeys (
    id bigint NOT NULL,
    route_id bigint,
    journey_pattern_id bigint,
    company_id bigint,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255),
    comment character varying(255),
    status_value character varying(255),
    transport_mode character varying(255),
    published_journey_name character varying(255),
    published_journey_identifier character varying(255),
    facility character varying(255),
    vehicle_type_identifier character varying(255),
    number bigint,
    mobility_restricted_suitability boolean,
    flexible_service boolean,
    journey_category integer DEFAULT 0 NOT NULL,
    checksum character varying(255),
    checksum_source text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE chouette_gui.vehicle_journeys OWNER TO chouette;
CREATE SEQUENCE vehicle_journeys_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE chouette_gui.vehicle_journeys_id_seq OWNER TO chouette;
ALTER SEQUENCE vehicle_journeys_id_seq OWNED BY vehicle_journeys.id;



ALTER TABLE ONLY footnotes ALTER COLUMN id SET DEFAULT nextval('footnotes_id_seq'::regclass);
ALTER TABLE ONLY journey_frequencies ALTER COLUMN id SET DEFAULT nextval('journey_frequencies_id_seq'::regclass);
ALTER TABLE ONLY journey_pattern_sections ALTER COLUMN id SET DEFAULT nextval('journey_pattern_sections_id_seq'::regclass);
ALTER TABLE ONLY journey_patterns ALTER COLUMN id SET DEFAULT nextval('journey_patterns_id_seq'::regclass);
ALTER TABLE ONLY routes ALTER COLUMN id SET DEFAULT nextval('routes_id_seq'::regclass);
ALTER TABLE ONLY routing_constraint_zones ALTER COLUMN id SET DEFAULT nextval('routing_constraint_zones_id_seq'::regclass);
ALTER TABLE ONLY stop_points ALTER COLUMN id SET DEFAULT nextval('stop_points_id_seq'::regclass);
ALTER TABLE ONLY time_table_dates ALTER COLUMN id SET DEFAULT nextval('time_table_dates_id_seq'::regclass);
ALTER TABLE ONLY time_table_periods ALTER COLUMN id SET DEFAULT nextval('time_table_periods_id_seq'::regclass);
ALTER TABLE ONLY time_tables ALTER COLUMN id SET DEFAULT nextval('time_tables_id_seq'::regclass);
ALTER TABLE ONLY timebands ALTER COLUMN id SET DEFAULT nextval('timebands_id_seq'::regclass);
ALTER TABLE ONLY vehicle_journey_at_stops ALTER COLUMN id SET DEFAULT nextval('vehicle_journey_at_stops_id_seq'::regclass);
ALTER TABLE ONLY vehicle_journeys ALTER COLUMN id SET DEFAULT nextval('vehicle_journeys_id_seq'::regclass);


------------------------------------------------------------------
SELECT pg_catalog.setval('footnotes_id_seq', 1, true);
SELECT pg_catalog.setval('journey_frequencies_id_seq', 1, true);
SELECT pg_catalog.setval('journey_pattern_sections_id_seq', 1, true);
SELECT pg_catalog.setval('journey_patterns_id_seq', 1, true);
SELECT pg_catalog.setval('routes_id_seq', 1, true);
SELECT pg_catalog.setval('routing_constraint_zones_id_seq', 1, true);
SELECT pg_catalog.setval('stop_points_id_seq', 1, true);
SELECT pg_catalog.setval('time_table_dates_id_seq', 1, true);
SELECT pg_catalog.setval('time_table_periods_id_seq', 1, true);
SELECT pg_catalog.setval('time_tables_id_seq', 1, true);
SELECT pg_catalog.setval('timebands_id_seq', 1, true);
SELECT pg_catalog.setval('vehicle_journey_at_stops_id_seq', 1, true);
SELECT pg_catalog.setval('vehicle_journeys_id_seq', 1, true);
------------------------------------------------------------------


ALTER TABLE ONLY footnotes
    ADD CONSTRAINT footnotes_pkey PRIMARY KEY (id);
ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT journey_frequencies_pkey PRIMARY KEY (id);
ALTER TABLE ONLY journey_pattern_sections
    ADD CONSTRAINT journey_pattern_sections_pkey PRIMARY KEY (id);
ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT journey_patterns_pkey PRIMARY KEY (id);
ALTER TABLE ONLY routes
    ADD CONSTRAINT routes_pkey PRIMARY KEY (id);
ALTER TABLE ONLY routing_constraint_zones
    ADD CONSTRAINT routing_constraint_zones_pkey PRIMARY KEY (id);
ALTER TABLE ONLY stop_points
    ADD CONSTRAINT stop_points_pkey PRIMARY KEY (id);
ALTER TABLE ONLY time_table_dates
    ADD CONSTRAINT time_table_dates_pkey PRIMARY KEY (id);
ALTER TABLE ONLY time_table_periods
    ADD CONSTRAINT time_table_periods_pkey PRIMARY KEY (id);
ALTER TABLE ONLY time_tables
    ADD CONSTRAINT time_tables_pkey PRIMARY KEY (id);
ALTER TABLE ONLY timebands
    ADD CONSTRAINT timebands_pkey PRIMARY KEY (id);
ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vehicle_journey_at_stops_pkey PRIMARY KEY (id);
ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vehicle_journeys_pkey PRIMARY KEY (id);

CREATE INDEX index_journey_frequencies_on_timeband_id ON journey_frequencies USING btree (timeband_id);
CREATE INDEX index_journey_frequencies_on_vehicle_journey_id ON journey_frequencies USING btree (vehicle_journey_id);
CREATE INDEX index_journey_pattern_id_on_journey_patterns_stop_points ON journey_patterns_stop_points USING btree (journey_pattern_id);
CREATE INDEX index_journey_pattern_sections_on_journey_pattern_id ON journey_pattern_sections USING btree (journey_pattern_id);
CREATE INDEX index_journey_pattern_sections_on_route_section_id ON journey_pattern_sections USING btree (route_section_id);
CREATE UNIQUE INDEX index_jps_on_journey_pattern_id_and_route_section_id_and_rank ON journey_pattern_sections USING btree (journey_pattern_id, route_section_id, rank);
CREATE INDEX index_time_table_dates_on_time_table_id ON time_table_dates USING btree (time_table_id);
CREATE INDEX index_time_table_periods_on_time_table_id ON time_table_periods USING btree (time_table_id);
CREATE INDEX index_time_tables_vehicle_journeys_on_time_table_id ON time_tables_vehicle_journeys USING btree (time_table_id);
CREATE INDEX index_time_tables_vehicle_journeys_on_vehicle_journey_id ON time_tables_vehicle_journeys USING btree (vehicle_journey_id);
CREATE INDEX index_vehicle_journey_at_stops_on_stop_pointid ON vehicle_journey_at_stops USING btree (stop_point_id);
CREATE INDEX index_vehicle_journey_at_stops_on_vehicle_journey_id ON vehicle_journey_at_stops USING btree (vehicle_journey_id);
CREATE INDEX index_vehicle_journeys_on_route_id ON vehicle_journeys USING btree (route_id);
CREATE UNIQUE INDEX journey_patterns_objectid_key ON journey_patterns USING btree (objectid);
CREATE UNIQUE INDEX routes_objectid_key ON routes USING btree (objectid);
CREATE UNIQUE INDEX stop_points_objectid_key ON stop_points USING btree (objectid);
CREATE UNIQUE INDEX time_tables_objectid_key ON time_tables USING btree (objectid);
CREATE UNIQUE INDEX vehicle_journeys_objectid_key ON vehicle_journeys USING btree (objectid);


ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT journey_frequencies_timeband_id_fk FOREIGN KEY (timeband_id) REFERENCES timebands(id) ON DELETE SET NULL;
ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT journey_frequencies_vehicle_journey_id_fk FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE SET NULL;
ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT arrival_point_fkey FOREIGN KEY (arrival_stop_point_id) REFERENCES stop_points(id) ON DELETE SET NULL;
ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT departure_point_fkey FOREIGN KEY (departure_stop_point_id) REFERENCES stop_points(id) ON DELETE SET NULL;
ALTER TABLE ONLY journey_pattern_sections
    ADD CONSTRAINT journey_pattern_sections_journey_pattern_id_fk FOREIGN KEY (journey_pattern_id) REFERENCES journey_patterns(id) ON DELETE CASCADE;
    
    

    
    
ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT jp_route_fkey FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE;
ALTER TABLE ONLY journey_patterns_stop_points
    ADD CONSTRAINT jpsp_jp_fkey FOREIGN KEY (journey_pattern_id) REFERENCES journey_patterns(id) ON DELETE CASCADE;
ALTER TABLE ONLY journey_patterns_stop_points
    ADD CONSTRAINT jpsp_stoppoint_fkey FOREIGN KEY (stop_point_id) REFERENCES stop_points(id) ON DELETE CASCADE;
ALTER TABLE ONLY routes
    ADD CONSTRAINT route_opposite_route_fkey FOREIGN KEY (opposite_route_id) REFERENCES routes(id) ON DELETE SET NULL;
ALTER TABLE ONLY time_table_dates
    ADD CONSTRAINT tm_date_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;
ALTER TABLE ONLY time_table_periods
    ADD CONSTRAINT tm_period_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;
ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vj_jp_fkey FOREIGN KEY (journey_pattern_id) REFERENCES journey_patterns(id) ON DELETE CASCADE;
ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vj_route_fkey FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE;
ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vjas_sp_fkey FOREIGN KEY (stop_point_id) REFERENCES stop_points(id) ON DELETE CASCADE;
ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vjas_vj_fkey FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE CASCADE;
ALTER TABLE ONLY time_tables_vehicle_journeys
    ADD CONSTRAINT vjtm_tm_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;
ALTER TABLE ONLY time_tables_vehicle_journeys
    ADD CONSTRAINT vjtm_vj_fkey FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE CASCADE;


GRANT ALL ON SCHEMA chouette_gui TO chouette;
GRANT ALL ON SCHEMA chouette_gui TO PUBLIC;
