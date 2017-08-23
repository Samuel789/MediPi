package org.medipi.concentrator.logic;

import org.junit.Before;
import org.junit.Test;
import org.medipi.medication.Schedule;
import org.medipi.medication.ScheduledDose;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;

/**
 * Created by sam on 23/08/17.
 */
public class TestScheduleUtilities {

    Schedule testSchedule;
    ScheduledDose testDose1;
    ScheduledDose testDose2;
    ScheduledDose testDose3;
    @Before
    public void beforeTests() {
        testSchedule = new Schedule();
        testSchedule.setAssignedStartDate(Date.valueOf("2017-08-01"));
        testSchedule.setAssignedEndDate(Date.valueOf("2017-08-24"));

        testDose1 = new ScheduledDose();
        testDose2 = new ScheduledDose();
        testDose3 = new ScheduledDose();

        testDose1.setStartDay(0);
        testDose1.setRepeatInterval(5);
        testDose1.setEndDay(18);
        testDose1.setSchedule(testSchedule);
        testDose2.setStartDay(3);
        testDose2.setRepeatInterval(1);
        testDose2.setEndDay(null);
        testDose2.setSchedule(testSchedule);
        testDose3.setStartDay(2);
        testDose3.setRepeatInterval(null);
        testDose3.setEndDay(null);
        testDose3.setSchedule(testSchedule);

        testSchedule.getScheduledDoses().add(testDose1);
        testSchedule.getScheduledDoses().add(testDose2);
        testSchedule.getScheduledDoses().add(testDose3);
    }

    @Test
    public void dateInSchedule_convertToDayOfSchedule_correctAnswer() {
        assert ScheduleUtilities.toDayOfSchedule(testSchedule, LocalDate.of(2017, 8, 12)) == 11;
    }

    @Test(expected = IllegalArgumentException.class)
    public void beforeScheduleStart_convertToDayOfSchedule_throwsException() {
        ScheduleUtilities.toDayOfSchedule(testSchedule, LocalDate.of(2017, 7, 30));
    }

    @Test
    public void scheduleFirstDay_convertToDayOfSchedule_CorectAnswer() {
        assert ScheduleUtilities.toDayOfSchedule(testSchedule, LocalDate.of(2017, 8, 1)) == 0;
    }

    @Test
    public void afterScheduleEnd_convertToDayOfSchedule_CorectAnswer() {
        assert ScheduleUtilities.toDayOfSchedule(testSchedule, LocalDate.of(2017, 9, 1)) == 31;
    }

    @Test(expected = IllegalArgumentException.class)
    public void beforeScheduleStart_convertFromDayOfSchedule_throwsException() {
        ScheduleUtilities.fromDayOfSchedule(testSchedule, -1);
    }

    @Test
    public void scheduleFirstDay_convertFromDayOfSchedule_correctAnswer() {
        assert ScheduleUtilities.fromDayOfSchedule(testSchedule, 0).equals(LocalDate.of(2017, 8, 1));
    }

    @Test
    public void dayInSchedule_convertFromDayOfSchedule_correctAnswer() {
        assert ScheduleUtilities.fromDayOfSchedule(testSchedule, 5).equals(LocalDate.of(2017, 8, 6));
    }

    @Test
    public void afterScheduleEnd_convertFromDayOfSchedule_correctAnswer() {
        assert ScheduleUtilities.fromDayOfSchedule(testSchedule, 31).equals(LocalDate.of(2017, 9, 1));
    }

    @Test
    public void unoptimizedSchedule_optimizeEndDate_correctlyOptimized() {
        testSchedule.getScheduledDoses().remove(testDose2);
        assert ScheduleUtilities.getOptimizedEndDate(testSchedule).equals(Date.valueOf("2017-08-17"));
    }

    @Test
    public void boundedInfiniteSchedule_optimizeEndDate_unchanged() {
        assert ScheduleUtilities.getOptimizedEndDate(testSchedule).equals(Date.valueOf("2017-08-24"));
    }

    @Test
    public void truncatedSchedule_optimizeEndDate_unchanged() {
        testSchedule.setAssignedEndDate(Date.valueOf("2017-08-16"));
        assert ScheduleUtilities.getOptimizedEndDate(testSchedule).equals(Date.valueOf("2017-08-16"));
    }

    @Test
    public void unoptimizedSchedule_GetUnboundedEndDate_correctlyOptimized() {
        testSchedule.getScheduledDoses().remove(testDose2);
        assert ScheduleUtilities.getOptimizedUnboundedEndDate(testSchedule).equals(Date.valueOf("2017-08-17"));
    }

    @Test
    public void boundedInfiniteSchedule_getUnboundedEndDate_null() {
        assert ScheduleUtilities.getOptimizedUnboundedEndDate(testSchedule) == null;
    }

    @Test
    public void truncatedSchedule_getUnboundedEndDate_null() {
        testSchedule.setAssignedEndDate(Date.valueOf("2017-08-16"));
        assert ScheduleUtilities.getOptimizedUnboundedEndDate(testSchedule) == null;
    }

    @Test
    public void infiniteAsNeededSchedule_optimizeEndDate_null() {
        testSchedule.setScheduledDoses(new HashSet<>());
        testSchedule.setAssignedEndDate(null);
        assert ScheduleUtilities.getOptimizedEndDate(testSchedule) == null;
    }

