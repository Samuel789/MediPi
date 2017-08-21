import datetime

from django import template

register = template.Library()


@register.filter
def as_percentage(value):
    if value == None or value == "":
        return ""
    return "%d%%" % (float(value) * 100)


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


@register.filter
def nhs_part(nhs_no, index):
    if index == 1:
        return nhs_no[0:3]
    if index == 2:
        return nhs_no[3:6]
    if index == 3:
        return nhs_no[6:10]

@register.filter
def present(schedule_list):
    return [schedule for schedule in schedule_list if schedule.start_date <= datetime.date.today() and (schedule.end_date is None or schedule.end_date > datetime.date.today())]

@register.filter
def future(schedule_list):
    return [schedule for schedule in schedule_list if schedule.start_date > datetime.date.today()]