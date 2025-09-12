package com.unis.controller;

import com.unis.model.Faq;
import com.unis.service.FaqService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FaqControllerTest {

    @Mock
    private FaqService faqService;

    @InjectMocks
    private FaqController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarPreguntas_devuelveLista() {
        when(faqService.listarPreguntas()).thenReturn(Arrays.asList(new Faq(), new Faq()));
        List<Faq> result = controller.listarPreguntas();
        assertEquals(2, result.size());
        verify(faqService).listarPreguntas();
    }

    @Test
    void obtenerFaqPorId_encontrado() {
        Faq faq = new Faq();
        // No llamar a setId; la entidad no lo tiene
        when(faqService.buscarPorId(1L)).thenReturn(faq);

        Response r = controller.obtenerFaqPorId(1L);

        assertEquals(200, r.getStatus());
        assertSame(faq, r.getEntity());
    }

    @Test
    void obtenerFaqPorId_noEncontrado() {
        when(faqService.buscarPorId(99L)).thenReturn(null);

        Response r = controller.obtenerFaqPorId(99L);

        assertEquals(404, r.getStatus());
    }

    @Test
    void guardarPregunta_valida_retorna201() {
        Faq faq = new Faq();
        faq.setPregunta("¿Cuál es tu nombre?");
        faq.setEditadoPor("editor@mail.com");

        Response r = controller.guardarPregunta(faq);

        assertEquals(201, r.getStatus());
        assertSame(faq, r.getEntity());
        assertEquals("PROCESO", faq.getStatus());
        verify(faqService).guardarPregunta(faq);
    }

    @Test
    void guardarPregunta_invalidaPreguntaVacia() {
        Faq faq = new Faq();
        faq.setPregunta("   ");
        faq.setEditadoPor("editor@mail.com");

        Response r = controller.guardarPregunta(faq);

        assertEquals(400, r.getStatus());
        verify(faqService, never()).guardarPregunta(any());
    }

    @Test
    void guardarPregunta_sinEditorEmail_lanzaExcepcion() {
        Faq faq = new Faq();
        faq.setPregunta("Pregunta");

        assertThrows(IllegalArgumentException.class, () -> controller.guardarPregunta(faq));
    }

    @Test
    void editarPregunta_noEncontrada() {
        when(faqService.buscarPorId(1L)).thenReturn(null);

        Response r = controller.editarPregunta(1L, new Faq());

        assertEquals(404, r.getStatus());
    }

    @Test
    void editarPregunta_editorEmailInvalido() {
        Faq existente = new Faq();
        when(faqService.buscarPorId(1L)).thenReturn(existente);

        Faq entrada = new Faq();
        entrada.setEditadoPor("   ");

        Response r = controller.editarPregunta(1L, entrada);

        assertEquals(400, r.getStatus());
    }

    @Test
    void editarPregunta_ok_200_actualizaCampos() {
        Faq existente = new Faq();
        existente.setPregunta("Vieja");
        existente.setRespuesta("Vieja resp");
        existente.setAutor("Autor");
        existente.setStatus("PROCESO");
        existente.setRejectionReason("x");

        when(faqService.buscarPorId(1L)).thenReturn(existente);

        Faq entrada = new Faq();
        entrada.setPregunta("Nueva?");
        entrada.setRespuesta("Resp nueva");
        entrada.setAutor("Nuevo autor");
        entrada.setEditadoPor("editor@mail.com");
        entrada.setStatus("PUBLICADO");
        entrada.setRejectionReason(null); // no se sobrescribe porque es null

        Response r = controller.editarPregunta(1L, entrada);

        assertEquals(200, r.getStatus());
        Faq actualizado = (Faq) r.getEntity();
        assertEquals("Nueva?", actualizado.getPregunta());
        assertEquals("Resp nueva", actualizado.getRespuesta());
        assertEquals("Nuevo autor", actualizado.getAutor());
        assertEquals("editor@mail.com", actualizado.getEditadoPor());
        assertEquals("PUBLICADO", actualizado.getStatus());
        assertEquals("x", actualizado.getRejectionReason()); // se mantiene "x"
        verify(faqService).actualizarFaq(existente);
    }

    @Test
    void editarPregunta_ok_200_actualizaMotivoRechazo_siVieneNoNulo() {
        Faq existente = new Faq();
        existente.setRejectionReason("viejo");
        when(faqService.buscarPorId(2L)).thenReturn(existente);

        Faq entrada = new Faq();
        entrada.setEditadoPor("editor@mail.com");
        entrada.setRejectionReason("nuevo");

        Response r = controller.editarPregunta(2L, entrada);

        assertEquals(200, r.getStatus());
        Faq actualizado = (Faq) r.getEntity();
        assertEquals("nuevo", actualizado.getRejectionReason());
        verify(faqService).actualizarFaq(existente);
    }

    @Test
    void eliminarPregunta_existe() {
        when(faqService.eliminarFaq(1L)).thenReturn(true);

        Response r = controller.eliminarPregunta(1L);

        assertEquals(200, r.getStatus());
        verify(faqService).eliminarFaq(1L);
    }

    @Test
    void eliminarPregunta_noExiste() {
        when(faqService.eliminarFaq(1L)).thenReturn(false);

        Response r = controller.eliminarPregunta(1L);

        assertEquals(404, r.getStatus());
    }

    @Test
    void listarPendientes_ok() {
        when(faqService.listarPorEstado("PROCESO")).thenReturn(Arrays.asList(new Faq()));

        List<Faq> result = controller.listarPendientes();

        assertEquals(1, result.size());
        verify(faqService).listarPorEstado("PROCESO");
    }

    @Test
    void aprobarPregunta_existe() {
        Faq faq = new Faq();
        when(faqService.buscarPorId(1L)).thenReturn(faq);

        Response r = controller.aprobarPregunta(1L);

        assertEquals(200, r.getStatus());
        Faq actualizado = (Faq) r.getEntity();
        assertEquals("PUBLICADO", actualizado.getStatus());
        assertNull(actualizado.getRejectionReason());
        verify(faqService).actualizarFaq(faq);
    }

    @Test
    void aprobarPregunta_noExiste() {
        when(faqService.buscarPorId(1L)).thenReturn(null);

        Response r = controller.aprobarPregunta(1L);

        assertEquals(404, r.getStatus());
    }

    @Test
    void rechazarPregunta_existe() {
        Faq faq = new Faq();
        when(faqService.buscarPorId(1L)).thenReturn(faq);

        Response r = controller.rechazarPregunta(1L, "motivo");

        assertEquals(200, r.getStatus());
        Faq actualizado = (Faq) r.getEntity();
        assertEquals("RECHAZADO", actualizado.getStatus());
        assertEquals("motivo", actualizado.getRejectionReason());
        verify(faqService).actualizarFaq(faq);
    }

    @Test
    void rechazarPregunta_noExiste() {
        when(faqService.buscarPorId(1L)).thenReturn(null);

        Response r = controller.rechazarPregunta(1L, "motivo");

        assertEquals(404, r.getStatus());
    }
}
