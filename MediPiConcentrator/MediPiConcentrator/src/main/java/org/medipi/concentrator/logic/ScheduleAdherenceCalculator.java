package org.medipi.concentrator.logic;

import org.medipi.medication.DoseInstance;
import org.medipi.medication.RecordedDose;
import org.medipi.medication.Schedule;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.medipi.concentrator.logic.ScheduleUtilities.fromDayOfSchedule;
import static org.medipi.concentrator.logic.ScheduleUtilities.toDayOfSchedule;

public class ScheduleAdherenceCalculator implements ScheduleAdherenceCalculatorInterface {
    private Integer numDosesTakenCorrectly;
    private Integer numDosesMissed;
    private Integer numDosesTakenIncorrectly;
    private Integer numDosesToTake;
    private Double adherenceFraction;
    private Integer lastErrantDay;
    private Integer streakLength;
    private int queryStartDay;
    private Schedule schedule;
    private List<DoseInstance> doseInstances;
    private int queryEndDay;
    private boolean calculatingStreak;
    public ScheduleAdherenceCalculator(Schedule schedule, int queryStartDay, int queryEndDay, boolean calculatingStreak) {
        initialize(schedule, queryStartDay, queryEndDay, calculatingStreak);
    }

    public ScheduleAdherenceCalculator(Schedule schedule, LocalDate queryStartDate, LocalDate queryEndDate, boolean calculatingStreak) {
        LocalDate scheduleStart = schedule.getAssignedStartDate().toLocalDate();
        if (queryEndDate.isBefore(queryStartDate)) {
            throw new IllegalArgumentException("Query end date cannot be before start date");
        }
        if (queryStartDate.isBefore(scheduleStart)) {
            if (queryEndDate.isBefore(scheduleStart)) {
                queryStartDate = queryEndDate;
            } else {
                queryStartDate = schedule.getAssignedStartDate().toLocalDate();
            }
        }
        Integer queryStartDay;
        if (queryStartDate.isBefore(scheduleStart)) {
            queryStartDay = 0;
        } else {
            queryStartDay = toDayOfSchedule(schedule, queryStartDate);
        }
        Integer queryEndDay;
        if (queryEndDate.isBefore(scheduleStart)) {
            queryEndDay = 0;
        } else {
            queryEndDay = toDayOfSchedule(schedule, queryEndDate);
        }
        initialize(schedule, queryStartDay, queryEndDay, calculatingStreak);
    }



    private static int[] calculate_day_adherence(List<DoseInstance> dosesToTake, List<RecordedDose> takenDoses, Time endTime) {
        int numDosesToTake = dosesToTake.size();
        int numDosesTaken = takenDoses.size();

        int countHit = 0;
        for (DoseInstance doseToTake : dosesToTake) {
            if (endTime != null && doseToTake.getTimeStart().after(endTime)) {
                numDosesTaken -= 1;
                numDosesToTake -= 1;
                continue;
            }
            for (RecordedDose takenDose : takenDoses) {
                if (takenDose.getTimeTaken().after(doseToTake.getTimeStart()) && takenDose.getTimeTaken().before(doseToTake.getTimeEnd()) && takenDose.getDoseValue() == doseToTake.getDose().getDoseValue()) {
                    doseToTake.setTakenDose(takenDose);
                    countHit++;
                    break;
                } else if (endTime != null && doseToTake.getTimeEnd().after(endTime)) {
                    numDosesTaken -= 1;
                    numDosesToTake -= 1;
                }
            }
        }

        int numLeftUntaken = numDosesToTake - countHit;
        int numIncorrectlyTaken = numDosesTaken - countHit;
        return new int[]{countHit, numLeftUntaken, numIncorrectlyTaken};
    }

    private static boolean dayQualifiesForStreak(int dosesTakenCorrectly, int dosesLeftUntaken, int dosesIncorrectlyTaken) {
        return dosesLeftUntaken == 0 && dosesIncorrectlyTaken == 0;
    }

    @Override
    public List<DoseInstance> getDoseInstances() {
        return doseInstances;
    }

    @Override
    public Integer getNumDosesToTake() {
        return numDosesToTake;
    }

    @Override
    public Double getAdherenceFraction() {
        return adherenceFraction;
    }

    @Override
    public Integer getNumDosesTakenCorrectly() {
        return numDosesTakenCorrectly;
    }

    @Override
    public Integer getNumDosesMissed() {
        return numDosesMissed;
    }

    @Override
    public Integer getNumDosesTakenIncorrectly() {
        return numDosesTakenIncorrectly;
    }

    @Override
    public Integer getLastErrantDay() {
        return lastErrantDay;
    }

    @Override
    public Integer getStreakLength() {
        return streakLength;
    }

    @Override
    public Schedule getSchedule() {
        return schedule;
    }

    private Time penultimateDayEndTime = null;


    private void initialize(Schedule schedule, int queryStartDay, int queryEndDay, boolean calculatingStreak) {
        assert (queryStartDay <= queryEndDay);
        assert (queryStartDay >= 0);
        this.queryStartDay = queryStartDay;
        this.queryEndDay = queryEndDay;
        this.calculatingStreak = calculatingStreak;
        this.schedule = schedule;
        resetResults();
    }

    @Override
    public int getQueryStartDay() {
        return queryStartDay;
    }

    @Override
    public void setQueryStartDay(int queryStartDay) {
        this.queryStartDay = queryStartDay;
        resetResults();
    }

    @Override
    public int getQueryEndDay() {
        return queryEndDay;
    }

