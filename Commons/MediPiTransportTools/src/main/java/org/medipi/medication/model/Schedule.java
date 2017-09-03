package org.medipi.medication.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class Schedule implements Serializable {

    private Date assignedStartDate;
    private Date assignedEndDate;
    private String alternateName;
    private String purposeStatement;
    private String patientUuid;
    private Integer scheduleId;
    private Medication medication;
    private ScheduleAdherence scheduleAdherence;
    private Set<ScheduledDose> scheduledDoses = new HashSet<ScheduledDose>(0);
    private Set<RecordedDose> recordedDoses = new HashSet<RecordedDose>(0);
    public Schedule() {
    }

    public ScheduleAdherence getScheduleAdherence() {
        return scheduleAdherence;
    }

    public void setScheduleAdherence(ScheduleAdherence scheduleAdherence) {
        this.scheduleAdherence = scheduleAdherence;
    }

    public Set<ScheduledDose> getScheduledDoses() {
        return scheduledDoses;
    }

    public void setScheduledDoses(Set<ScheduledDose> scheduledDoses) {
        this.scheduledDoses = scheduledDoses;
    }

    public Set<RecordedDose> getRecordedDoses() {
        return recordedDoses;
    }

    public void setRecordedDoses(Set<RecordedDose> recordedDoses) {
        this.recordedDoses = recordedDoses;
    }

    public String getPatientUuid() {
        return patientUuid;
    }

    public void setPatientUuid(String patientUuid) {
        this.patientUuid = patientUuid;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Date getAssignedStartDate() {
        return assignedStartDate;
    }

    public void setAssignedStartDate(Date assignedStartDate) {
        this.assignedStartDate = assignedStartDate;
    }

    public Date getAssignedEndDate() {
        return assignedEndDate;
    }

    public void setAssignedEndDate(Date assignedEndDate) {
        this.assignedEndDate = assignedEndDate;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public String getPurposeStatement() {
        return purposeStatement;
    }

    public void setPurposeStatement(String purposeStatement) {
        this.purposeStatement = purposeStatement;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public String determineDisplayName() {
        if (alternateName != null && alternateName != "") {
            return alternateName;
        } else {
            return medication.getShortName();
        }
    }

}
