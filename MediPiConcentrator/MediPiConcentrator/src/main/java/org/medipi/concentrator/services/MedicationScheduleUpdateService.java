package org.medipi.concentrator.services;

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
    private MedicationDAOImpl medicationDAOImpl;

    private void moveScheduleStartDate(Schedule schedule, LocalDate newStartDate) {
        LocalDate existingStartDate = schedule.getAssignedStartDate().toLocalDate();
        if (existingStartDate.equals(newStartDate)) {
            return;
        }
        int dateDifference = (int) existingStartDate.until(newStartDate, ChronoUnit.DAYS);
        schedule.setAssignedStartDate(Date.valueOf(newStartDate));
        List<ScheduledDose> removeList = new ArrayList<>();
        for (ScheduledDose dose : schedule.getScheduledDoses()) {
            try {
                transposeDose(dose, dateDifference);
            } catch (DoseOutOfBoundsException e) {
                //
                removeList.add(dose);
            }
        }
        schedule.getScheduledDoses().removeAll(removeList);
    }

    private void transposeDose(ScheduledDose dose, int days) throws DoseOutOfBoundsException {
        Integer repeatUnit = dose.getRepeatInterval();
        if (repeatUnit == null) {
            if (dose.getStartDay() < days) {
                throw new DoseOutOfBoundsException();
            } else {
                dose.setStartDay(dose.getStartDay() - days);
            }
        } else {
            dose.setStartDay(repeatUnit - (days % repeatUnit));
            if (dose.getEndDay() != null) {
                dose.setEndDay(dose.getEndDay() - days);
                if (dose.getEndDay() <= dose.getStartDay()) {
                    throw new DoseOutOfBoundsException();
                }
            }
        }
    }

    private Schedule getExistingSchedule(LocalDate date, Medication medication, String patientUuid) {
        List<Schedule> existing_schedules = scheduleDAOimpl.findByMedicationAndPatient(medication, patientUuid);
        for (Schedule schedule : existing_schedules) {
            if ((schedule.getAssignedEndDate() == null || schedule.getAssignedEndDate().toLocalDate().isAfter(date.plusDays(1)))) {
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
        for (ScheduledDose dose : schedule.getScheduledDoses()) {
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
    public void addSchedule(Schedule newSchedule, List<ScheduledDose> newDoses, int medicationId) {
        Medication medication = medicationDAOImpl.findByMedicationId(medicationId);
        newSchedule.setMedication(medication);
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        newSchedule.setScheduleId(null);
        Schedule existing_schedule = getExistingSchedule(tomorrow, medication, newSchedule.getPatientUuid());
        scheduleDAOimpl.save(newSchedule);
        newSchedule.setScheduledDoses(new HashSet<>(newDoses));
        if (newSchedule.getAssignedStartDate().toLocalDate().isBefore(tomorrow)) {
            moveScheduleStartDate(newSchedule, tomorrow);
        }
        if (existing_schedule != null) {
            assert (existing_schedule.getPatientUuid().equals(newSchedule.getPatientUuid()));
            existing_schedule.setAssignedEndDate(Date.valueOf(tomorrow));

            // Clean up previous schedule changes from today (which were never able to come into effect)
            if (existing_schedule.getAssignedEndDate().equals(existing_schedule.getAssignedStartDate())) {
                deleteSchedule(existing_schedule);
            }
        }
        for (ScheduledDose new_dose : newSchedule.getScheduledDoses()) {
            new_dose.setScheduledDoseId(null);
            new_dose.setScheduleId(newSchedule.getScheduleId());
            scheduledDoseDAOimpl.save(new_dose);
        }
        newSchedule.setAssignedEndDate(getMaxEndDate(newSchedule));
        scheduleDAOimpl.update(newSchedule);
    }

    private void deleteSchedule(Schedule schedule) {
        for (ScheduledDose dose : schedule.getScheduledDoses()) {
            scheduledDoseDAOimpl.delete(dose.getScheduledDoseId());
        }
        scheduleDAOimpl.delete(schedule.getScheduleId());
    }
}

class DoseOutOfBoundsException extends Exception {
}