package org.medipi.concentrator.dao;

import org.medipi.medication.PatientAdherence;
import org.medipi.medication.Schedule;

import java.util.List;

public interface PatientAdherenceDAO extends GenericDAO<PatientAdherence> {
    PatientAdherence findByPatientUuid(String patientUuid);
}
