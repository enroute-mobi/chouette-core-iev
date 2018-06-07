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
CREATE TABLE public.access_links (
    id bigint NOT NULL,
    access_point_id bigint,
    stop_area_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
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
    updated_at timestamp without time zone,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.access_links OWNER TO chouette;
CREATE SEQUENCE public.access_links_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.access_links_id_seq OWNER TO chouette;
ALTER SEQUENCE public.access_links_id_seq OWNED BY public.access_links.id;
CREATE TABLE public.access_points (
    id bigint NOT NULL,
    objectid character varying,
    object_version bigint,
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
    updated_at timestamp without time zone,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.access_points OWNER TO chouette;
CREATE SEQUENCE public.access_points_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.access_points_id_seq OWNER TO chouette;
ALTER SEQUENCE public.access_points_id_seq OWNED BY public.access_points.id;
CREATE TABLE public.api_keys (
    id bigint NOT NULL,
    referential_id bigint,
    token character varying,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    organisation_id bigint,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.api_keys OWNER TO chouette;
CREATE SEQUENCE public.api_keys_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.api_keys_id_seq OWNER TO chouette;
ALTER SEQUENCE public.api_keys_id_seq OWNED BY public.api_keys.id;
CREATE TABLE public.calendars (
    id bigint NOT NULL,
    name character varying,
    date_ranges daterange[],
    dates date[],
    shared boolean DEFAULT false,
    organisation_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    workgroup_id bigint,
    int_day_types integer,
    excluded_dates date[],
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.calendars OWNER TO chouette;
CREATE SEQUENCE public.calendars_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.calendars_id_seq OWNER TO chouette;
ALTER SEQUENCE public.calendars_id_seq OWNED BY public.calendars.id;
CREATE TABLE public.clean_up_results (
    id bigint NOT NULL,
    message_key character varying,
    message_attributes shared_extensions.hstore,
    clean_up_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE public.clean_up_results OWNER TO chouette;
CREATE SEQUENCE public.clean_up_results_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.clean_up_results_id_seq OWNER TO chouette;
ALTER SEQUENCE public.clean_up_results_id_seq OWNED BY public.clean_up_results.id;
CREATE TABLE public.clean_ups (
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
ALTER TABLE public.clean_ups OWNER TO chouette;
CREATE SEQUENCE public.clean_ups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.clean_ups_id_seq OWNER TO chouette;
ALTER SEQUENCE public.clean_ups_id_seq OWNED BY public.clean_ups.id;
CREATE TABLE public.companies (
    id bigint NOT NULL,
    objectid character varying NOT NULL,
    object_version bigint,
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
    updated_at timestamp without time zone,
    custom_field_values jsonb DEFAULT '{}'::jsonb,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.companies OWNER TO chouette;
CREATE SEQUENCE public.companies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.companies_id_seq OWNER TO chouette;
ALTER SEQUENCE public.companies_id_seq OWNED BY public.companies.id;
CREATE TABLE public.compliance_control_blocks (
    id bigint NOT NULL,
    name character varying,
    condition_attributes shared_extensions.hstore,
    compliance_control_set_id bigint,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);
ALTER TABLE public.compliance_control_blocks OWNER TO chouette;
CREATE SEQUENCE public.compliance_control_blocks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.compliance_control_blocks_id_seq OWNER TO chouette;
ALTER SEQUENCE public.compliance_control_blocks_id_seq OWNED BY public.compliance_control_blocks.id;
CREATE TABLE public.compliance_control_sets (
    id bigint NOT NULL,
    name character varying,
    organisation_id bigint,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.compliance_control_sets OWNER TO chouette;
CREATE SEQUENCE public.compliance_control_sets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.compliance_control_sets_id_seq OWNER TO chouette;
ALTER SEQUENCE public.compliance_control_sets_id_seq OWNED BY public.compliance_control_sets.id;
CREATE TABLE public.compliance_controls (
    id bigint NOT NULL,
    compliance_control_set_id bigint,
    type character varying,
    control_attributes json,
    name character varying,
    code character varying,
    criticity character varying,
    comment text,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    origin_code character varying,
    compliance_control_block_id bigint
);
ALTER TABLE public.compliance_controls OWNER TO chouette;
CREATE SEQUENCE public.compliance_controls_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.compliance_controls_id_seq OWNER TO chouette;
ALTER SEQUENCE public.compliance_controls_id_seq OWNED BY public.compliance_controls.id;
CREATE TABLE public.connection_links (
    id bigint NOT NULL,
    departure_id bigint,
    arrival_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
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
    updated_at timestamp without time zone,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.connection_links OWNER TO chouette;
CREATE SEQUENCE public.connection_links_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.connection_links_id_seq OWNER TO chouette;
ALTER SEQUENCE public.connection_links_id_seq OWNED BY public.connection_links.id;
CREATE TABLE public.custom_fields (
    id bigint NOT NULL,
    code character varying,
    resource_type character varying,
    name character varying,
    field_type character varying,
    options json,
    workgroup_id bigint,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);
ALTER TABLE public.custom_fields OWNER TO chouette;
CREATE SEQUENCE public.custom_fields_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.custom_fields_id_seq OWNER TO chouette;
ALTER SEQUENCE public.custom_fields_id_seq OWNED BY public.custom_fields.id;
CREATE TABLE public.facilities (
    id bigint NOT NULL,
    stop_area_id bigint,
    line_id bigint,
    connection_link_id bigint,
    stop_point_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    creation_time timestamp without time zone,
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
ALTER TABLE public.facilities OWNER TO chouette;
CREATE TABLE public.facilities_features (
    facility_id bigint,
    choice_code integer
);
ALTER TABLE public.facilities_features OWNER TO chouette;
CREATE SEQUENCE public.facilities_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.facilities_id_seq OWNER TO chouette;
ALTER SEQUENCE public.facilities_id_seq OWNED BY public.facilities.id;
CREATE TABLE public.footnotes (
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
ALTER TABLE public.footnotes OWNER TO chouette;
CREATE SEQUENCE public.footnotes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.footnotes_id_seq OWNER TO chouette;
ALTER SEQUENCE public.footnotes_id_seq OWNED BY public.footnotes.id;
CREATE TABLE public.footnotes_vehicle_journeys (
    vehicle_journey_id bigint,
    footnote_id bigint
);
ALTER TABLE public.footnotes_vehicle_journeys OWNER TO chouette;
CREATE TABLE public.group_of_lines (
    id bigint NOT NULL,
    objectid character varying NOT NULL,
    object_version bigint,
    name character varying,
    comment character varying,
    registration_number character varying,
    line_referential_id bigint,
    import_xml text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.group_of_lines OWNER TO chouette;
CREATE SEQUENCE public.group_of_lines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.group_of_lines_id_seq OWNER TO chouette;
ALTER SEQUENCE public.group_of_lines_id_seq OWNED BY public.group_of_lines.id;
CREATE TABLE public.group_of_lines_lines (
    group_of_line_id bigint,
    line_id bigint
);
ALTER TABLE public.group_of_lines_lines OWNER TO chouette;
CREATE TABLE public.journey_frequencies (
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
ALTER TABLE public.journey_frequencies OWNER TO chouette;
CREATE SEQUENCE public.journey_frequencies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.journey_frequencies_id_seq OWNER TO chouette;
ALTER SEQUENCE public.journey_frequencies_id_seq OWNED BY public.journey_frequencies.id;
CREATE TABLE public.journey_patterns (
    id bigint NOT NULL,
    route_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    name character varying,
    comment character varying,
    registration_number character varying,
    published_name character varying,
    departure_stop_point_id bigint,
    arrival_stop_point_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    checksum character varying,
    checksum_source text,
    data_source_ref character varying,
    costs json,
    metadata jsonb DEFAULT '{}'::jsonb,
    custom_field_values jsonb
);
ALTER TABLE public.journey_patterns OWNER TO chouette;
CREATE SEQUENCE public.journey_patterns_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.journey_patterns_id_seq OWNER TO chouette;
ALTER SEQUENCE public.journey_patterns_id_seq OWNED BY public.journey_patterns.id;
CREATE TABLE public.journey_patterns_stop_points (
    journey_pattern_id bigint,
    stop_point_id bigint
);
ALTER TABLE public.journey_patterns_stop_points OWNER TO chouette;
CREATE TABLE public.line_referential_memberships (
    id bigint NOT NULL,
    organisation_id bigint,
    line_referential_id bigint,
    owner boolean
);
ALTER TABLE public.line_referential_memberships OWNER TO chouette;
CREATE SEQUENCE public.line_referential_memberships_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.line_referential_memberships_id_seq OWNER TO chouette;
ALTER SEQUENCE public.line_referential_memberships_id_seq OWNED BY public.line_referential_memberships.id;
CREATE TABLE public.line_referential_sync_messages (
    id bigint NOT NULL,
    criticity integer,
    message_key character varying,
    message_attributes shared_extensions.hstore,
    line_referential_sync_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE public.line_referential_sync_messages OWNER TO chouette;
CREATE SEQUENCE public.line_referential_sync_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.line_referential_sync_messages_id_seq OWNER TO chouette;
ALTER SEQUENCE public.line_referential_sync_messages_id_seq OWNED BY public.line_referential_sync_messages.id;
CREATE TABLE public.line_referential_syncs (
    id bigint NOT NULL,
    line_referential_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    started_at timestamp without time zone,
    ended_at timestamp without time zone,
    status character varying
);
ALTER TABLE public.line_referential_syncs OWNER TO chouette;
CREATE SEQUENCE public.line_referential_syncs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.line_referential_syncs_id_seq OWNER TO chouette;
ALTER SEQUENCE public.line_referential_syncs_id_seq OWNED BY public.line_referential_syncs.id;
CREATE TABLE public.line_referentials (
    id bigint NOT NULL,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    sync_interval integer DEFAULT 1,
    objectid_format character varying
);
ALTER TABLE public.line_referentials OWNER TO chouette;
CREATE SEQUENCE public.line_referentials_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.line_referentials_id_seq OWNER TO chouette;
ALTER SEQUENCE public.line_referentials_id_seq OWNED BY public.line_referentials.id;
CREATE TABLE public.lines (
    id bigint NOT NULL,
    network_id bigint,
    company_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
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
    seasonal boolean,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.lines OWNER TO chouette;
CREATE SEQUENCE public.lines_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.lines_id_seq OWNER TO chouette;
ALTER SEQUENCE public.lines_id_seq OWNED BY public.lines.id;
CREATE TABLE public.merges (
    id bigint NOT NULL,
    workbench_id bigint,
    referential_ids bigint[],
    creator character varying,
    status character varying,
    started_at timestamp without time zone,
    ended_at timestamp without time zone,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);
ALTER TABLE public.merges OWNER TO chouette;
CREATE SEQUENCE public.merges_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.merges_id_seq OWNER TO chouette;
ALTER SEQUENCE public.merges_id_seq OWNED BY public.merges.id;
CREATE TABLE public.networks (
    id bigint NOT NULL,
    objectid character varying NOT NULL,
    object_version bigint,
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
    updated_at timestamp without time zone,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.networks OWNER TO chouette;
CREATE SEQUENCE public.networks_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.networks_id_seq OWNER TO chouette;
ALTER SEQUENCE public.networks_id_seq OWNED BY public.networks.id;
CREATE TABLE public.organisations (
    id bigint NOT NULL,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    data_format character varying DEFAULT 'neptune'::character varying,
    code character varying,
    synced_at timestamp without time zone,
    sso_attributes shared_extensions.hstore,
    custom_view character varying,
    features character varying[] DEFAULT '{}'::character varying[]
);
ALTER TABLE public.organisations OWNER TO chouette;
CREATE SEQUENCE public.organisations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.organisations_id_seq OWNER TO chouette;
ALTER SEQUENCE public.organisations_id_seq OWNED BY public.organisations.id;
CREATE TABLE public.pt_links (
    id bigint NOT NULL,
    start_of_link_id bigint,
    end_of_link_id bigint,
    route_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    name character varying,
    comment character varying,
    link_distance numeric(19,2),
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.pt_links OWNER TO chouette;
CREATE SEQUENCE public.pt_links_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.pt_links_id_seq OWNER TO chouette;
ALTER SEQUENCE public.pt_links_id_seq OWNED BY public.pt_links.id;
CREATE TABLE public.referential_clonings (
    id bigint NOT NULL,
    status character varying,
    started_at timestamp without time zone,
    ended_at timestamp without time zone,
    source_referential_id bigint,
    target_referential_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE public.referential_clonings OWNER TO chouette;
CREATE SEQUENCE public.referential_clonings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.referential_clonings_id_seq OWNER TO chouette;
ALTER SEQUENCE public.referential_clonings_id_seq OWNED BY public.referential_clonings.id;
CREATE TABLE public.referential_metadata (
    id bigint NOT NULL,
    referential_id bigint,
    line_ids bigint[],
    referential_source_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    periodes daterange[]
);
ALTER TABLE public.referential_metadata OWNER TO chouette;
CREATE SEQUENCE public.referential_metadata_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.referential_metadata_id_seq OWNER TO chouette;
ALTER SEQUENCE public.referential_metadata_id_seq OWNED BY public.referential_metadata.id;
CREATE TABLE public.referential_suites (
    id bigint NOT NULL,
    new_id bigint,
    current_id bigint,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL
);
ALTER TABLE public.referential_suites OWNER TO chouette;
CREATE SEQUENCE public.referential_suites_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.referential_suites_id_seq OWNER TO chouette;
ALTER SEQUENCE public.referential_suites_id_seq OWNED BY public.referential_suites.id;
CREATE TABLE public.referentials (
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
    referential_suite_id bigint,
    objectid_format character varying,
    merged_at timestamp without time zone,
    failed_at timestamp without time zone
);
ALTER TABLE public.referentials OWNER TO chouette;
CREATE SEQUENCE public.referentials_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.referentials_id_seq OWNER TO chouette;
ALTER SEQUENCE public.referentials_id_seq OWNED BY public.referentials.id;
CREATE TABLE public.routes (
    id bigint NOT NULL,
    line_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
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
    data_source_ref character varying,
    costs json,
    metadata jsonb
);
ALTER TABLE public.routes OWNER TO chouette;
CREATE SEQUENCE public.routes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.routes_id_seq OWNER TO chouette;
ALTER SEQUENCE public.routes_id_seq OWNED BY public.routes.id;
CREATE TABLE public.routing_constraint_zones (
    id bigint NOT NULL,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    objectid character varying NOT NULL,
    object_version bigint,
    route_id bigint,
    stop_point_ids bigint[],
    checksum character varying,
    checksum_source text,
    data_source_ref character varying,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.routing_constraint_zones OWNER TO chouette;
CREATE SEQUENCE public.routing_constraint_zones_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.routing_constraint_zones_id_seq OWNER TO chouette;
ALTER SEQUENCE public.routing_constraint_zones_id_seq OWNED BY public.routing_constraint_zones.id;
CREATE TABLE public.routing_constraints_lines (
    stop_area_id bigint,
    line_id bigint
);
ALTER TABLE public.routing_constraints_lines OWNER TO chouette;
CREATE TABLE public.schema_migrations (
    version character varying NOT NULL
);
ALTER TABLE public.schema_migrations OWNER TO chouette;
CREATE TABLE public.stop_area_referential_memberships (
    id bigint NOT NULL,
    organisation_id bigint,
    stop_area_referential_id bigint,
    owner boolean
);
ALTER TABLE public.stop_area_referential_memberships OWNER TO chouette;
CREATE SEQUENCE public.stop_area_referential_memberships_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.stop_area_referential_memberships_id_seq OWNER TO chouette;
ALTER SEQUENCE public.stop_area_referential_memberships_id_seq OWNED BY public.stop_area_referential_memberships.id;
CREATE TABLE public.stop_area_referential_sync_messages (
    id bigint NOT NULL,
    criticity integer,
    message_key character varying,
    message_attributes shared_extensions.hstore,
    stop_area_referential_sync_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);
ALTER TABLE public.stop_area_referential_sync_messages OWNER TO chouette;
CREATE SEQUENCE public.stop_area_referential_sync_messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.stop_area_referential_sync_messages_id_seq OWNER TO chouette;
ALTER SEQUENCE public.stop_area_referential_sync_messages_id_seq OWNED BY public.stop_area_referential_sync_messages.id;
CREATE TABLE public.stop_area_referential_syncs (
    id bigint NOT NULL,
    stop_area_referential_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    ended_at timestamp without time zone,
    started_at timestamp without time zone,
    status character varying
);
ALTER TABLE public.stop_area_referential_syncs OWNER TO chouette;
CREATE SEQUENCE public.stop_area_referential_syncs_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.stop_area_referential_syncs_id_seq OWNER TO chouette;
ALTER SEQUENCE public.stop_area_referential_syncs_id_seq OWNED BY public.stop_area_referential_syncs.id;
CREATE TABLE public.stop_area_referentials (
    id bigint NOT NULL,
    name character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    objectid_format character varying,
    registration_number_format character varying
);
ALTER TABLE public.stop_area_referentials OWNER TO chouette;
CREATE SEQUENCE public.stop_area_referentials_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.stop_area_referentials_id_seq OWNER TO chouette;
ALTER SEQUENCE public.stop_area_referentials_id_seq OWNED BY public.stop_area_referentials.id;
CREATE TABLE public.stop_areas (
    id bigint NOT NULL,
    parent_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
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
    stif_type character varying,
    waiting_time integer,
    kind character varying,
    localized_names jsonb,
    confirmed_at timestamp without time zone,
    custom_field_values jsonb,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.stop_areas OWNER TO chouette;
CREATE SEQUENCE public.stop_areas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.stop_areas_id_seq OWNER TO chouette;
ALTER SEQUENCE public.stop_areas_id_seq OWNED BY public.stop_areas.id;
CREATE TABLE public.stop_areas_stop_areas (
    child_id bigint,
    parent_id bigint
);
ALTER TABLE public.stop_areas_stop_areas OWNER TO chouette;
CREATE TABLE public.stop_points (
    id bigint NOT NULL,
    route_id bigint,
    stop_area_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    "position" integer,
    for_boarding character varying,
    for_alighting character varying,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.stop_points OWNER TO chouette;
CREATE SEQUENCE public.stop_points_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.stop_points_id_seq OWNER TO chouette;
ALTER SEQUENCE public.stop_points_id_seq OWNED BY public.stop_points.id;
CREATE TABLE public.taggings (
    id bigint NOT NULL,
    tag_id bigint,
    taggable_id bigint,
    taggable_type character varying,
    tagger_id bigint,
    tagger_type character varying,
    context character varying(128),
    created_at timestamp without time zone
);
ALTER TABLE public.taggings OWNER TO chouette;
CREATE SEQUENCE public.taggings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.taggings_id_seq OWNER TO chouette;
ALTER SEQUENCE public.taggings_id_seq OWNED BY public.taggings.id;
CREATE TABLE public.tags (
    id bigint NOT NULL,
    name character varying,
    taggings_count integer DEFAULT 0
);
ALTER TABLE public.tags OWNER TO chouette;
CREATE SEQUENCE public.tags_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.tags_id_seq OWNER TO chouette;
ALTER SEQUENCE public.tags_id_seq OWNED BY public.tags.id;
CREATE TABLE public.time_table_dates (
    time_table_id bigint NOT NULL,
    date date,
    "position" integer NOT NULL,
    id bigint NOT NULL,
    in_out boolean,
    checksum character varying,
    checksum_source text
);
ALTER TABLE public.time_table_dates OWNER TO chouette;
CREATE SEQUENCE public.time_table_dates_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.time_table_dates_id_seq OWNER TO chouette;
ALTER SEQUENCE public.time_table_dates_id_seq OWNED BY public.time_table_dates.id;
CREATE TABLE public.time_table_periods (
    time_table_id bigint NOT NULL,
    period_start date,
    period_end date,
    "position" integer NOT NULL,
    id bigint NOT NULL,
    checksum character varying,
    checksum_source text
);
ALTER TABLE public.time_table_periods OWNER TO chouette;
CREATE SEQUENCE public.time_table_periods_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.time_table_periods_id_seq OWNER TO chouette;
ALTER SEQUENCE public.time_table_periods_id_seq OWNED BY public.time_table_periods.id;
CREATE TABLE public.time_tables (
    id bigint NOT NULL,
    objectid character varying NOT NULL,
    object_version bigint DEFAULT 1,
    version character varying,
    comment character varying,
    int_day_types integer DEFAULT 0,
    start_date date,
    end_date date,
    calendar_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    color character varying,
    created_from_id bigint,
    checksum character varying,
    checksum_source text,
    data_source_ref character varying,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.time_tables OWNER TO chouette;
CREATE SEQUENCE public.time_tables_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.time_tables_id_seq OWNER TO chouette;
ALTER SEQUENCE public.time_tables_id_seq OWNED BY public.time_tables.id;
CREATE TABLE public.time_tables_vehicle_journeys (
    time_table_id bigint,
    vehicle_journey_id bigint
);
ALTER TABLE public.time_tables_vehicle_journeys OWNER TO chouette;
CREATE TABLE public.timebands (
    id bigint NOT NULL,
    objectid character varying NOT NULL,
    object_version bigint,
    name character varying,
    start_time time without time zone NOT NULL,
    end_time time without time zone NOT NULL,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    metadata jsonb DEFAULT '{}'::jsonb
);
ALTER TABLE public.timebands OWNER TO chouette;
CREATE SEQUENCE public.timebands_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.timebands_id_seq OWNER TO chouette;
ALTER SEQUENCE public.timebands_id_seq OWNED BY public.timebands.id;
CREATE TABLE public.users (
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
ALTER TABLE public.users OWNER TO chouette;
CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.users_id_seq OWNER TO chouette;
ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;
CREATE TABLE public.vehicle_journey_at_stops (
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
ALTER TABLE public.vehicle_journey_at_stops OWNER TO chouette;
CREATE SEQUENCE public.vehicle_journey_at_stops_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.vehicle_journey_at_stops_id_seq OWNER TO chouette;
ALTER SEQUENCE public.vehicle_journey_at_stops_id_seq OWNED BY public.vehicle_journey_at_stops.id;
CREATE TABLE public.vehicle_journeys (
    id bigint NOT NULL,
    route_id bigint,
    journey_pattern_id bigint,
    company_id bigint,
    objectid character varying NOT NULL,
    object_version bigint,
    comment character varying,
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
    data_source_ref character varying,
    custom_field_values jsonb DEFAULT '{}'::jsonb,
    metadata jsonb DEFAULT '{}'::jsonb,
    ignored_routing_contraint_zone_ids integer[] DEFAULT '{}'::integer[]
);
ALTER TABLE public.vehicle_journeys OWNER TO chouette;
CREATE SEQUENCE public.vehicle_journeys_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.vehicle_journeys_id_seq OWNER TO chouette;
ALTER SEQUENCE public.vehicle_journeys_id_seq OWNED BY public.vehicle_journeys.id;
CREATE TABLE public.workbenches (
    id bigint NOT NULL,
    name character varying,
    organisation_id bigint,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    line_referential_id bigint,
    stop_area_referential_id bigint,
    output_id bigint,
    objectid_format character varying,
    workgroup_id bigint,
    owner_compliance_control_set_ids shared_extensions.hstore
);
ALTER TABLE public.workbenches OWNER TO chouette;
CREATE SEQUENCE public.workbenches_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.workbenches_id_seq OWNER TO chouette;
ALTER SEQUENCE public.workbenches_id_seq OWNED BY public.workbenches.id;
CREATE TABLE public.workgroups (
    id bigint NOT NULL,
    name character varying,
    line_referential_id bigint,
    stop_area_referential_id bigint,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    import_types character varying[] DEFAULT '{}'::character varying[],
    export_types character varying[] DEFAULT '{}'::character varying[],
    owner_id bigint
);
ALTER TABLE public.workgroups OWNER TO chouette;
CREATE SEQUENCE public.workgroups_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
ALTER TABLE public.workgroups_id_seq OWNER TO chouette;
ALTER SEQUENCE public.workgroups_id_seq OWNED BY public.workgroups.id;
ALTER TABLE ONLY public.access_links ALTER COLUMN id SET DEFAULT nextval('public.access_links_id_seq'::regclass);
ALTER TABLE ONLY public.access_points ALTER COLUMN id SET DEFAULT nextval('public.access_points_id_seq'::regclass);
ALTER TABLE ONLY public.api_keys ALTER COLUMN id SET DEFAULT nextval('public.api_keys_id_seq'::regclass);
ALTER TABLE ONLY public.calendars ALTER COLUMN id SET DEFAULT nextval('public.calendars_id_seq'::regclass);
ALTER TABLE ONLY public.clean_up_results ALTER COLUMN id SET DEFAULT nextval('public.clean_up_results_id_seq'::regclass);
ALTER TABLE ONLY public.clean_ups ALTER COLUMN id SET DEFAULT nextval('public.clean_ups_id_seq'::regclass);
ALTER TABLE ONLY public.companies ALTER COLUMN id SET DEFAULT nextval('public.companies_id_seq'::regclass);
ALTER TABLE ONLY public.compliance_control_blocks ALTER COLUMN id SET DEFAULT nextval('public.compliance_control_blocks_id_seq'::regclass);
ALTER TABLE ONLY public.compliance_control_sets ALTER COLUMN id SET DEFAULT nextval('public.compliance_control_sets_id_seq'::regclass);
ALTER TABLE ONLY public.compliance_controls ALTER COLUMN id SET DEFAULT nextval('public.compliance_controls_id_seq'::regclass);
ALTER TABLE ONLY public.connection_links ALTER COLUMN id SET DEFAULT nextval('public.connection_links_id_seq'::regclass);
ALTER TABLE ONLY public.custom_fields ALTER COLUMN id SET DEFAULT nextval('public.custom_fields_id_seq'::regclass);
ALTER TABLE ONLY public.facilities ALTER COLUMN id SET DEFAULT nextval('public.facilities_id_seq'::regclass);
ALTER TABLE ONLY public.footnotes ALTER COLUMN id SET DEFAULT nextval('public.footnotes_id_seq'::regclass);
ALTER TABLE ONLY public.group_of_lines ALTER COLUMN id SET DEFAULT nextval('public.group_of_lines_id_seq'::regclass);
ALTER TABLE ONLY public.journey_frequencies ALTER COLUMN id SET DEFAULT nextval('public.journey_frequencies_id_seq'::regclass);
ALTER TABLE ONLY public.journey_patterns ALTER COLUMN id SET DEFAULT nextval('public.journey_patterns_id_seq'::regclass);
ALTER TABLE ONLY public.line_referential_memberships ALTER COLUMN id SET DEFAULT nextval('public.line_referential_memberships_id_seq'::regclass);
ALTER TABLE ONLY public.line_referential_sync_messages ALTER COLUMN id SET DEFAULT nextval('public.line_referential_sync_messages_id_seq'::regclass);
ALTER TABLE ONLY public.line_referential_syncs ALTER COLUMN id SET DEFAULT nextval('public.line_referential_syncs_id_seq'::regclass);
ALTER TABLE ONLY public.line_referentials ALTER COLUMN id SET DEFAULT nextval('public.line_referentials_id_seq'::regclass);
ALTER TABLE ONLY public.lines ALTER COLUMN id SET DEFAULT nextval('public.lines_id_seq'::regclass);
ALTER TABLE ONLY public.merges ALTER COLUMN id SET DEFAULT nextval('public.merges_id_seq'::regclass);
ALTER TABLE ONLY public.networks ALTER COLUMN id SET DEFAULT nextval('public.networks_id_seq'::regclass);
ALTER TABLE ONLY public.organisations ALTER COLUMN id SET DEFAULT nextval('public.organisations_id_seq'::regclass);
ALTER TABLE ONLY public.pt_links ALTER COLUMN id SET DEFAULT nextval('public.pt_links_id_seq'::regclass);
ALTER TABLE ONLY public.referential_clonings ALTER COLUMN id SET DEFAULT nextval('public.referential_clonings_id_seq'::regclass);
ALTER TABLE ONLY public.referential_metadata ALTER COLUMN id SET DEFAULT nextval('public.referential_metadata_id_seq'::regclass);
ALTER TABLE ONLY public.referential_suites ALTER COLUMN id SET DEFAULT nextval('public.referential_suites_id_seq'::regclass);
ALTER TABLE ONLY public.referentials ALTER COLUMN id SET DEFAULT nextval('public.referentials_id_seq'::regclass);
ALTER TABLE ONLY public.routes ALTER COLUMN id SET DEFAULT nextval('public.routes_id_seq'::regclass);
ALTER TABLE ONLY public.routing_constraint_zones ALTER COLUMN id SET DEFAULT nextval('public.routing_constraint_zones_id_seq'::regclass);
ALTER TABLE ONLY public.stop_area_referential_memberships ALTER COLUMN id SET DEFAULT nextval('public.stop_area_referential_memberships_id_seq'::regclass);
ALTER TABLE ONLY public.stop_area_referential_sync_messages ALTER COLUMN id SET DEFAULT nextval('public.stop_area_referential_sync_messages_id_seq'::regclass);
ALTER TABLE ONLY public.stop_area_referential_syncs ALTER COLUMN id SET DEFAULT nextval('public.stop_area_referential_syncs_id_seq'::regclass);
ALTER TABLE ONLY public.stop_area_referentials ALTER COLUMN id SET DEFAULT nextval('public.stop_area_referentials_id_seq'::regclass);
ALTER TABLE ONLY public.stop_areas ALTER COLUMN id SET DEFAULT nextval('public.stop_areas_id_seq'::regclass);
ALTER TABLE ONLY public.stop_points ALTER COLUMN id SET DEFAULT nextval('public.stop_points_id_seq'::regclass);
ALTER TABLE ONLY public.taggings ALTER COLUMN id SET DEFAULT nextval('public.taggings_id_seq'::regclass);
ALTER TABLE ONLY public.tags ALTER COLUMN id SET DEFAULT nextval('public.tags_id_seq'::regclass);
ALTER TABLE ONLY public.time_table_dates ALTER COLUMN id SET DEFAULT nextval('public.time_table_dates_id_seq'::regclass);
ALTER TABLE ONLY public.time_table_periods ALTER COLUMN id SET DEFAULT nextval('public.time_table_periods_id_seq'::regclass);
ALTER TABLE ONLY public.time_tables ALTER COLUMN id SET DEFAULT nextval('public.time_tables_id_seq'::regclass);
ALTER TABLE ONLY public.timebands ALTER COLUMN id SET DEFAULT nextval('public.timebands_id_seq'::regclass);
ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);
ALTER TABLE ONLY public.vehicle_journey_at_stops ALTER COLUMN id SET DEFAULT nextval('public.vehicle_journey_at_stops_id_seq'::regclass);
ALTER TABLE ONLY public.vehicle_journeys ALTER COLUMN id SET DEFAULT nextval('public.vehicle_journeys_id_seq'::regclass);
ALTER TABLE ONLY public.workbenches ALTER COLUMN id SET DEFAULT nextval('public.workbenches_id_seq'::regclass);
ALTER TABLE ONLY public.workgroups ALTER COLUMN id SET DEFAULT nextval('public.workgroups_id_seq'::regclass);
ALTER TABLE ONLY public.access_links    ADD CONSTRAINT access_links_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.access_points    ADD CONSTRAINT access_points_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.api_keys    ADD CONSTRAINT api_keys_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.calendars    ADD CONSTRAINT calendars_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.clean_up_results    ADD CONSTRAINT clean_up_results_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.clean_ups    ADD CONSTRAINT clean_ups_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.companies    ADD CONSTRAINT companies_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.compliance_control_blocks    ADD CONSTRAINT compliance_control_blocks_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.compliance_control_sets    ADD CONSTRAINT compliance_control_sets_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.compliance_controls    ADD CONSTRAINT compliance_controls_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.connection_links    ADD CONSTRAINT connection_links_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.custom_fields    ADD CONSTRAINT custom_fields_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.facilities    ADD CONSTRAINT facilities_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.footnotes    ADD CONSTRAINT footnotes_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.group_of_lines    ADD CONSTRAINT group_of_lines_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.journey_frequencies    ADD CONSTRAINT journey_frequencies_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.journey_patterns    ADD CONSTRAINT journey_patterns_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.line_referential_memberships    ADD CONSTRAINT line_referential_memberships_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.line_referential_sync_messages    ADD CONSTRAINT line_referential_sync_messages_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.line_referential_syncs    ADD CONSTRAINT line_referential_syncs_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.line_referentials    ADD CONSTRAINT line_referentials_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.lines    ADD CONSTRAINT lines_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.merges    ADD CONSTRAINT merges_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.networks    ADD CONSTRAINT networks_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.organisations    ADD CONSTRAINT organisations_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.pt_links    ADD CONSTRAINT pt_links_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.referential_clonings    ADD CONSTRAINT referential_clonings_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.referential_metadata    ADD CONSTRAINT referential_metadata_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.referential_suites    ADD CONSTRAINT referential_suites_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.referentials    ADD CONSTRAINT referentials_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.routes    ADD CONSTRAINT routes_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.routing_constraint_zones    ADD CONSTRAINT routing_constraint_zones_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.stop_area_referential_memberships    ADD CONSTRAINT stop_area_referential_memberships_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.stop_area_referential_sync_messages    ADD CONSTRAINT stop_area_referential_sync_messages_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.stop_area_referential_syncs    ADD CONSTRAINT stop_area_referential_syncs_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.stop_area_referentials    ADD CONSTRAINT stop_area_referentials_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.stop_areas    ADD CONSTRAINT stop_areas_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.stop_points    ADD CONSTRAINT stop_points_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.taggings    ADD CONSTRAINT taggings_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.tags    ADD CONSTRAINT tags_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.time_table_dates    ADD CONSTRAINT time_table_dates_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.time_table_periods    ADD CONSTRAINT time_table_periods_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.time_tables    ADD CONSTRAINT time_tables_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.timebands    ADD CONSTRAINT timebands_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.users    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.vehicle_journey_at_stops    ADD CONSTRAINT vehicle_journey_at_stops_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.vehicle_journeys    ADD CONSTRAINT vehicle_journeys_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.workbenches    ADD CONSTRAINT workbenches_pkey PRIMARY KEY (id);
ALTER TABLE ONLY public.workgroups    ADD CONSTRAINT workgroups_pkey PRIMARY KEY (id);
CREATE UNIQUE INDEX access_links_objectid_key ON public.access_links USING btree (objectid);
CREATE UNIQUE INDEX access_points_objectid_key ON public.access_points USING btree (objectid);
CREATE UNIQUE INDEX companies_objectid_key ON public.companies USING btree (objectid);
CREATE INDEX companies_registration_number_key ON public.companies USING btree (registration_number);
CREATE UNIQUE INDEX connection_links_objectid_key ON public.connection_links USING btree (objectid);
CREATE UNIQUE INDEX facilities_objectid_key ON public.facilities USING btree (objectid);
CREATE UNIQUE INDEX group_of_lines_objectid_key ON public.group_of_lines USING btree (objectid);
CREATE INDEX index_api_keys_on_organisation_id ON public.api_keys USING btree (organisation_id);
CREATE INDEX index_calendars_on_organisation_id ON public.calendars USING btree (organisation_id);
CREATE INDEX index_calendars_on_workgroup_id ON public.calendars USING btree (workgroup_id);
CREATE INDEX index_clean_up_results_on_clean_up_id ON public.clean_up_results USING btree (clean_up_id);
CREATE INDEX index_clean_ups_on_referential_id ON public.clean_ups USING btree (referential_id);
CREATE INDEX index_companies_on_line_referential_id ON public.companies USING btree (line_referential_id);
CREATE INDEX index_companies_on_referential_id_and_registration_number ON public.companies USING btree (line_referential_id, registration_number);
CREATE INDEX index_compliance_control_blocks_on_compliance_control_set_id ON public.compliance_control_blocks USING btree (compliance_control_set_id);
CREATE INDEX index_compliance_control_sets_on_organisation_id ON public.compliance_control_sets USING btree (organisation_id);
CREATE UNIQUE INDEX index_compliance_controls_on_code_and_compliance_control_set_id ON public.compliance_controls USING btree (code, compliance_control_set_id);
CREATE INDEX index_compliance_controls_on_compliance_control_block_id ON public.compliance_controls USING btree (compliance_control_block_id);
CREATE INDEX index_compliance_controls_on_compliance_control_set_id ON public.compliance_controls USING btree (compliance_control_set_id);
CREATE INDEX index_custom_fields_on_resource_type ON public.custom_fields USING btree (resource_type);
CREATE INDEX index_group_of_lines_on_line_referential_id ON public.group_of_lines USING btree (line_referential_id);
CREATE INDEX index_journey_frequencies_on_timeband_id ON public.journey_frequencies USING btree (timeband_id);
CREATE INDEX index_journey_frequencies_on_vehicle_journey_id ON public.journey_frequencies USING btree (vehicle_journey_id);
CREATE INDEX index_journey_pattern_id_on_journey_patterns_stop_points ON public.journey_patterns_stop_points USING btree (journey_pattern_id);
CREATE INDEX index_line_referential_syncs_on_line_referential_id ON public.line_referential_syncs USING btree (line_referential_id);
CREATE INDEX index_lines_on_line_referential_id ON public.lines USING btree (line_referential_id);
CREATE INDEX index_lines_on_referential_id_and_registration_number ON public.lines USING btree (line_referential_id, registration_number);
CREATE INDEX index_lines_on_secondary_company_ids ON public.lines USING gin (secondary_company_ids);
CREATE INDEX index_merges_on_workbench_id ON public.merges USING btree (workbench_id);
CREATE INDEX index_networks_on_line_referential_id ON public.networks USING btree (line_referential_id);
CREATE UNIQUE INDEX index_organisations_on_code ON public.organisations USING btree (code);
CREATE INDEX index_referential_clonings_on_source_referential_id ON public.referential_clonings USING btree (source_referential_id);
CREATE INDEX index_referential_clonings_on_target_referential_id ON public.referential_clonings USING btree (target_referential_id);
CREATE INDEX index_referential_metadata_on_line_ids ON public.referential_metadata USING gin (line_ids);
CREATE INDEX index_referential_metadata_on_referential_id ON public.referential_metadata USING btree (referential_id);
CREATE INDEX index_referential_metadata_on_referential_source_id ON public.referential_metadata USING btree (referential_source_id);
CREATE INDEX index_referential_suites_on_current_id ON public.referential_suites USING btree (current_id);
CREATE INDEX index_referential_suites_on_new_id ON public.referential_suites USING btree (new_id);
CREATE INDEX index_referentials_on_created_from_id ON public.referentials USING btree (created_from_id);
CREATE INDEX index_referentials_on_referential_suite_id ON public.referentials USING btree (referential_suite_id);
CREATE INDEX index_stop_area_referential_syncs_on_stop_area_referential_id ON public.stop_area_referential_syncs USING btree (stop_area_referential_id);
CREATE INDEX index_stop_areas_on_name ON public.stop_areas USING btree (name);
CREATE INDEX index_stop_areas_on_parent_id ON public.stop_areas USING btree (parent_id);
CREATE INDEX index_stop_areas_on_referential_id_and_registration_number ON public.stop_areas USING btree (stop_area_referential_id, registration_number);
CREATE INDEX index_stop_areas_on_stop_area_referential_id ON public.stop_areas USING btree (stop_area_referential_id);
CREATE INDEX index_taggings_on_taggable_id_and_taggable_type_and_context ON public.taggings USING btree (taggable_id, taggable_type, context);
CREATE UNIQUE INDEX index_tags_on_name ON public.tags USING btree (name);
CREATE INDEX index_time_table_dates_on_time_table_id ON public.time_table_dates USING btree (time_table_id);
CREATE INDEX index_time_table_periods_on_time_table_id ON public.time_table_periods USING btree (time_table_id);
CREATE INDEX index_time_tables_on_calendar_id ON public.time_tables USING btree (calendar_id);
CREATE INDEX index_time_tables_on_created_from_id ON public.time_tables USING btree (created_from_id);
CREATE INDEX index_time_tables_vehicle_journeys_on_time_table_id ON public.time_tables_vehicle_journeys USING btree (time_table_id);
CREATE INDEX index_time_tables_vehicle_journeys_on_vehicle_journey_id ON public.time_tables_vehicle_journeys USING btree (vehicle_journey_id);
CREATE UNIQUE INDEX index_users_on_email ON public.users USING btree (email);
CREATE UNIQUE INDEX index_users_on_invitation_token ON public.users USING btree (invitation_token);
CREATE UNIQUE INDEX index_users_on_reset_password_token ON public.users USING btree (reset_password_token);
CREATE UNIQUE INDEX index_users_on_username ON public.users USING btree (username);
CREATE INDEX index_vehicle_journey_at_stops_on_stop_pointid ON public.vehicle_journey_at_stops USING btree (stop_point_id);
CREATE INDEX index_vehicle_journey_at_stops_on_vehicle_journey_id ON public.vehicle_journey_at_stops USING btree (vehicle_journey_id);
CREATE INDEX index_vehicle_journeys_on_route_id ON public.vehicle_journeys USING btree (route_id);
CREATE INDEX index_workbenches_on_line_referential_id ON public.workbenches USING btree (line_referential_id);
CREATE INDEX index_workbenches_on_organisation_id ON public.workbenches USING btree (organisation_id);
CREATE INDEX index_workbenches_on_stop_area_referential_id ON public.workbenches USING btree (stop_area_referential_id);
CREATE INDEX index_workbenches_on_workgroup_id ON public.workbenches USING btree (workgroup_id);
CREATE UNIQUE INDEX journey_patterns_objectid_key ON public.journey_patterns USING btree (objectid);
CREATE INDEX line_referential_sync_id ON public.line_referential_sync_messages USING btree (line_referential_sync_id);
CREATE UNIQUE INDEX lines_objectid_key ON public.lines USING btree (objectid);
CREATE INDEX lines_registration_number_key ON public.lines USING btree (registration_number);
CREATE UNIQUE INDEX networks_objectid_key ON public.networks USING btree (objectid);
CREATE INDEX networks_registration_number_key ON public.networks USING btree (registration_number);
CREATE UNIQUE INDEX pt_links_objectid_key ON public.pt_links USING btree (objectid);
CREATE UNIQUE INDEX routes_objectid_key ON public.routes USING btree (objectid);
CREATE INDEX stop_area_referential_sync_id ON public.stop_area_referential_sync_messages USING btree (stop_area_referential_sync_id);
CREATE UNIQUE INDEX stop_areas_objectid_key ON public.stop_areas USING btree (objectid);
CREATE UNIQUE INDEX stop_points_objectid_key ON public.stop_points USING btree (objectid);
CREATE UNIQUE INDEX taggings_idx ON public.taggings USING btree (tag_id, taggable_id, taggable_type, context, tagger_id, tagger_type);
CREATE UNIQUE INDEX time_tables_objectid_key ON public.time_tables USING btree (objectid);
CREATE UNIQUE INDEX unique_schema_migrations ON public.schema_migrations USING btree (version);
CREATE UNIQUE INDEX vehicle_journeys_objectid_key ON public.vehicle_journeys USING btree (objectid);
