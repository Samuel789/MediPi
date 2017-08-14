import json
from datetime import datetime
from enum import Enum

import requests

from MedWeb import settings
from MedWeb.clinical_database.clinical_database import medications
from MedWeb.clinical_database.medication_data import medication_data
from MedWeb.concentrator_interface import entities
from MedWeb.patient_database.entities import Status
from MedWeb.patient_database.patient_data import patient_data
from MedWeb.patient_database.patient_database import patients


class SyncStatus(Enum):
    not_yet_synched = -1
    synched = 0
    error = 1

schedules = {}
last_synched = None
sync_status = SyncStatus.not_yet_synched

def _download_concentrator_data():
    url = settings.MEDIPI_CONCENTRATOR_ADDRESS + 'medication/clinician/getPatientData'
    response = requests.get(url, cert=(settings.SIGN_CERT_PATH, settings.SIGN_KEY_PATH), verify=False)
    medication_info = json.loads(response.text)
    deserialized_data = {}
    deserialized_data["schedules"] = [entities.fromDict(entities.Schedule, schedule_data) for schedule_data in medication_info["schedules"]]
    deserialized_data["patient_adherence_objects"] = [entities.fromDict(entities.PatientAdherenceObject, adherence_data) for adherence_data in medication_info["patientAdherenceList"]]
    deserialized_data["registered_patient_uuids"] = medication_info["patientUuids"]
    return deserialized_data

def update_patient_data(registered_patients, patient_adherence_objects):
    for patient_uuid in registered_patients:
        try:
            patients[patient_uuid].status = Status.never_synched
        except (KeyError):
            print("WARNING: Patient " + patient_uuid + " registered with Concentrator but not in patient database")
    for adherence_object in patient_adherence_objects:
        patient_uuid = adherence_object.patient_uuid
        patients[patient_uuid].status = Status.normal
        patients[patient_uuid].adherence = adherence_object

def update_schedule_data(schedules):
    for schedule in schedules:
        schedule.patient = patients[schedule.patient_uuid]
        schedule.medication = medications[schedule.medication_id]
        for recorded_dose in schedule.recorded_doses:
            recorded_dose.schedule = schedule
        for scheduled_dose in schedule.scheduled_doses:
            scheduled_dose.schedule = schedule
        schedules[schedule.id] = schedule

def update_from_concentrator():
    try:
        concentrator_data = _download_concentrator_data()
        update_patient_data(concentrator_data["registered_patient_uuids"], concentrator_data["patient_adherence_objects"])
        update_schedule_data(concentrator_data["schedules"])
    except(Exception) as e:
        print("SYNC FAILED - RESTORING BACKUP DATA")
        schedules.clear()
        last_synched = None
        sync_status = SyncStatus.error
        raise e
    last_synched = datetime.now()
    sync_status = SyncStatus.synched
    return