package org.medipi.concentrator.services;

import org.medipi.concentrator.dao.*;
import org.medipi.concentrator.exception.NotFound404Exception;
import org.medipi.concentrator.logic.MedicationLogic;
import org.medipi.concentrator.utilities.Utilities;
import org.medipi.medication.Medication;
import org.medipi.medication.RecordedDose;
import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;
import org.medipi.model.MedicationDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;

@Service
public class MedicationDownloadService {

    @Autowired
    private Utilities utils;

    @Autowired
    private PatientDeviceValidationService patientDeviceValidationService;

    @Autowired
    private ScheduleDAOImpl scheduleDAOimpl;

    @Autowired
    private PatientAdherenceDAOImpl patientAdherenceDAOimpl;

    @Autowired
    private RecordedDoseDAOImpl recordedDoseDAOimpl;

    @Transactional(rollbackFor = RuntimeException.class)
    private MedicationDO getMedicationData(String patientUuid) {

        MedicationDO medicationInfo = new MedicationDO();
        List<ScheduledDose> doses = new ArrayList<>();
        System.out.println(org.hibernate.Version.getVersionString());
        try {
            medicationInfo.setSchedules(scheduleDAOimpl.findAll());
            for (Schedule schedule: medicationInfo.getSchedules()) {
                System.out.println(schedule.getAssignedStartDate());
            }
            medicationInfo.setPatientAdherence(patientAdherenceDAOimpl.findByPatientUuid(patientUuid));

        } catch (Exception e) {
            System.out.println(String.format("Failed to process query results (or none returned). Error was %s: %s", e.getClass(), e.getMessage()));
        }
        return medicationInfo;
    }

    @Transactional(rollbackFor = Throwable.class)
    private void uploadRecordedDoses(MedicationDO uploadedData) {
        List<RecordedDose> doseData = new ArrayList<>();
        for (Schedule schedule: uploadedData.getSchedules()) {
            doseData.addAll(schedule.getRecordedDoses());
        }
        System.out.println("RECEIVED UPLOAD: " + " " + doseData.size());
        for (RecordedDose dose: doseData) {
            try{recordedDoseDAOimpl.findByRecordedDoseUUID(dose.getRecordedDoseUUID());
            } catch (EmptyResultDataAccessException e) {
                dose.setAdherent(isDoseAdherent(dose));
                recordedDoseDAOimpl.save(dose);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ResponseEntity<MedicationDO> synchronize(MedicationDO uploadedData) {
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
        uploadRecordedDoses(uploadedData);
        updateAdherence(patientUuid);
        MedicationDO returnData = getMedicationData(patientUuid);
        try {
            return new ResponseEntity<MedicationDO>(returnData, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(String.format("Creation of ResponseEntity failed. Error was %s: %s", e.getClass(), e.getMessage()));
        }
        return new ResponseEntity<MedicationDO>(returnData, HttpStatus.OK);
    }

    @Transactional(rollbackFor = Throwable.class)
    private void updateAdherence(String patientUuid) {
        LocalDate searchEndTime = LocalDate.now();
        LocalDate searchStartTime = searchEndTime.minusDays(7);
        List<RecordedDose> recordedDoses = recordedDoseDAOimpl.findByPatientUuid(patientUuid);
        List<ScheduledDose> scheduledDoses = scheduledDoseDAOimpl.findByPatientUuid(patientUuid);
        HashMap<Schedule, int> recordedDoseCount = new HashMap<>();
        HashMap<Schedule, int> takenDoseCount = new HashMap<>();
        for (RecordedDose dose: recordedDoses) {
            LocalDateTime timeTaken = dose.getTimeTaken().toLocalDateTime();
            if (timeTaken.isBefore(searchStartTime)) {
                continue;
            } else if (timeTaken.isAfter(searchEndTime)) {
                break;
            }
            if (dose.isAdherent()) adherentDoses++;
        }
        for (ScheduledDose dose: scheduledDoses) {
            int startDayOfSchedule = MedicationLogic.getDayOfSchedule(dose.getSchedule(), searchStartTime);
            int endDayOfSchedule = MedicationLogic.getDayOfSchedule(dose.getSchedule(), searchEndTime);
            if (!recordedDoseCount.containsKey(dose.getSchedule())) {
                recordedDoseCount.put(dose.getSchedule(), 0);
            }
            recordedDoseCount.put(dose.getSchedule(), recordedDoseCount.get(dose.getSchedule()) + MedicationLogic.countDosesBetween(startDayOfSchedule, endDayOfSchedule, dose));

        }
    }

    private boolean isDoseAdherent(RecordedDose dose) {
        LocalDateTime timeTaken = dose.getTimeTaken().toLocalDateTime();
        // Check that dose is within Schedule boundaries
        LocalDate scheduleStart = dose.getSchedule().getAssignedStartDate().toLocalDate();
        LocalDate scheduleEnd = dose.getSchedule().getDeviceEndDate().toLocalDate();
        if (timeTaken.isBefore(scheduleStart.atTime(0, 0)) || timeTaken.isAfter(scheduleEnd.atTime(0,0))) {
            return false;
        }
        int dayOfSchedule = (int) scheduleStart.until(timeTaken, ChronoUnit.DAYS);
        for (ScheduledDose scheduledDose: dose.getSchedule().getScheduledDoses()) {
            if (dayOfSchedule != scheduledDose.getStartDay()) {
                if (scheduledDose.getRepeatInterval() == null) {
                    continue;
                } else {
                    if ((dayOfSchedule - scheduledDose.getStartDay()) % scheduledDose.getRepeatInterval() != 0) {
                        continue;
                    }
                }
            }
            LocalTime timeTakenTime = timeTaken.toLocalTime();
            LocalTime windowStartTime = scheduledDose.getWindowStartTime().toLocalTime();
            LocalTime windowEndTime = scheduledDose.getWindowEndTime().toLocalTime();
            if (timeTakenTime.isAfter(windowStartTime) && timeTakenTime.isBefore(windowEndTime)) {
                return true;
            }
        }
    }
}