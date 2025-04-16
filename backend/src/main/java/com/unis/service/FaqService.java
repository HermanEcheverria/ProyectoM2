package com.unis.service;

import com.unis.model.Faq;
import com.unis.repository.FaqRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class FaqService {

    @Inject
    FaqRepository faqRepository;

    /**
     * Guardar una nueva pregunta.
     */
    @Transactional
    public void guardarPregunta(Faq faq) {
        if (faq.getEditadoPor() == null || faq.getEditadoPor().isBlank()) {
            throw new IllegalArgumentException("El campo 'editadoPor' es obligatorio.");
        }
        faqRepository.persist(faq);
    }

    /**
     * Listar todas las preguntas (sin filtro).
     */
    public List<Faq> listarPreguntas() {
        return faqRepository.listAll();
    }

    /**
     * Buscar una pregunta por ID.
     */
    public Faq buscarPorId(Long id) {
        return faqRepository.findById(id);
    }

    /**
     * Actualizar una pregunta existente.
     */
    @Transactional
    public void actualizarFaq(Faq faq) {
        if (faq.getEditadoPor() == null || faq.getEditadoPor().isBlank()) {
            throw new IllegalArgumentException("El campo 'editadoPor' es obligatorio para actualizar.");
        }
        faqRepository.getEntityManager().merge(faq);
    }

    /**
     * Eliminar una pregunta por ID.
     */
    @Transactional
    public boolean eliminarFaq(Long id) {
        return faqRepository.deleteById(id);
    }

    /**
     * Listar preguntas filtradas por estado (PROCESO, PUBLICADO, RECHAZADO).
     */
    public List<Faq> listarPorEstado(String estado) {
        return faqRepository.findByStatus(estado);
    }
}
