--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.13
-- Dumped by pg_dump version 9.4.13
-- Started on 2017-08-21 11:11:38 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 11 (class 2615 OID 533250)
-- Name: iev_export; Type: SCHEMA; Schema: -; Owner: chouette
--
DROP SCHEMA IF EXISTS iev_export CASCADE;

CREATE SCHEMA iev_export;



SET search_path = iev_export, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 231 (class 1259 OID 533251)
-- Name: footnotes; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE TABLE footnotes (
    id bigint NOT NULL,
    line_id bigint,
    code character varying(255),
    label character varying(255),
    checksum character varying(255),
    checksum_source text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    data_source_ref character varying
);



--
-- TOC entry 232 (class 1259 OID 533257)
-- Name: footnotes_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE footnotes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3776 (class 0 OID 0)
-- Dependencies: 232
-- Name: footnotes_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE footnotes_id_seq OWNED BY footnotes.id;


--
-- TOC entry 233 (class 1259 OID 533259)
-- Name: footnotes_vehicle_journeys; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE TABLE footnotes_vehicle_journeys (
    vehicle_journey_id bigint,
    footnote_id bigint
);



--
-- TOC entry 234 (class 1259 OID 533262)
-- Name: journey_frequencies; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

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



--
-- TOC entry 235 (class 1259 OID 533266)
-- Name: journey_frequencies_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE journey_frequencies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3777 (class 0 OID 0)
-- Dependencies: 235
-- Name: journey_frequencies_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE journey_frequencies_id_seq OWNED BY journey_frequencies.id;



--
-- TOC entry 238 (class 1259 OID 533273)
-- Name: journey_patterns; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

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
    updated_at timestamp without time zone,
    data_source_ref character varying
);



--
-- TOC entry 239 (class 1259 OID 533280)
-- Name: journey_patterns_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE journey_patterns_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3779 (class 0 OID 0)
-- Dependencies: 239
-- Name: journey_patterns_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE journey_patterns_id_seq OWNED BY journey_patterns.id;


--
-- TOC entry 240 (class 1259 OID 533282)
-- Name: journey_patterns_stop_points; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE TABLE journey_patterns_stop_points (
    journey_pattern_id bigint,
    stop_point_id bigint
);



--
-- TOC entry 241 (class 1259 OID 533285)
-- Name: routes; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

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
    updated_at timestamp without time zone,
    data_source_ref character varying
);



--
-- TOC entry 242 (class 1259 OID 533291)
-- Name: routes_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE routes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3780 (class 0 OID 0)
-- Dependencies: 242
-- Name: routes_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE routes_id_seq OWNED BY routes.id;


--
-- TOC entry 243 (class 1259 OID 533293)
-- Name: routing_constraint_zones; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

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
    updated_at timestamp without time zone,
    data_source_ref character varying
);



--
-- TOC entry 244 (class 1259 OID 533299)
-- Name: routing_constraint_zones_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE routing_constraint_zones_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3781 (class 0 OID 0)
-- Dependencies: 244
-- Name: routing_constraint_zones_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE routing_constraint_zones_id_seq OWNED BY routing_constraint_zones.id;


--
-- TOC entry 245 (class 1259 OID 533301)
-- Name: stop_points; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

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



--
-- TOC entry 246 (class 1259 OID 533307)
-- Name: stop_points_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE stop_points_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3782 (class 0 OID 0)
-- Dependencies: 246
-- Name: stop_points_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE stop_points_id_seq OWNED BY stop_points.id;


--
-- TOC entry 247 (class 1259 OID 533309)
-- Name: time_table_dates; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE TABLE time_table_dates (
    time_table_id bigint NOT NULL,
    date date,
    "position" integer NOT NULL,
    id bigint NOT NULL,
    in_out boolean,
    checksum character varying(255),
    checksum_source text
);



--
-- TOC entry 248 (class 1259 OID 533315)
-- Name: time_table_dates_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE time_table_dates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3783 (class 0 OID 0)
-- Dependencies: 248
-- Name: time_table_dates_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE time_table_dates_id_seq OWNED BY time_table_dates.id;


--
-- TOC entry 249 (class 1259 OID 533317)
-- Name: time_table_periods; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE TABLE time_table_periods (
    time_table_id bigint NOT NULL,
    period_start date,
    period_end date,
    "position" integer NOT NULL,
    id bigint NOT NULL,
    checksum character varying(255),
    checksum_source text
);



--
-- TOC entry 250 (class 1259 OID 533323)
-- Name: time_table_periods_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE time_table_periods_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3784 (class 0 OID 0)
-- Dependencies: 250
-- Name: time_table_periods_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE time_table_periods_id_seq OWNED BY time_table_periods.id;


--
-- TOC entry 251 (class 1259 OID 533325)
-- Name: time_tables; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

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
    updated_at timestamp without time zone,
    data_source_ref character varying
);



--
-- TOC entry 252 (class 1259 OID 533333)
-- Name: time_tables_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE time_tables_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3785 (class 0 OID 0)
-- Dependencies: 252
-- Name: time_tables_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE time_tables_id_seq OWNED BY time_tables.id;


--
-- TOC entry 253 (class 1259 OID 533335)
-- Name: time_tables_vehicle_journeys; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE TABLE time_tables_vehicle_journeys (
    time_table_id bigint,
    vehicle_journey_id bigint
);



--
-- TOC entry 254 (class 1259 OID 533338)
-- Name: timebands; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

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



--
-- TOC entry 255 (class 1259 OID 533344)
-- Name: timebands_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE timebands_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3786 (class 0 OID 0)
-- Dependencies: 255
-- Name: timebands_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE timebands_id_seq OWNED BY timebands.id;


--
-- TOC entry 256 (class 1259 OID 533346)
-- Name: vehicle_journey_at_stops; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

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
    departure_day_offset integer DEFAULT 0 NOT NULL,
    arrival_day_offset integer DEFAULT 0 NOT NULL,
    checksum character varying(255),
    checksum_source text
);



--
-- TOC entry 257 (class 1259 OID 533354)
-- Name: vehicle_journey_at_stops_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE vehicle_journey_at_stops_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3787 (class 0 OID 0)
-- Dependencies: 257
-- Name: vehicle_journey_at_stops_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE vehicle_journey_at_stops_id_seq OWNED BY vehicle_journey_at_stops.id;


--
-- TOC entry 258 (class 1259 OID 533356)
-- Name: vehicle_journeys; Type: TABLE; Schema: iev_export; Owner: chouette; Tablespace: 
--

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
    updated_at timestamp without time zone,
    data_source_ref character varying
);



--
-- TOC entry 259 (class 1259 OID 533363)
-- Name: vehicle_journeys_id_seq; Type: SEQUENCE; Schema: iev_export; Owner: chouette
--

CREATE SEQUENCE vehicle_journeys_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- TOC entry 3788 (class 0 OID 0)
-- Dependencies: 259
-- Name: vehicle_journeys_id_seq; Type: SEQUENCE OWNED BY; Schema: iev_export; Owner: chouette
--

ALTER SEQUENCE vehicle_journeys_id_seq OWNED BY vehicle_journeys.id;


--
-- TOC entry 3545 (class 2604 OID 533365)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY footnotes ALTER COLUMN id SET DEFAULT nextval('footnotes_id_seq'::regclass);


--
-- TOC entry 3547 (class 2604 OID 533366)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY journey_frequencies ALTER COLUMN id SET DEFAULT nextval('journey_frequencies_id_seq'::regclass);



--
-- TOC entry 3550 (class 2604 OID 533368)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY journey_patterns ALTER COLUMN id SET DEFAULT nextval('journey_patterns_id_seq'::regclass);


--
-- TOC entry 3551 (class 2604 OID 533369)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY routes ALTER COLUMN id SET DEFAULT nextval('routes_id_seq'::regclass);


--
-- TOC entry 3552 (class 2604 OID 533370)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY routing_constraint_zones ALTER COLUMN id SET DEFAULT nextval('routing_constraint_zones_id_seq'::regclass);


--
-- TOC entry 3553 (class 2604 OID 533371)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY stop_points ALTER COLUMN id SET DEFAULT nextval('stop_points_id_seq'::regclass);


--
-- TOC entry 3554 (class 2604 OID 533372)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY time_table_dates ALTER COLUMN id SET DEFAULT nextval('time_table_dates_id_seq'::regclass);


--
-- TOC entry 3555 (class 2604 OID 533373)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY time_table_periods ALTER COLUMN id SET DEFAULT nextval('time_table_periods_id_seq'::regclass);


--
-- TOC entry 3556 (class 2604 OID 533374)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY time_tables ALTER COLUMN id SET DEFAULT nextval('time_tables_id_seq'::regclass);


--
-- TOC entry 3559 (class 2604 OID 533375)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY timebands ALTER COLUMN id SET DEFAULT nextval('timebands_id_seq'::regclass);


--
-- TOC entry 3560 (class 2604 OID 533376)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY vehicle_journey_at_stops ALTER COLUMN id SET DEFAULT nextval('vehicle_journey_at_stops_id_seq'::regclass);


--
-- TOC entry 3563 (class 2604 OID 533377)
-- Name: id; Type: DEFAULT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY vehicle_journeys ALTER COLUMN id SET DEFAULT nextval('vehicle_journeys_id_seq'::regclass);


--
-- TOC entry 3742 (class 0 OID 533251)
-- Dependencies: 231
-- Data for Name: footnotes; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO footnotes VALUES (2, 5, '1', 'note numéro 1', NULL, NULL, '2017-08-21 10:57:20.856', '2017-08-21 10:57:20.856','CITYWAY');
INSERT INTO footnotes VALUES (3, 4, '1', 'note numéro 1', NULL, NULL, '2017-08-21 10:57:21.19', '2017-08-21 10:57:21.19','CITYWAY');
INSERT INTO footnotes VALUES (4, 7, '1', 'note numéro 1', NULL, NULL, '2017-08-21 10:57:21.484', '2017-08-21 10:57:21.484','CITYWAY');
INSERT INTO footnotes VALUES (5, 3, '1', 'note numéro 1', NULL, NULL, '2017-08-21 10:57:21.671', '2017-08-21 10:57:21.671','CITYWAY');
INSERT INTO footnotes VALUES (6, 6, '3', 'note numéro 3', NULL, NULL, '2017-08-21 10:57:21.815', '2017-08-21 10:57:21.815','CITYWAY');
INSERT INTO footnotes VALUES (7, 6, '1', 'note numéro 1', NULL, NULL, '2017-08-21 10:57:21.812', '2017-08-21 10:57:21.812','CITYWAY');
INSERT INTO footnotes VALUES (8, 6, '2', 'note numéro 2', NULL, NULL, '2017-08-21 10:57:21.813', '2017-08-21 10:57:21.813','CITYWAY');
INSERT INTO footnotes VALUES (9, 8, '2', 'note numéro 2', NULL, NULL, '2017-08-21 10:57:21.968', '2017-08-21 10:57:21.968','CITYWAY');


--
-- TOC entry 3789 (class 0 OID 0)
-- Dependencies: 232
-- Name: footnotes_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('footnotes_id_seq', 12, true);


