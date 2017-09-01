from django.conf.urls import url

from MedWeb.medication_manager.api import *
from MedWeb.medication_manager.views import *

urlpatterns = [
    url(r'^$', browse_patients, name='browse_patients'),
    url(r'^viewpatient/', patient_summary, name='patient_summary'),
    url(r'^assignmedication/', assign_medication, name='assign_medication'),
    url(r'^addschedule/', create_new_schedule, name='create_new_schedule'),
    url(r'^modifyschedule/', modify_schedule, name='modify_schedule'),
    url(r'^queryConcentrator/', query_concentrator, name='query_concentrator'),
    url(r'^api/get_medication_information', get_medication_details, name='APIMedDetails'),
    url(r'^api/add_patient_schedule', add_patient_schedule, name='APIAddSchedule'),
    url(r'^api/cancel_medication', cancel_schedule, name='APICancelMedication'),
    url(r'^api/update', update, name='APIUpdate'),
]
