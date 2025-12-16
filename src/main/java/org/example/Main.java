package org.example;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Starting Member 5 Test ---");
        // 1. Setup Managers

        DocumentManager docManager = new DocumentManager();
        HistoryManager historyManager = new HistoryManager();

        // CHANGE THIS to the name of the PDF you pasted in your project root
        File sourceFile = new File("test.pdf");

        if (sourceFile.exists()) {
            // 2. Test Saving Document
            System.out.println("\n[1] Testing Save Document...");
            File savedFile = docManager.saveDocument(sourceFile);

            // 3. Test Extracting Text
            if (savedFile != null) {
                System.out.println("\n[2] Testing Text Extraction...");
                String text = docManager.extractText(savedFile);

                // Print first 100 characters just to see if it worked
                System.out.println("Preview of extracted text: " +
                        (text.length() > 100 ? text.substring(0, 100) + "..." : text));

                // 4. Test Saving Extracted Text
                System.out.println("\n[3] Saving Extracted Text to File...");
                docManager.saveExtractedText(text, sourceFile.getName());
            }
        } else {
            System.err.println("Could not find test.pdf! Make sure you put a file in the project folder.");
        }

        // 5. Test Chat History
        System.out.println("\n[4] Testing History Manager...");
        historyManager.saveChatLog("User: Hello AI");
        historyManager.saveChatLog("Bot: Hello Student");

        System.out.println("\n--- Test Complete ---");
    }
}