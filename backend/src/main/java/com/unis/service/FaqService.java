package com.unis.service;

import com.unis.model.Faq;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@ApplicationScoped
public class FaqService {

    @PersistenceContext
    EntityManager entityManager;

    public void guardarPregunta(Faq faq) {
        entityManager.persist(faq);
    }

    public List<Faq> listarPreguntas() {
        return entityManager.createQuery("SELECT f FROM Faq f", Faq.class).getResultList();
    }

    public void responderPregunta(Long id, String respuesta) {
        Faq faq = entityManager.find(Faq.class, id);
        if (faq != null) {
            faq.setRespuesta(respuesta);
            entityManager.merge(faq);
        }
    }

    //editar por admin
    public Faq buscarPorId(Long id) {
    return entityManager.find(Faq.class, id);
}

public void actualizarFaq(Faq faq) {
    entityManager.merge(faq);
}

public boolean eliminarFaq(Long id) {
    Faq faq = entityManager.find(Faq.class, id);
    if (faq != null) {
        entityManager.remove(faq);
        return true;
    }
    return false;
}

}