--
-- TOC entry 3744 (class 0 OID 533259)
-- Dependencies: 233
-- Data for Name: footnotes_vehicle_journeys; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO footnotes_vehicle_journeys VALUES (2, 2);
INSERT INTO footnotes_vehicle_journeys VALUES (3, 2);
INSERT INTO footnotes_vehicle_journeys VALUES (4, 2);
INSERT INTO footnotes_vehicle_journeys VALUES (5, 2);
INSERT INTO footnotes_vehicle_journeys VALUES (6, 2);
INSERT INTO footnotes_vehicle_journeys VALUES (7, 2);
INSERT INTO footnotes_vehicle_journeys VALUES (8, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (9, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (10, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (11, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (12, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (13, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (14, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (15, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (16, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (17, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (18, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (19, 3);
INSERT INTO footnotes_vehicle_journeys VALUES (20, 4);
INSERT INTO footnotes_vehicle_journeys VALUES (21, 4);
INSERT INTO footnotes_vehicle_journeys VALUES (22, 4);
INSERT INTO footnotes_vehicle_journeys VALUES (23, 4);
INSERT INTO footnotes_vehicle_journeys VALUES (24, 5);
INSERT INTO footnotes_vehicle_journeys VALUES (25, 5);
INSERT INTO footnotes_vehicle_journeys VALUES (26, 5);
INSERT INTO footnotes_vehicle_journeys VALUES (27, 5);
INSERT INTO footnotes_vehicle_journeys VALUES (28, 5);
INSERT INTO footnotes_vehicle_journeys VALUES (29, 5);
INSERT INTO footnotes_vehicle_journeys VALUES (30, 7);
INSERT INTO footnotes_vehicle_journeys VALUES (31, 8);
INSERT INTO footnotes_vehicle_journeys VALUES (32, 7);
INSERT INTO footnotes_vehicle_journeys VALUES (32, 8);
INSERT INTO footnotes_vehicle_journeys VALUES (33, 7);
INSERT INTO footnotes_vehicle_journeys VALUES (33, 6);
INSERT INTO footnotes_vehicle_journeys VALUES (34, 6);
INSERT INTO footnotes_vehicle_journeys VALUES (35, 9);
INSERT INTO footnotes_vehicle_journeys VALUES (36, 9);
INSERT INTO footnotes_vehicle_journeys VALUES (37, 9);
INSERT INTO footnotes_vehicle_journeys VALUES (38, 9);


--
-- TOC entry 3745 (class 0 OID 533262)
-- Dependencies: 234
-- Data for Name: journey_frequencies; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--



--
-- TOC entry 3790 (class 0 OID 0)
-- Dependencies: 235
-- Name: journey_frequencies_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('journey_frequencies_id_seq', 1, true);



--
-- TOC entry 3749 (class 0 OID 533273)
-- Dependencies: 238
-- Data for Name: journey_patterns; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO journey_patterns VALUES (3, 2, 'CITYWAY:ServiceJourneyPattern:C00165-2:LOC', 1503305833129, NULL, 'bullion vers rambouillet', NULL, '05021', 'Mission 2', 12, 21, 0, NULL, NULL, '2017-08-21 10:57:20.842', '2017-08-21 10:57:20.842','CITYWAY');
INSERT INTO journey_patterns VALUES (2, 3, 'CITYWAY:ServiceJourneyPattern:C00165-1:LOC', 1503305833129, NULL, 'rambouillet vers bullion', NULL, '05010', 'Mission 1', 2, 11, 0, NULL, NULL, '2017-08-21 10:57:20.839', '2017-08-21 10:57:20.839','CITYWAY');
INSERT INTO journey_patterns VALUES (6, 4, 'CITYWAY:ServiceJourneyPattern:C00164-2:LOC', 1503305833129, NULL, ' omnibus rambouillet', NULL, '03021', 'dourdan omni', 29, 35, 0, NULL, NULL, '2017-08-21 10:57:21.188', '2017-08-21 10:57:21.188','CITYWAY');
INSERT INTO journey_patterns VALUES (7, 4, 'CITYWAY:ServiceJourneyPattern:C00164-4:LOC', 1503305833129, NULL, ' semi direct rambouillet', NULL, '03022', 'dourdan sd', 29, 35, 0, NULL, NULL, '2017-08-21 10:57:21.188', '2017-08-21 10:57:21.188','CITYWAY');
INSERT INTO journey_patterns VALUES (4, 5, 'CITYWAY:ServiceJourneyPattern:C00164-1:LOC', 1503305833129, NULL, 'omnibus pontevrard', NULL, '03011', 'pontevrard omni', 22, 28, 0, NULL, NULL, '2017-08-21 10:57:21.187', '2017-08-21 10:57:21.187','CITYWAY');
INSERT INTO journey_patterns VALUES (5, 5, 'CITYWAY:ServiceJourneyPattern:C00164-3:LOC', 1503305833129, NULL, 'semi direct pontevrard', NULL, '03012', 'pontevrard sd', 22, 28, 0, NULL, NULL, '2017-08-21 10:57:21.188', '2017-08-21 10:57:21.188','CITYWAY');
INSERT INTO journey_patterns VALUES (9, 6, 'CITYWAY:ServiceJourneyPattern:C00168-3:LOC', 1503305833129, NULL, 'auffargis SNCF rambouillet sncf', NULL, '2345', 'rambouillet', 43, 49, 0, NULL, NULL, '2017-08-21 10:57:21.479', '2017-08-21 10:57:21.479','CITYWAY');
INSERT INTO journey_patterns VALUES (11, 8, 'CITYWAY:ServiceJourneyPattern:C00168-4:LOC', 1503305833129, NULL, 'les essarts SNCF rambouillet sncf', NULL, '2345', 'rambouillet', 58, 65, 0, NULL, NULL, '2017-08-21 10:57:21.48', '2017-08-21 10:57:21.48','CITYWAY');
INSERT INTO journey_patterns VALUES (10, 9, 'CITYWAY:ServiceJourneyPattern:C00168-2:LOC', 1503305833129, NULL, 'de rambouillet aux essarts via le perray', NULL, '2345', 'les essarts sncf', 50, 57, 0, NULL, NULL, '2017-08-21 10:57:21.478', '2017-08-21 10:57:21.478','CITYWAY');
INSERT INTO journey_patterns VALUES (8, 7, 'CITYWAY:ServiceJourneyPattern:C00168-1:LOC', 1503305833129, NULL, 'de rambouillet vers auffargis ', NULL, '1234', 'Auffargis route du perray', 36, 42, 0, NULL, NULL, '2017-08-21 10:57:21.477', '2017-08-21 10:57:21.477','CITYWAY');
INSERT INTO journey_patterns VALUES (13, 10, 'CITYWAY:ServiceJourneyPattern:C00163-2:LOC', 1503305833129, NULL, 'poigny vers rambouillet', NULL, '01201', 'Mission 2', 71, 75, 0, NULL, NULL, '2017-08-21 10:57:21.67', '2017-08-21 10:57:21.67','CITYWAY');
INSERT INTO journey_patterns VALUES (12, 11, 'CITYWAY:ServiceJourneyPattern:C00163-1:LOC', 1503305833129, NULL, 'rambouillet vers poigny', NULL, '01101', 'Mission 1', 66, 70, 0, NULL, NULL, '2017-08-21 10:57:21.669', '2017-08-21 10:57:21.669','CITYWAY');
INSERT INTO journey_patterns VALUES (15, 12, 'CITYWAY:ServiceJourneyPattern:C00166-2:LOC', 1503305833129, NULL, 'Par là', NULL, '05021', 'Mission 2', 81, 85, 0, NULL, NULL, '2017-08-21 10:57:21.811', '2017-08-21 10:57:21.811','CITYWAY');
INSERT INTO journey_patterns VALUES (14, 13, 'CITYWAY:ServiceJourneyPattern:C00166-1:LOC', 1503305833129, NULL, 'vers orphin', NULL, '05011', 'Mission 1', 76, 80, 0, NULL, NULL, '2017-08-21 10:57:21.81', '2017-08-21 10:57:21.81','CITYWAY');
INSERT INTO journey_patterns VALUES (18, 14, 'CITYWAY:ServiceJourneyPattern:C00171-3:LOC', 1503305833129, NULL, 'orsonville rambouillet', NULL, '11031', 'vers rambouillet', 95, 99, 0, NULL, NULL, '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966','CITYWAY');
INSERT INTO journey_patterns VALUES (19, 14, 'CITYWAY:ServiceJourneyPattern:C00171-4:LOC', 1503305833129, NULL, 'auneau SNCF  rambouillet', NULL, '11031', 'vers rambouillet', 93, 99, 0, NULL, NULL, '2017-08-21 10:57:21.967', '2017-08-21 10:57:21.967','CITYWAY');
INSERT INTO journey_patterns VALUES (16, 15, 'CITYWAY:ServiceJourneyPattern:C00171-1:LOC', 1503305833129, NULL, 'orsonville eglise ', NULL, '11011', 'Vers orsonville', 86, 90, 0, NULL, NULL, '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966','CITYWAY');
INSERT INTO journey_patterns VALUES (17, 15, 'CITYWAY:ServiceJourneyPattern:C00171-2:LOC', 1503305833129, NULL, 'auneau SNCF ', NULL, '11021', 'vers auneau', 86, 92, 0, NULL, NULL, '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966','CITYWAY');


--
-- TOC entry 3792 (class 0 OID 0)
-- Dependencies: 239
-- Name: journey_patterns_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('journey_patterns_id_seq', 22, true);


--
-- TOC entry 3751 (class 0 OID 533282)
-- Dependencies: 240
-- Data for Name: journey_patterns_stop_points; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO journey_patterns_stop_points VALUES (2, 2);
INSERT INTO journey_patterns_stop_points VALUES (2, 3);
INSERT INTO journey_patterns_stop_points VALUES (2, 4);
INSERT INTO journey_patterns_stop_points VALUES (2, 5);
INSERT INTO journey_patterns_stop_points VALUES (2, 6);
INSERT INTO journey_patterns_stop_points VALUES (2, 7);
INSERT INTO journey_patterns_stop_points VALUES (2, 8);
INSERT INTO journey_patterns_stop_points VALUES (2, 9);
INSERT INTO journey_patterns_stop_points VALUES (2, 10);
INSERT INTO journey_patterns_stop_points VALUES (2, 11);
INSERT INTO journey_patterns_stop_points VALUES (3, 12);
INSERT INTO journey_patterns_stop_points VALUES (3, 13);
INSERT INTO journey_patterns_stop_points VALUES (3, 14);
INSERT INTO journey_patterns_stop_points VALUES (3, 15);
INSERT INTO journey_patterns_stop_points VALUES (3, 16);
INSERT INTO journey_patterns_stop_points VALUES (3, 17);
INSERT INTO journey_patterns_stop_points VALUES (3, 18);
INSERT INTO journey_patterns_stop_points VALUES (3, 19);
INSERT INTO journey_patterns_stop_points VALUES (3, 20);
INSERT INTO journey_patterns_stop_points VALUES (3, 21);
INSERT INTO journey_patterns_stop_points VALUES (4, 22);
INSERT INTO journey_patterns_stop_points VALUES (4, 23);
INSERT INTO journey_patterns_stop_points VALUES (4, 24);
INSERT INTO journey_patterns_stop_points VALUES (4, 25);
INSERT INTO journey_patterns_stop_points VALUES (4, 26);
INSERT INTO journey_patterns_stop_points VALUES (4, 27);
INSERT INTO journey_patterns_stop_points VALUES (4, 28);
INSERT INTO journey_patterns_stop_points VALUES (5, 22);
INSERT INTO journey_patterns_stop_points VALUES (5, 25);
INSERT INTO journey_patterns_stop_points VALUES (5, 27);
INSERT INTO journey_patterns_stop_points VALUES (5, 28);
INSERT INTO journey_patterns_stop_points VALUES (6, 29);
INSERT INTO journey_patterns_stop_points VALUES (6, 30);
INSERT INTO journey_patterns_stop_points VALUES (6, 31);
INSERT INTO journey_patterns_stop_points VALUES (6, 32);
INSERT INTO journey_patterns_stop_points VALUES (6, 33);
INSERT INTO journey_patterns_stop_points VALUES (6, 34);
INSERT INTO journey_patterns_stop_points VALUES (6, 35);
INSERT INTO journey_patterns_stop_points VALUES (7, 29);
INSERT INTO journey_patterns_stop_points VALUES (7, 30);
INSERT INTO journey_patterns_stop_points VALUES (7, 32);
INSERT INTO journey_patterns_stop_points VALUES (7, 35);
INSERT INTO journey_patterns_stop_points VALUES (8, 36);
INSERT INTO journey_patterns_stop_points VALUES (8, 37);
INSERT INTO journey_patterns_stop_points VALUES (8, 38);
INSERT INTO journey_patterns_stop_points VALUES (8, 39);
INSERT INTO journey_patterns_stop_points VALUES (8, 40);
INSERT INTO journey_patterns_stop_points VALUES (8, 41);
INSERT INTO journey_patterns_stop_points VALUES (8, 42);
INSERT INTO journey_patterns_stop_points VALUES (9, 43);
INSERT INTO journey_patterns_stop_points VALUES (9, 44);
INSERT INTO journey_patterns_stop_points VALUES (9, 45);
INSERT INTO journey_patterns_stop_points VALUES (9, 46);
INSERT INTO journey_patterns_stop_points VALUES (9, 47);
INSERT INTO journey_patterns_stop_points VALUES (9, 48);
INSERT INTO journey_patterns_stop_points VALUES (9, 49);
INSERT INTO journey_patterns_stop_points VALUES (10, 50);
INSERT INTO journey_patterns_stop_points VALUES (10, 51);
INSERT INTO journey_patterns_stop_points VALUES (10, 52);
INSERT INTO journey_patterns_stop_points VALUES (10, 53);
INSERT INTO journey_patterns_stop_points VALUES (10, 54);
INSERT INTO journey_patterns_stop_points VALUES (10, 55);
INSERT INTO journey_patterns_stop_points VALUES (10, 56);
INSERT INTO journey_patterns_stop_points VALUES (10, 57);
INSERT INTO journey_patterns_stop_points VALUES (11, 58);
INSERT INTO journey_patterns_stop_points VALUES (11, 59);
INSERT INTO journey_patterns_stop_points VALUES (11, 60);
INSERT INTO journey_patterns_stop_points VALUES (11, 61);
INSERT INTO journey_patterns_stop_points VALUES (11, 62);
INSERT INTO journey_patterns_stop_points VALUES (11, 63);
INSERT INTO journey_patterns_stop_points VALUES (11, 64);
INSERT INTO journey_patterns_stop_points VALUES (11, 65);
INSERT INTO journey_patterns_stop_points VALUES (12, 66);
INSERT INTO journey_patterns_stop_points VALUES (12, 67);
INSERT INTO journey_patterns_stop_points VALUES (12, 68);
INSERT INTO journey_patterns_stop_points VALUES (12, 69);
INSERT INTO journey_patterns_stop_points VALUES (12, 70);
INSERT INTO journey_patterns_stop_points VALUES (13, 71);
INSERT INTO journey_patterns_stop_points VALUES (13, 72);
INSERT INTO journey_patterns_stop_points VALUES (13, 73);
INSERT INTO journey_patterns_stop_points VALUES (13, 74);
INSERT INTO journey_patterns_stop_points VALUES (13, 75);
INSERT INTO journey_patterns_stop_points VALUES (14, 76);
INSERT INTO journey_patterns_stop_points VALUES (14, 77);
INSERT INTO journey_patterns_stop_points VALUES (14, 78);
INSERT INTO journey_patterns_stop_points VALUES (14, 79);
INSERT INTO journey_patterns_stop_points VALUES (14, 80);
INSERT INTO journey_patterns_stop_points VALUES (15, 81);
INSERT INTO journey_patterns_stop_points VALUES (15, 82);
INSERT INTO journey_patterns_stop_points VALUES (15, 83);
INSERT INTO journey_patterns_stop_points VALUES (15, 84);
INSERT INTO journey_patterns_stop_points VALUES (15, 85);
INSERT INTO journey_patterns_stop_points VALUES (16, 86);
INSERT INTO journey_patterns_stop_points VALUES (16, 87);
INSERT INTO journey_patterns_stop_points VALUES (16, 88);
INSERT INTO journey_patterns_stop_points VALUES (16, 90);
INSERT INTO journey_patterns_stop_points VALUES (17, 86);
INSERT INTO journey_patterns_stop_points VALUES (17, 88);
INSERT INTO journey_patterns_stop_points VALUES (17, 89);
INSERT INTO journey_patterns_stop_points VALUES (17, 91);
INSERT INTO journey_patterns_stop_points VALUES (17, 92);
INSERT INTO journey_patterns_stop_points VALUES (18, 95);
INSERT INTO journey_patterns_stop_points VALUES (18, 97);
INSERT INTO journey_patterns_stop_points VALUES (18, 98);
INSERT INTO journey_patterns_stop_points VALUES (18, 99);
INSERT INTO journey_patterns_stop_points VALUES (19, 93);
INSERT INTO journey_patterns_stop_points VALUES (19, 94);
INSERT INTO journey_patterns_stop_points VALUES (19, 96);
INSERT INTO journey_patterns_stop_points VALUES (19, 97);
INSERT INTO journey_patterns_stop_points VALUES (19, 99);


--
-- TOC entry 3752 (class 0 OID 533285)
-- Dependencies: 241
-- Data for Name: routes; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO routes VALUES (2, 5, 'CITYWAY:Route:C00165-2:LOC', 1503305833129, NULL, 'route 2', NULL, 3, 'bullon vers rambouillet', NULL, 'R', 'inbound', NULL, NULL, '2017-08-21 10:57:20.837', '2017-08-21 10:57:20.837','CITYWAY');
INSERT INTO routes VALUES (3, 5, 'CITYWAY:Route:C00165-1:LOC', 1503305833129, NULL, 'route 1', NULL, 2, 'rambouillet vers bullion', NULL, 'A', 'outbound', NULL, NULL, '2017-08-21 10:57:20.834', '2017-08-21 10:57:20.834','CITYWAY');
INSERT INTO routes VALUES (4, 4, 'CITYWAY:Route:C00164-2:LOC', 1503305833129, NULL, 'route 2', NULL, 5, ' pontevrard vers rambouillet', NULL, 'R', 'inbound', NULL, NULL, '2017-08-21 10:57:21.187', '2017-08-21 10:57:21.187','CITYWAY');
INSERT INTO routes VALUES (5, 4, 'CITYWAY:Route:C00164-1:LOC', 1503305833129, NULL, 'route 1', NULL, 4, 'rambouillet vers pontevrard', NULL, 'A', 'outbound', NULL, NULL, '2017-08-21 10:57:21.187', '2017-08-21 10:57:21.187','CITYWAY');
INSERT INTO routes VALUES (6, 7, 'CITYWAY:Route:C00168-3:LOC', 1503305833129, NULL, 'route 3', NULL, 7, 'auffargis vers rambouillet', NULL, 'R', 'inbound', NULL, NULL, '2017-08-21 10:57:21.477', '2017-08-21 10:57:21.477','CITYWAY');
INSERT INTO routes VALUES (7, 7, 'CITYWAY:Route:C00168-1:LOC', 1503305833129, NULL, 'route 1', NULL, 6, 'rambouillet vers auffargis', NULL, 'A', 'outbound', NULL, NULL, '2017-08-21 10:57:21.476', '2017-08-21 10:57:21.476','CITYWAY');
INSERT INTO routes VALUES (8, 7, 'CITYWAY:Route:C00168-4:LOC', 1503305833129, NULL, 'route 4', NULL, 9, 'les essarts vers rambouillet', NULL, 'R', 'inbound', NULL, NULL, '2017-08-21 10:57:21.477', '2017-08-21 10:57:21.477','CITYWAY');
INSERT INTO routes VALUES (9, 7, 'CITYWAY:Route:C00168-2:LOC', 1503305833129, NULL, 'route 2', NULL, 8, 'rambouillet vers les essarts', NULL, 'A', 'outbound', NULL, NULL, '2017-08-21 10:57:21.477', '2017-08-21 10:57:21.477','CITYWAY');
INSERT INTO routes VALUES (10, 3, 'CITYWAY:Route:C00163-2:LOC', 1503305833129, NULL, ' itinéraire poigny vers rambouillet', NULL, 11, 'rambouillet SNCF', NULL, 'R', 'inbound', NULL, NULL, '2017-08-21 10:57:21.669', '2017-08-21 10:57:21.669','CITYWAY');
INSERT INTO routes VALUES (11, 3, 'CITYWAY:Route:C00163-1:LOC', 1503305833129, NULL, ' itinéraire rambouillet vers poigny', NULL, 10, 'poigny feuillettes', NULL, 'A', 'outbound', NULL, NULL, '2017-08-21 10:57:21.668', '2017-08-21 10:57:21.668','CITYWAY');
INSERT INTO routes VALUES (12, 6, 'CITYWAY:Route:C00166-2:LOC', 1503305833129, NULL, 'de Orphin vers rambouillet', NULL, 13, 'vers rambouillet', NULL, 'R', 'inbound', NULL, NULL, '2017-08-21 10:57:21.81', '2017-08-21 10:57:21.81','CITYWAY');
INSERT INTO routes VALUES (13, 6, 'CITYWAY:Route:C00166-1:LOC', 1503305833129, NULL, 'de rambouillet vers orphin', NULL, 12, 'vers orphin', NULL, 'A', 'outbound', NULL, NULL, '2017-08-21 10:57:21.81', '2017-08-21 10:57:21.81','CITYWAY');
INSERT INTO routes VALUES (14, 8, 'CITYWAY:Route:C00171-2:LOC', 1503305833129, NULL, 'route 2', NULL, 15, 'Auneau Rambouillet', NULL, 'R', 'inbound', NULL, NULL, '2017-08-21 10:57:21.965', '2017-08-21 10:57:21.965','CITYWAY');
INSERT INTO routes VALUES (15, 8, 'CITYWAY:Route:C00171-1:LOC', 1503305833129, NULL, 'route 1', NULL, 14, 'Rambouillet Auneau ', NULL, 'A', 'outbound', NULL, NULL, '2017-08-21 10:57:21.965', '2017-08-21 10:57:21.965','CITYWAY');


--
-- TOC entry 3793 (class 0 OID 0)
-- Dependencies: 242
-- Name: routes_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('routes_id_seq', 52, true);


--
-- TOC entry 3754 (class 0 OID 533293)
-- Dependencies: 243
-- Data for Name: routing_constraint_zones; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO routing_constraint_zones VALUES (12, 'ITL 1', '{67,68}', 11, 'CITYWAY:RoutingConstraintZone:C00163-1-1:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.671', '2017-08-21 10:57:21.671','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (13, 'ITL 2', '{73,74}', 10, 'CITYWAY:RoutingConstraintZone:C00163-2-2:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.671', '2017-08-21 10:57:21.671','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (2, 'ITL RBT clairefontaine ', '{2,3,4,5}', 3, 'CITYWAY:RoutingConstraintZone:C00165-1-1:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:20.851', '2017-08-21 10:57:20.851','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (3, 'ITL bullion ', '{10,11}', 3, 'CITYWAY:RoutingConstraintZone:C00165-1-3:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:20.852', '2017-08-21 10:57:20.852','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (4, 'ITL clairefontaine RBT', '{18,19,20,21}', 2, 'CITYWAY:RoutingConstraintZone:C00165-2-2:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:20.852', '2017-08-21 10:57:20.852','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (5, 'ITL bullion ', '{12,13}', 2, 'CITYWAY:RoutingConstraintZone:C00165-2-3:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:20.852', '2017-08-21 10:57:20.852','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (14, 'ITL 1', '{76,77,78}', 13, 'CITYWAY:RoutingConstraintZone:C00166-1-1:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.812', '2017-08-21 10:57:21.812','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (15, 'ITL 2', '{83,84,85}', 12, 'CITYWAY:RoutingConstraintZone:C00166-2-2:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.812', '2017-08-21 10:57:21.812','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (6, 'ITL 1', '{23,24}', 5, 'CITYWAY:RoutingConstraintZone:C00164-1-1:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.19', '2017-08-21 10:57:21.19','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (7, 'ITL 2', '{33,34}', 4, 'CITYWAY:RoutingConstraintZone:C00164-2-2:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.19', '2017-08-21 10:57:21.19','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (16, 'ITL 1', '{86,87}', 15, 'CITYWAY:RoutingConstraintZone:C00171-1-1:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.968', '2017-08-21 10:57:21.968','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (17, 'ITL 2', '{98,99}', 14, 'CITYWAY:RoutingConstraintZone:C00171-2-2:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.968', '2017-08-21 10:57:21.968','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (8, 'ITL 1', '{38,39}', 7, 'CITYWAY:RoutingConstraintZone:C00168-1-1:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.483', '2017-08-21 10:57:21.483','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (9, 'ITL 2', '{46,47}', 6, 'CITYWAY:RoutingConstraintZone:C00168-3-2:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.483', '2017-08-21 10:57:21.483','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (10, 'ITL 1', '{52,53}', 9, 'CITYWAY:RoutingConstraintZone:C00168-2-1:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.483', '2017-08-21 10:57:21.483','CITYWAY');
INSERT INTO routing_constraint_zones VALUES (11, 'ITL 2', '{62,63}', 8, 'CITYWAY:RoutingConstraintZone:C00168-4-2:LOC', 1503305833129, NULL, NULL, NULL, '2017-08-21 10:57:21.483', '2017-08-21 10:57:21.483','CITYWAY');


--
-- TOC entry 3794 (class 0 OID 0)
-- Dependencies: 244
-- Name: routing_constraint_zones_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('routing_constraint_zones_id_seq', 52, true);


--
-- TOC entry 3756 (class 0 OID 533301)
-- Dependencies: 245
-- Data for Name: stop_points; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO stop_points VALUES (2, 3, 93531, 'CITYWAY:ScheduledStopPoint:C00165-1-1-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:20.84', '2017-08-21 10:57:20.84');
INSERT INTO stop_points VALUES (3, 3, 98795, 'CITYWAY:ScheduledStopPoint:C00165-1-1-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:20.841', '2017-08-21 10:57:20.841');
INSERT INTO stop_points VALUES (4, 3, 77033, 'CITYWAY:ScheduledStopPoint:C00165-1-1-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:20.841', '2017-08-21 10:57:20.841');
INSERT INTO stop_points VALUES (5, 3, 114340, 'CITYWAY:ScheduledStopPoint:C00165-1-1-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:20.841', '2017-08-21 10:57:20.841');
INSERT INTO stop_points VALUES (6, 3, 77034, 'CITYWAY:ScheduledStopPoint:C00165-1-1-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:20.841', '2017-08-21 10:57:20.841');
INSERT INTO stop_points VALUES (7, 3, 114336, 'CITYWAY:ScheduledStopPoint:C00165-1-1-6-6:LOC', 1503305833129, NULL, 5, 'normal', 'normal', '2017-08-21 10:57:20.841', '2017-08-21 10:57:20.841');
INSERT INTO stop_points VALUES (8, 3, 114334, 'CITYWAY:ScheduledStopPoint:C00165-1-1-7-7:LOC', 1503305833129, NULL, 6, 'normal', 'normal', '2017-08-21 10:57:20.841', '2017-08-21 10:57:20.841');
INSERT INTO stop_points VALUES (9, 3, 114331, 'CITYWAY:ScheduledStopPoint:C00165-1-1-8-8:LOC', 1503305833129, NULL, 7, 'normal', 'normal', '2017-08-21 10:57:20.841', '2017-08-21 10:57:20.841');
INSERT INTO stop_points VALUES (10, 3, 131451, 'CITYWAY:ScheduledStopPoint:C00165-1-1-9-9:LOC', 1503305833129, NULL, 8, 'normal', 'normal', '2017-08-21 10:57:20.842', '2017-08-21 10:57:20.842');
INSERT INTO stop_points VALUES (11, 3, 131448, 'CITYWAY:ScheduledStopPoint:C00165-1-1-10-10:LOC', 1503305833129, NULL, 9, 'normal', 'normal', '2017-08-21 10:57:20.842', '2017-08-21 10:57:20.842');
INSERT INTO stop_points VALUES (12, 2, 131446, 'CITYWAY:ScheduledStopPoint:C00165-2-2-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:20.843', '2017-08-21 10:57:20.843');
INSERT INTO stop_points VALUES (13, 2, 131450, 'CITYWAY:ScheduledStopPoint:C00165-2-2-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:20.843', '2017-08-21 10:57:20.843');
INSERT INTO stop_points VALUES (14, 2, 114330, 'CITYWAY:ScheduledStopPoint:C00165-2-2-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:20.843', '2017-08-21 10:57:20.843');
INSERT INTO stop_points VALUES (15, 2, 114335, 'CITYWAY:ScheduledStopPoint:C00165-2-2-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:20.843', '2017-08-21 10:57:20.843');
INSERT INTO stop_points VALUES (16, 2, 114337, 'CITYWAY:ScheduledStopPoint:C00165-2-2-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:20.843', '2017-08-21 10:57:20.843');
INSERT INTO stop_points VALUES (17, 2, 77035, 'CITYWAY:ScheduledStopPoint:C00165-2-2-6-6:LOC', 1503305833129, NULL, 5, 'normal', 'normal', '2017-08-21 10:57:20.844', '2017-08-21 10:57:20.844');
INSERT INTO stop_points VALUES (18, 2, 114341, 'CITYWAY:ScheduledStopPoint:C00165-2-2-7-7:LOC', 1503305833129, NULL, 6, 'normal', 'normal', '2017-08-21 10:57:20.844', '2017-08-21 10:57:20.844');
INSERT INTO stop_points VALUES (19, 2, 77032, 'CITYWAY:ScheduledStopPoint:C00165-2-2-8-8:LOC', 1503305833129, NULL, 7, 'normal', 'normal', '2017-08-21 10:57:20.844', '2017-08-21 10:57:20.844');
INSERT INTO stop_points VALUES (20, 2, 98794, 'CITYWAY:ScheduledStopPoint:C00165-2-2-9-9:LOC', 1503305833129, NULL, 8, 'normal', 'normal', '2017-08-21 10:57:20.844', '2017-08-21 10:57:20.844');
INSERT INTO stop_points VALUES (21, 2, 93531, 'CITYWAY:ScheduledStopPoint:C00165-2-2-10-10:LOC', 1503305833129, NULL, 9, 'normal', 'normal', '2017-08-21 10:57:20.844', '2017-08-21 10:57:20.844');
INSERT INTO stop_points VALUES (22, 5, 92773, 'CITYWAY:ScheduledStopPoint:C00164-1-1-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.187', '2017-08-21 10:57:21.187');
INSERT INTO stop_points VALUES (23, 5, 122614, 'CITYWAY:ScheduledStopPoint:C00164-1-1-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'forbidden', '2017-08-21 10:57:21.187', '2017-08-21 10:57:21.187');
INSERT INTO stop_points VALUES (24, 5, 77306, 'CITYWAY:ScheduledStopPoint:C00164-1-1-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'forbidden', '2017-08-21 10:57:21.187', '2017-08-21 10:57:21.187');
INSERT INTO stop_points VALUES (25, 5, 77300, 'CITYWAY:ScheduledStopPoint:C00164-1-1-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.187', '2017-08-21 10:57:21.187');
INSERT INTO stop_points VALUES (26, 5, 77304, 'CITYWAY:ScheduledStopPoint:C00164-1-1-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.187', '2017-08-21 10:57:21.187');
INSERT INTO stop_points VALUES (27, 5, 77246, 'CITYWAY:ScheduledStopPoint:C00164-1-1-6-6:LOC', 1503305833129, NULL, 5, 'normal', 'normal', '2017-08-21 10:57:21.187', '2017-08-21 10:57:21.187');
INSERT INTO stop_points VALUES (28, 5, 77248, 'CITYWAY:ScheduledStopPoint:C00164-1-1-7-7:LOC', 1503305833129, NULL, 6, 'normal', 'normal', '2017-08-21 10:57:21.187', '2017-08-21 10:57:21.187');
INSERT INTO stop_points VALUES (29, 4, 77249, 'CITYWAY:ScheduledStopPoint:C00164-2-2-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.188', '2017-08-21 10:57:21.188');
INSERT INTO stop_points VALUES (30, 4, 77247, 'CITYWAY:ScheduledStopPoint:C00164-2-2-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:21.188', '2017-08-21 10:57:21.188');
INSERT INTO stop_points VALUES (31, 4, 77303, 'CITYWAY:ScheduledStopPoint:C00164-2-2-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:21.188', '2017-08-21 10:57:21.188');
INSERT INTO stop_points VALUES (32, 4, 77299, 'CITYWAY:ScheduledStopPoint:C00164-2-2-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.188', '2017-08-21 10:57:21.188');
INSERT INTO stop_points VALUES (33, 4, 77305, 'CITYWAY:ScheduledStopPoint:C00164-2-2-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.188', '2017-08-21 10:57:21.188');
INSERT INTO stop_points VALUES (34, 4, 122614, 'CITYWAY:ScheduledStopPoint:C00164-2-2-6-6:LOC', 1503305833129, NULL, 5, 'normal', 'normal', '2017-08-21 10:57:21.188', '2017-08-21 10:57:21.188');
INSERT INTO stop_points VALUES (35, 4, 92773, 'CITYWAY:ScheduledStopPoint:C00164-2-2-7-7:LOC', 1503305833129, NULL, 6, 'normal', 'normal', '2017-08-21 10:57:21.188', '2017-08-21 10:57:21.188');
INSERT INTO stop_points VALUES (36, 7, 131672, 'CITYWAY:ScheduledStopPoint:C00168-1-1-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.477', '2017-08-21 10:57:21.477');
INSERT INTO stop_points VALUES (37, 7, 85387, 'CITYWAY:ScheduledStopPoint:C00168-1-1-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:21.477', '2017-08-21 10:57:21.477');
INSERT INTO stop_points VALUES (38, 7, 93531, 'CITYWAY:ScheduledStopPoint:C00168-1-1-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:21.478', '2017-08-21 10:57:21.478');
INSERT INTO stop_points VALUES (39, 7, 132100, 'CITYWAY:ScheduledStopPoint:C00168-1-1-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.478', '2017-08-21 10:57:21.478');
INSERT INTO stop_points VALUES (40, 7, 98357, 'CITYWAY:ScheduledStopPoint:C00168-1-1-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.478', '2017-08-21 10:57:21.478');
INSERT INTO stop_points VALUES (41, 7, 82960, 'CITYWAY:ScheduledStopPoint:C00168-1-1-6-6:LOC', 1503305833129, NULL, 5, 'normal', 'normal', '2017-08-21 10:57:21.478', '2017-08-21 10:57:21.478');
INSERT INTO stop_points VALUES (42, 7, 114296, 'CITYWAY:ScheduledStopPoint:C00168-1-1-7-7:LOC', 1503305833129, NULL, 6, 'normal', 'normal', '2017-08-21 10:57:21.478', '2017-08-21 10:57:21.478');
INSERT INTO stop_points VALUES (43, 6, 114297, 'CITYWAY:ScheduledStopPoint:C00168-3-2-4-4:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.479', '2017-08-21 10:57:21.479');
INSERT INTO stop_points VALUES (44, 6, 82959, 'CITYWAY:ScheduledStopPoint:C00168-3-2-5-5:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:21.479', '2017-08-21 10:57:21.479');
INSERT INTO stop_points VALUES (45, 6, 98357, 'CITYWAY:ScheduledStopPoint:C00168-3-2-6-6:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:21.48', '2017-08-21 10:57:21.48');
INSERT INTO stop_points VALUES (46, 6, 132099, 'CITYWAY:ScheduledStopPoint:C00168-3-2-7-7:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.48', '2017-08-21 10:57:21.48');
INSERT INTO stop_points VALUES (47, 6, 93531, 'CITYWAY:ScheduledStopPoint:C00168-3-2-8-8:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.48', '2017-08-21 10:57:21.48');
INSERT INTO stop_points VALUES (48, 6, 85385, 'CITYWAY:ScheduledStopPoint:C00168-3-2-9-9:LOC', 1503305833129, NULL, 5, 'normal', 'normal', '2017-08-21 10:57:21.48', '2017-08-21 10:57:21.48');
INSERT INTO stop_points VALUES (49, 6, 131671, 'CITYWAY:ScheduledStopPoint:C00168-3-2-10-10:LOC', 1503305833129, NULL, 6, 'normal', 'normal', '2017-08-21 10:57:21.48', '2017-08-21 10:57:21.48');
INSERT INTO stop_points VALUES (50, 9, 131672, 'CITYWAY:ScheduledStopPoint:C00168-2-1-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.478', '2017-08-21 10:57:21.478');
INSERT INTO stop_points VALUES (51, 9, 85387, 'CITYWAY:ScheduledStopPoint:C00168-2-1-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:21.478', '2017-08-21 10:57:21.478');
INSERT INTO stop_points VALUES (52, 9, 93531, 'CITYWAY:ScheduledStopPoint:C00168-2-1-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:21.478', '2017-08-21 10:57:21.478');
INSERT INTO stop_points VALUES (53, 9, 132100, 'CITYWAY:ScheduledStopPoint:C00168-2-1-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.479', '2017-08-21 10:57:21.479');
INSERT INTO stop_points VALUES (54, 9, 98357, 'CITYWAY:ScheduledStopPoint:C00168-2-1-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.479', '2017-08-21 10:57:21.479');
INSERT INTO stop_points VALUES (55, 9, 77146, 'CITYWAY:ScheduledStopPoint:C00168-2-1-8-6:LOC', 1503305833129, NULL, 5, 'normal', 'normal', '2017-08-21 10:57:21.479', '2017-08-21 10:57:21.479');
INSERT INTO stop_points VALUES (56, 9, 77151, 'CITYWAY:ScheduledStopPoint:C00168-2-1-9-7:LOC', 1503305833129, NULL, 6, 'normal', 'normal', '2017-08-21 10:57:21.479', '2017-08-21 10:57:21.479');
INSERT INTO stop_points VALUES (57, 9, 98290, 'CITYWAY:ScheduledStopPoint:C00168-2-1-10-8:LOC', 1503305833129, NULL, 7, 'normal', 'normal', '2017-08-21 10:57:21.479', '2017-08-21 10:57:21.479');
INSERT INTO stop_points VALUES (58, 8, 98290, 'CITYWAY:ScheduledStopPoint:C00168-4-2-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.48', '2017-08-21 10:57:21.48');
INSERT INTO stop_points VALUES (59, 8, 77150, 'CITYWAY:ScheduledStopPoint:C00168-4-2-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:21.48', '2017-08-21 10:57:21.48');
INSERT INTO stop_points VALUES (60, 8, 77147, 'CITYWAY:ScheduledStopPoint:C00168-4-2-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:21.48', '2017-08-21 10:57:21.48');
INSERT INTO stop_points VALUES (61, 8, 98357, 'CITYWAY:ScheduledStopPoint:C00168-4-2-6-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.48', '2017-08-21 10:57:21.48');
INSERT INTO stop_points VALUES (62, 8, 132099, 'CITYWAY:ScheduledStopPoint:C00168-4-2-7-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.481', '2017-08-21 10:57:21.481');
INSERT INTO stop_points VALUES (63, 8, 93531, 'CITYWAY:ScheduledStopPoint:C00168-4-2-8-6:LOC', 1503305833129, NULL, 5, 'normal', 'normal', '2017-08-21 10:57:21.481', '2017-08-21 10:57:21.481');
INSERT INTO stop_points VALUES (64, 8, 85385, 'CITYWAY:ScheduledStopPoint:C00168-4-2-9-7:LOC', 1503305833129, NULL, 6, 'normal', 'normal', '2017-08-21 10:57:21.481', '2017-08-21 10:57:21.481');
INSERT INTO stop_points VALUES (65, 8, 131671, 'CITYWAY:ScheduledStopPoint:C00168-4-2-10-8:LOC', 1503305833129, NULL, 7, 'normal', 'normal', '2017-08-21 10:57:21.481', '2017-08-21 10:57:21.481');
INSERT INTO stop_points VALUES (66, 11, 93531, 'CITYWAY:ScheduledStopPoint:C00163-1-1-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.669', '2017-08-21 10:57:21.669');
INSERT INTO stop_points VALUES (67, 11, 77230, 'CITYWAY:ScheduledStopPoint:C00163-1-1-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:21.669', '2017-08-21 10:57:21.669');
INSERT INTO stop_points VALUES (68, 11, 77237, 'CITYWAY:ScheduledStopPoint:C00163-1-1-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:21.669', '2017-08-21 10:57:21.669');
INSERT INTO stop_points VALUES (69, 11, 77233, 'CITYWAY:ScheduledStopPoint:C00163-1-1-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.669', '2017-08-21 10:57:21.669');
INSERT INTO stop_points VALUES (70, 11, 77235, 'CITYWAY:ScheduledStopPoint:C00163-1-1-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.669', '2017-08-21 10:57:21.669');
INSERT INTO stop_points VALUES (71, 10, 77234, 'CITYWAY:ScheduledStopPoint:C00163-2-2-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.67', '2017-08-21 10:57:21.67');
INSERT INTO stop_points VALUES (72, 10, 77232, 'CITYWAY:ScheduledStopPoint:C00163-2-2-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:21.67', '2017-08-21 10:57:21.67');
INSERT INTO stop_points VALUES (73, 10, 77236, 'CITYWAY:ScheduledStopPoint:C00163-2-2-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:21.67', '2017-08-21 10:57:21.67');
INSERT INTO stop_points VALUES (74, 10, 77231, 'CITYWAY:ScheduledStopPoint:C00163-2-2-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.67', '2017-08-21 10:57:21.67');
INSERT INTO stop_points VALUES (75, 10, 93531, 'CITYWAY:ScheduledStopPoint:C00163-2-2-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.67', '2017-08-21 10:57:21.67');
INSERT INTO stop_points VALUES (76, 13, 93531, 'CITYWAY:ScheduledStopPoint:C00166-1-1-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.81', '2017-08-21 10:57:21.81');
INSERT INTO stop_points VALUES (77, 13, 77205, 'CITYWAY:ScheduledStopPoint:C00166-1-1-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:21.81', '2017-08-21 10:57:21.81');
INSERT INTO stop_points VALUES (78, 13, 77203, 'CITYWAY:ScheduledStopPoint:C00166-1-1-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:21.81', '2017-08-21 10:57:21.81');
INSERT INTO stop_points VALUES (79, 13, 131456, 'CITYWAY:ScheduledStopPoint:C00166-1-1-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.811', '2017-08-21 10:57:21.811');
INSERT INTO stop_points VALUES (80, 13, 77215, 'CITYWAY:ScheduledStopPoint:C00166-1-1-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.811', '2017-08-21 10:57:21.811');
INSERT INTO stop_points VALUES (81, 12, 77216, 'CITYWAY:ScheduledStopPoint:C00166-2-2-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.811', '2017-08-21 10:57:21.811');
INSERT INTO stop_points VALUES (82, 12, 131457, 'CITYWAY:ScheduledStopPoint:C00166-2-2-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:21.811', '2017-08-21 10:57:21.811');
INSERT INTO stop_points VALUES (83, 12, 77202, 'CITYWAY:ScheduledStopPoint:C00166-2-2-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:21.811', '2017-08-21 10:57:21.811');
INSERT INTO stop_points VALUES (84, 12, 77206, 'CITYWAY:ScheduledStopPoint:C00166-2-2-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.811', '2017-08-21 10:57:21.811');
INSERT INTO stop_points VALUES (85, 12, 93531, 'CITYWAY:ScheduledStopPoint:C00166-2-2-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.811', '2017-08-21 10:57:21.811');
INSERT INTO stop_points VALUES (86, 15, 93528, 'CITYWAY:ScheduledStopPoint:C00171-1-1-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966');
INSERT INTO stop_points VALUES (87, 15, 114274, 'CITYWAY:ScheduledStopPoint:C00171-1-1-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966');
INSERT INTO stop_points VALUES (88, 15, 114270, 'CITYWAY:ScheduledStopPoint:C00171-1-1-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966');
INSERT INTO stop_points VALUES (89, 15, 114263, 'CITYWAY:ScheduledStopPoint:C00171-1-1-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966');
INSERT INTO stop_points VALUES (90, 15, 77221, 'CITYWAY:ScheduledStopPoint:C00171-1-1-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966');
INSERT INTO stop_points VALUES (91, 15, 92299, 'CITYWAY:ScheduledStopPoint:C00171-1-1-6-6:LOC', 1503305833129, NULL, 5, 'normal', 'normal', '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966');
INSERT INTO stop_points VALUES (92, 15, 92295, 'CITYWAY:ScheduledStopPoint:C00171-1-1-7-7:LOC', 1503305833129, NULL, 6, 'normal', 'normal', '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966');
INSERT INTO stop_points VALUES (93, 14, 92294, 'CITYWAY:ScheduledStopPoint:C00171-2-2-1-1:LOC', 1503305833129, NULL, 0, 'normal', 'normal', '2017-08-21 10:57:21.967', '2017-08-21 10:57:21.967');
INSERT INTO stop_points VALUES (94, 14, 92300, 'CITYWAY:ScheduledStopPoint:C00171-2-2-2-2:LOC', 1503305833129, NULL, 1, 'normal', 'normal', '2017-08-21 10:57:21.967', '2017-08-21 10:57:21.967');
INSERT INTO stop_points VALUES (95, 14, 77222, 'CITYWAY:ScheduledStopPoint:C00171-2-2-3-3:LOC', 1503305833129, NULL, 2, 'normal', 'normal', '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966');
INSERT INTO stop_points VALUES (96, 14, 114262, 'CITYWAY:ScheduledStopPoint:C00171-2-2-4-4:LOC', 1503305833129, NULL, 3, 'normal', 'normal', '2017-08-21 10:57:21.967', '2017-08-21 10:57:21.967');
INSERT INTO stop_points VALUES (97, 14, 114271, 'CITYWAY:ScheduledStopPoint:C00171-2-2-5-5:LOC', 1503305833129, NULL, 4, 'normal', 'normal', '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966');
INSERT INTO stop_points VALUES (98, 14, 114275, 'CITYWAY:ScheduledStopPoint:C00171-2-2-6-6:LOC', 1503305833129, NULL, 5, 'normal', 'normal', '2017-08-21 10:57:21.966', '2017-08-21 10:57:21.966');
INSERT INTO stop_points VALUES (99, 14, 93528, 'CITYWAY:ScheduledStopPoint:C00171-2-2-7-7:LOC', 1503305833129, NULL, 6, 'normal', 'normal', '2017-08-21 10:57:21.967', '2017-08-21 10:57:21.967');


--
-- TOC entry 3795 (class 0 OID 0)
-- Dependencies: 246
-- Name: stop_points_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('stop_points_id_seq', 102, true);


--
-- TOC entry 3758 (class 0 OID 533309)
-- Dependencies: 247
-- Data for Name: time_table_dates; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO time_table_dates VALUES (5, '2017-07-05', 0, 2, true, NULL, NULL);
INSERT INTO time_table_dates VALUES (6, '2017-08-14', 0, 3, false, NULL, NULL);
INSERT INTO time_table_dates VALUES (6, '2017-08-15', 1, 4, false, NULL, NULL);
INSERT INTO time_table_dates VALUES (6, '2017-08-16', 2, 5, false, NULL, NULL);


--
-- TOC entry 3796 (class 0 OID 0)
-- Dependencies: 248
-- Name: time_table_dates_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('time_table_dates_id_seq', 5, true);


--
-- TOC entry 3760 (class 0 OID 533317)
-- Dependencies: 249
-- Data for Name: time_table_periods; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO time_table_periods VALUES (2, '2017-06-25', '2017-07-01', 0, 2, NULL, NULL);
INSERT INTO time_table_periods VALUES (2, '2017-09-01', '2017-12-31', 1, 3, NULL, NULL);
INSERT INTO time_table_periods VALUES (3, '2017-06-25', '2017-07-01', 0, 4, NULL, NULL);
INSERT INTO time_table_periods VALUES (3, '2017-09-01', '2017-12-31', 1, 5, NULL, NULL);
INSERT INTO time_table_periods VALUES (4, '2017-07-10', '2017-08-31', 0, 6, NULL, NULL);
INSERT INTO time_table_periods VALUES (6, '2017-07-10', '2017-08-31', 0, 7, NULL, NULL);


--
-- TOC entry 3797 (class 0 OID 0)
-- Dependencies: 250
-- Name: time_table_periods_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('time_table_periods_id_seq', 7, true);


--
-- TOC entry 3762 (class 0 OID 533325)
-- Dependencies: 251
-- Data for Name: time_tables; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO time_tables VALUES (2, 'CITYWAY:DayType:1:LOC', 1503305833129, NULL, NULL, 'Semaine', 124, NULL, NULL, NULL, NULL, NULL, '2017-08-21 10:57:20.596', '2017-08-21 10:57:20.596','CITYWAY');
INSERT INTO time_tables VALUES (3, 'CITYWAY:DayType:2:LOC', 1503305833129, NULL, NULL, 'Fin de semaine', 384, NULL, NULL, NULL, NULL, NULL, '2017-08-21 10:57:20.599', '2017-08-21 10:57:20.599','CITYWAY');
INSERT INTO time_tables VALUES (4, 'CITYWAY:DayType:3:LOC', 1503305833129, NULL, NULL, 'Semaine vacances', 124, NULL, NULL, NULL, NULL, NULL, '2017-08-21 10:57:20.6', '2017-08-21 10:57:20.6','CITYWAY');
INSERT INTO time_tables VALUES (5, 'CITYWAY:DayType:5:LOC', 1503305833129, NULL, NULL, 'Service spécial', 0, NULL, NULL, NULL, NULL, NULL, '2017-08-21 10:57:20.6', '2017-08-21 10:57:20.6','CITYWAY');
INSERT INTO time_tables VALUES (6, 'CITYWAY:DayType:3-without-6:LOC', 1503305833129, NULL, NULL, 'Semaine vacances', 124, NULL, NULL, NULL, NULL, NULL, '2017-08-21 10:57:21.816', '2017-08-21 10:57:21.816','CITYWAY');


--
-- TOC entry 3798 (class 0 OID 0)
-- Dependencies: 252
-- Name: time_tables_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('time_tables_id_seq', 102, true);


--
-- TOC entry 3764 (class 0 OID 533335)
-- Dependencies: 253
-- Data for Name: time_tables_vehicle_journeys; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO time_tables_vehicle_journeys VALUES (2, 2);
INSERT INTO time_tables_vehicle_journeys VALUES (3, 3);
INSERT INTO time_tables_vehicle_journeys VALUES (3, 4);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 4);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 5);
INSERT INTO time_tables_vehicle_journeys VALUES (3, 6);
INSERT INTO time_tables_vehicle_journeys VALUES (3, 7);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 7);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 8);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 9);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 10);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 11);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 12);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 13);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 14);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 15);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 16);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 17);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 18);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 19);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 20);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 21);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 22);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 23);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 24);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 25);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 26);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 27);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 28);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 29);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 30);
INSERT INTO time_tables_vehicle_journeys VALUES (3, 31);
INSERT INTO time_tables_vehicle_journeys VALUES (4, 32);
INSERT INTO time_tables_vehicle_journeys VALUES (5, 32);
INSERT INTO time_tables_vehicle_journeys VALUES (6, 33);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 34);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 35);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 36);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 37);
INSERT INTO time_tables_vehicle_journeys VALUES (2, 38);


