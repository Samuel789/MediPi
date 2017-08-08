package org.medipi.medication.medicationui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.medipi.MediPi;
import org.medipi.medication.*;
import org.medipi.medication.reminders.MedicationReminderEvent;
import org.medipi.medication.reminders.ReminderEventInterface;
import org.medipi.medication.reminders.ReminderService;
import org.medipi.ui.*;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;


public class RecordDoseMenu extends TileMenu {
    double adherenceRate =0.8;
    TileMenu mainPane;
    List<Schedule> schedules;
    MediPi mediPi;
    TileMenu upperMenu;
    HashMap<MedicationTile, ScheduledDose> dueDose;

    public RecordDoseMenu(MediPi mediPi, TileMenu upperMenu, ScheduledDose dose) {
        super(new WindowManager(), 5, 3.15, upperMenu);
        setOverlayWindow(new DoseDetailsScreen(mediPi, upperMenu, dose));
        showOverlayWindow();
        this.mediPi = mediPi;
    }
    public RecordDoseMenu(MediPi medipi, TileMenu upperMenu) {
        super(new WindowManager(), 5, 3.15, upperMenu);
        this.mediPi = medipi;
        this.upperMenu = upperMenu;
        HeaderTile header = new HeaderTile(new SimpleBooleanProperty(true), 3, 1);
        header.setTitleText("Recording a Dose");
        ButtonTile backButton = new ButtonTile(new SimpleBooleanProperty(true), 2, 1);
        backButton.setText("Back");
        backButton.setOnButtonClick((MouseEvent event) -> {close();});
        header.setMainContent(new Label("First, tap the medication you've taken"));
        this.addTile(backButton);
        this.addTile(header);

        ScrollControlTile sidebarTile = new ScrollControlTile(new SimpleBooleanProperty(true), 1, 2);
        mainPane = new TileMenu(getUnitWidth()*4, getUnitHeight()*2, 2, 2.2, this);
        Tile mainPaneTile = new Tile(new SimpleBooleanProperty(true), 4, 2);
        mainPaneTile.getContent().setCenter(mainPane);
        this.addTile(sidebarTile);
        this.addTile(mainPaneTile);
        sidebarTile.setOnUpClick((MouseEvent event) -> {mainPane.scrollUp();});
        sidebarTile.setOnDownClick((MouseEvent event) -> {mainPane.scrollDown();});
        populateMedicationTiles();
    }

    private void populateMedicationTiles() {
        dueDose = new HashMap<>();
        schedules = ((MedicationManager) mediPi.getElement("Medication")).getDatestore().getPatientSchedules();
        ArrayList<MedicationTile> orderedTiles = new ArrayList<>();
        ReminderService reminderService = ((MedicationManager) mediPi.getElement("Medication")).getReminderService();
        LocalTime currentTime = LocalTime.now();
        // Categorize medications by due status (take now, as needed or not now)
        for (Schedule schedule: schedules) {
            MedicationTile newTile = new MedicationTile(new SimpleBooleanProperty(true), 1, 1, schedule);
            if (schedule.getScheduledDoses().size() == 0) {
                newTile.setDueStatus(MedicationTile.DueStatus.ASNEEDED);
            } else {
                boolean foundDueDose = false;
                for (ReminderEventInterface event: reminderService.getTodayActiveEvents()) {
                    MedicationReminderEvent medEvent = (MedicationReminderEvent) event;
                    if (medEvent.getDose().getSchedule() == schedule) {
                        if (medEvent.getStartTime().isBefore(currentTime) && medEvent.getEndTime().isAfter(currentTime)) {
                            newTile.setDueStatus(MedicationTile.DueStatus.TAKENOW);
                            dueDose.put(newTile, medEvent.getDose());
                            foundDueDose = true;
                            break;
                        }
                    }
                }
                if (!foundDueDose) {
                    newTile.setDueStatus(MedicationTile.DueStatus.NOTNOW);
                }
            }
            newTile.setDisplayType(MedicationTile.DisplayType.DUESTATUS);
            newTile.setOnTileClick((MouseEvent event) -> {selectTile(newTile);});
            orderedTiles.add(newTile);
        }
        orderedTiles.sort(Comparator.comparing(MedicationTile::getDueStatus));
        for (MedicationTile tile: orderedTiles) {
            mainPane.addTile(tile);
        }
    }

