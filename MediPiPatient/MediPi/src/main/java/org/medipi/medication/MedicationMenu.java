package org.medipi.medication;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.medipi.MediPi;
import org.medipi.ui.CentralScreen;
import org.medipi.ui.Tile;
import org.medipi.ui.TileMenu;


class MedicationMenu extends TileMenu {
    public MedicationMenu(MediPi medipi) {
        super(new CentralScreen(), 3, 2);
        Tile medTile = new Tile(new SimpleBooleanProperty(true), 1, 1);
        medTile.addTitle("Back");
        Tile medTile2 = new Tile(new SimpleBooleanProperty(true), 2, 1);
        medTile2.addTitle("Blah");
        medTile.setOnMouseClicked((MouseEvent event) -> {medipi.callDashboard();});
        this.addTile(medTile);
        this.addTile(medTile2);
    }
}
