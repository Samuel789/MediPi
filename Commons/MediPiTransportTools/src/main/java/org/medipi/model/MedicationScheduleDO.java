package org.medipi.model;

import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;

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

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    private int medicationId;
}
