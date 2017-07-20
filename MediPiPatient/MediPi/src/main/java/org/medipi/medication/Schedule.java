package org.medipi.medication;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private LocalDate startDate;
    private LocalDate endDate;
    private String alternateName;
    private String purposeStatement;

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public String getPurposeStatement() {
        return purposeStatement;
    }

    public Medication getMedication() {
        return medication;
    }

    public String getDisplayName() {
        if (alternateName != null) {
            return alternateName;
        } else {
            return medication.getShortName();
        }
    }

    private Medication medication;

    public Schedule() {
        startDate = LocalDate.now();
        endDate = LocalDate.now();
        alternateName = "TestAltName";
        purposeStatement = "To do something";
        medication = new Medication();
    }
}
