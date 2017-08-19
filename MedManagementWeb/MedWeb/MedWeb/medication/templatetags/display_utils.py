import datetime
import json

from django import template

register = template.Library()

@register.filter
def as_percentage(value):
    if value == None:
        return ""
    return "%d%%" % (value*100)

@register.filter
def patient_display_name(patient):
    return patient.first_name + " " + patient.last_name

@register.filter
def dose_start_date(dose):
    date = dose.schedule.start_date + datetime.timedelta(days=dose.start_day)
    return date