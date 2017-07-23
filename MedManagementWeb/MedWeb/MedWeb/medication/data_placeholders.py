from datetime import date

from copy import copy
def get_medication_by_name(full_name):
    for medication in medications_set:
        print(medication.full_name, full_name)
        if medication.full_name == full_name:
            print("Hit")
            return medication
    print("Fail")
    return None

def get_patient_by_name(full_name):
    for patient in patients_set:
        print(full_name, patient.name)
        if patient.name == full_name:
            return patient
    return None

class Patient:
    patient_count = 0
    def __init__(self, name, dob, nhs_number):
        self.name = name
        self.dob = dob
        self.nhs_number = nhs_number
        self.warnings = ["fesfes", "fsfe"]
        self.adherence_percentage = 30
        self.adherence_streak = 4
        self.schedules = set()
        self.id = copy(Patient.patient_count)
        Patient.patient_count += 1

class Medication:
    medication_count = 0

    def __init__(self, full_name, short_name, cautionary_text, dose_unit):
        self.short_name = short_name
        self.full_name = full_name
        self.cautionary_text = cautionary_text
        self.icon = None
        self.dose_unit = dose_unit
        self.id = copy(Medication.medication_count)
        Medication.medication_count += 1

class Schedule:
    schedule_count = 0

    def __init__(self, start_date, end_date, alternate_name, purpose_statement, patient, medication):
        self.start_date = start_date
        self.end_date = end_date
        self.alternate_name = alternate_name
        self.purpose_statement = purpose_statement
        self.patient = patient
        self.adherence_percentage = 30
        self.adherence_streak = 4
        self.medication = medication
        self.id = copy(Schedule.schedule_count)
        Schedule.schedule_count += 1

patients_set = {Patient("Mr John Smith", date(1974, 1, 23), "FBJTEK2353"),
            Patient("Miss Karen Brady", date(1988, 6, 21), "FBJTEK2353"),
            Patient("Mrs Justine Adams", date(1974, 1, 23), "FBJTEK2353"),
            Patient("Mr Tim Orphen", date(1974, 1, 23), "FBJTEK2353"),
            Patient("Mrs Sandra Liu", date(1974, 1, 23), "FBJTEK2353")}

patients = {}
for patient in patients_set:
    patients[patient.id] = patient

medications_set = {Medication("Mycophenolate 360mg tablets", "Mycophenolate", "", "tablet"),
                   Medication("Docusate 100mg capsules", "Docusate", "", "capsule"),
                   Medication("Sulfamethoxazole 80mg tablets", "Sulfamethoxazole", "", "tablet"),
                   Medication("Tacrolimus 1mg capsules", "Tacrolimus", "", "capsule"),
                   Medication("Prednisone 1mg capsules", "Prednisone", "", "tablet")}
medications = {}
for medication in medications_set:
    medications[medication.id] = medication

schedules_set = {Schedule(date(2017, 5, 21), date(2017, 9, 21), "Myfortic", "To prevent rejection", get_patient_by_name("Mr Tim Orphen"), get_medication_by_name("Mycophenolate 360mg tablets")),
                 Schedule(date(2017, 5, 23), date(2017, 9, 23), None, "To prevent rejection", get_patient_by_name("Mr Tim Orphen"), get_medication_by_name("Tacrolimus 1mg capsules"))}
schedules = {}
for schedule in schedules_set:
    schedules[schedule.id] = schedule
    schedule.patient.schedules.add(schedule)

