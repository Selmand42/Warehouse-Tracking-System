package Project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogHelper {
    private static final String LOG_FILE = "log.txt";
    
    public static void logAction(String action, String username) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(timestamp + " - " + username + " - " + action);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
