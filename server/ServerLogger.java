package server;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogger {

    private static final String LOG_FILE_PATH = "server.log";
    private static FileWriter fileWriter;

    static {
        try {
            fileWriter = new FileWriter(LOG_FILE_PATH, true); // Append to existing log
        } catch (IOException e) {
            System.err.println("Error opening log file: " + e.getMessage());
        }
    }

    public static void log(String message) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = LocalDateTime.now().format(formatter);
            fileWriter.write("[" + timestamp + "] " + message + "\n");
            fileWriter.flush();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}