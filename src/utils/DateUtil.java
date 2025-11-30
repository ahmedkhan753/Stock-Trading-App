package stockapp.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Utility class for date and time operations.
 * Provides helper methods for date formatting and conversions.
 */
public class DateUtil {
    
    // Date formatters
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
    
    /**
     * Gets current date as string in yyyy-MM-dd format.
     */
    public static String getCurrentDate() {
        return LocalDate.now().format(DATE_FORMATTER);
    }
    
    /**
     * Gets current date and time as string in yyyy-MM-dd HH:mm:ss format.
     */
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }
    
    /**
     * Gets current time as string in HH:mm:ss format.
     */
    public static String getCurrentTime() {
        return LocalDateTime.now().format(TIME_FORMATTER);
    }
    
    /**
     * Formats a Timestamp to display format (MMM dd, yyyy hh:mm a).
     */
    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return "N/A";
        }
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        return dateTime.format(DISPLAY_FORMATTER);
    }
    
    /**
     * Formats a Timestamp to date only format (yyyy-MM-dd).
     */
    public static String formatTimestampDate(Timestamp timestamp) {
        if (timestamp == null) {
            return "N/A";
        }
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        return dateTime.format(DATE_FORMATTER);
    }
    
    /**
     * Formats a Timestamp to time only format (HH:mm:ss).
     */
    public static String formatTimestampTime(Timestamp timestamp) {
        if (timestamp == null) {
            return "N/A";
        }
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        return dateTime.format(TIME_FORMATTER);
    }
    
    /**
     * Converts Date object to string in yyyy-MM-dd format.
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "N/A";
        }
        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
        return localDate.format(DATE_FORMATTER);
    }
    
    /**
     * Parses string date in yyyy-MM-dd format to LocalDate.
     */
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (Exception e) {
            System.err.println("ERROR: Invalid date format: " + dateString);
            return null;
        }
    }
    
    /**
     * Checks if a string is a valid date in yyyy-MM-dd format.
     */
    public static boolean isValidDate(String dateString) {
        try {
            LocalDate.parse(dateString, DATE_FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets current timestamp as SQL Timestamp.
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Gets a formatted string showing time difference from now.
     */
    public static String getTimeAgoString(Timestamp timestamp) {
        if (timestamp == null) {
            return "Unknown";
        }
        
        long millisAgo = System.currentTimeMillis() - timestamp.getTime();
        long secondsAgo = millisAgo / 1000;
        long minutesAgo = secondsAgo / 60;
        long hoursAgo = minutesAgo / 60;
        long daysAgo = hoursAgo / 24;
        
        if (secondsAgo < 60) {
            return "just now";
        } else if (minutesAgo < 60) {
            return minutesAgo + " minute" + (minutesAgo == 1 ? "" : "s") + " ago";
        } else if (hoursAgo < 24) {
            return hoursAgo + " hour" + (hoursAgo == 1 ? "" : "s") + " ago";
        } else if (daysAgo < 7) {
            return daysAgo + " day" + (daysAgo == 1 ? "" : "s") + " ago";
        } else {
            return formatTimestamp(timestamp);
        }
    }
}
