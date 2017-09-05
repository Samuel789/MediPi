package org.medipi.concentrator.dao;

import org.medipi.medication.model.Schedule;
import org.medipi.medication.model.ScheduleAdherence;

import java.util.List;

public interface ScheduleAdherenceDAO extends GenericDAO<ScheduleAdherence> {
    void deleteWithId(int id);

}
