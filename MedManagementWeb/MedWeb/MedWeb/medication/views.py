import json
import random

import django
from django.http import HttpResponse
from django.template import RequestContext
from django.template.loader import get_template

from MedWeb import settings
from MedWeb.medication.data_placeholders import Patient, patients, medications

from datetime import date

sidebar_menu_urls = {"Medications": "/viewpatient",
                        "Schedule & History": "/schedule",
                        "Add Medication": "/assignmedication"}

sidebar_menu_entries = ["Medications", "Schedule & History", "Add Medication"]

def browse_patients(request):
    template = get_template("medication/browse_patients.djt.html")
    version_string = "%d.%d.%d %s" % django.VERSION[0:4]
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
    patient_id = int(request.GET.get("pid", None))
    if patient_id is None:
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
                              "active_patient": patients[patient_id]})
    return HttpResponse(output)

def assign_medication(request):
    patient_id = int(request.GET.get("pid", None))
    if patient_id is None:
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
                              "active_patient": patients[patient_id],
                              "medications": medications.values(),
                              }, request)
    return HttpResponse(output)