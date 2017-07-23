import django
from django.http import HttpResponse
from django.template.loader import get_template

from MedWeb import settings
from MedWeb.medication.patient import Patient

from datetime import date

def browse_patients(request):
    template = get_template("medication/browse_patients.djt.html")
    version_string = "%d.%d.%d %s" % django.VERSION[0:4]
    output = template.render({"title": settings.SITE_NAME,
                              "version": version_string,
                              "siteurl": settings.SITE_URL,
                              "sections": settings.SECTIONS,
                              "active_section": "patients",
                              "active_patient": None})
    return HttpResponse(output)

def patient_summary(request):
    template = get_template("medication/browse_patients.djt.html")
    version_string = "%d.%d.%d %s" % django.VERSION[0:4]
    output = template.render({"title": settings.SITE_NAME,
                              "version": version_string,
                              "siteurl": settings.SITE_URL,
                              "sections": settings.SECTIONS,
                              "active_section": "patients",
                              "active_patient": Patient("Mr John Smith", date(1974, 1, 23), "FBJTEK2353")})
    return HttpResponse(output)