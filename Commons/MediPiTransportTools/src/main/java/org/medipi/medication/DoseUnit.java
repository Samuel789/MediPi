package org.medipi.medication;

import java.io.Serializable;

public class DoseUnit implements Serializable {

    public String getName() {
        return name;
    }
    public DoseUnit() {

    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public int getDoseUnitId() {
        return doseUnitId;
    }

    public void setDoseUnitId(int id) {
        this.doseUnitId = id;
    }

    private int doseUnitId;
}
