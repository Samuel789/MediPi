package org.medipi.medication;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;
import org.medipi.MediPi;
import org.medipi.ui.*;


class MedicationMenu extends TileMenu {
    public MedicationMenu(MediPi medipi) {
        super(new CentralScreen(), 3, 2.15);
        MajorMinorTile medTile1 = new MajorMinorTile(new SimpleBooleanProperty(true), 1, 1);
        medTile1.setMajorText("Back");
        medTile1.setMinorText("Settings");
        ButtonTile medTile2 = new ButtonTile(new SimpleBooleanProperty(true), 2, 1);
        medTile2.addTitle("Medication Manager");
        medTile1.setOnMajorClick((MouseEvent event) -> {medipi.callDashboard();});
        this.addTile(medTile1);
        this.addTile(medTile2);
        ButtonTile recordDoseTile = new ButtonTile(new SimpleBooleanProperty(true), 1, 1);
        ButtonTile viewScheduleTile = new ButtonTile(new SimpleBooleanProperty(true), 1, 1);
        ButtonTile showMedicationsTile = new ButtonTile(new SimpleBooleanProperty(true), 1 ,1);
        recordDoseTile.addTitle("Record a Dose");
        showMedicationsTile.addTitle("Show Medications");
        viewScheduleTile.addTitle("View Schedule");
        this.addTile(recordDoseTile);
        this.addTile(showMedicationsTile);
        this.addTile(viewScheduleTile);
    }
}
