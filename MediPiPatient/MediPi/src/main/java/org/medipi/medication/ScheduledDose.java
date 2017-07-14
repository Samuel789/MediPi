package org.medipi.medication;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduledDose {
    private double doseValue;
    private LocalDate startDay;
    private LocalDate endDay;
    private int repeatInterval;
    private LocalTime windowStartTime;
    private LocalTime windowEndTime;
    private LocalTime reminderTime;
}
