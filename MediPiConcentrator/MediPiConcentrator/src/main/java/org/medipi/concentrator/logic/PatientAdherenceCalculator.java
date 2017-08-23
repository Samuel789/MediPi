package org.medipi.concentrator.logic;

import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduleAdherence;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

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
    private HashMap<Schedule, ScheduleAdherenceCalculatorInterface> scheduleAdherenceCalculators;

    public PatientAdherenceCalculator(Collection<Schedule> schedules, LocalDate startDate, LocalDate endDate, boolean calculatingStreak) {
        this.schedules = schedules;
        this.queryStartDate = startDate;
        this.queryEndDate = endDate;
        this.calculatingStreak = calculatingStreak;
        resetResults();
    }

    public HashMap<Schedule, ScheduleAdherenceCalculatorInterface> getScheduleAdherenceCalculators() {
        return scheduleAdherenceCalculators;
    }

    private LocalTime penultimateDayEndTime = null;

    public void setPenultimateDayEndTime(LocalTime time) {
        penultimateDayEndTime = time;
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
        Integer streakLength = null;
        Double adherenceFraction;
        HashMap<Schedule, ScheduleAdherenceCalculatorInterface> scheduleAdherenceCalculators = new HashMap<>();
        LocalDate earliestStart = LocalDate.now();
        HashSet<LocalDate> errantDays = new HashSet<>();
        for (Schedule schedule : schedules) {
            LocalDate scheduleStart = schedule.getAssignedStartDate().toLocalDate();
            if (scheduleStart.isBefore(earliestStart)) earliestStart = scheduleStart;
            ScheduleAdherenceCalculatorInterface scheduleAdherenceCalculatorInterface = new ScheduleAdherenceCalculator(schedule, queryStartDate, queryEndDate, calculatingStreak);
            scheduleAdherenceCalculatorInterface.setQueryEndDateTime(queryEndDate, penultimateDayEndTime);
            scheduleAdherenceCalculatorInterface.calculateScheduleAdherence();
            scheduleAdherenceCalculators.put(schedule, scheduleAdherenceCalculatorInterface);
            numDosesTakenCorrectly += scheduleAdherenceCalculatorInterface.getNumDosesTakenCorrectly();
            numDosesMissed += scheduleAdherenceCalculatorInterface.getNumDosesMissed();
            numDosesTakenIncorrectly += scheduleAdherenceCalculatorInterface.getNumDosesTakenIncorrectly();
            numDosesToTake += scheduleAdherenceCalculatorInterface.getNumDosesToTake();
            if (calculatingStreak) {
                LocalDate scheduleLastErrantDate = scheduleAdherenceCalculatorInterface.getLastErrantDate();
                if (scheduleLastErrantDate != null) {
                    errantDays.add(scheduleLastErrantDate);
                }
            }
        }
        adherenceFraction = calculateAdherenceFraction(numDosesTakenCorrectly, numDosesMissed, numDosesTakenIncorrectly);
        if (calculatingStreak && adherenceFraction != null) {
            streakLength = calculateStreakLength(errantDays, earliestStart);
        }
        this.numDosesTakenCorrectly = numDosesTakenCorrectly;
        this.numDosesMissed = numDosesMissed;
        this.numDosesTakenIncorrectly = numDosesTakenIncorrectly;
        this.numDosesToTake = numDosesToTake;
        this.scheduleAdherenceCalculators = scheduleAdherenceCalculators;
        this.adherenceFraction = adherenceFraction;
        this.streakLength = streakLength;
    }

    private Double calculateAdherenceFraction(int dosesCorrectlyTaken, int dosesNotTaken, int dosesTakenIncorrectly) {
        if (dosesCorrectlyTaken + dosesNotTaken + dosesTakenIncorrectly == 0) {
            return null;
        }
        return ((double) dosesCorrectlyTaken) / (dosesCorrectlyTaken + dosesNotTaken);
    }

    private Integer calculateStreakLength(Collection<LocalDate> errantDays, LocalDate earliestScheduleStart) {
        int streakLength;
        if (errantDays.size() == 0) {
            streakLength = (int) earliestScheduleStart.until(queryEndDate, ChronoUnit.DAYS);
        } else {
            LocalDate maxDate = errantDays.stream().max(LocalDate::compareTo).get();
            streakLength = (int) maxDate.until(queryEndDate, ChronoUnit.DAYS) - 1;
        }
        return streakLength;
    }


}

