package org.medipi.medication.medicationui;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.medipi.medication.Schedule;
import org.medipi.ui.Tile;

import java.util.Random;

public class MedicationTile extends Tile {

    Label titleLabel;
    Label purposeLabel;
    AdherenceBar adherenceBar;

    public MedicationTile(BooleanProperty bprop, int widthUnits, int heightUnits, Schedule medicationSchedule) {
        super(bprop, widthUnits, heightUnits);
        content.setId("mainwindow-dashboard-component");
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));

        content.setPadding(new Insets(5, 5, 5, 5));

        VBox vbox = new VBox();
        vbox.setFillWidth(true);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(0, 10, 0, 10));
        content.setTop(vbox);
        content.setAlignment(vbox, Pos.CENTER_LEFT);

        titleLabel = new Label("No title set");
        //titleLabel.setPrefSize(content.getWidth(), 30);
        titleLabel.setId("mainwindow-dashboard-component-title");

        purposeLabel = new Label("No purpose set");
        //purposeLabel.setPrefSize(content.getWidth(), 30);

        adherenceBar = new AdherenceBar();

        vbox.getChildren().add(titleLabel);
        vbox.getChildren().add(purposeLabel);
        content.setCenter(adherenceBar);
        content.setAlignment(adherenceBar, Pos.CENTER);
        adherenceBar.setWidth(200);


        setMedicationSchedule(medicationSchedule);
    }

    public void setMedicationSchedule(Schedule schedule) {
        adherenceBar.setProgress(new Random().nextDouble() % 1);
        adherenceBar.setStreakLength((int) ((new Random().nextDouble() % 1)*20));
        this.setOnTileClick((MouseEvent event) -> {
            Stage popupWindow = new Stage();
            MedicationInformation popupContents = new MedicationInformation(schedule);
            Scene scene = new Scene(popupContents);
            scene.getStylesheets().addAll(content.getScene().getStylesheets());
            popupWindow.setScene(scene);
            popupWindow.setTitle("Medication Information");
            popupWindow.initModality(Modality.WINDOW_MODAL);
            popupWindow.initOwner(content.getScene().getWindow());
            popupWindow.showAndWait();
        });
    }

}
