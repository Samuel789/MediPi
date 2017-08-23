package org.medipi.concentrator.logic;

import org.medipi.medication.DoseInstance;
import org.medipi.medication.Schedule;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by sam on 23/08/17.
 */
public interface ScheduleAdherenceCalculatorInterface {
    List<DoseInstance> getDoseInstances();

    Integer getNumDosesToTake();

    String getSummary();

    Double getAdherenceFraction();

    Integer getNumDosesTakenCorrectly();

    Integer getNumDosesMissed();

    Integer getNumDosesTakenIncorrectly();

    Integer getLastErrantDay();

    Integer getStreakLength();

    Schedule getSchedule();

    int getQueryStartDay();

    void setQueryStartDay(int queryStartDay);

    int getQueryEndDay();

    void setQueryEndDayTime(int queryEndDay, LocalTime endTime);
    void setQueryEndDateTime(LocalDate queryEndDate, LocalTime endTime);

    boolean isCalculatingStreak();

    void setCalculatingStreak(boolean calculatingStreak);

    LocalDate getLastErrantDate();

    void calculateScheduleAdherence();
}
