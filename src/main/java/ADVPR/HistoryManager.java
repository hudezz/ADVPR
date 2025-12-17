package ADVPR;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class HistoryManager {

    private static final Path HISTORY_FILE = Paths.get("data/chat_history.json");

    /**
     * Goal 3: Append a chat log to the history file.
     */
    public void saveChatLog(String logEntry) {
        try {
            // Ensure the directory exists
            if (HISTORY_FILE.getParent() != null && !Files.exists(HISTORY_FILE.getParent())) {
                Files.createDirectories(HISTORY_FILE.getParent());
            }

            // Prepare the string to write (add a new line at the end)
            String contentToAppend = logEntry + "\n";

            // Write to file (Create if missing, Append if exists)
            Files.writeString(HISTORY_FILE, contentToAppend,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            System.out.println("Chat log saved.");

        } catch (IOException e) {
            System.err.println("Error saving chat log: " + e.getMessage());
        }
    }
}