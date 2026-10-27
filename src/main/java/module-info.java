module ni.edu.uam.hibernateorm {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    opens ni.edu.uam.hibernateorm to javafx.fxml;
    exports ni.edu.uam.hibernateorm;
}