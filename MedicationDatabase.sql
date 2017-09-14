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

ALTER TABLE ONLY public.recording_device_attribute DROP CONSTRAINT recording_device_type_recording_device_attribute_fk;
ALTER TABLE ONLY public.alert DROP CONSTRAINT recording_device_data_alert_fk;
ALTER TABLE ONLY public.recording_device_data DROP CONSTRAINT recording_device_attribute_recording_device_data_fk;
ALTER TABLE ONLY public.recording_device_data DROP CONSTRAINT patient_recording_device_data_fk;
ALTER TABLE ONLY public.patient_downloadable DROP CONSTRAINT patient_patient_downloadable_fk;
ALTER TABLE ONLY public.patient_certificate DROP CONSTRAINT patient_patient_certificates_fk;
ALTER TABLE ONLY public.hardware DROP CONSTRAINT patient_hardware_fk;
ALTER TABLE ONLY public.patient DROP CONSTRAINT patient_group_patient_fk;
ALTER TABLE ONLY public.alert DROP CONSTRAINT patient_alert_fk;
ALTER TABLE ONLY public.patient_adherence DROP CONSTRAINT patient_adherence_patient_patient_uuid_fk;
ALTER TABLE ONLY public.schedule DROP CONSTRAINT medication_schedule_patient_patient_uuid_fk;
ALTER TABLE ONLY public.medication DROP CONSTRAINT medication_dose_unit_id_fk;
ALTER TABLE ONLY public.hardware_downloadable DROP CONSTRAINT hardware_hardware_downloadable_fk;
ALTER TABLE ONLY public.all_hardware_downloaded DROP CONSTRAINT hardware_all_hardware_downloaded_fk;
ALTER TABLE ONLY public.all_hardware_downloaded DROP CONSTRAINT all_hardware_downloadable_all_hardware_downloaded_fk;
ALTER TABLE ONLY public.schedule_adherence DROP CONSTRAINT adherence_schedule_schedule_id_fk;
DROP INDEX public.schedule_id_uindex;
DROP INDEX public.patient_adherence_patient_uuid_uindex;
DROP INDEX public.medication_unique_name_uindex;
DROP INDEX public.medication_scheduled_dose_id_uindex;
DROP INDEX public.medication_recorded_dose_id_uindex;
DROP INDEX public.medication_id_uindex;
DROP INDEX public.dose_unit_name_uindex;
DROP INDEX public.dose_unit_id_uindex;
DROP INDEX public.adherence_schedule_id_uindex;
ALTER TABLE ONLY public.schedule DROP CONSTRAINT schedule_pkey;
ALTER TABLE ONLY public.recording_device_type DROP CONSTRAINT recording_device_type_pkey;
ALTER TABLE ONLY public.recording_device_data DROP CONSTRAINT recording_device_data_pkey;
ALTER TABLE ONLY public.recording_device_attribute DROP CONSTRAINT recording_device_attribute_pkey;
ALTER TABLE ONLY public.patient DROP CONSTRAINT patient_id_pk;
ALTER TABLE ONLY public.patient_group DROP CONSTRAINT patient_group_pk;
ALTER TABLE ONLY public.patient_downloadable DROP CONSTRAINT patient_downloadable_pk;
ALTER TABLE ONLY public.patient_certificate DROP CONSTRAINT patient_certificate_pk;
ALTER TABLE ONLY public.patient_adherence DROP CONSTRAINT patient_adherence_pkey;
ALTER TABLE ONLY public.scheduled_dose DROP CONSTRAINT medication_scheduled_dose_pkey;
ALTER TABLE ONLY public.recorded_dose DROP CONSTRAINT medication_recorded_dose_pkey;
ALTER TABLE ONLY public.medication DROP CONSTRAINT medication_pkey;
ALTER TABLE ONLY public.hardware DROP CONSTRAINT hardware_pkey;
ALTER TABLE ONLY public.hardware_downloadable DROP CONSTRAINT hardware_downloadable_pkey;
ALTER TABLE ONLY public.dose_unit DROP CONSTRAINT dose_unit_pkey;
ALTER TABLE ONLY public.all_hardware_downloaded DROP CONSTRAINT all_hardware_downloaded_pkey;
ALTER TABLE ONLY public.all_hardware_downloadable DROP CONSTRAINT all_hardware_downloadable_pkey;
ALTER TABLE ONLY public.alert DROP CONSTRAINT alert_pk;
ALTER TABLE ONLY public.schedule_adherence DROP CONSTRAINT adherence_pkey;
ALTER TABLE public.scheduled_dose ALTER COLUMN scheduled_dose_id DROP DEFAULT;
ALTER TABLE public.schedule_adherence ALTER COLUMN schedule_id DROP DEFAULT;
ALTER TABLE public.schedule ALTER COLUMN schedule_id DROP DEFAULT;
ALTER TABLE public.recording_device_data ALTER COLUMN data_id DROP DEFAULT;
ALTER TABLE public.recorded_dose ALTER COLUMN recorded_dose_uuid DROP DEFAULT;
ALTER TABLE public.medication ALTER COLUMN medication_id DROP DEFAULT;
ALTER TABLE public.dose_unit ALTER COLUMN dose_unit_id DROP DEFAULT;
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
DROP TABLE public.patient_adherence;
DROP TABLE public.patient;
DROP SEQUENCE public.messages_message_id_seq;
DROP SEQUENCE public.medication_scheduled_dose_id_seq;
DROP TABLE public.scheduled_dose;
DROP SEQUENCE public.medication_schedule_id_seq;
DROP TABLE public.schedule;
DROP SEQUENCE public.medication_recorded_dose_id_seq;
DROP TABLE public.recorded_dose;
DROP SEQUENCE public.medication_id_seq;
DROP TABLE public.medication;
DROP TABLE public.hardware_downloadable;
DROP TABLE public.hardware;
DROP SEQUENCE public.dose_unit_id_seq;
DROP TABLE public.dose_unit;
DROP SEQUENCE public.device_data_data_id_seq;
DROP SEQUENCE public.all_hardware_downloaded_id_seq;
DROP SEQUENCE public.all_hardware_downloaded_all_hardware_downloaded_id_seq;
DROP TABLE public.all_hardware_downloaded;
DROP SEQUENCE public.all_hardware_downloadable_id_seq;
DROP TABLE public.all_hardware_downloadable;
DROP SEQUENCE public.alert_alert_id_seq;
DROP TABLE public.alert;
DROP SEQUENCE public.adherence_schedule_id_seq;
DROP TABLE public.schedule_adherence;
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
    start_date date NOT NULL,
    end_date date,
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

