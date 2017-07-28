package org.medipi.medication;

import java.io.Serializable;
import java.time.LocalDateTime;

public class RecordedDose implements Serializable {
    private double doseValue;
    private LocalDateTime timeTaken;
}
