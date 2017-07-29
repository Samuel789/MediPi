package org.medipi.concentrator.services;

import ma.glasnost.orika.MapperFacade;
import org.medipi.concentrator.dao.AllHardwareDownloadableDAOImpl;
import org.medipi.concentrator.dao.HardwareDownloadableDAOImpl;
import org.medipi.concentrator.dao.MedicationDAOImpl;
import org.medipi.concentrator.dao.PatientDownloadableDAOImpl;
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
        medicationInfo.setMedications(medicationDAOImpl.findAll());
        System.out.println(medicationInfo.getTestMessage());
        System.out.println(medicationInfo.getMedications().get(0).getCautionaryText());
        // TODO - Embed medication info
        return new ResponseEntity<MedicationDO>(medicationInfo, HttpStatus.OK);
    }
}