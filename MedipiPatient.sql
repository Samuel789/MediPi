--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.3
-- Dumped by pg_dump version 9.6.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE medipidb2;
--
-- Name: medipidb2; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE medipidb2 WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_GB.UTF-8' LC_CTYPE = 'en_GB.UTF-8';


ALTER DATABASE medipidb2 OWNER TO postgres;

\connect medipidb2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: alert; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE alert (
    alert_id bigint NOT NULL,
    data_id bigint NOT NULL,
    patient_uuid character varying(100) NOT NULL,
    alert_time timestamp with time zone NOT NULL,
    alert_text character varying(5000) NOT NULL,
    transmit_success_date timestamp with time zone,
    retry_attempts integer NOT NULL
);


ALTER TABLE alert OWNER TO postgres;

--
-- Name: alert_alert_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE alert_alert_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE alert_alert_id_seq OWNER TO postgres;

--
-- Name: alert_alert_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE alert_alert_id_seq OWNED BY alert.alert_id;


--
-- Name: all_hardware_downloadable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE all_hardware_downloadable (
    downloadable_uuid character varying(100) NOT NULL,
    version character varying(100) NOT NULL,
    version_author character varying(100) NOT NULL,
    version_date timestamp with time zone NOT NULL,
    script_location character varying(100) NOT NULL,
    signature character varying(10000) NOT NULL
);


ALTER TABLE all_hardware_downloadable OWNER TO postgres;

--
-- Name: all_hardware_downloadable_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE all_hardware_downloadable_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE all_hardware_downloadable_id_seq OWNER TO postgres;

--
-- Name: all_hardware_downloaded; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE all_hardware_downloaded (
    all_hardware_downloaded_id integer NOT NULL,
    downloaded_date timestamp with time zone,
    downloadable_uuid character varying(100) NOT NULL,
    hardware_name character varying(100) NOT NULL
);


ALTER TABLE all_hardware_downloaded OWNER TO postgres;

--
-- Name: all_hardware_downloaded_all_hardware_downloaded_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE all_hardware_downloaded_all_hardware_downloaded_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE all_hardware_downloaded_all_hardware_downloaded_id_seq OWNER TO postgres;

--
-- Name: all_hardware_downloaded_all_hardware_downloaded_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE all_hardware_downloaded_all_hardware_downloaded_id_seq OWNED BY all_hardware_downloaded.all_hardware_downloaded_id;


--
-- Name: all_hardware_downloaded_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE all_hardware_downloaded_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE all_hardware_downloaded_id_seq OWNER TO postgres;

--
-- Name: device_data_data_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE device_data_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE device_data_data_id_seq OWNER TO postgres;

--
-- Name: dose_unit; Type: TABLE; Schema: public; Owner: medipiconc
--

CREATE TABLE dose_unit (
    dose_unit_id integer NOT NULL,
    name character varying NOT NULL
);


ALTER TABLE dose_unit OWNER TO medipiconc;

--
-- Name: dose_unit_id_seq; Type: SEQUENCE; Schema: public; Owner: medipiconc
--

CREATE SEQUENCE dose_unit_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE dose_unit_id_seq OWNER TO medipiconc;

--
-- Name: dose_unit_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: medipiconc
--

ALTER SEQUENCE dose_unit_id_seq OWNED BY dose_unit.dose_unit_id;


--
-- Name: hardware; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE hardware (
    hardware_name character varying(100) NOT NULL,
    mac_address character varying(100) NOT NULL,
    current_software_version character varying(100) NOT NULL,
    patient_uuid character varying(100)
);


ALTER TABLE hardware OWNER TO postgres;

--
-- Name: hardware_downloadable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE hardware_downloadable (
    downloadable_uuid character varying(100) NOT NULL,
    hardware_name character varying(100) NOT NULL,
    version character varying(100) NOT NULL,
    version_author character varying(100) NOT NULL,
    version_date timestamp with time zone NOT NULL,
    downloaded_date timestamp with time zone,
    script_location character varying(100) NOT NULL,
    signature character varying(10000) NOT NULL
);


ALTER TABLE hardware_downloadable OWNER TO postgres;

