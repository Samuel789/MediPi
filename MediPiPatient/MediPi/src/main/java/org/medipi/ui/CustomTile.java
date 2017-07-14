package org.medipi.ui;

import javafx.beans.property.BooleanProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class CustomTile extends Tile {
    public CustomTile(BooleanProperty bprop, int widthUnits, int heightUnits) {
        super(bprop, widthUnits, heightUnits);
    }
    public BorderPane getTileContent() {
        return component;
    }
}
