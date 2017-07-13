package org.medipi.medication;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;


public class TileScreen {
    ScrollPane dashTileSc;
    TilePane dashTile;

    VBox subWindow;
    public TileScreen() {
        dashTileSc = new ScrollPane();
        dashTile = new TilePane();
        dashTile.setMinWidth(800);
        dashTile.setId("mainwindow-dashboard");
        dashTileSc.setContent(dashTile);
        dashTileSc.setFitToWidth(true);
        dashTileSc.setFitToHeight(true);
        dashTileSc.setMinHeight(380);
        dashTileSc.setMaxHeight(380);
        dashTileSc.setMinWidth(800);
        dashTileSc.setId("mainwindow-dashboard-scroll");
        dashTileSc.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        dashTileSc.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        //bind the visibility property so that when not visible the panel doesnt take any space
        dashTileSc.managedProperty().bind(dashTileSc.visibleProperty());
        subWindow = new VBox();
        subWindow.setId("subwindow");
        subWindow.setAlignment(Pos.TOP_CENTER);
        subWindow.getChildren().addAll(
                dashTileSc
        );
    }
}
