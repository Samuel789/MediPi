package org.medipi.medication.ui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import org.medipi.MediPi;
import org.medipi.medication.MedicationManager;
import org.medipi.medication.model.Medication;
import org.medipi.medication.model.Schedule;
import org.medipi.ui.*;

import java.util.*;


public class ShowMedicationsMenu extends TileMenu {
    TileMenu mainPane;
    MediPi medipi;

    public ShowMedicationsMenu(MediPi medipi, TileMenu upperMenu) {
        super(new WindowManager(), 3, 2.15, upperMenu);
        setPadding(new Insets(15, 0, 10, 0));
        DualButtonTile medTile1 = new DualButtonTile(new SimpleBooleanProperty(true), 1, 1);
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
        for (Schedule schedule : ((MedicationManager) medipi.getElement("Medication")).getDatestore().getActiveSchedules()) {
            mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, schedule));
        }
        List<Schedule> sorted_schedules = new ArrayList<>(((MedicationManager) medipi.getElement("Medication")).getDatestore().getUpcomingSchedules());
        sorted_schedules.sort(Comparator.comparing(Schedule::determineDisplayName));
        for (Schedule schedule : sorted_schedules) {
            MedicationTile tile = new MedicationTile(new SimpleBooleanProperty(true), 1, 1, schedule);
            tile.setUpcoming(true);
            mainPane.addTile(tile);
        }
    }
}
