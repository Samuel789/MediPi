create sequence all_hardware_downloadable_id_seq
;

create sequence all_hardware_downloaded_id_seq
;

create sequence device_data_data_id_seq
;

create sequence messages_message_id_seq
;

create sequence patient_hardware_downloadable_id_seq
;

create sequence recording_device_attribute_attribute_id_seq
;

create sequence recording_device_type_type_id_seq
;

create table alert
(
	alert_id bigserial not null
		constraint alert_pk
			primary key,
	data_id bigint not null,
	patient_uuid varchar(100) not null,
	alert_time timestamp with time zone not null,
	alert_text varchar(5000) not null,
	transmit_success_date timestamp with time zone,
	retry_attempts integer not null
)
;

create table all_hardware_downloadable
(
	downloadable_uuid varchar(100) not null
		constraint all_hardware_downloadable_pkey
			primary key,
	version varchar(100) not null,
	version_author varchar(100) not null,
	version_date timestamp with time zone not null,
	script_location varchar(100) not null,
	signature varchar(10000) not null
)
;

create table all_hardware_downloaded
(
	all_hardware_downloaded_id serial not null
		constraint all_hardware_downloaded_pkey
			primary key,
	downloaded_date timestamp with time zone,
	downloadable_uuid varchar(100) not null
		constraint all_hardware_downloadable_all_hardware_downloaded_fk
			references all_hardware_downloadable,
	hardware_name varchar(100) not null
)
;

create table hardware
(
	hardware_name varchar(100) not null
		constraint hardware_pkey
			primary key,
	mac_address varchar(100) not null,
	current_software_version varchar(100) not null,
	patient_uuid varchar(100)
)
;

alter table all_hardware_downloaded
	add constraint hardware_all_hardware_downloaded_fk
		foreign key (hardware_name) references hardware
;

create table hardware_downloadable
(
	downloadable_uuid varchar(100) not null
		constraint hardware_downloadable_pkey
			primary key,
	hardware_name varchar(100) not null
		constraint hardware_hardware_downloadable_fk
			references hardware,
	version varchar(100) not null,
	version_author varchar(100) not null,
	version_date timestamp with time zone not null,
	downloaded_date timestamp with time zone,
	script_location varchar(100) not null,
	signature varchar(10000) not null
)
;

create table patient
(
	patient_uuid varchar(100) not null
		constraint patient_id_pk
			primary key,
	patient_group_uuid varchar
)
;

alter table alert
	add constraint patient_alert_fk
		foreign key (patient_uuid) references patient
;

alter table hardware
	add constraint patient_hardware_fk
		foreign key (patient_uuid) references patient
;

create table patient_certificate
(
	patient_uuid varchar(100) not null
		constraint patient_certificate_pk
			primary key
		constraint patient_patient_certificates_fk
			references patient,
	certificate_location varchar(1000) not null
)
;

create table patient_downloadable
(
	downloadable_uuid varchar(100) not null
		constraint patient_downloadable_pk
			primary key,
	patient_uuid varchar(100) not null
		constraint patient_patient_downloadable_fk
			references patient,
	version varchar(100) not null,
	version_author varchar(255) not null,
	version_date timestamp with time zone not null,
	downloaded_date timestamp with time zone,
	script_location varchar(100) not null,
	signature varchar(10000) not null
)
;

create table patient_group
(
	patient_group_uuid varchar(100) not null
		constraint patient_group_pk
			primary key,
	patient_group_name varchar(100)
)
;

alter table patient
	add constraint patient_group_patient_fk
		foreign key (patient_group_uuid) references patient_group
;

create table recording_device_attribute
(
	attribute_id integer default nextval('recording_device_attribute_attribute_id_seq'::regclass) not null
		constraint recording_device_attribute_pkey
			primary key,
	type_id integer not null,
	attribute_name varchar(100) not null,
	attribute_units varchar(100),
	attribute_type varchar(100) not null
)
;

create table recording_device_data
(
	data_id bigserial not null
		constraint recording_device_data_pkey
			primary key,
	attribute_id integer not null
		constraint recording_device_attribute_recording_device_data_fk
			references recording_device_attribute,
	data_value varchar(1000) not null,
	patient_uuid varchar(100) not null
		constraint patient_recording_device_data_fk
			references patient,
	data_value_time timestamp with time zone not null,
	downloaded_time timestamp with time zone default ('now'::text)::timestamp(3) with time zone not null,
	schedule_effective_time timestamp with time zone,
	schedule_expiry_time timestamp with time zone
)
;

alter table alert
	add constraint recording_device_data_alert_fk
		foreign key (data_id) references recording_device_data
;

create table recording_device_type
(
	type_id integer default nextval('recording_device_type_type_id_seq'::regclass) not null
		constraint recording_device_type_pkey
			primary key,
	type varchar(100) not null,
	make varchar(100) not null,
	model varchar(100) not null,
	display_name varchar(1000) not null
)
;

alter table recording_device_attribute
	add constraint recording_device_type_recording_device_attribute_fk
		foreign key (type_id) references recording_device_type
;

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
			references medication,
	patient varchar
		constraint medication_schedule_patient_patient_uuid_fk
			references patient
)
;

create unique index schedule_id_uindex
	on medication_schedule (id)
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


