package org.medipi.concentrator.logic;

import org.junit.Before;
import org.junit.Test;
import org.medipi.medication.model.RecordedDose;
import org.medipi.medication.model.Schedule;
import org.medipi.medication.model.ScheduledDose;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sam on 23/08/17.
 */
public class TestPatientAdherenceCalculator {
    private Schedule schedule;

    @Before
    public void runBeforeTestMethod() {
        schedule = new Schedule();
        schedule.setStartDate(Date.valueOf(LocalDate.of(2017, 7, 1)));
    }

    @Test
    public void simpleNoAdherenceTet() {
    }

    @Test
    public void simplePerfectAdherenceTest() {
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
        Set<RecordedDose> takenDoses = new HashSet<>();
        for (int i: new int [] {2, 4, 6, 8, 10}) {
            RecordedDose takenDose = new RecordedDose();
            takenDose.setDayTaken(i);
            takenDose.setDoseValue(2);
            takenDose.setTimeTaken(Time.valueOf("14:30:00"));
            takenDoses.add(takenDose);
        }
        dose.getSchedule().setRecordedDoses(takenDoses);
        PatientAdherenceCalculator patientAdherenceCalculator = new PatientAdherenceCalculator(Collections.singletonList(schedule), LocalDate.of(2017, 7, 2), LocalDate.of(2017, 7, 12), true);
        patientAdherenceCalculator.calculatePatientAdherence();
        assert patientAdherenceCalculator.getNumDosesMissed() == 0;
        assert patientAdherenceCalculator.getNumDosesToTake() == 5;
        assert patientAdherenceCalculator.getAdherenceFraction() == 1;
        assert patientAdherenceCalculator.getStreakLength() == 11;

        assert patientAdherenceCalculator.getNumDosesTakenIncorrectly() == 0;
        assert patientAdherenceCalculator.getNumDosesTakenCorrectly() == 5;
        }
        @Test
        public void imperfectAdherenceTest() {
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
            Set<RecordedDose> takenDoses = new HashSet<>();
            for (int i: new int [] {2, 6, 10}) {
                RecordedDose takenDose = new RecordedDose();
                takenDose.setDayTaken(i);
                takenDose.setDoseValue(2);
                takenDose.setTimeTaken(Time.valueOf("14:30:00"));
                takenDoses.add(takenDose);
            }
            dose.getSchedule().setRecordedDoses(takenDoses);
            PatientAdherenceCalculator patientAdherenceCalculator = new PatientAdherenceCalculator(Collections.singletonList(schedule), LocalDate.of(2017, 7, 2), LocalDate.of(2017, 7, 12), true);
            patientAdherenceCalculator.calculatePatientAdherence();
            assert patientAdherenceCalculator.getNumDosesMissed() == 2;
            assert patientAdherenceCalculator.getNumDosesToTake() == 5;
            assert patientAdherenceCalculator.getAdherenceFraction() == 3./5;
            assert patientAdherenceCalculator.getStreakLength() == 2;

            assert patientAdherenceCalculator.getNumDosesTakenIncorrectly() == 0;
            assert patientAdherenceCalculator.getNumDosesTakenCorrectly() == 3;
    }

    @Test
    public void noSchedulesGivesNullTest() {
        PatientAdherenceCalculator patientAdherenceCalculator = new PatientAdherenceCalculator(Collections.emptyList(), LocalDate.of(2017, 7, 2), LocalDate.of(2017, 7, 12), true);
        patientAdherenceCalculator.calculatePatientAdherence();
        assert patientAdherenceCalculator.getNumDosesMissed() == 0;
        assert patientAdherenceCalculator.getNumDosesToTake() == 0;
        assert patientAdherenceCalculator.getAdherenceFraction() == null;
        assert patientAdherenceCalculator.getStreakLength() == null;

        assert patientAdherenceCalculator.getNumDosesTakenIncorrectly() == 0;
        assert patientAdherenceCalculator.getNumDosesTakenCorrectly() == 0;
    }


}