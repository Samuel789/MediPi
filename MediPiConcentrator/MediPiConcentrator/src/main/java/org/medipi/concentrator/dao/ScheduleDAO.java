package org.medipi.concentrator.dao;

import org.medipi.medication.Medication;
import org.medipi.medication.Schedule;

import java.util.List;

public interface ScheduleDAO extends GenericDAO<Schedule> {
    Schedule findByScheduleId(int medicationId);
    List<Schedule> findByPatientUuid(String patientUuid);
    List<Schedule> findByMedicationAndPatient(Medication medication, String patientUuid);
    List<Schedule> findAll();
}
