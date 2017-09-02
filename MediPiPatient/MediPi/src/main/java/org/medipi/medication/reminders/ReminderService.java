package org.medipi.medication.reminders;

import javafx.application.Platform;
import org.medipi.MediPi;
import org.medipi.medication.*;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;

public class ReminderService {
    private MediPi mediPi;
    private Collection<ReminderEventInterface> todayActiveEvents;
    private Collection<ReminderEventInterface> events;
    private LocalDate today;
    private ReminderServiceAsync runningReminderThread;

    public ReminderService(MediPi mediPi, Collection<ReminderEventInterface> events) {
        this.mediPi = mediPi;
        setEvents(events);
        startService();
    }

    public MediPi getMediPi() {
        return mediPi;
    }

    LocalDate getToday() {
        return today;
    }

    public void setEvents(Collection<ReminderEventInterface> events) {
        this.events = events;
        redetermineTodaysEvents();
    }

    public void dismissEvent(ReminderEventInterface event) {
        todayActiveEvents.remove(event);
    }

    synchronized void redetermineTodaysEvents() {
        boolean serviceRunning = runningReminderThread != null;
        stopService();
        todayActiveEvents = new HashSet<>();
        today = LocalDate.now();
        for (ReminderEventInterface event : events) {
            if (event.activeOnDay(today)) {
                todayActiveEvents.add(event);
            }
        }
        if (serviceRunning) {
            startService();
        }
    }

    public void startService() {
        stopService();
        runningReminderThread = new ReminderServiceAsync(this);
        runningReminderThread.start();
    }

    public void stopService() {
        if (runningReminderThread != null) {
            runningReminderThread.kill();
        }
        runningReminderThread = null;
    }

    public Collection<ReminderEventInterface> getTodayActiveEvents() {
        return todayActiveEvents;
    }

    public Collection<ReminderEventInterface> getAllEvents() {
        return todayActiveEvents;
    }
}

class ReminderServiceAsync extends Thread {
    private ReminderService reminderService;
    private volatile boolean isRunning;

    ReminderServiceAsync(ReminderService reminderService) {
        this.reminderService = reminderService;
        isRunning = true;
    }

    @Override
    public void run() {
        Collection<ReminderEventInterface> activeEvents;
        while (isRunning) {
            activeEvents = reminderService.getTodayActiveEvents();
            if (!reminderService.getToday().equals(LocalDate.now())) {
                reminderService.redetermineTodaysEvents();
                isRunning = false;
                break;
            }
            LocalTime currentTime = LocalTime.now();
            synchronized (activeEvents) {
                for (ReminderEventInterface event : activeEvents) {
                    if (!event.isFrozen() && event.getReminderStartTime().isBefore(currentTime) && event.getReminderEndTime().isAfter(currentTime)) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                event.trigger(reminderService.getMediPi());
                            }
                        });
                    }
                }

            }
            try {
                sleep(10000);
            } catch (InterruptedException e) {
                continue;
            }
        }
    }

    public void kill() {
        isRunning = false;
    }
}