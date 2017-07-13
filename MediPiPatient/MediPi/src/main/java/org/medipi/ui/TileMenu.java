package org.medipi.ui;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;

public class TileMenu extends Group {
    private FlowPane dashTile;
    private ScrollPane contents;
    private final int targetWidth;
    private final int targetHeight;
    private boolean scrollBarShowing = false;
    private int columns;
    private double visibleRows;

    public TileMenu(CentralScreen screen, int columns, double visibleRows) {
        // Set up the Dashboard view
        this.columns = columns;
        this.visibleRows = visibleRows;
        targetHeight = screen.getTargetHeight();
        targetWidth = screen.getTargetWidth();
        contents = new ScrollPane();
        this.getChildren().add(contents);
        contents.setFitToWidth(true);
        contents.setMinHeight(targetHeight);
        contents.setMaxHeight(targetHeight);
        contents.setMinWidth(targetWidth);
        contents.setMaxWidth(targetWidth);
        contents.setId("mainwindow-dashboard-scroll");
        contents.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contents.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        dashTile = new FlowPane();
        dashTile.setMinWidth(targetWidth);
        dashTile.setHgap(0);
        dashTile.setId("mainwindow-dashboard");
        contents.setContent(dashTile);
        //bind the visibility property so that when not visible the panel doesnt take any space
        this.managedProperty().bind(this.visibleProperty());

    }
    public void addTile(Tile tile) {
        dashTile.getChildren().add(tile.getNode((targetWidth-15)/columns, (int) (targetHeight/visibleRows)));
    }
}
