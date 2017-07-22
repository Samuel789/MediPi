from django.http import HttpResponse
from django.shortcuts import render_to_response, render
from django.template.loader import get_template

from django.conf import settings
import django

from enum import Enum



def about_view(request):
    template = get_template("home/about.djt.html")
    version_string = "%d.%d.%d %s" % django.VERSION[0:4]
    output = template.render({"title": settings.SITE_NAME,
                              "version": version_string,
                              "siteurl": settings.SITE_URL,
                              "sections": settings.SECTIONS,
                              "active_section": "about"})
    return HttpResponse(output)
