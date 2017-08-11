package org.medipi.medication;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class RecordedDose implements Serializable {
    public String getRecordedDoseUUID() {
        return recordedDoseUUID;
    }

    public void setRecordedDoseUUID(String recordedDoseId) {
        this.recordedDoseUUID = recordedDoseId;
    }

    public double getDoseValue() {
        return doseValue;
    }

    public void setDoseValue(double doseValue) {
        this.doseValue = doseValue;
    }

    public Time getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Time timeTaken) {
        this.timeTaken = timeTaken;
    }

    private String recordedDoseUUID;
    private double doseValue;

    public int getDayTaken() {
        return dayTaken;
    }

    public void setDayTaken(int dayTaken) {
        this.dayTaken = dayTaken;
    }

    private int dayTaken;
    private Time timeTaken;

    public boolean isAdherent() {
        return adherent;
    }

    public void setAdherent(boolean adherent) {
        this.adherent = adherent;
    }

    private boolean adherent;

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

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    private Integer scheduleId;

    public RecordedDose() {}

    public RecordedDose(int dayTaken, Time timeTaken, double doseValue, Schedule schedule) {
        recordedDoseUUID = UUID.randomUUID().toString();
        this.dayTaken = dayTaken;
        this.timeTaken = timeTaken;
        this.doseValue = doseValue;
        this.scheduleId = schedule.getScheduleId();
        this.schedule = schedule;
    }

    public String toString() {
        return "Dose " + recordedDoseUUID + " containing " + doseValue + " units taken at " + timeTaken;
    }
}
