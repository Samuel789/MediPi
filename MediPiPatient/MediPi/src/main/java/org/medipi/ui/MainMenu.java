package org.medipi.ui;

import org.medipi.devices.Element;

public class MainMenu extends TileMenu {
    public MainMenu(WindowManager screen, TileMenu upperMenu) {
        super(screen, 4, 2.5, upperMenu);
    }
    public void addElementMenuEntry(Element element) throws Exception {
        addTile(element.getDashboardTile());
    }
}
