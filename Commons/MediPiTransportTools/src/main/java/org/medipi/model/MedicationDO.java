/*
 Copyright 2016  Richard Robinson @ HSCIC <rrobinson@hscic.gov.uk, rrobinson@nhs.net>

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.medipi.model;

import org.medipi.medication.MedicationLogicException;
import org.medipi.medication.RecordedDose;
import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Class to hold medication information to be transmitted from the
 * MediPiConcentrator to the Patient unit.
 *
 * @author sc7898@gmail.com
 */
public class MedicationDO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String medicationPackageId;
    private String version;
    private String versionAuthor;
    private Date versionDate;
    private String signature;

    private List<Schedule> schedules;
    private Date downloadedDate;

    /**
     * Constructor
     */
    public MedicationDO() {
    }

    /**
     * Constructor
     *
     * @param downloadableUuid
     */
    public MedicationDO(String downloadableUuid) {
        this.medicationPackageId = downloadableUuid;
    }

    /**
     * Constructor
     *
     * @param downloadableUuid
     * @param version
     * @param versionAuthor
     * @param versionDate
     */
    public MedicationDO(String downloadableUuid, String version, String versionAuthor, Date versionDate) {
        this.medicationPackageId = downloadableUuid;
        this.version = version;
        this.versionAuthor = versionAuthor;
        this.versionDate = versionDate;
    }


    public String getMedicationPackageId() {
        return medicationPackageId;
    }

    public void setMedicationPackageId(String medicationPackageId) {
        this.medicationPackageId = medicationPackageId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionAuthor() {
        return versionAuthor;
    }

    public void setVersionAuthor(String versionAuthor) {
        this.versionAuthor = versionAuthor;
    }

    public Date getVersionDate() {
        return versionDate;
    }

    public void setVersionDate(Date versionDate) {
        this.versionDate = versionDate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getDownloadedDate() {
        return downloadedDate;
    }

    public void setDownloadedDate(Date downloadedDate) {
        this.downloadedDate = downloadedDate;
    }

    public void recreateReferences() {
        for (Schedule schedule: schedules) {
            for (ScheduledDose scheduledDose: schedule.getScheduledDoses()) {
                if (scheduledDose.getScheduleId() != schedule.getScheduleId()) {
                    throw new MedicationLogicException("Scheduled Dose ScheduleId (" + scheduledDose.getScheduleId() + ") does not match containing schedule (" + schedule.getScheduleId() + ")");
                }
                scheduledDose.setSchedule(schedule);
            }
            for (RecordedDose recordedDose: schedule.getRecordedDoses()) {
                if (recordedDose.getScheduleId() != schedule.getScheduleId()) {
                    throw new MedicationLogicException("RecordedDose ScheduleId (" + recordedDose.getScheduleId() + ") does not match containing schedule (" + schedule.getScheduleId() + ")");
                }
                recordedDose.setSchedule(schedule);
            }
        }
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (medicationPackageId != null ? medicationPackageId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MedicationDO)) {
            return false;
        }
        MedicationDO other = (MedicationDO) object;
        if ((this.medicationPackageId == null && other.medicationPackageId != null) || (this.medicationPackageId != null && !this.medicationPackageId.equals(other.medicationPackageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.medipi.model.MedicationDO[ medicationPackageId=" + medicationPackageId + " ]";
    }

}
