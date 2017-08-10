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

import java.util.Date;
import java.util.List;
import org.medipi.concentrator.logging.MediPiLogger;
import org.medipi.concentrator.model.DownloadableDO;
import org.medipi.concentrator.services.DownloadableListService;
import org.medipi.concentrator.services.HardwareDownloadableService;
import org.medipi.concentrator.services.MedicationDownloadService;
import org.medipi.concentrator.services.PatientDownloadableService;
import org.medipi.model.MedicationDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private MediPiLogger logger;

    @RequestMapping(value="/synchronize", method = RequestMethod.POST, consumes = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<MedicationDO> synchronize(@RequestBody MedicationDO uploadedData) {
//Removed to Reduce Logs size        logger.log(DownloadServiceController.class.getName(), new Date().toString() + " get DownloadableList called by patientUuid: " + patientUuid + " using hardwareName: " + hardwareName);
        return this.medicationDownloadService.synchronize(uploadedData);
    }

}
