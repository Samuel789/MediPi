from django.conf.urls import url
from MedWeb.medication.views import *

urlpatterns = [
    url(r'^$', browse_patients, name='post_list'),
url(r'^viewpatient/', patient_summary, name='post_list'),
]