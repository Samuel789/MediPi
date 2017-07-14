package org.medipi.medication;

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


class MedicationMenu extends TileMenu {
    double adherenceRate =0.8;
    public MedicationMenu(MediPi medipi) {
        super(new CentralScreen(), 3, 2.15);
        MajorMinorTile medTile1 = new MajorMinorTile(new SimpleBooleanProperty(true), 1, 1);
        medTile1.setMajorText("Back");
        medTile1.setMinorText("Settings");
        CustomTile medTile2 = createMedicationTile();
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

    private CustomTile createMedicationTile() {
        CustomTile tile = new CustomTile(new SimpleBooleanProperty(true), 2, 1);
        BorderPane tileContent = tile.getTileContent();
        tileContent.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), new Insets(15))));
        tileContent.setPadding(new Insets(30));
        Label titleLabel = new Label("Medication Manager");
        titleLabel.setId("mainwindow-dashboard-component-title");
        tileContent.setTop(titleLabel);
        tileContent.setAlignment(titleLabel, Pos.CENTER);
        AdherenceBar adherenceBar = new AdherenceBar();
        tileContent.setCenter(adherenceBar);
        adherenceBar.setPrefWidth(400);
        adherenceBar.setPrefHeight(40);
        adherenceBar.setProgress(adherenceRate);
        adherenceBar.setStyle("-fx-accent: yellow;");
        Label adherenceText = new Label(String.format("%d%% overall adherence in the last 7 days", (int) (adherenceRate*100)));
        adherenceText.setId("mainwindow-dashboard-component-title");
        tileContent.setBottom(adherenceText);
        tileContent.setAlignment(adherenceText, Pos.CENTER);
        return tile;
    }
}
