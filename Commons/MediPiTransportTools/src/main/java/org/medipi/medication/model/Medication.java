package org.medipi.medication.model;

import java.io.Serializable;

public class Medication implements Serializable {
    private String shortName;
    private String fullName;
    private String cautionaryText;
    private String icon_name;
    private long medicationId;
    private DoseUnit doseUnit;

    public Medication() {
        doseUnit = new DoseUnit();
        shortName = "TestShortName";
        fullName = "TestFullName";
        cautionaryText = "! Item 1\n ! Item 2";
    }

    public long getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(long id) {
        this.medicationId = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCautionaryText() {
        return cautionaryText;
    }

    public void setCautionaryText(String cautionaryText) {
        this.cautionaryText = cautionaryText;
    }

    public String getIconName() {
        return icon_name;
    }

    public void setIconName(String icon) {
        this.icon_name = icon;
    }

    public DoseUnit getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(DoseUnit doseUnit) {
        this.doseUnit = doseUnit;
    }
}
