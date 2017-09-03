package org.medipi.concentrator.logic;

import org.junit.Before;
import org.junit.Test;
import org.medipi.medication.model.DoseInstance;
import org.medipi.medication.model.Schedule;
import org.medipi.medication.model.ScheduledDose;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestScheduledDoseUnpacker {

    Schedule unboundSchedule = new Schedule();
    Schedule boundSchedule = new Schedule();
    ScheduledDose simpleDailyDose;
    ScheduledDose threeDayIntervalDose;
    ScheduledDose intervalTwoDose;
    ScheduledDose singleDayDose;
    ScheduledDose nullEndDayDose;

    @Before
    public void setUpUnboundSchedule() {
        unboundSchedule.setAssignedStartDate(Date.valueOf(LocalDate.of(2016, 2, 22)));
    }

    @Before
    public void setUpBoundSchedule() {
        boundSchedule.setAssignedStartDate(Date.valueOf(LocalDate.of(2016, 2, 22)));
        boundSchedule.setAssignedEndDate(Date.valueOf(LocalDate.of(2016, 2, 26)));
    }

    @Before
    public void createSimpleDailyDose() {
        Time startTime = Time.valueOf("08:00:00");
        Time endTime = Time.valueOf("14:00:00");
        simpleDailyDose = new ScheduledDose();
        simpleDailyDose.setSchedule(unboundSchedule);
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
        threeDayIntervalDose.setSchedule(unboundSchedule);
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
        singleDayDose.setSchedule(unboundSchedule);
        singleDayDose.setStartDay(5);
        singleDayDose.setEndDay(null);
        singleDayDose.setRepeatInterval(null);
        singleDayDose.setWindowStartTime(startTime);
        singleDayDose.setWindowEndTime(endTime);
    }

    @Before
    public void createIntervalTwoDose() {
        Time startTime = Time.valueOf("08:00:00");
        Time endTime = Time.valueOf("14:00:00");
        intervalTwoDose = new ScheduledDose();
        intervalTwoDose.setSchedule(unboundSchedule);
        intervalTwoDose.setStartDay(2);
        intervalTwoDose.setEndDay(null);
        intervalTwoDose.setRepeatInterval(2);
        intervalTwoDose.setWindowStartTime(startTime);
        intervalTwoDose.setWindowEndTime(endTime);
    }


    @Before
    public void createNullEndDayDose() {
        Time startTime = Time.valueOf("08:00:00");
        Time endTime = Time.valueOf("14:00:00");
        nullEndDayDose = new ScheduledDose();
        nullEndDayDose.setSchedule(unboundSchedule);
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
    public void testWindowBoundsResult() {
        List<DoseInstance> expectedResult = new ArrayList<>();
        expectedResult.add(new DoseInstance(simpleDailyDose, 2));
        expectedResult.add(new DoseInstance(simpleDailyDose, 3));
        expectedResult.add(new DoseInstance(simpleDailyDose, 4));
        assert ScheduledDoseUnpacker.unpack(simpleDailyDose, 0, 5).equals(expectedResult);
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
    public void repeatNullGivesSingleDoseInstance() {
        assert ScheduledDoseUnpacker.unpack(singleDayDose, 2, 7).equals(Collections.singletonList(new DoseInstance(singleDayDose, 5)));
    }

    @Test
    public void repeatNullOutOfBoundsGivesZeroDoses() {
        assert ScheduledDoseUnpacker.unpack(singleDayDose, 6, 10).equals(Collections.emptyList());
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
        singleDayDose.setSchedule(unboundSchedule);
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

    @Test
    public void endDateOnStartDateGivesNoDoses() {
        assert ScheduledDoseUnpacker.unpack(simpleDailyDose, 3, 3).size() == 0;
    }

    @Test
    public void simpleRange_rangeMethod() {
        Integer[] expected_result = {4, 5, 6, 7};
        ArrayList<Integer> results = new ArrayList<>();
        ScheduledDoseUnpacker.range(4, 8, 1).forEach(results::add);
        assert(results.equals(Arrays.asList(expected_result)));
    }

    @Test
    public void scheduleEndDateBoundsWindow() {
        List<DoseInstance> expectedResult = new ArrayList<>();
        ScheduledDose boundDose = new ScheduledDose();
        boundDose.setStartDay(2);
        boundDose.setEndDay(null);
        boundDose.setSchedule(boundSchedule);
        boundDose.setRepeatInterval(1);
        boundDose.setWindowStartTime(Time.valueOf("14:00:00"));
        boundDose.setWindowStartTime(Time.valueOf("16:00:00"));
        expectedResult.add(new DoseInstance(boundDose, 2));
        expectedResult.add(new DoseInstance(boundDose, 3));
        assert ScheduledDoseUnpacker.unpack(boundDose, 1, 12).equals(expectedResult);
    }

    @Test
    public void windowConstrainsDoseWithIntervalGTOne() {
        List<DoseInstance> expectedResult = new ArrayList<>();
        expectedResult.add(new DoseInstance(intervalTwoDose, 4));
        expectedResult.add(new DoseInstance(intervalTwoDose, 6));
        expectedResult.add(new DoseInstance(intervalTwoDose, 8));
        assert ScheduledDoseUnpacker.unpack(intervalTwoDose, 3, 10).equals(expectedResult);
    }

    @Test
    public void unnamedTest() {
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(0);
        dose.setEndDay(null);
        Schedule schedule = new Schedule();
        schedule.setAssignedStartDate(Date.valueOf("2017-08-20"));
        schedule.setAssignedEndDate(Date.valueOf("2017-08-30"));
        dose.setRepeatInterval(1);
        dose.setSchedule(schedule);
        List<DoseInstance> expectedResult = new ArrayList<>();
        expectedResult.add(new DoseInstance(dose, 0));
        expectedResult.add(new DoseInstance(dose, 1));
        expectedResult.add(new DoseInstance(dose, 2));
        expectedResult.add(new DoseInstance(dose, 3));
        expectedResult.add(new DoseInstance(dose, 4));
        assert ScheduledDoseUnpacker.unpack(dose, 0, 5).equals(expectedResult);
    }
}
