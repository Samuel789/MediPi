package org.medipi.ui;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;

import java.util.Vector;

public class TileMenu extends Group {
    private FlowPane dashTile;
    private ScrollPane contents;
    private Vector<Tile> tiles;

    public int getTargetWidth() {
        return targetWidth;
    }

    public int getTargetHeight() {
        return targetHeight;
    }

    private final int targetWidth;
    private final int targetHeight;
    private boolean scrollBarShowing = false;
    private int columns;

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
        updateDisplay();
    }

    public double getVisibleRows() {
        return visibleRows;
    }

    public void setVisibleRows(double visibleRows) {
        this.visibleRows = visibleRows;
        updateDisplay();
    }

    private double visibleRows;

    public TileMenu(WindowManager screen, int columns, double visibleRows) {
        // Set up the Dashboard view
        tiles = new Vector<Tile>();
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
        tiles.add(tile);
        dashTile.getChildren().add(tile.getNode((targetWidth - 15) / columns, (int) (targetHeight / visibleRows)));
    }

    private void updateDisplay() {
        dashTile.getChildren().clear();
        for (Tile tile: tiles) {
            dashTile.getChildren().add(tile.getNode((targetWidth - 15) / columns, (int) (targetHeight / visibleRows)));
        }
    }
}
