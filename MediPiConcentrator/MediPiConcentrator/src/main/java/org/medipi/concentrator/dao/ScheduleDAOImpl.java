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

import org.medipi.medication.model.Medication;
import org.medipi.medication.model.Schedule;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data Access Object for Hardware
 *
 * @author rick@robinsonhq.com
 */
@Repository
public class ScheduleDAOImpl extends GenericDAOImpl<Schedule> implements ScheduleDAO {
    @Override
    public Schedule findByScheduleId(int scheduleId) {
        return this.getEntityManager().createNamedQuery("Schedule.findByScheduleId", Schedule.class)
                .setParameter("id", scheduleId)
                .getSingleResult();
    }

    @Override
    public List<Schedule> findByPatientUuid(String patientUuid) {
        return this.getEntityManager().createNamedQuery("Schedule.findByPatientUuid", Schedule.class)
                .setParameter("uuid", patientUuid)
                .getResultList();
    }

    @Override
    public List<Schedule> findByMedicationAndPatient(Medication medication, String patientUuid) {
        return this.getEntityManager().createNamedQuery("Schedule.findByMedicationAndPatient", Schedule.class)
                .setParameter("uuid", patientUuid)
                .setParameter("medication", medication)
                .getResultList();
    }

    @Override
    public List<Schedule> findAll() {
        return this.getEntityManager().createNamedQuery("Schedule.findAll", Schedule.class)
                .getResultList();
    }
}
