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
package org.medipi;

import java.util.ArrayList;
import java.util.function.Function;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.medipi.devices.Element;

/**
 * Class to encapsulate a Dashboard Component node which is placed in the
 * dashboard. This class creates and handles the dashboard Tile and its
 * contents, allowing the tile to be clicked and the Element to be called.
 * Overlays can be added to the tile so that dynamically changing data can be
 * displayed or an alert when actions are required in the related Element.
 * The tile can be configured to be visible or not
 *
 * @author rick@robinsonhq.com
 */
public class DashboardTile extends Tile {
    /**
     * Constructor
     *
     * @param elem The element which this tile relates to
     */
    public DashboardTile(Element elem, BooleanProperty bprop) {
        super(bprop, 1, 1);
        this.setBackgroundImage(elem.getImage());
        this.setOnMouseClicked((MouseEvent event) -> {
            elem.callDeviceWindow();
        });
    }
}
