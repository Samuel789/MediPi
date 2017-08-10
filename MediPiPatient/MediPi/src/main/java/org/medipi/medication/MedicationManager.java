package org.medipi.medication;

import javafx.scene.Node;
import org.medipi.DashboardTile;
import org.medipi.devices.Element;
import org.medipi.medication.medicationui.MedicationMenu;
import org.medipi.medication.reminders.ReminderEventInterface;
import org.medipi.medication.reminders.ReminderService;

import java.util.HashSet;
import java.util.Set;


public class MedicationManager extends Element {
    private static final String NAME = "Medication";
    private static final String DISPLAYNAME = "Medication";
    private static final String MEDIPIIMAGESEXCLAIM = "medipi.images.exclaim";
    private static final String MEDIPIIMAGESALLINTHRESHOLD = "medipi.images.allinthreshold";
    private static final String MEDIPIIMAGESHIGHESTERROROUTOFTHRESHOLD = "medipi.images.highesterror.outofthreshold";
    private static final String MEDIPIIMAGESHIGHESTERRORCANTCALCULATE = "medipi.images.highesterror.cantcalculate";
    private static final String MEDIPIIMAGESHIGHESTERRORMISSING = "medipi.images.highesterror.missing";

    public MedicationMenu getMenu() {
        return menu;
    }

    private MedicationMenu menu;
    private Datastore datastore;
    private ReminderService reminderService;
    private Synchronizer synchronizer;

    public static String getDisplayname() {
        return DISPLAYNAME;
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

    public Datastore getDatestore() {
        return datastore;
    }

    public ReminderService getReminderService() {
        return reminderService;
    }

    public void reload() {
        menu.reload();
    }

    public Synchronizer getSynchronizer() {
        return synchronizer;
    }

    public String init() {
        reminderService = new ReminderService(medipi, new HashSet<ReminderEventInterface>());
        datastore = new Datastore(medipi);
        try {
            synchronizer = new Synchronizer(medipi);
        } catch (Exception e) {
            System.out.println("Unable to create Medication synchronizer - synchronization will not be available");
            System.out.println(e.getMessage());
        }
        menu = new MedicationMenu(medipi, null, datastore);
        return null;
    }

    @Override
    public Node getWindowComponent() {
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

    public String getName() {
        return NAME;
    }

    public void recordMedicationDose(ScheduledDose dose) {
        menu.recordMedicationDose(dose);
    }
}
