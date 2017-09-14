package org.medipi.medication.ui;

import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.medipi.MediPi;
import org.medipi.medication.model.Medication;
import org.medipi.medication.MedicationManager;
import org.medipi.medication.model.Schedule;
import org.medipi.medication.model.ScheduledDose;
import org.medipi.medication.reminders.MedicationReminderEvent;
import org.medipi.medication.reminders.ReminderService;
import org.medipi.medication.reminders.SnoozeNotAllowedException;

import java.io.File;

public class MedicationReminder extends Group {
    VBox content;
    ReminderService reminderService;
    private static final int SNOOZEDURATION = 600;

    public MedicationReminder(MedicationReminderEvent reminderEvent, MediPi mediPi, Stage window) {
        ScheduledDose dose = reminderEvent.getDose();
        reminderService = ((MedicationManager) mediPi.getElement("Medication")).getReminderService();
        content = new VBox();
        this.getChildren().add(content);
        Schedule schedule = dose.getSchedule();
        Medication medication = schedule.getMedication();
        content.setPadding(new Insets(10));
        content.setMaxWidth(mediPi.getMediPiWindow().getWidth());
        content.setStyle("-fx-border-color: deepskyblue; -fx-border-width: 2px");
        Label titleLabel = new Label("Medication Reminder");
        titleLabel.getStyleClass().add("title-text");
        Label reminderLabel = new Label("It's time to take " + schedule.determineDisplayName());
        reminderLabel.getStyleClass().add("heading-text");
        Label doseLabel = new Label("Dose: " + formatNumber(dose.getDoseValue()) + " " + medication.getDoseUnit().getName());
        doseLabel.getStyleClass().add("heading-text");
        doseLabel.setPadding(new Insets(5));
        Label fullNameLabel = new Label("(" + medication.getFullName() + ")");
        fullNameLabel.getStyleClass().add("heading-text");
        TextArea advisoryTextArea = new TextArea(medication.getCautionaryText());
        advisoryTextArea.getStyleClass().add("heading-text");
        advisoryTextArea.setEditable(false);
        advisoryTextArea.setMaxWidth(780);
        advisoryTextArea.setMinWidth(780);
        Button takeButton = new Button("Take Now!");
        takeButton.getStyleClass().addAll("button-advised", "mp-button", "button-large");
        Button delayButton = new Button("Delay (10 mins)");
        delayButton.getStyleClass().addAll("mp-button", "button-large");
        if (reminderEvent.isSnoozeAllowed(SNOOZEDURATION)) {
            delayButton.getStyleClass().add("button-neutral");
        } else {
            delayButton.getStyleClass().add("button-disabled");
        }
        delayButton.getStyleClass().addAll("button-neutral", "mp-button", "button-large");
        Button dismissButton = new Button("Dismiss");
        dismissButton.getStyleClass().addAll("button-caution", "mp-button", "button-large");
        Button confirmDismissButton = new Button("Yes, Dismiss");
        confirmDismissButton.getStyleClass().addAll("button-caution", "mp-button", "button-large");
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
        buttonBox.setPadding(new Insets(5));
        confirmDismissButton.setOnMouseClicked((MouseEvent event) -> {
            ((Stage) this.getScene().getWindow()).close();
            reminderService.dismissEvent(reminderEvent);
        });
        delayButton.setOnMouseClicked((MouseEvent event) -> {
            try {
                reminderEvent.snoozeForSeconds(SNOOZEDURATION);
                ((Stage) this.getScene().getWindow()).close();
            } catch (SnoozeNotAllowedException e) {
                delayButton.getStyleClass().add("button-disabled");
            }
        });
        dismissButton.setOnMouseClicked((MouseEvent event) -> {
            buttonBox.getChildren().remove(dismissButton);
            buttonBox.getChildren().add(confirmDismissButton);
            Label dismissWarningLabel = new Label("Are you sure you wish to dismiss this reminder without taking your medication?");
            dismissWarningLabel.setPadding(new Insets(10, 0, 10, 0));
            dismissWarningLabel.getStyleClass().add("heading-text");
            content.getScene().getWindow().setHeight(content.getHeight() + 4);
            content.getChildren().add(content.getChildren().indexOf(advisoryTextArea), dismissWarningLabel);
            content.getChildren().remove(advisoryTextArea);
            window.setHeight(280);
        });
        System.out.println("REMINDER - DoseId " + dose.getScheduledDoseId());
        takeButton.setOnMouseClicked((MouseEvent event) -> {
            ((MedicationManager) mediPi.getElement("Medication")).recordMedicationDose(dose);
            ((Stage) this.getScene().getWindow()).close();
        });
        content.getChildren().add(buttonBox);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(15);

        try {
            iconImage = new Image(new File("/home/sam/Pictures/medication.png").toURL().toString());
            iconImageView = new ImageView(iconImage);
            iconFullNameVBox.getChildren().add(iconImageView);
            iconImageView.setFitWidth(100);
            iconImageView.setFitHeight(100);
        } catch (Exception e) {
            System.out.println("Couldn't load icon");
        }
        iconFullNameVBox.getChildren().add(fullNameLabel);
    }

    private static String formatNumber(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }
}
