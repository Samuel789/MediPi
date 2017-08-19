package org.medipi.concentrator.logic;

import org.junit.*;
import org.medipi.medication.RecordedDose;
import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TestScheduleAdherenceCalculator {

    Schedule schedule;

    @Before
    public void runBeforeTestMethod() {
        schedule = new Schedule();
        schedule.setAssignedStartDate(Date.valueOf(LocalDate.of(2017, 7, 1)));
    }

    @Test
    public void simpleNoAdherenceTet() {
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(2);
        dose.setEndDay(7);
        dose.setWindowStartTime(Time.valueOf("14:00:00"));
        dose.setWindowEndTime(Time.valueOf("17:00:00"));
        dose.setReminderTime(Time.valueOf("16:30:00"));
        dose.setSchedule(schedule);
        dose.setRepeatInterval(2);
        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculator scheduleAdherenceCalculator = new ScheduleAdherenceCalculator(schedule, 0, 10, false);
        scheduleAdherenceCalculator.calculateScheduleAdherence();
        assert scheduleAdherenceCalculator.getNumDosesMissed() == 3;
        assert scheduleAdherenceCalculator.getAdherenceFraction() == 0;
        assert scheduleAdherenceCalculator.getStreakLength() == null;
        assert scheduleAdherenceCalculator.getNumDosesToTake() == 3;
        assert scheduleAdherenceCalculator.getNumDosesTakenIncorrectly() == 0;
        assert scheduleAdherenceCalculator.getNumDosesTakenCorrectly() == 0;
    }

    @Test
    public void simplePerfectAdherenceTet() {
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

        System.out.println(scheduleAdherenceCalculator.getSummary());
        assert scheduleAdherenceCalculator.getNumDosesMissed() == 0;
        assert scheduleAdherenceCalculator.getAdherenceFraction() == 1;
        assert scheduleAdherenceCalculator.getStreakLength() == null;
        assert scheduleAdherenceCalculator.getNumDosesToTake() == 3;
        assert scheduleAdherenceCalculator.getNumDosesTakenIncorrectly() == 0;
        assert scheduleAdherenceCalculator.getNumDosesTakenCorrectly() == 3;
    }

    @Test
    public void simpleFractionalAdherenceTet() {
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

        System.out.println(scheduleAdherenceCalculator.getSummary());
        assert scheduleAdherenceCalculator.getNumDosesMissed() == 1;
        assert scheduleAdherenceCalculator.getAdherenceFraction().equals(2/3.);
        assert scheduleAdherenceCalculator.getStreakLength() == null;
        assert scheduleAdherenceCalculator.getNumDosesToTake() == 3;
        assert scheduleAdherenceCalculator.getNumDosesTakenIncorrectly() == 0;
        assert scheduleAdherenceCalculator.getNumDosesTakenCorrectly() == 2;
    }

    @Test
    public void noScheduledDosesGivesNull() {
        schedule.setScheduledDoses(new HashSet<>());
        schedule.setRecordedDoses(new HashSet<>());
        ScheduleAdherenceCalculator scheduleAdherenceCalculator = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        scheduleAdherenceCalculator.calculateScheduleAdherence();
        assert scheduleAdherenceCalculator.getAdherenceFraction() == null;
    }

    @Test
    public void noDosesInRangeGivesNull() {
        schedule.setScheduledDoses(new HashSet<>());
        schedule.setRecordedDoses(new HashSet<>());
        ScheduledDose dose = new ScheduledDose();
        dose.setStartDay(0);
        dose.setSchedule(schedule);
        schedule.setScheduledDoses(Collections.singleton(dose));
        ScheduleAdherenceCalculator scheduleAdherenceCalculator = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        scheduleAdherenceCalculator.calculateScheduleAdherence();
        assert scheduleAdherenceCalculator.getAdherenceFraction() == null;
    }
    @Test
    public void noDoseInRangeGivesNullWithRecordedDoseOutOfRange() {
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
        ScheduleAdherenceCalculator scheduleAdherenceCalculator = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        scheduleAdherenceCalculator.calculateScheduleAdherence();
        assert scheduleAdherenceCalculator.getAdherenceFraction() == null;
    }

    @Test
    public void doseInRangeGivesZeroWithNoRdInRange() {
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
        ScheduleAdherenceCalculator scheduleAdherenceCalculator = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        scheduleAdherenceCalculator.calculateScheduleAdherence();
        assert scheduleAdherenceCalculator.getAdherenceFraction() == 0;
    }

    @Test
    public void incorrectlyTakenDoseIdentified() {
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
        ScheduleAdherenceCalculator scheduleAdherenceCalculator = new ScheduleAdherenceCalculator(schedule, 3, 10, false);
        scheduleAdherenceCalculator.calculateScheduleAdherence();
        System.out.println(scheduleAdherenceCalculator.getSummary());
        assert scheduleAdherenceCalculator.getAdherenceFraction() == 0;
        assert scheduleAdherenceCalculator.getNumDosesMissed() == 1;
        assert scheduleAdherenceCalculator.getNumDosesTakenIncorrectly() == 1;
        assert scheduleAdherenceCalculator.getNumDosesTakenCorrectly() == 0;
    }


    @Test
    public void test_method_2() {
        System.out.println("@Test - test_method_2");
    }
}
