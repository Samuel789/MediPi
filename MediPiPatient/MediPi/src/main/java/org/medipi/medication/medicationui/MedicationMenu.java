package org.medipi.medication.medicationui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import org.medipi.MediPi;
import org.medipi.medication.Datastore;
import org.medipi.medication.MedicationManager;
import org.medipi.medication.ScheduledDose;
import org.medipi.ui.*;


public class MedicationMenu extends TileMenu {
    MediPi mediPi;
    Datastore datastore;
    HeaderTile headerTile;

    public void setSynched(boolean synched) {
        this.synched = synched;
        loadPatientAdherenceStatistics();
    }

    boolean synched = false;

    public MedicationMenu(MediPi mediPi, TileMenu upperMenu, Datastore datastore) {
        super(new WindowManager(), 3, 2.15, upperMenu);
        this.mediPi = mediPi;
        this.datastore = datastore;
        DualButtonTile backSettingsTile = new DualButtonTile(new SimpleBooleanProperty(true), 1, 1);
        backSettingsTile.setMajorText("Back");
        backSettingsTile.setMinorText("< Synchronize >");
        headerTile = createAdherenceHeaderTile();
        backSettingsTile.setOnMajorClick((MouseEvent event) -> {
            mediPi.callDashboard();
        });
        backSettingsTile.setOnMinorClick((MouseEvent event) -> {
            ((MedicationManager) mediPi.getElement("Medication")).getSynchronizer().run();
        });
        this.addTile(backSettingsTile);
        this.addTile(headerTile);
        MajorButtonTile recordDoseTile = new MajorButtonTile(new SimpleBooleanProperty(true), 1, 1);
        MajorButtonTile viewScheduleTile = new MajorButtonTile(new SimpleBooleanProperty(true), 1, 1);
        MajorButtonTile showMedicationsTile = new MajorButtonTile(new SimpleBooleanProperty(true), 1, 1);
        recordDoseTile.addTitle("Record a Dose");
        recordDoseTile.setBackgroundImage(mediPi.utils.getImageView(MediPi.ELEMENTNAMESPACESTEM + "Medication" + ".recordDoseImage", null, null, false));
        showMedicationsTile.addTitle("Show Medications");
        showMedicationsTile.setBackgroundImage(mediPi.utils.getImageView(MediPi.ELEMENTNAMESPACESTEM + "Medication" + ".showMedicationsImage", null, null, false));
        viewScheduleTile.addTitle("View Schedule");
        viewScheduleTile.setBackgroundImage(mediPi.utils.getImageView(MediPi.ELEMENTNAMESPACESTEM + "Medication" + ".viewScheduleImage", null, null, false));
        recordDoseTile.setOnTileClick((MouseEvent event) -> {
            this.setOverlayWindow(new RecordDoseMenu(mediPi, this));
            this.showOverlayWindow();
        });
        viewScheduleTile.setOnTileClick((MouseEvent event) -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Feature not implemented");
            alert.setContentText("Sorry, this feature is not yet implemented.");
            alert.showAndWait();
        });
        showMedicationsTile.setOnTileClick((MouseEvent event) -> {
            this.setOverlayWindow(new ShowMedicationsMenu(mediPi, this));
            this.showOverlayWindow();
        });

        this.addTile(recordDoseTile);
        this.addTile(showMedicationsTile);
        this.addTile(viewScheduleTile);
        loadPatientAdherenceStatistics();
    }

    private void loadPatientAdherenceStatistics() {
        AdherenceBar adherenceBar = new AdherenceBar(datastore.getPatientAdherence());
        headerTile.setMainContent(adherenceBar);
        adherenceBar.setWidth(450);
        adherenceBar.setLongForm(true);
        adherenceBar.setUnSynched(!synched);
    }

    public void reload() {
        loadPatientAdherenceStatistics();
    }

    private HeaderTile createAdherenceHeaderTile() {
        HeaderTile tile = new HeaderTile(new SimpleBooleanProperty(true), 2, 1);
        tile.setTitleText("Medication Manager");
        return tile;
    }

    public void recordMedicationDose(ScheduledDose dose) {
        this.setOverlayWindow(new RecordDoseMenu(mediPi, this, dose));
        this.showOverlayWindow();
    }
}
