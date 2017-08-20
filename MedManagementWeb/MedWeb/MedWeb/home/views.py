import django
from django.conf import settings
from django.http import HttpResponse
from django.template.loader import get_template


def about_view(request):
    template = get_template("home/about.djt.html")
    version_string = "%d.%d.%d %s" % django.VERSION[0:4]
    output = template.render({"title": settings.SITE_NAME,
                              "version": version_string,
                              "siteurl": settings.SITE_URL,
                              "sections": settings.SECTIONS,
                              "active_section": "about"})
    return HttpResponse(output)
