# MediPi Medication Management System
![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/MedicationMenuScreenshot.png)
## Introduction

The MediPi Medication Management Project aims to develop an open-source telehealth solution to the problem of low medication adherence, helping patients to take their medication correctly and clinicians to monitor progress. It extends the software of the MediPi patient unit (http://rprobinson.github.io/MediPi/) to enable patients to view their scheduled doses, receive reminders when it is time to take their medications and record doses which have been taken. This information is then transmitted to the Concentrator, which additionally calculates several motivational adherence metrics including a streak length which are returned to the patient unit. An API enables control of a patient's medication regimen and monitoring of adherence by clinicians, demonstrated by MedWeb, a mock clinical system for medication management included in this project.

## System Overview

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/dataFlowDiagram.png)

## Security

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/systemStructure.jpg)

The system uses the same security protocols as the existing MediPi functions, including two-layer encryption with both patient- and device-specific certificates. More information is available on the [MediPi project home page](http://rprobinson.github.io/MediPi/).

## About this Project

This proof-of-concept system was developed by Sam Chase as an MSc project at the University of York.