INSERT INTO medication VALUES (8077011000001100, 'Mycophenolic acid 360mg gastro-resistant tablets', 'Mycophenolic acid', 'Avoid excessive exposure to UV light including sunlight.', '/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png', 0);
INSERT INTO medication VALUES (317544000, 'Docusate 100mg capsules', 'Docusate', 'Avoid excessive exposure to UV light including sunlight.', '/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png', 1);
INSERT INTO medication VALUES (327096008, 'Tacrolimus 1mg capsules', 'Tacrolimus', 'Avoid excessive exposure to UV light including sunlight.', '/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png', 1);
INSERT INTO medication VALUES (418349006, 'Prednisone 1mg tablets', 'Prednisone', 'Avoid excessive exposure to UV light including sunlight.', '/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png', 0);
INSERT INTO medication VALUES (408155001, 'Sirolimus 2mg tablets', 'Sirolimus', 'Avoid excessive exposure to UV light including sunlight.', '/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png', 0);
INSERT INTO medication VALUES (324430000, 'Trimethoprim 100mg tablets', 'Trimethoprim', 'Avoid excessive exposure to UV light including sunlight.', '/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png', 0);
INSERT INTO medication VALUES (324357002, 'Co-trimoxazole 80mg/400mg tablets', 'Co-trimoxazole', 'Avoid excessive exposure to UV light including sunlight.', '/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png', 0);
INSERT INTO medication VALUES (8791311000001100, 'Azathioprine 10mg capsules', 'Azathioprine', '! Take with food. Swallow with a glass of water.', '/home/sam/Git/MediPi/MediPiConcentrator/config/icons/capsule.png', 1);


--
-- Name: medication_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_id_seq', 3, true);


--
-- Name: medication_recorded_dose_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_recorded_dose_id_seq', 102, true);


--
-- Name: medication_schedule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_schedule_id_seq', 5, true);


--
-- Name: medication_scheduled_dose_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipiconc
--

SELECT pg_catalog.setval('medication_scheduled_dose_id_seq', 8, true);


--
-- Name: messages_message_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('messages_message_id_seq', 1, false);


--
-- Data for Name: patient; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO patient VALUES ('d9bc2478-062e-4b87-9060-4984f26b74be', 'MedWeb');


--
-- Data for Name: patient_adherence; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

INSERT INTO patient_adherence VALUES ('d9bc2478-062e-4b87-9060-4984f26b74be', 0.875, 0);


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
INSERT INTO patient_group VALUES ('MedWeb', 'MedWeb');


--
-- Name: patient_hardware_downloadable_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('patient_hardware_downloadable_id_seq', 1, false);


--
-- Data for Name: recorded_dose; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

