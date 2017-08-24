package org.medipi.ui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

import java.util.Vector;

public class TileMenu extends Group {
    private final int targetWidth;
    private final int targetHeight;
    int position = 0;
    private FlowPane dashTile;
    private ScrollPane contents;
    private Vector<Tile> tiles;
    private boolean scrollBarShowing = false;
    private int columns;
    private int margin;
    private Node overlayWindow = null;
    private double visibleRows;

    public TileMenu(WindowManager windowManager, int columns, double visibleRows, TileMenu upperMenu) {
        this(windowManager.getTargetWidth(), windowManager.getTargetHeight(), columns, visibleRows, upperMenu);
    }

    public void setPadding(Insets insets) {
        contents.setPadding(insets);
    }
    public TileMenu(int targetWidth, int targetHeight, int columns, double visibleRows, TileMenu upperMenu) {
        // Set up the Dashboard view
        tiles = new Vector<Tile>();
        this.columns = columns;
        margin = 15;
        this.visibleRows = visibleRows;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        contents = new ScrollPane();
        this.getChildren().add(contents);
        contents.setFitToWidth(true);
        contents.setMinHeight(targetHeight);
        contents.setMaxHeight(targetHeight);
        contents.setMinWidth(targetWidth);
        contents.setPadding(new Insets(0));
        contents.setMaxWidth(targetWidth);
        contents.setStyle("-fx-background-color: lightblue;");
        this.setStyle("-fx-background-color: lightblue;");
        contents.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contents.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        dashTile = new FlowPane();
        dashTile.setMinWidth(targetWidth);
        dashTile.setHgap(0);
        dashTile.setAlignment(Pos.TOP_LEFT);
        dashTile.setPadding(new Insets(0));
        dashTile.setOrientation(Orientation.HORIZONTAL);
        dashTile.setPrefWrapLength(targetWidth);
        dashTile.setId("mainwindow-dashboard");
        contents.setContent(dashTile);
        contents.addEventFilter(ScrollEvent.ANY, (x) -> {
            updateScrollPosition();
        });
        //bind the visibility property so that when not visible the panel doesnt take any space
        this.managedProperty().bind(this.visibleProperty());

    }

    public static Tile getNewBufferTile(int widthUnits, int heightUnits) {
        return new Tile(new SimpleBooleanProperty(true), widthUnits, heightUnits);
    }

    public int getTargetWidth() {
        return targetWidth;
    }

    public int getTargetHeight() {
        return targetHeight;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

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

    public void addTile(Tile tile) {
        tiles.add(tile);
        dashTile.getChildren().add(tile.getNode(getUnitWidth(), getUnitHeight(), columns));
    }

    private void updateDisplay() {
        dashTile.getChildren().clear();
        for (Tile tile : tiles) {
            dashTile.getChildren().add(tile.getNode(getUnitWidth(), getUnitHeight(), columns));
        }
    }

    public int getUnitWidth() {
        return (int) ((targetWidth - margin) / columns);
    }

    public int getUnitHeight() {
        return (int) (targetHeight / visibleRows);
    }

    public void setOverlayWindow(Node window) {
        overlayWindow = window;
    }

    public void hideOverlayWindow() {
        this.getChildren().clear();
        this.getChildren().add(dashTile);
    }

    public void showOverlayWindow() {
        this.getChildren().clear();
        this.getChildren().add(overlayWindow);
    }

    private void updateScrollPosition() {
        int unitHeight = getUnitHeight();
        int vSpace = (int) (dashTile.getHeight() - Math.round(visibleRows) * unitHeight);
        int numRows = vSpace / unitHeight;
        position = (int) (contents.getVvalue() * numRows);

    }

    public void closeOverlayWindow() {
        hideOverlayWindow();
        overlayWindow = null;
    }

    public void scrollDown() {
        System.out.println("Scrolling Down");
        contents.setVvalue(Math.min(((position + 1) * getUnitHeight() / (dashTile.getHeight() - visibleRows * getUnitHeight())), 1));
        updateScrollPosition();
    }

    public void scrollUp() {
        contents.setVvalue(Math.max(((position - 1) * getUnitHeight() / (dashTile.getHeight() - visibleRows * getUnitHeight())), 0));
        updateScrollPosition();
    }

    public void addBufferTile(int widthUnits, int heightUnits) {
        addTile(getNewBufferTile(widthUnits, heightUnits));
    }
}
