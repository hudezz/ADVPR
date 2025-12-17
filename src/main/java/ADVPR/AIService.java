package ADVPR;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AIService {

    // Helper method to talk to Ollama
    public String ask(String prompt) {
        try {
            // 1. Setup connection to Ollama (localhost:11434)
            URL url = new URL("http://localhost:11434/api/generate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // 2. Create the JSON Payload (Model: llama3.2)
            // Note: If you don't have llama3.2, change this to "llama2" or "mistral"
            String jsonInputString = String.format(
                    "{\"model\": \"llama3.2\", \"prompt\": \"%s\", \"stream\": false}",
                    prompt.replace("\"", "\\\"") // Escape quotes in prompt
            );

            // 3. Send Request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 4. Read Response
            if (conn.getResponseCode() != 200) {
                return "Error: Ollama returned code " + conn.getResponseCode();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            // 5. Extract just the "response" part from JSON (Simple parse)
            // A real JSON parser is better, but this works for basic text
            String raw = response.toString();
            int startIndex = raw.indexOf("\"response\":\"");
            if (startIndex != -1) {
                startIndex += 12; // Length of "response":"
                int endIndex = raw.indexOf("\",", startIndex);
                if (endIndex != -1) {
                    String answer = raw.substring(startIndex, endIndex);
                    return answer.replace("\\n", "\n").replace("\\\"", "\"");
                }
            }

            return "AI Response received (Raw): " + raw;

        } catch (Exception e) {
            e.printStackTrace();
            return "Connection Error: Is Ollama running? (" + e.getMessage() + ")";
        }
    }
}