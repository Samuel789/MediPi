package org.medipi.concentrator.services;

import org.apache.tomcat.jni.Local;
import org.medipi.concentrator.dao.MedicationDAOImpl;
import org.medipi.concentrator.dao.PatientDAOImpl;
import org.medipi.concentrator.dao.ScheduleDAOImpl;
import org.medipi.concentrator.dao.ScheduledDoseDAOImpl;
import org.medipi.medication.Medication;
import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by sam on 19/08/17.
 */
@Service
public class MedicationScheduleUpdateService {
    @Autowired
    private ScheduleDAOImpl scheduleDAOimpl;

    @Autowired
    private ScheduledDoseDAOImpl scheduledDoseDAOimpl;

    @Autowired
    private PatientDAOImpl patientDAOimpl;

    @Autowired
    private MedicationDAOImpl medicationDAOImpl;

    private void moveScheduleStartDate(Schedule schedule, LocalDate newStartDate) {
        LocalDate existingStartDate = schedule.getAssignedStartDate().toLocalDate();
        if (existingStartDate.equals(newStartDate)) {
            return;
        }
        int dateDifference = (int) existingStartDate.until(newStartDate, ChronoUnit.DAYS);
        schedule.setAssignedStartDate(Date.valueOf(newStartDate));
        List<ScheduledDose> removeDoses = new ArrayList<>();
        for (ScheduledDose dose: schedule.getScheduledDoses()) {
            Integer repeatUnit = dose.getRepeatInterval();
            if (repeatUnit == null) {
                if (dose.getStartDay() < dateDifference) {
                    removeDoses.add(dose);
                } else {
                    dose.setStartDay(dose.getStartDay() - dateDifference);
                }
            } else {
                dose.setStartDay(repeatUnit - (dateDifference % repeatUnit));
                if (dose.getEndDay() != null) {
                    dose.setEndDay(dose.getEndDay() - dateDifference);
                    if (dose.getEndDay() <= dose.getStartDay()) {
                        removeDoses.add(dose);
                    }
                }
            }
        }
        for (ScheduledDose dose: removeDoses) {
            schedule.getScheduledDoses().remove(dose);
        }
    }

    private Schedule getExistingSchedule(LocalDate date, Medication medication, String patientUuid) {
        List<Schedule> existing_schedules = scheduleDAOimpl.findByMedicationAndPatient(medication, patientUuid);
        for (Schedule schedule: existing_schedules) {
            if (!schedule.getAssignedStartDate().toLocalDate().isAfter(date) && (schedule.getAssignedEndDate() == null || schedule.getAssignedEndDate().toLocalDate().isAfter(date))) {
                return schedule;
            }
        }
        return null;
    }

    private Date getMaxEndDate(Schedule schedule) {
        if (schedule.getScheduledDoses().size() == 0) {
            return null; // Take as needed
        }
        int maxDay = 0;
        for (ScheduledDose dose: schedule.getScheduledDoses()) {
            if (dose.getRepeatInterval() != null) {
                if (dose.getEndDay() == null) {
                    return null;
                } else {
                    maxDay = Math.max(maxDay, dose.getEndDay() - ((dose.getEndDay() - dose.getStartDay()) % dose.getRepeatInterval()));
                }
            } else {
                maxDay = Math.max(maxDay, dose.getStartDay());
            }


        }
        return Date.valueOf(schedule.getAssignedStartDate().toLocalDate().plusDays(maxDay + 1));
    }


    @Transactional(rollbackFor = Throwable.class)
    public void addSchedule(Schedule newSchedule, List<ScheduledDose> new_doses, int medicationId) {
        Medication medication = medicationDAOImpl.findByMedicationId(medicationId);
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        newSchedule.setScheduleId(null);
        newSchedule.setMedication(medication);
        Schedule existing_schedule = getExistingSchedule(tomorrow, medication, newSchedule.getPatientUuid());
        if (existing_schedule == null) {
            System.out.println("CREATING NEW SCHEDULE");
            scheduleDAOimpl.save(newSchedule);
            newSchedule.setScheduledDoses(new HashSet<ScheduledDose>());
            for (ScheduledDose new_dose : new_doses) {
                new_dose.setScheduleId(newSchedule.getScheduleId());
                new_dose.setScheduledDoseId(null);
                newSchedule.getScheduledDoses().add(new_dose);
                scheduledDoseDAOimpl.save(new_dose);
            }
        } else {
            System.out.println("UPDATING EXISTING SCHEDULE");
            for (ScheduledDose dose: existing_schedule.getScheduledDoses()) {
                scheduledDoseDAOimpl.delete(dose.getScheduledDoseId());
            }
            assert (existing_schedule.getMedication() == newSchedule.getMedication());
            assert (existing_schedule.getPatientUuid().equals(newSchedule.getPatientUuid()));
            System.out.println("DOSES CHANGED");

            if (existing_schedule.getAssignedEndDate().toLocalDate().isAfter(tomorrow)) {
                existing_schedule.setAssignedEndDate(Date.valueOf(tomorrow));
            }
            scheduleDAOimpl.save(newSchedule);
            System.out.println("New ID: " + newSchedule.getScheduleId());
            newSchedule.setScheduledDoses(new HashSet<>());
            for (ScheduledDose new_dose : new_doses) {
                new_dose.setScheduleId(newSchedule.getScheduleId());
                new_dose.setScheduledDoseId(null);
                newSchedule.getScheduledDoses().add(new_dose);
                scheduledDoseDAOimpl.save(new_dose);
            }
            moveScheduleStartDate(newSchedule, tomorrow);
            scheduleDAOimpl.update(newSchedule);
            // Clean up previous schedule changes from today (which were never able to come into effect)
            if (existing_schedule.getAssignedEndDate().equals(existing_schedule.getAssignedStartDate())) {
                scheduleDAOimpl.delete(existing_schedule.getScheduleId());
            }
        }
        newSchedule.setAssignedEndDate(getMaxEndDate(newSchedule));
        scheduleDAOimpl.update(newSchedule);
    }
}
