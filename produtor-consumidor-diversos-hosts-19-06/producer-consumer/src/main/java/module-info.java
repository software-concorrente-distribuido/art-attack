module com.example.producer {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.producer to javafx.fxml;
    exports com.example.producer;
}