package org.medipi.medication;

import javafx.scene.image.Image;

import java.io.Serializable;

public class Medication implements Serializable{
    private String shortName;
    private String fullName;
    private String cautionaryText;
    private Image icon;

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCautionaryText() {
        return cautionaryText;
    }

    public Image getIcon() {
        return icon;
    }

    public DoseUnit getDoseUnit() {
        return doseUnit;
    }

    private DoseUnit doseUnit;

    public Medication() {
        doseUnit = new DoseUnit();
        shortName = "TestShortName";
        fullName = "TestFullName";
        cautionaryText = "! Item 1\n ! Item 2";
    }
}
