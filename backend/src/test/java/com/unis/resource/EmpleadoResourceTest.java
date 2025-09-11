package com.unis.resource;

import com.unis.model.Empleado;
import com.unis.service.EmpleadoService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmpleadoResourceTest {

  @Mock
  EmpleadoService empleadoService;

  @InjectMocks
  EmpleadoResource resource;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // ---------- GET /empleado ----------
  @Test
  void obtenerTodosLosEmpleados_devuelveLista() {
    Empleado e1 = new Empleado();
    Empleado e2 = new Empleado();
    when(empleadoService.getAllEmpleados()).thenReturn(List.of(e1, e2));

    List<Empleado> res = resource.obtenerTodosLosEmpleados();

    assertNotNull(res);
    assertEquals(2, res.size());
    verify(empleadoService, times(1)).getAllEmpleados();
  }

  // ---------- GET /empleado/{id} ----------
  @Test
  void obtenerEmpleado_encontrado_200() {
    Long id = 10L;
    Empleado e = new Empleado();
    when(empleadoService.getEmpleadoById(id)).thenReturn(Optional.of(e));

    Response r = resource.obtenerEmpleado(id);

    assertEquals(200, r.getStatus());
    assertSame(e, r.getEntity());
    verify(empleadoService, times(1)).getEmpleadoById(id);
  }

  @Test
  void obtenerEmpleado_noEncontrado_404() {
    Long id = 10L;
    when(empleadoService.getEmpleadoById(id)).thenReturn(Optional.empty());

    Response r = resource.obtenerEmpleado(id);

    assertEquals(404, r.getStatus());
    assertNull(r.getEntity());
    verify(empleadoService, times(1)).getEmpleadoById(id);
  }

  // ---------- POST /empleado ----------
  @Test
  void registrarEmpleado_crea_201() {
    Empleado nuevo = new Empleado();

    Response r = resource.registrarEmpleado(nuevo);

    assertEquals(201, r.getStatus());
    verify(empleadoService, times(1)).registrarEmpleado(nuevo);
  }

  // ---------- PUT /empleado/{id} ----------
  @Test
  void actualizarEmpleado_existe_200() {
    Long id = 3L;
    Empleado cambios = new Empleado();
    when(empleadoService.actualizarEmpleado(id, cambios)).thenReturn(true);

    Response r = resource.actualizarEmpleado(id, cambios);

    assertEquals(200, r.getStatus());
    verify(empleadoService, times(1)).actualizarEmpleado(id, cambios);
  }

  @Test
  void actualizarEmpleado_noExiste_404() {
    Long id = 3L;
    Empleado cambios = new Empleado();
    when(empleadoService.actualizarEmpleado(id, cambios)).thenReturn(false);

    Response r = resource.actualizarEmpleado(id, cambios);

    assertEquals(404, r.getStatus());
    verify(empleadoService, times(1)).actualizarEmpleado(id, cambios);
  }

  // ---------- DELETE /empleado/{id} ----------
  @Test
  void eliminarEmpleado_existe_200() {
    Long id = 7L;
    when(empleadoService.eliminarEmpleado(id)).thenReturn(true);

    Response r = resource.eliminarEmpleado(id);

    assertEquals(200, r.getStatus());
    verify(empleadoService, times(1)).eliminarEmpleado(id);
  }

  @Test
  void eliminarEmpleado_noExiste_404() {
    Long id = 7L;
    when(empleadoService.eliminarEmpleado(id)).thenReturn(false);

    Response r = resource.eliminarEmpleado(id);

    assertEquals(404, r.getStatus());
    verify(empleadoService, times(1)).eliminarEmpleado(id);
  }
}
