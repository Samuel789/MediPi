import datetime

"""Utilities to identify active schedules and medications. An active schedule is one which has not ended on or before
    the current day, and an active medication is a medication included in an active schedule"""

def patient_active_schedules(patient):
    return [sch for sch in patient.schedules if sch.end_date is None or sch.end_date > datetime.date.today()]

def patient_active_medication_ids(patient):
    return [sch.medication.id for sch in patient_active_schedules(patient)]