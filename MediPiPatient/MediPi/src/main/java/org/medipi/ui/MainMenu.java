package org.medipi.ui;

import org.medipi.devices.Element;

public class MainMenu extends TileMenu {
    public MainMenu(CentralScreen screen) {
        super(screen, 4, 2.5);
    }
    public void addElementMenuEntry(Element element) throws Exception {
        addTile(element.getDashboardTile());
    }
}
