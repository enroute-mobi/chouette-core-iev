--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.13
-- Dumped by pg_dump version 9.5.7

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
-- SET row_security = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: access_links; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE access_links (
    id bigint NOT NULL,
    access_point_id bigint,
    stop_area_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    name character varying,
    comment character varying,
    link_distance numeric(19,2),
    lift_availability boolean,
    mobility_restricted_suitability boolean,
    stairs_availability boolean,
    default_duration time without time zone,
    frequent_traveller_duration time without time zone,
    occasional_traveller_duration time without time zone,
    mobility_restricted_traveller_duration time without time zone,
    link_type character varying,
    int_user_needs integer,
    link_orientation character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE access_links OWNER TO chouette;

--
-- Name: access_links_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE access_links_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE access_links_id_seq OWNER TO chouette;

--
-- Name: access_links_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE access_links_id_seq OWNED BY access_links.id;


--
-- Name: access_points; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE access_points (
    id bigint NOT NULL,
    objectid character varying,
    object_version bigint,
    creator_id character varying,
    name character varying,
    comment character varying,
    longitude numeric(19,16),
    latitude numeric(19,16),
    long_lat_type character varying,
    country_code character varying,
    street_name character varying,
    contained_in character varying,
    openning_time time without time zone,
    closing_time time without time zone,
    access_type character varying,
    lift_availability boolean,
    mobility_restricted_suitability boolean,
    stairs_availability boolean,
    stop_area_id bigint,
    zip_code character varying,
    city_name character varying,
    import_xml text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE access_points OWNER TO chouette;

--
-- Name: access_points_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE access_points_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE access_points_id_seq OWNER TO chouette;

--
-- Name: access_points_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE access_points_id_seq OWNED BY access_points.id;


--
-- Name: api_keys; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE api_keys (
    id bigint NOT NULL,
    referential_id bigint,
    token character varying,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    organisation_id integer
);


ALTER TABLE api_keys OWNER TO chouette;

--
-- Name: api_keys_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE api_keys_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE api_keys_id_seq OWNER TO chouette;

--
-- Name: api_keys_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE api_keys_id_seq OWNED BY api_keys.id;


--
-- Name: calendars; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE calendars (
    id bigint NOT NULL,
    name character varying,
    short_name character varying,
    date_ranges daterange[],
    dates date[],
    shared boolean DEFAULT false,
    organisation_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE calendars OWNER TO chouette;

--
-- Name: calendars_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE calendars_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE calendars_id_seq OWNER TO chouette;

--
-- Name: calendars_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE calendars_id_seq OWNED BY calendars.id;


--
-- Name: clean_up_results; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE clean_up_results (
    id bigint NOT NULL,
    message_key character varying,
    message_attributs shared_extensions.hstore,
    clean_up_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE clean_up_results OWNER TO chouette;

--
-- Name: clean_up_results_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE clean_up_results_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clean_up_results_id_seq OWNER TO chouette;

--
-- Name: clean_up_results_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE clean_up_results_id_seq OWNED BY clean_up_results.id;


--
-- Name: clean_ups; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE clean_ups (
    id bigint NOT NULL,
    status character varying,
    started_at timestamp without time zone,
    ended_at timestamp without time zone,
    referential_id bigint,
    begin_date date,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    end_date date,
    date_type character varying
);


ALTER TABLE clean_ups OWNER TO chouette;

--
-- Name: clean_ups_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE clean_ups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clean_ups_id_seq OWNER TO chouette;

--
-- Name: clean_ups_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE clean_ups_id_seq OWNED BY clean_ups.id;


--
-- Name: companies; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE companies (
    id bigint NOT NULL,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    name character varying,
    short_name character varying,
    organizational_unit character varying,
    operating_department_name character varying,
    code character varying,
    phone character varying,
    fax character varying,
    email character varying,
    registration_number character varying,
    url character varying,
    time_zone character varying,
    line_referential_id bigint,
    import_xml text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE companies OWNER TO chouette;

--
-- Name: companies_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE companies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE companies_id_seq OWNER TO chouette;

--
-- Name: companies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE companies_id_seq OWNED BY companies.id;


--
-- Name: connection_links; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE connection_links (
    id bigint NOT NULL,
    departure_id bigint,
    arrival_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    name character varying,
    comment character varying,
    link_distance numeric(19,2),
    link_type character varying,
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

--
-- Name: connection_links_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE connection_links_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE connection_links_id_seq OWNER TO chouette;

--
-- Name: connection_links_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE connection_links_id_seq OWNED BY connection_links.id;


--
-- Name: exports; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE exports (
    id bigint NOT NULL,
    referential_id bigint,
    status character varying,
    type character varying,
    options character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    references_type character varying,
    reference_ids character varying
);


ALTER TABLE exports OWNER TO chouette;

--
-- Name: exports_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE exports_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE exports_id_seq OWNER TO chouette;

--
-- Name: exports_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE exports_id_seq OWNED BY exports.id;


--
-- Name: facilities; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE facilities (
    id bigint NOT NULL,
    stop_area_id bigint,
    line_id bigint,
    connection_link_id bigint,
    stop_point_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    creation_time timestamp without time zone,
    creator_id character varying,
    name character varying,
    comment character varying,
    description character varying,
    free_access boolean,
    longitude numeric(19,16),
    latitude numeric(19,16),
    long_lat_type character varying,
    x numeric(19,2),
    y numeric(19,2),
    projection_type character varying,
    country_code character varying,
    street_name character varying,
    contained_in character varying
);


ALTER TABLE facilities OWNER TO chouette;

--
-- Name: facilities_features; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE facilities_features (
    facility_id bigint,
    choice_code integer
);


ALTER TABLE facilities_features OWNER TO chouette;

--
-- Name: facilities_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE facilities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE facilities_id_seq OWNER TO chouette;

--
-- Name: facilities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE facilities_id_seq OWNED BY facilities.id;


--
-- Name: footnotes; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE footnotes (
    id bigint NOT NULL,
    line_id bigint,
    code character varying,
    label character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    checksum character varying,
    checksum_source text,
    data_source_ref character varying
);


ALTER TABLE footnotes OWNER TO chouette;

--
-- Name: footnotes_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE footnotes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE footnotes_id_seq OWNER TO chouette;

--
-- Name: footnotes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE footnotes_id_seq OWNED BY footnotes.id;


--
-- Name: footnotes_vehicle_journeys; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE footnotes_vehicle_journeys (
    vehicle_journey_id bigint,
    footnote_id bigint
);


ALTER TABLE footnotes_vehicle_journeys OWNER TO chouette;

--
-- Name: group_of_lines; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE group_of_lines (
    id bigint NOT NULL,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    name character varying,
    comment character varying,
    registration_number character varying,
    line_referential_id bigint,
    import_xml text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE group_of_lines OWNER TO chouette;

--
-- Name: group_of_lines_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE group_of_lines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE group_of_lines_id_seq OWNER TO chouette;

--
-- Name: group_of_lines_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE group_of_lines_id_seq OWNED BY group_of_lines.id;


--
-- Name: group_of_lines_lines; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE group_of_lines_lines (
    group_of_line_id bigint,
    line_id bigint
);


ALTER TABLE group_of_lines_lines OWNER TO chouette;

--
-- Name: import_messages; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE import_messages (
    id bigint NOT NULL,
    criticity integer,
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
-- Name: journey_frequencies; Type: TABLE; Schema: public; Owner: chouette
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
-- Name: journey_frequencies_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE journey_frequencies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE journey_frequencies_id_seq OWNER TO chouette;

--
-- Name: journey_frequencies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE journey_frequencies_id_seq OWNED BY journey_frequencies.id;


--
-- Name: journey_pattern_sections; Type: TABLE; Schema: public; Owner: chouette
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
-- Name: journey_pattern_sections_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE journey_pattern_sections_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE journey_pattern_sections_id_seq OWNER TO chouette;

--
-- Name: journey_pattern_sections_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE journey_pattern_sections_id_seq OWNED BY journey_pattern_sections.id;


--
-- Name: journey_patterns; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE journey_patterns (
    id bigint NOT NULL,
    route_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    name character varying,
    comment character varying,
    registration_number character varying,
    published_name character varying,
    departure_stop_point_id bigint,
    arrival_stop_point_id bigint,
    section_status integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    checksum character varying,
    checksum_source text,
    data_source_ref character varying
);


ALTER TABLE journey_patterns OWNER TO chouette;

--
-- Name: journey_patterns_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE journey_patterns_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE journey_patterns_id_seq OWNER TO chouette;

--
-- Name: journey_patterns_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE journey_patterns_id_seq OWNED BY journey_patterns.id;


--
-- Name: journey_patterns_stop_points; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE journey_patterns_stop_points (
    journey_pattern_id bigint,
    stop_point_id bigint
);


ALTER TABLE journey_patterns_stop_points OWNER TO chouette;

--
-- Name: line_referential_memberships; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE line_referential_memberships (
    id bigint NOT NULL,
    organisation_id bigint,
    line_referential_id bigint,
    owner boolean
);


ALTER TABLE line_referential_memberships OWNER TO chouette;

--
-- Name: line_referential_memberships_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE line_referential_memberships_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE line_referential_memberships_id_seq OWNER TO chouette;

--
-- Name: line_referential_memberships_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE line_referential_memberships_id_seq OWNED BY line_referential_memberships.id;


--
-- Name: line_referential_sync_messages; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE line_referential_sync_messages (
    id bigint NOT NULL,
    criticity integer,
    message_key character varying,
    message_attributes shared_extensions.hstore,
    line_referential_sync_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE line_referential_sync_messages OWNER TO chouette;

--
-- Name: line_referential_sync_messages_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE line_referential_sync_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE line_referential_sync_messages_id_seq OWNER TO chouette;

--
-- Name: line_referential_sync_messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE line_referential_sync_messages_id_seq OWNED BY line_referential_sync_messages.id;


--
-- Name: line_referential_syncs; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE line_referential_syncs (
    id bigint NOT NULL,
    line_referential_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    started_at timestamp without time zone,
    ended_at timestamp without time zone,
    status character varying
);


ALTER TABLE line_referential_syncs OWNER TO chouette;

--
-- Name: line_referential_syncs_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE line_referential_syncs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE line_referential_syncs_id_seq OWNER TO chouette;

--
-- Name: line_referential_syncs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE line_referential_syncs_id_seq OWNED BY line_referential_syncs.id;


--
-- Name: line_referentials; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE line_referentials (
    id bigint NOT NULL,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    sync_interval integer DEFAULT 1
);


ALTER TABLE line_referentials OWNER TO chouette;

--
-- Name: line_referentials_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE line_referentials_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE line_referentials_id_seq OWNER TO chouette;

--
-- Name: line_referentials_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE line_referentials_id_seq OWNED BY line_referentials.id;


--
-- Name: lines; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE lines (
    id bigint NOT NULL,
    network_id bigint,
    company_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    name character varying,
    number character varying,
    published_name character varying,
    transport_mode character varying,
    registration_number character varying,
    comment character varying,
    mobility_restricted_suitability boolean,
    int_user_needs integer,
    flexible_service boolean,
    url character varying,
    color character varying(6),
    text_color character varying(6),
    stable_id character varying,
    line_referential_id bigint,
    deactivated boolean DEFAULT false,
    import_xml text,
    transport_submode character varying,
    secondary_company_ids bigint[],
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    seasonal boolean
);


ALTER TABLE lines OWNER TO chouette;

--
-- Name: lines_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE lines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE lines_id_seq OWNER TO chouette;

--
-- Name: lines_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE lines_id_seq OWNED BY lines.id;


--
-- Name: networks; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE networks (
    id bigint NOT NULL,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    version_date date,
    description character varying,
    name character varying,
    registration_number character varying,
    source_name character varying,
    source_type character varying,
    source_identifier character varying,
    comment character varying,
    import_xml text,
    line_referential_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE networks OWNER TO chouette;

--
-- Name: networks_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE networks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE networks_id_seq OWNER TO chouette;

--
-- Name: networks_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE networks_id_seq OWNED BY networks.id;


--
-- Name: organisations; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE organisations (
    id bigint NOT NULL,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    data_format character varying DEFAULT 'neptune'::character varying,
    code character varying,
    synced_at timestamp without time zone,
    sso_attributes shared_extensions.hstore
);


ALTER TABLE organisations OWNER TO chouette;

--
-- Name: organisations_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE organisations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE organisations_id_seq OWNER TO chouette;

--
-- Name: organisations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE organisations_id_seq OWNED BY organisations.id;


--
-- Name: pt_links; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE pt_links (
    id bigint NOT NULL,
    start_of_link_id bigint,
    end_of_link_id bigint,
    route_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    name character varying,
    comment character varying,
    link_distance numeric(19,2),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE pt_links OWNER TO chouette;

--
-- Name: pt_links_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE pt_links_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE pt_links_id_seq OWNER TO chouette;

--
-- Name: pt_links_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE pt_links_id_seq OWNED BY pt_links.id;


--
-- Name: referential_clonings; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE referential_clonings (
    id bigint NOT NULL,
    status character varying,
    started_at timestamp without time zone,
    ended_at timestamp without time zone,
    source_referential_id bigint,
    target_referential_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE referential_clonings OWNER TO chouette;

--
-- Name: referential_clonings_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE referential_clonings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE referential_clonings_id_seq OWNER TO chouette;

--
-- Name: referential_clonings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE referential_clonings_id_seq OWNED BY referential_clonings.id;


--
-- Name: referential_metadata; Type: TABLE; Schema: public; Owner: chouette
--

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

--
-- Name: referential_metadata_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE referential_metadata_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE referential_metadata_id_seq OWNER TO chouette;

--
-- Name: referential_metadata_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE referential_metadata_id_seq OWNED BY referential_metadata.id;


--
-- Name: referential_suites; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE referential_suites (
    id bigint NOT NULL,
    new_id bigint,
    current_id bigint,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);


ALTER TABLE referential_suites OWNER TO chouette;

--
-- Name: referential_suites_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE referential_suites_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE referential_suites_id_seq OWNER TO chouette;

--
-- Name: referential_suites_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE referential_suites_id_seq OWNED BY referential_suites.id;


--
-- Name: referentials; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE referentials (
    id bigint NOT NULL,
    name character varying,
    slug character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    prefix character varying,
    projection_type character varying,
    time_zone character varying,
    bounds character varying,
    organisation_id bigint,
    geographical_bounds text,
    user_id bigint,
    user_name character varying,
    data_format character varying,
    line_referential_id bigint,
    stop_area_referential_id bigint,
    workbench_id bigint,
    archived_at timestamp without time zone,
    created_from_id bigint,
    ready boolean DEFAULT false,
    referential_suite_id bigint
);


ALTER TABLE referentials OWNER TO chouette;

--
-- Name: referentials_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE referentials_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE referentials_id_seq OWNER TO chouette;

--
-- Name: referentials_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE referentials_id_seq OWNED BY referentials.id;


--
-- Name: route_sections; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE route_sections (
    id bigint NOT NULL,
    departure_id bigint,
    arrival_id bigint,
    input_geometry shared_extensions.geometry(LineString,4326),
    processed_geometry shared_extensions.geometry(LineString,4326),
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    distance double precision,
    no_processing boolean,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE route_sections OWNER TO chouette;

--
-- Name: route_sections_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE route_sections_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE route_sections_id_seq OWNER TO chouette;

--
-- Name: route_sections_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE route_sections_id_seq OWNED BY route_sections.id;


--
-- Name: routes; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE routes (
    id bigint NOT NULL,
    line_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    name character varying,
    comment character varying,
    opposite_route_id bigint,
    published_name character varying,
    number character varying,
    direction character varying,
    wayback character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    checksum character varying,
    checksum_source text,
    data_source_ref character varying
);


ALTER TABLE routes OWNER TO chouette;

--
-- Name: routes_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE routes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE routes_id_seq OWNER TO chouette;

--
-- Name: routes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE routes_id_seq OWNED BY routes.id;


--
-- Name: routing_constraint_zones; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE routing_constraint_zones (
    id bigint NOT NULL,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    route_id bigint,
    stop_point_ids bigint[],
    checksum character varying,
    checksum_source text,
    data_source_ref character varying
);


ALTER TABLE routing_constraint_zones OWNER TO chouette;

--
-- Name: routing_constraint_zones_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE routing_constraint_zones_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE routing_constraint_zones_id_seq OWNER TO chouette;

--
-- Name: routing_constraint_zones_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE routing_constraint_zones_id_seq OWNED BY routing_constraint_zones.id;


--
-- Name: routing_constraints_lines; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE routing_constraints_lines (
    stop_area_id bigint,
    line_id bigint
);


ALTER TABLE routing_constraints_lines OWNER TO chouette;

--
-- Name: rule_parameter_sets; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE rule_parameter_sets (
    id bigint NOT NULL,
    parameters text,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    organisation_id bigint
);


ALTER TABLE rule_parameter_sets OWNER TO chouette;

--
-- Name: rule_parameter_sets_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE rule_parameter_sets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE rule_parameter_sets_id_seq OWNER TO chouette;

--
-- Name: rule_parameter_sets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE rule_parameter_sets_id_seq OWNED BY rule_parameter_sets.id;


--
-- Name: schema_migrations; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE schema_migrations (
    version character varying NOT NULL
);


ALTER TABLE schema_migrations OWNER TO chouette;

--
-- Name: stop_area_referential_memberships; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE stop_area_referential_memberships (
    id bigint NOT NULL,
    organisation_id bigint,
    stop_area_referential_id bigint,
    owner boolean
);


ALTER TABLE stop_area_referential_memberships OWNER TO chouette;

--
-- Name: stop_area_referential_memberships_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE stop_area_referential_memberships_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stop_area_referential_memberships_id_seq OWNER TO chouette;

--
-- Name: stop_area_referential_memberships_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE stop_area_referential_memberships_id_seq OWNED BY stop_area_referential_memberships.id;


--
-- Name: stop_area_referential_sync_messages; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE stop_area_referential_sync_messages (
    id bigint NOT NULL,
    criticity integer,
    message_key character varying,
    message_attributes shared_extensions.hstore,
    stop_area_referential_sync_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE stop_area_referential_sync_messages OWNER TO chouette;

--
-- Name: stop_area_referential_sync_messages_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE stop_area_referential_sync_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stop_area_referential_sync_messages_id_seq OWNER TO chouette;

--
-- Name: stop_area_referential_sync_messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE stop_area_referential_sync_messages_id_seq OWNED BY stop_area_referential_sync_messages.id;


--
-- Name: stop_area_referential_syncs; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE stop_area_referential_syncs (
    id bigint NOT NULL,
    stop_area_referential_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    ended_at timestamp without time zone,
    started_at timestamp without time zone,
    status character varying
);


ALTER TABLE stop_area_referential_syncs OWNER TO chouette;

--
-- Name: stop_area_referential_syncs_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE stop_area_referential_syncs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stop_area_referential_syncs_id_seq OWNER TO chouette;

--
-- Name: stop_area_referential_syncs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE stop_area_referential_syncs_id_seq OWNED BY stop_area_referential_syncs.id;


--
-- Name: stop_area_referentials; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE stop_area_referentials (
    id bigint NOT NULL,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE stop_area_referentials OWNER TO chouette;

--
-- Name: stop_area_referentials_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE stop_area_referentials_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stop_area_referentials_id_seq OWNER TO chouette;

--
-- Name: stop_area_referentials_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE stop_area_referentials_id_seq OWNED BY stop_area_referentials.id;


--
-- Name: stop_areas; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE stop_areas (
    id bigint NOT NULL,
    parent_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    name character varying,
    comment character varying,
    area_type character varying,
    registration_number character varying,
    nearest_topic_name character varying,
    fare_code integer,
    longitude numeric(19,16),
    latitude numeric(19,16),
    long_lat_type character varying,
    country_code character varying,
    street_name character varying,
    mobility_restricted_suitability boolean,
    stairs_availability boolean,
    lift_availability boolean,
    int_user_needs integer,
    zip_code character varying,
    city_name character varying,
    url character varying,
    time_zone character varying,
    stop_area_referential_id bigint,
    status character varying,
    import_xml text,
    deleted_at timestamp without time zone,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    stif_type character varying
);


ALTER TABLE stop_areas OWNER TO chouette;

--
-- Name: stop_areas_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE stop_areas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stop_areas_id_seq OWNER TO chouette;

--
-- Name: stop_areas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE stop_areas_id_seq OWNED BY stop_areas.id;


--
-- Name: stop_areas_stop_areas; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE stop_areas_stop_areas (
    child_id bigint,
    parent_id bigint
);


ALTER TABLE stop_areas_stop_areas OWNER TO chouette;

--
-- Name: stop_points; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE stop_points (
    id bigint NOT NULL,
    route_id bigint,
    stop_area_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    "position" integer,
    for_boarding character varying,
    for_alighting character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE stop_points OWNER TO chouette;

--
-- Name: stop_points_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE stop_points_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE stop_points_id_seq OWNER TO chouette;

--
-- Name: stop_points_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE stop_points_id_seq OWNED BY stop_points.id;


--
-- Name: taggings; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE taggings (
    id bigint NOT NULL,
    tag_id bigint,
    taggable_id bigint,
    taggable_type character varying,
    tagger_id bigint,
    tagger_type character varying,
    context character varying(128),
    created_at timestamp without time zone
);


ALTER TABLE taggings OWNER TO chouette;

--
-- Name: taggings_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE taggings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE taggings_id_seq OWNER TO chouette;

--
-- Name: taggings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE taggings_id_seq OWNED BY taggings.id;


--
-- Name: tags; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE tags (
    id bigint NOT NULL,
    name character varying,
    taggings_count integer DEFAULT 0
);


ALTER TABLE tags OWNER TO chouette;

--
-- Name: tags_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE tags_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tags_id_seq OWNER TO chouette;

--
-- Name: tags_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE tags_id_seq OWNED BY tags.id;


--
-- Name: time_table_dates; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE time_table_dates (
    time_table_id bigint NOT NULL,
    date date,
    "position" integer NOT NULL,
    id bigint NOT NULL,
    in_out boolean,
    checksum character varying,
    checksum_source text
);


ALTER TABLE time_table_dates OWNER TO chouette;

--
-- Name: time_table_dates_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE time_table_dates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE time_table_dates_id_seq OWNER TO chouette;

--
-- Name: time_table_dates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE time_table_dates_id_seq OWNED BY time_table_dates.id;


--
-- Name: time_table_periods; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE time_table_periods (
    time_table_id bigint NOT NULL,
    period_start date,
    period_end date,
    "position" integer NOT NULL,
    id bigint NOT NULL,
    checksum character varying,
    checksum_source text
);


ALTER TABLE time_table_periods OWNER TO chouette;

--
-- Name: time_table_periods_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE time_table_periods_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE time_table_periods_id_seq OWNER TO chouette;

--
-- Name: time_table_periods_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE time_table_periods_id_seq OWNED BY time_table_periods.id;


--
-- Name: time_tables; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE time_tables (
    id bigint NOT NULL,
    objectid character varying NOT NULL,
    object_version bigint DEFAULT 1,
    creator_id character varying,
    version character varying,
    comment character varying,
    int_day_types integer DEFAULT 0,
    start_date date,
    end_date date,
    calendar_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    color character varying,
    created_from_id integer,
    checksum character varying,
    checksum_source text,
    data_source_ref character varying
);


ALTER TABLE time_tables OWNER TO chouette;

--
-- Name: time_tables_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE time_tables_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE time_tables_id_seq OWNER TO chouette;

--
-- Name: time_tables_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE time_tables_id_seq OWNED BY time_tables.id;


--
-- Name: time_tables_vehicle_journeys; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE time_tables_vehicle_journeys (
    time_table_id bigint,
    vehicle_journey_id bigint
);


ALTER TABLE time_tables_vehicle_journeys OWNER TO chouette;

--
-- Name: timebands; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE timebands (
    id bigint NOT NULL,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    name character varying,
    start_time time without time zone NOT NULL,
    end_time time without time zone NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


ALTER TABLE timebands OWNER TO chouette;

--
-- Name: timebands_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE timebands_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE timebands_id_seq OWNER TO chouette;

--
-- Name: timebands_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE timebands_id_seq OWNED BY timebands.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE users (
    id bigint NOT NULL,
    email character varying DEFAULT ''::character varying NOT NULL,
    encrypted_password character varying DEFAULT ''::character varying,
    reset_password_token character varying,
    reset_password_sent_at timestamp without time zone,
    remember_created_at timestamp without time zone,
    sign_in_count integer DEFAULT 0,
    current_sign_in_at timestamp without time zone,
    last_sign_in_at timestamp without time zone,
    current_sign_in_ip character varying,
    last_sign_in_ip character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    organisation_id bigint,
    name character varying,
    confirmation_token character varying,
    confirmed_at timestamp without time zone,
    confirmation_sent_at timestamp without time zone,
    unconfirmed_email character varying,
    failed_attempts integer DEFAULT 0,
    unlock_token character varying,
    locked_at timestamp without time zone,
    authentication_token character varying,
    invitation_token character varying,
    invitation_sent_at timestamp without time zone,
    invitation_accepted_at timestamp without time zone,
    invitation_limit integer,
    invited_by_id bigint,
    invited_by_type character varying,
    invitation_created_at timestamp without time zone,
    username character varying,
    synced_at timestamp without time zone,
    permissions character varying[]
);


ALTER TABLE users OWNER TO chouette;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO chouette;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: vehicle_journey_at_stops; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE vehicle_journey_at_stops (
    id bigint NOT NULL,
    vehicle_journey_id bigint,
    stop_point_id bigint,
    connecting_service_id character varying,
    boarding_alighting_possibility character varying,
    arrival_time time without time zone,
    departure_time time without time zone,
    for_boarding character varying,
    for_alighting character varying,
    departure_day_offset integer DEFAULT 0,
    arrival_day_offset integer DEFAULT 0,
    checksum character varying,
    checksum_source text
);


ALTER TABLE vehicle_journey_at_stops OWNER TO chouette;

--
-- Name: vehicle_journey_at_stops_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE vehicle_journey_at_stops_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vehicle_journey_at_stops_id_seq OWNER TO chouette;

--
-- Name: vehicle_journey_at_stops_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE vehicle_journey_at_stops_id_seq OWNED BY vehicle_journey_at_stops.id;


--
-- Name: vehicle_journeys; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE vehicle_journeys (
    id bigint NOT NULL,
    route_id bigint,
    journey_pattern_id bigint,
    company_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    creator_id character varying,
    comment character varying,
    status_value character varying,
    transport_mode character varying,
    published_journey_name character varying,
    published_journey_identifier character varying,
    facility character varying,
    vehicle_type_identifier character varying,
    number bigint,
    mobility_restricted_suitability boolean,
    flexible_service boolean,
    journey_category integer DEFAULT 0 NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    checksum character varying,
    checksum_source text,
    data_source_ref character varying
);


ALTER TABLE vehicle_journeys OWNER TO chouette;

--
-- Name: vehicle_journeys_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE vehicle_journeys_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE vehicle_journeys_id_seq OWNER TO chouette;

--
-- Name: vehicle_journeys_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE vehicle_journeys_id_seq OWNED BY vehicle_journeys.id;


--
-- Name: workbenches; Type: TABLE; Schema: public; Owner: chouette
--

CREATE TABLE workbenches (
    id bigint NOT NULL,
    name character varying,
    organisation_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    line_referential_id bigint,
    stop_area_referential_id bigint,
    output_id bigint
);


ALTER TABLE workbenches OWNER TO chouette;

--
-- Name: workbenches_id_seq; Type: SEQUENCE; Schema: public; Owner: chouette
--

CREATE SEQUENCE workbenches_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE workbenches_id_seq OWNER TO chouette;

--
-- Name: workbenches_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: chouette
--

ALTER SEQUENCE workbenches_id_seq OWNED BY workbenches.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY access_links ALTER COLUMN id SET DEFAULT nextval('access_links_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY access_points ALTER COLUMN id SET DEFAULT nextval('access_points_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY api_keys ALTER COLUMN id SET DEFAULT nextval('api_keys_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY calendars ALTER COLUMN id SET DEFAULT nextval('calendars_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY clean_up_results ALTER COLUMN id SET DEFAULT nextval('clean_up_results_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY clean_ups ALTER COLUMN id SET DEFAULT nextval('clean_ups_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY companies ALTER COLUMN id SET DEFAULT nextval('companies_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY connection_links ALTER COLUMN id SET DEFAULT nextval('connection_links_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY exports ALTER COLUMN id SET DEFAULT nextval('exports_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY facilities ALTER COLUMN id SET DEFAULT nextval('facilities_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY footnotes ALTER COLUMN id SET DEFAULT nextval('footnotes_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY group_of_lines ALTER COLUMN id SET DEFAULT nextval('group_of_lines_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY import_messages ALTER COLUMN id SET DEFAULT nextval('import_messages_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY import_resources ALTER COLUMN id SET DEFAULT nextval('import_resources_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY imports ALTER COLUMN id SET DEFAULT nextval('imports_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_frequencies ALTER COLUMN id SET DEFAULT nextval('journey_frequencies_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_pattern_sections ALTER COLUMN id SET DEFAULT nextval('journey_pattern_sections_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_patterns ALTER COLUMN id SET DEFAULT nextval('journey_patterns_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY line_referential_memberships ALTER COLUMN id SET DEFAULT nextval('line_referential_memberships_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY line_referential_sync_messages ALTER COLUMN id SET DEFAULT nextval('line_referential_sync_messages_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY line_referential_syncs ALTER COLUMN id SET DEFAULT nextval('line_referential_syncs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY line_referentials ALTER COLUMN id SET DEFAULT nextval('line_referentials_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY lines ALTER COLUMN id SET DEFAULT nextval('lines_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY networks ALTER COLUMN id SET DEFAULT nextval('networks_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY organisations ALTER COLUMN id SET DEFAULT nextval('organisations_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY pt_links ALTER COLUMN id SET DEFAULT nextval('pt_links_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY referential_clonings ALTER COLUMN id SET DEFAULT nextval('referential_clonings_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY referential_metadata ALTER COLUMN id SET DEFAULT nextval('referential_metadata_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY referential_suites ALTER COLUMN id SET DEFAULT nextval('referential_suites_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY referentials ALTER COLUMN id SET DEFAULT nextval('referentials_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY route_sections ALTER COLUMN id SET DEFAULT nextval('route_sections_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY routes ALTER COLUMN id SET DEFAULT nextval('routes_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY routing_constraint_zones ALTER COLUMN id SET DEFAULT nextval('routing_constraint_zones_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY rule_parameter_sets ALTER COLUMN id SET DEFAULT nextval('rule_parameter_sets_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_area_referential_memberships ALTER COLUMN id SET DEFAULT nextval('stop_area_referential_memberships_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_area_referential_sync_messages ALTER COLUMN id SET DEFAULT nextval('stop_area_referential_sync_messages_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_area_referential_syncs ALTER COLUMN id SET DEFAULT nextval('stop_area_referential_syncs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_area_referentials ALTER COLUMN id SET DEFAULT nextval('stop_area_referentials_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_areas ALTER COLUMN id SET DEFAULT nextval('stop_areas_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_points ALTER COLUMN id SET DEFAULT nextval('stop_points_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY taggings ALTER COLUMN id SET DEFAULT nextval('taggings_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY tags ALTER COLUMN id SET DEFAULT nextval('tags_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY time_table_dates ALTER COLUMN id SET DEFAULT nextval('time_table_dates_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY time_table_periods ALTER COLUMN id SET DEFAULT nextval('time_table_periods_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY time_tables ALTER COLUMN id SET DEFAULT nextval('time_tables_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY timebands ALTER COLUMN id SET DEFAULT nextval('timebands_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY vehicle_journey_at_stops ALTER COLUMN id SET DEFAULT nextval('vehicle_journey_at_stops_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY vehicle_journeys ALTER COLUMN id SET DEFAULT nextval('vehicle_journeys_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY workbenches ALTER COLUMN id SET DEFAULT nextval('workbenches_id_seq'::regclass);


--
-- Name: access_links_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY access_links
    ADD CONSTRAINT access_links_pkey PRIMARY KEY (id);


--
-- Name: access_points_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY access_points
    ADD CONSTRAINT access_points_pkey PRIMARY KEY (id);


--
-- Name: api_keys_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY api_keys
    ADD CONSTRAINT api_keys_pkey PRIMARY KEY (id);


--
-- Name: calendars_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY calendars
    ADD CONSTRAINT calendars_pkey PRIMARY KEY (id);


--
-- Name: clean_up_results_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY clean_up_results
    ADD CONSTRAINT clean_up_results_pkey PRIMARY KEY (id);


--
-- Name: clean_ups_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY clean_ups
    ADD CONSTRAINT clean_ups_pkey PRIMARY KEY (id);


--
-- Name: companies_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY companies
    ADD CONSTRAINT companies_pkey PRIMARY KEY (id);


--
-- Name: connection_links_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY connection_links
    ADD CONSTRAINT connection_links_pkey PRIMARY KEY (id);


--
-- Name: exports_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY exports
    ADD CONSTRAINT exports_pkey PRIMARY KEY (id);


--
-- Name: facilities_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY facilities
    ADD CONSTRAINT facilities_pkey PRIMARY KEY (id);


--
-- Name: footnotes_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY footnotes
    ADD CONSTRAINT footnotes_pkey PRIMARY KEY (id);


--
-- Name: group_of_lines_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY group_of_lines
    ADD CONSTRAINT group_of_lines_pkey PRIMARY KEY (id);


--
-- Name: import_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY import_messages
    ADD CONSTRAINT import_messages_pkey PRIMARY KEY (id);


--
-- Name: import_resources_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY import_resources
    ADD CONSTRAINT import_resources_pkey PRIMARY KEY (id);


--
-- Name: imports_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY imports
    ADD CONSTRAINT imports_pkey PRIMARY KEY (id);


--
-- Name: journey_frequencies_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT journey_frequencies_pkey PRIMARY KEY (id);


--
-- Name: journey_pattern_sections_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_pattern_sections
    ADD CONSTRAINT journey_pattern_sections_pkey PRIMARY KEY (id);


--
-- Name: journey_patterns_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT journey_patterns_pkey PRIMARY KEY (id);


--
-- Name: line_referential_memberships_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY line_referential_memberships
    ADD CONSTRAINT line_referential_memberships_pkey PRIMARY KEY (id);


--
-- Name: line_referential_sync_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY line_referential_sync_messages
    ADD CONSTRAINT line_referential_sync_messages_pkey PRIMARY KEY (id);


--
-- Name: line_referential_syncs_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY line_referential_syncs
    ADD CONSTRAINT line_referential_syncs_pkey PRIMARY KEY (id);


--
-- Name: line_referentials_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY line_referentials
    ADD CONSTRAINT line_referentials_pkey PRIMARY KEY (id);


--
-- Name: lines_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY lines
    ADD CONSTRAINT lines_pkey PRIMARY KEY (id);


--
-- Name: networks_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY networks
    ADD CONSTRAINT networks_pkey PRIMARY KEY (id);


--
-- Name: organisations_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY organisations
    ADD CONSTRAINT organisations_pkey PRIMARY KEY (id);


--
-- Name: pt_links_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY pt_links
    ADD CONSTRAINT pt_links_pkey PRIMARY KEY (id);


--
-- Name: referential_clonings_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY referential_clonings
    ADD CONSTRAINT referential_clonings_pkey PRIMARY KEY (id);


--
-- Name: referential_metadata_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY referential_metadata
    ADD CONSTRAINT referential_metadata_pkey PRIMARY KEY (id);


--
-- Name: referential_suites_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY referential_suites
    ADD CONSTRAINT referential_suites_pkey PRIMARY KEY (id);


--
-- Name: referentials_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY referentials
    ADD CONSTRAINT referentials_pkey PRIMARY KEY (id);


--
-- Name: route_sections_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY route_sections
    ADD CONSTRAINT route_sections_pkey PRIMARY KEY (id);


--
-- Name: routes_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY routes
    ADD CONSTRAINT routes_pkey PRIMARY KEY (id);


--
-- Name: routing_constraint_zones_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY routing_constraint_zones
    ADD CONSTRAINT routing_constraint_zones_pkey PRIMARY KEY (id);


--
-- Name: rule_parameter_sets_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY rule_parameter_sets
    ADD CONSTRAINT rule_parameter_sets_pkey PRIMARY KEY (id);


--
-- Name: stop_area_referential_memberships_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_area_referential_memberships
    ADD CONSTRAINT stop_area_referential_memberships_pkey PRIMARY KEY (id);


--
-- Name: stop_area_referential_sync_messages_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_area_referential_sync_messages
    ADD CONSTRAINT stop_area_referential_sync_messages_pkey PRIMARY KEY (id);


--
-- Name: stop_area_referential_syncs_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_area_referential_syncs
    ADD CONSTRAINT stop_area_referential_syncs_pkey PRIMARY KEY (id);


--
-- Name: stop_area_referentials_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_area_referentials
    ADD CONSTRAINT stop_area_referentials_pkey PRIMARY KEY (id);


--
-- Name: stop_areas_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_areas
    ADD CONSTRAINT stop_areas_pkey PRIMARY KEY (id);


--
-- Name: stop_points_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_points
    ADD CONSTRAINT stop_points_pkey PRIMARY KEY (id);


--
-- Name: taggings_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY taggings
    ADD CONSTRAINT taggings_pkey PRIMARY KEY (id);


--
-- Name: tags_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY tags
    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);


--
-- Name: time_table_dates_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY time_table_dates
    ADD CONSTRAINT time_table_dates_pkey PRIMARY KEY (id);


--
-- Name: time_table_periods_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY time_table_periods
    ADD CONSTRAINT time_table_periods_pkey PRIMARY KEY (id);


--
-- Name: time_tables_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY time_tables
    ADD CONSTRAINT time_tables_pkey PRIMARY KEY (id);


--
-- Name: timebands_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY timebands
    ADD CONSTRAINT timebands_pkey PRIMARY KEY (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: vehicle_journey_at_stops_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vehicle_journey_at_stops_pkey PRIMARY KEY (id);


--
-- Name: vehicle_journeys_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vehicle_journeys_pkey PRIMARY KEY (id);


--
-- Name: workbenches_pkey; Type: CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY workbenches
    ADD CONSTRAINT workbenches_pkey PRIMARY KEY (id);


--
-- Name: access_links_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX access_links_objectid_key ON access_links USING btree (objectid);


--
-- Name: access_points_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX access_points_objectid_key ON access_points USING btree (objectid);


--
-- Name: companies_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX companies_objectid_key ON companies USING btree (objectid);


--
-- Name: companies_registration_number_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX companies_registration_number_key ON companies USING btree (registration_number);


--
-- Name: connection_links_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX connection_links_objectid_key ON connection_links USING btree (objectid);


--
-- Name: facilities_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX facilities_objectid_key ON facilities USING btree (objectid);


--
-- Name: group_of_lines_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX group_of_lines_objectid_key ON group_of_lines USING btree (objectid);


--
-- Name: index_api_keys_on_organisation_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_api_keys_on_organisation_id ON api_keys USING btree (organisation_id);


--
-- Name: index_calendars_on_organisation_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_calendars_on_organisation_id ON calendars USING btree (organisation_id);


--
-- Name: index_calendars_on_short_name; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX index_calendars_on_short_name ON calendars USING btree (short_name);


--
-- Name: index_clean_up_results_on_clean_up_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_clean_up_results_on_clean_up_id ON clean_up_results USING btree (clean_up_id);


--
-- Name: index_clean_ups_on_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_clean_ups_on_referential_id ON clean_ups USING btree (referential_id);


--
-- Name: index_companies_on_line_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_companies_on_line_referential_id ON companies USING btree (line_referential_id);


--
-- Name: index_exports_on_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_exports_on_referential_id ON exports USING btree (referential_id);


--
-- Name: index_group_of_lines_on_line_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_group_of_lines_on_line_referential_id ON group_of_lines USING btree (line_referential_id);


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
-- Name: index_journey_frequencies_on_timeband_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_journey_frequencies_on_timeband_id ON journey_frequencies USING btree (timeband_id);


--
-- Name: index_journey_frequencies_on_vehicle_journey_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_journey_frequencies_on_vehicle_journey_id ON journey_frequencies USING btree (vehicle_journey_id);


--
-- Name: index_journey_pattern_id_on_journey_patterns_stop_points; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_journey_pattern_id_on_journey_patterns_stop_points ON journey_patterns_stop_points USING btree (journey_pattern_id);


--
-- Name: index_journey_pattern_sections_on_journey_pattern_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_journey_pattern_sections_on_journey_pattern_id ON journey_pattern_sections USING btree (journey_pattern_id);


--
-- Name: index_journey_pattern_sections_on_route_section_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_journey_pattern_sections_on_route_section_id ON journey_pattern_sections USING btree (route_section_id);


--
-- Name: index_jps_on_journey_pattern_id_and_route_section_id_and_rank; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX index_jps_on_journey_pattern_id_and_route_section_id_and_rank ON journey_pattern_sections USING btree (journey_pattern_id, route_section_id, rank);


--
-- Name: index_line_referential_syncs_on_line_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_line_referential_syncs_on_line_referential_id ON line_referential_syncs USING btree (line_referential_id);


--
-- Name: index_lines_on_line_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_lines_on_line_referential_id ON lines USING btree (line_referential_id);


--
-- Name: index_lines_on_secondary_company_ids; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_lines_on_secondary_company_ids ON lines USING gin (secondary_company_ids);


--
-- Name: index_networks_on_line_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_networks_on_line_referential_id ON networks USING btree (line_referential_id);


--
-- Name: index_organisations_on_code; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX index_organisations_on_code ON organisations USING btree (code);


--
-- Name: index_referential_clonings_on_source_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_referential_clonings_on_source_referential_id ON referential_clonings USING btree (source_referential_id);


--
-- Name: index_referential_clonings_on_target_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_referential_clonings_on_target_referential_id ON referential_clonings USING btree (target_referential_id);


--
-- Name: index_referential_metadata_on_line_ids; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_referential_metadata_on_line_ids ON referential_metadata USING gin (line_ids);


--
-- Name: index_referential_metadata_on_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_referential_metadata_on_referential_id ON referential_metadata USING btree (referential_id);


--
-- Name: index_referential_metadata_on_referential_source_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_referential_metadata_on_referential_source_id ON referential_metadata USING btree (referential_source_id);


--
-- Name: index_referential_suites_on_current_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_referential_suites_on_current_id ON referential_suites USING btree (current_id);


--
-- Name: index_referential_suites_on_new_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_referential_suites_on_new_id ON referential_suites USING btree (new_id);


--
-- Name: index_referentials_on_created_from_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_referentials_on_created_from_id ON referentials USING btree (created_from_id);


--
-- Name: index_referentials_on_referential_suite_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_referentials_on_referential_suite_id ON referentials USING btree (referential_suite_id);


--
-- Name: index_stop_area_referential_syncs_on_stop_area_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_stop_area_referential_syncs_on_stop_area_referential_id ON stop_area_referential_syncs USING btree (stop_area_referential_id);


--
-- Name: index_stop_areas_on_name; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_stop_areas_on_name ON stop_areas USING btree (name);


--
-- Name: index_stop_areas_on_parent_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_stop_areas_on_parent_id ON stop_areas USING btree (parent_id);


--
-- Name: index_stop_areas_on_stop_area_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_stop_areas_on_stop_area_referential_id ON stop_areas USING btree (stop_area_referential_id);


--
-- Name: index_taggings_on_taggable_id_and_taggable_type_and_context; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_taggings_on_taggable_id_and_taggable_type_and_context ON taggings USING btree (taggable_id, taggable_type, context);


--
-- Name: index_tags_on_name; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX index_tags_on_name ON tags USING btree (name);


--
-- Name: index_time_table_dates_on_time_table_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_time_table_dates_on_time_table_id ON time_table_dates USING btree (time_table_id);


--
-- Name: index_time_table_periods_on_time_table_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_time_table_periods_on_time_table_id ON time_table_periods USING btree (time_table_id);


--
-- Name: index_time_tables_on_calendar_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_time_tables_on_calendar_id ON time_tables USING btree (calendar_id);


--
-- Name: index_time_tables_on_created_from_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_time_tables_on_created_from_id ON time_tables USING btree (created_from_id);


--
-- Name: index_time_tables_vehicle_journeys_on_time_table_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_time_tables_vehicle_journeys_on_time_table_id ON time_tables_vehicle_journeys USING btree (time_table_id);


--
-- Name: index_time_tables_vehicle_journeys_on_vehicle_journey_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_time_tables_vehicle_journeys_on_vehicle_journey_id ON time_tables_vehicle_journeys USING btree (vehicle_journey_id);


--
-- Name: index_users_on_email; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX index_users_on_email ON users USING btree (email);


--
-- Name: index_users_on_invitation_token; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX index_users_on_invitation_token ON users USING btree (invitation_token);


--
-- Name: index_users_on_reset_password_token; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX index_users_on_reset_password_token ON users USING btree (reset_password_token);


--
-- Name: index_users_on_username; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX index_users_on_username ON users USING btree (username);


--
-- Name: index_vehicle_journey_at_stops_on_stop_pointid; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_vehicle_journey_at_stops_on_stop_pointid ON vehicle_journey_at_stops USING btree (stop_point_id);


--
-- Name: index_vehicle_journey_at_stops_on_vehicle_journey_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_vehicle_journey_at_stops_on_vehicle_journey_id ON vehicle_journey_at_stops USING btree (vehicle_journey_id);


--
-- Name: index_vehicle_journeys_on_route_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_vehicle_journeys_on_route_id ON vehicle_journeys USING btree (route_id);


--
-- Name: index_workbenches_on_line_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_workbenches_on_line_referential_id ON workbenches USING btree (line_referential_id);


--
-- Name: index_workbenches_on_organisation_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_workbenches_on_organisation_id ON workbenches USING btree (organisation_id);


--
-- Name: index_workbenches_on_stop_area_referential_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX index_workbenches_on_stop_area_referential_id ON workbenches USING btree (stop_area_referential_id);


--
-- Name: journey_patterns_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX journey_patterns_objectid_key ON journey_patterns USING btree (objectid);


--
-- Name: line_referential_sync_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX line_referential_sync_id ON line_referential_sync_messages USING btree (line_referential_sync_id);


--
-- Name: lines_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX lines_objectid_key ON lines USING btree (objectid);


--
-- Name: lines_registration_number_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX lines_registration_number_key ON lines USING btree (registration_number);


--
-- Name: networks_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX networks_objectid_key ON networks USING btree (objectid);


--
-- Name: networks_registration_number_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX networks_registration_number_key ON networks USING btree (registration_number);


--
-- Name: pt_links_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX pt_links_objectid_key ON pt_links USING btree (objectid);


--
-- Name: routes_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX routes_objectid_key ON routes USING btree (objectid);


--
-- Name: stop_area_referential_sync_id; Type: INDEX; Schema: public; Owner: chouette
--

CREATE INDEX stop_area_referential_sync_id ON stop_area_referential_sync_messages USING btree (stop_area_referential_sync_id);


--
-- Name: stop_areas_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX stop_areas_objectid_key ON stop_areas USING btree (objectid);


--
-- Name: stop_points_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX stop_points_objectid_key ON stop_points USING btree (objectid);


--
-- Name: taggings_idx; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX taggings_idx ON taggings USING btree (tag_id, taggable_id, taggable_type, context, tagger_id, tagger_type);


--
-- Name: time_tables_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX time_tables_objectid_key ON time_tables USING btree (objectid);


--
-- Name: unique_schema_migrations; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX unique_schema_migrations ON schema_migrations USING btree (version);


--
-- Name: vehicle_journeys_objectid_key; Type: INDEX; Schema: public; Owner: chouette
--

CREATE UNIQUE INDEX vehicle_journeys_objectid_key ON vehicle_journeys USING btree (objectid);


--
-- Name: aclk_acpt_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY access_links
    ADD CONSTRAINT aclk_acpt_fkey FOREIGN KEY (access_point_id) REFERENCES access_points(id);


--
-- Name: area_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_areas
    ADD CONSTRAINT area_parent_fkey FOREIGN KEY (parent_id) REFERENCES stop_areas(id) ON DELETE SET NULL;


--
-- Name: arrival_point_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT arrival_point_fkey FOREIGN KEY (arrival_stop_point_id) REFERENCES stop_points(id) ON DELETE SET NULL;


--
-- Name: departure_point_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT departure_point_fkey FOREIGN KEY (departure_stop_point_id) REFERENCES stop_points(id) ON DELETE SET NULL;


--
-- Name: fk_rails_0dbc726f14; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_pattern_sections
    ADD CONSTRAINT fk_rails_0dbc726f14 FOREIGN KEY (route_section_id) REFERENCES route_sections(id) ON DELETE CASCADE;


--
-- Name: fk_rails_60bb6f7bd3; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT fk_rails_60bb6f7bd3 FOREIGN KEY (timeband_id) REFERENCES timebands(id) ON DELETE SET NULL;


--
-- Name: fk_rails_65d1354ca5; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY referentials
    ADD CONSTRAINT fk_rails_65d1354ca5 FOREIGN KEY (referential_suite_id) REFERENCES referential_suites(id);


--
-- Name: fk_rails_73ae46b20f; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_pattern_sections
    ADD CONSTRAINT fk_rails_73ae46b20f FOREIGN KEY (journey_pattern_id) REFERENCES journey_patterns(id) ON DELETE CASCADE;


--
-- Name: fk_rails_7561c6e512; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY api_keys
    ADD CONSTRAINT fk_rails_7561c6e512 FOREIGN KEY (organisation_id) REFERENCES organisations(id);


--
-- Name: fk_rails_97b8dcfe1a; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY route_sections
    ADD CONSTRAINT fk_rails_97b8dcfe1a FOREIGN KEY (departure_id) REFERENCES stop_areas(id);


--
-- Name: fk_rails_d322c5d659; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_frequencies
    ADD CONSTRAINT fk_rails_d322c5d659 FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE SET NULL;


--
-- Name: fk_rails_df1612606f; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY route_sections
    ADD CONSTRAINT fk_rails_df1612606f FOREIGN KEY (arrival_id) REFERENCES stop_areas(id);


--
-- Name: groupofline_group_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY group_of_lines_lines
    ADD CONSTRAINT groupofline_group_fkey FOREIGN KEY (group_of_line_id) REFERENCES group_of_lines(id) ON DELETE CASCADE;


--
-- Name: jp_route_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_patterns
    ADD CONSTRAINT jp_route_fkey FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE;


--
-- Name: jpsp_jp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_patterns_stop_points
    ADD CONSTRAINT jpsp_jp_fkey FOREIGN KEY (journey_pattern_id) REFERENCES journey_patterns(id) ON DELETE CASCADE;


--
-- Name: jpsp_stoppoint_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY journey_patterns_stop_points
    ADD CONSTRAINT jpsp_stoppoint_fkey FOREIGN KEY (stop_point_id) REFERENCES stop_points(id) ON DELETE CASCADE;


--
-- Name: route_opposite_route_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY routes
    ADD CONSTRAINT route_opposite_route_fkey FOREIGN KEY (opposite_route_id) REFERENCES routes(id);


--
-- Name: stoparea_child_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_areas_stop_areas
    ADD CONSTRAINT stoparea_child_fkey FOREIGN KEY (child_id) REFERENCES stop_areas(id) ON DELETE CASCADE;


--
-- Name: stoparea_parent_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY stop_areas_stop_areas
    ADD CONSTRAINT stoparea_parent_fkey FOREIGN KEY (parent_id) REFERENCES stop_areas(id) ON DELETE CASCADE;


--
-- Name: tm_date_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY time_table_dates
    ADD CONSTRAINT tm_date_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;


--
-- Name: tm_period_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY time_table_periods
    ADD CONSTRAINT tm_period_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;


--
-- Name: vj_jp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vj_jp_fkey FOREIGN KEY (journey_pattern_id) REFERENCES journey_patterns(id) ON DELETE CASCADE;


--
-- Name: vj_route_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY vehicle_journeys
    ADD CONSTRAINT vj_route_fkey FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE;


--
-- Name: vjas_sp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vjas_sp_fkey FOREIGN KEY (stop_point_id) REFERENCES stop_points(id) ON DELETE CASCADE;


--
-- Name: vjas_vj_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY vehicle_journey_at_stops
    ADD CONSTRAINT vjas_vj_fkey FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE CASCADE;


--
-- Name: vjtm_tm_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY time_tables_vehicle_journeys
    ADD CONSTRAINT vjtm_tm_fkey FOREIGN KEY (time_table_id) REFERENCES time_tables(id) ON DELETE CASCADE;


--
-- Name: vjtm_vj_fkey; Type: FK CONSTRAINT; Schema: public; Owner: chouette
--

ALTER TABLE ONLY time_tables_vehicle_journeys
    ADD CONSTRAINT vjtm_vj_fkey FOREIGN KEY (vehicle_journey_id) REFERENCES vehicle_journeys(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

