module com.mycompany.sportsequipmentsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; 

    opens com.mycompany.sportsequipmentsystem to javafx.fxml;
    exports com.mycompany.sportsequipmentsystem;
}