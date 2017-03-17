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
    started_at date,
    ended_at date,
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
    updated_at timestamp without time zone
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

SELECT pg_catalog.setval('companies_id_seq', 1, true);
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
</Line>', NULL, NULL, '2017-02-10 09:45:19.680043', NULL);

SELECT pg_catalog.setval('lines_id_seq', 1, true);

INSERT INTO networks VALUES (1, 'STIF:CODIFLIGNE:PTNetwork:117', 2, 'chouette', NULL, NULL, 'Veolia Ecquevilly', NULL, NULL, NULL, NULL, NULL, '<Network version="any" changed="2009-12-02T00:00:00Z" id="STIF:CODIFLIGNE:PTNetwork:117">
  <Name>Veolia Ecquevilly</Name>
  <members>
    <LineRef ref="STIF:CODIFLIGNE:Line:C00108"/>
  </members>
</Network>', 1, '2017-02-10 09:45:19.627071', NULL);

SELECT pg_catalog.setval('networks_id_seq', 1, true);

INSERT INTO organisations VALUES (1, 'STIF', '2017-02-10 09:37:12.285489', '2017-02-10 09:44:28.887246', 'neptune', 'STIF', '2017-02-10 09:44:28.720673', '"functional_scope"=>"[]"');
INSERT INTO organisations VALUES (2, 'Cityway', '2017-02-10 10:05:53.212327', '2017-02-10 10:05:53.212327', 'neptune', 'CITYWAY', '2017-02-10 10:05:53.21035', '"functional_scope"=>"[\"STIF:CODIFLIGNE:Line:C00108\"]"');

SELECT pg_catalog.setval('organisations_id_seq', 2, true);

INSERT INTO referential_metadata VALUES (1, 1, '{1}', NULL, '2017-02-10 10:06:41.901097', '2017-02-10 10:06:41.901097', '{"[2017-02-10,2017-03-11)"}');

SELECT pg_catalog.setval('referential_metadata_id_seq', 1, true);

INSERT INTO referentials VALUES (1, 'test', 'chouette_gui', '2017-02-10 10:06:27.034272', '2017-02-10 10:06:27.034272', 'test', '', 'Paris', 'SRID=4326;POLYGON((-5.2 42.25,-5.2 51.1,8.23 51.1,8.23 42.25,-5.2 42.25))', 2, NULL, 2, 'Etienne Michel', 'neptune', 1, 1, 1, NULL, NULL, true);

SELECT pg_catalog.setval('referentials_id_seq', 1, true);

INSERT INTO stop_area_referentials VALUES (1, 'Reflex', '2017-02-10 09:37:12.116926', '2017-02-10 09:37:12.116926');

SELECT pg_catalog.setval('stop_area_referentials_id_seq', 1, true);

INSERT INTO stop_areas VALUES (1, NULL, 'FR:78217:ZDE:32531:STIF', 1, 'chouette', 'MOULIN A VENT', NULL, 'zdep', NULL, NULL, NULL, 1.8099622622031800, 48.9454845919617000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32531" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32531:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:45:37.406026', NULL);
INSERT INTO stop_areas VALUES (2, NULL, 'FR:78217:ZDE:32523:STIF', 1, 'chouette', 'CHEMIN NEUF', NULL, 'zdep', NULL, NULL, NULL, 1.8162940080552900, 48.9524979333468000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32523" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32523:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:45:48.545597', NULL);
INSERT INTO stop_areas VALUES (3, NULL, 'FR:78217:ZDE:32529:STIF', 1, 'chouette', 'LES BICHES', NULL, 'zdep', NULL, NULL, NULL, 1.8118139867675400, 48.9526329360882000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32529" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32529:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:45:49.633456', NULL);
INSERT INTO stop_areas VALUES (4, NULL, 'FR:78217:ZDE:32521:STIF', 1, 'chouette', 'LE CHATEAU', NULL, 'zdep', NULL, NULL, NULL, 1.8133756580597400, 48.9533662680704000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32521" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32521:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:45:49.675806', NULL);
INSERT INTO stop_areas VALUES (5, NULL, 'FR:78217:ZDE:32522:STIF', 1, 'chouette', 'LE CHATEAU', NULL, 'zdep', NULL, NULL, NULL, 1.8126173239686100, 48.9533412676803000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32522" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32522:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:45:49.699328', NULL);
INSERT INTO stop_areas VALUES (6, NULL, 'FR:78217:ZDE:32784:STIF', 1, 'chouette', 'BROUILLARD', NULL, 'zdep', NULL, NULL, NULL, 1.7875870622303600, 48.9263228997569000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32784" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32784:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:53:18.141605', NULL);
INSERT INTO stop_areas VALUES (7, NULL, 'FR:78217:ZDE:32785:STIF', 1, 'chouette', 'BROUILLARD', NULL, 'zdep', NULL, NULL, NULL, 1.7870004023971600, 48.9261295695634000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32785" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32785:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:53:18.166346', NULL);
INSERT INTO stop_areas VALUES (8, NULL, 'FR:78217:ZDE:32786:STIF', 1, 'chouette', 'CANADA', NULL, 'zdep', NULL, NULL, NULL, 1.7903770996057600, 48.9298595765723000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32786" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32786:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:53:21.017113', NULL);
INSERT INTO stop_areas VALUES (9, NULL, 'FR:78217:ZDE:32787:STIF', 1, 'chouette', 'CANADA', NULL, 'zdep', NULL, NULL, NULL, 1.7902854280654100, 48.9289362426669000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32787" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32787:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:53:21.038821', NULL);
INSERT INTO stop_areas VALUES (10, NULL, 'FR:78217:ZDE:32516:STIF', 1, 'chouette', 'ALLEE DE PINCELOUP', NULL, 'zdep', NULL, NULL, NULL, 1.8084105849672000, 48.9439162501291000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32516" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32516:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:53:32.279403', NULL);
INSERT INTO stop_areas VALUES (11, NULL, 'FR:78217:ZDE:32533:STIF', 1, 'chouette', 'POTEAU', NULL, 'zdep', NULL, NULL, NULL, 1.8300191012754500, 48.9566545958770000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32533" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32533:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:56:19.188452', NULL);
INSERT INTO stop_areas VALUES (12, NULL, 'FR:78217:ZDE:32519:STIF', 1, 'chouette', 'CARREFOUR ST MARTIN', NULL, 'zdep', NULL, NULL, NULL, 1.8195940225965100, 48.9537812663945000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32519" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32519:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:56:17.057362', NULL);
INSERT INTO stop_areas VALUES (13, NULL, 'FR:78217:ZDE:32520:STIF', 1, 'chouette', 'CARREFOUR ST MARTIN', NULL, 'zdep', NULL, NULL, NULL, 1.8193123651273500, 48.9536762647935000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32520" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32520:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:56:17.090476', NULL);
INSERT INTO stop_areas VALUES (14, NULL, 'FR:78217:ZDE:32527:STIF', 1, 'chouette', 'LA FALAISE', NULL, 'zdep', NULL, NULL, NULL, 1.8215623902590100, 48.9563379380118000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32527" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32527:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:56:19.04396', NULL);
INSERT INTO stop_areas VALUES (15, NULL, 'FR:78217:ZDE:32528:STIF', 1, 'chouette', 'LA FALAISE', NULL, 'zdep', NULL, NULL, NULL, 1.8215273921141900, 48.9563896027813000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32528" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32528:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:56:19.075433', NULL);
INSERT INTO stop_areas VALUES (16, NULL, 'FR:78217:ZDE:32532:STIF', 1, 'chouette', 'POTEAU', NULL, 'zdep', NULL, NULL, NULL, 1.8301257677224300, 48.9567112696982000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32532" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32532:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:56:19.154373', NULL);
INSERT INTO stop_areas VALUES (17, NULL, 'FR:78217:ZDE:13660:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.8115371679130100, 48.9573485884751000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="819300" created="2014-12-29T03:12:51.0Z" changed="2016-11-07T04:11:21.0Z" id="FR:78217:ZDE:13660:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>052052080</Value>
        </KeyValue>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>LINE_ID</Key>
         <Value>011011010</Value>
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
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-02-10 09:56:19.62122', NULL);
INSERT INTO stop_areas VALUES (18, NULL, 'FR:78217:ZDE:32530:STIF', 1, 'chouette', 'Mairie', NULL, 'zdep', NULL, NULL, NULL, 1.8116042808075700, 48.9572862999147000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="819294" created="2014-12-29T03:12:51.0Z" changed="2016-11-07T04:11:55.0Z" id="FR:78217:ZDE:32530:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
        <MobilityImpairedAccess>partial</MobilityImpairedAccess>
       </AccessibilityAssessment>
      </Quay>', NULL, '2017-02-10 09:56:19.65563', NULL);
INSERT INTO stop_areas VALUES (19, NULL, 'FR:78217:ZDE:41202:STIF', 1, 'chouette', 'GARE D''EPONE MEZIERES', NULL, 'zdep', NULL, NULL, NULL, 1.8086450611031900, 48.9633073081647000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="41202" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:41202:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:58:53.642561', NULL);
INSERT INTO stop_areas VALUES (20, NULL, 'FR:78217:ZDE:32503:STIF', 1, 'chouette', 'Annexe Mairie (Gare SNCF)', NULL, 'zdep', NULL, NULL, NULL, 1.8085190228973600, 48.9626212928230000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32503" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32503:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:58:53.710717', NULL);
INSERT INTO stop_areas VALUES (21, NULL, 'FR:78217:ZDE:18304:STIF', 1, 'chouette', 'Gare d''Epône Mézières', NULL, 'zdep', NULL, NULL, NULL, 1.8083189204177400, 48.9631353611361000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="18304" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:18304:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:58:53.762431', NULL);
INSERT INTO stop_areas VALUES (22, NULL, 'FR:78217:ZDE:18305:STIF', 1, 'chouette', 'Gare d''Epône Mézières', NULL, 'zdep', NULL, NULL, NULL, 1.8083195437807300, 48.9630724138501000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="18305" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:18305:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:58:53.796372', NULL);
INSERT INTO stop_areas VALUES (23, NULL, 'FR:78217:ZDE:418584:STIF', 1, 'chouette', 'GARE D''EPONE MEZIERES', NULL, 'zdep', NULL, NULL, NULL, 1.8086450611031900, 48.9633073081647000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="709210" created="2015-06-15T09:06:29.0Z" changed="2015-06-15T09:06:29.0Z" id="FR:78217:ZDE:418584:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:58:53.821219', NULL);
INSERT INTO stop_areas VALUES (24, NULL, 'FR:78217:ZDE:32509:STIF', 1, 'chouette', 'BOUT DU MONDE', NULL, 'zdep', NULL, NULL, NULL, 1.8298075100030400, 48.9683646251586000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32509" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32509:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:58:58.44123', NULL);
INSERT INTO stop_areas VALUES (25, NULL, 'FR:78217:ZDE:32504:STIF', 1, 'chouette', 'AVENUE D''EPONE', NULL, 'zdep', NULL, NULL, NULL, 1.8337208598697700, 48.9686746309032000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32504" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32504:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:58:58.678861', NULL);
INSERT INTO stop_areas VALUES (26, NULL, 'FR:78217:ZDE:32507:STIF', 1, 'chouette', 'BOULEVARD DE LA PAIX', NULL, 'zdep', NULL, NULL, NULL, 1.8359592073884300, 48.9698496280250000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32507" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32507:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:58:59.955092', NULL);
INSERT INTO stop_areas VALUES (27, NULL, 'FR:78217:ZDE:32510:STIF', 1, 'chouette', 'LES DOLMENS', NULL, 'zdep', NULL, NULL, NULL, 1.8328141968442100, 48.9702879693091000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32510" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32510:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:59:00.20431', NULL);
INSERT INTO stop_areas VALUES (28, NULL, 'FR:78217:ZDE:32514:STIF', 1, 'chouette', 'PLACE MARECHAL JUIN', NULL, 'zdep', NULL, NULL, NULL, 1.8369392346693400, 48.9715229669525000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32514" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32514:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:59:02.425365', NULL);
INSERT INTO stop_areas VALUES (29, NULL, 'FR:78217:ZDE:416465:STIF', 1, 'chouette', 'Elisab. Pl du Maréchal Juin', NULL, 'zdep', NULL, NULL, NULL, 1.8368735974221300, 48.9714362201824000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="666350" created="2015-05-12T09:05:28.0Z" changed="2015-05-12T09:05:28.0Z" id="FR:78217:ZDE:416465:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:59:02.458789', NULL);
INSERT INTO stop_areas VALUES (30, NULL, 'FR:78217:ZDE:32511:STIF', 1, 'chouette', 'MAIRIE / ECOLE', NULL, 'zdep', NULL, NULL, NULL, 1.8369025767705700, 48.9732296340157000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32511" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32511:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:59:04.610939', NULL);
INSERT INTO stop_areas VALUES (31, NULL, 'FR:78217:ZDE:32508:STIF', 1, 'chouette', 'LA BERGERIE', NULL, 'zdep', NULL, NULL, NULL, 1.8388059189735000, 48.9739196359739000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32508" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32508:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 09:59:05.633874', NULL);
INSERT INTO stop_areas VALUES (32, NULL, 'FR:78217:ZDE:32525:STIF', 1, 'chouette', 'EMILE SERGENT', NULL, 'zdep', NULL, NULL, NULL, 1.8170356954507600, 48.9568029441520000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="32525" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDE:32525:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 10:02:44.451375', NULL);
INSERT INTO stop_areas VALUES (33, NULL, 'FR:78217:ZDE:32524:STIF', 1, 'chouette', 'EMILE SERGENT', NULL, 'zdep', NULL, NULL, NULL, 1.8169827330564200, 48.9566739779480000, 'WGS84', NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<Quay xmlns:gml="http://www.opengis.net/gml/3.2" version="729344" created="2014-12-29T12:12:00.0Z" changed="2015-10-07T04:10:33.0Z" id="FR:78217:ZDE:32524:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
      </Quay>', NULL, '2017-02-10 10:02:44.472555', NULL);
INSERT INTO stop_areas VALUES (34, NULL, 'FR:78217:LDA:65045:STIF', 1, 'chouette', 'Moulinà Vent', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65045" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65045:STIF">
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
      </StopPlace>', NULL, '2017-02-10 10:05:27.928283', NULL);
INSERT INTO stop_areas VALUES (35, NULL, 'FR:78217:ZDL:53999:STIF', 1, 'chouette', 'Moulinà Vent', NULL, 'zdlr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53999" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53999:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
       <ParentSiteRef ref="FR:78217:LDA:65045:STIF" version="65045"/>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32531:STIF" version="32531"/>
       </quays>
      </StopPlace>', NULL, '2017-02-10 10:05:27.954129', NULL);
INSERT INTO stop_areas VALUES (36, NULL, 'FR:78217:LDA:65144:STIF', 1, 'chouette', 'Chemin Neuf', NULL, 'lda', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="65144" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:LDA:65144:STIF">
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
      </StopPlace>', NULL, '2017-02-10 10:05:32.80977', NULL);
INSERT INTO stop_areas VALUES (37, NULL, 'FR:78217:ZDL:53995:STIF', 1, 'chouette', 'Chemin Neuf', NULL, 'zdlr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53995" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53995:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
       <ParentSiteRef ref="FR:78217:LDA:65144:STIF" version="65144"/>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32523:STIF" version="32523"/>
       </quays>
      </StopPlace>', NULL, '2017-02-10 10:05:32.835216', NULL);
INSERT INTO stop_areas VALUES (38, NULL, 'FR:78217:ZDL:53998:STIF', 1, 'chouette', 'Les Biches', NULL, 'zdlr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53998" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53998:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
       <ParentSiteRef ref="FR:78217:LDA:65159:STIF" version="65159"/>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32529:STIF" version="32529"/>
       </quays>
      </StopPlace>', NULL, '2017-02-10 10:05:33.446592', NULL);
INSERT INTO stop_areas VALUES (39, NULL, 'FR:78217:ZDL:53994:STIF', 1, 'chouette', 'Le Château', NULL, 'zdlr', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '78217', 'Épône', NULL, NULL, 1, NULL, '<StopPlace version="53994" created="2014-12-29T03:12:51.0Z" changed="2014-12-29T03:12:51.0Z" id="FR:78217:ZDL:53994:STIF">
       <keyList>
        <KeyValue typeOfKey="OBJET_QUALIFIER">
         <Key>OBJECT_STATUS</Key>
         <Value>REFERENCE_OBJECT</Value>
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
       <ParentSiteRef ref="FR:78217:LDA:65159:STIF" version="65159"/>
       <StopPlaceType>onstreetBus</StopPlaceType>
       <quays>
        <QuayRef ref="FR:78217:ZDE:32521:STIF" version="32521"/>
        <QuayRef ref="FR:78217:ZDE:32522:STIF" version="32522"/>
       </quays>
      </StopPlace>', NULL, '2017-02-10 10:05:33.469636', NULL);

SELECT pg_catalog.setval('stop_areas_id_seq', 39, true);

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
    stop_area_ids integer[],
    line_id integer,
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
CREATE INDEX index_routing_constraint_zones_on_line_id ON routing_constraint_zones USING btree (line_id);
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
ALTER TABLE ONLY stop_points
    ADD CONSTRAINT stoppoint_route_fkey FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE;
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


