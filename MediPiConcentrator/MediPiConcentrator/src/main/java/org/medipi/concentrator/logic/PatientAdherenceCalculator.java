package org.medipi.concentrator.logic;

import org.medipi.medication.model.Schedule;

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
    private LocalTime penultimateDayEndTime = null;

    public PatientAdherenceCalculator(Collection<Schedule> schedules, LocalDate startDate, LocalDate endDate, boolean calculatingStreak) {
        this.schedules = schedules;
        this.queryStartDate = startDate;
        this.queryEndDate = endDate;
        this.calculatingStreak = calculatingStreak;
        resetResults();
    }

    private static Double calculateAdherenceFraction(int numDosesToTake, int numDosesTakenCorrectly, int numDosesMissed, int numDosesTakenIncorrectly) {
        if (numDosesToTake + numDosesTakenIncorrectly != 0) {
            if (numDosesToTake == 0) {
                return 0.;
            } else {
                return Math.max(numDosesTakenCorrectly - numDosesTakenIncorrectly, 0) / ((double) numDosesToTake);
            }
        } else {
            return null;
        }
    }

    public HashMap<Schedule, ScheduleAdherenceCalculatorInterface> getScheduleAdherenceCalculators() {
        return scheduleAdherenceCalculators;
    }

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
            LocalDate scheduleStart = schedule.getStartDate().toLocalDate();
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
        adherenceFraction = calculateAdherenceFraction(numDosesToTake, numDosesTakenCorrectly, numDosesMissed, numDosesTakenIncorrectly);
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

