package org.medipi.medication;

import com.sun.prism.impl.Disposer;
import org.medipi.MediPi;
import org.medipi.medication.reminders.MedicationReminderEvent;
import org.medipi.medication.reminders.ReminderEventInterface;
import org.medipi.medication.reminders.ReminderService;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sam on 02/08/17.
 */
public class Datastore {

    private List<Schedule> patientSchedules;
    private MediPi medipi;

    public Datastore(MediPi medipi){
        this.medipi = medipi;
        patientSchedules = new ArrayList<>();
    }

    public List<Schedule> getPatientSchedules() {
        return patientSchedules;
    }

    public void replacePatientSchedules(List<Schedule> patientSchedules) {
        this.patientSchedules = patientSchedules;
        populateReminderService();
    }

    private void populateReminderService() {
        ReminderService reminderService = ((MedicationManager) medipi.getElement("Medication")).getReminderService();
        HashSet<ReminderEventInterface> events = new HashSet<>();
        // Add every scheduled dose to reminder service
        for (Schedule schedule: patientSchedules) {
            for (ScheduledDose dose : schedule.getScheduledDoses()) {
                events.add(new MedicationReminderEvent(dose));
            }
        }
        LocalDate today = LocalDate.now();
        reminderService.stopService();
        reminderService.setEvents(events);
        // Check if any doses have already been taken today
        for (Schedule schedule: patientSchedules) {
            HashSet<RecordedDose> todayTakenDoses = new HashSet<>();
            for (RecordedDose takenDose : schedule.getRecordedDoses()) {
                LocalDateTime takenTime = takenDose.getTimeTaken().toLocalDateTime();
                if (takenTime.toLocalDate().equals(today)) {
                    todayTakenDoses.add(takenDose);
                    System.out.println(takenDose.getTimeTaken());
                    System.out.println(takenDose.getTimeTaken());
                }
            }
            System.out.println(todayTakenDoses.size());
            if (todayTakenDoses.size() == 0) {
                    continue;
            }
            // If so, disable reminders for the corresponding scheduled doses
            Set<ReminderEventInterface> todayDoses = reminderService.getTodayActiveEvents().stream()
                    .filter(event -> ((MedicationReminderEvent)event).getDose().getSchedule() == schedule).collect(Collectors.toSet());
            for (RecordedDose takenDose: todayTakenDoses) {
                LocalTime takenTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(takenDose.getTimeTaken().getTime()/1000), ZoneId.systemDefault()).toLocalTime();
                for (ReminderEventInterface event: todayDoses) {
                    MedicationReminderEvent medEvent = (MedicationReminderEvent) event;
                    if (takenTime.isAfter(medEvent.getStartTime()) && takenTime.isBefore(medEvent.getEndTime())) {
                        reminderService.dismissEvent(event);
                        break;
                    }
            }
        }
        }
        reminderService.startService();
    }

}
