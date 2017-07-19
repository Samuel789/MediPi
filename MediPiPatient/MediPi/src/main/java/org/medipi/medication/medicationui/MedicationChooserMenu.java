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
import org.medipi.MediPi;
import org.medipi.medication.Schedule;
import org.medipi.ui.*;


public class MedicationChooserMenu extends TileMenu {
    double adherenceRate =0.8;
    TileMenu mainPane;
    public MedicationChooserMenu(MediPi medipi, TileMenu upperMenu) {
        super(new WindowManager(), 5, 3.15, upperMenu);
        HeaderTile header = new HeaderTile(new SimpleBooleanProperty(true), 3, 1);
        header.setTitleText("Record a Dose");
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
        ScrollControlTile scTile = new ScrollControlTile(new SimpleBooleanProperty(true), 1, 1);
        scTile.setOnUpClick((MouseEvent event) -> {mainPane.scrollUp();});
        scTile.setOnDownClick((MouseEvent event) -> {mainPane.scrollDown();});
        populateMedicationTiles();
    }

    private void populateMedicationTiles() {
        mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, new Schedule()));
        mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, new Schedule()));
        mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, new Schedule()));
        mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, new Schedule()));
        mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, new Schedule()));
        mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, new Schedule()));
        mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, new Schedule()));
        mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, new Schedule()));
        mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, new Schedule()));
        mainPane.addTile(new MedicationTile(new SimpleBooleanProperty(true), 1, 1, new Schedule()));
    }
}
