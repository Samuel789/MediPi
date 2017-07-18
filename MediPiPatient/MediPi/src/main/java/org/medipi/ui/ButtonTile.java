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

import java.util.ArrayList;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class ButtonTile extends Tile {

    private StackPane contentStack = new StackPane();
    private ImageView backgroundImage;
    private ImageView foregroundImage = new ImageView();
    private ArrayList<Label[]> labels = new ArrayList<>();

    /**
     * Constructor
     *
     */
    public ButtonTile(BooleanProperty bprop, int widthUnits, int heightUnits) {
        super(bprop, widthUnits, heightUnits);
        content.setId("mainwindow-dashboard-component");
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));

        contentStack.setPadding(new Insets(5, 5, 5, 5));
        contentStack.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        for (Label[] l : labels) {
            HBox h = new HBox();
            h.setAlignment(Pos.CENTER);
            h.getChildren().addAll(
                    l[0],
                    l[1]);
            vbox.getChildren().add(h);
        }
        contentStack.getChildren().add(vbox);
        content.setCenter(contentStack);
    }

    protected void setBackgroundImage(ImageView image) {
        backgroundImage = image;
        backgroundImage.setFitHeight(80);
        backgroundImage.setFitWidth(80);
        contentStack.getChildren().add(backgroundImage);

    }

    /**
     * Method to add a title to the ButtonTile
     *
     * @param title title name
     */
    public void addTitle(String title) {

        Text t = new Text(title);
        t.setId("mainwindow-dashboard-component-title");
        t.setWrappingWidth(180);
        t.setTextAlignment(TextAlignment.CENTER);
        HBox h = new HBox(t);
        h.setAlignment(Pos.CENTER);
        content.setTop(h);

    }

    /**
     * Method to add overlayed text to the ButtonTile.
     *
     * Every successive addition of this overlay will add another line of text
     * on top of the tile - watch out that it doesn't exceed the limits of the
     * tile! When data is added to the StringProperty of the passed in Label,
     * the background image is made opaque
     *
     * @param measure a label containing a StringParameter so that the changing
     * data can be displayed dynamically over the image.
     * @param u units of measurement
     */
    public void addOverlay(Label measure, String u) {
        Label units = new Label(u);
        units.setVisible(false);
        units.setId("mainwindow-dashboard-component-units");
        measure.setId("mainwindow-dashboard-component-measure");
        // in order to fade the image and superimpose the recorded values when they have been taken
        measure.textProperty().addListener((ObservableValue<? extends String> ov, String oldValue, String newValue) -> {
            if (!newValue.equals("")) {
                backgroundImage.setStyle("-fx-opacity:0.2;");
                units.setVisible(true);
            } else {if (backgroundImage != null) {
            contentStack.getChildren().add(backgroundImage);
        }
        if (foregroundImage != null) {
            contentStack.getChildren().add(foregroundImage);
        }
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        for (Label[] l : labels) {
            HBox h = new HBox();
            h.setAlignment(Pos.CENTER);
            h.getChildren().addAll(
                    l[0],
                    l[1]);
            vbox.getChildren().add(h);
        }
        contentStack.getChildren().add(vbox);
        content.setCenter(contentStack);
                backgroundImage.setStyle("-fx-opacity:1.0;");
                units.setVisible(false);
            }
        });
        Label[] l = new Label[2];
        l[0] = measure;
        l[1] = units;
        labels.add(l);
    }

    /**
     * Method to add an overlayed Image to the ButtonTile.
     *
     * To add an image on top of the background image
     *
     * @param image imageView of the alert to be superimposed over the tile
     * @param bp BooleanProperty to dynamically control whether the Image is
     * visible
     */
    public void addOverlay(ObjectProperty<Image> image, BooleanProperty bp) {
        foregroundImage.imageProperty().bind(image);
        foregroundImage.setFitHeight(80);
        foregroundImage.setFitWidth(80);
        if (bp.getValue()) {
            foregroundImage.setVisible(true);
        } else {
            foregroundImage.setVisible(false);
        }
        // in order to superimpose the recorded values when they have been taken
        bp.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                foregroundImage.setVisible(true);
                foregroundImage.setStyle("-fx-opacity:0.1;");
                backgroundImage.setStyle("-fx-opacity:1.0;");
            } else {
                foregroundImage.setVisible(false);
                backgroundImage.setStyle("-fx-opacity:1.0;");
            }
        });
        contentStack.getChildren().add(foregroundImage);
    }

    /**
     * Method to add a Colour to the ButtonTile.
     *
     * To paint the tile a background colour on tile
     *
     * @param colour Background colour of the tile
     * @param bp BooleanProperty to dynamically control whether the colour should be changed
     */
    public void addOverlay(Color colour, BooleanProperty bp) {
        
        content.backgroundProperty().bind(Bindings.when(bp)
                .then(new Background(new BackgroundFill(colour, new CornerRadii(10), Insets.EMPTY)))
                .otherwise(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY))));
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


        return content;
    }

}
