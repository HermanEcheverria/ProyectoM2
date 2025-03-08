package com.unis.repository;
import jakarta.persistence.TypedQuery;
import com.unis.model.Receta;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RecetaRepository implements PanacheRepository<Receta> {

    public List<Receta> obtenerPorPaciente(Long idPaciente) {
        return list("idPaciente", idPaciente);
    }

    public List<Receta> obtenerPorDoctor(Long idDoctor) {
        return list("idDoctor", idDoctor);
    }

    

public Receta buscarPorIdCita(int idCita) {
    TypedQuery<Receta> query = getEntityManager().createQuery(
        "SELECT r FROM Receta r LEFT JOIN FETCH r.medicamentos WHERE r.idCita = :idCita", Receta.class);
    query.setParameter("idCita", idCita);
    return query.getSingleResult();
}


}
