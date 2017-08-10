package org.medipi.medication.medicationui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.medipi.medication.PatientAdherence;
import org.medipi.medication.ScheduleAdherence;

public class AdherenceBar extends Group {
    ProgressBar progressBar;
    Label adherenceText;
    HBox hbox;
    VBox content;
    Label streakNumber;
    Label streakText;
    Integer streakLength;
    Double progress;

    public boolean isLongForm() {
        return longForm;
    }

    public void setLongForm(boolean longForm) {
        this.longForm = longForm;
        updateLabel();
    }

    boolean longForm = false;

    public AdherenceBar(ScheduleAdherence scheduleAdherence){
        this(scheduleAdherence.getSevenDayFraction(), scheduleAdherence.getStreakLength());
    }

    public AdherenceBar(PatientAdherence patientAdherence){
        this(patientAdherence.getSevenDayFraction(), patientAdherence.getStreakLength());
    }

    public AdherenceBar(Double sevenDayFraction, Integer streakLength) {
        content = new VBox();
        hbox = new HBox();
        adherenceText = new Label();
        streakText = new Label();
        progressBar = new ProgressBar();
        progressBar.setPrefHeight(40);
        progressBar.setMinHeight(40);
        progressBar.setMaxHeight(40);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);

        progressBar.setStyle("-fx-accent: yellow;");

        content.getChildren().add(hbox);
        streakNumber = new Label("-");
        content.getChildren().add(adherenceText);
        content.getChildren().add(streakText);
        hbox.setAlignment(Pos.CENTER);
        content.setAlignment(Pos.CENTER);
        progressBar.setPrefWidth(500);

        hbox.getChildren().add(progressBar);
        hbox.getChildren().add(streakNumber);


        adherenceText.setId("mainwindow-dashboard-component-title");
        adherenceText.setPadding(new Insets(10, 0, 0, 0));
        adherenceText.setAlignment(Pos.CENTER);
        streakText.setId("mainwindow-dashboard-component-title");
        streakText.setPadding(new Insets(5, 0, 0, 0));
        streakText.setAlignment(Pos.CENTER);
        setProgress(sevenDayFraction);
        setStreakLength(streakLength);
        updateLabel();
        this.getChildren().add(content);
    }

    private void setProgress(Double value) {
        progress = value;
        if (value == null) {
            progressBar.setProgress(100);
            progressBar.setStyle("-fx-accent: navy;");
        } else {
            progressBar.setProgress(value);
            progressBar.setStyle("-fx-accent: yellow;");
        }
    }

    private void updateLabel() {
        if (longForm) {
            if (progress == null) {
                adherenceText.setText("Sync with the Concentrator to use this feature.");
                streakText.setText("Tap the <Synchronize> button");
            } else {
                adherenceText.setText(String.format("%d%% overall adherence in the last 7 days", (int) (progressBar.getProgress() * 100), streakLength));
                streakText.setText(String.format("Perfect for %d days!", streakLength));
            }
        } else {
            if (progress == null) {
                adherenceText.setText("Unknown");
                streakText.setText("");
            } else {
                adherenceText.setText(String.format("%d%% adherence", (int) (progressBar.getProgress()*100)));
                if (streakLength > 1) {
                    streakText.setText(streakLength.toString() + " perfect days!");
                } else {
                    streakText.setText("");
                }
            }
        }
    }

    private void setStreakLength(Integer value) {
        streakLength = value;
        if (value == null) {
            streakNumber.setText("");
        } else {
            streakNumber.setText(String.format("%d", value));
        }
    }

    public void setWidth(int width) {
        progressBar.setPrefWidth(width);
        adherenceText.setPrefWidth(width);
        progressBar.setMinWidth(width);
        adherenceText.setMinWidth(width);
        progressBar.setMaxWidth(width);
        adherenceText.setMaxWidth(width);
    }

}
