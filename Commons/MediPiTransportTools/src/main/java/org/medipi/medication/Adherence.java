package org.medipi.medication;

/**
 * Created by sam on 10/08/17.
 */
public class Adherence {
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getStreakLength() {
        return streakLength;
    }

    public void setStreakLength(int streakLength) {
        this.streakLength = streakLength;
    }

    public double getSevenDayFraction() {
        return sevenDayFraction;
    }

    public void setSevenDayFraction(double sevenDayFraction) {
        this.sevenDayFraction = sevenDayFraction;
    }

    private int scheduleId;
    private int streakLength;
    private double sevenDayFraction;
}
