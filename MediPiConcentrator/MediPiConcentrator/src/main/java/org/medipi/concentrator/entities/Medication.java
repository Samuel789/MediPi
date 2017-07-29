/*
 Copyright 2016  Richard Robinson @ NHS Digital <rrobinson@nhs.net>

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
package org.medipi.concentrator.entities;

import org.medipi.medication.DoseUnitInterface;
import org.medipi.medication.MedicationInterface;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity Class to manage DB access for patient
 * @author rick@robinsonhq.com
 */
@Entity
@Table(name = "medication")
@NamedQueries({
    @NamedQuery(name = "Medication.findAll", query = "SELECT m FROM Medication m"),
    @NamedQuery(name = "Medication.findByMedicationId", query = "SELECT m FROM Medication m WHERE m.id = :id")})
public class Medication implements Serializable, MedicationInterface {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private int id;

    @Column(name = "display_name", nullable = false)
    private String shortName;

    @Column(name = "unique_name", nullable = false, unique = true)
    private String fullName;

    @Column(name = "advisory_stmt")
    private String cautionaryText;

    @Column(name = "icon_name")
    private String iconName;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DoseUnit.class)
    @JoinColumn(name = "dose_unit", referencedColumnName = "id")
    private DoseUnitInterface doseUnit;

    public Medication() {
    }

    public Medication(int id, String fullName, String shortName, String cautionaryText, DoseUnitInterface doseUnit) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.cautionaryText = cautionaryText;
        this.doseUnit = doseUnit;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medication)) {
            return false;
        }
        Medication other = (Medication) object;
        return this.id == other.id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getShortName() {
        return shortName;
    }

    @Override
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCautionaryText() {
        return cautionaryText;
    }

    public void setCautionaryText(String cautionaryText) {
        this.cautionaryText = cautionaryText;
    }

    @Override
    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public DoseUnitInterface getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(DoseUnitInterface doseUnit) {
        this.doseUnit = doseUnit;
    }

    @Override
    public String toString() {
        return "org.medipi.concentrator.entities.Medication[ id=" + id + " full_name = " + fullName + " ]";
    }


    
}
