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
ALTER TABLE ONLY public.medication_scheduled_dose DROP CONSTRAINT medication_scheduled_dose_medication_schedule_id_fk;
ALTER TABLE ONLY public.medication_recorded_dose DROP CONSTRAINT medication_recorded_dose_medication_schedule_id_fk;
ALTER TABLE ONLY public.medication_adherence DROP CONSTRAINT medication_adherence_medication_schedule_id_fk;
DROP INDEX public.schedule_id_uindex;
DROP INDEX public.medication_unique_name_uindex;
DROP INDEX public.medication_scheduled_dose_id_uindex;
DROP INDEX public.medication_recorded_dose_id_uindex;
DROP INDEX public.medication_id_uindex;
ALTER TABLE ONLY public.medication_schedule DROP CONSTRAINT schedule_pkey;
ALTER TABLE ONLY public.medication_scheduled_dose DROP CONSTRAINT medication_scheduled_dose_pkey;
ALTER TABLE ONLY public.medication_recorded_dose DROP CONSTRAINT medication_recorded_dose_pkey;
ALTER TABLE ONLY public.medication DROP CONSTRAINT medication_pkey;
ALTER TABLE public.medication_scheduled_dose ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.medication_schedule ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.medication_recorded_dose ALTER COLUMN id DROP DEFAULT;
ALTER TABLE public.medication ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE public.medication_scheduled_dose_id_seq;
DROP TABLE public.medication_scheduled_dose;
DROP SEQUENCE public.medication_schedule_id_seq;
DROP TABLE public.medication_schedule;
DROP SEQUENCE public.medication_recorded_dose_id_seq;
DROP TABLE public.medication_recorded_dose;
DROP SEQUENCE public.medication_id_seq;
DROP TABLE public.medication_adherence;
DROP TABLE public.medication;
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
-- Name: medication; Type: TABLE; Schema: public; Owner: medipipatient
--

CREATE TABLE medication (
    id integer NOT NULL,
    unique_name character varying NOT NULL,
    display_name character varying NOT NULL,
    advisory_stmt character varying NOT NULL,
    icon integer
);


ALTER TABLE medication OWNER TO medipipatient;

--
-- Name: medication_adherence; Type: TABLE; Schema: public; Owner: medipipatient
--

CREATE TABLE medication_adherence (
    schedule integer NOT NULL,
    sevenday_adherence double precision,
    streak_length double precision
);


ALTER TABLE medication_adherence OWNER TO medipipatient;

--
-- Name: medication_id_seq; Type: SEQUENCE; Schema: public; Owner: medipipatient
--

CREATE SEQUENCE medication_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE medication_id_seq OWNER TO medipipatient;

--
-- Name: medication_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: medipipatient
--

ALTER SEQUENCE medication_id_seq OWNED BY medication.id;


--
-- Name: medication_recorded_dose; Type: TABLE; Schema: public; Owner: medipipatient
--

CREATE TABLE medication_recorded_dose (
    id integer NOT NULL,
    value double precision NOT NULL,
    timetaken timestamp without time zone NOT NULL,
    schedule integer NOT NULL
);


ALTER TABLE medication_recorded_dose OWNER TO medipipatient;

--
-- Name: medication_recorded_dose_id_seq; Type: SEQUENCE; Schema: public; Owner: medipipatient
--

CREATE SEQUENCE medication_recorded_dose_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE medication_recorded_dose_id_seq OWNER TO medipipatient;

--
-- Name: medication_recorded_dose_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: medipipatient
--

ALTER SEQUENCE medication_recorded_dose_id_seq OWNED BY medication_recorded_dose.id;


--
-- Name: medication_schedule; Type: TABLE; Schema: public; Owner: medipipatient
--

CREATE TABLE medication_schedule (
    id integer NOT NULL,
    startdate date NOT NULL,
    enddate date,
    alternatename character varying,
    purposestatement character varying NOT NULL,
    medication integer NOT NULL
);


ALTER TABLE medication_schedule OWNER TO medipipatient;

--
-- Name: medication_schedule_id_seq; Type: SEQUENCE; Schema: public; Owner: medipipatient
--

CREATE SEQUENCE medication_schedule_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE medication_schedule_id_seq OWNER TO medipipatient;

--
-- Name: medication_schedule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: medipipatient
--

ALTER SEQUENCE medication_schedule_id_seq OWNED BY medication_schedule.id;


--
-- Name: medication_scheduled_dose; Type: TABLE; Schema: public; Owner: medipipatient
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


ALTER TABLE medication_scheduled_dose OWNER TO medipipatient;

--
-- Name: medication_scheduled_dose_id_seq; Type: SEQUENCE; Schema: public; Owner: medipipatient
--

CREATE SEQUENCE medication_scheduled_dose_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE medication_scheduled_dose_id_seq OWNER TO medipipatient;

--
-- Name: medication_scheduled_dose_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: medipipatient
--

ALTER SEQUENCE medication_scheduled_dose_id_seq OWNED BY medication_scheduled_dose.id;


--
-- Name: medication id; Type: DEFAULT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication ALTER COLUMN id SET DEFAULT nextval('medication_id_seq'::regclass);


