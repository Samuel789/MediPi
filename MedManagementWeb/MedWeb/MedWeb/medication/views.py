import django
from django.http import HttpResponse
from django.template.loader import get_template

from MedWeb import settings


def test_page(request):
    template = get_template("medication/patient_template.djt.html")
    version_string = "%d.%d.%d %s" % django.VERSION[0:4]
    output = template.render({"title": settings.SITE_NAME,
                              "version": version_string,
                              "siteurl": settings.SITE_URL,
                              "sections": settings.SECTIONS,
                              "active_section": "patients"})
    return HttpResponse(output)
