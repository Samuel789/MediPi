package org.medipi.medication;

import javafx.scene.Node;
import org.medipi.DashboardTile;
import org.medipi.devices.Element;


public class MedicationManager extends Element {
    private static final String NAME = "Medication";
    private static final String DISPLAYNAME = "Medication";
    private static final String MEDIPIIMAGESEXCLAIM = "medipi.images.exclaim";
    private static final String MEDIPIIMAGESALLINTHRESHOLD = "medipi.images.allinthreshold";
    private static final String MEDIPIIMAGESHIGHESTERROROUTOFTHRESHOLD = "medipi.images.highesterror.outofthreshold";
    private static final String MEDIPIIMAGESHIGHESTERRORCANTCALCULATE = "medipi.images.highesterror.cantcalculate";

    private MedicationMenu menu;
    public String init() {
        menu = new MedicationMenu(medipi);
        MedicationDataInterface dbInterface = new MedicationDataInterface();
        return null;
    }

    @Override
    public Node getWindowComponent(){
        return menu;
    }
    @Override
    public void hideDeviceWindow() {
        menu.setVisible(false);
    }
    @Override
    public void callDeviceWindow() {
        medipi.hideAllWindows();
        menu.setVisible(true);
    }

    public DashboardTile getDashboardTile() {
        DashboardTile dashComponent = new DashboardTile(this, showTile);
        dashComponent.addTitle(getSpecificDeviceDisplayName());
        return dashComponent;
    }

    public static String getDisplayname() {
        return DISPLAYNAME;
    }

    /**
     * method to get the Display Name of the device
     *
     * @return displayName of device
     */
    @Override
    public String getSpecificDeviceDisplayName() {
        return DISPLAYNAME;
    }

    @Override
    public String getGenericDeviceDisplayName() {
        return NAME;
    }

    public static String getMedipiimagesexclaim() {
        return MEDIPIIMAGESEXCLAIM;
    }

    public static String getMedipiimagesallinthreshold() {
        return MEDIPIIMAGESALLINTHRESHOLD;
    }

    public static String getMedipiimageshighesterroroutofthreshold() {
        return MEDIPIIMAGESHIGHESTERROROUTOFTHRESHOLD;
    }

    public static String getMedipiimageshighesterrorcantcalculate() {
        return MEDIPIIMAGESHIGHESTERRORCANTCALCULATE;
    }

    public static String getMedipiimageshighesterrormissing() {
        return MEDIPIIMAGESHIGHESTERRORMISSING;
    }

    private static final String MEDIPIIMAGESHIGHESTERRORMISSING = "medipi.images.highesterror.missing";

    public String getName() {
        return NAME;
    }
}
