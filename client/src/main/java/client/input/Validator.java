package client.input;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Validator {

    public static String requireNonEmpty(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " не может быть пустым");
        }
        return input;
    }

    public static double parseDouble(String input) {
        double val = Double.parseDouble(input);
        if (val <= 0) throw new IllegalArgumentException("Должен быть > 0");
        return val;
    }

    public static LocalDateTime parseDate(String input) {
        try {
            return LocalDateTime.parse(input);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(input).atStartOfDay();
        }
    }

    public static long parseLong(String input) {
        return Long.parseLong(input);
    }

    public static int parseInt(String input) {
        return Integer.parseInt(input);
    }
}