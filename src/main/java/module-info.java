module com.example.a2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;

    exports com.example.a2;

    opens com.example.a2 to javafx.fxml;

//    exports com.example;
//    opens com.example to javafx.fxml;
}