--
-- Name: medication; Type: TABLE; Schema: public; Owner: medipiconc
--

CREATE TABLE medication (
    medication_id integer NOT NULL,
    unique_name character varying NOT NULL,
    display_name character varying NOT NULL,
    advisory_stmt character varying NOT NULL,
    icon_name character varying,
    dose_unit integer NOT NULL
);


ALTER TABLE medication OWNER TO medipiconc;

--
-- Name: medication_id_seq; Type: SEQUENCE; Schema: public; Owner: medipiconc
--

CREATE SEQUENCE medication_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE medication_id_seq OWNER TO medipiconc;

--
-- Name: medication_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: medipiconc
--

ALTER SEQUENCE medication_id_seq OWNED BY medication.medication_id;


--
-- Name: recorded_dose; Type: TABLE; Schema: public; Owner: medipiconc
--

CREATE TABLE recorded_dose (
    recorded_dose_uuid character varying NOT NULL,
    value double precision NOT NULL,
    timetaken timestamp without time zone NOT NULL,
    schedule_id integer NOT NULL
);


ALTER TABLE recorded_dose OWNER TO medipiconc;

--
-- Name: medication_recorded_dose_id_seq; Type: SEQUENCE; Schema: public; Owner: medipiconc
--

CREATE SEQUENCE medication_recorded_dose_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE medication_recorded_dose_id_seq OWNER TO medipiconc;

--
-- Name: medication_recorded_dose_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: medipiconc
--

ALTER SEQUENCE medication_recorded_dose_id_seq OWNED BY recorded_dose.recorded_dose_uuid;


--
-- Name: schedule; Type: TABLE; Schema: public; Owner: medipiconc
--

CREATE TABLE schedule (
    schedule_id integer NOT NULL,
    assigned_start_date date NOT NULL,
    assigned_end_date date,
    alternate_name character varying,
    purpose_statement character varying NOT NULL,
    medication_id integer NOT NULL,
    patient_uuid character varying,
    device_start_date date,
    device_end_date date
);


ALTER TABLE schedule OWNER TO medipiconc;

--
-- Name: medication_schedule_id_seq; Type: SEQUENCE; Schema: public; Owner: medipiconc
--

CREATE SEQUENCE medication_schedule_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE medication_schedule_id_seq OWNER TO medipiconc;

--
-- Name: medication_schedule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: medipiconc
--

ALTER SEQUENCE medication_schedule_id_seq OWNED BY schedule.schedule_id;


--
-- Name: scheduled_dose; Type: TABLE; Schema: public; Owner: medipiconc
--

CREATE TABLE scheduled_dose (
    scheduled_dose_id integer NOT NULL,
    schedule_id integer NOT NULL,
    value double precision NOT NULL,
    start_day integer NOT NULL,
    repeat_interval integer,
    end_day integer,
    window_start_time time without time zone NOT NULL,
    window_end_time time without time zone NOT NULL,
    default_reminder_time time without time zone NOT NULL,
    reminder_time time without time zone NOT NULL,
    device_start_date integer,
    device_end_date integer
);


ALTER TABLE scheduled_dose OWNER TO medipiconc;

--
-- Name: medication_scheduled_dose_id_seq; Type: SEQUENCE; Schema: public; Owner: medipiconc
--

CREATE SEQUENCE medication_scheduled_dose_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE medication_scheduled_dose_id_seq OWNER TO medipiconc;

--
-- Name: medication_scheduled_dose_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: medipiconc
--

ALTER SEQUENCE medication_scheduled_dose_id_seq OWNED BY scheduled_dose.scheduled_dose_id;


--
-- Name: messages_message_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE messages_message_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE messages_message_id_seq OWNER TO postgres;

--
-- Name: patient; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE patient (
    patient_uuid character varying(100) NOT NULL,
    patient_group_uuid character varying
);


ALTER TABLE patient OWNER TO postgres;

--
-- Name: patient_certificate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE patient_certificate (
    patient_uuid character varying(100) NOT NULL,
    certificate_location character varying(1000) NOT NULL
);


ALTER TABLE patient_certificate OWNER TO postgres;

