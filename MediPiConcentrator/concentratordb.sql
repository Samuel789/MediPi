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

SET search_path = public, pg_catalog;

ALTER TABLE ONLY public.medication_schedule DROP CONSTRAINT schedule_medication_id_fk;
ALTER TABLE ONLY public.recording_device_attribute DROP CONSTRAINT recording_device_type_recording_device_attribute_fk;
ALTER TABLE ONLY public.alert DROP CONSTRAINT recording_device_data_alert_fk;
ALTER TABLE ONLY public.recording_device_data DROP CONSTRAINT recording_device_attribute_recording_device_data_fk;
ALTER TABLE ONLY public.recording_device_data DROP CONSTRAINT patient_recording_device_data_fk;
ALTER TABLE ONLY public.patient_downloadable DROP CONSTRAINT patient_patient_downloadable_fk;
ALTER TABLE ONLY public.patient_certificate DROP CONSTRAINT patient_patient_certificates_fk;
ALTER TABLE ONLY public.hardware DROP CONSTRAINT patient_hardware_fk;
ALTER TABLE ONLY public.patient DROP CONSTRAINT patient_group_patient_fk;
ALTER TABLE ONLY public.alert DROP CONSTRAINT patient_alert_fk;
ALTER TABLE ONLY public.scheduled_dose DROP CONSTRAINT medication_scheduled_dose_medication_schedule_id_fk;
ALTER TABLE ONLY public.medication_schedule DROP CONSTRAINT medication_schedule_patient_patient_uuid_fk;
ALTER TABLE ONLY public.recorded_dose DROP CONSTRAINT medication_recorded_dose_medication_schedule_id_fk;
ALTER TABLE ONLY public.hardware_downloadable DROP CONSTRAINT hardware_hardware_downloadable_fk;
ALTER TABLE ONLY public.all_hardware_downloaded DROP CONSTRAINT hardware_all_hardware_downloaded_fk;
ALTER TABLE ONLY public.all_hardware_downloaded DROP CONSTRAINT all_hardware_downloadable_all_hardware_downloaded_fk;
DROP INDEX public.schedule_id_uindex;
DROP INDEX public.medication_unique_name_uindex;
DROP INDEX public.medication_scheduled_dose_id_uindex;
DROP INDEX public.medication_recorded_dose_id_uindex;
DROP INDEX public.medication_id_uindex;
ALTER TABLE ONLY public.medication_schedule DROP CONSTRAINT schedule_pkey;
ALTER TABLE ONLY public.recording_device_type DROP CONSTRAINT recording_device_type_pkey;
ALTER TABLE ONLY public.recording_device_data DROP CONSTRAINT recording_device_data_pkey;
ALTER TABLE ONLY public.recording_device_attribute DROP CONSTRAINT recording_device_attribute_pkey;
ALTER TABLE ONLY public.patient DROP CONSTRAINT patient_id_pk;
ALTER TABLE ONLY public.patient_group DROP CONSTRAINT patient_group_pk;
ALTER TABLE ONLY public.patient_downloadable DROP CONSTRAINT patient_downloadable_pk;
ALTER TABLE ONLY public.patient_certificate DROP CONSTRAINT patient_certificate_pk;
ALTER TABLE ONLY public.scheduled_dose DROP CONSTRAINT medication_scheduled_dose_pkey;
ALTER TABLE ONLY public.recorded_dose DROP CONSTRAINT medication_recorded_dose_pkey;
ALTER TABLE ONLY public.medication DROP CONSTRAINT medication_pkey;
ALTER TABLE ONLY public.hardware DROP CONSTRAINT hardware_pkey;
ALTER TABLE ONLY public.hardware_downloadable DROP CONSTRAINT hardware_downloadable_pkey;
ALTER TABLE ONLY public.all_hardware_downloaded DROP CONSTRAINT all_hardware_downloaded_pkey;
ALTER TABLE ONLY public.all_hardware_downloadable DROP CONSTRAINT all_hardware_downloadable_pkey;
ALTER TABLE ONLY public.alert DROP CONSTRAINT alert_pk;
ALTER TABLE public.recording_device_data ALTER COLUMN data_id DROP DEFAULT;
ALTER TABLE public.scheduled_dose ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.medication_schedule ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.recorded_dose ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.medication ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.all_hardware_downloaded ALTER COLUMN all_hardware_downloaded_id DROP DEFAULT;
ALTER TABLE public.alert ALTER COLUMN alert_id DROP DEFAULT;
DROP TABLE public.recording_device_type;
DROP SEQUENCE public.recording_device_type_type_id_seq;
DROP SEQUENCE public.recording_device_data_data_id_seq;
DROP TABLE public.recording_device_data;
DROP TABLE public.recording_device_attribute;
DROP SEQUENCE public.recording_device_attribute_attribute_id_seq;
DROP SEQUENCE public.patient_hardware_downloadable_id_seq;
DROP TABLE public.patient_group;
DROP TABLE public.patient_downloadable;
DROP TABLE public.patient_certificate;
DROP TABLE public.patient;
DROP SEQUENCE public.messages_message_id_seq;
DROP SEQUENCE public.medication_scheduled_dose_id_seq;
DROP TABLE public.scheduled_dose;
DROP SEQUENCE public.medication_schedule_id_seq;
DROP TABLE public.medication_schedule;
DROP SEQUENCE public.medication_recorded_dose_id_seq;
DROP TABLE public.recorded_dose;
DROP SEQUENCE public.medication_id_seq;
DROP TABLE public.medication;
DROP TABLE public.hardware_downloadable;
DROP TABLE public.hardware;
DROP SEQUENCE public.device_data_data_id_seq;
DROP SEQUENCE public.all_hardware_downloaded_id_seq;
DROP SEQUENCE public.all_hardware_downloaded_all_hardware_downloaded_id_seq;
DROP TABLE public.all_hardware_downloaded;
DROP SEQUENCE public.all_hardware_downloadable_id_seq;
DROP TABLE public.all_hardware_downloadable;
DROP SEQUENCE public.alert_alert_id_seq;
DROP TABLE public.alert;
DROP EXTENSION plpgsql;
DROP SCHEMA public;
--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


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
    patientUuid character varying(100) NOT NULL,
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
-- Name: hardware; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE hardware (
    hardware_name character varying(100) NOT NULL,
    mac_address character varying(100) NOT NULL,
    current_software_version character varying(100) NOT NULL,
    patientUuid character varying(100)
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
    id integer NOT NULL,
    unique_name character varying NOT NULL,
    display_name character varying NOT NULL,
    advisory_stmt character varying NOT NULL,
    icon character varying
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

ALTER SEQUENCE medication_id_seq OWNED BY medication.id;


--
-- Name: medication_recorded_dose; Type: TABLE; Schema: public; Owner: medipiconc
--

CREATE TABLE medication_recorded_dose (
    id integer NOT NULL,
    value double precision NOT NULL,
    timetaken timestamp without time zone NOT NULL,
    schedule integer NOT NULL
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

ALTER SEQUENCE medication_recorded_dose_id_seq OWNED BY recorded_dose.id;


--
-- Name: medication_schedule; Type: TABLE; Schema: public; Owner: medipiconc
--

CREATE TABLE medication_schedule (
    id integer NOT NULL,
    startdate date NOT NULL,
    enddate date,
    alternatename character varying,
    purposestatement character varying NOT NULL,
    medication integer NOT NULL,
    patient character varying
);


ALTER TABLE medication_schedule OWNER TO medipiconc;

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

ALTER SEQUENCE medication_schedule_id_seq OWNED BY medication_schedule.id;


--
-- Name: medication_scheduled_dose; Type: TABLE; Schema: public; Owner: medipiconc
--

CREATE TABLE medication_scheduled_dose (
    id integer NOT NULL,
    schedule integer NOT NULL,
    value double precision NOT NULL,
    startday integer NOT NULL,
    repeatinterval integer,
    endday integer,
    windowstarttime time without time zone NOT NULL,
    windowendtime time without time zone NOT NULL,
    defaultremindertime time without time zone NOT NULL,
    remindertime time without time zone NOT NULL
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

ALTER SEQUENCE medication_scheduled_dose_id_seq OWNED BY scheduled_dose.id;


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
    patientUuid character varying(100) NOT NULL,
    patient_group_uuid character varying
);


ALTER TABLE patient OWNER TO postgres;

--
-- Name: patient_certificate; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE patient_certificate (
    patientUuid character varying(100) NOT NULL,
    certificate_location character varying(1000) NOT NULL
);


ALTER TABLE patient_certificate OWNER TO postgres;

--
-- Name: patient_downloadable; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE patient_downloadable (
    downloadable_uuid character varying(100) NOT NULL,
    patientUuid character varying(100) NOT NULL,
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
    patientUuid character varying(100) NOT NULL,
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
-- Name: medication id; Type: DEFAULT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY medication ALTER COLUMN id SET DEFAULT nextval('medication_id_seq'::regclass);


--
-- Name: medication_recorded_dose id; Type: DEFAULT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY recorded_dose ALTER COLUMN id SET DEFAULT nextval('medication_recorded_dose_id_seq'::regclass);


--
-- Name: medication_schedule id; Type: DEFAULT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY medication_schedule ALTER COLUMN id SET DEFAULT nextval('medication_schedule_id_seq'::regclass);


--
-- Name: medication_scheduled_dose id; Type: DEFAULT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY scheduled_dose ALTER COLUMN id SET DEFAULT nextval('medication_scheduled_dose_id_seq'::regclass);


--
-- Name: recording_device_data data_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY recording_device_data ALTER COLUMN data_id SET DEFAULT nextval('recording_device_data_data_id_seq'::regclass);


--
-- Data for Name: alert; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY alert (alert_id, data_id, patientUuid, alert_time, alert_text, transmit_success_date, retry_attempts) FROM stdin;
\.


--
-- Name: alert_alert_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('alert_alert_id_seq', 1, false);


--
-- Data for Name: all_hardware_downloadable; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY all_hardware_downloadable (downloadable_uuid, version, version_author, version_date, script_location, signature) FROM stdin;
\.


--
-- Name: all_hardware_downloadable_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('all_hardware_downloadable_id_seq', 1, false);


--
-- Data for Name: all_hardware_downloaded; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY all_hardware_downloaded (all_hardware_downloaded_id, downloaded_date, downloadable_uuid, hardware_name) FROM stdin;
\.


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
-- Data for Name: hardware; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hardware (hardware_name, mac_address, current_software_version, patientUuid) FROM stdin;
9b636f94-e1c2-4773-a5ca-3858ba176e9c	b8:27:eb:27:09:93	1	d9bc2478-062e-4b87-9060-4984f26b74be
\.


--
-- Data for Name: hardware_downloadable; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hardware_downloadable (downloadable_uuid, hardware_name, version, version_author, version_date, downloaded_date, script_location, signature) FROM stdin;
\.


--
-- Data for Name: medication; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

COPY medication (id, unique_name, display_name, advisory_stmt, icon) FROM stdin;
327096008	Tacrolimus 1mg capsules	Tacrolimus	Avoid excessive exposure to UV light including sunlight.	/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png
\.


--
-- Name: medication_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_id_seq', 1, false);


--
-- Data for Name: medication_recorded_dose; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

COPY recorded_dose (id, value, timetaken, schedule) FROM stdin;
\.


--
-- Name: medication_recorded_dose_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_recorded_dose_id_seq', 1, false);


--
-- Data for Name: medication_schedule; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

COPY medication_schedule (id, assigned_start_date, assigned_end_date, alternate_name, purpose_statement, medication_id, patientUuid) FROM stdin;
1	2017-07-16	\N	\N	To prevent rejection	327096008	d9bc2478-062e-4b87-9060-4984f26b74be
\.


--
-- Name: medication_schedule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_schedule_id_seq', 1, true);


--
-- Data for Name: medication_scheduled_dose; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

COPY scheduled_dose (id, schedule, value, start_day, repeat_interval, end_day, window_start_time, window_end_time, default_reminder_time, reminder_time) FROM stdin;
1	1	2	0	1	\N	06:00:00	11:00:00	09:30:00	09:30:00
2	1	2	0	1	\N	17:00:00	22:00:00	19:30:00	19:30:00
\.


--
-- Name: medication_scheduled_dose_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_scheduled_dose_id_seq', 1, true);


--
-- Name: messages_message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('messages_message_id_seq', 1, false);


--
-- Data for Name: patient; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY patient (patientUuid, patient_group_uuid) FROM stdin;
d9bc2478-062e-4b87-9060-4984f26b74be	01
\.


--
-- Data for Name: patient_certificate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY patient_certificate (patientUuid, certificate_location) FROM stdin;
d9bc2478-062e-4b87-9060-4984f26b74be	/home/sam/Git/MediPi/MediPiConcentrator/config/downloadables/patient/d9bc2478-062e-4b87-9060-4984f26b74be/d9bc2478-062e-4b87-9060-4984f26b74be.crt
\.


--
-- Data for Name: patient_downloadable; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY patient_downloadable (downloadable_uuid, patientUuid, version, version_author, version_date, downloaded_date, script_location, signature) FROM stdin;
\.


--
-- Data for Name: patient_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY patient_group (patient_group_uuid, patient_group_name) FROM stdin;
01	Group 1
\.


--
-- Name: patient_hardware_downloadable_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('patient_hardware_downloadable_id_seq', 1, false);


--
-- Data for Name: recording_device_attribute; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY recording_device_attribute (attribute_id, type_id, attribute_name, attribute_units, attribute_type) FROM stdin;
\.


--
-- Name: recording_device_attribute_attribute_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('recording_device_attribute_attribute_id_seq', 1, false);


--
-- Data for Name: recording_device_data; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY recording_device_data (data_id, attribute_id, data_value, patientUuid, data_value_time, downloaded_time, schedule_effective_time, schedule_expiry_time) FROM stdin;
\.


--
-- Name: recording_device_data_data_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('recording_device_data_data_id_seq', 1, false);


--
-- Data for Name: recording_device_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY recording_device_type (type_id, type, make, model, display_name) FROM stdin;
\.


--
-- Name: recording_device_type_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('recording_device_type_type_id_seq', 1, false);


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
    ADD CONSTRAINT medication_pkey PRIMARY KEY (id);


--
-- Name: medication_recorded_dose medication_recorded_dose_pkey; Type: CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY recorded_dose
    ADD CONSTRAINT medication_recorded_dose_pkey PRIMARY KEY (id);


--
-- Name: medication_scheduled_dose medication_scheduled_dose_pkey; Type: CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY scheduled_dose
    ADD CONSTRAINT medication_scheduled_dose_pkey PRIMARY KEY (id);


--
-- Name: patient_certificate patient_certificate_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY patient_certificate
    ADD CONSTRAINT patient_certificate_pk PRIMARY KEY (patientUuid);


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
    ADD CONSTRAINT patient_id_pk PRIMARY KEY (patientUuid);


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
-- Name: medication_schedule schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY medication_schedule
    ADD CONSTRAINT schedule_pkey PRIMARY KEY (id);


--
-- Name: medication_id_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX medication_id_uindex ON medication USING btree (id);


--
-- Name: medication_recorded_dose_id_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX medication_recorded_dose_id_uindex ON recorded_dose USING btree (id);


--
-- Name: medication_scheduled_dose_id_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX medication_scheduled_dose_id_uindex ON scheduled_dose USING btree (id);


--
-- Name: medication_unique_name_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX medication_unique_name_uindex ON medication USING btree (unique_name);


--
-- Name: schedule_id_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX schedule_id_uindex ON medication_schedule USING btree (id);


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
-- Name: medication_recorded_dose medication_recorded_dose_medication_schedule_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY recorded_dose
    ADD CONSTRAINT medication_recorded_dose_medication_schedule_id_fk FOREIGN KEY (schedule) REFERENCES medication_schedule(id);


--
-- Name: medication_schedule medication_schedule_patient_patient_uuid_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY medication_schedule
    ADD CONSTRAINT medication_schedule_patient_patient_uuid_fk FOREIGN KEY (patientUuid) REFERENCES patient(patientUuid);


--
-- Name: medication_scheduled_dose medication_scheduled_dose_medication_schedule_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY scheduled_dose
    ADD CONSTRAINT medication_scheduled_dose_medication_schedule_id_fk FOREIGN KEY (schedule) REFERENCES medication_schedule(id);


--
-- Name: alert patient_alert_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY alert
    ADD CONSTRAINT patient_alert_fk FOREIGN KEY (patientUuid) REFERENCES patient(patientUuid);


--
-- Name: patient patient_group_patient_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY patient
    ADD CONSTRAINT patient_group_patient_fk FOREIGN KEY (patient_group_uuid) REFERENCES patient_group(patient_group_uuid);


--
-- Name: hardware patient_hardware_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY hardware
    ADD CONSTRAINT patient_hardware_fk FOREIGN KEY (patientUuid) REFERENCES patient(patientUuid);


--
-- Name: patient_certificate patient_patient_certificates_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY patient_certificate
    ADD CONSTRAINT patient_patient_certificates_fk FOREIGN KEY (patientUuid) REFERENCES patient(patientUuid);


--
-- Name: patient_downloadable patient_patient_downloadable_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY patient_downloadable
    ADD CONSTRAINT patient_patient_downloadable_fk FOREIGN KEY (patientUuid) REFERENCES patient(patientUuid);


--
-- Name: recording_device_data patient_recording_device_data_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY recording_device_data
    ADD CONSTRAINT patient_recording_device_data_fk FOREIGN KEY (patientUuid) REFERENCES patient(patientUuid);


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
-- Name: medication_schedule schedule_medication_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY medication_schedule
    ADD CONSTRAINT schedule_medication_id_fk FOREIGN KEY (medication_id) REFERENCES medication(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

