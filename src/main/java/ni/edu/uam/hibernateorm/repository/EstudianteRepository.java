package ni.edu.uam.hibernateorm.repository;

import ni.edu.uam.hibernateorm.config.HibernateUtil;
import ni.edu.uam.hibernateorm.exception.CorreoDuplicadoException;
import ni.edu.uam.hibernateorm.model.Carrera;
import ni.edu.uam.hibernateorm.model.Estudiante;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EstudianteRepository implements IEstudianteRepository {

    /** RF01 / RF05 — Guarda el estudiante o lanza CorreoDuplicadoException si el correo ya existe. */
    @Override
    public void guardar(Estudiante estudiante) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            // RF05: verificar correo duplicado antes de persistir
            Long count = session.createQuery(
                            "SELECT COUNT(e) FROM Estudiante e WHERE e.correo = :correo",
                            Long.class)
                    .setParameter("correo", estudiante.getCorreo())
                    .getSingleResult();

            if (count > 0) {
                throw new CorreoDuplicadoException(
                        "El correo '" + estudiante.getCorreo() + "' ya está registrado.");
            }

            session.persist(estudiante);
            tx.commit();
        } catch (CorreoDuplicadoException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    /** RF02 — Retorna todos los estudiantes registrados. */
    @Override
    public List<Estudiante> listar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Estudiante", Estudiante.class)
                    .getResultList();
        }
    }

    /** Reto 3 — HQL parametrizado para filtrar por carrera. */
    @Override
    public List<Estudiante> listarPorCarrera(Carrera carrera) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Estudiante e WHERE e.carrera = :carrera",
                            Estudiante.class)
                    .setParameter("carrera", carrera)
                    .getResultList();
        }
    }
}
