create table medication
(
	id serial not null
		constraint medication_pkey
			primary key,
	unique_name varchar not null,
	display_name varchar not null,
	advisory_stmt varchar not null,
	icon integer
)
;

create unique index medication_id_uindex
	on medication (id)
;

create unique index medication_unique_name_uindex
	on medication (unique_name)
;

create table medication_schedule
(
	id serial not null
		constraint schedule_pkey
			primary key,
	startdate date not null,
	enddate date,
	alternatename varchar,
	purposestatement varchar not null,
	medication integer not null
		constraint schedule_medication_id_fk
			references medication
)
;

create unique index schedule_id_uindex
	on medication_schedule (id)
;

create table medication_adherence
(
	schedule integer not null
		constraint medication_adherence_medication_schedule_id_fk
			references medication_schedule,
	sevenday_adherence double precision,
	streak_length double precision
)
;

create table medication_scheduled_dose
(
	id serial not null
		constraint medication_scheduled_dose_pkey
			primary key,
	schedule integer not null
		constraint medication_scheduled_dose_medication_schedule_id_fk
			references medication_schedule,
	value double precision not null,
	startday date not null,
	repeatinterval integer,
	endday date,
	windowstarttime time not null,
	windowendtime time not null,
	defaultremindertime time not null,
	remindertime time not null
)
;

create unique index medication_scheduled_dose_id_uindex
	on medication_scheduled_dose (id)
;

create table medication_recorded_dose
(
	id serial not null
		constraint medication_recorded_dose_pkey
			primary key,
	value double precision not null,
	timetaken timestamp not null,
	schedule integer not null
		constraint medication_recorded_dose_medication_schedule_id_fk
			references medication_schedule
)
;

create unique index medication_recorded_dose_id_uindex
	on medication_recorded_dose (id)
;


