package org.medipi.medication.medicationui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;
import org.medipi.MediPi;
import org.medipi.medication.MedicationManager;
import org.medipi.medication.ScheduledDose;
import org.medipi.ui.*;


public class MedicationMenu extends TileMenu {
    double adherenceRate =0.8;
    MediPi mediPi;
    public MedicationMenu(MediPi mediPi, TileMenu upperMenu) {
        super(new WindowManager(), 3, 2.15, upperMenu);
        this.mediPi = mediPi;
        MajorMinorTile medTile1 = new MajorMinorTile(new SimpleBooleanProperty(true), 1, 1);
        medTile1.setMajorText("Back");
        medTile1.setMinorText("Settings");
        Tile medTile2 = createAdherenceSummaryTile();
        medTile1.setOnMajorClick((MouseEvent event) -> {mediPi.callDashboard();});
        this.addTile(medTile1);
        this.addTile(medTile2);
        EntityTile recordDoseTile = new EntityTile(new SimpleBooleanProperty(true), 1, 1);
        EntityTile viewScheduleTile = new EntityTile(new SimpleBooleanProperty(true), 1, 1);
        EntityTile showMedicationsTile = new EntityTile(new SimpleBooleanProperty(true), 1 ,1);
        recordDoseTile.addTitle("Record a Dose");
        showMedicationsTile.addTitle("Show Medications");
        viewScheduleTile.addTitle("View Schedule");
        recordDoseTile.setOnTileClick((MouseEvent event) -> {this.setOverlayWindow(new RecordDoseMenu(mediPi, this)); this.showOverlayWindow();});
        viewScheduleTile.setOnTileClick((MouseEvent event) -> {((MedicationManager) mediPi.getElement("Medication")).getMedicationReminderService().throwTestReminder();});
        showMedicationsTile.setOnTileClick((MouseEvent event) -> {this.setOverlayWindow(new ShowMedicationsMenu(mediPi, this)); this.showOverlayWindow();});

        this.addTile(recordDoseTile);
        this.addTile(showMedicationsTile);
        this.addTile(viewScheduleTile);
    }

    private Tile createAdherenceSummaryTile() {
        HeaderTile tile = new HeaderTile(new SimpleBooleanProperty(true), 2, 1);

        AdherenceBar adherenceBar = new AdherenceBar();
        tile.setMainContent(adherenceBar);
        tile.setTitleText("Medication Manager");
        adherenceBar.setProgress(adherenceRate);
        adherenceBar.setWidth(450);
        adherenceBar.setLongForm(true);

        return tile;
    }
    public void recordMedicationDose(ScheduledDose dose) {
        this.setOverlayWindow(new RecordDoseMenu(mediPi, this, dose));
        this.showOverlayWindow();
    }
}
