package org.medipi.medication;

/**
 * Created by sam on 10/08/17.
 */
public class PatientAdherence {

    private String patientUuid;
    private Integer streakLength;
    private Double sevenDayFraction;

    public Integer getStreakLength() {
        return streakLength;
    }

    public void setStreakLength(Integer streakLength) {
        this.streakLength = streakLength;
    }

    public Double getSevenDayFraction() {
        return sevenDayFraction;
    }

    public void setSevenDayFraction(Double sevenDayFraction) {
        this.sevenDayFraction = sevenDayFraction;
    }

    public String getPatientUuid() {
        return patientUuid;
    }

    public void setPatientUuid(String patientUuid) {
        this.patientUuid = patientUuid;
    }
}
