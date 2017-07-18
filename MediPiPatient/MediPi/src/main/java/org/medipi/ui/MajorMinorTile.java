/*
 * Copyright 2016  Richard Robinson @ NHS Digital <rrobinson@nhs.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.medipi.ui;

import javafx.beans.property.BooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

/**
 * Class to encapsulate a Dashboard Component node which is placed in the
 * dashboard. This class creates and handles the dashboard ButtonTile and its
 * contents, allowing the tile to be clicked and the Element to be called.
 * Overlays can be added to the tile so that dynamically changing data can be
 * displayed or an alert when actions are required in the related Element.
 * The tile can be configured to be visible or not
 *
 * @author rick@robinsonhq.com
 */
public class MajorMinorTile extends Tile {

    Button majorButton = new Button();
    Button minorButton = new Button();
    int buttonPadding = 25;

    public MajorMinorTile(BooleanProperty bprop, int widthUnits, int heightUnits) {
        super(bprop, widthUnits, heightUnits);
        content.setTop(majorButton);
        content.setBottom(minorButton);
        content.setMargin(majorButton, new Insets(buttonPadding, buttonPadding, buttonPadding/2, buttonPadding));
        content.setMargin(minorButton, new Insets(buttonPadding/2, buttonPadding, buttonPadding, buttonPadding));
        content.setAlignment(majorButton, Pos.CENTER);
        content.setAlignment(minorButton, Pos.CENTER);
    }

    /**
     * Method to return the Dashboard ButtonTile
     *
     * @return Dashboard ButtonTile content back to the main MediPi class
     */
    @Override
    public BorderPane getNode(int unitWidth, int unitHeight) {
        int width = unitWidth*widthUnits;
        int height = unitHeight*heightUnits;
        content.setPrefSize(width, height);
        content.setMaxSize(width, height);
        content.setMinSize(width, height);
        majorButton.setId("mainwindow-dashboard-content-title");
        minorButton.setId("mainwindow-dashboard-content-title");
        majorButton.setPrefWidth(width - 2*buttonPadding);
        minorButton.setPrefWidth((width - 2*buttonPadding)*0.7);
        majorButton.setPrefHeight((height - 2*buttonPadding)/2);
        minorButton.setPrefHeight((height - 2*buttonPadding)/2);
        return content;
    }

    public void setMajorText(String text) {
        majorButton.setText(text);
    }

    public String getMajorText() {
        return majorButton.getText();
    }

    public void setMinorText(String text) {
        minorButton.setText(text);
    }

    public String getMinorText() {
        return minorButton.getText();
    }

    public void setOnMajorClick(EventHandler<? super MouseEvent> event) {
        majorButton.setOnMouseClicked(event);
    }

    public void setOnMinorClick(EventHandler<? super MouseEvent> event) {
        minorButton.setOnMouseClicked(event);
    }
}
