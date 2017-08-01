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
package org.medipi.medication;

import org.medipi.MediPi;
import org.medipi.MediPiMessageBox;
import org.medipi.logging.MediPiLogger;
import org.medipi.messaging.rest.RESTfulMessagingEngine;
import org.medipi.messaging.vpn.VPNServiceManager;
import org.medipi.model.DownloadableDO;
import org.medipi.model.MedicationDO;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Class to poll the MediPi Concentrator and request any downloads for the user
 * or device
 *
 * This class polls the concentrator receives the list of responses and calls
 * the appropriate handler
 *
 * @author rick@robinsonhq.com
 */
public class Synchronizer
        implements Runnable {

    private static final String MEDIPITRANSMITRESOURCEPATH = "medipi.transmit.resourcepath";
    private static final String MEDIPIDEVICECERTNAME = "medipi.device.cert.name";
    private static final String MEDIPIPATIENTCERTNAME = "medipi.patient.cert.name";
    private String patientCertName;
    private final String deviceCertName;
    private final String resourcePath;
    private final MediPi medipi;
    private RESTfulMessagingEngine rme;

    /**
     * Constructor for PollIncomingMessage class
     *
     * @param medipi
     */
    public Synchronizer(MediPi medipi) throws Exception {
        this.medipi = medipi;
        resourcePath = medipi.getProperties().getProperty(MEDIPITRANSMITRESOURCEPATH);
        if (resourcePath == null || resourcePath.trim().equals("")) {
            MediPiLogger.getInstance().log(Synchronizer.class.getName() + ".error", "MediPi resource base path is not set");
            medipi.makeFatalErrorMessage(resourcePath + " - MediPi resource base path is not set", null);
        }
        // Get the device Cert
        deviceCertName = System.getProperty(MEDIPIDEVICECERTNAME);
        if (deviceCertName == null || deviceCertName.trim().length() == 0) {
            medipi.makeFatalErrorMessage("MediPi device cert not found", null);
        }
        String[] params = {"{deviceId}", "{patientId}"};
        rme = new RESTfulMessagingEngine(resourcePath + "medication/download", params);
    }

    @Override
    public void run() {
        System.out.println("MedUpdate run at: " + Instant.now());
        UUID uuid = UUID.randomUUID();
        VPNServiceManager vpnm = null;
        try {
            // get the patient cert - this is only available after the first login 
            // and therefore no downloads are attempted before the first login
            patientCertName = System.getProperty(MEDIPIPATIENTCERTNAME);
            if (!medipi.wifiSync.get()) {
                System.out.println("WIFI not available - no polling");
                // Do not try and download anything before wifi is available
            } else if (patientCertName == null || patientCertName.trim().length() == 0) {
                System.out.println("Patient Certificate Name not known");
                // Do not try and download anything before the user password is input for the first time
            } else {
                vpnm = VPNServiceManager.getInstance();
                if (vpnm.isEnabled()) {
                    vpnm.VPNConnection(VPNServiceManager.OPEN, uuid);
                }
                HashMap<String, Object> hs = new HashMap<>();
                hs.put("deviceId", deviceCertName);
                hs.put("patientId", patientCertName);
                Response response = rme.executeGet(hs);
                //
                MedicationDO recievedData = (MedicationDO) response.readEntity(new GenericType<MedicationDO>() {});
                System.out.println(recievedData);
                System.out.println(recievedData.getTestMessage());
                System.out.println(recievedData.getSchedules().get(0).getAssignedStartDate());
                System.out.println(recievedData.getSchedules().get(0).getMedication().getFullName());

            }
        } catch (ProcessingException pe) {
            MediPiLogger.getInstance().log(Synchronizer.class.getName() + ".error", "Attempt to retreive incoming messages has failed - MediPi Concentrator is not available - please try again later. " + pe.getLocalizedMessage());
            MediPiMessageBox.getInstance().makeErrorMessage("Attempt to retreive incoming messages has failed - MediPi Concentrator is not available - please try again later.", pe);
        } catch (Exception e) {
            MediPiLogger.getInstance().log(Synchronizer.class.getName() + ".error", "Error detected when attempting to poll the Concentrator: " + e.getLocalizedMessage());
            MediPiMessageBox.getInstance().makeErrorMessage("Error detected when attempting to poll the Concentrator: " + e.getLocalizedMessage(), e);
        } finally {
            System.out.println("pollFinally1");
            if (vpnm != null && vpnm.isEnabled()) {
                try {
                    System.out.println("pollFinally2");
                    vpnm.VPNConnection(VPNServiceManager.CLOSE, uuid);
                    System.out.println("pollFinally3");
                } catch (Exception ex) {
                    MediPiLogger.getInstance().log(Synchronizer.class.getName(), ex);
                }
            }
        }
    }
}
