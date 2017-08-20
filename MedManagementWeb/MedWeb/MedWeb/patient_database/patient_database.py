from MedWeb.patient_database.entities import Patient
from MedWeb.patient_database.patient_data import patient_data

patients = {}


def reload():
    patients.clear()
    for patient in patient_data:
        patients[patient["uuid"]] = Patient(**patient)


reload()
