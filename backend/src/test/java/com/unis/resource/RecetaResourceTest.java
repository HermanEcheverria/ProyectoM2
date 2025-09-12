package com.unis.resource;

import com.unis.dto.RecetaDTO;
import com.unis.model.Paciente;
import com.unis.model.Receta;
import com.unis.model.RecetaMedicamento;
import com.unis.service.RecetaService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RecetaResourceTest {

    @Mock
    RecetaService recetaService;

    @InjectMocks
    RecetaResource resource;

    AutoCloseable mocks;

    @BeforeEach
    void init() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    // ---------- POST /recetas (crearReceta) ----------

    @Test
    void crearReceta_ok_200_conEntidad() {
        Receta entrada = new Receta();
        Receta creada = new Receta();
        when(recetaService.crearReceta(entrada)).thenReturn(creada);

        Response r = resource.crearReceta(entrada);

        assertEquals(200, r.getStatus());
        assertSame(creada, r.getEntity());
        verify(recetaService).crearReceta(entrada);
    }

    @Test
    void crearReceta_error_500_conMensaje() {
        Receta entrada = new Receta();
        when(recetaService.crearReceta(any())).thenThrow(new RuntimeException("boom"));

        Response r = resource.crearReceta(entrada);

        assertEquals(500, r.getStatus());
        assertTrue(String.valueOf(r.getEntity()).contains("Error al crear la receta"));
        verify(recetaService).crearReceta(entrada);
    }

    // ---------- GET /recetas/cita/{idCita} ----------

    @Test
    void obtenerRecetaPorIdCita_encontrada_200() {
        Receta rec = new Receta();
        when(recetaService.buscarPorIdCita(7)).thenReturn(rec);

        Response r = resource.obtenerRecetaPorIdCita(7);

        assertEquals(200, r.getStatus());
        assertSame(rec, r.getEntity());
        verify(recetaService).buscarPorIdCita(7);
    }

    @Test
    void obtenerRecetaPorIdCita_noEncontrada_404() {
        when(recetaService.buscarPorIdCita(99)).thenReturn(null);

        Response r = resource.obtenerRecetaPorIdCita(99);

        assertEquals(404, r.getStatus());
        assertTrue(String.valueOf(r.getEntity()).contains("Receta no encontrada"));
        verify(recetaService).buscarPorIdCita(99);
    }

    // ---------- PUT /recetas/{idReceta} (actualizarReceta) ----------

    @Test
    void actualizarReceta_ok_200_conEntidad() {
        Receta upd = new Receta();
        Receta salida = new Receta();
        when(recetaService.actualizarReceta(5L, upd)).thenReturn(salida);

        Response r = resource.actualizarReceta(5L, upd);

        assertEquals(200, r.getStatus());
        assertSame(salida, r.getEntity());
        verify(recetaService).actualizarReceta(5L, upd);
    }

    @Test
    void actualizarReceta_error_500_conMensaje() {
        when(recetaService.actualizarReceta(eq(8L), any(Receta.class)))
                .thenThrow(new RuntimeException("falló"));

        Response r = resource.actualizarReceta(8L, new Receta());

        assertEquals(500, r.getStatus());
        assertTrue(String.valueOf(r.getEntity()).contains("Error al actualizar la receta"));
        verify(recetaService).actualizarReceta(eq(8L), any(Receta.class));
    }

    // ---------- POST /recetas/medicamentos (agregarMedicamento) ----------

    @Test
    void agregarMedicamento_ok_200_conEntidad() {
        RecetaMedicamento in = new RecetaMedicamento();
        RecetaMedicamento out = new RecetaMedicamento();
        when(recetaService.agregarMedicamento(in)).thenReturn(out);

        Response r = resource.agregarMedicamento(in);

        assertEquals(200, r.getStatus());
        assertSame(out, r.getEntity());
        verify(recetaService).agregarMedicamento(in);
    }

    @Test
    void agregarMedicamento_error_500_conMensaje() {
        when(recetaService.agregarMedicamento(any())).thenThrow(new RuntimeException("x"));

        Response r = resource.agregarMedicamento(new RecetaMedicamento());

        assertEquals(500, r.getStatus());
        assertTrue(String.valueOf(r.getEntity()).contains("Error al agregar medicamento"));
        verify(recetaService).agregarMedicamento(any());
    }

    // ---------- GET /recetas/{codigoReceta} (obtenerRecetaPorCodigo) ----------

    @Test
    void obtenerRecetaPorCodigo_encontrada_200_devuelveDTO_conNombrePaciente() {
        // Arrange: Receta con Paciente(nombre) para que el recurso construya el DTO
        Receta receta = mock(Receta.class);
        Paciente paciente = mock(Paciente.class);
        when(paciente.getNombre()).thenReturn("Juan Perez");
        when(receta.getPaciente()).thenReturn(paciente);
        when(recetaService.buscarPorCodigo("ABC123")).thenReturn(receta);

        // Act
        Response r = resource.obtenerRecetaPorCodigo("ABC123");

        // Assert
        assertEquals(200, r.getStatus());
        Object body = r.getEntity();
        assertNotNull(body);
        assertTrue(body instanceof RecetaDTO);

        RecetaDTO dto = (RecetaDTO) body;               // ← ahora SÍ usamos dto
        assertEquals("Juan Perez", dto.getNombrePaciente());

        verify(recetaService).buscarPorCodigo("ABC123");
    }

    @Test
    void obtenerRecetaPorCodigo_noEncontrada_404() {
        when(recetaService.buscarPorCodigo("ZZZ")).thenReturn(null);

        Response r = resource.obtenerRecetaPorCodigo("ZZZ");

        assertEquals(404, r.getStatus());
        assertTrue(String.valueOf(r.getEntity()).contains("Receta no encontrada"));
        verify(recetaService).buscarPorCodigo("ZZZ");
    }

    // ---------- cubrir validarSeguro() ----------

    @Test
    void validarSeguro_simple_call_cubreLinea() {
        resource.validarSeguro("OK");
        // No hay aserciones: solo cobertura de línea / no debe lanzar excepción
    }
}
