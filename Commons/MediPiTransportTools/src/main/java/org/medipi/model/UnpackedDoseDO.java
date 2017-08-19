package org.medipi.model;

import org.medipi.medication.DoseInstance;
import org.medipi.medication.Schedule;

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
