package com.unis.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Historia;
import com.unis.repository.HistoriaRepository;

public class HistoriaServiceTest {

    @Mock
    HistoriaRepository historiaRepository;

    @InjectMocks
    HistoriaService historiaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para listar todas las historias
    @Test
    public void testListar() {
        List<Historia> expected = Arrays.asList(new Historia(), new Historia());
        when(historiaRepository.listAll()).thenReturn(expected);

        List<Historia> result = historiaService.listar();
        assertEquals(expected, result, "La lista de historias debe coincidir con la esperada");
    }

    // Test para obtener una historia por ID cuando se encuentra
    @Test
    public void testObtenerPorIdFound() {
        Historia historia = new Historia();
        when(historiaRepository.findById(1L)).thenReturn(historia);

        Historia result = historiaService.obtenerPorId(1L);
        assertEquals(historia, result, "La historia obtenida debe ser la misma que la retornada por el repositorio");
    }

    // Test para obtener una historia por ID cuando no se encuentra
    @Test
    public void testObtenerPorIdNotFound() {
        when(historiaRepository.findById(1L)).thenReturn(null);

        Historia result = historiaService.obtenerPorId(1L);
        assertNull(result, "Al no encontrar la historia se debe retornar null");
    }

    // Test para crear (registrar) una historia
    @Test
    public void testCrear() {
        Historia historia = new Historia();
        // Simulamos que persist no lanza excepción
        doNothing().when(historiaRepository).persist(historia);

        Historia result = historiaService.crear(historia);
        verify(historiaRepository, times(1)).persist(historia);
        assertEquals(historia, result, "La historia creada debe ser la misma pasada como argumento");
    }

    // Test para actualizar una historia cuando se encuentra
    @Test
    public void testActualizarFound() {
        Historia existente = new Historia();
        // Se asignan valores iniciales
        existente.setNombreEntidad("EntidadVieja");
        existente.setHistoria("HistoriaVieja");
        existente.setMeritos("MéritosViejos");
        existente.setLineaDelTiempo("LíneaVieja");

        Historia updateData = new Historia();
        // Valores nuevos para actualizar
        updateData.setNombreEntidad("EntidadNueva");
        updateData.setHistoria("HistoriaNueva");
        updateData.setMeritos("MéritosNuevos");
        updateData.setLineaDelTiempo("LíneaNueva");

        when(historiaRepository.findById(1L)).thenReturn(existente);

        Historia result = historiaService.actualizar(1L, updateData);

        // Se verifica que los campos hayan sido actualizados
        assertEquals("EntidadNueva", existente.getNombreEntidad());
        assertEquals("HistoriaNueva", existente.getHistoria());
        assertEquals("MéritosNuevos", existente.getMeritos());
        assertEquals("LíneaNueva", existente.getLineaDelTiempo());
        assertEquals(existente, result, "El método debe retornar la historia actualizada");
    }

    // Test para actualizar una historia cuando no se encuentra
    @Test
    public void testActualizarNotFound() {
        when(historiaRepository.findById(1L)).thenReturn(null);

        Historia updateData = new Historia();
        Historia result = historiaService.actualizar(1L, updateData);
        assertNull(result, "Si no se encuentra la historia, se debe retornar null");
    }

    // Test para eliminar una historia cuando la eliminación es exitosa
    @Test
    public void testEliminarSuccess() {
        when(historiaRepository.deleteById(1L)).thenReturn(true);

        boolean result = historiaService.eliminar(1L);
        assertTrue(result, "La eliminación debe retornar true cuando es exitosa");
    }

    // Test para eliminar una historia cuando la eliminación falla
    @Test
    public void testEliminarFailure() {
        when(historiaRepository.deleteById(1L)).thenReturn(false);

        boolean result = historiaService.eliminar(1L);
        assertFalse(result, "La eliminación debe retornar false cuando falla");
    }
}
