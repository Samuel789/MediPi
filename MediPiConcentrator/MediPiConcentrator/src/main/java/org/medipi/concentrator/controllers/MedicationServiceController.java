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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * Controller for downloading a list of available updates to a patient
     * device. This method passes the incoming message to the service layer for
     * processing
     *
     * @param hardwareName incoming deviceId parameter from RESTful message
     * @param patientUuid incoming patientUuid parameter from RESTful message
     * @return Response to the request
     */
    @RequestMapping(value = "/download/{hardwareName}/{patientUuid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<MedicationDO> getDownloadableList(@PathVariable("hardwareName") String hardwareName, @PathVariable("patientUuid") String patientUuid) {
//Removed to Reduce Logs size        logger.log(DownloadServiceController.class.getName(), new Date().toString() + " get DownloadableList called by patientUuid: " + patientUuid + " using hardwareName: " + hardwareName);
        return this.medicationDownloadService.getMedicationData(hardwareName, patientUuid);
    }

}
