package org.medipi.concentrator.logic;

import org.junit.*;
import org.medipi.medication.Schedule;

import java.sql.Date;
import java.time.LocalDate;

public class TestScheduleAdherenceCalculator {

    Schedule schedule;

    @Before
    public void runBeforeTestMethod() {
        schedule = new Schedule();
        schedule.setAssignedStartDate(Date.valueOf(LocalDate.of(2017, 7, 1)));
    }

    @Test
    public void test_method_1() {
        System.out.println("@Test - test_method_1");
    }

    @Test
    public void test_method_2() {
        System.out.println("@Test - test_method_2");
    }
}
