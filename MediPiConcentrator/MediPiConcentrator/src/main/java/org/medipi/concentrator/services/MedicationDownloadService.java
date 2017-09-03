package org.medipi.concentrator.services;

import org.medipi.concentrator.dao.*;
import org.medipi.concentrator.entities.Patient;
import org.medipi.concentrator.exception.NotFound404Exception;
import org.medipi.concentrator.logic.PatientAdherenceCalculator;
import org.medipi.concentrator.logic.ScheduleAdherenceCalculator;
import org.medipi.concentrator.logic.ScheduleAdherenceCalculatorInterface;
import org.medipi.concentrator.utilities.Utilities;
import org.medipi.medication.model.*;
import org.medipi.model.MedWebDO;
import org.medipi.model.MedicationPatientDO;
import org.medipi.model.UnpackedDoseDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MedicationDownloadService {

    @Autowired
    private Utilities utils;

    @Autowired
    private PatientDeviceValidationService patientDeviceValidationService;

    @Autowired
    private ScheduleDAOImpl scheduleDAOimpl;

    @Autowired
    private PatientDAOImpl patientDAOimpl;

    @Autowired
    private PatientAdherenceDAOImpl patientAdherenceDAOimpl;

    @Autowired
    private ScheduleAdherenceDAOImpl scheduleAdherenceDAOimpl;

    @Autowired
    private MedicationDAOImpl medicationDAOImpl;

    @Autowired
    private RecordedDoseDAOImpl recordedDoseDAOimpl;

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<MedicationPatientDO> getMedicationData(String patientUuid) {

        MedicationPatientDO medicationInfo = new MedicationPatientDO();
        System.out.println(org.hibernate.Version.getVersionString());
        try {
            medicationInfo.setSchedules(scheduleDAOimpl.findAll());
            for (Schedule schedule : medicationInfo.getSchedules()) {
                System.out.println(schedule.getAssignedStartDate());
            }
            medicationInfo.setPatientAdherence(patientAdherenceDAOimpl.findByPatientUuid(patientUuid));

        } catch (Exception e) {
            System.out.println(String.format("Failed to process query results (or none returned). Error was %s: %s", e.getClass(), e.getMessage()));
        }
        try {
            return new ResponseEntity<MedicationPatientDO>(medicationInfo, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(String.format("Creation of ResponseEntity failed. Error was %s: %s", e.getClass(), e.getMessage()));
        }
        return new ResponseEntity<MedicationPatientDO>(medicationInfo, HttpStatus.OK);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void uploadRecordedDoses(MedicationPatientDO uploadedData) {
        List<RecordedDose> doseData = new ArrayList<>();
        for (Schedule schedule : uploadedData.getSchedules()) {
            doseData.addAll(schedule.getRecordedDoses());
        }
        System.out.println("RECEIVED UPLOAD: " + " " + doseData.size());
        for (RecordedDose dose : doseData) {
            try {
                recordedDoseDAOimpl.findByRecordedDoseUUID(dose.getRecordedDoseUUID());
            } catch (EmptyResultDataAccessException e) {
                recordedDoseDAOimpl.save(dose);
            }
        }
    }

    public String verifyPatient(MedicationPatientDO uploadedData) {
        assert patientDeviceValidationService != null;
        String patientUuid = uploadedData.getPatientUuid();
        String hardwareName = uploadedData.getHardwareName();
        try {
            // Check that the device and patient are registered with each other
            this.patientDeviceValidationService.validate(hardwareName, patientUuid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NotFound404Exception("Hardware and/or patient not registered" + e.getLocalizedMessage());
        }
        return patientUuid;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateAdherence(String patientUuid) {
        LocalDate searchEndTime = LocalDate.now().plusDays(1);
        LocalDate searchStartTime = searchEndTime.minusDays(7);
        Collection<Schedule> patientSchedules = scheduleDAOimpl.findByPatientUuid(patientUuid);
        for (Schedule schedule : patientSchedules) {
            for (ScheduledDose dose : schedule.getScheduledDoses()) {
                dose.setSchedule(schedule);
            }
        }
        PatientAdherenceCalculator patientAdherenceCalculator = new PatientAdherenceCalculator(patientSchedules, searchStartTime, searchEndTime, true);
        patientAdherenceCalculator.setPenultimateDayEndTime(LocalTime.now());
        patientAdherenceCalculator.calculatePatientAdherence();
        for (Schedule schedule : patientSchedules) {
            ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = patientAdherenceCalculator.getScheduleAdherenceCalculators().get(schedule);
            if (schedule.getScheduleAdherence() == null) {
                ScheduleAdherence scheduleAdherence = new ScheduleAdherence();
                scheduleAdherence.setScheduleId(schedule.getScheduleId());
                schedule.setScheduleAdherence(scheduleAdherence);
            }
            schedule.getScheduleAdherence().setSevenDayFraction(scheduleAdherenceCalculatorInterface.getAdherenceFraction());
            schedule.getScheduleAdherence().setStreakLength(scheduleAdherenceCalculatorInterface.getStreakLength());
            scheduleAdherenceDAOimpl.save(schedule.getScheduleAdherence());
        }
        PatientAdherence patientAdherence = patientAdherenceDAOimpl.findByPatientUuid(patientUuid);
        patientAdherence.setSevenDayFraction(patientAdherenceCalculator.getAdherenceFraction());
        patientAdherence.setStreakLength(patientAdherenceCalculator.getStreakLength());
        patientAdherenceDAOimpl.update(patientAdherence);
        //TODO - find a better way than setting and unsetting the schedule
        for (Schedule schedule : patientSchedules) {
            for (ScheduledDose dose : schedule.getScheduledDoses()) {
                dose.setSchedule(null);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ResponseEntity<MedWebDO> getAllPatientData() {
        List<Patient> medicationPatients = patientDAOimpl.findByGroup("MedWeb");
        List<Schedule> schedules = new ArrayList<Schedule>();
        List<PatientAdherence> adherence = new ArrayList<PatientAdherence>();
        List<String> patientUuids = new ArrayList<String>();
        for (Patient patient : medicationPatients) {
            schedules.addAll(scheduleDAOimpl.findByPatientUuid(patient.getPatientUuid()));
            adherence.add(patientAdherenceDAOimpl.findByPatientUuid(patient.getPatientUuid()));
            patientUuids.add(patient.getPatientUuid());
        }
        MedWebDO responseData = new MedWebDO();
        responseData.setPatientAdherenceList(adherence);
        responseData.setPatientUuids(patientUuids);
        responseData.setSchedules(schedules);
        List<Medication> medicationList = new ArrayList<>();
        for (Schedule schedule: schedules) {
            medicationList.add(schedule.getMedication());
        }
        responseData.setMedications(medicationList);
        return new ResponseEntity<MedWebDO>(responseData, HttpStatus.OK);
    }


    @Transactional(rollbackFor = Throwable.class)
    public ResponseEntity<List<DoseInstance>> unpackedDoses(String patientUuid, String startDateString, String endDateString) {
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalDate endDate = LocalDate.parse(endDateString);
        List<DoseInstance> doseInstances = new ArrayList<>();
        for (Schedule schedule : scheduleDAOimpl.findByPatientUuid(patientUuid)) {
            int startDay = Math.max(0, (int) schedule.getAssignedStartDate().toLocalDate().until(startDate, ChronoUnit.DAYS));
            int endDay = Math.max(0, (int) schedule.getAssignedStartDate().toLocalDate().until(endDate, ChronoUnit.DAYS));
            for (ScheduledDose dose : schedule.getScheduledDoses()) {
                dose.setSchedule(schedule);
            }
            ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, startDay, endDay, false);
            scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();
            doseInstances.addAll(scheduleAdherenceCalculatorInterface.getDoseInstances());
            for (ScheduledDose dose : schedule.getScheduledDoses()) {
                dose.setSchedule(null);
            }
        }
        UnpackedDoseDO response = new UnpackedDoseDO();
        response.setDoseInstances(doseInstances);
        return new ResponseEntity<List<DoseInstance>>(doseInstances, HttpStatus.OK);
    }
}