package org.medipi.medication;

import java.io.Serializable;

public class DoseUnit implements Serializable{
    public String getName() {
        return name;
    }
    public DoseUnit() {
        name = "milliQuads";
    }
    private String name;
}
