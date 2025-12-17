package ADVPR;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DocumentManager {

    // Paths to our folders
    private static final String DOCUMENTS_DIR = "data/documents/";
    private static final String EXTRACTED_DIR = "data/extracted/";

    /**
     * Goal 1: Save the uploaded file to the local data folder.
     */
    public File saveDocument(File file) {
        try {
            // Ensure the directory exists
            Path targetDir = Paths.get(DOCUMENTS_DIR);
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            // Create the path for the new file
            Path targetPath = targetDir.resolve(file.getName());

            // Copy the file (replace if it already exists)
            Files.copy(file.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File saved successfully at: " + targetPath.toAbsolutePath());
            return targetPath.toFile();

        } catch (IOException e) {
            System.err.println("Error saving document: " + e.getMessage());
            return null;
        }
    }

    /**
     * Goal 2: Extract text from the PDF file using Apache PDFBox.
     */
    public String extractText(File pdfFile) {
        String extractedText = "";
        // PDDocument.load parses the PDF file
        try (PDDocument document = PDDocument.load(pdfFile)) {
            // PDFTextStripper pulls the text out
            PDFTextStripper stripper = new PDFTextStripper();
            extractedText = stripper.getText(document);
        } catch (IOException e) {
            System.err.println("Error extracting text: " + e.getMessage());
        }
        return extractedText;
    }

    /**
     * Helper: Save the extracted text to a .txt file (Required by your Definition of Done).
     */
    public void saveExtractedText(String text, String originalFileName) {
        try {
            // Create a filename.txt based on original PDF name
            String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            Path targetPath = Paths.get(EXTRACTED_DIR, baseName + ".txt");

            // Ensure directory exists
            if (!Files.exists(targetPath.getParent())) {
                Files.createDirectories(targetPath.getParent());
            }

            // Write the text string to the file
            Files.writeString(targetPath, text);
            System.out.println("Extracted text saved to: " + targetPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}