--
-- Name: patient_downloadable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE patient_downloadable (
    downloadable_uuid character varying(100) NOT NULL,
    patient_uuid character varying(100) NOT NULL,
    version character varying(100) NOT NULL,
    version_author character varying(255) NOT NULL,
    version_date timestamp with time zone NOT NULL,
    downloaded_date timestamp with time zone,
    script_location character varying(100) NOT NULL,
    signature character varying(10000) NOT NULL
);


ALTER TABLE patient_downloadable OWNER TO postgres;

--
-- Name: patient_group; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE patient_group (
    patient_group_uuid character varying(100) NOT NULL,
    patient_group_name character varying(100)
);


ALTER TABLE patient_group OWNER TO postgres;

--
-- Name: patient_hardware_downloadable_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE patient_hardware_downloadable_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE patient_hardware_downloadable_id_seq OWNER TO postgres;

--
-- Name: recording_device_attribute_attribute_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE recording_device_attribute_attribute_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE recording_device_attribute_attribute_id_seq OWNER TO postgres;

--
-- Name: recording_device_attribute; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE recording_device_attribute (
    attribute_id integer DEFAULT nextval('recording_device_attribute_attribute_id_seq'::regclass) NOT NULL,
    type_id integer NOT NULL,
    attribute_name character varying(100) NOT NULL,
    attribute_units character varying(100),
    attribute_type character varying(100) NOT NULL
);


ALTER TABLE recording_device_attribute OWNER TO postgres;

--
-- Name: recording_device_data; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE recording_device_data (
    data_id bigint NOT NULL,
    attribute_id integer NOT NULL,
    data_value character varying(1000) NOT NULL,
    patient_uuid character varying(100) NOT NULL,
    data_value_time timestamp with time zone NOT NULL,
    downloaded_time timestamp with time zone DEFAULT ('now'::text)::timestamp(3) with time zone NOT NULL,
    schedule_effective_time timestamp with time zone,
    schedule_expiry_time timestamp with time zone
);


ALTER TABLE recording_device_data OWNER TO postgres;

--
-- Name: recording_device_data_data_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE recording_device_data_data_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE recording_device_data_data_id_seq OWNER TO postgres;

--
-- Name: recording_device_data_data_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE recording_device_data_data_id_seq OWNED BY recording_device_data.data_id;


--
-- Name: recording_device_type_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE recording_device_type_type_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE recording_device_type_type_id_seq OWNER TO postgres;

--
-- Name: recording_device_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE recording_device_type (
    type_id integer DEFAULT nextval('recording_device_type_type_id_seq'::regclass) NOT NULL,
    type character varying(100) NOT NULL,
    make character varying(100) NOT NULL,
    model character varying(100) NOT NULL,
    display_name character varying(1000) NOT NULL
);


ALTER TABLE recording_device_type OWNER TO postgres;

--
-- Name: alert alert_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY alert ALTER COLUMN alert_id SET DEFAULT nextval('alert_alert_id_seq'::regclass);


--
-- Name: all_hardware_downloaded all_hardware_downloaded_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY all_hardware_downloaded ALTER COLUMN all_hardware_downloaded_id SET DEFAULT nextval('all_hardware_downloaded_all_hardware_downloaded_id_seq'::regclass);


--
-- Name: dose_unit dose_unit_id; Type: DEFAULT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY dose_unit ALTER COLUMN dose_unit_id SET DEFAULT nextval('dose_unit_id_seq'::regclass);


--
-- Name: medication medication_id; Type: DEFAULT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY medication ALTER COLUMN medication_id SET DEFAULT nextval('medication_id_seq'::regclass);


--
-- Name: recorded_dose recorded_dose_uuid; Type: DEFAULT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY recorded_dose ALTER COLUMN recorded_dose_uuid SET DEFAULT nextval('medication_recorded_dose_id_seq'::regclass);


--
-- Name: recording_device_data data_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY recording_device_data ALTER COLUMN data_id SET DEFAULT nextval('recording_device_data_data_id_seq'::regclass);


