package org.medipi.medication;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.medipi.MediPi;
import org.medipi.medication.medicationui.MedicationReminder;

public class MedicationReminderService {
    Scene parentScene;
    MediPi mediPi;
    public MedicationReminderService(MediPi mediPi) {
        this.mediPi = mediPi;
        this.parentScene = mediPi.scene;
    }
    private void throwReminder(ScheduledDose dose) {
        Stage popupWindow = new Stage();
        MedicationReminder popupContents = new MedicationReminder(dose, mediPi);
        Scene popupScene = new Scene(popupContents);
        parentScene.getStylesheets().addAll(popupScene.getStylesheets());
        popupWindow.setScene(popupScene);
        popupWindow.setTitle("Medication Information");
        popupWindow.initModality(Modality.WINDOW_MODAL);
        popupWindow.initOwner(parentScene.getWindow());
        popupWindow.showAndWait();
    }
    public void throwTestReminder() {
        MedicationManager medicationManager =(MedicationManager) mediPi.getElement("Medication");
        System.out.println(medicationManager);
        throwReminder(
                medicationManager.getDatestore()
                        .getPatientSchedules()
                        .get(0)
                        .getScheduledDoses()
                        .iterator()
                        .next());
    }
}
