--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.11
-- Dumped by pg_dump version 9.5.7

  
DROP SCHEMA IF EXISTS chouette_gui CASCADE;

CREATE SCHEMA chouette_gui ;

SET search_path = chouette_gui, pg_catalog;

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = chouette_gui, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: footnotes; Type: TABLE; Schema: chouette_gui; Owner: chouette
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


ALTER TABLE footnotes OWNER TO chouette;

--
-- Name: footnotes_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE footnotes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE footnotes_id_seq OWNER TO chouette;

--
-- Name: footnotes_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE footnotes_id_seq OWNED BY footnotes.id;


--
-- Name: footnotes_vehicle_journeys; Type: TABLE; Schema: chouette_gui; Owner: chouette
--

CREATE TABLE footnotes_vehicle_journeys (
    vehicle_journey_id bigint,
    footnote_id bigint
);


ALTER TABLE footnotes_vehicle_journeys OWNER TO chouette;

--
-- Name: journey_frequencies; Type: TABLE; Schema: chouette_gui; Owner: chouette
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


ALTER TABLE journey_frequencies OWNER TO chouette;

--
-- Name: journey_frequencies_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE journey_frequencies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE journey_frequencies_id_seq OWNER TO chouette;

--
-- Name: journey_frequencies_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE journey_frequencies_id_seq OWNED BY journey_frequencies.id;


--
-- Name: journey_pattern_sections; Type: TABLE; Schema: chouette_gui; Owner: chouette
--

CREATE TABLE journey_pattern_sections (
    id bigint NOT NULL,
    journey_pattern_id bigint NOT NULL,
    route_section_id bigint NOT NULL,
    rank integer NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE journey_pattern_sections OWNER TO chouette;

--
-- Name: journey_pattern_sections_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE journey_pattern_sections_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE journey_pattern_sections_id_seq OWNER TO chouette;

--
-- Name: journey_pattern_sections_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE journey_pattern_sections_id_seq OWNED BY journey_pattern_sections.id;


--
-- Name: journey_patterns; Type: TABLE; Schema: chouette_gui; Owner: chouette
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
    updated_at timestamp without time zone
);


ALTER TABLE journey_patterns OWNER TO chouette;

--
-- Name: journey_patterns_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE journey_patterns_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE journey_patterns_id_seq OWNER TO chouette;

--
-- Name: journey_patterns_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE journey_patterns_id_seq OWNED BY journey_patterns.id;


--
-- Name: journey_patterns_stop_points; Type: TABLE; Schema: chouette_gui; Owner: chouette
--

CREATE TABLE journey_patterns_stop_points (
    journey_pattern_id bigint,
    stop_point_id bigint
);


ALTER TABLE journey_patterns_stop_points OWNER TO chouette;

--
-- Name: routes; Type: TABLE; Schema: chouette_gui; Owner: chouette
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
    updated_at timestamp without time zone
);


ALTER TABLE routes OWNER TO chouette;

--
-- Name: routes_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE routes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE routes_id_seq OWNER TO chouette;

--
-- Name: routes_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE routes_id_seq OWNED BY routes.id;


--
-- Name: routing_constraint_zones; Type: TABLE; Schema: chouette_gui; Owner: chouette
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
    updated_at timestamp without time zone
);


ALTER TABLE routing_constraint_zones OWNER TO chouette;

--
-- Name: routing_constraint_zones_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE routing_constraint_zones_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE routing_constraint_zones_id_seq OWNER TO chouette;

--
-- Name: routing_constraint_zones_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE routing_constraint_zones_id_seq OWNED BY routing_constraint_zones.id;


--
-- Name: stop_points; Type: TABLE; Schema: chouette_gui; Owner: chouette
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


ALTER TABLE stop_points OWNER TO chouette;

--
-- Name: stop_points_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE stop_points_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stop_points_id_seq OWNER TO chouette;

--
-- Name: stop_points_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE stop_points_id_seq OWNED BY stop_points.id;


--
-- Name: time_table_dates; Type: TABLE; Schema: chouette_gui; Owner: chouette
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