--
-- Name: schedule schedule_id; Type: DEFAULT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY schedule ALTER COLUMN schedule_id SET DEFAULT nextval('medication_schedule_id_seq'::regclass);


--
-- Name: scheduled_dose scheduled_dose_id; Type: DEFAULT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY scheduled_dose ALTER COLUMN scheduled_dose_id SET DEFAULT nextval('medication_scheduled_dose_id_seq'::regclass);


--
-- Data for Name: alert; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: alert_alert_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('alert_alert_id_seq', 1, false);


--
-- Data for Name: all_hardware_downloadable; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: all_hardware_downloadable_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('all_hardware_downloadable_id_seq', 1, false);


--
-- Data for Name: all_hardware_downloaded; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: all_hardware_downloaded_all_hardware_downloaded_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('all_hardware_downloaded_all_hardware_downloaded_id_seq', 1, false);


--
-- Name: all_hardware_downloaded_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('all_hardware_downloaded_id_seq', 1, false);


--
-- Name: device_data_data_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('device_data_data_id_seq', 1, false);


--
-- Data for Name: dose_unit; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

INSERT INTO dose_unit VALUES (0, 'tablets');
INSERT INTO dose_unit VALUES (1, 'capsules');


--
-- Name: dose_unit_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('dose_unit_id_seq', 1, false);


--
-- Data for Name: hardware; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO hardware VALUES ('9b636f94-e1c2-4773-a5ca-3858ba176e9c', 'b8:27:eb:27:09:93', '1', 'd9bc2478-062e-4b87-9060-4984f26b74be');


--
-- Data for Name: hardware_downloadable; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: medication; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

INSERT INTO medication VALUES (327096008, 'Tacrolimus 1mg capsules', 'Tacrolimus', 'Avoid excessive exposure to UV light including sunlight.', '/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png', 0);


--
-- Name: medication_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_id_seq', 1, false);


--
-- Name: medication_recorded_dose_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_recorded_dose_id_seq', 1, false);


--
-- Name: medication_schedule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_schedule_id_seq', 3, true);


--
-- Name: medication_scheduled_dose_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_scheduled_dose_id_seq', 2, true);


--
-- Name: messages_message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('messages_message_id_seq', 1, false);


--
-- Data for Name: patient; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO patient VALUES ('d9bc2478-062e-4b87-9060-4984f26b74be', '01');


--
-- Data for Name: patient_certificate; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO patient_certificate VALUES ('d9bc2478-062e-4b87-9060-4984f26b74be', '/home/sam/Git/MediPi/MediPiConcentrator/config/downloadables/patient/d9bc2478-062e-4b87-9060-4984f26b74be/d9bc2478-062e-4b87-9060-4984f26b74be.crt');


--
-- Data for Name: patient_downloadable; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: patient_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO patient_group VALUES ('01', 'Group 1');


--
-- Name: patient_hardware_downloadable_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('patient_hardware_downloadable_id_seq', 1, false);


--
-- Data for Name: recorded_dose; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

INSERT INTO recorded_dose VALUES ('b81923ea-9784-4d14-a583-ae6fb4df5097', 2, '2017-08-09 02:44:09.479', 3);
INSERT INTO recorded_dose VALUES ('70227096-bf03-4c6c-900c-f8794e4e361a', 2, '2017-08-09 02:45:48.879', 3);
INSERT INTO recorded_dose VALUES ('69a3e7bc-a425-4f80-9edb-e10023c6a80b', 2, '2017-08-09 01:07:31.135', 3);


--
-- Data for Name: recording_device_attribute; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: recording_device_attribute_attribute_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('recording_device_attribute_attribute_id_seq', 1, false);


--
-- Data for Name: recording_device_data; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: recording_device_data_data_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('recording_device_data_data_id_seq', 1, false);


--
-- Data for Name: recording_device_type; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Name: recording_device_type_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('recording_device_type_type_id_seq', 1, false);


--
-- Data for Name: schedule; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

