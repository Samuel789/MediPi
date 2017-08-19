package org.medipi.concentrator.logic;

import org.medipi.medication.Schedule;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PatientAdherenceCalculator {

    private Integer numDosesTakenCorrectly;
    private Integer numDosesMissed;
    private Integer numDosesTakenIncorrectly;
    private Integer numDosesToTake;
    private Double adherenceFraction;
    private Integer streakLength;
    private LocalDate queryStartDate;
    private Collection<Schedule> schedules;
    private LocalDate queryEndDate;
    private boolean calculatingStreak;

    public HashMap<Schedule, ScheduleAdherenceCalculator> getScheduleAdherenceCalculators() {
        return scheduleAdherenceCalculators;
    }

    private HashMap<Schedule, ScheduleAdherenceCalculator> scheduleAdherenceCalculators;

    public PatientAdherenceCalculator(Collection<Schedule> schedules, LocalDate startDate, LocalDate endDate, boolean calculatingStreak) {
        this.schedules = schedules;
        this.queryStartDate = startDate;
        this.queryEndDate = endDate;
        this.calculatingStreak = calculatingStreak;
        resetResults();
    }

    public Integer getNumDosesTakenCorrectly() {
        return numDosesTakenCorrectly;
    }

    public Integer getNumDosesMissed() {
        return numDosesMissed;
    }

    public Integer getNumDosesTakenIncorrectly() {
        return numDosesTakenIncorrectly;
    }

    public Integer getNumDosesToTake() {
        return numDosesToTake;
    }

    public Double getAdherenceFraction() {
        return adherenceFraction;
    }

    public Integer getStreakLength() {
        return streakLength;
    }

    public LocalDate getQueryStartDate() {
        return queryStartDate;
    }

    public void setQueryStartDate(LocalDate queryStartDate) {
        this.queryStartDate = queryStartDate;
        resetResults();
    }

    public Collection<Schedule> getSchedules() {
        return schedules;
    }

    public LocalDate getQueryEndDate() {
        return queryEndDate;
    }

    public void setQueryEndDate(LocalDate queryEndDate) {
        this.queryEndDate = queryEndDate;
        resetResults();
    }

    public boolean isCalculatingStreak() {
        return calculatingStreak;
    }

    public void setCalculatingStreak(boolean calculatingStreak) {
        this.calculatingStreak = calculatingStreak;
        resetResults();
    }

    private void resetResults() {
        numDosesTakenCorrectly = null;
        numDosesMissed = null;
        numDosesTakenIncorrectly = null;
        streakLength = null;
        adherenceFraction = null;
        numDosesToTake = null;
        scheduleAdherenceCalculators = new HashMap<>();
    }


    public void calculatePatientAdherence() {
        int numDosesTakenCorrectly = 0;
        int numDosesMissed = 0;
        int numDosesTakenIncorrectly = 0;
        int numDosesToTake = 0;
        int streakLength = 0;
        double adherenceFraction;
        HashMap<Schedule, ScheduleAdherenceCalculator> scheduleAdherenceCalculators = new HashMap<>();
        LocalDate earliestStart = LocalDate.now();
        HashSet<LocalDate> errantDays = new HashSet<>();
        HashMap<Schedule, Integer> scheduleStreakLength = new HashMap<>();
        HashMap<Schedule, Double> scheduleFraction = new HashMap<>();
        for (Schedule schedule : schedules) {
            LocalDate scheduleStart = schedule.getAssignedStartDate().toLocalDate();
            if (scheduleStart.isBefore(earliestStart)) earliestStart = scheduleStart;
            ScheduleAdherenceCalculator scheduleAdherenceCalculator = new ScheduleAdherenceCalculator(schedule, queryStartDate, queryEndDate, calculatingStreak);
            scheduleAdherenceCalculator.calculateScheduleAdherence();
            scheduleAdherenceCalculators.put(schedule, scheduleAdherenceCalculator);
            numDosesTakenCorrectly += scheduleAdherenceCalculator.getNumDosesTakenCorrectly();
            numDosesMissed += scheduleAdherenceCalculator.getNumDosesMissed();
            numDosesTakenIncorrectly += scheduleAdherenceCalculator.getNumDosesTakenIncorrectly();
            numDosesToTake += scheduleAdherenceCalculator.getNumDosesToTake();
            if (calculatingStreak) {
                LocalDate scheduleLastErrantDate = scheduleAdherenceCalculator.getLastErrantDate();
                if (scheduleLastErrantDate != null) {
                    errantDays.add(scheduleLastErrantDate);
                }
                scheduleStreakLength.put(schedule, scheduleAdherenceCalculator.getStreakLength());
            }
            scheduleFraction.put(schedule, scheduleAdherenceCalculator.getAdherenceFraction());
        }
        if (calculatingStreak) {
            streakLength = calculateStreakLength(errantDays, earliestStart);
        }
        adherenceFraction = calculateAdherenceFraction(numDosesTakenCorrectly, numDosesMissed, numDosesTakenIncorrectly);
        this.numDosesTakenCorrectly = numDosesTakenCorrectly;
        this.numDosesMissed = numDosesMissed;
        this.numDosesTakenIncorrectly = numDosesTakenIncorrectly;
        this.numDosesToTake = numDosesToTake;
        this.scheduleAdherenceCalculators = scheduleAdherenceCalculators;
        this.adherenceFraction = adherenceFraction;
        this.streakLength = streakLength;
    }

    private double calculateAdherenceFraction(int dosesCorrectlyTaken, int dosesNotTaken, int dosesTakenIncorrectly) {
        if (dosesCorrectlyTaken + dosesNotTaken + dosesTakenIncorrectly == 0) {
            return 1;
        }
        return dosesCorrectlyTaken / (dosesCorrectlyTaken + dosesNotTaken);
    }

    private int calculateStreakLength(Collection<LocalDate> errantDays, LocalDate earliestScheduleStart) {
        int streakLength;
        if (errantDays.size() == 0) {
            streakLength = (int) earliestScheduleStart.until(LocalDate.now(), ChronoUnit.DAYS);
        } else {
            LocalDate maxDate = errantDays.stream().max(LocalDate::compareTo).get();
            streakLength = (int) maxDate.until(LocalDate.now(), ChronoUnit.DAYS);
        }
        return streakLength;
    }


}

