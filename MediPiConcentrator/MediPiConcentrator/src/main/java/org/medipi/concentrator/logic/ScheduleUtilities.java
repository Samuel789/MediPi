package org.medipi.concentrator.logic;

import org.medipi.medication.model.Schedule;
import org.medipi.medication.model.ScheduledDose;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 23/08/17.
 */
public class ScheduleUtilities {

    public static Integer toDayOfSchedule(Schedule schedule, LocalDate date) {
        if (date.isBefore(schedule.getAssignedStartDate().toLocalDate())) {
            throw new IllegalArgumentException("Date cannot be before beginning of schedule");
        }
        return (int) schedule.getAssignedStartDate().toLocalDate().until(date, ChronoUnit.DAYS);
    }

    public static LocalDate fromDayOfSchedule(Schedule schedule, int day) {
        if (day < 0) {
            throw new IllegalArgumentException("Day of schedule cannot be negative");
        }
        return schedule.getAssignedStartDate().toLocalDate().plusDays(day);
    }

    public static Date getOptimizedEndDate(Schedule schedule) {
        Date optimumUnboundedEndDate = getOptimizedUnboundedEndDate(schedule);
        if (optimumUnboundedEndDate == null || (schedule.getAssignedEndDate() != null && schedule.getAssignedEndDate().before(optimumUnboundedEndDate))) {
            return schedule.getAssignedEndDate();
        } else {
            return optimumUnboundedEndDate;
        }
    }

    public static Date getOptimizedUnboundedEndDate(Schedule schedule) {
        if (schedule.getScheduledDoses().size() == 0) {
            return null; // Take as needed
        }
        int maxDay = 0;
        for (ScheduledDose dose : schedule.getScheduledDoses()) {
            if (dose.getRepeatInterval() != null) {
                if (dose.getEndDay() == null) {
                    return null;
                } else {
                    maxDay = Math.max(maxDay, dose.getEndDay() - ((dose.getEndDay() - dose.getStartDay()) % dose.getRepeatInterval()));
                }
            } else {
                maxDay = Math.max(maxDay, dose.getStartDay());
            }
        }
        return Date.valueOf(schedule.getAssignedStartDate().toLocalDate().plusDays(maxDay + 1));
    }

    public static void transposeDose(ScheduledDose dose, int days) throws DoseOutOfBoundsException {
        Integer repeatUnit = dose.getRepeatInterval();
        if (repeatUnit == null) {
            if (dose.getStartDay() < days) {
                throw new DoseOutOfBoundsException();
            } else {
                dose.setStartDay(dose.getStartDay() - days);
            }
        } else {
            int newStartDay = 0;
            if (dose.getStartDay() > days) {
                newStartDay += dose.getStartDay() - days;
            }
            if (days % repeatUnit != 0) {
                newStartDay += repeatUnit - (days % repeatUnit);
            }
            dose.setStartDay(newStartDay);
            if (dose.getEndDay() != null) {
                dose.setEndDay(dose.getEndDay() - days);
                if (dose.getEndDay() <= dose.getStartDay()) {
                    throw new DoseOutOfBoundsException();
                }
            }
        }
    }

    public static void moveScheduleStartDate(Schedule schedule, LocalDate newStartDate) {
        LocalDate existingStartDate = schedule.getAssignedStartDate().toLocalDate();
        if (existingStartDate.equals(newStartDate)) {
            return;
        }
        int dateDifference = (int) existingStartDate.until(newStartDate, ChronoUnit.DAYS);
        schedule.setAssignedStartDate(Date.valueOf(newStartDate));
        List<ScheduledDose> removeList = new ArrayList<>();
        for (ScheduledDose dose : schedule.getScheduledDoses()) {
            try {
                transposeDose(dose, dateDifference);
            } catch (DoseOutOfBoundsException e) {
                //
                removeList.add(dose);
            }
        }
        schedule.getScheduledDoses().removeAll(removeList);
    }
}

class DoseOutOfBoundsException extends Exception {
}