    @Test
    public void asNeededSchedule_getUnboundedEndDate_null() {
        testSchedule.setScheduledDoses(new HashSet<>());
        assert ScheduleUtilities.getOptimizedUnboundedEndDate(testSchedule) == null;
    }

    @Test
    public void repeatIntervalGreaterThanTransposeAmount_transpose_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 4);
        assert testDose1.getStartDay() == 1;
        assert testDose1.getRepeatInterval() == 5;
        assert testDose1.getEndDay() == 14;
    }
    @Test
    public void repeatIntervalEqualToTransposeAmount_transpose_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 5);
        assert testDose1.getStartDay() == 0;
        assert testDose1.getRepeatInterval() == 5;
        assert testDose1.getEndDay() == 13;
    }
    @Test
    public void repeatIntervalLessThanTransposeAmount_transpose_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 6);
        assert testDose1.getStartDay() == 4;
        assert testDose1.getRepeatInterval() == 5;
        assert testDose1.getEndDay() == 12;
    }
    @Test
    public void transposeAmountZero_transpose_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 0);
        assert testDose1.getStartDay() == 0;
        assert testDose1.getRepeatInterval() == 5;
        assert testDose1.getEndDay() == 18;
    }
    @Test
    public void intervalOneNullEnd_transpose_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose2, 6);
        assert testDose1.getStartDay() == 0;
        assert testDose1.getRepeatInterval() == 1;
        assert testDose1.getEndDay() == null;
    }
    @Test
    public void nonRepeatingDose_transpose_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose3, 1);
        assert testDose1.getStartDay() == 1;
        assert testDose1.getRepeatInterval() == null;
        assert testDose1.getEndDay() == null;
    }
    @Test(expected = DoseOutOfBoundsException.class)
    public void transposeEqualsEndDay_transpose_throwsException() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 18);
    }
    @Test(expected = DoseOutOfBoundsException.class)
    public void transposeGreaterThanEndDay_transpose_throwsException() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 19);
    }
    @Test(expected = DoseOutOfBoundsException.class)
    public void transposeLessThanEndDay_transpose_throwsException() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 17);
    }
    @Test(expected = DoseOutOfBoundsException.class)
    public void transposeToDayAfterLastDose_transpose_throwsException() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 16);
    }
    @Test
    public void transposeToLastDose_transpose_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 15);
        assert testDose1.getStartDay() == 0;
        assert testDose1.getRepeatInterval() == 5;
        assert testDose1.getEndDay() == 3;
    }
    @Test(expected = DoseOutOfBoundsException.class)
    public void transposeGreaterThanEndDaySingleDose_transpose_throwsException() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose3, 3);
    }
    @Test
    public void transposeToEndDaySingleDose_transpose_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose3, 2);
        assert testDose3.getStartDay() == 0;
        assert testDose3.getRepeatInterval() == null;
    }
    @Test
    public void transposeBeforeScheduleStart_transpose_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose2, 2);
        assert testDose2.getStartDay() == 1;
        assert testDose2.getRepeatInterval() == 1;
        assert testDose2.getEndDay() == null;
    }
    @Test
    public void transposeToScheduleStart_transpose_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose2, 3);
        assert testDose2.getStartDay() == 0;
        assert testDose2.getRepeatInterval() == 1;
        assert testDose2.getEndDay() == null;
    }

    @Test
    public void moveEqualsEndDay_move_throwsException() throws DoseOutOfBoundsException{
        ScheduleUtilities.moveScheduleStartDate(testSchedule, LocalDate.of(2017,8,19));
        assert(testSchedule.getScheduledDoses().contains(testDose1) == false);
    }
    @Test(expected = DoseOutOfBoundsException.class)
    public void moveGreaterThanEndDay_move_throwsException() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 19);
    }
    @Test(expected = DoseOutOfBoundsException.class)
    public void transposeLessThanEndDay_move_throwsException() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 17);
    }
    @Test(expected = DoseOutOfBoundsException.class)
    public void transposeToDayAfterLastDose_move_throwsException() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 16);
    }
    @Test
    public void transposeToLastDose_move_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose1, 15);
        assert testDose1.getStartDay() == 0;
        assert testDose1.getRepeatInterval() == 5;
        assert testDose1.getEndDay() == 3;
    }
    @Test(expected = DoseOutOfBoundsException.class)
    public void transposeGreaterThanEndDaySingleDose_move_throwsException() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose3, 3);
    }
    @Test
    public void transposeToEndDaySingleDose_move_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose3, 2);
        assert testDose3.getStartDay() == 0;
        assert testDose3.getRepeatInterval() == null;
    }
    @Test
    public void transposeBeforeScheduleStart_move_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose2, 2);
        assert testDose2.getStartDay() == 1;
        assert testDose2.getRepeatInterval() == 1;
        assert testDose2.getEndDay() == null;
    }
    @Test
    public void transposeToScheduleStart_move_correctAnswer() throws DoseOutOfBoundsException{
        ScheduleUtilities.transposeDose(testDose2, 3);
        assert testDose2.getStartDay() == 0;
        assert testDose2.getRepeatInterval() == 1;
        assert testDose2.getEndDay() == null;
    }
}
