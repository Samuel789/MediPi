package org.medipi.medication;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlTransient;
import java.beans.Transient;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public Date getDeviceStartDate() {
        return deviceStartDate;
    }

    public void setDeviceStartDate(Date deviceStartDate) {
        this.deviceStartDate = deviceStartDate;
    }

    public Date getDeviceEndDate() {
        return deviceEndDate;
    }

    public void setDeviceEndDate(Date deviceEndDate) {
        this.deviceEndDate = deviceEndDate;
    }

    private Time defaultReminderTime;
    private Date deviceStartDate;
    private Date deviceEndDate;
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

    public boolean calculateIsDue() {

        java.util.Date currentDateTime = new java.util.Date();
        long diff = Math.abs(currentDateTime.getTime() - schedule.getAssignedStartDate().getTime());
        int thisDayNumber = (int) (diff/(24 * 60 * 60 * 1000));
        if (getRepeatInterval() != 0 && getRepeatInterval() != null) {
            double intervals = thisDayNumber / getRepeatInterval();
            boolean repeatsToday = (intervals == (int) intervals);
            if (!repeatsToday) {
                return false;
            }
        }
        long startTime = windowStartTime.getTime() % MILLIS_PER_DAY;
        long endTime = windowEndTime.getTime() % MILLIS_PER_DAY;
        long currentTime = currentDateTime.getTime() % MILLIS_PER_DAY;
        return (currentTime < endTime && currentTime >= startTime && !hasBeenTaken());
    }

    public boolean hasBeenTaken() {
        LocalDate today = LocalDate.now();
        LocalDateTime startTime = today.atTime(windowStartTime.toLocalTime());
        LocalDateTime endTime = today.atTime(windowEndTime.toLocalTime());
        for (RecordedDose takenDose: schedule.getRecordedDoses()) {
            System.out.println(takenDose.getTimeTaken());
            LocalDateTime takenTime = takenDose.getTimeTaken().toLocalDateTime();
            if (takenTime.isAfter(startTime) && takenTime.isBefore(endTime)) {
                return true;
            }
        }
        return false;
    }
}
