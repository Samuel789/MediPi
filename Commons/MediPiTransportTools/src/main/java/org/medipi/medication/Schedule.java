package org.medipi.medication;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

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
    private String alternateName;
    private String purposeStatement;
    private String patientUuid;
    private Integer scheduleId;
    private Medication medication;

    public ScheduleAdherence getScheduleAdherence() {
        return scheduleAdherence;
    }

    public void setScheduleAdherence(ScheduleAdherence scheduleAdherence) {
        this.scheduleAdherence = scheduleAdherence;
    }

    private ScheduleAdherence scheduleAdherence;

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

}