ALTER TABLE time_table_dates OWNER TO chouette;

--
-- Name: time_table_dates_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE time_table_dates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE time_table_dates_id_seq OWNER TO chouette;

--
-- Name: time_table_dates_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE time_table_dates_id_seq OWNED BY time_table_dates.id;


--
-- Name: time_table_periods; Type: TABLE; Schema: chouette_gui; Owner: chouette
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


ALTER TABLE time_table_periods OWNER TO chouette;

--
-- Name: time_table_periods_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE time_table_periods_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE time_table_periods_id_seq OWNER TO chouette;

--
-- Name: time_table_periods_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE time_table_periods_id_seq OWNED BY time_table_periods.id;


--
-- Name: time_tables; Type: TABLE; Schema: chouette_gui; Owner: chouette
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
    updated_at timestamp without time zone
);


ALTER TABLE time_tables OWNER TO chouette;

--
-- Name: time_tables_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE time_tables_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE time_tables_id_seq OWNER TO chouette;

--
-- Name: time_tables_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE time_tables_id_seq OWNED BY time_tables.id;


--
-- Name: time_tables_vehicle_journeys; Type: TABLE; Schema: chouette_gui; Owner: chouette
--

CREATE TABLE time_tables_vehicle_journeys (
    time_table_id bigint,
    vehicle_journey_id bigint
);


ALTER TABLE time_tables_vehicle_journeys OWNER TO chouette;

--
-- Name: timebands; Type: TABLE; Schema: chouette_gui; Owner: chouette
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


ALTER TABLE timebands OWNER TO chouette;

--
-- Name: timebands_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE timebands_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE timebands_id_seq OWNER TO chouette;

--
-- Name: timebands_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE timebands_id_seq OWNED BY timebands.id;


--
-- Name: vehicle_journey_at_stops; Type: TABLE; Schema: chouette_gui; Owner: chouette
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


ALTER TABLE vehicle_journey_at_stops OWNER TO chouette;

--
-- Name: vehicle_journey_at_stops_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE vehicle_journey_at_stops_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vehicle_journey_at_stops_id_seq OWNER TO chouette;

--
-- Name: vehicle_journey_at_stops_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE vehicle_journey_at_stops_id_seq OWNED BY vehicle_journey_at_stops.id;


--
-- Name: vehicle_journeys; Type: TABLE; Schema: chouette_gui; Owner: chouette
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
    updated_at timestamp without time zone
);


ALTER TABLE vehicle_journeys OWNER TO chouette;

--
-- Name: vehicle_journeys_id_seq; Type: SEQUENCE; Schema: chouette_gui; Owner: chouette
--

CREATE SEQUENCE vehicle_journeys_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vehicle_journeys_id_seq OWNER TO chouette;

--
-- Name: vehicle_journeys_id_seq; Type: SEQUENCE OWNED BY; Schema: chouette_gui; Owner: chouette
--