INSERT INTO recorded_dose VALUES ('29', 2, 4, 0, '14:00:00');
INSERT INTO recorded_dose VALUES ('31', 2, 8, 0, '14:00:00');
INSERT INTO recorded_dose VALUES ('30', 2, 6, 0, '11:00:00');
INSERT INTO recorded_dose VALUES ('28', 2, 2, 0, '14:30:00');
INSERT INTO recorded_dose VALUES ('32', 2, 7, 0, '11:00:00');
INSERT INTO recorded_dose VALUES ('33', 2, 4, 1, '14:00:00');
INSERT INTO recorded_dose VALUES ('34', 2, 8, 1, '14:00:00');
INSERT INTO recorded_dose VALUES ('35', 2, 6, 1, '11:00:00');
INSERT INTO recorded_dose VALUES ('36', 2, 2, 1, '14:30:00');
INSERT INTO recorded_dose VALUES ('37', 2, 7, 1, '11:00:00');
INSERT INTO recorded_dose VALUES ('38', 2, 4, 2, '14:00:00');
INSERT INTO recorded_dose VALUES ('39', 2, 8, 2, '14:00:00');
INSERT INTO recorded_dose VALUES ('40', 2, 6, 2, '11:00:00');
INSERT INTO recorded_dose VALUES ('42', 2, 7, 2, '11:00:00');
INSERT INTO recorded_dose VALUES ('44', 2, 8, 3, '14:00:00');
INSERT INTO recorded_dose VALUES ('45', 2, 6, 3, '11:00:00');
INSERT INTO recorded_dose VALUES ('46', 2, 2, 3, '14:30:00');
INSERT INTO recorded_dose VALUES ('49', 2, 7, 0, '17:00:00');
INSERT INTO recorded_dose VALUES ('50', 2, 7, 2, '17:00:00');
INSERT INTO recorded_dose VALUES ('51', 2, 7, 3, '17:00:00');
INSERT INTO recorded_dose VALUES ('53', 2, 8, 5, '14:00:00');
INSERT INTO recorded_dose VALUES ('54', 2, 7, 4, '17:00:00');
INSERT INTO recorded_dose VALUES ('55', 2, 7, 5, '17:00:00');
INSERT INTO recorded_dose VALUES ('56', 2, 6, 4, '11:00:00');
INSERT INTO recorded_dose VALUES ('57', 2, 6, 5, '11:00:00');
INSERT INTO recorded_dose VALUES ('59', 2, 4, 5, '14:00:00');
INSERT INTO recorded_dose VALUES ('61', 2, 2, 5, '14:30:00');
INSERT INTO recorded_dose VALUES ('62', 2, 7, 4, '11:00:00');
INSERT INTO recorded_dose VALUES ('63', 2, 7, 5, '11:00:00');
INSERT INTO recorded_dose VALUES ('64', 2, 7, 3, '11:00:00');
INSERT INTO recorded_dose VALUES ('f070c0a3-1e1c-4ad4-abd9-2249e72c47a3', 2, 7, 6, '11:03:14');
INSERT INTO recorded_dose VALUES ('a2eef40c-d4ab-4120-9378-d33bd25526be', 2, 4, 6, '14:00:56');
INSERT INTO recorded_dose VALUES ('c312e519-30df-4c14-8c63-3b3004064a84', 2, 6, 7, '21:54:04');
INSERT INTO recorded_dose VALUES ('9458d323-b36c-41f3-8f8f-2be19a707ad9', 2, 13, 0, '21:51:07');
INSERT INTO recorded_dose VALUES ('65', 2, 7, 8, '17:00:00');
INSERT INTO recorded_dose VALUES ('66', 2, 6, 8, '11:00:00');
INSERT INTO recorded_dose VALUES ('67', 2, 4, 8, '14:00:00');
INSERT INTO recorded_dose VALUES ('68', 2, 2, 8, '14:30:00');
INSERT INTO recorded_dose VALUES ('69', 2, 7, 8, '11:00:00');
INSERT INTO recorded_dose VALUES ('70', 2, 2, 6, '14:30:00');
INSERT INTO recorded_dose VALUES ('71', 2, 8, 7, '13:44:20');
INSERT INTO recorded_dose VALUES ('72', 2, 8, 8, '13:44:20');
INSERT INTO recorded_dose VALUES ('73', 2, 8, 4, '13:44:20');
INSERT INTO recorded_dose VALUES ('74', 2, 13, 1, '21:51:07');
INSERT INTO recorded_dose VALUES ('75', 2, 13, 2, '21:51:07');
INSERT INTO recorded_dose VALUES ('76', 2, 13, 3, '21:51:07');
INSERT INTO recorded_dose VALUES ('77', 2, 13, 4, '21:51:07');
INSERT INTO recorded_dose VALUES ('78', 2, 13, 5, '21:51:07');
INSERT INTO recorded_dose VALUES ('79', 2, 13, 6, '21:51:07');
INSERT INTO recorded_dose VALUES ('81', 2, 13, 0, '12:51:07');
INSERT INTO recorded_dose VALUES ('82', 2, 13, 1, '12:51:07');
INSERT INTO recorded_dose VALUES ('83', 2, 13, 2, '12:51:07');
INSERT INTO recorded_dose VALUES ('84', 2, 13, 3, '12:51:07');
INSERT INTO recorded_dose VALUES ('85', 2, 13, 4, '12:51:07');
INSERT INTO recorded_dose VALUES ('86', 2, 13, 5, '12:51:07');
INSERT INTO recorded_dose VALUES ('87', 2, 13, 6, '12:51:07');
INSERT INTO recorded_dose VALUES ('80', 2, 13, 7, '12:51:07');
INSERT INTO recorded_dose VALUES ('88', 2, 13, 7, '21:51:07');
INSERT INTO recorded_dose VALUES ('96', 1, 15, 7, '12:00:00');
INSERT INTO recorded_dose VALUES ('95', 1, 15, 6, '12:00:00');
INSERT INTO recorded_dose VALUES ('93', 1, 15, 5, '12:00:00');
INSERT INTO recorded_dose VALUES ('94', 1, 15, 4, '12:00:00');
INSERT INTO recorded_dose VALUES ('92', 1, 15, 3, '12:00:00');
INSERT INTO recorded_dose VALUES ('91', 1, 15, 2, '12:00:00');
INSERT INTO recorded_dose VALUES ('90', 1, 15, 1, '12:00:00');
INSERT INTO recorded_dose VALUES ('89', 1, 15, 0, '12:00:00');
INSERT INTO recorded_dose VALUES ('97', 2, 4, 7, '14:00:00');
INSERT INTO recorded_dose VALUES ('98', 1, 15, 8, '12:00:00');
INSERT INTO recorded_dose VALUES ('99', 2, 2, 2, '14:30:00');
INSERT INTO recorded_dose VALUES ('100', 2, 2, 7, '14:30:00');
INSERT INTO recorded_dose VALUES ('101', 2, 2, 4, '14:30:00');
INSERT INTO recorded_dose VALUES ('102', 1, 15, 9, '12:00:00');


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

