from enum import Enum


class Status(Enum):
    normal = 0
    not_registered_with_concentrator = 1
    never_synched = 2
    not_synched_recently = 3


class Patient:
    _json_name = "patient"

    def __init__(self, uuid, first_name, last_name, dob, nhs_no):
        self.uuid = uuid
        self.adherence = None
        self.schedules = set()
        self.first_name = first_name
        self.last_name = last_name
        self.dob = dob
        self.nhs_no = nhs_no
        self.status = 1
        self.warnings = 0
        self.name = first_name + " " + last_name