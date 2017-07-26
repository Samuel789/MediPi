from django.conf.urls import url
from MedWeb.medication.views import *
from MedWeb.medication.api import *

urlpatterns = [
    url(r'^$', browse_patients, name='browse_patients'),
url(r'^viewpatient/', patient_summary, name='patient_summary'),
url(r'^assignmedication/', assign_medication, name='assign_medication'),
url(r'^api/get_medication_information', get_medication_details, name='APIMedDetails'),
]