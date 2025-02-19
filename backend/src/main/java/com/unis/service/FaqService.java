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

    // Crear/guardar nueva pregunta
    public void guardarPregunta(Faq faq) {
        entityManager.persist(faq);
    }

    // Listar todas las preguntas
    public List<Faq> listarPreguntas() {
        return entityManager.createQuery("SELECT f FROM Faq f", Faq.class)
                            .getResultList();
    }

    // Buscar por ID
    public Faq buscarPorId(Long id) {
        return entityManager.find(Faq.class, id);
    }

    // Actualizar pregunta/respuesta/autor/etc.
    public void actualizarFaq(Faq faq) {
        entityManager.merge(faq);
    }

    // Eliminar pregunta
    public boolean eliminarFaq(Long id) {
        Faq faq = entityManager.find(Faq.class, id);
        if (faq != null) {
            entityManager.remove(faq);
            return true;
        }
        return false;
    }
}
