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

--
-- Data for Name: compliance_control_sets; Type: TABLE DATA; Schema: public; Owner: chouette
--

INSERT INTO compliance_control_sets (id, name, organisation_id, created_at, updated_at) VALUES (3, 'BlipJDC', 4, '2017-10-03 09:03:29.665395', '2017-10-03 09:03:29.665395');


--
-- Data for Name: compliance_check_sets; Type: TABLE DATA; Schema: public; Owner: chouette
--



--
-- Data for Name: compliance_check_blocks; Type: TABLE DATA; Schema: public; Owner: chouette
--



--
-- Name: compliance_check_blocks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: chouette
--

SELECT pg_catalog.setval('compliance_check_blocks_id_seq', 1, false);


--
-- Data for Name: compliance_check_resources; Type: TABLE DATA; Schema: public; Owner: chouette
--



--
-- Data for Name: compliance_checks; Type: TABLE DATA; Schema: public; Owner: chouette
--



--
-- Data for Name: compliance_check_messages; Type: TABLE DATA; Schema: public; Owner: chouette
--



--
-- Name: compliance_check_messages_id_seq; Type: SEQUENCE SET; Schema: public; Owner: chouette
--

SELECT pg_catalog.setval('compliance_check_messages_id_seq', 1, false);


--
-- Name: compliance_check_resources_id_seq; Type: SEQUENCE SET; Schema: public; Owner: chouette
--

SELECT pg_catalog.setval('compliance_check_resources_id_seq', 1, false);


--
-- Name: compliance_check_sets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: chouette
--

SELECT pg_catalog.setval('compliance_check_sets_id_seq', 1, false);


--
-- Name: compliance_checks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: chouette
--

SELECT pg_catalog.setval('compliance_checks_id_seq', 1, false);


--
-- Data for Name: compliance_control_blocks; Type: TABLE DATA; Schema: public; Owner: chouette
--

INSERT INTO compliance_control_blocks (id, name, condition_attributes, compliance_control_set_id, created_at, updated_at) VALUES (36, NULL, '"transport_mode"=>"Bus", "transport_submode"=>"Vol de navette"', 3, '2017-10-03 09:04:17.937385', '2017-10-03 09:04:17.937385');


--
-- Name: compliance_control_blocks_id_seq; Type: SEQUENCE SET; Schema: public; Owner: chouette
--

SELECT pg_catalog.setval('compliance_control_blocks_id_seq', 36, true);


--
-- Name: compliance_control_sets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: chouette
--

SELECT pg_catalog.setval('compliance_control_sets_id_seq', 3, true);


--
-- Data for Name: compliance_controls; Type: TABLE DATA; Schema: public; Owner: chouette
--

INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (2, 3, 'GenericAttributeControl::Uniqueness', '"name"=>"MonNomAMoi"', 'Generic-Uniqueness', '3-Generic-3', 'info', 'taire', '2017-10-03 09:05:03.41123', '2017-10-03 09:05:03.41123', '3-Generic-3', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (3, 3, 'GenericAttributeControl::MinMax', '"maximum"=>"6", "minimum"=>"12"', 'Generic-MinMax', '3-Generic-2', 'info', 'taire taire', '2017-10-03 09:08:34.674019', '2017-10-03 09:08:34.674019', '3-Generic-2', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (4, 3, 'GenericAttributeControl::Pattern', '"value"=>"my-value", "pattern"=>"my-pattern"', 'Generic-Pattern', '3-Generic-3', 'info', 'Generic-Pattern commentaire', '2017-10-04 11:48:33.070631', '2017-10-04 11:48:33.070631', '3-Generic-3', 36);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (5, 3, 'JourneyPatternControl::Duplicates', NULL, 'JourneyPattern-Duplicates', '3-JourneyPattern-1', 'info', 'JourneyPattern-Duplicates commentaire', '2017-10-04 11:59:30.11936', '2017-10-04 11:59:30.11936', '3-JourneyPattern-1', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (6, 3, 'JourneyPatternControl::VehicleJourney', NULL, 'JourneyPattern-VehicleJourney', '3-JourneyPattern-2', 'info', 'JourneyPattern-VehicleJourney comment', '2017-10-04 12:01:04.902027', '2017-10-04 12:01:04.902027', '3-JourneyPattern-2', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (7, 3, 'LineControl::Route', NULL, 'Line-Route', '3-Line-1', 'info', 'Line-Route comment', '2017-10-04 12:04:14.708367', '2017-10-04 12:04:14.708367', '3-Line-1', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (8, 3, 'RouteControl::Duplicates', NULL, 'Route-Duplicates', '3-Route-4', 'info', 'Route-Duplicates comment', '2017-10-04 12:04:37.065069', '2017-10-04 12:04:37.065069', '3-Route-4', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (9, 3, 'RouteControl::JourneyPattern', NULL, 'Route-JourneyPattern', '3-Route-3', 'info', 'Route-JourneyPattern comment', '2017-10-04 12:06:40.396786', '2017-10-04 12:06:40.396786', '3-Route-3', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (10, 3, 'RouteControl::MinimumLength', NULL, 'Route-MinimumLength', '3-Route-6', 'info', 'Route-MinimumLength comment', '2017-10-04 12:08:14.733401', '2017-10-04 12:08:14.733401', '3-Route-6', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (11, 3, 'RouteControl::OmnibusJourneyPattern', NULL, 'Route-OmnibusJourneyPattern', '3-Route-9', 'info', 'Route-OmnibusJourneyPattern comment', '2017-10-04 12:10:17.84162', '2017-10-04 12:10:17.84162', '3-Route-9', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (12, 3, 'RouteControl::OppositeRouteTerminus', NULL, 'Route-OppositeRouteTerminus', '3-Route-5', 'info', '', '2017-10-04 12:11:39.750624', '2017-10-04 12:11:39.750624', '3-Route-5', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (13, 3, 'RouteControl::OppositeRoute', NULL, 'Route-OppositeRoute', '3-Route-2', 'info', '', '2017-10-04 12:12:05.197009', '2017-10-04 12:12:05.197009', '3-Route-2', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (15, 3, 'RouteControl::StopPointsInJourneyPattern', NULL, 'Route-StopPointsInJourneyPattern', '3-Route-6', 'info', '', '2017-10-04 12:13:30.801847', '2017-10-04 12:13:30.801847', '3-Route-6', NULL);
INSERT INTO compliance_controls (id, compliance_control_set_id, type, control_attributes, name, code, criticity, comment, created_at, updated_at, origin_code, compliance_control_block_id) VALUES (16, 3, 'RouteControl::TimeTable', NULL, 'Route-TimeTable', '3-VehicleJourney-4', 'info', '', '2017-10-04 12:13:45.899074', '2017-10-04 12:13:45.899074', '3-VehicleJourney-4', NULL);


--
-- Name: compliance_controls_id_seq; Type: SEQUENCE SET; Schema: public; Owner: chouette
--

SELECT pg_catalog.setval('compliance_controls_id_seq', 16, true);


--
-- PostgreSQL database dump complete
--

