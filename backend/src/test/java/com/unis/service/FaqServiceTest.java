package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Faq;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class FaqServiceTest {

    @Mock
    EntityManager entityManager;

    @Mock
    TypedQuery<Faq> query;

    @InjectMocks
    FaqService faqService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para guardar una pregunta (guardarPregunta)
    @Test
    public void testGuardarPregunta() {
        Faq faq = new Faq();
        // Se podría configurar propiedades de faq si se requiere
        faqService.guardarPregunta(faq);
        verify(entityManager, times(1)).persist(faq);
    }

    // Test para listar todas las preguntas (listarPreguntas)
    @Test
    public void testListarPreguntas() {
        List<Faq> expectedFaqs = Arrays.asList(new Faq(), new Faq());
        when(entityManager.createQuery("SELECT f FROM Faq f", Faq.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(expectedFaqs);

        List<Faq> result = faqService.listarPreguntas();
        assertEquals(expectedFaqs, result);
    }

    // Test para buscar una pregunta por ID (buscarPorId)
    @Test
    public void testBuscarPorId() {
        Faq faq = new Faq();
        Long id = 1L;
        when(entityManager.find(Faq.class, id)).thenReturn(faq);

        Faq result = faqService.buscarPorId(id);
        assertEquals(faq, result);
    }

    // Test para actualizar una FAQ (actualizarFaq)
    @Test
    public void testActualizarFaq() {
        Faq faq = new Faq();
        // Se puede configurar faq según sea necesario
        faqService.actualizarFaq(faq);
        verify(entityManager, times(1)).merge(faq);
    }

    // Test para eliminar una FAQ cuando se encuentra
    @Test
    public void testEliminarFaqFound() {
        Faq faq = new Faq();
        Long id = 1L;
        when(entityManager.find(Faq.class, id)).thenReturn(faq);

        boolean result = faqService.eliminarFaq(id);
        verify(entityManager, times(1)).remove(faq);
        assertTrue(result);
    }

    // Test para eliminar una FAQ cuando no se encuentra
    @Test
    public void testEliminarFaqNotFound() {
        Long id = 1L;
        when(entityManager.find(Faq.class, id)).thenReturn(null);

        boolean result = faqService.eliminarFaq(id);
        verify(entityManager, never()).remove(any(Faq.class));
        assertFalse(result);
    }
}