    public void selectTile(MedicationTile tile) {
        if (dueDose.containsKey(tile)) {
            setOverlayWindow(new DoseDetailsScreen(mediPi, this, dueDose.get(tile)));
        } else {
            setOverlayWindow(new DoseDetailsScreen(mediPi, this, tile.getMedicationSchedule()));
        }
        showOverlayWindow();

    }

    void close() {
        upperMenu.closeOverlayWindow();
    }
}

class DoseDetailsScreen extends TileMenu {
    Label doseDescriptor;
    LocalDate doseDay;
    LocalTime doseTime;
    double doseValue;
    DoseUnit doseUnit;
    String medicationName;
    MediPi mediPi;

    public DoseDetailsScreen(MediPi mediPi, TileMenu upperMenu, ScheduledDose dose) {
        this(mediPi, upperMenu, dose.getSchedule());
        doseValue = dose.getDoseValue();
        generateDoseString();
    }

    public DoseDetailsScreen(MediPi medipi, TileMenu upperMenu, Schedule medicationSchedule) {
        super(new WindowManager(), 8, 3.15, upperMenu);
        this.mediPi = medipi;

        HeaderTile header = new HeaderTile(new SimpleBooleanProperty(true), 5, 1);
        header.setTitleText("Recording a Dose");
        ButtonTile backButton = new ButtonTile(new SimpleBooleanProperty(true), 3, 1);
        backButton.setText("Back");
        backButton.setOnButtonClick((MouseEvent event) -> {
            upperMenu.closeOverlayWindow();
        });
        header.setMainContent(new Label("Now, make sure the dose and time are correct"));
        doseUnit = (DoseUnit) medicationSchedule.getMedication().getDoseUnit();
        medicationName = medicationSchedule.determineDisplayName();
        doseDay = LocalDate.now();
        doseTime = LocalTime.now();
        doseValue = 0;
        doseDescriptor = new Label();
        generateDoseString();


        Tile centralTile = new Tile(new SimpleBooleanProperty(true), 8, 1);
        centralTile.setContent(new BorderPane());
        centralTile.getContent().setCenter(doseDescriptor);
        doseDescriptor.setId("mainwindow-dashboard-component-title");
        centralTile.getContent().setId("mainwindow-dashboard-component");
        centralTile.getContent().setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));

        ButtonTile doseButton = new ButtonTile(new SimpleBooleanProperty(true), 2, 1);
        doseButton.setText("Change Dose");
        ButtonTile timeButton = new ButtonTile(new SimpleBooleanProperty(true), 2, 1);
        timeButton.setText("Change Time");
        ButtonTile saveButton = new ButtonTile(new SimpleBooleanProperty(true), 2, 1);
        saveButton.setText("Save");

        addTile(backButton);
        addTile(header);

        addTile(centralTile);

        addTile(doseButton);
        addTile(timeButton);
        addBufferTile(1, 1);
        addTile(saveButton);

        saveButton.setOnButtonClick((ActionEvent) -> {
            if (getUserConfirmation()) {
                ZonedDateTime timeOnSystem = ZonedDateTime.of(doseTime.atDate(doseDay),ZoneId.systemDefault());
                Timestamp ts = Timestamp.valueOf(doseTime.atDate(doseDay));
                System.out.println(ts);
                System.out.println(ts.getTime());
                medicationSchedule.getRecordedDoses().add(new RecordedDose(Timestamp.valueOf(doseTime.atDate(doseDay)), doseValue, medicationSchedule));
                if (upperMenu instanceof RecordDoseMenu) {
                    ((RecordDoseMenu) upperMenu).close();
                } else {
                    upperMenu.closeOverlayWindow();
                }
            }
        });
    }

    private void generateDoseString() {
        doseDescriptor.setText(String.format("Recording %f %s(s) of %s taken %s at %s", doseValue, doseUnit.getName(), medicationName, doseDay, doseTime));
    }

    private boolean getUserConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirm recording dose");
        alert.setHeaderText(null);
        System.out.println(mediPi);
        System.out.println(mediPi.getCssfile());
        alert.getDialogPane().getStylesheets().add("file:///" + mediPi.getCssfile());
        alert.getDialogPane().setMaxSize(600, 300);
        alert.getDialogPane().setId("message-box");
        Text text = new Text("Are you sure you want to record this dose?\n" + doseDescriptor.getText());
        text.setWrappingWidth(600);
        alert.getDialogPane().setContent(text);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.YES;
    }
}