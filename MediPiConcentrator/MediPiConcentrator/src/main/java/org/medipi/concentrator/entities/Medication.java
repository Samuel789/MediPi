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

import org.medipi.medication.DoseUnit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 * Entity Class to manage DB access for patient
 * @author rick@robinsonhq.com
 */
@Entity
@Table(name = "medication")
@NamedQueries({
    @NamedQuery(name = "Medication.findAll", query = "SELECT m FROM medication m"),
    @NamedQuery(name = "Medication.findByMedicationId", query = "SELECT m FROM medication m WHERE m.id = :id")})
public class Medication implements Serializable {

    @JoinColumn(name = "dose_unit", referencedColumnName = "id")
    private Collection<DoseUnit> doseUnit;

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;

    @Column(name = "display_name")
    private String shortName;

    @Column(name = "unique_name")
    private String fullName;

    @Column(name = "advisory_stmt")
    private String advisoryStmt;

    @Column(name = "icon")
    private String icon;

    public Medication() {
    }

    public Medication(int id, String fullName, String shortName, String advisoryStmt, DoseUnit doseUnit) {
        this.id = id;
        this.fullName = fullName;
        this.shortName = shortName;
        this.advisoryStmt = advisoryStmt;
        this.doseUnits
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

    @Override
    public String toString() {
        return "org.medipi.concentrator.entities.Medication[ id=" + id + " full_name = " + full_name + " ]";
    }


    
}
