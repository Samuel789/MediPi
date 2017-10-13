# MediPi Medication Management System
![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/Images/medipi.png)
## Introduction

The MediPi Medication Management Project aims to develop an open-source telehealth solution to the problem of low medication adherence, helping patients to take their medication correctly and clinicians to monitor progress. It extends the software of the MediPi patient unit (http://rprobinson.github.io/MediPi/) to enable patients to view their scheduled doses, receive reminders when it is time to take their medications and record doses which have been taken. This information is then transmitted to the Concentrator, which additionally calculates several motivational adherence metrics including a streak length which are returned to the patient unit. An API enables control of a patient's medication regimen and monitoring of adherence by clinicians, demonstrated by MedWeb, a mock clinical system for medication management included in this project.

## System Overview

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/dataFlowDiagram.png)

## Security

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/systemStructure.jpg)

The system uses the same security protocols as the existing MediPi functions, including two-layer encryption with both patient- and device-specific certificates. More information is available on the [MediPi project home page](http://rprobinson.github.io/MediPi/).

## Screenshots and Feature Tour

### Patient Unit
The medication management system appears as a tile on the MediPi dashboard, alongside the MediPi's existing functionality.

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/Images/MainMenu.png)

Medication menu

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/Images/MedicationMenu.png)

The two steps of the Record a Dose process. A patient can record a dose of any assigned medication taken at any time during the day. A colour scheme distinguishes those which should be taken right now (green) from those when are to be taken as needed (white) and are not due (red).

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/Images/RecordDose.png)

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/Images/DoseDetails.png)

The patient can view medications assigned to them, and are presented with each medication's 'purpose statement' and adherence information. Tapping a medication displays additional information, such as cautionary and advisory text, the medication's full name, an icon if assigned, and an explanation of the adherence statistics.

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/Images/ShowMedications.png)

The view schedule functionality is not yet implemented.

### Mock Clinical System (MedWeb)

The Patient Browser shows a summary of all patients with MediPi devices assigned to the system. Patients with warnings are highlighted in red. Warnings occur when the adherence rate over the previous seven days falls below 80%.

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/Images/MedWebPatientChooser.png)

The Patient Summary screen displays the selected patient's assigned and upcoming medication schedules. A schedule represents a collection of doses for a medication as understood at a particular time. Modifying a set of doses truncates the existing schedule and creates a new one starting the next day with the updated regimen. This ensures medication and adherence history is never lost.

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/Images/MedWebPatientSummary.png)

Medications can be assigned to a patient by searching for their name or dn+d/SNOMED id. Currently a mock database containing a small sample of medications is used, in place of a connection to the dn+d database.

![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/Images/MedWebAddMed.png)

Complex combinations of repeating doses can be created within a medication schedule. Clinicians can also assign a purpose statement to motivate adherence.

 ![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/Images/MedWebDoseEditor.png)

 ![Element image](https://raw.githubusercontent.com/Samuel789/MediPi/master/Images/MedWebDoseConfig.png)

## About this Project

This proof-of-concept system was developed by Sam Chase as an MSc project at the University of York.
