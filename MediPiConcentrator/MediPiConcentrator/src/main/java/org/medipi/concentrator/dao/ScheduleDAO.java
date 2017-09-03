package org.medipi.concentrator.dao;

import org.medipi.medication.model.Medication;
import org.medipi.medication.model.Schedule;

import java.util.List;

public interface ScheduleDAO extends GenericDAO<Schedule> {
    Schedule findByScheduleId(int medicationId);

    List<Schedule> findByPatientUuid(String patientUuid);

    List<Schedule> findByMedicationAndPatient(Medication medication, String patientUuid);

    List<Schedule> findAll();
}
