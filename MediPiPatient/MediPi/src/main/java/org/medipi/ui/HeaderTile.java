package org.medipi.ui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class HeaderTile extends Tile {
    Label titleLabel;

    public HeaderTile(SimpleBooleanProperty bpop, int widthUnits, int heightUnits) {
        super(bpop, widthUnits, heightUnits);
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(5), new Insets(15))));
        content.setPadding(new Insets(30));
        titleLabel = new Label("Title");
        titleLabel.setId("mainwindow-dashboard-component-title");
        content.setTop(titleLabel);
        content.setAlignment(titleLabel, Pos.TOP_CENTER);
        titleLabel.setPadding(new Insets(0, 0, 10, 0));
        content.setAlignment(titleLabel, Pos.TOP_CENTER);
    }

    public void setTitleText(String title) {
        titleLabel.setText(title);
    }

    public Node getMainContent() {
        return content.getCenter();
    }

    public void setMainContent(Node centerContent) {
        content.setCenter(centerContent);
        content.setAlignment(centerContent, Pos.CENTER);
    }
}
