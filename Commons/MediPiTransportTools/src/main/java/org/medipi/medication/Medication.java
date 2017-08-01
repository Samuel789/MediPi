package org.medipi.medication;

import java.io.Serializable;

public class Medication implements Serializable {
    private String shortName;
    private String fullName;
    private String cautionaryText;
    private String icon_name;

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int id) {
        this.medicationId = id;
    }

    private int medicationId;

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCautionaryText() {
        return cautionaryText;
    }

    public String getIconName() {
        return icon_name;
    }

    public DoseUnit getDoseUnit() {
        return doseUnit;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCautionaryText(String cautionaryText) {
        this.cautionaryText = cautionaryText;
    }

    public void setIconName(String icon) {
        this.icon_name = icon;
    }

    public void setDoseUnit(DoseUnit doseUnit) {
        this.doseUnit = doseUnit;
    }

    private DoseUnit doseUnit;

    public Medication() {
        doseUnit = new DoseUnit();
        shortName = "TestShortName";
        fullName = "TestFullName";
        cautionaryText = "! Item 1\n ! Item 2";
    }
}
