package org.medipi.medication.medicationui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.medipi.MediPi;
import org.medipi.medication.DoseUnit;
import org.medipi.medication.Schedule;
import org.medipi.ui.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Vector;


public class RecordDoseMenu extends TileMenu {
    double adherenceRate =0.8;
    TileMenu mainPane;
    Vector<Schedule> schedules;
    MediPi mediPi;
    public RecordDoseMenu(MediPi medipi, TileMenu upperMenu) {
        super(new WindowManager(), 5, 3.15, upperMenu);
        this.mediPi = medipi;
        HeaderTile header = new HeaderTile(new SimpleBooleanProperty(true), 3, 1);
        header.setTitleText("Recording a Dose");
        ButtonTile backButton = new ButtonTile(new SimpleBooleanProperty(true), 2, 1);
        backButton.setText("Back");
        backButton.setOnButtonClick((MouseEvent event) -> {upperMenu.closeOverlayWindow();});
        header.setMainContent(new Label("First, tap the medication you've taken"));
        this.addTile(backButton);
        this.addTile(header);

        ScrollControlTile sidebarTile = new ScrollControlTile(new SimpleBooleanProperty(true), 1, 2);
        mainPane = new TileMenu(getUnitWidth()*4, getUnitHeight()*2, 2, 2.2, this);
        Tile mainPaneTile = new Tile(new SimpleBooleanProperty(true), 4, 2);
        mainPaneTile.getContent().setCenter(mainPane);
        this.addTile(sidebarTile);
        this.addTile(mainPaneTile);
        sidebarTile.setOnUpClick((MouseEvent event) -> {mainPane.scrollUp();});
        sidebarTile.setOnDownClick((MouseEvent event) -> {mainPane.scrollDown();});
        populateMedicationTiles();
    }

    private void populateMedicationTiles() {
        schedules = new Vector<>();
        schedules.add(new Schedule());
        schedules.add(new Schedule());
        schedules.add(new Schedule());
        schedules.add(new Schedule());
        schedules.add(new Schedule());
        schedules.add(new Schedule());
        schedules.add(new Schedule());
        schedules.add(new Schedule());
        schedules.add(new Schedule());
        for (Schedule schedule: schedules) {
            MedicationTile newTile = new MedicationTile(new SimpleBooleanProperty(true), 1, 1, schedule);
            mainPane.addTile(newTile);
            newTile.setDisplayType(MedicationTile.DisplayType.DUESTATUS);
            newTile.setOnTileClick((MouseEvent event) -> {selectTile(newTile);});
        }
    }

    public void selectTile(MedicationTile tile) {
        Schedule selectedSchedule = tile.getMedicationSchedule();
        setOverlayWindow(new DoseDetailsScreen(mediPi, this, tile.getMedicationSchedule()));
        showOverlayWindow();

    }
}

class DoseDetailsScreen extends TileMenu {
    Label doseDescriptor;
    LocalDate doseDay;
    LocalTime doseTime;
    double doseValue;
    DoseUnit doseUnit;
    String medicationName;

    public DoseDetailsScreen(MediPi medipi, TileMenu upperMenu, Schedule medicationSchedule) {
        super(new WindowManager(), 8, 3.15, upperMenu);
        HeaderTile header = new HeaderTile(new SimpleBooleanProperty(true), 5, 1);
        header.setTitleText("Recording a Dose");
        ButtonTile backButton = new ButtonTile(new SimpleBooleanProperty(true), 3, 1);
        backButton.setText("Back");
        backButton.setOnButtonClick((MouseEvent event) -> {
            upperMenu.closeOverlayWindow();
        });
        header.setMainContent(new Label("Now, make sure the dose and time are correct"));
        doseUnit = medicationSchedule.getMedication().getDoseUnit();
        medicationName = medicationSchedule.getDisplayName();
        doseDay = LocalDate.now();
        doseTime = LocalTime.now();
        doseValue = 0.1;
        doseDescriptor = new Label();
        generateDoseString();


        Tile centralTile = new Tile(new SimpleBooleanProperty(true), 8, 1);
        centralTile.setContent(new BorderPane());
        centralTile.getContent().setCenter(doseDescriptor);
        doseDescriptor.setId("mainwindow-dashboard-component-title");
        centralTile.getContent().setId("mainwindow-dashboard-component");
        centralTile.getContent().setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));

        ButtonTile doseButton = new ButtonTile(new SimpleBooleanProperty(true), 2, 1);
        doseButton.setText("Change Dose");
        ButtonTile timeButton = new ButtonTile(new SimpleBooleanProperty(true), 2, 1);
        timeButton.setText("Change Time");
        ButtonTile saveButton = new ButtonTile(new SimpleBooleanProperty(true), 2, 1);
        saveButton.setText("Save");

        addTile(backButton);
        addTile(header);

        addTile(centralTile);

        addBufferTile(1, 1);
        addTile(doseButton);
        addTile(timeButton);
        addBufferTile(1, 1);
        addTile(saveButton);
    }

    private void generateDoseString() {
        doseDescriptor.setText(String.format("Recording %f %s(s) of %s taken %s at %s", doseValue, doseUnit.getName(), medicationName, doseDay, doseTime));
    }
}