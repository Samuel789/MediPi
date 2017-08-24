package org.medipi.concentrator.logic;

import org.junit.*;
import org.medipi.medication.RecordedDose;
import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

public class TestScheduleAdherenceCalculator {

    Schedule schedule;

    @Before
    public void runBeforeTestMethod() {
        schedule = new Schedule();
        schedule.setAssignedStartDate(Date.valueOf(LocalDate.of(2017, 7, 1)));
    }

    @Test
    public void zeroAdherenceSchedule() {
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(2);
        dose.setEndDay(7);
        dose.setWindowStartTime(Time.valueOf("14:00:00"));
        dose.setWindowEndTime(Time.valueOf("17:00:00"));
        dose.setReminderTime(Time.valueOf("16:30:00"));
        dose.setSchedule(schedule);
        dose.setRepeatInterval(2);
        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, 0, 10, false);
        scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();
        assert scheduleAdherenceCalculatorInterface.getNumDosesMissed() == 3;
        assert scheduleAdherenceCalculatorInterface.getAdherenceFraction() == 0;
        assert scheduleAdherenceCalculatorInterface.getStreakLength() == null;
        assert scheduleAdherenceCalculatorInterface.getNumDosesToTake() == 3;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenIncorrectly() == 0;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenCorrectly() == 0;
    }

    @Test
    public void perfectAdherenceSchedule() {
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(2);
        dose.setEndDay(null);
        dose.setWindowStartTime(Time.valueOf("14:00:00"));
        dose.setWindowEndTime(Time.valueOf("17:00:00"));
        dose.setReminderTime(Time.valueOf("16:30:00"));
        dose.setDoseValue(2);
        dose.setSchedule(schedule);
        dose.setRepeatInterval(2);

        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculator scheduleAdherenceCalculator = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        Set<RecordedDose> takenDoses = new HashSet<>();
        for (int i: new int [] {4, 6, 8}) {
            RecordedDose takenDose = new RecordedDose();
            takenDose.setDayTaken(i);
            takenDose.setDoseValue(2);
            takenDose.setTimeTaken(Time.valueOf("14:30:00"));
            takenDoses.add(takenDose);
        }
        dose.getSchedule().setRecordedDoses(takenDoses);
        scheduleAdherenceCalculator.calculateScheduleAdherence();

        assert scheduleAdherenceCalculator.getNumDosesMissed() == 0;
        assert scheduleAdherenceCalculator.getAdherenceFraction() == 1;
        assert scheduleAdherenceCalculator.getStreakLength() == null;
        assert scheduleAdherenceCalculator.getNumDosesToTake() == 3;
        assert scheduleAdherenceCalculator.getNumDosesTakenIncorrectly() == 0;
        assert scheduleAdherenceCalculator.getNumDosesTakenCorrectly() == 3;
    }

    @Test
    public void fractionalAdherenceSchedule() {
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(2);
        dose.setEndDay(null);
        dose.setWindowStartTime(Time.valueOf("14:00:00"));
        dose.setWindowEndTime(Time.valueOf("17:00:00"));
        dose.setReminderTime(Time.valueOf("16:30:00"));
        dose.setDoseValue(2);
        dose.setSchedule(schedule);
        dose.setRepeatInterval(2);

        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculator scheduleAdherenceCalculator = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        Set<RecordedDose> takenDoses = new HashSet<>();
        for (int i: new int [] {4, 8}) {
            RecordedDose takenDose = new RecordedDose();
            takenDose.setDayTaken(i);
            takenDose.setDoseValue(2);
            takenDose.setTimeTaken(Time.valueOf("14:30:00"));
            takenDoses.add(takenDose);
        }
        dose.getSchedule().setRecordedDoses(takenDoses);
        scheduleAdherenceCalculator.calculateScheduleAdherence();

        assert scheduleAdherenceCalculator.getNumDosesMissed() == 1;
        assert scheduleAdherenceCalculator.getAdherenceFraction().equals(2/3.);
        assert scheduleAdherenceCalculator.getStreakLength() == null;
        assert scheduleAdherenceCalculator.getNumDosesToTake() == 3;
        assert scheduleAdherenceCalculator.getNumDosesTakenIncorrectly() == 0;
        assert scheduleAdherenceCalculator.getNumDosesTakenCorrectly() == 2;
    }

    @Test
    public void noScheduledDoses_nullAdherence() {
        schedule.setScheduledDoses(new HashSet<>());
        schedule.setRecordedDoses(new HashSet<>());
        ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();
        assert scheduleAdherenceCalculatorInterface.getAdherenceFraction() == null;
    }

    @Test
    public void noScheduledDosesInRange_nullAdherence() {
        schedule.setScheduledDoses(new HashSet<>());
        schedule.setRecordedDoses(new HashSet<>());
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(0);
        dose.setSchedule(schedule);
        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();
        assert scheduleAdherenceCalculatorInterface.getAdherenceFraction() == null;
    }
    @Test
    public void noScheduledDosesInRangeWithRecordedDosesOutOfRange() {
        schedule.setScheduledDoses(new HashSet<>());
        schedule.setRecordedDoses(new HashSet<>());
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(0);
        dose.setSchedule(schedule);
        RecordedDose rDose = new RecordedDose();
        rDose.setDayTaken(2);
        rDose.setTimeTaken(Time.valueOf("14:00:00"));
        rDose.setSchedule(schedule);
        schedule.setRecordedDoses(Collections.singleton(rDose));
        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();
        assert scheduleAdherenceCalculatorInterface.getAdherenceFraction() == null;
    }

    @Test
    public void untakenDosesInRangeWithRecordedDosesOutOfRange() {
        schedule.setScheduledDoses(new HashSet<>());
        schedule.setRecordedDoses(new HashSet<>());
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(5);
        dose.setSchedule(schedule);
        RecordedDose rDose = new RecordedDose();
        rDose.setDayTaken(2);
        rDose.setTimeTaken(Time.valueOf("14:00:00"));
        rDose.setSchedule(schedule);
        schedule.setRecordedDoses(Collections.singleton(rDose));
        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();
        assert scheduleAdherenceCalculatorInterface.getAdherenceFraction() == 0;
    }

    @Test
    public void incorrectlyTakenDosesCounted() {
        schedule.setScheduledDoses(new HashSet<>());
        schedule.setRecordedDoses(new HashSet<>());
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(5);
        dose.setSchedule(schedule);
        dose.setWindowStartTime(Time.valueOf("13:00:00"));
        dose.setWindowEndTime(Time.valueOf("15:00:00"));
        dose.setDoseValue(3);
        RecordedDose rDose = new RecordedDose();
        rDose.setDayTaken(5);
        rDose.setDoseValue(2);
        rDose.setTimeTaken(Time.valueOf("14:00:00"));
        rDose.setSchedule(schedule);
        schedule.setRecordedDoses(Collections.singleton(rDose));
        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();
        assert scheduleAdherenceCalculatorInterface.getAdherenceFraction() == 0;
        assert scheduleAdherenceCalculatorInterface.getNumDosesMissed() == 1;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenIncorrectly() == 1;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenCorrectly() == 0;
    }

    @Test
    public void zeroRange_null() {
        schedule.setScheduledDoses(new HashSet<>());
        schedule.setRecordedDoses(new HashSet<>());
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(0);
        dose.setSchedule(schedule);
        dose.setWindowStartTime(Time.valueOf("13:00:00"));
        dose.setWindowEndTime(Time.valueOf("15:00:00"));
        dose.setDoseValue(3);
        dose.setRepeatInterval(1);
        dose.setEndDay(null);
        RecordedDose rDose = new RecordedDose();
        rDose.setDayTaken(5);
        rDose.setDoseValue(2);
        rDose.setTimeTaken(Time.valueOf("14:00:00"));
        rDose.setSchedule(schedule);
        schedule.setRecordedDoses(Collections.singleton(rDose));
        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, 3, 3, false);
        scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();
        assert scheduleAdherenceCalculatorInterface.getAdherenceFraction() == null;
        assert scheduleAdherenceCalculatorInterface.getNumDosesMissed() == 0;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenIncorrectly() == 0;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenCorrectly() == 0;
        assert scheduleAdherenceCalculatorInterface.getNumDosesToTake() == 0;
    }

    @Test
    public void streak_calculatedCorrectly() {
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(2);
        dose.setEndDay(null);
        dose.setWindowStartTime(Time.valueOf("14:00:00"));
        dose.setWindowEndTime(Time.valueOf("17:00:00"));
        dose.setReminderTime(Time.valueOf("16:30:00"));
        dose.setDoseValue(2);
        dose.setSchedule(schedule);
        dose.setRepeatInterval(2);

        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, 0, 6, true);
        Set<RecordedDose> takenDoses = new HashSet<>();
        for (int i: new int [] {2, 4}) {
            RecordedDose takenDose = new RecordedDose();
            takenDose.setDayTaken(i);
            takenDose.setDoseValue(2);
            takenDose.setTimeTaken(Time.valueOf("14:30:00"));
            takenDoses.add(takenDose);
        }
        dose.getSchedule().setRecordedDoses(takenDoses);
        scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();

        assert scheduleAdherenceCalculatorInterface.getNumDosesMissed() == 0;
        assert scheduleAdherenceCalculatorInterface.getAdherenceFraction() == 1;
        assert scheduleAdherenceCalculatorInterface.getStreakLength() == 6;
        assert scheduleAdherenceCalculatorInterface.getNumDosesToTake() == 2;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenIncorrectly() == 0;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenCorrectly() == 2;
    }

    @Test
    public void streakWithBoundedWindow_boundingIgnored() {
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(2);
        dose.setEndDay(null);
        dose.setWindowStartTime(Time.valueOf("14:00:00"));
        dose.setWindowEndTime(Time.valueOf("17:00:00"));
        dose.setReminderTime(Time.valueOf("16:30:00"));
        dose.setDoseValue(2);
        dose.setSchedule(schedule);
        dose.setRepeatInterval(2);

        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, 2, 6, true);
        Set<RecordedDose> takenDoses = new HashSet<>();
        for (int i: new int [] {2, 4}) {
            RecordedDose takenDose = new RecordedDose();
            takenDose.setDayTaken(i);
            takenDose.setDoseValue(2);
            takenDose.setTimeTaken(Time.valueOf("14:30:00"));
            takenDoses.add(takenDose);
        }
        dose.getSchedule().setRecordedDoses(takenDoses);
        scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();

        assert scheduleAdherenceCalculatorInterface.getNumDosesMissed() == 0;
        assert scheduleAdherenceCalculatorInterface.getAdherenceFraction() == 1;
        assert scheduleAdherenceCalculatorInterface.getStreakLength() == 6;
        assert scheduleAdherenceCalculatorInterface.getNumDosesToTake() == 2;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenIncorrectly() == 0;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenCorrectly() == 2;
    }

    @Test
    public void imperfectAdherence_streakCalculatedCorrectly() {
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(2);
        dose.setEndDay(null);
        dose.setWindowStartTime(Time.valueOf("14:00:00"));
        dose.setWindowEndTime(Time.valueOf("17:00:00"));
        dose.setReminderTime(Time.valueOf("16:30:00"));
        dose.setDoseValue(2);
        dose.setSchedule(schedule);
        dose.setRepeatInterval(2);

        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, 2, 11, true);
        Set<RecordedDose> takenDoses = new HashSet<>();
        for (int i: new int [] {2, 6, 10}) {
            RecordedDose takenDose = new RecordedDose();
            takenDose.setDayTaken(i);
            takenDose.setDoseValue(2);
            takenDose.setTimeTaken(Time.valueOf("14:30:00"));
            takenDoses.add(takenDose);
        }
        dose.getSchedule().setRecordedDoses(takenDoses);
        scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();

        assert scheduleAdherenceCalculatorInterface.getNumDosesMissed() == 2;
        assert scheduleAdherenceCalculatorInterface.getAdherenceFraction() == 3./5;
        assert scheduleAdherenceCalculatorInterface.getStreakLength() == 2;
        assert scheduleAdherenceCalculatorInterface.getNumDosesToTake() == 5;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenIncorrectly() == 0;
        assert scheduleAdherenceCalculatorInterface.getNumDosesTakenCorrectly() == 3;
    }

    @Test
    public void lastErrantDayCorrectlyConvertedToDate() {
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(2);
        dose.setEndDay(null);
        dose.setWindowStartTime(Time.valueOf("14:00:00"));
        dose.setWindowEndTime(Time.valueOf("17:00:00"));
        dose.setReminderTime(Time.valueOf("16:30:00"));
        dose.setDoseValue(2);
        dose.setSchedule(schedule);
        dose.setRepeatInterval(2);

        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, 2, 11, true);
        Set<RecordedDose> takenDoses = new HashSet<>();
        for (int i: new int [] {2, 6, 10}) {
            RecordedDose takenDose = new RecordedDose();
            takenDose.setDayTaken(i);
            takenDose.setDoseValue(2);
            takenDose.setTimeTaken(Time.valueOf("14:30:00"));
            takenDoses.add(takenDose);
        }
        dose.getSchedule().setRecordedDoses(takenDoses);
        scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();
        assert scheduleAdherenceCalculatorInterface.getLastErrantDay() == 8;
        assert scheduleAdherenceCalculatorInterface.getLastErrantDate().equals(LocalDate.of(2017, 7, 9));
    }

    @Test
    public void constructFromDates() {
        ScheduleAdherenceCalculatorInterface calculator = new ScheduleAdherenceCalculator(schedule, LocalDate.of(2017, 7, 5), LocalDate.of(2017, 7, 11), true);
        assert calculator.getQueryStartDay() == 4;
        assert calculator.getQueryEndDay() == 10;
        assert calculator.isCalculatingStreak() == true;
    }

    @Test
    public void constructFromDatesOutOfBounds() {
        ScheduleAdherenceCalculatorInterface calculator = new ScheduleAdherenceCalculator(schedule, LocalDate.of(2017, 6, 20), LocalDate.of(2017, 7, 11), false);
        assert calculator.getQueryStartDay() == 0;
        assert calculator.getQueryEndDay() == 10;
        assert calculator.isCalculatingStreak() == false;
    }
    @Test
    public void constructFromDatesZeroRange() {
        ScheduleAdherenceCalculatorInterface calculator = new ScheduleAdherenceCalculator(schedule, LocalDate.of(2017, 7, 20), LocalDate.of(2017, 7, 20), true);
        assert calculator.getQueryStartDay() == 19;
        assert calculator.getQueryEndDay() == 19;
        assert calculator.isCalculatingStreak() == true;
    }

    @Test
    public void constructFromDatesBeforeSchedule() {
        ScheduleAdherenceCalculatorInterface calculator = new ScheduleAdherenceCalculator(schedule, LocalDate.of(2017, 6, 10), LocalDate.of(2017, 6, 20), true);
        assert calculator.getQueryStartDay() == 0;
        assert calculator.getQueryEndDay() == 0;
        assert calculator.isCalculatingStreak() == true;
    }

    @Test(expected = IllegalArgumentException.class)
    public void queryEndBeforeQueryStartThrowsException() {
        ScheduleAdherenceCalculatorInterface calculator = new ScheduleAdherenceCalculator(schedule, LocalDate.of(2017, 6, 20), LocalDate.of(2017, 6, 10), true);
    }
}
