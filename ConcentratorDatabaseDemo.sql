--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.4
-- Dumped by pg_dump version 9.6.4

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
-- Name: schedule_adherence; Type: TABLE; Schema: public; Owner: medipiconc
--

CREATE TABLE schedule_adherence (
    schedule_id integer NOT NULL,
    seven_day_fraction double precision,
    streak_length integer
);


ALTER TABLE schedule_adherence OWNER TO medipiconc;

--
-- Name: adherence_schedule_id_seq; Type: SEQUENCE; Schema: public; Owner: medipiconc
--

CREATE SEQUENCE adherence_schedule_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE adherence_schedule_id_seq OWNER TO medipiconc;

--
-- Name: adherence_schedule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: medipiconc
--

ALTER SEQUENCE adherence_schedule_id_seq OWNED BY schedule_adherence.schedule_id;


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
    medication_id bigint NOT NULL,
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
    schedule_id integer NOT NULL,
    day_taken integer NOT NULL,
    time_taken time without time zone NOT NULL
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
    medication_id bigint NOT NULL,
    patient_uuid character varying NOT NULL,
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
-- Name: patient_adherence; Type: TABLE; Schema: public; Owner: medipiconc
--

CREATE TABLE patient_adherence (
    patient_uuid character varying NOT NULL,
    seven_day_fraction double precision,
    streak_length integer
);


ALTER TABLE patient_adherence OWNER TO medipiconc;

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
-- Name: schedule_adherence schedule_id; Type: DEFAULT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY schedule_adherence ALTER COLUMN schedule_id SET DEFAULT nextval('adherence_schedule_id_seq'::regclass);


--
-- Name: scheduled_dose scheduled_dose_id; Type: DEFAULT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY scheduled_dose ALTER COLUMN scheduled_dose_id SET DEFAULT nextval('medication_scheduled_dose_id_seq'::regclass);


--
-- Name: adherence_schedule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('adherence_schedule_id_seq', 1, false);


--
-- Data for Name: alert; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY alert (alert_id, data_id, patient_uuid, alert_time, alert_text, transmit_success_date, retry_attempts) FROM stdin;
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
-- Data for Name: dose_unit; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

COPY dose_unit (dose_unit_id, name) FROM stdin;
0	tablets
1	capsules
\.


--
-- Name: dose_unit_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('dose_unit_id_seq', 1, false);


--
-- Data for Name: hardware; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY hardware (hardware_name, mac_address, current_software_version, patient_uuid) FROM stdin;
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

COPY medication (medication_id, unique_name, display_name, advisory_stmt, icon_name, dose_unit) FROM stdin;
8077011000001100	Mycophenolic acid 360mg gastro-resistant tablets	Mycophenolic acid	Avoid excessive exposure to UV light including sunlight.	/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png	0
317544000	Docusate 100mg capsules	Docusate	Avoid excessive exposure to UV light including sunlight.	/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png	1
327096008	Tacrolimus 1mg capsules	Tacrolimus	Avoid excessive exposure to UV light including sunlight.	/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png	1
418349006	Prednisone 1mg tablets	Prednisone		/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png	0
408155001	Sirolimus 2mg tablets	Sirolimus		/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png	0
8791311000001100	Azathioprine 10mg capsules	Azathioprine		/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png	1
324430000	Trimethoprim 100mg tablets	Trimethoprim		/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png	0
324357002	Co-trimoxazole 80mg/400mg tablets	Co-trimoxazole		/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png	0
\.


--
-- Name: medication_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_id_seq', 3, true);


--
-- Name: medication_recorded_dose_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_recorded_dose_id_seq', 27, true);


--
-- Name: medication_schedule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_schedule_id_seq', 5, true);


--
-- Name: medication_scheduled_dose_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_scheduled_dose_id_seq', 7, true);


--
-- Name: messages_message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('messages_message_id_seq', 1, false);


--
-- Data for Name: patient; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY patient (patient_uuid, patient_group_uuid) FROM stdin;
d9bc2478-062e-4b87-9060-4984f26b74be	MedWeb
\.


--
-- Data for Name: patient_adherence; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

COPY patient_adherence (patient_uuid, seven_day_fraction, streak_length) FROM stdin;
d9bc2478-062e-4b87-9060-4984f26b74be	0.800000000000000044	0
\.


--
-- Data for Name: patient_certificate; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY patient_certificate (patient_uuid, certificate_location) FROM stdin;
d9bc2478-062e-4b87-9060-4984f26b74be	/home/sam/Git/MediPi/MediPiConcentrator/config/downloadables/patient/d9bc2478-062e-4b87-9060-4984f26b74be/d9bc2478-062e-4b87-9060-4984f26b74be.crt
\.


--
-- Data for Name: patient_downloadable; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY patient_downloadable (downloadable_uuid, patient_uuid, version, version_author, version_date, downloaded_date, script_location, signature) FROM stdin;
\.


