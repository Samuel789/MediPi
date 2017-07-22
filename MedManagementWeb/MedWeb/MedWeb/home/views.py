from django.http import HttpResponse
from django.shortcuts import render_to_response, render
from django.template.loader import get_template

from django.conf import settings
from django import VERSION

def index_view(request):
    output = "您好"
    template = get_template("home/main.djt.html")
    output = template.render({"title": settings.SITE_NAME, "version": VERSION})
    print(settings.SITE_NAME)
    return HttpResponse(output)