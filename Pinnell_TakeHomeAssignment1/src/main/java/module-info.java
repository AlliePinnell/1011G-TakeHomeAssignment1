module com.example.pinnell_takehomeassignment1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pinnell_takehomeassignment1 to javafx.fxml;
    exports com.example.pinnell_takehomeassignment1;
}