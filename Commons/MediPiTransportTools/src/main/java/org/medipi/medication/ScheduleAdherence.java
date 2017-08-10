package org.medipi.medication;

/**
 * Created by sam on 10/08/17.
 */
public class ScheduleAdherence {
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getStreakLength() {
        return streakLength;
    }

    public void setStreakLength(Integer streakLength) {
        this.streakLength = streakLength;
    }

    public Double getSevenDayFraction() {
        return sevenDayFraction;
    }

    public void setSevenDayFraction(Double sevenDayFraction) {
        this.sevenDayFraction = sevenDayFraction;
    }

    private int scheduleId;
    private Integer streakLength;
    private Double sevenDayFraction;
}
