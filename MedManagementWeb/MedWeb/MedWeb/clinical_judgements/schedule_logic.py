import datetime


def patient_active_schedules(patient):
    return [sch for sch in patient.schedules if sch.end_date is None or sch.end_date > datetime.date.today()]

def patient_active_medication_ids(patient):
    return [sch.medication.id for sch in patient.schedules if sch.end_date is None or sch.end_date > datetime.date.today()]