package ADVPR;


import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.util.List;
import java.util.stream.Collectors;

public class AIService {

    // 1. We make the storage STATIC so it is shared across the whole app
    // (This fixes the issue where switching screens deletes the memory)
    private static final EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

    private final ChatLanguageModel chatModel;
    private final EmbeddingModel embeddingModel;

    public AIService() {
        // Ollama LLM (The Brain)
        this.chatModel = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama2")
                .temperature(0.3)
                .build();

        // Ollama Embedding Model (The Translator)
        this.embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("nomic-embed-text")
                .build();
    }

    /**
     * Goal 1: Feed the PDF text into the AI's Brain
     */
    public void addDocument(String textContent) {
        System.out.println("--- AI: Ingesting Document... ---");

        // Convert raw text to a document
        Document document = Document.from(textContent);

        // Split document into smaller chunks (so the AI can digest it)
        List<TextSegment> segments =
                DocumentSplitters.recursive(500, 50).split(document);

        // Embed and store chunks
        embeddingStore.addAll(
                embeddingModel.embedAll(segments).content(),
                segments
        );
        System.out.println("--- AI: Document Ingested (" + segments.size() + " chunks) ---");
    }

    /**
     * Goal 2: Ask the AI a question based on the document
     */
    public String getAnswer(String userQuestion) {

        // 1. Convert user question to numbers
        var queryEmbedding = embeddingModel.embed(userQuestion).content();

        // 2. Find the 5 most relevant chunks from the PDF
        List<EmbeddingMatch<TextSegment>> matches =
                embeddingStore.findRelevant(queryEmbedding, 5);

        // If the AI knows nothing about the topic:
        if (matches.isEmpty()) {
            return "I don't have any information on that yet. Please upload a PDF first.";
        }

        // 3. Combine the found chunks into a "Context" string
        String context = matches.stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.joining("\n"));

        // 4. Create the prompt for Llama2
        String prompt = """
                You are a helpful assistant.
                Answer the question using ONLY the context below.
                
                Context:
                %s
                
                Question:
                %s
                """.formatted(context, userQuestion);

        // 5. Generate answer
        return chatModel.generate(prompt);
    }
}