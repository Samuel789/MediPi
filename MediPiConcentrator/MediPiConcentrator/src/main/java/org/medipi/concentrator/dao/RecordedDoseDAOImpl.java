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

import org.medipi.medication.RecordedDose;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data Access Object for Hardware
 *
 * @author rick@robinsonhq.com
 */
@Repository
public class RecordedDoseDAOImpl extends GenericDAOImpl<RecordedDose> implements RecordedDoseDAO {
    @Override
    public RecordedDose findByRecordedDoseUUID(String recordedDoseId) {
        return this.getEntityManager().createNamedQuery("RecordedDose.findByRecordedDoseUUID", RecordedDose.class)
                .setParameter("id", recordedDoseId)
                .getSingleResult();
    }

    @Override
    public List<RecordedDose> findAll() {
        return this.getEntityManager().createNamedQuery("RecordedDose.findAll", RecordedDose.class)
                .getResultList();
    }

    @Override
    public List<RecordedDose> findByPatientUuid(String patientUuid) {
        return this.getEntityManager().createNamedQuery("RecordedDose.findByPatientUuid", RecordedDose.class)
                .setParameter("patientUuid", patientUuid)
                .getResultList();
    }
}
