package ADVPR;

import java.io.IOException;

public class PDFManager {

    // If you already have an AIService class shown in your screenshot, we use it here.
    private final AIService aiService = new AIService();

    /**
     * Called when you click "Re-Index PDF".
     * It tells the AI to read the files in your documents folder.
     */
    public void updateIndex() throws IOException {
        System.out.println("PDFManager: Starting Indexing Process...");

        // INTEGRATION NOTE:
        // This links your documents to the AI.
        // For now, we print to console so the app doesn't crash.
        // Later, you can add: aiService.ingestAllDocuments();
    }

    /**
     * Called when you ask a question in the Chat screen.
     */
    public String askQuestion(String question) {
        System.out.println("PDFManager: Asking AI -> " + question);

        // This tries to use your existing AIService.
        // If your AIService is empty, this might need updating.
        return aiService.ask(question);
    }
}