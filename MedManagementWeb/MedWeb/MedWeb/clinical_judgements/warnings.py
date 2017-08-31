from MedWeb.clinical_judgements.schedule_logic import patient_active_schedules


def has_warning(schedule):
    if schedule.adherence is not None and schedule.adherence.seven_day_fraction is not None:
        return schedule.adherence.seven_day_fraction < 0.8
    else:
        return False

def count_warnings(patient):
    return sum((has_warning(schedule) for schedule in patient_active_schedules(patient)))