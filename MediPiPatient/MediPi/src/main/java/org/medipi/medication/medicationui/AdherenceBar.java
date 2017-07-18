package org.medipi.medication.medicationui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AdherenceBar extends Group {
    ProgressBar progressBar;
    Label adherenceText;
    HBox hbox;
    VBox content;
    Label streakNumber;
    Label streakText;
    int streakLength;

    public boolean isLongForm() {
        return longForm;
    }

    public void setLongForm(boolean longForm) {
        this.longForm = longForm;
        updateLabel();
    }

    boolean longForm = false;
    public AdherenceBar() {
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

        hbox.getChildren().add(progressBar);
        hbox.getChildren().add(streakNumber);


        adherenceText.setId("mainwindow-dashboard-component-title");
        adherenceText.setPadding(new Insets(10, 0, 0, 0));
        adherenceText.setAlignment(Pos.CENTER);
        streakText.setId("mainwindow-dashboard-component-title");
        streakText.setPadding(new Insets(5, 0, 0, 0));
        streakText.setAlignment(Pos.CENTER);

        this.getChildren().add(content);
    }

    public void setProgress(double value) {
        progressBar.setProgress(value);
        updateLabel();
    }

    private void updateLabel() {
        if (longForm) {
            adherenceText.setText(String.format("%d%% overall adherence in the last 7 days", (int) (progressBar.getProgress()*100), streakLength));
            streakText.setText(String.format("Perfect for %d days!", streakLength));
        } else {
            adherenceText.setText(String.format("%d%% adherence", (int) (progressBar.getProgress()*100)));
            streakText.setText("");
        }
    }

    public void setStreakLength(int value) {
        streakLength = value;
        streakNumber.setText(String.format("%d", value));
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
