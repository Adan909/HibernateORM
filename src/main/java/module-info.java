module ni.edu.uam.hibernateorm {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.hibernateorm to javafx.fxml;
    exports ni.edu.uam.hibernateorm;
}