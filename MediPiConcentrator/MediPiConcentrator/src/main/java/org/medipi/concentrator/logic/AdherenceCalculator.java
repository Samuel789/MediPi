package org.medipi.concentrator.logic;

import org.apache.tomcat.jni.Local;
import org.medipi.medication.RecordedDose;
import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public List<DoseInstance> unpackDoses(ScheduledDose scheduledDose, int startDay, int endDay) {
        assert(startDay < endDay && startDay >= 0 && endDay >= 0);
        int sequenceStartDay = Math.max(startDay, scheduledDose.getStartDay());
        int sequenceEndDay = Math.min(endDay, scheduledDose.getEndDay());
        if (scheduledDose.getRepeatInterval() == null) {
            if (startDay >= sequenceStartDay && startDay < sequenceEndDay) {
                return Arrays.singletonList(new DoseInstance(scheduledDose, startDay));
            }
        }
        int relativeStartDay = Math.max(startDay, scheduledDose.getStartDay()) - startDay;
        int offset = (sequenceStartDay - startDay) % scheduledDose.getRepeatInterval();
        List<DoseInstance> doses = range(sequenceStartDay + offset, sequenceEndDay, scheduledDose.getRepeatInterval()).mapToObj(day -> new DoseInstance(scheduledDose, day)).collect(Collectors.toList());
        return doses;
    }

    private IntStream range(int start, int stop, int step) {
        return IntStream.range(start, stop/step).map(i -> i*step);
    }

    public computePatientAdherence(String patientUuid) {

    }
}

class DoseInstance {
    public ScheduledDose getDose() {
        return dose;
    }

    private ScheduledDose dose;

    public int getDay() {
        return day;
    }


    private int day;

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    private LocalTime timeStart;
    private LocalTime timeEnd;

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    private boolean taken;

    DoseInstance(ScheduledDose dose, int day) {
        this.dose = dose;
        this.day = day;
        this.timeStart = dose.getWindowStartTime().toLocalTime();
        this.timeEnd = dose.getWindowEndTime().toLocalTime();
    }

}