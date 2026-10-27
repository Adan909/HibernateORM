package ni.edu.uam.hibernateorm.model;

public enum Carrera {
    INGENIERIA_SISTEMAS("Ingeniería de Sistemas"),
    INGENIERIA_INDUSTRIAL("Ingeniería Industrial"),
    ADMINISTRACION("Administración de Empresas"),
    MEDICINA("Medicina");

    private final String nombreMostrar;

    Carrera(String nombreMostrar) {
        this.nombreMostrar = nombreMostrar;
    }

    public String getNombreMostrar() {
        return nombreMostrar;
    }

    @Override
    public String toString() {
        return nombreMostrar;
    }
}