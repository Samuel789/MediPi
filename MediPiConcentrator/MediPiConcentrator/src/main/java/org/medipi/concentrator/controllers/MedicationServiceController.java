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
package org.medipi.concentrator.controllers;

import org.medipi.concentrator.logging.MediPiLogger;
import org.medipi.concentrator.services.MedicationDownloadService;
import org.medipi.concentrator.services.MedicationScheduleUpdateService;
import org.medipi.medication.DoseInstance;
import org.medipi.model.MedWebDO;
import org.medipi.model.MedicationPatientDO;
import org.medipi.model.MedicationScheduleDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Class for controlling the download service for the MediPi patient units .
 * This exposes interfaces for: Listing all available downloads for a patient
 * device, Downloading AllHardware, Hardware and Patient Messaging
 *
 * @author rick@robinsonhq.com
 */
@RestController
@RequestMapping("MediPiConcentrator/webresources/medication")
public class MedicationServiceController {

    @Autowired
    private MedicationDownloadService medicationDownloadService;

    @Autowired
    private MedicationScheduleUpdateService medicationScheduleUpdateService;

    @Autowired
    private MediPiLogger logger;

    @RequestMapping(value = "/synchronize", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<MedicationPatientDO> synchronize(@RequestBody MedicationPatientDO uploadedData) {
        String patientUuid = this.medicationDownloadService.verifyPatient(uploadedData);
        this.medicationDownloadService.uploadRecordedDoses(uploadedData);
        this.medicationDownloadService.updateAdherence(patientUuid);
        return this.medicationDownloadService.getMedicationData(patientUuid);
    }

    @RequestMapping(value = "/clinician/getPatientData", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<MedWebDO> getMedicationData() {
        return this.medicationDownloadService.getAllPatientData();
    }

    @RequestMapping(value = "/clinician/addSchedule", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void addSchedule(@RequestBody MedicationScheduleDO content) {
        this.medicationScheduleUpdateService.addSchedule(content.getSchedule(), content.getDoses(), content.getMedicationId());
    }

    @RequestMapping(value = "/clinician/unpackPatientSchedules", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<DoseInstance>> unpackPatientSchedules(@RequestParam("patientUuid") String patientUuid, @RequestParam("startDate") String startDateString, @RequestParam("endDate") String endDateString) {
        return this.medicationDownloadService.unpackedDoses(patientUuid, startDateString, endDateString);
    }


}
