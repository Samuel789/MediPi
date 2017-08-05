package org.medipi.concentrator.dao;

import org.medipi.medication.ScheduledDose;

import java.util.List;

/**
 * Created by sam on 02/08/17.
 */
public interface ScheduledDoseDAO extends GenericDAO<ScheduledDose> {
    ScheduledDose findByMedicationId(int scheduledDoseId);

    List<ScheduledDose> findAll();
}
