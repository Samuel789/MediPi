package org.medipi.concentrator.dao;

import org.medipi.medication.model.PatientAdherence;

public interface PatientAdherenceDAO extends GenericDAO<PatientAdherence> {
    PatientAdherence findByPatientUuid(String patientUuid);
}
