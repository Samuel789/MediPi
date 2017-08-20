class Medication:
    json_name = "medication"
    json_attribute_map = {"medicationId": "id",
                          "shortName": "short_name",
                          "fullName": "full_name",
                          "cautionaryText": "cautionary_text",
                          "iconName": "icon_name",
                          "doseUnit": "dose_unit", }

    def __init__(self, id, short_name, full_name, cautionary_text, icon_name, dose_unit, dose_unit_id):
        self.id = id
        self.short_name = short_name
        self.full_name = full_name
        self.cautionary_text = cautionary_text
        self.icon_name = icon_name
        self.dose_unit = dose_unit
        self.dose_unit_id = dose_unit_id

    def __str__(self):
        return "Medication %d: %s" % (self.id, self.full_name)


class DoseUnit:
    json_name = "doseUnit"
    json_attribute_map = {"doseUnitId": "id",
                          "name": "name"}

    def __init__(self, id, name):
        self.id = id
        self.name = name

    def __str__(self):
        return self.name
