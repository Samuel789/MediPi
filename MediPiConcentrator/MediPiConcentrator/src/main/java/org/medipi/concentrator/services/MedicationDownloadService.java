package org.medipi.concentrator.services;

import org.medipi.concentrator.dao.*;
import org.medipi.concentrator.exception.NotFound404Exception;
import org.medipi.concentrator.utilities.Utilities;
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
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class MedicationDownloadService {

    @Autowired
    private Utilities utils;

    @Autowired
    private PatientDeviceValidationService patientDeviceValidationService;

    @Autowired
    private MedicationDAOImpl medicationDAOImpl;

    @Autowired
    private ScheduleDAOImpl scheduleDAOimpl;

    @Autowired
    private ScheduledDoseDAOImpl scheduledDoseDAOimpl;

    @Autowired
    private RecordedDoseDAOImpl recordedDoseDAOimpl;

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<MedicationDO> getMedicationData(String hardware_name, String patientUuid) {
        assert patientDeviceValidationService != null;
        ResponseEntity<?> r = null;
        try {
            // Check that the device and patient are registered with each other
            r = this.patientDeviceValidationService.validate(hardware_name, patientUuid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NotFound404Exception("Hardware and/or patient not registered" + e.getLocalizedMessage());
        }
        MedicationDO medicationInfo = new MedicationDO();
        List<ScheduledDose> doses = new ArrayList<>();
        System.out.println(org.hibernate.Version.getVersionString());
        try {
            medicationInfo.setSchedules(scheduleDAOimpl.findAll());
        for (Schedule schedule: medicationInfo.getSchedules()) {
            System.out.println(schedule.getAssignedStartDate());
        }


        } catch (Exception e) {
            System.out.println(String.format("Failed to process query results (or none returned). Error was %s: %s", e.getClass(), e.getMessage()));
        }
        // TODO - Embed medication info
        try {
            return new ResponseEntity<MedicationDO>(medicationInfo, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(String.format("Creation of ResponseEntity failed. Error was %s: %s", e.getClass(), e.getMessage()));
        }
        return new ResponseEntity<MedicationDO>(medicationInfo, HttpStatus.OK);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void uploadRecordedDoses(MedicationDO uploadedData) {
        List<RecordedDose> doseData = new ArrayList<>();
        for (Schedule schedule: uploadedData.getSchedules()) {
            for (RecordedDose dose : schedule.getRecordedDoses()) {
                System.out.println(dose.getTimeTaken());
            }
            doseData.addAll(schedule.getRecordedDoses());
        }
        System.out.println("RECEIVED UPLOAD: " + " " + doseData.size());
        for (RecordedDose dose: doseData) {
            try{recordedDoseDAOimpl.findByRecordedDoseUUID(dose.getRecordedDoseUUID());
            } catch (EmptyResultDataAccessException e) {
                // TODO - Deserialization results in misinterpretation of UTC timestamp, currently using a dirty fix;
                ZonedDateTime zonedDateTime = dose.getTimeTaken().toLocalDateTime().atZone(TimeZone.getTimeZone("Europe/London").toZoneId());
                dose.setTimeTaken(new Timestamp(zonedDateTime.toEpochSecond()*1000));
                recordedDoseDAOimpl.save(dose);
            }
        }
    }
}