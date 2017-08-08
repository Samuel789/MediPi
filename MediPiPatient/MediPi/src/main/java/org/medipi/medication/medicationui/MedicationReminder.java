package org.medipi.medication.medicationui;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.medipi.MediPi;
import org.medipi.medication.Medication;
import org.medipi.medication.MedicationManager;
import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;

import java.io.File;

public class MedicationReminder extends Group {
    VBox content;
    public MedicationReminder(ScheduledDose dose, MediPi mediPi) {
        content = new VBox();
        this.getChildren().add(content);
        Schedule schedule = dose.getSchedule();
        Medication medication = schedule.getMedication();
        Label titleLabel = new Label("Medication Reminder");
        Label reminderLabel = new Label("It's time to take " + schedule.determineDisplayName());
        Label doseLabel = new Label("Dose: " + dose.getDoseValue() + " " + medication.getDoseUnit().getName());
        Label fullNameLabel = new Label("(" + medication.getFullName() + ")");
        TextArea advisoryTextArea = new TextArea(medication.getCautionaryText());
        advisoryTextArea.setEditable(false);
        Button takeButton = new Button("Take Now!");
        takeButton.getStyleClass().add("button-advised");
        Button delayButton = new Button("Delay");
        delayButton.getStyleClass().add("button-neutral");
        Button dismissButton = new Button("Dismiss");
        dismissButton.getStyleClass().add("button-caution");
        Image iconImage;
        ImageView iconImageView;

        HBox iconSeparator = new HBox();
        VBox topVBox = new VBox();
        HBox.setHgrow(topVBox, Priority.ALWAYS);
        iconSeparator.getChildren().add(topVBox);
        topVBox.getChildren().add(titleLabel);
        topVBox.getChildren().add(reminderLabel);
        content.getChildren().add(iconSeparator);
        VBox iconFullNameVBox = new VBox();
        iconFullNameVBox.setAlignment(Pos.CENTER);
        iconSeparator.getChildren().add(iconFullNameVBox);
        content.getChildren().add(doseLabel);
        content.getChildren().add(advisoryTextArea);
        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(takeButton);
        buttonBox.getChildren().add(delayButton);
        buttonBox.getChildren().add(dismissButton);
        dismissButton.setOnMouseClicked((MouseEvent event) -> {((Stage)this.getScene().getWindow()).close();});
        takeButton.setOnMouseClicked((MouseEvent event) -> {
            ((MedicationManager)mediPi.getElement("Medication")).recordMedicationDose(dose);
            ((Stage)this.getScene().getWindow()).close();});
        content.getChildren().add(buttonBox);

        try {
            iconImage = new Image(new File("/home/sam/Pictures/TommyWHead.png").toURL().toString());
            iconImageView = new ImageView(iconImage);
            iconFullNameVBox.getChildren().add(iconImageView);
            iconImageView.setFitWidth(100);
            iconImageView.setFitHeight(100);
        } catch (Exception e) {
            System.out.println("Couldn't load icon");
        }
        iconFullNameVBox.getChildren().add(fullNameLabel);

    }
}
