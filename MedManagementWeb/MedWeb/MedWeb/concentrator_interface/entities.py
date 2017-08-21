import inspect

import sys
from datetime import datetime


class Schedule:
    _json_name = "schedule"
    _json_attribute_map = {"scheduleId": "id",
                           "assignedStartDate": "start_date",
                           "assignedEndDate": "end_date",
                           "alternateName": "alternate_name",
                           "purposeStatement": "purpose_statement",
                           "patientUuid": "patient_uuid",
                           "medication": "medication_id <- medicationId",
                           "recordedDoses": "recorded_doses",
                           "scheduledDoses": "scheduled_doses",
                           "scheduleAdherence": "adherence"}

    def __init__(self, id, start_date, end_date, alternate_name, purpose_statement, patient_uuid, medication_id,
                 recorded_doses, scheduled_doses, adherence):
        self.id = id
        self.start_date = start_date
        self.end_date = end_date
        self.alternate_name = alternate_name
        self.purpose_statement = purpose_statement
        self.patient_uuid = patient_uuid
        self.medication = None
        self.medication_id = medication_id
        self.recorded_doses = recorded_doses
        self.adherence = adherence
        self.scheduled_doses = scheduled_doses

    def registerAdherenceObject(self, adherenceObject):
        self.adherenceObject = adherenceObject


class ScheduledDose:
    _json_attribute_map = {"scheduledDoseId": "id",
                           "startDay": "start_day",
                           "endDay": "end_day",
                           "repeatInterval": "repeat_interval",
                           "windowStartTime": "start_time",
                           "windowEndTime": "end_time",
                           "doseValue": "dose_value",
                           "scheduleId": "schedule_id",
                           "defaultReminderTime": "default_reminder_time",
                           "reminderTime": "reminder_time"}
    _json_ignore = {"schedule", }
    _json_name = "scheduledDose"

    def __init__(self, id, schedule_id, dose_value, start_day, end_day, repeat_interval, start_time, end_time,
                 default_reminder_time, reminder_time):
        self.id = id
        self.schedule_id = schedule_id
        self.dose_value = dose_value
        self.start_day = start_day
        self.end_day = end_day
        self.repeat_interval = repeat_interval
        self.start_time = start_time
        self.end_time = end_time
        self.default_reminder_time = default_reminder_time
        self.reminder_time = reminder_time
        self.schedule = None


class RecordedDose:
    _json_attribute_map = {"recordedDoseUUID": "uuid",
                           "dayTaken": "day_taken",
                           "timeTaken": "time_taken",
                           "doseValue": "dose_value",
                           "scheduleId": "schedule_id"}
    _json_name = "recordedDose"
    _json_ignore = "schedule"

    def __init__(self, uuid, dose_value, day_taken, time_taken, schedule_id):
        self.uuid = uuid
        self.dose_value = dose_value
        self.day_taken = day_taken
        self.time_taken = time_taken
        self.schedule_id = schedule_id
        self.schedule = None


class AdherenceObject:
    def __init__(self, streak_length, seven_day_fraction):
        self.seven_day_fraction = seven_day_fraction
        self.streak_length = streak_length


class PatientAdherenceObject(AdherenceObject):
    _json_name = "patientAdherence"
    _json_attribute_map = {"patientUuid": "patient_uuid",
                           "sevenDayFraction": "seven_day_fraction",
                           "streakLength": "streak_length"}

    def __init__(self, streak_length, seven_day_fraction, patient_uuid):
        super().__init__(streak_length, seven_day_fraction)
        self.patient_uuid = patient_uuid


class ScheduleAdherenceObject(AdherenceObject):
    _json_name = "scheduleAdherence"
    _json_attribute_map = {"scheduleId": "schedule_id",
                           "sevenDayFraction": "seven_day_fraction",
                           "streakLength": "streak_length"}

    def __init__(self, streak_length, seven_day_fraction, schedule_id):
        super().__init__(streak_length, seven_day_fraction)


class DoseInstance:
    _json_name = "doseInstance"
    _json_attribute_map = {"dose": "scheduled_dose_id <- scheduledDoseId",
                           "takenDose": "taken_dose_uuid <- recordedDoseUUID",
                           "day": "day",
                           "timeStart": "start_time",
                           "timeEnd": "end_time",
                           "doseValue": "dose_value",
                           "scheduleId": "schedule_id"}

    def __init__(self, day, start_time, end_time, schedule_id, scheduled_dose_id, taken_dose_uuid, dose_value):
        self.day = day
        self.start_time = start_time
        self.end_time = end_time
        self.schedule_id = schedule_id
        from MedWeb.concentrator_interface.interface import schedules
        self.schedule = schedules[schedule_id]
        self.scheduled_dose_id = scheduled_dose_id
        self.taken_dose_uuid = taken_dose_uuid
        self.dose_value = dose_value


clsmembers = inspect.getmembers(sys.modules[__name__], inspect.isclass)
names = {}
for member in clsmembers:
    try:
        names[member[1]._json_name] = member[1]
    except:
        continue


def from_dict(object_class, json_properties_dict):
    python_properties = {}
    for key, value in json_properties_dict.items():
        try:
            if key in object_class._json_ignore:
                continue
        except AttributeError:
            pass
        try:
            python_name = object_class._json_attribute_map[key]
        except(KeyError) as e:
            raise JSONMappingException("Class %s has no JSON property '%s' defined in its json_attribute_map" % (
            object_class.__name__, e.args[0]))
        if "<-" in python_name:
            python_name, field_redirect = (x.strip() for x in python_name.split("<-"))
            try:
                value = value[field_redirect]
            except(KeyError) as e:
                raise JSONMappingException("Field redirect failed for class %s, target %s has no gettable %s" % (
                object_class.__name__, value.__class__, field_redirect))
            except(TypeError) as e:
                value = None
        if type(value) == type({}):
            try:
                object_value = from_dict(names[key], value)
            except(KeyError) as e:
                raise JSONMappingException(
                    "JSON object property '%s' (for class %s) not defined in names" % (
                    e.args[0], object_class.__name__))
            python_properties[python_name] = object_value
        elif type(value) == type([]):
            try:
                list_value = [from_dict(names[key[:-1]], value_element) for value_element in value]
            except(KeyError) as e:
                raise JSONMappingException(
                    "JSON object property '%s' (for class %s) not defined in names" % (
                    e.args[0], object_class.__name__))
            python_properties[python_name] = list_value
        else:
            if value is None:
                python_value = value
            elif "time" in python_name:
                python_value = datetime.strptime(value, "%H:%M:%S").time()
            elif "date" in python_name:
                python_value = datetime.strptime(value, "%Y-%m-%d").date()
            elif "_id" in python_name:
                python_value = int(value)
            else:
                python_value = value
            python_properties[python_name] = python_value
    try:
        object = object_class(**python_properties)
    except(TypeError) as e:
        raise JSONMappingException(
            "Class '%s' is missing an attribute in its constructor: %s" % (object_class.__name__, e.args[0]))
    if object.__class__.__name__ == "Schedule":
        print(object.id)
    return object


class JSONMappingException(Exception):
    def __init__(self, message):
        super(JSONMappingException, self).__init__(message)
