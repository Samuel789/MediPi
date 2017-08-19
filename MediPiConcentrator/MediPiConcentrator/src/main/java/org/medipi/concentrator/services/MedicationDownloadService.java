package org.medipi.concentrator.services;

import org.medipi.concentrator.dao.*;
import org.medipi.concentrator.entities.Patient;
import org.medipi.concentrator.exception.NotFound404Exception;
import org.medipi.concentrator.logic.PatientAdherenceCalculator;
import org.medipi.concentrator.logic.ScheduleAdherenceCalculator;
import org.medipi.concentrator.utilities.Utilities;
import org.medipi.medication.*;
import org.medipi.model.MedWebDO;
import org.medipi.model.MedicationPatientDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.*;
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
        try {
            return new ResponseEntity<MedicationPatientDO>(medicationInfo, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(String.format("Creation of ResponseEntity failed. Error was %s: %s", e.getClass(), e.getMessage()));
        }
        return new ResponseEntity<MedicationPatientDO>(medicationInfo, HttpStatus.OK);
    }

    @Transactional(rollbackFor = Throwable.class)
    private void uploadRecordedDoses(MedicationPatientDO uploadedData) {
        List<RecordedDose> doseData = new ArrayList<>();
        for (Schedule schedule: uploadedData.getSchedules()) {
            doseData.addAll(schedule.getRecordedDoses());
        }
        System.out.println("RECEIVED UPLOAD: " + " " + doseData.size());
        for (RecordedDose dose: doseData) {
            try{recordedDoseDAOimpl.findByRecordedDoseUUID(dose.getRecordedDoseUUID());
            } catch (EmptyResultDataAccessException e) {
                recordedDoseDAOimpl.save(dose);
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ResponseEntity<MedicationPatientDO> synchronize(MedicationPatientDO uploadedData) {
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
        return getMedicationData(patientUuid);
    }

    @Transactional(rollbackFor = Throwable.class)
    private void updateAdherence(String patientUuid) {
        LocalDate searchEndTime = LocalDate.now();
        LocalDate searchStartTime = searchEndTime.minusDays(7);
        Collection<Schedule> patientSchedules = scheduleDAOimpl.findByPatientUuid(patientUuid);
        for (Schedule schedule: patientSchedules) {
            for (ScheduledDose dose: schedule.getScheduledDoses()) {
                dose.setSchedule(schedule);
            }
        }
        PatientAdherenceCalculator patientAdherenceCalculator = new PatientAdherenceCalculator(patientSchedules, searchStartTime, searchEndTime, true);
        patientAdherenceCalculator.calculatePatientAdherence();
        for (Schedule schedule: patientSchedules) {
            ScheduleAdherenceCalculator scheduleAdherenceCalculator = patientAdherenceCalculator.getScheduleAdherenceCalculators().get(schedule);
            if (schedule.getScheduleAdherence() == null) {
                ScheduleAdherence scheduleAdherence = new ScheduleAdherence();
                scheduleAdherence.setScheduleId(schedule.getScheduleId());
                schedule.setScheduleAdherence(scheduleAdherence);
            }
            schedule.getScheduleAdherence().setSevenDayFraction(scheduleAdherenceCalculator.getAdherenceFraction());
            schedule.getScheduleAdherence().setStreakLength(scheduleAdherenceCalculator.getStreakLength());
            scheduleAdherenceDAOimpl.save(schedule.getScheduleAdherence());
        }
        PatientAdherence patientAdherence = patientAdherenceDAOimpl.findByPatientUuid(patientUuid);
        patientAdherence.setSevenDayFraction(patientAdherenceCalculator.getAdherenceFraction());
        patientAdherence.setStreakLength(patientAdherenceCalculator.getStreakLength());
        patientAdherenceDAOimpl.update(patientAdherence);
        //TODO - find a better way than setting and unsetting the schedule
        for (Schedule schedule: patientSchedules) {
            for (ScheduledDose dose: schedule.getScheduledDoses()) {
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
        for (Patient patient: medicationPatients) {
            schedules.addAll(scheduleDAOimpl.findByPatientUuid(patient.getPatientUuid()));
            adherence.add(patientAdherenceDAOimpl.findByPatientUuid(patient.getPatientUuid()));
            patientUuids.add(patient.getPatientUuid());
        }
        MedWebDO responseData = new MedWebDO();
        responseData.setPatientAdherenceList(adherence);
        responseData.setPatientUuids(patientUuids);
        responseData.setSchedules(schedules);
        responseData.setMedications(medicationDAOImpl.findAll());
        return new ResponseEntity<MedWebDO>(responseData, HttpStatus.OK);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addSchedule(Schedule new_schedule, List<ScheduledDose> new_doses, int medicationId) {
        Schedule existing_schedule = scheduleDAOimpl.findByPrimaryKey(new_schedule.getScheduleId());
        if(existing_schedule == null) {
            new_schedule.setMedication(medicationDAOImpl.findByMedicationId(medicationId));
            scheduleDAOimpl.save(new_schedule);
            for (ScheduledDose new_dose: new_doses) {
                new_dose.setScheduleId(new_schedule.getScheduleId());
                new_schedule.getScheduledDoses().add(new_dose);
            }
            scheduleDAOimpl.save(new_schedule);
        } else {
            assert(existing_schedule.getMedication() == new_schedule.getMedication());
            assert(existing_schedule.getPatientUuid().equals(new_schedule.getPatientUuid()));
            boolean doses_changed = !existing_schedule.getScheduledDoses().equals(new_doses);
            if (doses_changed) {
                if (existing_schedule.getAssignedEndDate().getTime() > Date.valueOf(LocalDate.now().plusDays(1)).getTime()) {
                    existing_schedule.setAssignedEndDate(Date.valueOf(LocalDate.now().plusDays(1)));
                }
                new_schedule.setAssignedStartDate(new Date(Math.max(Date.valueOf(LocalDate.now()).getTime(), new_schedule.getAssignedStartDate().getTime())));
                new_schedule.setAlternateName(existing_schedule.getAlternateName());
                if (existing_schedule.getAssignedEndDate().equals(existing_schedule.getAssignedStartDate())) {
                    scheduleDAOimpl.delete(existing_schedule);
                }
            } else {
                existing_schedule.setPurposeStatement(new_schedule.getPurposeStatement());
                existing_schedule.setAssignedStartDate(new_schedule.getAssignedStartDate());
                existing_schedule.setAssignedEndDate(new_schedule.getAssignedEndDate());
                scheduleDAOimpl.save(existing_schedule);
            }
            //TODO - if doses change, new schedule, else modify schedule
        }
    }

}