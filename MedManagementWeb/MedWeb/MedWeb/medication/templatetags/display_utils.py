import datetime
import json

from django import template

register = template.Library()

@register.filter
def as_percentage(value):
    if value == None or value == "":
        return ""
    return "%d%%" % (float(value)*100)

@register.filter
def patient_display_name(patient):
    return patient.first_name + " " + patient.last_name

@register.filter
def dose_start_date(dose):
    date = dose.schedule.start_date + datetime.timedelta(days=dose.day)
    time = dose.start_time
    return datetime.datetime.combine(date, time)

@register.filter
def dose_end_date(dose):
    date = dose.schedule.start_date + datetime.timedelta(days=dose.day)
    time = dose.end_time
    return datetime.datetime.combine(date, time)