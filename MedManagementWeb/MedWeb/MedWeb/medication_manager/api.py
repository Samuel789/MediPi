import json
from datetime import datetime, timedelta

from django.http import HttpResponse

from MedWeb.clinical_database.clinical_database import medications
from MedWeb.concentrator_interface.entities import Schedule, from_dict, ScheduledDose
from MedWeb.concentrator_interface.interface import schedules, send_to_concentrator, update_from_concentrator


def get_medication_details(request):
    """Client API request to provide expanded medication information given a medication_id"""
    medication_id = request.POST.get("id")
    medication = medications[int(medication_id)]
    return HttpResponse(json.dumps({"full_name": medication.full_name, "short_name": medication.short_name,
                                    "cautionary_text": medication.cautionary_text, "id": medication.id,
                                    "dose_unit": medication.dose_unit.name}))


def validate_schedule():
    # TODO - server-side validation of schedule data
    pass


def validate_scheduled_dose(dose):
    # TODO - server-side validation of schedule data
    pass


def add_patient_schedule(request):
    """Client API request to add or replace a patient schedule"""
    schedule_data = json.loads(request.POST["schedule"])
    patient_uuid = request.POST["patient_uuid"]
    medication_id = request.POST["medication_id"]
    doses = []
    for dose_data in json.loads(request.POST["doses"]):
        print(dose_data)
        dose = ScheduledDose(**dose_data, schedule_id=None, reminder_time=dose_data["default_reminder_time"])
        validate_scheduled_dose(dose)
        doses.append(dose)
    schedule = Schedule(schedule_data["id"], schedule_data["start_date"], schedule_data["end_date"],
                        schedule_data["alternate_name"], schedule_data["purpose_statement"], patient_uuid,
                        medication_id, None, None, None)
    send_to_concentrator(schedule, doses)
    return HttpResponse()


def cancel_schedule(request):
    """Client API request to cancel the active schedule of a particular medication_id"""
    patient_uuid = request.POST["patient_uuid"]
    medication_id = request.POST["medication_id"]
    doses = []
    tomorrow = (datetime.today() + timedelta(days=1)).strftime("%Y-%m-%d")
    schedule = Schedule(None, tomorrow, tomorrow,
                        "", "", patient_uuid,
                        medication_id, None, None, None)
    send_to_concentrator(schedule, doses)
    return HttpResponse()


def update(request):
    """Client API request to update the local database from the Concentrator. Client should trigger a page refresh once
        an OK response is received."""
    update_from_concentrator()
    return HttpResponse()


class RequestProcessingException(Exception):
    def __init__(self, message):
        super(RequestProcessingException, self).__init__(message)
