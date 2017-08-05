package org.medipi.medication.medicationui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.medipi.medication.Schedule;

import java.io.File;
import java.net.MalformedURLException;


public class MedicationInformation extends Group {
    Label titleLabel;
    Label fullNameLabel;
    Label purposeLabel;
    TextArea advisoryTextArea;
    AdherenceBar adherenceBar;
    Image iconImage;
    ImageView iconImageView;

    BorderPane content;
    public MedicationInformation(Schedule schedule) {
        content = new BorderPane();
        this.getChildren().add(content);
        content.setPrefWidth(550);
        content.setPrefHeight(300);
        titleLabel = new Label(schedule.getDisplayName());
        fullNameLabel = new Label(schedule.getMedication().getFullName());
        purposeLabel = new Label(schedule.getPurposeStatement());
        advisoryTextArea = new TextArea(schedule.getMedication().getCautionaryText());
        adherenceBar = new AdherenceBar();
        adherenceBar.setProgress(0.4);
        adherenceBar.setStreakLength(12);
        try {
            iconImage = new Image(new File(schedule.getMedication().getIconName()).toURL().toString());

        } catch (Exception e) {
            System.out.println("Couldn't load icon");
        }
        iconImageView = new ImageView(iconImage);
        iconImageView.setFitWidth(100);
        iconImageView.setFitHeight(100);
        titleLabel.setId("mainwindow-dashboard-component-title");
        content.setPadding(new Insets(10));
        content.setCenter(titleLabel);


        BorderPane topPane = new BorderPane();
        content.setTop(topPane);
        topPane.setLeft(titleLabel);
        Button closeButton = new Button();
        closeButton.setOnMouseClicked((MouseEvent event) -> {((Stage)this.getScene().getWindow()).close();});
        closeButton.setText("Back");
        closeButton.setPrefHeight(40);
        closeButton.setPrefWidth(140);
        topPane.setAlignment(titleLabel, Pos.CENTER_LEFT);
        topPane.setRight(closeButton);

        VBox centre = new VBox();
        centre.getChildren().add(fullNameLabel);
        centre.getChildren().add(purposeLabel);
        HBox iconAdvisoryBox = new HBox();
        iconAdvisoryBox.getChildren().add(advisoryTextArea);
        iconAdvisoryBox.getChildren().add(iconImageView);

        advisoryTextArea.setEditable(false);
        iconAdvisoryBox.setAlignment(Pos.CENTER);
        centre.getChildren().add(iconAdvisoryBox);
        content.setCenter(centre);

        content.setBottom(adherenceBar);
        content.setAlignment(adherenceBar, Pos.CENTER);
    }
}
