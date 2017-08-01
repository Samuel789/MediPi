package org.medipi.medication;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduledDose implements Serializable {
    public double getDoseValue() {
        return doseValue;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public int getRepeatInterval() {
        return repeatInterval;
    }

    public LocalTime getWindowStartTime() {
        return windowStartTime;
    }

    public LocalTime getWindowEndTime() {
        return windowEndTime;
    }

    public LocalTime getReminderTime() {
        return reminderTime;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    private double doseValue;
    private LocalDate startDay;
    private LocalDate endDay;
    private int repeatInterval;
    private LocalTime windowStartTime;
    private LocalTime windowEndTime;
    private LocalTime reminderTime;
    private Schedule schedule;

    public int getScheduledDoseId() {
        return scheduledDoseId;
    }

    public void setScheduledDoseId(int scheduledDoseId) {
        this.scheduledDoseId = scheduledDoseId;
    }

    private int scheduledDoseId;

    public ScheduledDose() {
        schedule = new Schedule();
        doseValue = 5;
        startDay = LocalDate.now();
    }
}