--
-- Name: medication_recorded_dose id; Type: DEFAULT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication_recorded_dose ALTER COLUMN id SET DEFAULT nextval('medication_recorded_dose_id_seq'::regclass);


--
-- Name: medication_schedule id; Type: DEFAULT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication_schedule ALTER COLUMN id SET DEFAULT nextval('medication_schedule_id_seq'::regclass);


--
-- Name: medication_scheduled_dose id; Type: DEFAULT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication_scheduled_dose ALTER COLUMN id SET DEFAULT nextval('medication_scheduled_dose_id_seq'::regclass);


--
-- Data for Name: medication; Type: TABLE DATA; Schema: public; Owner: medipipatient
--

COPY medication (id, unique_name, display_name, advisory_stmt, icon) FROM stdin;
\.


--
-- Data for Name: medication_adherence; Type: TABLE DATA; Schema: public; Owner: medipipatient
--

COPY medication_adherence (schedule, sevenday_adherence, streak_length) FROM stdin;
\.


--
-- Name: medication_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipipatient
--

SELECT pg_catalog.setval('medication_id_seq', 1, false);


--
-- Data for Name: medication_recorded_dose; Type: TABLE DATA; Schema: public; Owner: medipipatient
--

COPY medication_recorded_dose (id, value, timetaken, schedule) FROM stdin;
\.


--
-- Name: medication_recorded_dose_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipipatient
--

SELECT pg_catalog.setval('medication_recorded_dose_id_seq', 1, false);


--
-- Data for Name: medication_schedule; Type: TABLE DATA; Schema: public; Owner: medipipatient
--

COPY medication_schedule (id, startdate, enddate, alternatename, purposestatement, medication) FROM stdin;
\.


--
-- Name: medication_schedule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipipatient
--

SELECT pg_catalog.setval('medication_schedule_id_seq', 1, false);


--
-- Data for Name: medication_scheduled_dose; Type: TABLE DATA; Schema: public; Owner: medipipatient
--

COPY medication_scheduled_dose (id, schedule, value, startday, repeatinterval, endday, windowstarttime, windowendtime, defaultremindertime, remindertime) FROM stdin;
\.


--
-- Name: medication_scheduled_dose_id_seq; Type: SEQUENCE SET; Schema: public; Owner: medipipatient
--

SELECT pg_catalog.setval('medication_scheduled_dose_id_seq', 1, false);


--
-- Name: medication medication_pkey; Type: CONSTRAINT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication
    ADD CONSTRAINT medication_pkey PRIMARY KEY (id);


--
-- Name: medication_recorded_dose medication_recorded_dose_pkey; Type: CONSTRAINT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication_recorded_dose
    ADD CONSTRAINT medication_recorded_dose_pkey PRIMARY KEY (id);


--
-- Name: medication_scheduled_dose medication_scheduled_dose_pkey; Type: CONSTRAINT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication_scheduled_dose
    ADD CONSTRAINT medication_scheduled_dose_pkey PRIMARY KEY (id);


--
-- Name: medication_schedule schedule_pkey; Type: CONSTRAINT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication_schedule
    ADD CONSTRAINT schedule_pkey PRIMARY KEY (id);


--
-- Name: medication_id_uindex; Type: INDEX; Schema: public; Owner: medipipatient
--

CREATE UNIQUE INDEX medication_id_uindex ON medication USING btree (id);


--
-- Name: medication_recorded_dose_id_uindex; Type: INDEX; Schema: public; Owner: medipipatient
--

CREATE UNIQUE INDEX medication_recorded_dose_id_uindex ON medication_recorded_dose USING btree (id);


--
-- Name: medication_scheduled_dose_id_uindex; Type: INDEX; Schema: public; Owner: medipipatient
--

CREATE UNIQUE INDEX medication_scheduled_dose_id_uindex ON medication_scheduled_dose USING btree (id);


--
-- Name: medication_unique_name_uindex; Type: INDEX; Schema: public; Owner: medipipatient
--

CREATE UNIQUE INDEX medication_unique_name_uindex ON medication USING btree (unique_name);


--
-- Name: schedule_id_uindex; Type: INDEX; Schema: public; Owner: medipipatient
--

CREATE UNIQUE INDEX schedule_id_uindex ON medication_schedule USING btree (id);


--
-- Name: medication_adherence medication_adherence_medication_schedule_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication_adherence
    ADD CONSTRAINT medication_adherence_medication_schedule_id_fk FOREIGN KEY (schedule) REFERENCES medication_schedule(id);


--
-- Name: medication_recorded_dose medication_recorded_dose_medication_schedule_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication_recorded_dose
    ADD CONSTRAINT medication_recorded_dose_medication_schedule_id_fk FOREIGN KEY (schedule) REFERENCES medication_schedule(id);


--
-- Name: medication_scheduled_dose medication_scheduled_dose_medication_schedule_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication_scheduled_dose
    ADD CONSTRAINT medication_scheduled_dose_medication_schedule_id_fk FOREIGN KEY (schedule) REFERENCES medication_schedule(id);


--
-- Name: medication_schedule schedule_medication_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: medipipatient
--

ALTER TABLE ONLY medication_schedule
    ADD CONSTRAINT schedule_medication_id_fk FOREIGN KEY (medication) REFERENCES medication(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