ALTER SEQUENCE vehicle_journeys_id_seq OWNED BY vehicle_journeys.id;


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY footnotes ALTER COLUMN id SET DEFAULT nextval('footnotes_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_frequencies ALTER COLUMN id SET DEFAULT nextval('journey_frequencies_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_pattern_sections ALTER COLUMN id SET DEFAULT nextval('journey_pattern_sections_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_patterns ALTER COLUMN id SET DEFAULT nextval('journey_patterns_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY routes ALTER COLUMN id SET DEFAULT nextval('routes_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY routing_constraint_zones ALTER COLUMN id SET DEFAULT nextval('routing_constraint_zones_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY stop_points ALTER COLUMN id SET DEFAULT nextval('stop_points_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY time_table_dates ALTER COLUMN id SET DEFAULT nextval('time_table_dates_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY time_table_periods ALTER COLUMN id SET DEFAULT nextval('time_table_periods_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY time_tables ALTER COLUMN id SET DEFAULT nextval('time_tables_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY timebands ALTER COLUMN id SET DEFAULT nextval('timebands_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY vehicle_journey_at_stops ALTER COLUMN id SET DEFAULT nextval('vehicle_journey_at_stops_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY vehicle_journeys ALTER COLUMN id SET DEFAULT nextval('vehicle_journeys_id_seq'::regclass);


--
-- Name: footnotes_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY footnotes
    ADD CONSTRAINT footnotes_pkey PRIMARY KEY (id);


--
-- Name: journey_frequencies_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT journey_frequencies_pkey PRIMARY KEY (id);


--
-- Name: journey_pattern_sections_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_pattern_sections
    ADD CONSTRAINT journey_pattern_sections_pkey PRIMARY KEY (id);


--
-- Name: journey_patterns_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT journey_patterns_pkey PRIMARY KEY (id);


--
-- Name: routes_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY routes
    ADD CONSTRAINT routes_pkey PRIMARY KEY (id);


--
-- Name: routing_constraint_zones_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY routing_constraint_zones
    ADD CONSTRAINT routing_constraint_zones_pkey PRIMARY KEY (id);


--
-- Name: stop_points_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY stop_points
    ADD CONSTRAINT stop_points_pkey PRIMARY KEY (id);


--
-- Name: time_table_dates_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY time_table_dates
    ADD CONSTRAINT time_table_dates_pkey PRIMARY KEY (id);


--
-- Name: time_table_periods_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY time_table_periods
    ADD CONSTRAINT time_table_periods_pkey PRIMARY KEY (id);


--
-- Name: time_tables_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY time_tables
    ADD CONSTRAINT time_tables_pkey PRIMARY KEY (id);


--
-- Name: timebands_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY timebands
    ADD CONSTRAINT timebands_pkey PRIMARY KEY (id);


--
-- Name: vehicle_journey_at_stops_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vehicle_journey_at_stops_pkey PRIMARY KEY (id);


--
-- Name: vehicle_journeys_pkey; Type: CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vehicle_journeys_pkey PRIMARY KEY (id);


--
-- Name: index_journey_frequencies_on_timeband_id; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_journey_frequencies_on_timeband_id ON journey_frequencies USING btree (timeband_id);


--
-- Name: index_journey_frequencies_on_vehicle_journey_id; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_journey_frequencies_on_vehicle_journey_id ON journey_frequencies USING btree (vehicle_journey_id);


--
-- Name: index_journey_pattern_id_on_journey_patterns_stop_points; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_journey_pattern_id_on_journey_patterns_stop_points ON journey_patterns_stop_points USING btree (journey_pattern_id);


--
-- Name: index_journey_pattern_sections_on_journey_pattern_id; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_journey_pattern_sections_on_journey_pattern_id ON journey_pattern_sections USING btree (journey_pattern_id);


--
-- Name: index_journey_pattern_sections_on_route_section_id; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_journey_pattern_sections_on_route_section_id ON journey_pattern_sections USING btree (route_section_id);


--
-- Name: index_jps_on_journey_pattern_id_and_route_section_id_and_rank; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE UNIQUE INDEX index_jps_on_journey_pattern_id_and_route_section_id_and_rank ON journey_pattern_sections USING btree (journey_pattern_id, route_section_id, rank);


--
-- Name: index_time_table_dates_on_time_table_id; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_time_table_dates_on_time_table_id ON time_table_dates USING btree (time_table_id);


--
-- Name: index_time_table_periods_on_time_table_id; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_time_table_periods_on_time_table_id ON time_table_periods USING btree (time_table_id);


--
-- Name: index_time_tables_vehicle_journeys_on_time_table_id; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_time_tables_vehicle_journeys_on_time_table_id ON time_tables_vehicle_journeys USING btree (time_table_id);


--
-- Name: index_time_tables_vehicle_journeys_on_vehicle_journey_id; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_time_tables_vehicle_journeys_on_vehicle_journey_id ON time_tables_vehicle_journeys USING btree (vehicle_journey_id);


--
-- Name: index_vehicle_journey_at_stops_on_stop_pointid; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_vehicle_journey_at_stops_on_stop_pointid ON vehicle_journey_at_stops USING btree (stop_point_id);


--
-- Name: index_vehicle_journey_at_stops_on_vehicle_journey_id; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_vehicle_journey_at_stops_on_vehicle_journey_id ON vehicle_journey_at_stops USING btree (vehicle_journey_id);


--
-- Name: index_vehicle_journeys_on_route_id; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE INDEX index_vehicle_journeys_on_route_id ON vehicle_journeys USING btree (route_id);


--
-- Name: journey_patterns_objectid_key; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE UNIQUE INDEX journey_patterns_objectid_key ON journey_patterns USING btree (objectid);


--
-- Name: routes_objectid_key; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE UNIQUE INDEX routes_objectid_key ON routes USING btree (objectid);


--
-- Name: stop_points_objectid_key; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE UNIQUE INDEX stop_points_objectid_key ON stop_points USING btree (objectid);


--
-- Name: time_tables_objectid_key; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE UNIQUE INDEX time_tables_objectid_key ON time_tables USING btree (objectid);


--
-- Name: vehicle_journeys_objectid_key; Type: INDEX; Schema: chouette_gui; Owner: chouette
--

CREATE UNIQUE INDEX vehicle_journeys_objectid_key ON vehicle_journeys USING btree (objectid);


--
-- Name: arrival_point_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT arrival_point_fkey FOREIGN KEY (arrival_stop_point_id) REFERENCES stop_points(id) ON DELETE SET NULL;


--
-- Name: departure_point_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT departure_point_fkey FOREIGN KEY (departure_stop_point_id) REFERENCES stop_points(id) ON DELETE SET NULL;


--
-- Name: journey_frequencies_timeband_id_fk; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT journey_frequencies_timeband_id_fk FOREIGN KEY (timeband_id) REFERENCES timebands(id) ON DELETE SET NULL;


--
-- Name: journey_frequencies_vehicle_journey_id_fk; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT journey_frequencies_vehicle_journey_id_fk FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE SET NULL;


--
-- Name: journey_pattern_sections_journey_pattern_id_fk; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_pattern_sections
    ADD CONSTRAINT journey_pattern_sections_journey_pattern_id_fk FOREIGN KEY (journey_pattern_id) REFERENCES journey_patterns(id) ON DELETE CASCADE;


--
-- Name: jp_route_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT jp_route_fkey FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE;


--
-- Name: jpsp_jp_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_patterns_stop_points
    ADD CONSTRAINT jpsp_jp_fkey FOREIGN KEY (journey_pattern_id) REFERENCES journey_patterns(id) ON DELETE CASCADE;


--
-- Name: jpsp_stoppoint_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY journey_patterns_stop_points
    ADD CONSTRAINT jpsp_stoppoint_fkey FOREIGN KEY (stop_point_id) REFERENCES stop_points(id) ON DELETE CASCADE;


--
-- Name: route_opposite_route_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY routes
    ADD CONSTRAINT route_opposite_route_fkey FOREIGN KEY (opposite_route_id) REFERENCES routes(id) ON DELETE SET NULL;


--
-- Name: tm_date_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY time_table_dates
    ADD CONSTRAINT tm_date_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;


--
-- Name: tm_period_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY time_table_periods
    ADD CONSTRAINT tm_period_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;


--
-- Name: vj_jp_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vj_jp_fkey FOREIGN KEY (journey_pattern_id) REFERENCES journey_patterns(id) ON DELETE CASCADE;


--
-- Name: vj_route_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vj_route_fkey FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE;


--
-- Name: vjas_sp_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vjas_sp_fkey FOREIGN KEY (stop_point_id) REFERENCES stop_points(id) ON DELETE CASCADE;


--
-- Name: vjas_vj_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vjas_vj_fkey FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE CASCADE;


--
-- Name: vjtm_tm_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY time_tables_vehicle_journeys
    ADD CONSTRAINT vjtm_tm_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;


--
-- Name: vjtm_vj_fkey; Type: FK CONSTRAINT; Schema: chouette_gui; Owner: chouette
--

ALTER TABLE ONLY time_tables_vehicle_journeys
    ADD CONSTRAINT vjtm_vj_fkey FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

