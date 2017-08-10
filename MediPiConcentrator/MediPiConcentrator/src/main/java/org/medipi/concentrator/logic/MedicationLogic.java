package org.medipi.concentrator.logic;

import org.medipi.medication.RecordedDose;
import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class MedicationLogic {
    public static boolean dueOnDay(ScheduledDose dose, int day) {
        int dayOfDose = day - dose.getStartDay();
        if (dayOfDose == dose.getStartDay()) return true;
        else if (dayOfDose < 0) return false;
        else if (dose.getRepeatInterval() == null || dose.getRepeatInterval() == 0) return false;
        else if (dose.getEndDay() != null && dayOfDose >= dose.getEndDay()) return false;
        else return dayOfDose % dose.getRepeatInterval() == 0;
    }
    public static int getDayOfSchedule(Schedule schedule, LocalDate date) {
        return (int) schedule.getAssignedStartDate().toLocalDate().until(date, ChronoUnit.DAYS);
    }
    public static boolean satisfiesScheduledDose(RecordedDose recordedDose, ScheduledDose scheduledDose) {
        if (recordedDose.getSchedule() != scheduledDose.getSchedule()) {
            return false;
        }
        LocalDateTime takenDateTime = recordedDose.getTimeTaken().toLocalDateTime();
        int dayOfSchedule = getDayOfSchedule(recordedDose.getSchedule(), takenDateTime.toLocalDate());
        if (!dueOnDay(scheduledDose, dayOfSchedule)) {
            return false;
        }
        LocalTime windowStartTime = scheduledDose.getWindowStartTime().toLocalTime();
        LocalTime windowEndTime = scheduledDose.getWindowEndTime().toLocalTime();
        LocalTime takenTime = takenDateTime.toLocalTime();
        return takenTime.isAfter(windowStartTime) && takenTime.isBefore(windowEndTime);
    }
    public static int countDosesBetween(int startDay, int endDay, ScheduledDose dose) {
        assert(startDay < endDay && startDay >= 0 && endDay >= 0);
        int relativeStartDay = Math.max(startDay, dose.getStartDay()) - startDay;
        int relativeEndDay;
        if (dose.getEndDay() == null) {
            relativeEndDay = endDay - startDay;
        } else {
            relativeEndDay = Math.min(endDay, dose.getEndDay()) - startDay;
        }
        if (dose.getRepeatInterval() == null) {
            return relativeStartDay == 0 ? 1 : 0;
        } else {
            int intervalWidth = relativeEndDay - relativeStartDay;
            int baseCount = intervalWidth / dose.getRepeatInterval();
            if (intervalWidth % dose.getRepeatInterval() == 0) {
                return baseCount;
            } else if (relativeStartDay == 0) {
                return baseCount + 1;
            } else {
                if (relativeStartDay % dose.getRepeatInterval() + intervalWidth < intervalWidth) {
                    return baseCount + 1;
                } else {
                    return baseCount;
                }
            }
        }
    }
}
