module ADVPR {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;            // Logging support
    requires java.sql;             // Often needed for database/AI dependencies
    requires org.apache.pdfbox;
    requires javafx.graphics;    // For your PDF reading features later

    opens ADVPR to javafx.fxml;
    exports ADVPR;
}