from MedWeb.clinical_database.dose_units import dose_unit_data
from MedWeb.clinical_database.entities import Medication, DoseUnit
from MedWeb.clinical_database.medication_data import medication_data

medications = {}
dose_units = {}

for id, name in dose_unit_data.items():
    dose_units[id] = DoseUnit(id, name)

for medication in medication_data:
    medication["dose_unit"] = dose_units[medication["dose_unit_id"]]
    medications[medication["id"]] = Medication(**medication)
