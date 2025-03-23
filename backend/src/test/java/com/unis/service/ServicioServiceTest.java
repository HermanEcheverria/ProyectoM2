package com.unis.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.unis.model.Servicio;
import com.unis.repository.ServicioRepository;

import jakarta.persistence.EntityManager;
import jakarta.ws.rs.WebApplicationException;

public class ServicioServiceTest {

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ServicioService servicioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test listarTodos()
    @Test
    public void testListarTodos() {
        Servicio s1 = new Servicio();
        Servicio s2 = new Servicio();
        List<Servicio> expected = Arrays.asList(s1, s2);
        when(servicioRepository.listAll()).thenReturn(expected);
        List<Servicio> result = servicioService.listarTodos();
        assertEquals(expected, result);
    }

    // Test buscarPorId(Long id)
    @Test
    public void testBuscarPorIdFound() {
        Servicio s = new Servicio();
        when(servicioRepository.findById(1L)).thenReturn(s);
        Servicio result = servicioService.buscarPorId(1L);
        assertEquals(s, result);
    }
    
    @Test
    public void testBuscarPorIdNotFound() {
        when(servicioRepository.findById(1L)).thenReturn(null);
        Servicio result = servicioService.buscarPorId(1L);
        assertNull(result);
    }

    // Test agregarServicio sin padre (parentId null)
    @Test
    public void testAgregarServicioSinPadre() {
        Servicio s = new Servicio();
        doNothing().when(servicioRepository).persist(s);
        Servicio result = servicioService.agregarServicio(s, null);
        verify(servicioRepository, times(1)).persist(s);
        assertNull(s.servicioPadre, "No se debe asignar servicioPadre cuando parentId es null");
        assertEquals(s, result);
    }

    // Test agregarServicio con padre encontrado
    @Test
    public void testAgregarServicioConPadreEncontrado() {
        Servicio s = new Servicio();
        Servicio padre = new Servicio();
        padre.id = 10L;
        when(servicioRepository.findById(10L)).thenReturn(padre);
        doNothing().when(servicioRepository).persist(s);
        Servicio result = servicioService.agregarServicio(s, 10L);
        verify(servicioRepository, times(1)).persist(s);
        assertEquals(padre, s.servicioPadre, "El servicio padre debe asignarse correctamente");
        assertEquals(s, result);
    }
    
    // Test agregarServicio con padre no encontrado
    @Test
    public void testAgregarServicioConPadreNoEncontrado() {
        Servicio s = new Servicio();
        when(servicioRepository.findById(10L)).thenReturn(null);
        doNothing().when(servicioRepository).persist(s);
        Servicio result = servicioService.agregarServicio(s, 10L);
        assertNull(s.servicioPadre, "El servicio padre no se asigna si no se encuentra");
        verify(servicioRepository, times(1)).persist(s);
        assertEquals(s, result);
    }
    
    // Test eliminarServicio cuando se encuentra
    @Test
    public void testEliminarServicioFound() {
        Servicio s = new Servicio();
        when(servicioRepository.findById(1L)).thenReturn(s);
        // Simulamos que delete() es void
        doNothing().when(servicioRepository).delete(s);
        servicioService.eliminarServicio(1L);
        verify(servicioRepository, times(1)).delete(s);
    }
    
    // Test eliminarServicio cuando no se encuentra
    @Test
    public void testEliminarServicioNotFound() {
        when(servicioRepository.findById(1L)).thenReturn(null);
        servicioService.eliminarServicio(1L);
        verify(servicioRepository, never()).delete(any(Servicio.class));
    }
    
    // Test listarSubServicios cuando el padre existe
// Test listarSubServicios cuando el padre existe
@Test
public void testListarSubServiciosFound() {
    Servicio padre = new Servicio();
    Servicio sub1 = new Servicio();
    Servicio sub2 = new Servicio();
    // Asignamos los subservicios al padre
    padre.subServicios = new HashSet<>(Arrays.asList(sub1, sub2));
    
    when(servicioRepository.findById(1L)).thenReturn(padre);
    List<Servicio> result = servicioService.listarSubServicios(1L);
    
    // Se espera que se retorne la misma lista asignada
    assertEquals(Arrays.asList(sub1, sub2), result, "Las sublistas deben coincidir");
}



    
    // Test listarSubServicios cuando no se encuentra el padre
    @Test
    public void testListarSubServiciosNotFound() {
        when(servicioRepository.findById(1L)).thenReturn(null);
        List<Servicio> result = servicioService.listarSubServicios(1L);
        assertTrue(result.isEmpty(), "Si no se encuentra el padre, se retorna lista vacía");
    }
    
    // Test agregarSubServicio exitoso
    @Test
    public void testAgregarSubServicioSuccess() {
        Servicio servicioPadre = new Servicio();
        servicioPadre.id = 1L;
        Servicio subServicio = new Servicio();
        subServicio.id = 2L;
        when(servicioRepository.findById(1L)).thenReturn(servicioPadre);
        when(servicioRepository.findById(2L)).thenReturn(subServicio);
        when(entityManager.merge(subServicio)).thenReturn(subServicio);
        servicioService.agregarSubServicio(1L, 2L);
        assertEquals(servicioPadre, subServicio.servicioPadre, "El subservicio debe tener asignado el servicio padre");
        verify(entityManager, times(1)).merge(subServicio);
    }
    
    // Test agregarSubServicio cuando alguno no se encuentra
    @Test
    public void testAgregarSubServicioNotFound() {
        when(servicioRepository.findById(1L)).thenReturn(null);
        Servicio subServicio = new Servicio();
        subServicio.id = 2L;
        when(servicioRepository.findById(2L)).thenReturn(subServicio);
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            servicioService.agregarSubServicio(1L, 2L);
        });
        assertEquals(404, ex.getResponse().getStatus());
    }
    
    // Test eliminarRelacion exitosa
    @Test
    public void testEliminarRelacionSuccess() {
        Servicio padre = new Servicio();
        padre.id = 1L;
        Servicio subServicio = new Servicio();
        subServicio.id = 2L;
        subServicio.servicioPadre = padre;
        when(servicioRepository.findById(2L)).thenReturn(subServicio);
        when(entityManager.merge(subServicio)).thenReturn(subServicio);
        boolean result = servicioService.eliminarRelacion(1L, 2L);
        assertTrue(result);
        assertNull(subServicio.servicioPadre);
        verify(entityManager, times(1)).merge(subServicio);
    }
    
    // Test eliminarRelacion cuando la relación no existe
    @Test
    public void testEliminarRelacionFailure() {
        Servicio subServicio = new Servicio();
        subServicio.id = 2L;
        subServicio.servicioPadre = null;
        when(servicioRepository.findById(2L)).thenReturn(subServicio);
        boolean result = servicioService.eliminarRelacion(1L, 2L);
        assertFalse(result);
        verify(entityManager, never()).merge(any(Servicio.class));
    }
}
