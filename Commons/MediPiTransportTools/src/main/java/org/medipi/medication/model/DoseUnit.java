package org.medipi.medication.model;

import java.io.Serializable;

public class DoseUnit implements Serializable {

    private String name;
    private int doseUnitId;

    public DoseUnit() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDoseUnitId() {
        return doseUnitId;
    }

    public void setDoseUnitId(int id) {
        this.doseUnitId = id;
    }
}
