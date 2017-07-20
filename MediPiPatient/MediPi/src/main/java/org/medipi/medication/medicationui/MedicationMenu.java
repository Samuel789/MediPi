package org.medipi.medication.medicationui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;
import org.medipi.MediPi;
import org.medipi.ui.*;


public class MedicationMenu extends TileMenu {
    double adherenceRate =0.8;
    public MedicationMenu(MediPi medipi, TileMenu upperMenu) {
        super(new WindowManager(), 3, 2.15, upperMenu);
        MajorMinorTile medTile1 = new MajorMinorTile(new SimpleBooleanProperty(true), 1, 1);
        medTile1.setMajorText("Back");
        medTile1.setMinorText("Settings");
        Tile medTile2 = createAdherenceSummaryTile();
        medTile1.setOnMajorClick((MouseEvent event) -> {medipi.callDashboard();});
        this.addTile(medTile1);
        this.addTile(medTile2);
        EntityTile recordDoseTile = new EntityTile(new SimpleBooleanProperty(true), 1, 1);
        EntityTile viewScheduleTile = new EntityTile(new SimpleBooleanProperty(true), 1, 1);
        EntityTile showMedicationsTile = new EntityTile(new SimpleBooleanProperty(true), 1 ,1);
        recordDoseTile.addTitle("Record a Dose");
        showMedicationsTile.addTitle("Show Medications");
        viewScheduleTile.addTitle("View Schedule");
        recordDoseTile.setOnTileClick((MouseEvent event) -> {this.setOverlayWindow(new RecordDoseMenu(medipi, this)); this.showOverlayWindow();});
        showMedicationsTile.setOnTileClick((MouseEvent event) -> {this.setOverlayWindow(new ShowMedicationsMenu(medipi, this)); this.showOverlayWindow();});

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
}
