package com.jonathan.marsroverphotos.service;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileDateParseServiceTest {
    private FileDateParseService fdpService = new FileDateParseService();
    private DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("Y-MM-dd", Locale.ENGLISH);
    private static final String FILENAME = "./static/rover_test_inputs.txt";

    @Test
    void getDefaultDateList() {
        List<String> dates = new ArrayList<>();
        dates.add("2017-02-27");
        dates.add("2018-06-02");
        dates.add("2016-07-13");
//        dates.add("2018-04-31"); // This date will get converted to a valid date
        dates.add("2018-04-30");

        List<String> compareDates = new ArrayList<>();
        fdpService.getDefaultDateList().forEach(date -> compareDates.add(date.format(myFormat)));
        assertEquals(dates, compareDates);
    }

    @Test
    void getDateList() {
        List<String> dates = new ArrayList<>();
        dates.add("2016-11-02");
        dates.add("2019-06-08");
        dates.add("2016-12-13");
        dates.add("2018-02-28"); // This date geta converted to a valid date

        List<String> compareDates = new ArrayList<>();
        fdpService.getDateList(FILENAME).forEach(date -> compareDates.add(date.format(myFormat)));
        assertEquals(dates, compareDates);
    }
}