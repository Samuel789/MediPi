import json

from django.http import HttpResponse

from MedWeb.medication.data_placeholders import medications


def get_medication_details(request):
    medication_id = request.POST.get("id")
    medication = medications[int(medication_id)]
    return HttpResponse(json.dumps({"full_name": medication.full_name, "short_name": medication.short_name, "cautionary_text": medication.cautionary_text, "id": medication.id, "dose_unit": medication.dose_unit}))