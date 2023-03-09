module com.example.miguelgarciasoftwareiisubission {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.miguelgarciasoftwareiisubission to javafx.fxml;
    exports com.example.miguelgarciasoftwareiisubission;
    exports Model;
    opens Model to javafx.fxml;
}