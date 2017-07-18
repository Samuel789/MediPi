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
        ButtonTile recordDoseTile = new ButtonTile(new SimpleBooleanProperty(true), 1, 1);
        ButtonTile viewScheduleTile = new ButtonTile(new SimpleBooleanProperty(true), 1, 1);
        ButtonTile showMedicationsTile = new ButtonTile(new SimpleBooleanProperty(true), 1 ,1);
        recordDoseTile.addTitle("Record a Dose");
        showMedicationsTile.addTitle("Show Medications");
        viewScheduleTile.addTitle("View Schedule");
        recordDoseTile.setOnTileClick((MouseEvent event) -> {this.setColumns(this.getColumns() + 1);});
        viewScheduleTile.setOnTileClick((MouseEvent event) -> {this.setColumns(this.getColumns() - 1);});
        showMedicationsTile.setOnTileClick((MouseEvent event) -> {this.setOverlayWindow(new ShowMedicationsMenu(medipi, this)); this.showOverlayWindow();});

        this.addTile(recordDoseTile);
        this.addTile(showMedicationsTile);
        this.addTile(viewScheduleTile);
    }

    private Tile createAdherenceSummaryTile() {
        Tile tile = new Tile(new SimpleBooleanProperty(true), 2, 1);
        BorderPane tileContent = tile.getContent();
        tileContent.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), new Insets(15))));
        tileContent.setPadding(new Insets(30));
        Label titleLabel = new Label("Medication Manager");
        titleLabel.setId("mainwindow-dashboard-component-title");
        tileContent.setTop(titleLabel);
        tileContent.setAlignment(titleLabel, Pos.TOP_CENTER);
        titleLabel.setPadding(new Insets(0, 0, 10, 0));
        tileContent.setAlignment(titleLabel, Pos.TOP_CENTER);
        AdherenceBar adherenceBar = new AdherenceBar();
        tileContent.setCenter(adherenceBar);
        tileContent.setAlignment(adherenceBar, Pos.CENTER);
        adherenceBar.setProgress(adherenceRate);
        adherenceBar.setWidth(450);
        adherenceBar.setLongForm(true);

        return tile;
    }
}
