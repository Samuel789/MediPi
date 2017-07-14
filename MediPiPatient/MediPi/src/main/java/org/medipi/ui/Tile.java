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

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

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
public class Tile {

    protected BorderPane component = new BorderPane();
    protected int widthUnits;
    protected int heightUnits;

    /**
     * Constructor
     *
     */
    public Tile(BooleanProperty bprop, int widthUnits, int heightUnits) {
        this.widthUnits = widthUnits;
        this.heightUnits = heightUnits;
        if(bprop!=null){
            component.visibleProperty().bind(bprop);
            component.managedProperty().bind(bprop);
        }
    }

    /**
     * Method to return the Dashboard ButtonTile
     *
     * @return Dashboard ButtonTile component back to the main MediPi class
     */
    public Pane getNode(int unitWidth, int unitHeight) {
        int width = unitWidth*widthUnits;
        int height = unitHeight*heightUnits;
        component.setPrefSize(width, height);
        component.setMaxSize(width, height);
        component.setMinSize(width, height);
        return component;
    }

    public void setOnTileClick(EventHandler<? super MouseEvent> event) {
        component.setOnMouseClicked(event);
    }
}
