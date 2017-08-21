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
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

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
public class Tile {

    protected BorderPane content = new BorderPane();
    protected int widthUnits;
    protected int heightUnits;
    protected EventHandler<? super MouseEvent> mouseClickEvent;

    /**
     * Constructor
     */
    public Tile(BooleanProperty bprop, int widthUnits, int heightUnits) {
        this.widthUnits = widthUnits;
        this.heightUnits = heightUnits;
        if (bprop != null) {
            content.visibleProperty().bind(bprop);
            content.managedProperty().bind(bprop);
        }
    }

    public int getWidthUnits() {
        return widthUnits;
    }

    public void setWidthUnits(int widthUnits) {
        this.widthUnits = widthUnits;
    }

    public int getHeightUnits() {
        return heightUnits;
    }

    public void setHeightUnits(int heightUnits) {
        this.heightUnits = heightUnits;
    }

    /**
     * Method to return the Dashboard EntityTile
     *
     * @return Dashboard EntityTile content back to the main MediPi class
     */
    public Pane getNode(int unitWidth, int unitHeight, int availableWidthUnits) {
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
        return content;
    }

    public void setOnTileClick(EventHandler<? super MouseEvent> event) {
        if (mouseClickEvent != null) {
            content.removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseClickEvent);
        }
        content.addEventFilter(MouseEvent.MOUSE_CLICKED, event);
        mouseClickEvent = event;
    }

    public BorderPane getContent() {
        return content;
    }

    public void setContent(BorderPane content) {
        this.content = content;
    }

    public ObservableList<String> getStyleClass() {
        return content.getStyleClass();
    }
}
