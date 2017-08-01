package org.medipi.concentrator.dao;

import org.medipi.medication.Schedule;

import java.util.List;

public interface ScheduleDAO extends GenericDAO<Schedule> {
    Schedule findByMedicationId(int medicationId);

    List<Schedule> findAll();
}
