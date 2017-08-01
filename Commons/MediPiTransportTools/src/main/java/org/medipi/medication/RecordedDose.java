package org.medipi.medication;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RecordedDose implements Serializable {
    public int getRecordedDoseId() {
        return recordedDoseId;
    }

    public void setRecordedDoseId(int recordedDoseId) {
        this.recordedDoseId = recordedDoseId;
    }

    public double getDoseValue() {
        return doseValue;
    }

    public void setDoseValue(double doseValue) {
        this.doseValue = doseValue;
    }

    public LocalDateTime getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(LocalDateTime timeTaken) {
        this.timeTaken = timeTaken;
    }

    private int recordedDoseId;
    private double doseValue;
    private LocalDateTime timeTaken;
}
