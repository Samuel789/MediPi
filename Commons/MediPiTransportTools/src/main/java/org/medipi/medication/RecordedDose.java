package org.medipi.medication;

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

    public Timestamp getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(Timestamp timeTaken) {
        this.timeTaken = timeTaken;
    }

    private String recordedDoseUUID;
    private double doseValue;
    private Timestamp timeTaken;

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    private Integer scheduleId;

    public RecordedDose() {}

    public RecordedDose(Timestamp timeTaken, double doseValue, Schedule schedule) {
        recordedDoseUUID = UUID.randomUUID().toString();
        this.timeTaken = timeTaken;
        this.doseValue = doseValue;
        this.scheduleId = schedule.getScheduleId();
    }

    public String toString() {
        return "Dose " + recordedDoseUUID + " containing " + doseValue + " units taken at " + timeTaken;
    }
}
