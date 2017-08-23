package org.medipi.concentrator.logic;

import org.medipi.medication.DoseInstance;
import org.medipi.medication.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by sam on 23/08/17.
 */
public class MockScheduleAdherenceCalculator implements ScheduleAdherenceCalculatorInterface {
    @Override
    public Integer getNumDosesTakenCorrectly() {
        return numDosesTakenCorrectly;
    }

    public void setNumDosesTakenCorrectly(Integer numDosesTakenCorrectly) {
        this.numDosesTakenCorrectly = numDosesTakenCorrectly;
    }

    @Override
    public Integer getNumDosesMissed() {
        return numDosesMissed;
    }

    public void setNumDosesMissed(Integer numDosesMissed) {
        this.numDosesMissed = numDosesMissed;
    }

    @Override
    public Integer getNumDosesTakenIncorrectly() {
        return numDosesTakenIncorrectly;
    }

    public void setNumDosesTakenIncorrectly(Integer numDosesTakenIncorrectly) {
        this.numDosesTakenIncorrectly = numDosesTakenIncorrectly;
    }

    @Override
    public List<DoseInstance> getDoseInstances() {
        return null;
    }

    @Override
    public Integer getNumDosesToTake() {
        return numDosesToTake;
    }

    @Override
    public String getSummary() {
        return null;
    }

    public void setNumDosesToTake(Integer numDosesToTake) {
        this.numDosesToTake = numDosesToTake;
    }

    @Override
    public Double getAdherenceFraction() {
        return adherenceFraction;
    }

    public void setAdherenceFraction(Double adherenceFraction) {
        this.adherenceFraction = adherenceFraction;
    }

    @Override
    public Integer getLastErrantDay() {
        return lastErrantDay;
    }

    public void setLastErrantDay(Integer lastErrantDay) {
        this.lastErrantDay = lastErrantDay;
    }

    @Override
    public Integer getStreakLength() {
        return streakLength;
    }

    @Override
    public Schedule getSchedule() {
        return null;
    }

    @Override
    public int getQueryStartDay() {
        return 0;
    }

    @Override
    public void setQueryStartDay(int queryStartDay) {

    }

    @Override
    public int getQueryEndDay() {
        return 0;
    }

    @Override
    public void setQueryEndDayTime(int queryEndDay, LocalTime time) {

    }

    @Override
    public void setQueryEndDateTime(LocalDate queryEndDate, LocalTime endTime) {

    }

    @Override
    public boolean isCalculatingStreak() {
        return false;
    }

    @Override
    public void setCalculatingStreak(boolean calculatingStreak) {

    }

    @Override
    public LocalDate getLastErrantDate() {
        return null;
    }

    @Override
    public void calculateScheduleAdherence() {

    }

    public void setStreakLength(Integer streakLength) {
        this.streakLength = streakLength;
    }

    private Integer numDosesTakenCorrectly;
    private Integer numDosesMissed;
    private Integer numDosesTakenIncorrectly;
    private Integer numDosesToTake;
    private Double adherenceFraction;
    private Integer lastErrantDay;
    private Integer streakLength;

}
