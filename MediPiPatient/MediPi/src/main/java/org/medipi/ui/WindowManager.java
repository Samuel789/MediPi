package org.medipi.ui;

import javafx.scene.Group;
import javafx.scene.layout.VBox;
import org.medipi.ui.MainMenu;
import org.medipi.devices.Element;

import java.util.ArrayList;

public class WindowManager extends Group {
    private VBox contents;

    private int targetWidth = 800;
    private int targetHeight = 380;

    public MainMenu getDashboard() {
        return dashboard;
    }
    private MainMenu dashboard;
    private ArrayList<Element> elements = new ArrayList<>();


    public WindowManager() {
        contents = new VBox();
        dashboard = new MainMenu(this);
        contents.getChildren().add(dashboard);
        this.getChildren().add(contents);
        contents.setMinHeight(targetHeight);
        contents.setMaxHeight(targetHeight);
        contents.setMinWidth(targetWidth);
        contents.setMaxWidth(targetWidth);
    }
    public void addElement(Element element) throws Exception {
        elements.add(element);
        contents.getChildren().add(element.getWindowComponent());
        dashboard.addElementMenuEntry(element);
    }
    /**
     * Method to call the mainwindow back to the dashboard
     *
     */
    public void callDashboard() {
        hideAllWindows();
        dashboard.setVisible(true);
    }

    /**
     * Method to hide all the element windows from the MediPi mainwindow
     */
    public void hideAllWindows() {
        dashboard.setVisible(false);
        for (Element e : elements) {
            e.hideDeviceWindow();
        }
    }

    public int getTargetWidth() {
        return targetWidth;
    }

    public int getTargetHeight() {
        return targetHeight;
    }

}
