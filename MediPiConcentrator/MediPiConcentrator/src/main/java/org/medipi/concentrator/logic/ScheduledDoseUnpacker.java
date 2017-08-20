package org.medipi.concentrator.logic;

import org.medipi.concentrator.dao.ScheduleDAOImpl;
import org.medipi.medication.DoseInstance;
import org.medipi.medication.ScheduledDose;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ScheduledDoseUnpacker {
    @Autowired
    private ScheduleDAOImpl scheduleDAOimpl;

    public static List<DoseInstance> unpack(ScheduledDose scheduledDose, int startDay, int endDay) {
        validateArguments(scheduledDose, startDay, endDay);
        int sequenceStartDay = Math.max(startDay, scheduledDose.getStartDay());
        int sequenceEndDay;
        if (scheduledDose.getEndDay() != null) {
            sequenceEndDay = Math.min(endDay, scheduledDose.getEndDay());
        } else {
            sequenceEndDay = endDay;

        }
        if (scheduledDose.getSchedule().getAssignedEndDate() != null) {
            sequenceEndDay = (int) scheduledDose.getSchedule().getAssignedStartDate().toLocalDate().until(scheduledDose.getSchedule().getAssignedEndDate().toLocalDate(), ChronoUnit.DAYS);
        }
        if (scheduledDose.getRepeatInterval() == null) {
            if (scheduledDose.getStartDay() >= sequenceStartDay && scheduledDose.getStartDay() < sequenceEndDay) {
                return Collections.singletonList(new DoseInstance(scheduledDose, scheduledDose.getStartDay()));
            } else {
                return Collections.emptyList();
            }
        }
        int offset = (sequenceStartDay - scheduledDose.getStartDay()) % scheduledDose.getRepeatInterval();
        int startOffset = (scheduledDose.getRepeatInterval() - offset) % scheduledDose.getRepeatInterval();
        int endOffset = ((sequenceEndDay - scheduledDose.getStartDay()) % scheduledDose.getRepeatInterval() > 0) ? scheduledDose.getRepeatInterval() : 0;
        List<DoseInstance> doses = range(sequenceStartDay + startOffset, sequenceEndDay + endOffset, scheduledDose.getRepeatInterval()).mapToObj(day -> new DoseInstance(scheduledDose, day)).collect(Collectors.toList());
        return doses;
    }

    private static void validateArguments(ScheduledDose scheduledDose, int startDay, int endDay) {
        if (startDay < 0) {
            throw new IllegalArgumentException("Start day cannot be less than zero (" + startDay + " provided)");
        } else if (startDay > endDay) {
            throw new IllegalArgumentException("End day cannot be before (" + startDay + ", " + endDay + " provided)");
        }
    }

    static IntStream range(int start, int stop, int step) {
        return IntStream.range(0, (stop - start) / step).map(i -> i * step + start);
    }
}
