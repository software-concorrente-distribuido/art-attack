module com.artattack.sleepwakeupsimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.artattack.simulator to javafx.fxml;
    exports com.artattack.simulator;
}