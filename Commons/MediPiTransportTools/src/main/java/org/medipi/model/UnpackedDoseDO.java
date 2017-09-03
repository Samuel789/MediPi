package org.medipi.model;

import org.medipi.medication.model.DoseInstance;

import java.util.List;

public class UnpackedDoseDO {
    public List<DoseInstance> getDoseInstances() {
        return doseInstances;
    }

    public void setDoseInstances(List<DoseInstance> doseInstances) {
        this.doseInstances = doseInstances;
    }

    List<DoseInstance> doseInstances;
}
