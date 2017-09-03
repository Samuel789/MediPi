package org.medipi.model;

import org.medipi.medication.model.Schedule;
import org.medipi.medication.model.ScheduledDose;

import java.util.List;

public class MedicationScheduleDO {
    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<ScheduledDose> getDoses() {
        return doses;
    }

    public void setDoses(List<ScheduledDose> doses) {
        this.doses = doses;
    }

    private Schedule schedule;
    private List<ScheduledDose> doses;

    public long getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(long medicationId) {
        this.medicationId = medicationId;
    }

    private long medicationId;
}