--
-- TOC entry 3765 (class 0 OID 533338)
-- Dependencies: 254
-- Data for Name: timebands; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--



--
-- TOC entry 3799 (class 0 OID 0)
-- Dependencies: 255
-- Name: timebands_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('timebands_id_seq', 1, true);


--
-- TOC entry 3767 (class 0 OID 533346)
-- Dependencies: 256
-- Data for Name: vehicle_journey_at_stops; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO vehicle_journey_at_stops VALUES (2, 2, 2, NULL, NULL, '06:01:00', '06:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (3, 2, 3, NULL, NULL, '06:05:00', '06:05:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (4, 2, 4, NULL, NULL, '06:09:00', '06:09:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (5, 2, 5, NULL, NULL, '06:12:00', '06:12:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (6, 2, 6, NULL, NULL, '06:15:00', '06:15:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (7, 2, 7, NULL, NULL, '06:21:00', '06:22:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (8, 2, 8, NULL, NULL, '06:25:00', '06:25:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (9, 2, 9, NULL, NULL, '06:29:00', '06:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (10, 2, 10, NULL, NULL, '06:31:00', '06:32:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (11, 2, 11, NULL, NULL, '06:34:00', '06:35:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (12, 3, 2, NULL, NULL, '07:01:00', '07:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (13, 3, 3, NULL, NULL, '07:05:00', '07:05:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (14, 3, 4, NULL, NULL, '07:09:00', '07:09:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (15, 3, 5, NULL, NULL, '07:12:00', '07:12:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (16, 3, 6, NULL, NULL, '07:15:00', '07:15:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (17, 3, 7, NULL, NULL, '07:21:00', '07:22:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (18, 3, 8, NULL, NULL, '07:25:00', '07:25:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (19, 3, 9, NULL, NULL, '07:29:00', '07:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (20, 3, 10, NULL, NULL, '07:31:00', '07:32:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (21, 3, 11, NULL, NULL, '07:34:00', '07:35:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (22, 4, 2, NULL, NULL, '08:01:00', '08:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (23, 4, 3, NULL, NULL, '08:05:00', '08:05:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (24, 4, 4, NULL, NULL, '08:09:00', '08:09:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (25, 4, 5, NULL, NULL, '08:12:00', '08:12:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (26, 4, 6, NULL, NULL, '08:15:00', '08:15:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (27, 4, 7, NULL, NULL, '08:21:00', '08:22:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (28, 4, 8, NULL, NULL, '08:25:00', '08:25:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (29, 4, 9, NULL, NULL, '08:29:00', '08:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (30, 4, 10, NULL, NULL, '08:31:00', '08:32:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (31, 4, 11, NULL, NULL, '08:34:00', '08:35:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (32, 5, 12, NULL, NULL, '05:21:00', '05:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (33, 5, 13, NULL, NULL, '05:25:00', '05:25:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (34, 5, 14, NULL, NULL, '05:29:00', '05:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (35, 5, 15, NULL, NULL, '05:32:00', '05:32:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (36, 5, 16, NULL, NULL, '05:35:00', '05:35:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (37, 5, 17, NULL, NULL, '05:41:00', '05:42:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (38, 5, 18, NULL, NULL, '05:45:00', '05:45:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (39, 5, 19, NULL, NULL, '05:49:00', '05:49:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (40, 5, 20, NULL, NULL, '05:51:00', '05:52:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (41, 5, 21, NULL, NULL, '05:54:00', '05:55:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (42, 6, 12, NULL, NULL, '06:21:00', '06:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (43, 6, 13, NULL, NULL, '06:25:00', '06:25:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (44, 6, 14, NULL, NULL, '06:29:00', '06:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (45, 6, 15, NULL, NULL, '06:32:00', '06:32:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (46, 6, 16, NULL, NULL, '06:35:00', '06:35:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (47, 6, 17, NULL, NULL, '06:41:00', '06:42:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (48, 6, 18, NULL, NULL, '06:45:00', '06:45:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (49, 6, 19, NULL, NULL, '06:49:00', '06:49:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (50, 6, 20, NULL, NULL, '06:51:00', '06:52:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (51, 6, 21, NULL, NULL, '06:54:00', '06:55:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (52, 7, 12, NULL, NULL, '07:21:00', '07:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (53, 7, 13, NULL, NULL, '07:25:00', '07:25:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (54, 7, 14, NULL, NULL, '07:29:00', '07:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (55, 7, 15, NULL, NULL, '07:32:00', '07:32:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (56, 7, 16, NULL, NULL, '07:35:00', '07:35:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (57, 7, 17, NULL, NULL, '07:41:00', '07:42:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (58, 7, 18, NULL, NULL, '07:45:00', '07:45:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (59, 7, 19, NULL, NULL, '07:49:00', '07:49:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (60, 7, 20, NULL, NULL, '07:51:00', '07:52:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (61, 7, 21, NULL, NULL, '07:54:00', '07:55:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (62, 8, 22, NULL, NULL, '05:00:00', '05:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (63, 8, 23, NULL, NULL, '05:04:00', '05:04:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (64, 8, 24, NULL, NULL, '05:08:00', '05:09:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (65, 8, 25, NULL, NULL, '05:13:00', '05:13:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (66, 8, 26, NULL, NULL, '05:18:00', '05:18:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (67, 8, 27, NULL, NULL, '05:21:00', '05:22:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (68, 8, 28, NULL, NULL, '05:25:00', '05:26:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (69, 9, 22, NULL, NULL, '06:00:00', '06:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (70, 9, 23, NULL, NULL, '06:04:00', '06:04:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (71, 9, 24, NULL, NULL, '06:08:00', '06:09:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (72, 9, 25, NULL, NULL, '06:13:00', '06:13:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (73, 9, 26, NULL, NULL, '06:18:00', '06:18:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (74, 9, 27, NULL, NULL, '06:21:00', '06:22:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (75, 9, 28, NULL, NULL, '06:25:00', '06:26:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (76, 10, 22, NULL, NULL, '07:00:00', '07:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (77, 10, 23, NULL, NULL, '07:04:00', '07:04:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (78, 10, 24, NULL, NULL, '07:08:00', '07:09:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (79, 10, 25, NULL, NULL, '07:13:00', '07:13:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (80, 10, 26, NULL, NULL, '07:18:00', '07:18:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (81, 10, 27, NULL, NULL, '07:21:00', '07:22:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (82, 10, 28, NULL, NULL, '07:25:00', '07:26:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (83, 11, 22, NULL, NULL, '05:30:00', '05:31:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (84, 11, 25, NULL, NULL, '05:37:00', '05:37:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (85, 11, 27, NULL, NULL, '05:40:00', '05:41:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (86, 11, 28, NULL, NULL, '05:44:00', '05:45:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (87, 12, 22, NULL, NULL, '06:30:00', '06:31:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (88, 12, 25, NULL, NULL, '06:37:00', '06:37:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (89, 12, 27, NULL, NULL, '06:40:00', '06:41:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (90, 12, 28, NULL, NULL, '06:44:00', '06:45:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (91, 13, 22, NULL, NULL, '07:30:00', '07:31:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (92, 13, 25, NULL, NULL, '07:37:00', '07:37:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (93, 13, 27, NULL, NULL, '07:40:00', '07:41:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (94, 13, 28, NULL, NULL, '07:44:00', '07:45:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (95, 14, 29, NULL, NULL, '05:20:00', '05:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (96, 14, 30, NULL, NULL, '05:24:00', '05:24:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (97, 14, 31, NULL, NULL, '05:28:00', '05:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (98, 14, 32, NULL, NULL, '05:33:00', '05:33:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (99, 14, 33, NULL, NULL, '05:38:00', '05:38:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (100, 14, 34, NULL, NULL, '05:41:00', '05:42:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (101, 14, 35, NULL, NULL, '05:45:00', '05:46:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (102, 15, 29, NULL, NULL, '06:20:00', '06:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (103, 15, 30, NULL, NULL, '06:24:00', '06:24:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (104, 15, 31, NULL, NULL, '06:28:00', '06:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (105, 15, 32, NULL, NULL, '06:33:00', '06:33:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (106, 15, 33, NULL, NULL, '06:38:00', '06:38:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (107, 15, 34, NULL, NULL, '06:41:00', '06:42:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (108, 15, 35, NULL, NULL, '06:45:00', '06:46:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (109, 16, 29, NULL, NULL, '07:20:00', '07:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (110, 16, 30, NULL, NULL, '07:24:00', '07:24:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (111, 16, 31, NULL, NULL, '07:28:00', '07:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (112, 16, 32, NULL, NULL, '07:33:00', '07:33:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (113, 16, 33, NULL, NULL, '07:38:00', '07:38:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (114, 16, 34, NULL, NULL, '07:41:00', '07:42:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (115, 16, 35, NULL, NULL, '07:45:00', '07:46:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (116, 17, 29, NULL, NULL, '05:40:00', '05:41:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (117, 17, 30, NULL, NULL, '05:47:00', '05:47:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (118, 17, 32, NULL, NULL, '05:50:00', '05:51:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (119, 17, 35, NULL, NULL, '05:54:00', '05:55:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (120, 18, 29, NULL, NULL, '06:40:00', '06:41:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (121, 18, 30, NULL, NULL, '06:47:00', '06:47:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (122, 18, 32, NULL, NULL, '06:50:00', '06:51:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (123, 18, 35, NULL, NULL, '06:54:00', '06:55:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (124, 19, 29, NULL, NULL, '07:40:00', '07:41:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (125, 19, 30, NULL, NULL, '07:47:00', '07:47:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (126, 19, 32, NULL, NULL, '07:50:00', '07:51:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (127, 19, 35, NULL, NULL, '07:54:00', '07:55:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (128, 20, 36, NULL, NULL, '06:01:00', '06:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (129, 20, 37, NULL, NULL, '06:04:00', '06:04:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (130, 20, 38, NULL, NULL, '06:06:00', '06:06:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (131, 20, 39, NULL, NULL, '06:08:00', '06:08:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (132, 20, 40, NULL, NULL, '06:18:00', '06:18:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (133, 20, 41, NULL, NULL, '06:21:00', '06:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (134, 20, 42, NULL, NULL, '06:24:00', '06:24:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (135, 21, 43, NULL, NULL, '09:01:00', '09:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (136, 21, 44, NULL, NULL, '09:04:00', '09:04:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (137, 21, 45, NULL, NULL, '09:06:00', '09:06:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (138, 21, 46, NULL, NULL, '09:16:00', '09:16:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (139, 21, 47, NULL, NULL, '09:18:00', '09:18:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (140, 21, 48, NULL, NULL, '09:21:00', '09:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (141, 21, 49, NULL, NULL, '09:24:00', '09:24:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (142, 22, 50, NULL, NULL, '07:01:00', '07:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (143, 22, 51, NULL, NULL, '07:04:00', '07:04:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (144, 22, 52, NULL, NULL, '07:07:00', '07:07:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (145, 22, 53, NULL, NULL, '07:08:00', '07:08:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (146, 22, 54, NULL, NULL, '07:18:00', '07:18:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (147, 22, 55, NULL, NULL, '07:21:00', '07:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (148, 22, 56, NULL, NULL, '07:23:00', '07:23:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (149, 22, 57, NULL, NULL, '07:29:00', '07:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (150, 23, 58, NULL, NULL, '10:01:00', '10:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (151, 23, 59, NULL, NULL, '10:04:00', '10:04:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (152, 23, 60, NULL, NULL, '10:07:00', '10:07:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (153, 23, 61, NULL, NULL, '10:08:00', '10:08:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (154, 23, 62, NULL, NULL, '10:18:00', '10:18:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (155, 23, 63, NULL, NULL, '10:21:00', '10:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (156, 23, 64, NULL, NULL, '10:23:00', '10:23:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (157, 23, 65, NULL, NULL, '10:29:00', '10:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (158, 24, 66, NULL, NULL, '05:01:00', '05:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (159, 24, 67, NULL, NULL, '05:09:00', '05:09:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (160, 24, 68, NULL, NULL, '05:11:00', '05:11:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (161, 24, 69, NULL, NULL, '05:13:00', '05:13:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (162, 24, 70, NULL, NULL, '05:15:00', '05:15:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (163, 25, 66, NULL, NULL, '06:01:00', '06:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (164, 25, 67, NULL, NULL, '06:09:00', '06:09:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (165, 25, 68, NULL, NULL, '06:11:00', '06:11:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (166, 25, 69, NULL, NULL, '06:13:00', '06:13:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (167, 25, 70, NULL, NULL, '06:15:00', '06:15:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (168, 26, 66, NULL, NULL, '06:31:00', '06:31:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (169, 26, 67, NULL, NULL, '06:39:00', '06:39:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (170, 26, 68, NULL, NULL, '06:41:00', '06:41:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (171, 26, 69, NULL, NULL, '06:43:00', '06:43:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (172, 26, 70, NULL, NULL, '06:45:00', '06:45:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (173, 27, 71, NULL, NULL, '05:21:00', '05:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (174, 27, 72, NULL, NULL, '05:29:00', '05:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (175, 27, 73, NULL, NULL, '05:31:00', '05:31:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (176, 27, 74, NULL, NULL, '05:33:00', '05:33:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (177, 27, 75, NULL, NULL, '05:35:00', '05:35:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (178, 28, 71, NULL, NULL, '06:21:00', '06:21:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (179, 28, 72, NULL, NULL, '06:29:00', '06:29:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (180, 28, 73, NULL, NULL, '06:31:00', '06:31:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (181, 28, 74, NULL, NULL, '06:33:00', '06:33:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (182, 28, 75, NULL, NULL, '06:35:00', '06:35:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (183, 29, 71, NULL, NULL, '07:51:00', '07:51:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (184, 29, 72, NULL, NULL, '07:59:00', '07:59:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (185, 29, 73, NULL, NULL, '08:01:00', '08:01:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (186, 29, 74, NULL, NULL, '08:03:00', '08:03:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (187, 29, 75, NULL, NULL, '08:05:00', '08:05:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (188, 30, 76, NULL, NULL, '08:01:00', '08:02:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (189, 30, 77, NULL, NULL, '08:10:00', '08:11:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (190, 30, 78, NULL, NULL, '08:15:00', '08:16:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (191, 30, 79, NULL, NULL, '08:18:00', '08:19:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (192, 30, 80, NULL, NULL, '08:21:00', '08:22:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (193, 31, 76, NULL, NULL, '09:01:00', '09:02:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (194, 31, 77, NULL, NULL, '09:10:00', '09:11:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (195, 31, 78, NULL, NULL, '09:15:00', '09:16:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (196, 31, 79, NULL, NULL, '09:18:00', '09:19:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (197, 31, 80, NULL, NULL, '09:21:00', '09:22:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (198, 32, 76, NULL, NULL, '10:01:00', '10:02:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (199, 32, 77, NULL, NULL, '10:10:00', '10:11:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (200, 32, 78, NULL, NULL, '10:15:00', '10:16:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (201, 32, 79, NULL, NULL, '10:18:00', '10:19:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (202, 32, 80, NULL, NULL, '10:21:00', '10:22:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (203, 33, 76, NULL, NULL, '11:01:00', '11:02:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (204, 33, 77, NULL, NULL, '11:10:00', '11:11:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (205, 33, 78, NULL, NULL, '11:15:00', '11:16:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (206, 33, 79, NULL, NULL, '11:18:00', '11:19:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (207, 33, 80, NULL, NULL, '11:21:00', '11:22:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (208, 34, 81, NULL, NULL, '13:01:00', '13:02:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (209, 34, 82, NULL, NULL, '13:10:00', '13:11:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (210, 34, 83, NULL, NULL, '13:15:00', '13:16:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (211, 34, 84, NULL, NULL, '13:18:00', '13:19:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (212, 34, 85, NULL, NULL, '13:21:00', '13:22:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (213, 35, 86, NULL, NULL, '10:58:00', '10:59:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (214, 35, 87, NULL, NULL, '11:03:00', '11:04:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (215, 35, 88, NULL, NULL, '11:10:00', '11:11:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (216, 35, 90, NULL, NULL, '11:17:00', '11:18:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (217, 36, 86, NULL, NULL, '14:58:00', '14:59:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (218, 36, 88, NULL, NULL, '15:03:00', '15:04:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (219, 36, 89, NULL, NULL, '15:10:00', '15:11:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (220, 36, 91, NULL, NULL, '15:17:00', '15:18:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (221, 36, 92, NULL, NULL, '15:22:00', '15:23:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (222, 37, 95, NULL, NULL, '20:58:00', '20:59:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (223, 37, 97, NULL, NULL, '21:03:00', '21:04:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (224, 37, 98, NULL, NULL, '21:10:00', '21:11:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (225, 37, 99, NULL, NULL, '21:17:00', '21:18:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (226, 38, 93, NULL, NULL, '23:58:00', '23:59:00', NULL, NULL, 0, 0, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (227, 38, 94, NULL, NULL, '00:03:00', '00:04:00', NULL, NULL, 1, 1, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (228, 38, 96, NULL, NULL, '00:10:00', '00:11:00', NULL, NULL, 1, 1, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (229, 38, 97, NULL, NULL, '00:13:00', '00:14:00', NULL, NULL, 1, 1, NULL, NULL);
INSERT INTO vehicle_journey_at_stops VALUES (230, 38, 99, NULL, NULL, '00:17:00', '00:18:00', NULL, NULL, 1, 1, NULL, NULL);


--
-- TOC entry 3800 (class 0 OID 0)
-- Dependencies: 257
-- Name: vehicle_journey_at_stops_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('vehicle_journey_at_stops_id_seq', 304, true);


--
-- TOC entry 3769 (class 0 OID 533356)
-- Dependencies: 258
-- Data for Name: vehicle_journeys; Type: TABLE DATA; Schema: iev_export; Owner: chouette
--

INSERT INTO vehicle_journeys VALUES (2, 3, 2, NULL, 'CITYWAY:ServiceJourney:C00165-1-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'C1 bullion semaine', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:20.855', '2017-08-21 10:57:20.855','CITYWAY');
INSERT INTO vehicle_journeys VALUES (3, 3, 2, NULL, 'CITYWAY:ServiceJourney:C00165-1-2:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'C bullion WE', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:20.86', '2017-08-21 10:57:20.86','CITYWAY');
INSERT INTO vehicle_journeys VALUES (4, 3, 2, NULL, 'CITYWAY:ServiceJourney:C00165-1-3:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'C bullion WE', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:20.862', '2017-08-21 10:57:20.862','CITYWAY');
INSERT INTO vehicle_journeys VALUES (5, 2, 3, NULL, 'CITYWAY:ServiceJourney:C00165-2-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'C rbt semaine', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:20.864', '2017-08-21 10:57:20.864','CITYWAY');
INSERT INTO vehicle_journeys VALUES (6, 2, 3, NULL, 'CITYWAY:ServiceJourney:C00165-2-2:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'C rbt we', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:20.865', '2017-08-21 10:57:20.865','CITYWAY');
INSERT INTO vehicle_journeys VALUES (7, 2, 3, NULL, 'CITYWAY:ServiceJourney:C00165-2-3:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'C rbt semaine et we', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:20.868', '2017-08-21 10:57:20.868','CITYWAY');
INSERT INTO vehicle_journeys VALUES (8, 5, 4, NULL, 'CITYWAY:ServiceJourney:C00164-1-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1  omni vers pontevrard', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.19', '2017-08-21 10:57:21.19','CITYWAY');
INSERT INTO vehicle_journeys VALUES (9, 5, 4, NULL, 'CITYWAY:ServiceJourney:C00164-1-2:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 2  omni vers pontevrard', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.191', '2017-08-21 10:57:21.191','CITYWAY');
INSERT INTO vehicle_journeys VALUES (10, 5, 4, NULL, 'CITYWAY:ServiceJourney:C00164-1-3:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 3  omni vers pontevrard', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.192', '2017-08-21 10:57:21.192','CITYWAY');
INSERT INTO vehicle_journeys VALUES (11, 5, 5, NULL, 'CITYWAY:ServiceJourney:C00164-1-4:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 4  sd vers pontevrard', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.194', '2017-08-21 10:57:21.194','CITYWAY');
INSERT INTO vehicle_journeys VALUES (12, 5, 5, NULL, 'CITYWAY:ServiceJourney:C00164-1-5:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 5  sd vers pontevrard', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.194', '2017-08-21 10:57:21.194','CITYWAY');
INSERT INTO vehicle_journeys VALUES (13, 5, 5, NULL, 'CITYWAY:ServiceJourney:C00164-1-6:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 6  sd vers pontevrard', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.195', '2017-08-21 10:57:21.195','CITYWAY');
INSERT INTO vehicle_journeys VALUES (14, 4, 6, NULL, 'CITYWAY:ServiceJourney:C00164-2-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1  omni vers rambouillet', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.196', '2017-08-21 10:57:21.196','CITYWAY');
INSERT INTO vehicle_journeys VALUES (15, 4, 6, NULL, 'CITYWAY:ServiceJourney:C00164-2-2:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 2  omni vers rambouillet', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.197', '2017-08-21 10:57:21.197','CITYWAY');
INSERT INTO vehicle_journeys VALUES (16, 4, 6, NULL, 'CITYWAY:ServiceJourney:C00164-2-3:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 3  omni vers rambouillet', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.198', '2017-08-21 10:57:21.198','CITYWAY');
INSERT INTO vehicle_journeys VALUES (17, 4, 7, NULL, 'CITYWAY:ServiceJourney:C00164-2-4:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 4  sd vers rambouillet', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.2', '2017-08-21 10:57:21.199','CITYWAY');
INSERT INTO vehicle_journeys VALUES (18, 4, 7, NULL, 'CITYWAY:ServiceJourney:C00164-2-5:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 5  sd vers rambouillet', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.2', '2017-08-21 10:57:21.2','CITYWAY');
INSERT INTO vehicle_journeys VALUES (19, 4, 7, NULL, 'CITYWAY:ServiceJourney:C00164-2-6:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 6  sd vers rambouillet', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.201', '2017-08-21 10:57:21.201','CITYWAY');
INSERT INTO vehicle_journeys VALUES (20, 7, 8, NULL, 'CITYWAY:ServiceJourney:C00168-1-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1 direction auffargis', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.483', '2017-08-21 10:57:21.483','CITYWAY');
INSERT INTO vehicle_journeys VALUES (21, 6, 9, NULL, 'CITYWAY:ServiceJourney:C00168-3-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1 direction rambouillet', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.487', '2017-08-21 10:57:21.487','CITYWAY');
INSERT INTO vehicle_journeys VALUES (22, 9, 10, NULL, 'CITYWAY:ServiceJourney:C00168-2-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1 directionles essarts', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.485', '2017-08-21 10:57:21.485','CITYWAY');
INSERT INTO vehicle_journeys VALUES (23, 8, 11, NULL, 'CITYWAY:ServiceJourney:C00168-4-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1 les essarts rambouillet', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.489', '2017-08-21 10:57:21.489','CITYWAY');
INSERT INTO vehicle_journeys VALUES (24, 11, 12, NULL, 'CITYWAY:ServiceJourney:C00163-1-01:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 01 rambouillet vers poigny', '01101', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.671', '2017-08-21 10:57:21.671','CITYWAY');
INSERT INTO vehicle_journeys VALUES (25, 11, 12, NULL, 'CITYWAY:ServiceJourney:C00163-1-02:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 02 rambouillet vers poigny', '01101', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.672', '2017-08-21 10:57:21.672','CITYWAY');
INSERT INTO vehicle_journeys VALUES (26, 11, 12, NULL, 'CITYWAY:ServiceJourney:C00163-1-03:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 03 rambouillet vers poigny', '01101', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.673', '2017-08-21 10:57:21.673','CITYWAY');
INSERT INTO vehicle_journeys VALUES (27, 10, 13, NULL, 'CITYWAY:ServiceJourney:C00163-2-01:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 01 poigny vers rambouillet', '01101', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.674', '2017-08-21 10:57:21.674','CITYWAY');
INSERT INTO vehicle_journeys VALUES (28, 10, 13, NULL, 'CITYWAY:ServiceJourney:C00163-2-02:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 02 poigny vers rambouillet', '01101', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.674', '2017-08-21 10:57:21.674','CITYWAY');
INSERT INTO vehicle_journeys VALUES (29, 10, 13, NULL, 'CITYWAY:ServiceJourney:C00163-2-03:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 03 poigny vers rambouillet', '01101', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.675', '2017-08-21 10:57:21.675','CITYWAY');
INSERT INTO vehicle_journeys VALUES (30, 13, 14, NULL, 'CITYWAY:ServiceJourney:C00166-1-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1 s note 1 ', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.812', '2017-08-21 10:57:21.812','CITYWAY');
INSERT INTO vehicle_journeys VALUES (31, 13, 14, NULL, 'CITYWAY:ServiceJourney:C00166-1-2:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1 we not 2 ', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.813', '2017-08-21 10:57:21.813','CITYWAY');
INSERT INTO vehicle_journeys VALUES (32, 13, 14, NULL, 'CITYWAY:ServiceJourney:C00166-1-3:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1 exceptionnel note 1 et 2 ', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.814', '2017-08-21 10:57:21.814','CITYWAY');
INSERT INTO vehicle_journeys VALUES (33, 13, 14, NULL, 'CITYWAY:ServiceJourney:C00166-1-4:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1 semaine en vacances et exclusions  note 3 ', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.815', '2017-08-21 10:57:21.815','CITYWAY');
INSERT INTO vehicle_journeys VALUES (34, 12, 15, NULL, 'CITYWAY:ServiceJourney:C00166-2-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1 semaine  note 3 ', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.816', '2017-08-21 10:57:21.816','CITYWAY');
INSERT INTO vehicle_journeys VALUES (35, 15, 16, NULL, 'CITYWAY:ServiceJourney:C00171-1-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1 direction orsonville', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.968', '2017-08-21 10:57:21.968','CITYWAY');
INSERT INTO vehicle_journeys VALUES (36, 15, 17, NULL, 'CITYWAY:ServiceJourney:C00171-2-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'Course 1 direction Auneau sncf', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.968', '2017-08-21 10:57:21.968','CITYWAY');
INSERT INTO vehicle_journeys VALUES (37, 14, 18, NULL, 'CITYWAY:ServiceJourney:C00171-3-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'direction rambouillet retour', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.969', '2017-08-21 10:57:21.969','CITYWAY');
INSERT INTO vehicle_journeys VALUES (38, 14, 19, NULL, 'CITYWAY:ServiceJourney:C00171-4-1:LOC', 1503305833129, NULL, NULL, NULL, NULL, 'de auneau sncf à rambouillet ', '1234', NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, '2017-08-21 10:57:21.97', '2017-08-21 10:57:21.97','CITYWAY');


--
-- TOC entry 3801 (class 0 OID 0)
-- Dependencies: 259
-- Name: vehicle_journeys_id_seq; Type: SEQUENCE SET; Schema: iev_export; Owner: chouette
--

SELECT pg_catalog.setval('vehicle_journeys_id_seq', 102, true);


--
-- TOC entry 3566 (class 2606 OID 533379)
-- Name: footnotes_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY footnotes
    ADD CONSTRAINT footnotes_pkey PRIMARY KEY (id);


--
-- TOC entry 3570 (class 2606 OID 533381)
-- Name: journey_frequencies_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT journey_frequencies_pkey PRIMARY KEY (id);



--
-- TOC entry 3578 (class 2606 OID 533385)
-- Name: journey_patterns_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT journey_patterns_pkey PRIMARY KEY (id);


--
-- TOC entry 3582 (class 2606 OID 533387)
-- Name: routes_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY routes
    ADD CONSTRAINT routes_pkey PRIMARY KEY (id);


--
-- TOC entry 3584 (class 2606 OID 533389)
-- Name: routing_constraint_zones_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY routing_constraint_zones
    ADD CONSTRAINT routing_constraint_zones_pkey PRIMARY KEY (id);


--
-- TOC entry 3587 (class 2606 OID 533391)
-- Name: stop_points_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY stop_points
    ADD CONSTRAINT stop_points_pkey PRIMARY KEY (id);


--
-- TOC entry 3590 (class 2606 OID 533393)
-- Name: time_table_dates_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY time_table_dates
    ADD CONSTRAINT time_table_dates_pkey PRIMARY KEY (id);


--
-- TOC entry 3593 (class 2606 OID 533395)
-- Name: time_table_periods_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY time_table_periods
    ADD CONSTRAINT time_table_periods_pkey PRIMARY KEY (id);


--
-- TOC entry 3596 (class 2606 OID 533397)
-- Name: time_tables_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY time_tables
    ADD CONSTRAINT time_tables_pkey PRIMARY KEY (id);


--
-- TOC entry 3600 (class 2606 OID 533399)
-- Name: timebands_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY timebands
    ADD CONSTRAINT timebands_pkey PRIMARY KEY (id);


--
-- TOC entry 3604 (class 2606 OID 533401)
-- Name: vehicle_journey_at_stops_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vehicle_journey_at_stops_pkey PRIMARY KEY (id);


--
-- TOC entry 3608 (class 2606 OID 533403)
-- Name: vehicle_journeys_pkey; Type: CONSTRAINT; Schema: iev_export; Owner: chouette; Tablespace: 
--

ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vehicle_journeys_pkey PRIMARY KEY (id);


--
-- TOC entry 3567 (class 1259 OID 533404)
-- Name: index_journey_frequencies_on_timeband_id; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE INDEX index_journey_frequencies_on_timeband_id ON journey_frequencies USING btree (timeband_id);


--
-- TOC entry 3568 (class 1259 OID 533405)
-- Name: index_journey_frequencies_on_vehicle_journey_id; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE INDEX index_journey_frequencies_on_vehicle_journey_id ON journey_frequencies USING btree (vehicle_journey_id);


--
-- TOC entry 3579 (class 1259 OID 533406)
-- Name: index_journey_pattern_id_on_journey_patterns_stop_points; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE INDEX index_journey_pattern_id_on_journey_patterns_stop_points ON journey_patterns_stop_points USING btree (journey_pattern_id);


--
-- TOC entry 3588 (class 1259 OID 533410)
-- Name: index_time_table_dates_on_time_table_id; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE INDEX index_time_table_dates_on_time_table_id ON time_table_dates USING btree (time_table_id);


--
-- TOC entry 3591 (class 1259 OID 533411)
-- Name: index_time_table_periods_on_time_table_id; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE INDEX index_time_table_periods_on_time_table_id ON time_table_periods USING btree (time_table_id);


--
-- TOC entry 3597 (class 1259 OID 533412)
-- Name: index_time_tables_vehicle_journeys_on_time_table_id; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE INDEX index_time_tables_vehicle_journeys_on_time_table_id ON time_tables_vehicle_journeys USING btree (time_table_id);


--
-- TOC entry 3598 (class 1259 OID 533413)
-- Name: index_time_tables_vehicle_journeys_on_vehicle_journey_id; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE INDEX index_time_tables_vehicle_journeys_on_vehicle_journey_id ON time_tables_vehicle_journeys USING btree (vehicle_journey_id);


--
-- TOC entry 3601 (class 1259 OID 533414)
-- Name: index_vehicle_journey_at_stops_on_stop_pointid; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE INDEX index_vehicle_journey_at_stops_on_stop_pointid ON vehicle_journey_at_stops USING btree (stop_point_id);


--
-- TOC entry 3602 (class 1259 OID 533415)
-- Name: index_vehicle_journey_at_stops_on_vehicle_journey_id; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE INDEX index_vehicle_journey_at_stops_on_vehicle_journey_id ON vehicle_journey_at_stops USING btree (vehicle_journey_id);


--
-- TOC entry 3605 (class 1259 OID 533416)
-- Name: index_vehicle_journeys_on_route_id; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE INDEX index_vehicle_journeys_on_route_id ON vehicle_journeys USING btree (route_id);


--
-- TOC entry 3576 (class 1259 OID 533417)
-- Name: journey_patterns_objectid_key; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE UNIQUE INDEX journey_patterns_objectid_key ON journey_patterns USING btree (objectid);


--
-- TOC entry 3580 (class 1259 OID 533418)
-- Name: routes_objectid_key; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE UNIQUE INDEX routes_objectid_key ON routes USING btree (objectid);


--
-- TOC entry 3585 (class 1259 OID 533419)
-- Name: stop_points_objectid_key; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE UNIQUE INDEX stop_points_objectid_key ON stop_points USING btree (objectid);


--
-- TOC entry 3594 (class 1259 OID 533420)
-- Name: time_tables_objectid_key; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE UNIQUE INDEX time_tables_objectid_key ON time_tables USING btree (objectid);


--
-- TOC entry 3606 (class 1259 OID 533421)
-- Name: vehicle_journeys_objectid_key; Type: INDEX; Schema: iev_export; Owner: chouette; Tablespace: 
--

CREATE UNIQUE INDEX vehicle_journeys_objectid_key ON vehicle_journeys USING btree (objectid);


--
-- TOC entry 3612 (class 2606 OID 533432)
-- Name: arrival_point_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT arrival_point_fkey FOREIGN KEY (arrival_stop_point_id) REFERENCES stop_points(id) ON DELETE SET NULL;


--
-- TOC entry 3613 (class 2606 OID 533437)
-- Name: departure_point_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT departure_point_fkey FOREIGN KEY (departure_stop_point_id) REFERENCES stop_points(id) ON DELETE SET NULL;


--
-- TOC entry 3609 (class 2606 OID 533422)
-- Name: journey_frequencies_timeband_id_fk; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT journey_frequencies_timeband_id_fk FOREIGN KEY (timeband_id) REFERENCES timebands(id) ON DELETE SET NULL;


--
-- TOC entry 3610 (class 2606 OID 533427)
-- Name: journey_frequencies_vehicle_journey_id_fk; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT journey_frequencies_vehicle_journey_id_fk FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE SET NULL;



--
-- TOC entry 3614 (class 2606 OID 533447)
-- Name: jp_route_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT jp_route_fkey FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE;


--
-- TOC entry 3615 (class 2606 OID 533452)
-- Name: jpsp_jp_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY journey_patterns_stop_points
    ADD CONSTRAINT jpsp_jp_fkey FOREIGN KEY (journey_pattern_id) REFERENCES journey_patterns(id) ON DELETE CASCADE;


--
-- TOC entry 3616 (class 2606 OID 533457)
-- Name: jpsp_stoppoint_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY journey_patterns_stop_points
    ADD CONSTRAINT jpsp_stoppoint_fkey FOREIGN KEY (stop_point_id) REFERENCES stop_points(id) ON DELETE CASCADE;


--
-- TOC entry 3617 (class 2606 OID 533462)
-- Name: route_opposite_route_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY routes
    ADD CONSTRAINT route_opposite_route_fkey FOREIGN KEY (opposite_route_id) REFERENCES routes(id) ON DELETE SET NULL;


--
-- TOC entry 3618 (class 2606 OID 533467)
-- Name: tm_date_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY time_table_dates
    ADD CONSTRAINT tm_date_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;


--
-- TOC entry 3619 (class 2606 OID 533472)
-- Name: tm_period_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY time_table_periods
    ADD CONSTRAINT tm_period_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;


--
-- TOC entry 3624 (class 2606 OID 533477)
-- Name: vj_jp_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vj_jp_fkey FOREIGN KEY (journey_pattern_id) REFERENCES journey_patterns(id) ON DELETE CASCADE;


--
-- TOC entry 3625 (class 2606 OID 533482)
-- Name: vj_route_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vj_route_fkey FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE;


--
-- TOC entry 3622 (class 2606 OID 533487)
-- Name: vjas_sp_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vjas_sp_fkey FOREIGN KEY (stop_point_id) REFERENCES stop_points(id) ON DELETE CASCADE;


--
-- TOC entry 3623 (class 2606 OID 533492)
-- Name: vjas_vj_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vjas_vj_fkey FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE CASCADE;


--
-- TOC entry 3620 (class 2606 OID 533497)
-- Name: vjtm_tm_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY time_tables_vehicle_journeys
    ADD CONSTRAINT vjtm_tm_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;


--
-- TOC entry 3621 (class 2606 OID 533502)
-- Name: vjtm_vj_fkey; Type: FK CONSTRAINT; Schema: iev_export; Owner: chouette
--

ALTER TABLE ONLY time_tables_vehicle_journeys
    ADD CONSTRAINT vjtm_vj_fkey FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE CASCADE;


--
-- TOC entry 3775 (class 0 OID 0)
-- Dependencies: 11
-- Name: iev_export; Type: ACL; Schema: -; Owner: chouette
--

REVOKE ALL ON SCHEMA iev_export FROM PUBLIC;
REVOKE ALL ON SCHEMA iev_export FROM chouette;
GRANT ALL ON SCHEMA iev_export TO chouette;
GRANT ALL ON SCHEMA iev_export TO PUBLIC;


-- Completed on 2017-08-21 11:11:39 CEST

--
-- PostgreSQL database dump complete
--

