package org.medipi.ui;

import javafx.geometry.Insets;
import org.medipi.devices.Element;

public class MainMenu extends TileMenu {
    public MainMenu(WindowManager screen, TileMenu upperMenu) {
        super(screen, 4, 2.5, upperMenu);
        setPadding(new Insets(10, 5, 10, 5));
    }

    public void addElementMenuEntry(Element element) throws Exception {
        addTile(element.getDashboardTile());
    }
}
