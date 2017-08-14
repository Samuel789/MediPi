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

import org.medipi.medication.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Class to hold medication information to be transmitted from the
 * MediPiConcentrator to the Patient unit.
 *
 * @author sc7898@gmail.com
 */
public class MedWebDO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String medicationPackageId;
    private String version;



    public String getHardwareName() {
        return hardwareName;
    }

    public void setHardwareName(String hardwareName) {
        this.hardwareName = hardwareName;
    }

    private String hardwareName;
    private String versionAuthor;
    private Date versionDate;
    private String signature;

    public List<String> getPatientUuids() {
        return patientUuids;
    }

    public void setPatientUuids(List<String> patientUuids) {
        this.patientUuids = patientUuids;
    }

    private List<String> patientUuids;

    public List<PatientAdherence> getPatientAdherenceList() {
        return patientAdherenceList;
    }

    public void setPatientAdherenceList(List<PatientAdherence> patientAdherenceList) {
        this.patientAdherenceList = patientAdherenceList;
    }

    private List<PatientAdherence> patientAdherenceList;

    private List<Schedule> schedules;
    private Date downloadedDate;

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }

    private List<Medication> medications;

    /**
     * Constructor
     */
    public MedWebDO() {
    }

    /**
     * Constructor
     *
     * @param downloadableUuid
     */
    public MedWebDO(String downloadableUuid) {
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
    public MedWebDO(String downloadableUuid, String version, String versionAuthor, Date versionDate) {
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
        if (!(object instanceof MedWebDO)) {
            return false;
        }
        MedWebDO other = (MedWebDO) object;
        if ((this.medicationPackageId == null && other.medicationPackageId != null) || (this.medicationPackageId != null && !this.medicationPackageId.equals(other.medicationPackageId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.medipi.model.MedicationPatientDO[ medicationPackageId=" + medicationPackageId + " ]";
    }

}