INSERT INTO schedule VALUES (8, '2017-09-05', NULL, '', 'To prevent rejection', 8791311000001100, 'd9bc2478-062e-4b87-9060-4984f26b74be', NULL, NULL);
INSERT INTO schedule VALUES (12, '2017-09-05', '2017-09-30', 'Colace', 'To reduce constipation', 317544000, 'd9bc2478-062e-4b87-9060-4984f26b74be', NULL, NULL);
INSERT INTO schedule VALUES (15, '2017-09-05', NULL, '', 'To prevent rejection', 408155001, 'd9bc2478-062e-4b87-9060-4984f26b74be', NULL, NULL);
INSERT INTO schedule VALUES (13, '2017-09-05', NULL, '', 'To prevent rejection', 8077011000001100, 'd9bc2478-062e-4b87-9060-4984f26b74be', NULL, NULL);
INSERT INTO schedule VALUES (2, '2017-09-05', NULL, 'Bactrim', 'To prevent infection', 324357002, 'd9bc2478-062e-4b87-9060-4984f26b74be', NULL, NULL);


--
-- Data for Name: schedule_adherence; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

INSERT INTO schedule_adherence VALUES (12, NULL, NULL);
INSERT INTO schedule_adherence VALUES (8, 0.83333333333333337, 3);
INSERT INTO schedule_adherence VALUES (13, 0.769230769230769273, 0);
INSERT INTO schedule_adherence VALUES (2, 1, 7);
INSERT INTO schedule_adherence VALUES (15, 1, 7);


--
-- Data for Name: scheduled_dose; Type: TABLE DATA; Schema: public; Owner: medipiconc
--

INSERT INTO scheduled_dose VALUES (17, 15, 1, 0, 1, 20, '10:00:00', '14:00:00', '10:00:00', '10:00:00', NULL, NULL);
INSERT INTO scheduled_dose VALUES (9, 13, 2, 0, 1, NULL, '11:00:00', '12:55:00', '15:00:00', '15:00:00', NULL, NULL);
INSERT INTO scheduled_dose VALUES (8, 8, 2, 0, 1, NULL, '12:00:00', '19:00:00', '13:00:00', '13:00:00', NULL, NULL);
INSERT INTO scheduled_dose VALUES (2, 2, 2, 0, 1, NULL, '14:00:00', '19:00:00', '14:01:00', '18:00:00', NULL, NULL);
INSERT INTO scheduled_dose VALUES (10, 13, 2, 0, 1, NULL, '19:00:00', '23:00:00', '18:00:00', '18:00:00', NULL, NULL);


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

