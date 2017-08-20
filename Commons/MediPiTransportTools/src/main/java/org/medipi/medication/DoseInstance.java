package org.medipi.medication;

import java.sql.Time;

public class DoseInstance {
    public RecordedDose takenDose;
    private ScheduledDose dose;
    private int scheduleId;
    private double doseValue;
    private int day;
    private Time timeStart;
    private Time timeEnd;

    public DoseInstance(ScheduledDose dose, int day) {
        this.dose = dose;
        this.day = day;
        this.timeStart = dose.getWindowStartTime();
        this.timeEnd = dose.getWindowEndTime();
        this.scheduleId = dose.getScheduleId();
        this.doseValue = dose.getDoseValue();
    }

    public ScheduledDose getDose() {
        return dose;
    }

    public RecordedDose getTakenDose() {
        return takenDose;
    }

    public void setTakenDose(RecordedDose takenDose) {
        this.takenDose = takenDose;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public double getDoseValue() {
        return doseValue;
    }

    public void setDoseValue(double doseValue) {
        this.doseValue = doseValue;
    }

    public int getDay() {
        return day;
    }

    public Time getTimeStart() {
        return timeStart;
    }

    public Time getTimeEnd() {
        return timeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DoseInstance that = (DoseInstance) o;

        if (day != that.day) return false;
        if (dose != null ? !dose.equals(that.dose) : that.dose != null) return false;
        if (timeStart != null ? !timeStart.equals(that.timeStart) : that.timeStart != null) return false;
        return timeEnd != null ? timeEnd.equals(that.timeEnd) : that.timeEnd == null;
    }

    public String toString() {
        return String.format("DoseInstance: Day %d from %s to %s", day, timeStart, timeEnd);
    }

}
