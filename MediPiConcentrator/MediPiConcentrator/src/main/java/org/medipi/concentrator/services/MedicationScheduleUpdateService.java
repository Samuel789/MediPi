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

import static org.medipi.concentrator.logic.ScheduleUtilities.getOptimizedEndDate;
import static org.medipi.concentrator.logic.ScheduleUtilities.moveScheduleStartDate;

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

    private Schedule getExistingSchedule(LocalDate date, Medication medication, String patientUuid) {
        List<Schedule> existing_schedules = scheduleDAOimpl.findByMedicationAndPatient(medication, patientUuid);
        for (Schedule schedule : existing_schedules) {
            if ((schedule.getAssignedEndDate() == null || schedule.getAssignedEndDate().toLocalDate().isAfter(date.plusDays(1)))) {
                return schedule;
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addSchedule(Schedule newSchedule, List<ScheduledDose> newDoses, long medicationId) {
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
        newSchedule.setAssignedEndDate(getOptimizedEndDate(newSchedule));
        scheduleDAOimpl.update(newSchedule);
    }

    private void deleteSchedule(Schedule schedule) {
        for (ScheduledDose dose : schedule.getScheduledDoses()) {
            scheduledDoseDAOimpl.delete(dose.getScheduledDoseId());
        }
        scheduleDAOimpl.delete(schedule.getScheduleId());
    }
}

