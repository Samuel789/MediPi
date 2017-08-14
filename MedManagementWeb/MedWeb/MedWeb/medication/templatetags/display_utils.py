from django import template

register = template.Library()

@register.filter
def as_percentage(value):
    return "%d%%" % (value*100)

@register.filter
def patient_display_name(patient):
    return patient.first_name + " " + patient.last_name