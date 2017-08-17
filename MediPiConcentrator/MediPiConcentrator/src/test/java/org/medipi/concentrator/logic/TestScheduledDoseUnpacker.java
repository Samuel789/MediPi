package org.medipi.concentrator.logic;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestScheduledDoseUnpacker {

    Schedule schedule;
    ScheduledDose simpleDailyDose;
    ScheduledDose threeDayIntervalDose;
    ScheduledDose singleDayDose;
    ScheduledDose nullEndDayDose;

    @Before
    public void setUpSchedule() {
        schedule = new Schedule();
        schedule.setAssignedStartDate(Date.valueOf(LocalDate.of(2016, 2, 22)));
    }

    @Before
    public void createSimpleDailyDose() {
        Time startTime = Time.valueOf("08:00:00");
        Time endTime = Time.valueOf("14:00:00");
        simpleDailyDose = new ScheduledDose();
        simpleDailyDose.setStartDay(2);
        simpleDailyDose.setEndDay(8);
        simpleDailyDose.setRepeatInterval(1);
        simpleDailyDose.setWindowStartTime(startTime);
        simpleDailyDose.setWindowEndTime(endTime);
    }

    @Before
    public void createThreeDayIntervalDose() {
        Time startTime = Time.valueOf("08:00:00");
        Time endTime = Time.valueOf("14:00:00");
        threeDayIntervalDose = new ScheduledDose();
        threeDayIntervalDose.setStartDay(2);
        threeDayIntervalDose.setEndDay(15);
        threeDayIntervalDose.setRepeatInterval(3);
        threeDayIntervalDose.setWindowStartTime(startTime);
        threeDayIntervalDose.setWindowEndTime(endTime);
    }

    @Before
    public void createSingleDayDose() {
        Time startTime = Time.valueOf("08:00:00");
        Time endTime = Time.valueOf("14:00:00");
        singleDayDose = new ScheduledDose();
        singleDayDose.setStartDay(5);
        singleDayDose.setEndDay(null);
        singleDayDose.setRepeatInterval(null);
        singleDayDose.setWindowStartTime(startTime);
        singleDayDose.setWindowEndTime(endTime);
    }


    @Before
    public void createNullEndDayDose() {
        Time startTime = Time.valueOf("08:00:00");
        Time endTime = Time.valueOf("14:00:00");
        nullEndDayDose = new ScheduledDose();
        nullEndDayDose.setStartDay(3);
        nullEndDayDose.setEndDay(null);
        nullEndDayDose.setRepeatInterval(2);
        nullEndDayDose.setWindowStartTime(startTime);
        nullEndDayDose.setWindowEndTime(endTime);
    }

    @Test
    public void simpleDailyDose() {
        List<DoseInstance> expectedResult = new ArrayList<>();
        expectedResult.add(new DoseInstance(simpleDailyDose, 2));
        expectedResult.add(new DoseInstance(simpleDailyDose, 3));
        expectedResult.add(new DoseInstance(simpleDailyDose, 4));
        expectedResult.add(new DoseInstance(simpleDailyDose, 5));
        expectedResult.add(new DoseInstance(simpleDailyDose, 6));
        expectedResult.add(new DoseInstance(simpleDailyDose, 7));
        assert ScheduledDoseUnpacker.unpack(simpleDailyDose, 0, 14).equals(expectedResult);
    }

    @Test
    public void startAndEndDatesCorrectlyInterpreted() {
        List<DoseInstance> expectedResult = new ArrayList<>();
        expectedResult.add(new DoseInstance(simpleDailyDose, 2));
        expectedResult.add(new DoseInstance(simpleDailyDose, 3));
        expectedResult.add(new DoseInstance(simpleDailyDose, 4));
        assert ScheduledDoseUnpacker.unpack(simpleDailyDose, 2, 5).equals(expectedResult);
    }
    
    @Test
    public void arbitraryIntervalsWithNoOffset() {
        List<DoseInstance> expectedResult = new ArrayList<>();
        expectedResult.add(new DoseInstance(threeDayIntervalDose, 2));
        expectedResult.add(new DoseInstance(threeDayIntervalDose, 5));
        expectedResult.add(new DoseInstance(threeDayIntervalDose, 8));
        List<DoseInstance> result = ScheduledDoseUnpacker.unpack(threeDayIntervalDose, 0, 10);
        assert result.equals(expectedResult);
    }
    @Test
    public void arbitraryIntervalsWithOffset() {
        List<DoseInstance> expectedResult = new ArrayList<>();
        expectedResult.add(new DoseInstance(threeDayIntervalDose, 5));
        expectedResult.add(new DoseInstance(threeDayIntervalDose, 8));
        List<DoseInstance> result = ScheduledDoseUnpacker.unpack(threeDayIntervalDose, 3, 10);
        assert result.equals(expectedResult);
    }

    @Test
    public void endsOnDayBeforeEndDay() {
        List<DoseInstance> expectedResult = new ArrayList<>();
        expectedResult.add(new DoseInstance(simpleDailyDose, 2));
        expectedResult.add(new DoseInstance(simpleDailyDose, 3));
        assert ScheduledDoseUnpacker.unpack(simpleDailyDose, 0, 4).equals(expectedResult);
    }

    @Test
    public void nullRepeatMeansSingleDay() {
        List<DoseInstance> expectedResult = new ArrayList<>();
        expectedResult.add(new DoseInstance(singleDayDose, 5));
        assert ScheduledDoseUnpacker.unpack(singleDayDose, 1, 11).equals(expectedResult);
    }

    private static void printDoseList(List<DoseInstance> doseList, String identifier) {
        System.out.println(identifier + " - " + doseList.size() + " entries:");
        for (DoseInstance dose: doseList) {
            System.out.println(dose);
        }
    }

    @Test
    public void nullEndDayMeansIndefiniteRepeat() {
        List<DoseInstance> expectedResult = new ArrayList<>();
        expectedResult.add(new DoseInstance(nullEndDayDose, 3));
        expectedResult.add(new DoseInstance(nullEndDayDose, 5));
        expectedResult.add(new DoseInstance(nullEndDayDose, 7));
        expectedResult.add(new DoseInstance(nullEndDayDose, 9));
        expectedResult.add(new DoseInstance(nullEndDayDose, 11));
        assert ScheduledDoseUnpacker.unpack(nullEndDayDose, 1, 12).equals(expectedResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroRepeatUnitIsInvalid() {
        singleDayDose = new ScheduledDose();
        singleDayDose.setStartDay(5);
        singleDayDose.setEndDay(null);
        singleDayDose.setRepeatInterval(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeStartDateIsInvalid() {
        ScheduledDoseUnpacker.unpack(simpleDailyDose, -1, 3);
    }

    @Test()
    public void zeroStartDateIsValid() {
        ScheduledDoseUnpacker.unpack(simpleDailyDose, 0, 3);
    }

    @Test()
    public void endDateAfterDoseEndIsValid() {
        ScheduledDoseUnpacker.unpack(simpleDailyDose, 3, 35);
    }

    @Test(expected = IllegalArgumentException.class)
    public void endDateBeforeStartDateIsInvalid() {
        ScheduledDoseUnpacker.unpack(simpleDailyDose, 3, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void endDateOnStartDateIsInvalid() {
        ScheduledDoseUnpacker.unpack(simpleDailyDose, 3, 3);
    }

    @Test
    public void simpleRangeTest() {
        Integer[] expected_result = {4, 5, 6, 7};
        ArrayList<Integer> results = new ArrayList<>();
        ScheduledDoseUnpacker.range(4, 8, 1).forEach(results::add);
        assert(results.equals(Arrays.asList(expected_result)));
    }
}