    @Override
    public void setQueryEndDayTime(int queryEndDay, LocalTime endTime) {
        this.queryEndDay = queryEndDay;
        if (endTime != null) {
            this.penultimateDayEndTime = Time.valueOf(endTime);
        } else {
            this.penultimateDayEndTime = null;
        }
        resetResults();
    }

    @Override
    public void setQueryEndDateTime(LocalDate queryEndDate, LocalTime endTime) {
        setQueryEndDayTime(toDayOfSchedule(schedule, queryEndDate), endTime);
    }

    @Override
    public boolean isCalculatingStreak() {
        return calculatingStreak;
    }

    @Override
    public void setCalculatingStreak(boolean calculatingStreak) {
        this.calculatingStreak = calculatingStreak;
        resetResults();
    }

    @Override
    public LocalDate getLastErrantDate() {
        if (getLastErrantDay() == null) {
            return null;
        } else {
            return fromDayOfSchedule(schedule, getLastErrantDay());
        }
    }

    private void resetResults() {
        numDosesTakenCorrectly = null;
        numDosesMissed = null;
        numDosesTakenIncorrectly = null;
        lastErrantDay = null;
        doseInstances = null;
        streakLength = null;
        adherenceFraction = null;
        numDosesToTake = null;
    }

    public String toString() {
        if (numDosesToTake == null) {
            return "ScheduleAdherenceCalculator: not yet run";
        }
        return "ScheduleAdherenceCalculator: with results";
    }

    public String getSummary() {
        if (numDosesToTake == null) {
            return "Not yet run, call calculateScheduleAdherence()";
        }
        String results = "ScheduleAdherenceCalculator Results:";
        results += "\n Doses taken correctly: " + numDosesTakenCorrectly;
        results += "\n Doses taken incorrectly: " + numDosesTakenIncorrectly;
        results += "\n Doses missed: " + numDosesMissed;
        results += "\n Total doses to take: " + numDosesToTake;
        results += "\n Total doses taken: " + schedule.getRecordedDoses().size();
        results += "\n Adherence fraction: " + adherenceFraction;
        results += "\n Streak length: " + streakLength;
        return results;
    }

    @Override
    public void calculateScheduleAdherence() {
        int numDosesTakenCorrectly = 0;
        int numDosesMissed = 0;
        int numDosesTakenIncorrectly = 0;
        Integer lastErrantDay = null;
        Integer streakLength = null;
        int actualStartDay;
        // If the streak length is being calculated, must start counting from schedule start day even if values are not used for fractional adherence
        if (calculatingStreak) {
            actualStartDay = 0;
        } else {
            actualStartDay = queryStartDay;
        }
        ArrayList<DoseInstance> doseInstances = new ArrayList<>();
        schedule.getScheduledDoses().forEach(dose -> doseInstances.addAll(ScheduledDoseUnpacker.unpack(dose, queryStartDay, queryEndDay)));
        int numDosesToTake = doseInstances.size();
        doseInstances.sort(Comparator.comparingInt(DoseInstance::getDay));
        ArrayList<RecordedDose> recordedDoses = new ArrayList<>(schedule.getRecordedDoses());
        recordedDoses.sort(Comparator.comparingInt(RecordedDose::getDayTaken));
        int day = actualStartDay;
        int currentRdIndex = 0;
        int currentSdIndex = 0;
        while (day < queryEndDay) {
            ArrayList<RecordedDose> dayRds = new ArrayList<>();
            ArrayList<DoseInstance> daySds = new ArrayList<>();
            while (currentRdIndex < recordedDoses.size() && recordedDoses.get(currentRdIndex).getDayTaken() <= day) {
                if (recordedDoses.get(currentRdIndex).getDayTaken() == day) {
                    dayRds.add(recordedDoses.get(currentRdIndex));
                }
                currentRdIndex++;
            }
            while (currentSdIndex < doseInstances.size() && doseInstances.get(currentSdIndex).getDay() <= day) {
                if (doseInstances.get(currentSdIndex).getDay() == day) {
                    daySds.add(doseInstances.get(currentSdIndex));
                }
                currentSdIndex++;
            }
            int[] adherenceResults;
            if (day == queryEndDay - 1) {
                adherenceResults = calculate_day_adherence(daySds, dayRds, penultimateDayEndTime);
            } else {
                adherenceResults = calculate_day_adherence(daySds, dayRds, null);
            }
            if (calculatingStreak && !dayQualifiesForStreak(adherenceResults[0], adherenceResults[1], adherenceResults[2])) {
                lastErrantDay = day;
            }
            if (day >= queryStartDay) {
                numDosesTakenCorrectly += adherenceResults[0];
                numDosesMissed += adherenceResults[1];
                numDosesTakenIncorrectly += adherenceResults[2];
            }
            day += 1;
        }
        if (calculatingStreak) {
            streakLength = calculateStreakLength(lastErrantDay);
        }
        this.numDosesTakenCorrectly = numDosesTakenCorrectly;
        this.numDosesMissed = numDosesMissed;
        this.numDosesTakenIncorrectly = numDosesTakenIncorrectly;
        this.streakLength = streakLength;
        this.lastErrantDay = lastErrantDay;
        this.doseInstances = doseInstances;
        this.numDosesToTake = numDosesToTake;
        if (numDosesToTake != 0) {
            this.adherenceFraction = Math.max(numDosesTakenCorrectly - numDosesTakenIncorrectly, 0) / ((double) numDosesToTake);
        }
    }

    private Integer calculateStreakLength(Integer lastErrantDay) {
        if (lastErrantDay == null) {
            return queryEndDay;
        } else {
            return queryEndDay - lastErrantDay - 1;
        }
    }
}
