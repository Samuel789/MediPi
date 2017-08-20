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
 * dashboard. This class creates and handles the dashboard EntityTile and its
 * contents, allowing the tile to be clicked and the Element to be called.
 * Overlays can be added to the tile so that dynamically changing data can be
 * displayed or an alert when actions are required in the related Element.
 * The tile can be configured to be visible or not
 *
 * @author rick@robinsonhq.com
 */
public class ScrollControlTile extends Tile {

    Button upButton = new Button();
    Button downButton = new Button();

    public ScrollControlTile(BooleanProperty bprop, int widthUnits, int heightUnits) {
        super(bprop, widthUnits, heightUnits);
        content.setTop(upButton);
        content.setBottom(downButton);
        content.setMargin(upButton, new Insets(10));
        content.setMargin(downButton, new Insets(10));
        content.setAlignment(upButton, Pos.CENTER);
        content.setAlignment(downButton, Pos.CENTER);
        upButton.setText("Up");
        downButton.setText("Down");
    }

    /**
     * Method to return the Dashboard EntityTile
     *
     * @return Dashboard EntityTile content back to the main MediPi class
     */
    @Override
    public BorderPane getNode(int unitWidth, int unitHeight, int availableWidthUnits) {
        int widthUnitsToUse;
        if (widthUnits > availableWidthUnits) {
            widthUnitsToUse = availableWidthUnits;
        } else {
            widthUnitsToUse = widthUnits;
        }
        int width = unitWidth * widthUnitsToUse;
        int height = unitHeight * heightUnits;
        content.setPrefSize(width, height);
        content.setMaxSize(width, height);
        content.setMinSize(width, height);
        upButton.getStyleClass().add("mp-button");
        downButton.getStyleClass().add("mp-button");
        upButton.setPrefWidth(width - 20);
        downButton.setPrefWidth(width - 20);
        upButton.setPrefHeight((height - 30) / 2);
        downButton.setPrefHeight((height - 30) / 2);
        return content;
    }

    public void setOnUpClick(EventHandler<? super MouseEvent> event) {
        upButton.setOnMouseClicked(event);
    }

    public void setOnDownClick(EventHandler<? super MouseEvent> event) {
        downButton.setOnMouseClicked(event);
    }
}
