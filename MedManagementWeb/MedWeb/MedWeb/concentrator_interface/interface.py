import json
from datetime import datetime
from enum import Enum

import dateparser as dateparser
import requests

from MedWeb import settings
from MedWeb.clinical_database.clinical_database import medications
from MedWeb.clinical_judgements.warnings import count_warnings
from MedWeb.concentrator_interface import entities
from MedWeb.concentrator_interface.entities import Schedule, ScheduledDose
from MedWeb.patient_database.entities import Status
from MedWeb.patient_database.patient_database import patients


class SyncStatus(Enum):
    not_yet_synched = -1
    synched = 0
    error = 1


schedules = {}
last_synched = None
sync_status = SyncStatus.not_yet_synched


def _reset_existing_data():
    schedules.clear()
    for patient_uuid in patients:
        patients[patient_uuid].status = Status.not_registered_with_concentrator
        patients[patient_uuid].adherence = None
        patients[patient_uuid].schedules.clear()


def _download_concentrator_data():
    url = settings.MEDIPI_CONCENTRATOR_ADDRESS + 'medication/clinician/getPatientData'
    response = requests.get(url, cert=(settings.SIGN_CERT_PATH, settings.SIGN_KEY_PATH), verify=False)
    medication_info = json.loads(response.text)
    deserialized_data = {}
    deserialized_data["schedules"] = [entities.from_dict(entities.Schedule, schedule_data) for schedule_data in
                                      medication_info["schedules"]]
    deserialized_data["patient_adherence_objects"] = [
        entities.from_dict(entities.PatientAdherenceObject, adherence_data) for adherence_data in
        medication_info["patientAdherenceList"]]
    deserialized_data["registered_patient_uuids"] = medication_info["patientUuids"]
    return deserialized_data


def _update_patient_data(registered_patients, patient_adherence_objects):
    for patient_uuid in registered_patients:
        try:
            patients[patient_uuid].status = Status.never_synched
        except KeyError as e:
            print(
                "Patient with UUID '%s' not registered with MedWeb but present in Concentrator, ignoring." % patient_uuid)
    for adherence_object in patient_adherence_objects:
        patient_uuid = adherence_object.patient_uuid
        patients[patient_uuid].status = Status.normal
        patients[patient_uuid].adherence = adherence_object


def _update_schedule_data(schedule_list):
    for schedule in schedule_list:
        schedule.patient = patients[schedule.patient_uuid]
        schedule.patient.schedules.add(schedule)
        schedule.medication = medications[schedule.medication_id]
        for recorded_dose in schedule.recorded_doses:
            recorded_dose.schedule = schedule
        for scheduled_dose in schedule.scheduled_doses:
            scheduled_dose.schedule = schedule

        schedules[schedule.id] = schedule


def update_from_concentrator():
    try:
        _reset_existing_data()
        concentrator_data = _download_concentrator_data()
        _update_patient_data(concentrator_data["registered_patient_uuids"],
                             concentrator_data["patient_adherence_objects"])
        _update_schedule_data(concentrator_data["schedules"])
        for patient in patients.values():
            patient.warnings = count_warnings(patient)
    except(Exception) as e:
        print("SYNC FAILED - RESTORING BACKUP DATA")
        schedules.clear()
        last_synched = None
        sync_status = SyncStatus.error
        raise e
    last_synched = datetime.now()
    sync_status = SyncStatus.synched
    return


def send_to_concentrator(schedule, doses):
    url = settings.MEDIPI_CONCENTRATOR_ADDRESS + 'medication/clinician/addSchedule'
    medicationDo = MedicationScheduleDO(schedule, doses)
    json = medicationDo.as_JSON()
    print(json)
    requests.post(url, data=json, cert=(settings.SIGN_CERT_PATH, settings.SIGN_KEY_PATH), verify=False,
                  headers={'Content-type': 'application/json'}, timeout=7000)
    update_from_concentrator()


def get_patient_dose_instances(patient_uuid, start_date, end_date):
    url = settings.MEDIPI_CONCENTRATOR_ADDRESS + 'medication/clinician/unpackPatientSchedules'
    data = json.loads(
        requests.get(url, params={"patientUuid": patient_uuid, "startDate": start_date, "endDate": end_date},
                     cert=(settings.SIGN_CERT_PATH, settings.SIGN_KEY_PATH), verify=False).text)
    print(data)
    dose_instances = [entities.from_dict(entities.DoseInstance, dose_instance_data) for dose_instance_data in data]
    return dose_instances


class MedicationScheduleDO:
    def __init__(self, schedule, doses):
        schedule_reverse_keys = {value: key for key, value in Schedule._json_attribute_map.items()}
        dose_reverse_keys = {value: key for key, value in ScheduledDose._json_attribute_map.items()}

        schedule_data = {schedule_reverse_keys[key]: value for key, value in schedule.__dict__.items() if
                         key in schedule_reverse_keys}
        self.medication_id = schedule.medication_id
        self.schedule = json.dumps(schedule_data)
        dose_data = [{dose_reverse_keys[key]: value for key, value in dose.__dict__.items() if key in dose_reverse_keys}
                     for dose in doses]
        for dose in dose_data:
            dose["windowStartTime"] = dateparser.parse(dose["windowStartTime"]).strftime("%H:%M:%S")
            dose["windowEndTime"] = dateparser.parse(dose["windowEndTime"]).strftime("%H:%M:%S")
            dose["reminderTime"] = dateparser.parse(dose["reminderTime"]).strftime("%H:%M:%S")
            dose["defaultReminderTime"] = dateparser.parse(dose["defaultReminderTime"]).strftime("%H:%M:%S")
        self.doses = json.dumps(dose_data)

    def as_JSON(self):
        return "{\"schedule\": %s, \"doses\": %s, \"medicationId\": %s}" % (
        self.schedule, self.doses, self.medication_id)
