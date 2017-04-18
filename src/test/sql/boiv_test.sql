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
    message_attributs shared_extensions.hstore,
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
    type character varying(255),
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
    token_download character varying(255)
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

SELECT pg_catalog.setval('companies_id_seq', 3, true);
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

SELECT pg_catalog.setval('lines_id_seq', 2, true);

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

INSERT INTO referential_metadata VALUES (1, 1, '{1,2}', NULL, '2017-02-10 10:06:41.901097', '2017-02-10 10:06:41.901097', '{"[2017-02-10,2017-03-11)"}');

SELECT pg_catalog.setval('referential_metadata_id_seq', 1, true);

INSERT INTO referentials VALUES (1, 'test', 'chouette_gui', '2017-02-10 10:06:27.034272', '2017-02-10 10:06:27.034272', 'test', '', 'Paris', 'SRID=4326;POLYGON((-5.2 42.25,-5.2 51.1,8.23 51.1,8.23 42.25,-5.2 42.25))', 2, NULL, 2, 'Etienne Michel', 'neptune', 1, 1, 1, NULL, NULL, true);

SELECT pg_catalog.setval('referentials_id_seq', 1, true);

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
INSERT INTO stop_areas VALUES (125584, NULL, 'FR:78217:ZDE:50009034:STIF', 1, 'chouette', 'AVENUE D''EPONE', NULL, 'zdep', NULL, NULL, NULL, 1.8337208462130300, 48.9686746307704000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="445775" created="2015-02-03T07:02:44.0Z" changed="2015-02-03T07:02:44.0Z" id="FR:78217:ZDE:50009034:STIF">
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
INSERT INTO stop_areas VALUES (129290, NULL, 'FR:78217:ZDE:50009387:STIF', 1, 'chouette', 'CANADA', NULL, 'zdep', NULL, NULL, NULL, 1.7902854146282800, 48.9289362335376000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="444544" created="2015-02-03T06:02:34.0Z" changed="2015-02-03T06:02:34.0Z" id="FR:78217:ZDE:50009387:STIF">
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
INSERT INTO stop_areas VALUES (129791, NULL, 'FR:78217:ZDE:50009064:STIF', 1, 'chouette', 'EMILE SERGENT', NULL, 'zdep', NULL, NULL, NULL, 1.8170356954507600, 48.9568029441520000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="443297" created="2015-02-03T06:02:33.0Z" changed="2015-02-03T06:02:33.0Z" id="FR:78217:ZDE:50009064:STIF">
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
INSERT INTO stop_areas VALUES (129792, NULL, 'FR:78217:ZDE:50009063:STIF', 1, 'chouette', 'EMILE SERGENT', NULL, 'zdep', NULL, NULL, NULL, 1.8169827219037900, 48.9566756685959000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" dataSourceRef="STIF-REFLEX:Operator:21" version="717630" created="2015-02-03T06:02:33.0Z" changed="2015-07-17T09:07:43.0Z" id="FR:78217:ZDE:50009063:STIF">
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
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    objectid character varying(255) NOT NULL,
    object_version bigint,
    creator_id character varying(255)
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
    in_out boolean
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
    id bigint NOT NULL
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
    arrival_day_offset int not null default 0
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


--
--


