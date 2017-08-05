package org.medipi.medication;

import java.beans.Transient;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Schedule implements Serializable {

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public void setPurposeStatement(String purposeStatement) {
        this.purposeStatement = purposeStatement;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public void setAssignedStartDate(Date assignedStartDate) {
        this.assignedStartDate = assignedStartDate;
    }

    public void setAssignedEndDate(Date assignedEndDate) {
        this.assignedEndDate = assignedEndDate;
    }

    private Date assignedStartDate;
    private Date assignedEndDate;
    private Date deviceStartDate;
    private Date deviceEndDate;
    private String alternateName;
    private String purposeStatement;
    private String patientUuid;
    private int scheduleId;
    private Medication medication;

    public Set<ScheduledDose> getScheduledDoses() {
        return scheduledDoses;
    }

    public void setScheduledDoses(Set<ScheduledDose> scheduledDoses) {
        this.scheduledDoses = scheduledDoses;
    }

    private Set<ScheduledDose> scheduledDoses = new HashSet<ScheduledDose>(0);

    public Set<RecordedDose> getRecordedDoses() {
        return recordedDoses;
    }

    public void setRecordedDoses(Set<RecordedDose> recordedDoses) {
        this.recordedDoses = recordedDoses;
    }

    private Set<RecordedDose> recordedDoses = new HashSet<RecordedDose>(0);
    public Schedule() {
    }

    public Date getDeviceStartDate() {
        return deviceStartDate;
    }

    public void setDeviceStartDate(Date deviceStartDate) {
        this.deviceStartDate = deviceStartDate;
    }

    public Date getDeviceEndDate() {
        return deviceEndDate;
    }

    public void setDeviceEndDate(Date deviceEndDate) {
        this.deviceEndDate = deviceEndDate;
    }

    public String getPatientUuid() {
        return patientUuid;
    }

    public void setPatientUuid(String patientUuid) {
        this.patientUuid = patientUuid;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Date getAssignedStartDate() {
        return assignedStartDate;
    }

    public Date getAssignedEndDate() {
        return assignedEndDate;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public String getPurposeStatement() {
        return purposeStatement;
    }

    public Medication getMedication() {
        return medication;
    }

    public String determineDisplayName() {
        if (alternateName != null) {
            return alternateName;
        } else {
            return medication.getShortName();
        }
    }

    public ScheduledDose findDueDose() {
        for (ScheduledDose dose: getScheduledDoses()) {
            if (dose.calculateIsDue()) {
                return dose;
            }
        }
        return null;
    }

    public void recordDose(Timestamp timeTaken, double doseValue) {
        recordedDoses.add(new RecordedDose(timeTaken, doseValue, this));
    }

}
