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

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity Class to manage DB access for patient
 * @author rick@robinsonhq.com
 */
@Entity
@Table(name = "dose_unit")
@NamedQueries({
    @NamedQuery(name = "DoseUnit.findAll", query = "SELECT d FROM DoseUnit d"),
    @NamedQuery(name = "DoseUnit.findByDoseUnitId", query = "SELECT d FROM DoseUnit d WHERE d.id = :id")})
public class DoseUnit implements Serializable, DoseUnitInterface {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private int id;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "name", nullable = false)
    private String name;

    public DoseUnit(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public DoseUnit() {

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DoseUnit)) {
            return false;
        }
        DoseUnit other = (DoseUnit) object;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "org.medipi.concentrator.entities.DoseUnit[ id=" + id + " name = " + name + " ]";
    }


    
}
