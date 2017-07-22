from django.conf.urls import url
from MedWeb.medication.views import *

urlpatterns = [
    url(r'^$', test_page, name='post_list'),
]