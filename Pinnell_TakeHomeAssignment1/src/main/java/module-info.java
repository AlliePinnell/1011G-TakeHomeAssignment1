module com.example.takehomeassignment1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.takehomeassignment1 to javafx.fxml, com.google.gson;
    exports com.example.takehomeassignment1;
}