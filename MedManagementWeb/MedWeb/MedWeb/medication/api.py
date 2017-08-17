import json

from django.http import HttpResponse

from MedWeb.clinical_database.clinical_database import medications
from MedWeb.clinical_database.entities import Medication
from MedWeb.concentrator_interface.entities import Schedule, from_dict, ScheduledDose
from MedWeb.concentrator_interface.interface import schedules, send_to_concentrator
from MedWeb.patient_database.patient_database import patients

def get_medication_details(request):
    medication_id = request.POST.get("id")
    medication = medications[int(medication_id)]
    return HttpResponse(json.dumps({"full_name": medication.full_name, "short_name": medication.short_name, "cautionary_text": medication.cautionary_text, "id": medication.id, "dose_unit": medication.dose_unit.name}))

def patch_schedule(request):
    data = request.POST
    schedule_id = data["schedule_id"]
    scheduled_doses = [from_dict(ScheduledDose.__class__, dose_data) for dose_data in data["doses"]]
    for dose in scheduled_doses:
        validate_scheduled_dose(dose)
    schedules[schedule_id].scheduled_doses += scheduled_doses

def put_schedule(request):
    data = request.POST
    schedule = from_dict(Schedule.__class__, data["schedule"])
    validate_schedule(schedule)
    patients[schedule.id].schedules.append(schedule)

def validate_schedule():
    pass

def validate_scheduled_dose(dose):
    pass


def add_patient_schedule(request):
    print(request.POST)

    schedule_data = json.loads(request.POST["schedule"])
    patient_uuid = request.POST["patient_uuid"]
    medication_id = request.POST["medication_id"]
    doses = []
    for dose_data in json.loads(request.POST["doses"]):
        print(dose_data)
        dose = ScheduledDose(**dose_data, schedule_id=None, reminder_time=dose_data["default_reminder_time"])
        validate_scheduled_dose(dose)
        doses.append(dose)
    schedule = Schedule(schedule_data["id"], schedule_data["start_date"], schedule_data["end_date"], schedule_data["alternate_name"], schedule_data["purpose_statement"], patient_uuid, medication_id,None,None,None)
    send_to_concentrator(schedule, doses)
    return HttpResponse()


def error_response(exception):
    return HttpResponse(exception.message)

class RequestProcessingException(Exception):
    def __init__(self, message):
        super(RequestProcessingException, self).__init__(message)