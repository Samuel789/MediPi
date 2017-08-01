package org.medipi.concentrator.services;

import ma.glasnost.orika.MapperFacade;
import net.sf.saxon.functions.Serialize;
import org.medipi.concentrator.dao.*;
import org.medipi.concentrator.exception.NotFound404Exception;
import org.medipi.concentrator.logging.MediPiLogger;
import org.medipi.concentrator.model.DownloadableDO;
import org.medipi.concentrator.utilities.Utilities;
import org.medipi.model.MedicationDO;
import org.medipi.security.CertificateDefinitions;
import org.medipi.security.UploadEncryptionAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class MedicationDownloadService {

    @Autowired
    private Utilities utils;

    @Autowired
    private PatientDeviceValidationService patientDeviceValidationService;

    @Autowired
    private MedicationDAOImpl medicationDAOImpl;

    @Autowired
    private ScheduleDAOImpl scheduleDAOimpl;

    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseEntity<MedicationDO> getMedicationData(String hardware_name, String patientUuid) {
        assert patientDeviceValidationService != null;
        ResponseEntity<?> r = null;
        try {
            // Check that the device and patient are registered with each other
            r = this.patientDeviceValidationService.validate(hardware_name, patientUuid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new NotFound404Exception("Hardware and/or patient not registered" + e.getLocalizedMessage());
        }
        MedicationDO medicationInfo = new MedicationDO();
        medicationInfo.setTestMessage("你好我朋友！我会给你电话！");
        try {
            medicationInfo.setMedications(medicationDAOImpl.findAll());
            medicationInfo.setSchedules(scheduleDAOimpl.findAll());
        } catch (Exception e) {
            System.out.println(String.format("Failed to execute query. Error was %s: %s", e.getClass(), e.getMessage()));
        }
        System.out.println(org.hibernate.Version.getVersionString());
        System.out.println(medicationInfo.getTestMessage());
        try {
        System.out.println(medicationInfo.getSchedules().get(0).getAssignedStartDate());
        System.out.println(medicationInfo.getSchedules().get(0).getMedication().getFullName());

        } catch (Exception e) {
            System.out.println(String.format("Failed to process query results (or none returned). Error was %s: %s", e.getClass(), e.getMessage()));
        }
        // TODO - Embed medication info
        try {
            return new ResponseEntity<MedicationDO>(medicationInfo, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(String.format("Creation of ResponseEntity failed. Error was %s: %s", e.getClass(), e.getMessage()));
        }
        return new ResponseEntity<MedicationDO>(medicationInfo, HttpStatus.OK);
    }
}