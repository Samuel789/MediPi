package org.medipi.medication.reminders;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.medipi.MediPi;
import org.medipi.medication.model.ScheduledDose;
import org.medipi.medication.medicationui.MedicationReminder;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by sam on 07/08/17.
 */
public class MedicationReminderEvent implements ReminderEventInterface {

    private LocalTime alarmTime;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean frozen = false;
    private ScheduledDose dose;

    public MedicationReminderEvent(ScheduledDose dose) {
        alarmTime = dose.getReminderTime().toLocalTime();
        startTime = dose.getWindowStartTime().toLocalTime();
        endTime = dose.getWindowEndTime().toLocalTime();
        this.dose = dose;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public ScheduledDose getDose() {
        return dose;
    }

    @Override
    public boolean isFrozen() {
        return frozen;
    }

    @Override
    public LocalTime getReminderStartTime() {
        return alarmTime;
    }

    @Override
    public LocalTime getReminderEndTime() {
        return endTime;
    }

    public void snoozeForSeconds(int seconds) throws SnoozeNotAllowedException {
        if (isSnoozeAllowed(seconds)) {
            alarmTime = LocalTime.now().plusSeconds(seconds);
            frozen = false;
        } else {
            throw new SnoozeNotAllowedException();
        }
    }

    public boolean isSnoozeAllowed(int seconds) {
        return LocalTime.now().plusSeconds(seconds).isBefore(endTime);
    }

    @Override
    public void trigger(MediPi mediPi) {
        frozen = true;
        Stage popupWindow = new Stage();
        MedicationReminder popupContents = new MedicationReminder(this, mediPi, popupWindow);
        Scene popupScene = new Scene(popupContents);
        popupContents.getStylesheets().addAll(mediPi.scene.getStylesheets());
        popupWindow.initStyle(StageStyle.UNDECORATED);
        popupWindow.setScene(popupScene);
        popupWindow.setTitle("Medication Information");
        popupWindow.initModality(Modality.WINDOW_MODAL);
        popupWindow.initOwner(mediPi.scene.getWindow());
        popupWindow.showAndWait();
    }

    @Override
    public boolean activeOnDay(LocalDate date) {
        LocalDate scheduleStartDate = dose.getSchedule().getAssignedStartDate().toLocalDate();
        long dayOfSchedule = scheduleStartDate.until(date, DAYS);
        // Check that date lies within schedule boundaries
        if (dayOfSchedule < 0) {
            return false;
        }
        Date scheduleEndDateSql = dose.getSchedule().getAssignedEndDate();
        if (scheduleEndDateSql != null) {
            LocalDate scheduleEndDate = scheduleEndDateSql.toLocalDate();
            long scheduleEndDay = scheduleStartDate.until(scheduleEndDate, DAYS);
            if (dayOfSchedule >= scheduleEndDay) {
                return false;
            }
        }
        // Determine if dose repeats on given date
        boolean repeatsToday;
        if (dayOfSchedule == dose.getStartDay()) {
            repeatsToday = true;
        } else if (dayOfSchedule < dose.getStartDay()) {
            repeatsToday = false;
        } else if (dose.getRepeatInterval() == null || dose.getRepeatInterval() == 0) {
            repeatsToday = false;
        } else {
            double intervals = (dayOfSchedule - dose.getStartDay()) / dose.getRepeatInterval();
            repeatsToday = (intervals == (int) intervals);
        }
        return repeatsToday;
    }

}
