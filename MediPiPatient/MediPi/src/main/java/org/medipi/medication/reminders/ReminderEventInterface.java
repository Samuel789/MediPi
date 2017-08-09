package org.medipi.medication.reminders;

import org.medipi.MediPi;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by sam on 07/08/17.
 */
public interface ReminderEventInterface {
    public LocalTime getReminderStartTime();
    public LocalTime getReminderEndTime();
    public boolean activeOnDay(LocalDate date);
    public boolean isFrozen();
    public void execute(MediPi mediPi);
}
