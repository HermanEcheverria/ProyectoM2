package com.unis.controller;

import com.unis.model.Historia;
import com.unis.service.HistoriaService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class HistoriaControllerTest {

    @Mock
    HistoriaService historiaService;

    @InjectMocks
    HistoriaController controller;

    AutoCloseable mocks;

    @BeforeEach
    void init() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    // -------- Helpers --------
    private Historia h(Long id, String email, String status) {
        Historia hi = new Historia();
        hi.setId(id);
        hi.setEditorEmail(email);
        hi.setStatus(status);
        hi.setHistoria("texto");
        hi.setMeritos("meritos");
        hi.setLineaDelTiempo("linea");
        hi.setNombreEntidad("Entidad X");
        return hi;
    }

    // -------- GET /historias --------
    @Test
    void getHistorias_ok_lista() {
        List<Historia> data = Arrays.asList(h(1L, "ed@x.com", "PUBLICADO"), h(2L, "y@x.com", "PROCESO"));
        when(historiaService.listar()).thenReturn(data);

        List<Historia> out = controller.getHistorias();

        assertEquals(2, out.size());
        verify(historiaService).listar();
    }

    // -------- GET /historias/publicadas --------
    @Test
    void getHistoriasPublicadas_ok() {
        when(historiaService.listarPorEstado("PUBLICADO")).thenReturn(Collections.singletonList(h(1L, "a@a", "PUBLICADO")));

        List<Historia> out = controller.getHistoriasPublicadas();

        assertEquals(1, out.size());
        verify(historiaService).listarPorEstado("PUBLICADO");
    }

    // -------- GET /historias/{id} --------
    @Test
    void getHistoria_encontrada_200() {
        when(historiaService.obtenerPorId(10L)).thenReturn(h(10L, "a@a", "PROCESO"));

        Response r = controller.getHistoria(10L);

        assertEquals(200, r.getStatus());
        assertTrue(r.getEntity() instanceof Historia);
        verify(historiaService).obtenerPorId(10L);
    }

    @Test
    void getHistoria_noEncontrada_404() {
        when(historiaService.obtenerPorId(99L)).thenReturn(null);

        Response r = controller.getHistoria(99L);

        assertEquals(404, r.getStatus());
        verify(historiaService).obtenerPorId(99L);
    }

    // -------- POST /historias --------
    @Test
    void createHistoria_sinEditorEmail_400() {
        Historia in = h(null, "   ", "IGNORAR");
        Response r = controller.createHistoria(in);

        assertEquals(400, r.getStatus());
        verify(historiaService, never()).crear(any());
    }

    @Test
    void createHistoria_ok_201_yStatusProceso() {
        Historia in = h(null, "editor@site.com", null);
        Historia creado = h(5L, "editor@site.com", "PROCESO");
        when(historiaService.crear(any(Historia.class))).thenReturn(creado);

        Response r = controller.createHistoria(in);

        assertEquals(201, r.getStatus());
        Historia body = (Historia) r.getEntity();
        assertEquals("PROCESO", body.getStatus());
        assertEquals(5L, body.getId());
        verify(historiaService).crear(any(Historia.class));
    }

    // -------- PUT /historias/{id} (update) --------
    @Test
    void updateHistoria_noExiste_404() {
        when(historiaService.obtenerPorId(77L)).thenReturn(null);

        Response r = controller.updateHistoria(77L, h(null, "ed@ed.com", "PUBLICADO"));

        assertEquals(404, r.getStatus());
        verify(historiaService).obtenerPorId(77L);
        verify(historiaService, never()).actualizar(anyLong(), any());
    }

    @Test
    void updateHistoria_sinEditorEmail_400() {
        when(historiaService.obtenerPorId(7L)).thenReturn(h(7L, "old@old", "PROCESO"));

        Historia actualizado = h(null, null, "PUBLICADO"); // editorEmail null -> 400
        Response r = controller.updateHistoria(7L, actualizado);

        assertEquals(400, r.getStatus());
        verify(historiaService).obtenerPorId(7L);
        verify(historiaService, never()).actualizar(anyLong(), any());
    }

    @Test
    void updateHistoria_ok_200_parcial() {
        Historia existente = h(8L, "old@old", "PROCESO");
        when(historiaService.obtenerPorId(8L)).thenReturn(existente);
        when(historiaService.actualizar(eq(8L), any(Historia.class)))
                .thenAnswer(inv -> inv.getArgument(1)); // devuelve el mismo objeto actualizado

        Historia patch = new Historia();
        patch.setEditorEmail("nuevo@site.com"); // requerido
        patch.setNombreEntidad("Nueva Entidad");
        patch.setStatus("PUBLICADO"); // se permite actualizar
        patch.setRejectionReason("motivo"); // también se puede

        Response r = controller.updateHistoria(8L, patch);

        assertEquals(200, r.getStatus());
        Historia out = (Historia) r.getEntity();
        assertEquals("Nueva Entidad", out.getNombreEntidad());
        assertEquals("PUBLICADO", out.getStatus());
        assertEquals("motivo", out.getRejectionReason());
        assertEquals("nuevo@site.com", out.getEditorEmail());
        verify(historiaService).actualizar(eq(8L), any(Historia.class));
    }

    // -------- DELETE /historias/{id} --------
    @Test
    void deleteHistoria_ok_204() {
        when(historiaService.eliminar(3L)).thenReturn(true);

        Response r = controller.deleteHistoria(3L);

        assertEquals(204, r.getStatus());
        assertNull(r.getEntity());
        verify(historiaService).eliminar(3L);
    }

    @Test
    void deleteHistoria_noExiste_404() {
        when(historiaService.eliminar(4L)).thenReturn(false);

        Response r = controller.deleteHistoria(4L);

        assertEquals(404, r.getStatus());
        verify(historiaService).eliminar(4L);
    }

    // -------- GET /historias/pendientes --------
    @Test
    void getPendientesModeracion_ok() {
        when(historiaService.listarPorEstado("PROCESO")).thenReturn(Collections.emptyList());

        List<Historia> out = controller.getPendientesModeracion();

        assertNotNull(out);
        verify(historiaService).listarPorEstado("PROCESO");
    }

    // -------- PUT /historias/aprobar/{id} --------
    @Test
    void aprobarHistoria_noExiste_404() {
        when(historiaService.obtenerPorId(12L)).thenReturn(null);

        Response r = controller.aprobarHistoria(12L);

        assertEquals(404, r.getStatus());
        verify(historiaService).obtenerPorId(12L);
        verify(historiaService, never()).actualizar(anyLong(), any());
    }

    @Test
    void aprobarHistoria_ok_200_statusPublicado_yLimpiaRechazo() {
        Historia existente = h(13L, "ed@site", "PROCESO");
        existente.setRejectionReason("antes");
        when(historiaService.obtenerPorId(13L)).thenReturn(existente);
        when(historiaService.actualizar(eq(13L), any(Historia.class)))
                .thenAnswer(inv -> inv.getArgument(1));

        Response r = controller.aprobarHistoria(13L);

        assertEquals(200, r.getStatus());
        Historia out = (Historia) r.getEntity();
        assertEquals("PUBLICADO", out.getStatus());
        assertNull(out.getRejectionReason());
        verify(historiaService).actualizar(eq(13L), any(Historia.class));
    }

    // -------- PUT /historias/rechazar/{id}?motivo=... --------
    @Test
    void rechazarHistoria_noExiste_404() {
        when(historiaService.obtenerPorId(14L)).thenReturn(null);

        Response r = controller.rechazarHistoria(14L, "incompleto");

        assertEquals(404, r.getStatus());
        verify(historiaService).obtenerPorId(14L);
        verify(historiaService, never()).actualizar(anyLong(), any());
    }

    @Test
    void rechazarHistoria_ok_200_statusRechazado_conMotivo() {
        Historia existente = h(15L, "ed@site", "PROCESO");
        when(historiaService.obtenerPorId(15L)).thenReturn(existente);
        when(historiaService.actualizar(eq(15L), any(Historia.class)))
                .thenAnswer(inv -> inv.getArgument(1));

        Response r = controller.rechazarHistoria(15L, "contenido insuficiente");

        assertEquals(200, r.getStatus());
        Historia out = (Historia) r.getEntity();
        assertEquals("RECHAZADO", out.getStatus());
        assertEquals("contenido insuficiente", out.getRejectionReason());
        verify(historiaService).actualizar(eq(15L), any(Historia.class));
    }
    @Test
void updateHistoria_editorEmailEnBlanco_400() {
    // existe el registro
    when(historiaService.obtenerPorId(21L)).thenReturn(h(21L, "old@old", "PROCESO"));

    // editorEmail con solo espacios -> debe disparar el 400 de trim().isEmpty()
    Historia patch = new Historia();
    patch.setEditorEmail("   "); // <- blanco

    Response r = controller.updateHistoria(21L, patch);

    assertEquals(400, r.getStatus());
    String msg = String.valueOf(r.getEntity());
    assertTrue(msg.contains("editorEmail"), "Debe indicar que editorEmail es requerido");
    verify(historiaService, never()).actualizar(anyLong(), any());
}
@Test
void updateHistoria_ok_200_actualizaTodosLosCamposDeTexto() {
    // entidad existente inicial
    Historia existente = h(30L, "old@old.com", "PROCESO");
    existente.setNombreEntidad("Vieja Entidad");
    existente.setHistoria("historia vieja");
    existente.setMeritos("meritos viejos");
    existente.setLineaDelTiempo("linea vieja");
    existente.setRejectionReason("rechazo viejo");

    when(historiaService.obtenerPorId(30L)).thenReturn(existente);
    when(historiaService.actualizar(eq(30L), any(Historia.class)))
            .thenAnswer(inv -> inv.getArgument(1));

    // patch con TODOS los campos condicionales no nulos
    Historia patch = new Historia();
    patch.setEditorEmail("nuevo@site.com");     // requerido para pasar validación
    patch.setNombreEntidad("Nueva Entidad");    // if != null
    patch.setHistoria("historia nueva");        // if != null
    patch.setMeritos("meritos nuevos");         // if != null
    patch.setLineaDelTiempo("linea nueva");     // if != null
    patch.setStatus("PUBLICADO");               // if != null
    patch.setRejectionReason("nuevo motivo");   // if != null

    Response r = controller.updateHistoria(30L, patch);

    assertEquals(200, r.getStatus());
    Historia out = (Historia) r.getEntity();

    assertEquals("Nueva Entidad", out.getNombreEntidad());
    assertEquals("historia nueva", out.getHistoria());
    assertEquals("meritos nuevos", out.getMeritos());
    assertEquals("linea nueva", out.getLineaDelTiempo());
    assertEquals("PUBLICADO", out.getStatus());
    assertEquals("nuevo motivo", out.getRejectionReason());
    assertEquals("nuevo@site.com", out.getEditorEmail());

    verify(historiaService).actualizar(eq(30L), any(Historia.class));
}


}
