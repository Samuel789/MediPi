package org.medipi.medication.medicationui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.MouseEvent;
import org.medipi.MediPi;
import org.medipi.medication.MedicationManager;
import org.medipi.medication.Schedule;
import org.medipi.ui.*;


public class ShowMedicationsMenu extends TileMenu {
    TileMenu mainPane;
    MediPi medipi;

    public ShowMedicationsMenu(MediPi medipi, TileMenu upperMenu) {
        super(new WindowManager(), 3, 2.15, upperMenu);
        MajorMinorTile medTile1 = new MajorMinorTile(new SimpleBooleanProperty(true), 1, 1);
        medTile1.setMajorText("Back");
        this.medipi = medipi;
        medTile1.setOnMajorClick((MouseEvent event) -> {
            upperMenu.closeOverlayWindow();
        });
        medTile1.setMinorText("Help");
        System.out.println(getUnitWidth());
        System.out.println(this.getTargetWidth());
        TileMenu sidebar = new TileMenu(getUnitWidth(), getUnitHeight() * 2, 1, 2.2, this);
        Tile sidebarTile = new Tile(new SimpleBooleanProperty(true), 1, 2);
        mainPane = new TileMenu(getUnitWidth() * 2, getUnitHeight() * 2, 2, 2.2, this);
        Tile mainPaneTile = new Tile(new SimpleBooleanProperty(true), 2, 2);
        sidebarTile.getContent().setCenter(sidebar);
        mainPaneTile.getContent().setCenter(mainPane);
        this.addTile(sidebarTile);
        this.addTile(mainPaneTile);
        sidebar.addTile(medTile1);
        ScrollControlTile scTile = new ScrollControlTile(new SimpleBooleanProperty(true), 1, 1);
        scTile.setOnUpClick((MouseEvent event) -> {
            mainPane.scrollUp();
        });
        scTile.setOnDownClick((MouseEvent event) -> {
            mainPane.scrollDown();
        });
        sidebar.addTile(scTile);
        sidebar.setMargin(0);
        populateMedicationTiles();
    }

    private void populateMedicationTiles() {
        for (Schedule schedule : ((MedicationManager) medipi.getElement("Medication")).getDatestore().getPatientSchedules()) {
            mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, schedule));
        }
    }
}
