module com.example.bufferserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bufferserver to javafx.fxml;
    exports com.example.bufferserver;
}