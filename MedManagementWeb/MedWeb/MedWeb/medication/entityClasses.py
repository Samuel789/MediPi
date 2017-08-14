import inspect

import sys


class DoseUnit:
    json_name = "doseUnit"
    json_object_map = {"doseUnitId": "id",
                       "name": "name"}
    def __init__(self, id, name):
        self.id = id
        self.name = name

class Medication:
    json_name = "medication"
    json_object_map = {"medicationId": "id",
                       "shortName": "short_name",
                       "fullName": "full_name",
                       "cautionaryText": "cautionary_text",
                       "iconName": "icon_name",
                       "doseUnit": "dose_unit"}
    def __init__(self, id, short_name, full_name, cautionary_text, icon_name, dose_unit):
        self.id = id
        self.short_name = short_name
        self.full_name = full_name
        self.cautionary_text = cautionary_text
        self.icon_name = icon_name
        self.dose_unit = dose_unit

class Schedule:
    json_name = "schedule"
    json_object_map = {"scheduleId": "id",
                       "assignedStartDate": "start_date",
                       "assignedEndDate": "end_date",
                       "alternateName": "alternate_name",
                       "purposeStatement": "purpose_statement",
                       "patientUuid": "patient_uuid",
                       "medication": "medication",
                       "recordedDoses": "recorded_doses",
                       "scheduledDoses": "scheduled_doses",
                       "scheduleAdherence": "adherence"}
    def __init__(self, id, start_date, end_date, alternate_name, purpose_statement, patient_uuid, medication, recorded_doses, scheduled_doses, adherence):
        self.id = id
        self.start_date = start_date
        self.end_date = end_date
        self.alternate_name = alternate_name
        self.purpose_statment = purpose_statement
        self.patient_uuid = patient_uuid
        self.medication = medication
        self.recorded_doses = recorded_doses
        self.adherence = adherence

    def registerAdherenceObject(self, adherenceObject):
        self.adherenceObject = adherenceObject

class ScheduledDose:
    json_object_map = {"scheduledDoseId": "id",
                     "startDay": "start_day",
                     "endDay": "end_day",
                     "repeatInterval": "repeat_interval",
                     "windowStartTime": "start_time",
                     "windowEndTime": "end_time",
                     "doseValue": "dose_value",
                     "scheduleId": "schedule_id",
                     "defaultReminderTime": "default_reminder_time",
                     "reminderTime": "reminder_time"}
    json_name = "scheduledDose"
    def __init__(self, id, schedule_id, dose_value, start_day, end_day, repeat_interval, start_time, end_time, default_reminder_time, reminder_time):
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
    json_object_map = {"recordedDoseUUID": "uuid",
                     "dayTaken": "day_taken",
                     "timeTaken": "time_taken",
                     "doseValue": "dose_value",
                     "scheduleId": "schedule_id"}
    json_name = "recordedDose"
    def __init__(self, uuid, dose_value, day_taken, time_taken, schedule_id):
        self.uuid = uuid
        self.dose_value = dose_value
        self.day_taken = day_taken
        self.time_taken = time_taken
        self.schedule_id = schedule_id
        self.schedule = None

class Patient:
    json_name = "patient"
    def __init__(self, uuid, device_name):
        self.uuid = uuid
        self.device_name = device_name
        self.adherenceObject = None

class AdherenceObject:
    def __init__(self, streak_length, seven_day_fraction):
        self.seven_day_fraction = seven_day_fraction
        self.streak_length = streak_length

class PatientAdherenceObject(AdherenceObject):
    json_name = "patientAdherence"
    json_object_map = {"patientUuid": "patient_uuid",
                       "sevenDayFraction": "seven_day_fraction",
                       "streakLength": "streak_length"}
    def __init__(self, streak_length, seven_day_fraction, patient_uuid):
        super().__init__(streak_length, seven_day_fraction)
        self.patient_uuid = patient_uuid
        self.patient = None

class ScheduleAdherenceObject(AdherenceObject):
    json_name = "scheduleAdherence"
    json_object_map = {"scheduleId": "schedule_id",
                       "sevenDayFraction": "seven_day_fraction",
                       "streakLength": "streak_length"}
    def __init__(self, streak_length, seven_day_fraction, schedule_id):
        super().__init__(streak_length, seven_day_fraction)
        self.schedule = None

clsmembers = inspect.getmembers(sys.modules[__name__], inspect.isclass)
names = {}
for member in clsmembers:
    print(member[1])
    try:
        names[member[1].json_name] = member[1]
    except:
        continue


def fromDict(object_class, json_properties_dict):
    python_properties = {}
    for key, value in json_properties_dict.items():
        if type(value) == type({}):
            try:
                object_value = fromDict(names[key], value)
            except(KeyError) as e:
                raise JSONMappingException(
                    "JSON object property '%s' (for class %s) not defined in names" % (e.args[0], object_class))
            try:
                python_properties[object_class.json_object_map[key]] = object_value
            except(KeyError) as e:
                raise JSONMappingException("Class %s has no JSON property '%s' defined in its json_properties_dict" % (object_class.__name__, e.args[0]))
        elif type(value) == type([]):
            try:
                list_value = [fromDict(names[key[:-1]], value_element) for value_element in value]
            except(KeyError) as e:
                raise JSONMappingException(
                    "JSON object property '%s' (for class %s) not defined in names" % (e.args[0], object_class))
            try:
                python_properties[object_class.json_object_map[key]] = list_value
            except(KeyError) as e:
                raise JSONMappingException("Class %s has no JSON property '%s' defined in its json_properties_dict" % (object_class.__name__, e.args[0]))

        else:
            try:
                python_properties[object_class.json_object_map[key]] = value
            except(KeyError) as e:
                raise JSONMappingException("Class %s has no JSON property '%s' defined in its json_properties_dict" % (
                object_class.__name__, e.args[0]))

    return object_class(**python_properties)

class JSONMappingException(Exception):
    def __init__(self, message):
        super(JSONMappingException, self).__init__(message)
print(names)