package ni.edu.uam.hibernateorm.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ni.edu.uam.hibernateorm.exception.CorreoDuplicadoException;
import ni.edu.uam.hibernateorm.exception.ValidacionEstudianteException;
import ni.edu.uam.hibernateorm.model.Carrera;
import ni.edu.uam.hibernateorm.model.Estudiante;
import ni.edu.uam.hibernateorm.repository.EstudianteRepository;
import ni.edu.uam.hibernateorm.repository.IEstudianteRepository;

import java.util.List;

public class EstudianteController {

    // ── Campos del formulario ────────────────────────────────────────────────
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private ComboBox<Carrera> cmbCarrera;

    // ── Tabla y columnas ─────────────────────────────────────────────────────
    @FXML private TableView<Estudiante> tablaEstudiantes;
    @FXML private TableColumn<Estudiante, Long>   colId;
    @FXML private TableColumn<Estudiante, String> colNombres;
    @FXML private TableColumn<Estudiante, String> colApellidos;
    @FXML private TableColumn<Estudiante, String> colCorreo;
    @FXML private TableColumn<Estudiante, String> colCarrera;
    @FXML private TableColumn<Estudiante, String> colTelefono;

    // ── Repositorio ──────────────────────────────────────────────────────────
    private final IEstudianteRepository repo = new EstudianteRepository();

    // ── Inicialización ───────────────────────────────────────────────────────

    @FXML
    public void initialize() {
        // Cargar valores del enum en el ComboBox
        cmbCarrera.setItems(FXCollections.observableArrayList(Carrera.values()));

        // Configurar columnas con PropertyValueFactory
        colId.setCellValueFactory(
                data -> new javafx.beans.property.SimpleLongProperty(
                        data.getValue().getId()).asObject());
        colNombres.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getNombres()));
        colApellidos.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getApellidos()));
        colCorreo.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCorreo()));
        colCarrera.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCarrera().getNombreMostrar()));
        colTelefono.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getTelefono()));
    }

    // ── RF01: Guardar ────────────────────────────────────────────────────────

    @FXML
    private void handleGuardar() {
        try {
            Estudiante estudiante = construirEstudiante();

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    repo.guardar(estudiante);
                    return null;
                }
            };

            task.setOnSucceeded(e -> {
                mostrarAlerta(Alert.AlertType.INFORMATION,
                        "Éxito", "Estudiante registrado correctamente.");
                limpiarFormulario();
                cargarTabla(repo.listar());
            });

            task.setOnFailed(e -> {
                Throwable causa = task.getException();
                if (causa instanceof CorreoDuplicadoException) {
                    mostrarAlerta(Alert.AlertType.ERROR,
                            "Correo duplicado", causa.getMessage());
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR,
                            "Error inesperado", causa.getMessage());
                }
            });

            new Thread(task).start();

        } catch (ValidacionEstudianteException ex) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", ex.getMessage());
        }
    }

    // ── RF02: Consultar todos ────────────────────────────────────────────────

    @FXML
    private void handleConsultar() {
        Task<List<Estudiante>> task = new Task<>() {
            @Override
            protected List<Estudiante> call() {
                return repo.listar();
            }
        };

        task.setOnSucceeded(e -> cargarTabla(task.getValue()));

        task.setOnFailed(e -> mostrarAlerta(Alert.AlertType.ERROR,
                "Error al consultar", task.getException().getMessage()));

        new Thread(task).start();
    }

    // ── Reto 3: Filtrar por carrera ──────────────────────────────────────────

    @FXML
    private void handleFiltrarPorCarrera() {
        Carrera carreraSeleccionada = cmbCarrera.getValue();
        if (carreraSeleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING,
                    "Selección requerida", "Seleccione una carrera para filtrar.");
            return;
        }

        Task<List<Estudiante>> task = new Task<>() {
            @Override
            protected List<Estudiante> call() {
                return repo.listarPorCarrera(carreraSeleccionada);
            }
        };

        task.setOnSucceeded(e -> cargarTabla(task.getValue()));

        task.setOnFailed(e -> mostrarAlerta(Alert.AlertType.ERROR,
                "Error al filtrar", task.getException().getMessage()));

        new Thread(task).start();
    }

    // ── Utilidades privadas ───────────────────────────────────────────────────

    /** Valida los campos y construye el objeto Estudiante. */
    private Estudiante construirEstudiante() {
        String nombres   = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String correo    = txtCorreo.getText().trim();
        String telefono  = txtTelefono.getText().trim();
        Carrera carrera  = cmbCarrera.getValue();

        if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || carrera == null) {
            throw new ValidacionEstudianteException(
                    "Nombres, apellidos, correo y carrera son obligatorios.");
        }

        return new Estudiante(nombres, apellidos, correo, carrera, telefono);
    }

    /** Carga una lista de estudiantes en la tabla. */
    private void cargarTabla(List<Estudiante> estudiantes) {
        ObservableList<Estudiante> datos = FXCollections.observableArrayList(estudiantes);
        tablaEstudiantes.setItems(datos);
    }

    /** Limpia todos los campos del formulario. */
    private void limpiarFormulario() {
        txtNombres.clear();
        txtApellidos.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        cmbCarrera.getSelectionModel().clearSelection();
    }

    /** Muestra un Alert con el tipo, título y mensaje indicados. */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
