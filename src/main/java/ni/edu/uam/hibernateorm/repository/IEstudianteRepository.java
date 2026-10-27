package ni.edu.uam.hibernateorm.repository;

import ni.edu.uam.hibernateorm.model.Carrera;
import ni.edu.uam.hibernateorm.model.Estudiante;

import java.util.List;

public interface IEstudianteRepository {

    /** RF01 — Persiste un nuevo estudiante en la base de datos. */
    void guardar(Estudiante estudiante);

    /** RF02 — Retorna todos los estudiantes registrados. */
    List<Estudiante> listar();

    /** Reto 3 — Filtra estudiantes por carrera usando HQL parametrizado. */
    List<Estudiante> listarPorCarrera(Carrera carrera);
}
