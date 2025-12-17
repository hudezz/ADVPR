package ADVPR;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * Local storage utilities.
 * Adapted for ADVPR package.
 */
public class StorageService {

    private final Path dataRoot;

    public StorageService() {
        this(Paths.get("data"));
    }

    public StorageService(Path dataRoot) {
        this.dataRoot = dataRoot;
    }

    public Path documentsDir() {
        return dataRoot.resolve("documents");
    }

    public Path knowledgeBaseDir() {
        return dataRoot.resolve("knowledge_base");
    }

    public Path knowledgeBaseTextFile() {
        return knowledgeBaseDir().resolve("knowledge_base.txt");
    }

    public void ensureDirectories() throws IOException {
        Files.createDirectories(documentsDir());
        Files.createDirectories(knowledgeBaseDir());
    }

    public String readKnowledgeBaseText() throws IOException {
        ensureDirectories();
        Path f = knowledgeBaseTextFile();
        if (!Files.exists(f)) {
            String starter = "University Admission Knowledge Base\n" +
                    "- Add or edit core admission information here.\n";
            Files.writeString(f, starter, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        }
        return Files.readString(f, StandardCharsets.UTF_8);
    }

    public void writeKnowledgeBaseText(String text) throws IOException {
        ensureDirectories();
        Files.writeString(knowledgeBaseTextFile(), text, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public Path saveUploadedDocument(Path sourceFile) throws IOException {
        ensureDirectories();
        if (sourceFile == null) {
            throw new IllegalArgumentException("sourceFile cannot be null");
        }
        String fileName = sourceFile.getFileName().toString();
        Path dest = documentsDir().resolve(fileName);

        if (Files.exists(dest)) {
            String base = fileName;
            String ext = "";
            int dot = fileName.lastIndexOf('.');
            if (dot > 0) {
                base = fileName.substring(0, dot);
                ext = fileName.substring(dot);
            }
            int i = 1;
            while (Files.exists(dest)) {
                dest = documentsDir().resolve(base + "_" + i + ext);
                i++;
            }
        }
        return Files.copy(sourceFile, dest, StandardCopyOption.COPY_ATTRIBUTES);
    }
}