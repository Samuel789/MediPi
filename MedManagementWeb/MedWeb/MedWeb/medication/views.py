import json

import requests
from django.http import HttpResponse
from django.template.loader import get_template

from MedWeb import settings
from MedWeb.concentrator_interface import entities
from MedWeb.concentrator_interface.entities import Schedule

from MedWeb.concentrator_interface.interface import update_from_concentrator
from MedWeb.concentrator_interface.interface import patients, medications, schedules

sidebar_menu_urls = {"Medications": "/viewpatient",
                        "Schedule & History": "/schedule",
                        "Add Medication": "/assignmedication"}

sidebar_menu_entries = ["Medications", "Schedule & History", "Add Medication"]

def browse_patients(request):
    template = get_template("medication/browse_patients.djt.html")
    output = template.render({"title": settings.SITE_NAME,
                              "version": settings.version_string,
                              "siteurl": settings.SITE_URL,
                              "sections": settings.SECTIONS,
                              "sidebar_menu_urls": sidebar_menu_urls,
                              "active_section": "patients",
                              "sidebar_menu_entries": sidebar_menu_entries,
                              "patients": patients.values(),
                              "active_patient": None})
    return HttpResponse(output)

def patient_summary(request):
    patient_uuid = request.GET.get("patient_uuid", None)
    if patient_uuid is None:
        return browse_patients(request)
    template = get_template("medication/medication_summary.djt.html")
    output = template.render({"title": settings.SITE_NAME,
                              "version": settings.version_string,
                              "siteurl": settings.SITE_URL,
                              "sections": settings.SECTIONS,
                              "sidebar_menu_entries": sidebar_menu_entries,
                              "sidebar_menu_urls": sidebar_menu_urls,
                              "active_sidebar_entry": "Medications",
                              "active_section": "patients",
                              "active_patient": patients[patient_uuid]})
    return HttpResponse(output)

def assign_medication(request):
    patient_uuid = request.GET.get("patient_uuid", None)
    if patient_uuid is None:
        return browse_patients(request)
    template = get_template("medication/assign_medication.djt.html")
    output = template.render({"title": settings.SITE_NAME,
                              "version": settings.version_string,
                              "siteurl": settings.SITE_URL,
                              "sections": settings.SECTIONS,
                              "sidebar_menu_entries": sidebar_menu_entries,
                              "sidebar_menu_urls": sidebar_menu_urls,
                              "active_sidebar_entry": "Add Medication",
                              "active_section": "patients",
                              "active_patient": patients[patient_uuid],
                              "active_patient_medication_ids": json.dumps([sch.medication.id for sch in patients[patient_uuid].schedules]),
                              "medications": medications.values(),
                              }, request)
    return HttpResponse(output)

def create_new_schedule(request):
    patient_uuid = request.GET.get("patient_uuid", None)
    if patient_uuid is None:
        return browse_patients(request)
    medication_id_str = request.GET.get("mid", None)
    if medication_id_str is None:
        return assign_medication(request)
    medication_id = int(medication_id_str)
    template = get_template("medication/schedule_editor.html")
    output = template.render({"title": settings.SITE_NAME,
                              "version": settings.version_string,
                              "siteurl": settings.SITE_URL,
                              "sections": settings.SECTIONS,
                              "sidebar_menu_entries": sidebar_menu_entries,
                              "sidebar_menu_urls": sidebar_menu_urls,
                              "active_sidebar_entry": "Add Medication",
                              "active_patient": patients[patient_uuid],
                              "active_medication": medications[medication_id],
                              "mode": 'create'})
    return HttpResponse(output)


def modify_schedule(request):
    schedule_id_str = request.GET.get("schedule_id", None)
    if schedule_id_str is None:
        return patient_summary(request)
    schedule_id = int(schedule_id_str)
    template = get_template("medication/schedule_editor.html")
    output = template.render({"title": settings.SITE_NAME,
                              "version": settings.version_string,
                              "siteurl": settings.SITE_URL,
                              "sections": settings.SECTIONS,
                              "sidebar_menu_entries": sidebar_menu_entries,
                              "sidebar_menu_urls": sidebar_menu_urls,
                              "active_sidebar_entry": "Medications",
                              "active_patient": schedules[schedule_id].patient,
                              "active_schedule": schedules[schedule_id],
                              "active_medication": schedules[schedule_id].medication,
                              "mode": 'modify'})
    return HttpResponse(output)

def query_concentrator(request):
    update_from_concentrator()