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
package org.medipi.concentrator.dao;

import org.medipi.medication.DoseUnit;
import org.medipi.medication.Medication;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data Access Object for Hardware
 * @author rick@robinsonhq.com
 */
@Repository
public class DoseUnitImpl extends GenericDAOImpl<DoseUnit> implements DoseUnitDAO {
    @Override
    public DoseUnit findByDoseUnitId(int doseUnitId) {
        return this.getEntityManager().createNamedQuery("DoseUnit.findByDoseUnitId", DoseUnit.class)
                .setParameter("id", doseUnitId)
                .getSingleResult();
    }

    @Override
    public List<DoseUnit> findAll() {
        return this.getEntityManager().createNamedQuery("DoseUnit.findAll", DoseUnit.class)
                .getResultList();
    }
}