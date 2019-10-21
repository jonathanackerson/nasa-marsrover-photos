package com.jonathan.marsroverphotos.service;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service to parse dates from a file
 *
 * Return an Array of formatted dates
 */
public class FileDateParseService {
    private static final String FILENAME = "static/rover_inputs.txt"; // static date resource
    static final DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("Y-MM-dd", Locale.ENGLISH);

    private List<LocalDate> defaultDates;

    /**
     * @return List of LocalDate parsed from file
     */
    public List<LocalDate> getDefaultDateList() {
        return defaultDates;
    }

    /**
     * @param filename Custom file or input to parse
     *
     * @return List of LocalDate parsed from file
     */
    public List<LocalDate> getDateList(String filename) {
        List<String> lines = readFileAsList(filename);
        return listToDates(lines);
    }

    public FileDateParseService() {
        List<String> lines = readFileAsList(FILENAME);
        defaultDates = listToDates(lines);
    }

    private static List<String> readFileAsList(String filename) {
        ClassLoader classLoader = MarsRoverService.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));

        List<String> lines = reader.lines()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .flatMap(s -> Stream.of(s.split("/n")))
                .collect(Collectors.toList());

        // print lines read in from file
        // lines.forEach(System.out::println);

        return lines;
    }

    private static List<LocalDate> listToDates(List<String> lines) {
        List<LocalDate> dates = new ArrayList<>();

        // mm/dd/yy
        DateTimeFormatter parser = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("M/d/yy"))
                .appendOptional(DateTimeFormatter.ofPattern("LLLL d, u"))
                .appendOptional(DateTimeFormatter.ofPattern("LLL-d-u"))
                .appendOptional(DateTimeFormatter.ISO_LOCAL_DATE)
                .toFormatter();

        lines.forEach(line -> {
            try {
                dates.add(LocalDate.parse(line, parser));
            } catch (DateTimeException e) {
                System.out.println("Cant convert this line to date - " + line);
            }
        });

        // print converted date formats found
        // dates.forEach(d -> System.out.println(d.format(myFormat)));

        return dates;
    }
}
