module com.example.mavenjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.jsoup;

    opens com.example.mavenjavafx to javafx.fxml;
    exports com.example.mavenjavafx;
}