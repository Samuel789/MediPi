package org.medipi.medication.medicationui;

import javafx.beans.property.SimpleBooleanProperty;
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

    public MedicationMenu(MediPi mediPi, TileMenu upperMenu, Datastore datastore) {
        super(new WindowManager(), 3, 2.15, upperMenu);
        this.mediPi = mediPi;
        this.datastore = datastore;
        MajorMinorTile backSettingsTile = new MajorMinorTile(new SimpleBooleanProperty(true), 1, 1);
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
        EntityTile recordDoseTile = new EntityTile(new SimpleBooleanProperty(true), 1, 1);
        EntityTile viewScheduleTile = new EntityTile(new SimpleBooleanProperty(true), 1, 1);
        EntityTile showMedicationsTile = new EntityTile(new SimpleBooleanProperty(true), 1, 1);
        recordDoseTile.addTitle("Record a Dose");
        showMedicationsTile.addTitle("Show Medications");
        viewScheduleTile.addTitle("View Schedule");
        recordDoseTile.setOnTileClick((MouseEvent event) -> {
            this.setOverlayWindow(new RecordDoseMenu(mediPi, this));
            this.showOverlayWindow();
        });
        viewScheduleTile.setOnTileClick((MouseEvent event) -> {
            ((MedicationManager) mediPi.getElement("Medication")).getReminderService().triggerTestReminder();
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