INSERT INTO schedule VALUES (1, '2017-07-16', NULL, NULL, 'To prevent rejection', 327096008, 'd9bc2478-062e-4b87-9060-4984f26b74be', NULL, NULL);
INSERT INTO schedule VALUES (2, '2017-07-16', NULL, NULL, 'To prevent infection', 327096008, 'd9bc2478-062e-4b87-9060-4984f26b74be', NULL, NULL);
INSERT INTO schedule VALUES (3, '2017-07-16', NULL, 'Bactrim', 'To reduce constipation', 327096008, 'd9bc2478-062e-4b87-9060-4984f26b74be', NULL, NULL);


--
-- Data for Name: scheduled_dose; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

INSERT INTO scheduled_dose VALUES (1, 1, 2, 0, 1, NULL, '06:00:00', '11:00:00', '09:30:00', '09:30:00', NULL, NULL);
INSERT INTO scheduled_dose VALUES (2, 1, 2, 0, 1, NULL, '17:00:00', '22:00:00', '19:30:00', '19:30:00', NULL, NULL);
INSERT INTO scheduled_dose VALUES (3, 3, 2, 0, 1, NULL, '00:00:00', '02:00:00', '00:30:00', '00:30:00', NULL, NULL);


--
-- Name: alert alert_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY alert
    ADD CONSTRAINT alert_pk PRIMARY KEY (alert_id);


--
-- Name: all_hardware_downloadable all_hardware_downloadable_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY all_hardware_downloadable
    ADD CONSTRAINT all_hardware_downloadable_pkey PRIMARY KEY (downloadable_uuid);


--
-- Name: all_hardware_downloaded all_hardware_downloaded_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY all_hardware_downloaded
    ADD CONSTRAINT all_hardware_downloaded_pkey PRIMARY KEY (all_hardware_downloaded_id);


--
-- Name: dose_unit dose_unit_pkey; Type: CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY dose_unit
    ADD CONSTRAINT dose_unit_pkey PRIMARY KEY (dose_unit_id);


--
-- Name: hardware_downloadable hardware_downloadable_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hardware_downloadable
    ADD CONSTRAINT hardware_downloadable_pkey PRIMARY KEY (downloadable_uuid);


--
-- Name: hardware hardware_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hardware
    ADD CONSTRAINT hardware_pkey PRIMARY KEY (hardware_name);


--
-- Name: medication medication_pkey; Type: CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY medication
    ADD CONSTRAINT medication_pkey PRIMARY KEY (medication_id);


--
-- Name: recorded_dose medication_recorded_dose_pkey; Type: CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY recorded_dose
    ADD CONSTRAINT medication_recorded_dose_pkey PRIMARY KEY (recorded_dose_uuid);


--
-- Name: scheduled_dose medication_scheduled_dose_pkey; Type: CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY scheduled_dose
    ADD CONSTRAINT medication_scheduled_dose_pkey PRIMARY KEY (scheduled_dose_id);


--
-- Name: patient_certificate patient_certificate_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY patient_certificate
    ADD CONSTRAINT patient_certificate_pk PRIMARY KEY (patient_uuid);


--
-- Name: patient_downloadable patient_downloadable_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY patient_downloadable
    ADD CONSTRAINT patient_downloadable_pk PRIMARY KEY (downloadable_uuid);


--
-- Name: patient_group patient_group_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY patient_group
    ADD CONSTRAINT patient_group_pk PRIMARY KEY (patient_group_uuid);


--
-- Name: patient patient_id_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY patient
    ADD CONSTRAINT patient_id_pk PRIMARY KEY (patient_uuid);


--
-- Name: recording_device_attribute recording_device_attribute_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY recording_device_attribute
    ADD CONSTRAINT recording_device_attribute_pkey PRIMARY KEY (attribute_id);


--
-- Name: recording_device_data recording_device_data_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY recording_device_data
    ADD CONSTRAINT recording_device_data_pkey PRIMARY KEY (data_id);


--
-- Name: recording_device_type recording_device_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY recording_device_type
    ADD CONSTRAINT recording_device_type_pkey PRIMARY KEY (type_id);


--
-- Name: schedule schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT schedule_pkey PRIMARY KEY (schedule_id);


--
-- Name: dose_unit_id_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX dose_unit_id_uindex ON dose_unit USING btree (dose_unit_id);


