package org.medipi.medication;

import com.sun.javaws.exceptions.InvalidArgumentException;

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


    @Override
    public int hashCode() {
        return scheduleId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScheduledDose that = (ScheduledDose) o;

        if (Double.compare(that.doseValue, doseValue) != 0) return false;
        if (!startDay.equals(that.startDay)) return false;
        if (endDay != null ? !endDay.equals(that.endDay) : that.endDay != null) return false;
        if (repeatInterval != null ? !repeatInterval.equals(that.repeatInterval) : that.repeatInterval != null)
            return false;
        if (!windowStartTime.equals(that.windowStartTime)) return false;
        if (!windowEndTime.equals(that.windowEndTime)) return false;
        if (!defaultReminderTime.equals(that.defaultReminderTime)) return false;
        if (!scheduleId.equals(that.scheduleId)) return false;
        return scheduledDoseId.equals(that.scheduledDoseId);
    }

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
        if (repeatInterval != null && repeatInterval == 0) {
            System.out.println(repeatInterval);
            throw new IllegalArgumentException("Repeat Interval cannot be zero. Set to 'null' for no repeat.");
        }
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
