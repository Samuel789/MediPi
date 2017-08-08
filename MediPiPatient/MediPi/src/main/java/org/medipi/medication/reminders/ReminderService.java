package org.medipi.medication.reminders;

import org.medipi.MediPi;
import org.medipi.medication.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

public class ReminderService {
    private MediPi mediPi;
    private Collection<ReminderEventInterface> todayActiveEvents;
    private Collection<ReminderEventInterface> events;
    private LocalDate today;

    public ReminderService(MediPi mediPi, Collection<ReminderEventInterface> events) {
        this.mediPi = mediPi;
        setEvents(events);
        startReminderService();
    }
    public void throwTestReminder() {
        MedicationManager medicationManager =(MedicationManager) mediPi.getElement("Medication");
        System.out.println(medicationManager);
        Schedule testSchedule = new Schedule();
        ScheduledDose testDose = new ScheduledDose();
        Medication testMedication = new Medication();
        DoseUnit testDoseUnit = new DoseUnit();
        testDoseUnit.setName("dose units");
        testMedication.setFullName("Medication full name");
        testMedication.setCautionaryText("! Cautionary Line #1\n! Cautionary Line #2");
        testMedication.setShortName("Short Name");
        testMedication.setDoseUnit(testDoseUnit);
        testSchedule.setAlternateName("Medication Alternate Name");
        testSchedule.setMedication(testMedication);
        testSchedule.setPurposeStatement("Medication Purpose Statement");
        testDose.setSchedule(testSchedule);
        testDose.setWindowStartTime(Time.valueOf("14:00:00"));
        testDose.setWindowEndTime(Time.valueOf("16:00:00"));
        testDose.setReminderTime(Time.valueOf("15:00:00"));
        testDose.setDoseValue(4.5);
        MedicationReminderEvent event = new MedicationReminderEvent(testDose);
        event.execute(mediPi);
    }

    public void setEvents(Collection<ReminderEventInterface> events) {
        this.events = events;
        redetermineTodaysEvents();
    }

    public void dismissEvent(ReminderEventInterface event) {
        todayActiveEvents.remove(event);
    }

    private void redetermineTodaysEvents() {
        todayActiveEvents = new HashSet<>();
        today = LocalDate.now();
        for (ReminderEventInterface event: events) {
            if (event.activeOnDay(today)) {
                todayActiveEvents.add(event);
            }
        }
    }

    private void startReminderService() {
        // TODO - start reminder service
    }

    public Collection<ReminderEventInterface> getTodayActiveEvents() {
        return todayActiveEvents;
    }

    public Collection<ReminderEventInterface> getAllEvents() {
        return todayActiveEvents;
    }
}
