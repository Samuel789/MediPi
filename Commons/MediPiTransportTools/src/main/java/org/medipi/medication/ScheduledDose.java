package org.medipi.medication;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ScheduledDose implements Serializable {
    public double getDoseValue() {
        return doseValue;
    }
    private final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;

    public Integer getStartDay() {
        return startDay;
    }

    public Integer getEndDay() {
        return endDay;
    }

    public Integer getRepeatInterval() {
        return repeatInterval;
    }

    public Time getWindowStartTime() {
        return windowStartTime;
    }

    public Time getWindowEndTime() {
        return windowEndTime;
    }

    public Time getReminderTime() {
        return reminderTime;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    private double doseValue;
    private Integer startDay;
    private Integer endDay;
    private Integer repeatInterval;
    private Time windowStartTime;
    private Time windowEndTime;

    public Time getDefaultReminderTime() {
        return defaultReminderTime;
    }

    public void setDefaultReminderTime(Time defaultReminderTime) {
        this.defaultReminderTime = defaultReminderTime;
    }

    private Time defaultReminderTime;
    private Time reminderTime;

    public void setDoseValue(double doseValue) {
        this.doseValue = doseValue;
    }

    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }

    public void setEndDay(Integer endDay) {
        this.endDay = endDay;
    }

    public void setRepeatInterval(Integer repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public void setWindowStartTime(Time windowStartTime) {
        this.windowStartTime = windowStartTime;
    }

    public void setWindowEndTime(Time windowEndTime) {
        this.windowEndTime = windowEndTime;
    }

    @XmlTransient
    public Schedule getSchedule() {
        return schedule;
    }

    @XmlTransient
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @XmlTransient
    private transient Schedule schedule;


    public void setReminderTime(Time reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    private Integer scheduleId;

    public Integer getScheduledDoseId() {
        return scheduledDoseId;
    }

    public void setScheduledDoseId(Integer scheduledDoseId) {
        this.scheduledDoseId = scheduledDoseId;
    }

    private Integer scheduledDoseId;

    public ScheduledDose() {
    }


}