--
-- Data for Name: patient_group; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY patient_group (patient_group_uuid, patient_group_name) FROM stdin;
01	Group 1
MedWeb	MedWeb
\.


--
-- Name: patient_hardware_downloadable_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('patient_hardware_downloadable_id_seq', 1, false);


--
-- Data for Name: recorded_dose; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

COPY recorded_dose (recorded_dose_uuid, value, schedule_id, day_taken, time_taken) FROM stdin;
11	2	15	0	12:00:00
12	2	15	1	12:00:00
10	3	15	0	17:00:00
14	3	15	1	17:00:00
18	2	15	3	12:00:00
20	3	15	3	17:00:00
22	3	15	4	17:00:00
23	2	15	4	12:00:00
15	3	15	2	17:00:00
21	2	15	2	12:00:00
5	2	11	0	15:00:00
7	2	11	2	15:00:00
6	2	11	3	15:00:00
9	2	11	4	15:00:00
24	2	4	0	17:00:00
25	2	4	1	17:00:00
26	2	4	2	17:00:00
27	2	15	4	17:00:00
\.


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

COPY recording_device_data (data_id, attribute_id, data_value, patient_uuid, data_value_time, downloaded_time, schedule_effective_time, schedule_expiry_time) FROM stdin;
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
-- Data for Name: schedule; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

COPY schedule (schedule_id, assigned_start_date, assigned_end_date, alternate_name, purpose_statement, medication_id, patient_uuid, device_start_date, device_end_date) FROM stdin;
13	2017-08-20	2017-08-28	Imuran	To reduce constipation	317544000	d9bc2478-062e-4b87-9060-4984f26b74be	\N	\N
4	2017-08-20	2017-08-28	Trimopan	To prevent infection	324430000	d9bc2478-062e-4b87-9060-4984f26b74be	\N	\N
11	2017-08-20	2017-08-28		To prevent rejection	327096008	d9bc2478-062e-4b87-9060-4984f26b74be	\N	\N
15	2017-08-20	2017-08-28		To prevent rejection	8791311000001100	d9bc2478-062e-4b87-9060-4984f26b74be	\N	\N
5	2017-08-20	2017-08-28	Bactrim	To prevent rejection	324357002	d9bc2478-062e-4b87-9060-4984f26b74be	\N	\N
\.


--
-- Data for Name: schedule_adherence; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

COPY schedule_adherence (schedule_id, seven_day_fraction, streak_length) FROM stdin;
13	\N	5
11	0.800000000000000044	3
15	0.900000000000000022	0
4	0.599999999999999978	0
5	\N	5
\.


--
-- Data for Name: scheduled_dose; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

COPY scheduled_dose (scheduled_dose_id, schedule_id, value, start_day, repeat_interval, end_day, window_start_time, window_end_time, default_reminder_time, reminder_time, device_start_date, device_end_date) FROM stdin;
13	11	2	0	1	\N	14:00:00	16:00:00	15:00:00	15:00:00	\N	\N
7	4	2	0	1	\N	16:00:00	18:00:00	17:00:00	17:00:00	\N	\N
5	15	3	0	1	\N	16:00:00	18:00:00	17:00:00	17:00:00	\N	\N
6	15	2	0	1	\N	11:00:00	14:00:00	11:20:00	11:20:00	\N	\N
\.


--
-- Name: schedule_adherence adherence_pkey; Type: CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY schedule_adherence
    ADD CONSTRAINT adherence_pkey PRIMARY KEY (schedule_id);


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
-- Name: patient_adherence patient_adherence_pkey; Type: CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY patient_adherence
    ADD CONSTRAINT patient_adherence_pkey PRIMARY KEY (patient_uuid);


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
-- Name: adherence_schedule_id_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX adherence_schedule_id_uindex ON schedule_adherence USING btree (schedule_id);


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
-- Name: patient_adherence_patient_uuid_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX patient_adherence_patient_uuid_uindex ON patient_adherence USING btree (patient_uuid);


--
-- Name: schedule_id_uindex; Type: INDEX; Schema: public; Owner: medipiconc
--

CREATE UNIQUE INDEX schedule_id_uindex ON schedule USING btree (schedule_id);


--
-- Name: schedule_adherence adherence_schedule_schedule_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY schedule_adherence
    ADD CONSTRAINT adherence_schedule_schedule_id_fk FOREIGN KEY (schedule_id) REFERENCES schedule(schedule_id);


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
-- Name: patient_adherence patient_adherence_patient_patient_uuid_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipiconc
--

ALTER TABLE ONLY patient_adherence
    ADD CONSTRAINT patient_adherence_patient_patient_uuid_fk FOREIGN KEY (patient_uuid) REFERENCES patient(patient_uuid);


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