--
-- Name: dose_unit_name_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX dose_unit_name_uindex ON dose_unit USING btree (name);


--
-- Name: medication_id_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX medication_id_uindex ON medication USING btree (medication_id);


--
-- Name: medication_recorded_dose_id_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX medication_recorded_dose_id_uindex ON recorded_dose USING btree (recorded_dose_uuid);


--
-- Name: medication_scheduled_dose_id_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX medication_scheduled_dose_id_uindex ON scheduled_dose USING btree (scheduled_dose_id);


--
-- Name: medication_unique_name_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX medication_unique_name_uindex ON medication USING btree (unique_name);


--
-- Name: schedule_id_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX schedule_id_uindex ON schedule USING btree (schedule_id);


--
-- Name: all_hardware_downloaded all_hardware_downloadable_all_hardware_downloaded_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY all_hardware_downloaded
    ADD CONSTRAINT all_hardware_downloadable_all_hardware_downloaded_fk FOREIGN KEY (downloadable_uuid) REFERENCES all_hardware_downloadable(downloadable_uuid);


--
-- Name: all_hardware_downloaded hardware_all_hardware_downloaded_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY all_hardware_downloaded
    ADD CONSTRAINT hardware_all_hardware_downloaded_fk FOREIGN KEY (hardware_name) REFERENCES hardware(hardware_name);


--
-- Name: hardware_downloadable hardware_hardware_downloadable_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hardware_downloadable
    ADD CONSTRAINT hardware_hardware_downloadable_fk FOREIGN KEY (hardware_name) REFERENCES hardware(hardware_name);


--
-- Name: medication medication_dose_unit_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY medication
    ADD CONSTRAINT medication_dose_unit_id_fk FOREIGN KEY (dose_unit) REFERENCES dose_unit(dose_unit_id);


--
-- Name: schedule medication_schedule_patient_patient_uuid_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY schedule
    ADD CONSTRAINT medication_schedule_patient_patient_uuid_fk FOREIGN KEY (patient_uuid) REFERENCES patient(patient_uuid);


--
-- Name: alert patient_alert_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY alert
    ADD CONSTRAINT patient_alert_fk FOREIGN KEY (patient_uuid) REFERENCES patient(patient_uuid);


--
-- Name: patient patient_group_patient_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY patient
    ADD CONSTRAINT patient_group_patient_fk FOREIGN KEY (patient_group_uuid) REFERENCES patient_group(patient_group_uuid);


--
-- Name: hardware patient_hardware_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hardware
    ADD CONSTRAINT patient_hardware_fk FOREIGN KEY (patient_uuid) REFERENCES patient(patient_uuid);


--
-- Name: patient_certificate patient_patient_certificates_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY patient_certificate
    ADD CONSTRAINT patient_patient_certificates_fk FOREIGN KEY (patient_uuid) REFERENCES patient(patient_uuid);


--
-- Name: patient_downloadable patient_patient_downloadable_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY patient_downloadable
    ADD CONSTRAINT patient_patient_downloadable_fk FOREIGN KEY (patient_uuid) REFERENCES patient(patient_uuid);


--
-- Name: recording_device_data patient_recording_device_data_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY recording_device_data
    ADD CONSTRAINT patient_recording_device_data_fk FOREIGN KEY (patient_uuid) REFERENCES patient(patient_uuid);


--
-- Name: recording_device_data recording_device_attribute_recording_device_data_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY recording_device_data
    ADD CONSTRAINT recording_device_attribute_recording_device_data_fk FOREIGN KEY (attribute_id) REFERENCES recording_device_attribute(attribute_id);


--
-- Name: alert recording_device_data_alert_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY alert
    ADD CONSTRAINT recording_device_data_alert_fk FOREIGN KEY (data_id) REFERENCES recording_device_data(data_id);


--
-- Name: recording_device_attribute recording_device_type_recording_device_attribute_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY recording_device_attribute
    ADD CONSTRAINT recording_device_type_recording_device_attribute_fk FOREIGN KEY (type_id) REFERENCES recording_device_type(type_id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

