package org.medipi.concentrator.logic;

import com.sun.prism.impl.Disposer;
import org.apache.tomcat.jni.Local;
import org.medipi.medication.RecordedDose;
import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;

import java.sql.Array;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AdherenceCalculator {

    private LocalDate startDay;
    private LocalDate endDay;
    private boolean calculatingStreak;
    private Collection<Schedule> schedules;
    private HashMap<Schedule, Integer> scheduleStreakLength;
    private HashMap<Schedule, Double> scheduleFraction;
    private int patientStreaklength;
    private double patientFraction;


    public AdherenceCalculator(Collection<Schedule> schedules, LocalDate startDay, LocalDate endDay, boolean calculatingStreak) {
        this.schedules = schedules;
        this.startDay = startDay;
        this.endDay = endDay;
        this.calculatingStreak = calculatingStreak;
        calculatePatientAdherence();
    }

    public static boolean dueOnDay(ScheduledDose dose, int day) {
        int dayOfDose = day - dose.getStartDay();
        if (dayOfDose == dose.getStartDay()) return true;
        else if (dayOfDose < 0) return false;
        else if (dose.getRepeatInterval() == null || dose.getRepeatInterval() == 0) return false;
        else if (dose.getEndDay() != null && dayOfDose >= dose.getEndDay()) return false;
        else return dayOfDose % dose.getRepeatInterval() == 0;
    }
    public static int getDayOfSchedule(Schedule schedule, LocalDate date) {
        return (int) schedule.getAssignedStartDate().toLocalDate().until(date, ChronoUnit.DAYS);
    }

    public static int countDosesBetween(int startDay, int endDay, ScheduledDose dose) {
        assert(startDay < endDay && startDay >= 0 && endDay >= 0);
        int relativeStartDay = Math.max(startDay, dose.getStartDay()) - startDay;
        int relativeEndDay;
        if (dose.getEndDay() == null) {
            relativeEndDay = endDay - startDay;
        } else {
            relativeEndDay = Math.min(endDay, dose.getEndDay()) - startDay;
        }
        if (dose.getRepeatInterval() == null) {
            return relativeStartDay == 0 ? 1 : 0;
        } else {
            int intervalWidth = relativeEndDay - relativeStartDay;
            int baseCount = intervalWidth / dose.getRepeatInterval();
            if (intervalWidth % dose.getRepeatInterval() == 0) {
                return baseCount;
            } else if (relativeStartDay == 0) {
                return baseCount + 1;
            } else {
                if (relativeStartDay % dose.getRepeatInterval() + intervalWidth < intervalWidth) {
                    return baseCount + 1;
                } else {
                    return baseCount;
                }
            }
        }
    }
    public List<DoseInstance> unpackDoses(ScheduledDose scheduledDose, int startDay, int endDay) {
        assert(startDay < endDay && startDay >= 0 && endDay >= 0);
        int sequenceStartDay = Math.max(startDay, scheduledDose.getStartDay());
        int sequenceEndDay = Math.min(endDay, scheduledDose.getEndDay());
        if (scheduledDose.getRepeatInterval() == null) {
            if (startDay >= sequenceStartDay && startDay < sequenceEndDay) {
                return Collections.singletonList(new DoseInstance(scheduledDose, startDay));
            } else {
                return Collections.emptyList();
            }
        }
        int offset = (sequenceStartDay - startDay) % scheduledDose.getRepeatInterval();
        List<DoseInstance> doses = range(sequenceStartDay + offset, sequenceEndDay, scheduledDose.getRepeatInterval()).mapToObj(day -> new DoseInstance(scheduledDose, day)).collect(Collectors.toList());
        return doses;
    }

    private IntStream range(int start, int stop, int step) {
        return IntStream.range(start, stop/step).map(i -> i*step);
    }

    private void calculatePatientAdherence() {
        int patientDosesCorrectlyTaken = 0;
        int patientDosesMissed = 0;
        int patientDosesTakenIncorrectly = 0;
        int streakLength = 0;
        double adherenceFraction;
        LocalDate earliestStart = LocalDate.now();
        HashSet<LocalDate> errantDays = new HashSet<>();
        HashMap<Schedule, Integer> scheduleStreakLength = new HashMap<>();
        HashMap<Schedule, Double> scheduleFraction = new HashMap<>();
        for (Schedule schedule: schedules) {
            LocalDate scheduleStart = schedule.getAssignedStartDate().toLocalDate();
            if (scheduleStart.isBefore(earliestStart)) earliestStart = scheduleStart;
            int queryStartDay = getDayOfSchedule(schedule, startDay);
            int queryEndDay = getDayOfSchedule(schedule, endDay);
            int[] adherenceStatistics = calculateScheduleAdherence(schedule, queryStartDay, queryEndDay);
            patientDosesCorrectlyTaken = adherenceStatistics[0];
            patientDosesMissed = adherenceStatistics[1];
            patientDosesTakenIncorrectly = adherenceStatistics[2];
            if (calculatingStreak && adherenceStatistics[3] >= queryStartDay) {
                errantDays.add(schedule.getAssignedStartDate().toLocalDate().plusDays(adherenceStatistics[3]));
                scheduleStreakLength.put(schedule, calculateStreakLength(errantDays, scheduleStart));
            }
            scheduleFraction.put(schedule, calculateAdherenceFraction(adherenceStatistics[0], adherenceStatistics[1], adherenceStatistics[2]));
        }
        if (calculatingStreak) {
            streakLength = calculateStreakLength(errantDays, earliestStart);
        }
        adherenceFraction = calculateAdherenceFraction(patientDosesCorrectlyTaken, patientDosesMissed, patientDosesTakenIncorrectly);
        this.patientStreaklength = streakLength;
        this.patientFraction = adherenceFraction;
        this.scheduleFraction = scheduleFraction;
        this.scheduleStreakLength = scheduleStreakLength;
    }

    private double calculateAdherenceFraction(int dosesCorrectlyTaken, int dosesNotTaken, int dosesTakenIncorrectly) {
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



    private int[] calculateScheduleAdherence(Schedule schedule, int queryStartDay, int queryEndDay) {
        int scheduleHitScheduledDoses = 0;
        int scheduleMissedScheduledDoses = 0;
        int scheduleStrayTakenDoses = 0;
        int lastErrantDay = -1;

        int actualStartDay;
        // If the streak length is being calculated, must start counting from schedule start day even if values are not used for fractional adherence
        if (calculatingStreak) {
            actualStartDay = 0;
        } else {
            actualStartDay = queryStartDay;
        }
        ArrayList<DoseInstance> doseInstances = new ArrayList<>();
        schedule.getScheduledDoses().forEach(dose -> doseInstances.addAll(unpackDoses(dose, queryStartDay, queryEndDay)));
        doseInstances.sort(Comparator.comparingInt(DoseInstance::getDay));
        ArrayList<RecordedDose> recordedDoses = new ArrayList<>(schedule.getRecordedDoses());
        recordedDoses.sort(Comparator.comparingInt(RecordedDose::getDayTaken));
        int day = actualStartDay;
        int currentRdIndex = 0;
        int currentSdIndex = 0;
        while (day < queryEndDay) {
            ArrayList<RecordedDose> dayRds = new ArrayList<>();
            ArrayList<DoseInstance> daySds = new ArrayList<>();
            while(recordedDoses.get(currentRdIndex).getDayTaken() <= day) {
                if (recordedDoses.get(currentRdIndex).getDayTaken() == day) {
                    dayRds.add(recordedDoses.get(currentRdIndex));
                }
            }
            while(doseInstances.get(currentSdIndex).getDay() <= day) {
                if (doseInstances.get(currentSdIndex).getDay() == day) {
                    daySds.add(doseInstances.get(currentSdIndex));
                }
            }
            int[] adherenceResults = calculate_day_adherence(daySds, dayRds);
            if (calculatingStreak && !dayQualifiesForStreak(adherenceResults[0], adherenceResults[1], adherenceResults[2])) {
                lastErrantDay = day;
            }
            if (day >= queryStartDay){
                scheduleHitScheduledDoses += adherenceResults[0];
                scheduleMissedScheduledDoses += adherenceResults[1];
                scheduleStrayTakenDoses += adherenceResults[2];
            }
            day += 1;
        }
        return new int[] {scheduleHitScheduledDoses, scheduleMissedScheduledDoses, scheduleStrayTakenDoses, lastErrantDay};
    }

    private int[] calculate_day_adherence(List<DoseInstance> dosesToTake, List<RecordedDose> takenDoses) {
        int numDosesToTake = dosesToTake.size();
        int numDosesTaken = takenDoses.size();

        int countHit = 0;
        for (DoseInstance doseToTake: dosesToTake) {
            for (RecordedDose takenDose: takenDoses) {
                if (takenDose.getTimeTaken().after(doseToTake.getTimeStart()) && takenDose.getTimeTaken().before(doseToTake.getTimeEnd()) && takenDose.getDoseValue() == doseToTake.getDose().getDoseValue()) {
                    countHit++;
                    break;
                }
            }
        }

        int numLeftUntaken = numDosesToTake - countHit;
        int numIncorrectlyTaken = numDosesTaken - countHit;
        return new int[] {countHit, numLeftUntaken, numIncorrectlyTaken};
    }

    private boolean dayQualifiesForStreak(int dosesTakenCorrectly, int dosesLeftUntaken, int dosesIncorrectlyTaken) {
        return dosesLeftUntaken == 0 && dosesIncorrectlyTaken == 0;
    }
}

class DoseInstance {
    public ScheduledDose getDose() {
        return dose;
    }

    private ScheduledDose dose;

    public int getDay() {
        return day;
    }

    private int day;

    public Time getTimeStart() {
        return timeStart;
    }

    public Time getTimeEnd() {
        return timeEnd;
    }

    private Time timeStart;
    private Time timeEnd;

    DoseInstance(ScheduledDose dose, int day) {
        this.dose = dose;
        this.day = day;
        this.timeStart = dose.getWindowStartTime();
        this.timeEnd = dose.getWindowEndTime();
    }

}