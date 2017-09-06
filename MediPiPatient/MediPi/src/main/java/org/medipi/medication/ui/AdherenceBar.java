package org.medipi.medication.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.medipi.medication.model.PatientAdherence;
import org.medipi.medication.model.ScheduleAdherence;

public class AdherenceBar extends Group {
    static final double YELLOWTHRESHOLD = 0.95;
    static final double REDTHRESHOLD = 0.85;
    static final double MINVISIBLE = 0.05;
    static final double MINSTREAKLENGTH = 2;
    ProgressBar progressBar;
    Label adherenceText;
    HBox hbox;
    VBox content;
    Label streakNumber;
    Label streakText;
    Integer streakLength;
    Double progress;
    boolean longForm = false;

    public boolean isUnSynched() {
        return unSynched;
    }

    public void setUnSynched(boolean unSynched) {
        this.unSynched = unSynched;
        updateLabel();
    }

    boolean unSynched = false;


    public AdherenceBar(ScheduleAdherence scheduleAdherence) {
        this(scheduleAdherence.getSevenDayFraction(), scheduleAdherence.getStreakLength());
    }

    public AdherenceBar(PatientAdherence patientAdherence) {
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

    public boolean isLongForm() {
        return longForm;
    }

    public void setLongForm(boolean longForm) {
        this.longForm = longForm;
        updateLabel();
    }

    private void setProgress(Double value) {
        progress = value;
        if (value == null) {
            progressBar.setProgress(100);
            progressBar.setStyle("-fx-accent: navy;");
        } else {
            if (progress < MINVISIBLE) {
                progressBar.setProgress(MINVISIBLE);
            } else {
                progressBar.setProgress(value);
            }
            progressBar.setStyle(String.format("-fx-accent: %s;", determineColor(progress)));
        }
    }

    private String determineColor(double progress) {
        if (progress < REDTHRESHOLD) {
            return "red";
        } else if (progress <= YELLOWTHRESHOLD) {
            return "yellow";
        } else {
            return "green";
        }
    }

    public void setVerticalPadding(int pixels) {
        content.setPadding(new Insets(pixels, 0, pixels, 0));
    }

    private void updateLabel() {
        if (unSynched) {
            adherenceText.setText("Sync with the Concentrator to use this feature.");
            streakText.setText("Tap the Synchronize button");
        } else if (longForm) {
            if (progress == null) {
                adherenceText.setText("No statistics yet.");
                streakText.setText("");
            } else {
                adherenceText.setText(String.format("%d%% overall adherence in the last 7 days", (int) (progress * 100), streakLength));
                if (streakLength >= MINSTREAKLENGTH) {
                    streakText.setText(String.format("Perfect for %d days!", streakLength));
                } else {
                    streakText.setText("");
                }
            }
        } else {
            if (progress == null) {
                adherenceText.setText("");
                streakText.setText("");
            } else {
                adherenceText.setText(String.format("%d%% adherence", (int) (progress * 100)));
                if (streakLength >= MINSTREAKLENGTH) {
                    streakText.setText(streakLength.toString() + " perfect days!");
                } else {
                    streakText.setText("");
                }
            }
        }
    }

    private void setStreakLength(Integer value) {
        streakLength = value;
        if (value == null || value < MINSTREAKLENGTH) {
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
