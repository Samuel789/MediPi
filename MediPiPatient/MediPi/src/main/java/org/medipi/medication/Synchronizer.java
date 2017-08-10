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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.medipi.MediPi;
import org.medipi.MediPiMessageBox;
import org.medipi.logging.MediPiLogger;
import org.medipi.messaging.rest.RESTfulMessagingEngine;
import org.medipi.messaging.vpn.VPNServiceManager;
import org.medipi.model.MedicationDO;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
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
    private final String deviceCertName;
    private final String resourcePath;
    private final MediPi medipi;

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

    }

    private MedicationDO downloadScheduleData() throws Exception {
        RESTfulMessagingEngine rme = new RESTfulMessagingEngine(resourcePath + "medication/download", new String[] {"{deviceId}", "{patientId}"});
        UUID uuid = UUID.randomUUID();
        VPNServiceManager vpnm = null;
        String patientCertName = System.getProperty(MEDIPIPATIENTCERTNAME);
        HashMap<String, Object> params = new HashMap<>();
        params.put("deviceId", deviceCertName);
        params.put("patientId", patientCertName);
        if (!medipi.wifiSync.get()) {
            throw new RuntimeException("No wifi connection");
        }
        if (patientCertName == null || patientCertName.trim().length() == 0) {
            throw new RuntimeException("Patient certificate is not available");
        }
        vpnm = VPNServiceManager.getInstance();
        if (vpnm.isEnabled()) {
            vpnm.VPNConnection(VPNServiceManager.OPEN, uuid);
        }
        System.out.println(params);
        Response response = rme.executeGet(params);
        //
        String jsonResponse = response.readEntity(String.class);
        System.out.println(jsonResponse);
        MedicationDO recievedData = new ObjectMapper().readValue(jsonResponse, MedicationDO.class);

        return recievedData;

    }

    private List<Schedule> processScheduleData(MedicationDO recievedData) {
        recievedData.recreateReferences();
        List<Schedule> newSchedules = recievedData.getSchedules();
        return newSchedules;
    }

    private void uploadDoseData(MedicationDO doses) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        String patientCertName = System.getProperty(MEDIPIPATIENTCERTNAME);
        params.put("deviceId", deviceCertName);
        params.put("patientId", patientCertName);
        RESTfulMessagingEngine rme = new RESTfulMessagingEngine(resourcePath + "medication/upload", new String[] {});
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Data-Format", "MediPiNative");
        for (Schedule schedule: doses.getSchedules()) {
            for (RecordedDose dose: schedule.getRecordedDoses()) {
                System.out.println("TIMESTAMP DEBUG FOR " + dose.getRecordedDoseUUID() + ":");
                System.out.println("STAMP: " + dose.getTimeTaken().getTime());
                System.out.println("TO LOCALDATETIME: " + dose.getTimeTaken().toLocalDateTime());
                System.out.println("TO LONDON ZONE: " + dose.getTimeTaken().toLocalDateTime().atZone(TimeZone.getTimeZone("Europe/London").toZoneId()));
                System.out.println("--STAMP: " + dose.getTimeTaken().toLocalDateTime().atZone(TimeZone.getTimeZone("Europe/London").toZoneId()).getNano()/1000);
                System.out.println("TO DEFAULT ZONE: " + dose.getTimeTaken().toLocalDateTime().atZone(TimeZone.getDefault().toZoneId()));
                System.out.println("--STAMP: " + dose.getTimeTaken().toLocalDateTime().atZone(TimeZone.getDefault().toZoneId()).getNano()/1000);
                System.out.println("TO UTC ZONE: " + dose.getTimeTaken().toLocalDateTime().atZone(ZoneOffset.UTC));
                System.out.println("--STAMP: " + dose.getTimeTaken().toLocalDateTime().atZone(ZoneOffset.UTC).getNano()/1000);
            }
        }
        Response postResponse = rme.executePut(params, Entity.json(doses), headers);
        System.out.println(postResponse.getStatusInfo());
    }

    @Override
    public void run() {
        MedicationManager medicationManager = (MedicationManager) medipi.getElement("Medication");
        Datastore datastore = medicationManager.getDatestore();
        System.out.println("MedUpdate run at: " + Instant.now());
        try {
            MedicationDO uploadData = new MedicationDO();
            uploadData.setSchedules(datastore.getPatientSchedules());
            uploadDoseData(uploadData);
            System.out.println("Sent upload data");
            MedicationDO recievedData = downloadScheduleData();
            List<Schedule> schedules = processScheduleData(recievedData);
            datastore.replacePatientSchedules(schedules);
            datastore.setPatientAdherence(recievedData.getPatientAdherence());
            medicationManager.reload();

        }  catch (ProcessingException pe) {
            MediPiLogger.getInstance().log(Synchronizer.class.getName() + ".error", "Attempt to synchronize medication data has failed - MediPi Concentrator is not available - please try again later. " + pe.getLocalizedMessage());
            MediPiMessageBox.getInstance().makeErrorMessage("Attempt to synchronize medication data has failed - MediPi Concentrator is not available - please try again later. " + pe.getLocalizedMessage(), pe);
        } catch (Exception e) {
            MediPiLogger.getInstance().log(Synchronizer.class.getName() + ".error", "Error detected when attempting to synchronize medication data: " + e.getLocalizedMessage());
            MediPiMessageBox.getInstance().makeErrorMessage("Error detected when attempting to synchronize medication data: " + e.getLocalizedMessage(), e);
        }
    }
}
