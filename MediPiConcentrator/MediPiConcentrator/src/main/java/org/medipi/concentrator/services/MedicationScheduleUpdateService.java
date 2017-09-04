package org.medipi.concentrator.services;

import org.medipi.concentrator.dao.MedicationDAOImpl;
import org.medipi.concentrator.dao.ScheduleDAOImpl;
import org.medipi.concentrator.dao.ScheduledDoseDAOImpl;
import org.medipi.medication.model.Medication;
import org.medipi.medication.model.Schedule;
import org.medipi.medication.model.ScheduledDose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
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
            if ((schedule.getEndDate() == null || schedule.getEndDate().toLocalDate().isAfter(date))) {
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
        if (existing_schedule != null) {
            assert (existing_schedule.getPatientUuid().equals(newSchedule.getPatientUuid()));
            existing_schedule.setEndDate(Date.valueOf(tomorrow));

            // Clean up previous schedule changes from today (which were never able to come into effect)
            if (existing_schedule.getEndDate().equals(existing_schedule.getStartDate())) {
                deleteSchedule(existing_schedule);
            }
        }
        if (newSchedule.getStartDate().equals(newSchedule.getEndDate())) {
            return;
        }
        scheduleDAOimpl.save(newSchedule);
        newSchedule.setScheduledDoses(new HashSet<>(newDoses));
        if (newSchedule.getStartDate().toLocalDate().isBefore(tomorrow)) {
            moveScheduleStartDate(newSchedule, tomorrow);
        }
        for (ScheduledDose new_dose : newSchedule.getScheduledDoses()) {
            new_dose.setScheduledDoseId(null);
            new_dose.setScheduleId(newSchedule.getScheduleId());
            scheduledDoseDAOimpl.save(new_dose);
        }
        newSchedule.setEndDate(getOptimizedEndDate(newSchedule));
        scheduleDAOimpl.update(newSchedule);
    }

    private void deleteSchedule(Schedule schedule) {
        for (ScheduledDose dose : schedule.getScheduledDoses()) {
            scheduledDoseDAOimpl.delete(dose.getScheduledDoseId());
        }
        scheduleDAOimpl.delete(schedule.getScheduleId());
    }
}

