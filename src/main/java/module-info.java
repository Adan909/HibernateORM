module ni.edu.uam.hibernateorm {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens ni.edu.uam.hibernateorm to javafx.fxml;
    opens ni.edu.uam.hibernateorm.controller to javafx.fxml;

    // Abierto a TODOS los módulos (incluido ByteBuddy de Hibernate 6)
    // para que pueda instanciar entidades via reflexión
    opens ni.edu.uam.hibernateorm.model;

    exports ni.edu.uam.hibernateorm;
    exports ni.edu.uam.hibernateorm.model;
    exports ni.edu.uam.hibernateorm.repository;
    exports ni.edu.uam.hibernateorm.exception;
}
