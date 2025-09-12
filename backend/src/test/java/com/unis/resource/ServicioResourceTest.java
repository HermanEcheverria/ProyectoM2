package com.unis.resource;

import com.unis.model.Servicio;
import com.unis.service.ServicioService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicioResourceTest {

  @Mock
  ServicioService servicioService;

  @InjectMocks
  ServicioResource resource;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
  }

  // ---------- GET /api/servicios ----------
  @Test
  void listarServicios_ok() {
    Servicio s = new Servicio();
    when(servicioService.listarTodos()).thenReturn(List.of(s));

    List<Servicio> out = resource.listarServicios();

    assertEquals(1, out.size());
    assertSame(s, out.get(0));
    verify(servicioService).listarTodos();
  }

  // ---------- GET /api/servicios/{id}/subservicios ----------
  @Test
  void listarSubServicios_ok() {
    Servicio sub = new Servicio();
    when(servicioService.listarSubServicios(10L)).thenReturn(List.of(sub));

    List<Servicio> out = resource.listarSubServicios(10L);

    assertEquals(1, out.size());
    assertSame(sub, out.get(0));
    verify(servicioService).listarSubServicios(10L);
  }

  // ---------- POST /api/servicios (agregarServicio) ----------
  @Test
  void agregarServicio_400_nombreObligatorio() {
    Response r1 = resource.agregarServicio(null);
    assertEquals(400, r1.getStatus());
    assertTrue(String.valueOf(r1.getEntity()).contains("obligatorio"));

    Servicio s = new Servicio();
    s.nombre = "   ";
    Response r2 = resource.agregarServicio(s);
    assertEquals(400, r2.getStatus());
    verifyNoInteractions(servicioService);
  }

  @Test
  void agregarServicio_400_padreNoExiste() {
    Servicio s = spy(new Servicio());
    s.nombre = "Hijo";
    doReturn(99L).when(s).getParentId(); // <- simula parentId

    when(servicioService.buscarPorId(99L)).thenReturn(null);

    Response r = resource.agregarServicio(s);

    assertEquals(400, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("padre no existe"));
    verify(servicioService).buscarPorId(99L);
    verify(servicioService, never()).agregarServicio(any(), any());
  }

  @Test
  void agregarServicio_201_sinPadre() {
    Servicio in = spy(new Servicio()); // getParentId() -> null por defecto
    in.nombre = "Raiz";

    Servicio creado = new Servicio();
    creado.id = 1L;
    creado.nombre = "Raiz";

    when(servicioService.agregarServicio(any(Servicio.class), isNull())).thenReturn(creado);

    Response r = resource.agregarServicio(in);

    assertEquals(201, r.getStatus());
    assertSame(creado, r.getEntity());
    verify(servicioService).agregarServicio(in, null);
  }

  @Test
  void agregarServicio_201_conPadreExistente() {
    Servicio padre = new Servicio();
    padre.id = 7L;
    padre.nombre = "Padre";

    Servicio hijoIn = spy(new Servicio());
    hijoIn.nombre = "Hijo";
    doReturn(7L).when(hijoIn).getParentId(); // <- simula parentId

    Servicio hijoCreado = new Servicio();
    hijoCreado.id = 8L;
    hijoCreado.nombre = "Hijo";
    hijoCreado.servicioPadre = padre;

    when(servicioService.buscarPorId(7L)).thenReturn(padre);
    when(servicioService.agregarServicio(any(Servicio.class), eq(7L))).thenReturn(hijoCreado);

    Response r = resource.agregarServicio(hijoIn);

    assertEquals(201, r.getStatus());
    assertSame(hijoCreado, r.getEntity());
    assertSame(padre, hijoIn.servicioPadre); // el recurso asigna el padre
    verify(servicioService).buscarPorId(7L);
    verify(servicioService).agregarServicio(hijoIn, 7L);
  }

  @Test
  void agregarServicio_500_errorInterno() {
    Servicio s = spy(new Servicio());
    s.nombre = "Algo";
    // parentId null
    when(servicioService.agregarServicio(any(Servicio.class), isNull()))
        .thenThrow(new RuntimeException("boom"));

    Response r = resource.agregarServicio(s);

    assertEquals(500, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("Error al agregar servicio"));
  }

  // ---------- POST /api/servicios/{id}/subservicios (agregarSubServicio) ----------
  @Test
  void agregarSubServicio_400_sinClave() {
    Response r = resource.agregarSubServicio(5L, Map.of("otra","x"));
    assertEquals(400, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("subServicioId"));
    verifyNoInteractions(servicioService);
  }

  @Test
  void agregarSubServicio_400_tipoInvalido() {
    Response r = resource.agregarSubServicio(5L, Map.of("subServicioId","no-num"));
    assertEquals(400, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("número válido"));
    verifyNoInteractions(servicioService);
  }

  @Test
  void agregarSubServicio_200_ok() {
    Response r = resource.agregarSubServicio(5L, Map.of("subServicioId", 12L));
    assertEquals(200, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("agregado correctamente"));
    verify(servicioService).agregarSubServicio(5L, 12L);
  }

  @Test
  void agregarSubServicio_500_error() {
    doThrow(new RuntimeException("X")).when(servicioService).agregarSubServicio(3L, 2L);

    Response r = resource.agregarSubServicio(3L, Map.of("subServicioId", 2L));

    assertEquals(500, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("Error al agregar subservicio"));
  }

  // ---------- DELETE /api/servicios/{id} ----------
  @Test
  void eliminarServicio_200_ok() {
    Response r = resource.eliminarServicio(11L);
    assertEquals(200, r.getStatus());
    verify(servicioService).eliminarServicio(11L);
  }

  @Test
  void eliminarServicio_500_error() {
    doThrow(new RuntimeException("db")).when(servicioService).eliminarServicio(20L);

    Response r = resource.eliminarServicio(20L);

    assertEquals(500, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("Error al eliminar el servicio"));
  }

  // ---------- DELETE /api/servicios/{id}/subservicios/{subServicioId} ----------
  @Test
  void eliminarRelacion_200_ok() {
    when(servicioService.eliminarRelacion(1L, 2L)).thenReturn(true);

    Response r = resource.eliminarRelacion(1L, 2L);

    assertEquals(200, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("Relación eliminada"));
    verify(servicioService).eliminarRelacion(1L, 2L);
  }

  @Test
  void eliminarRelacion_404_noExiste() {
    when(servicioService.eliminarRelacion(1L, 99L)).thenReturn(false);

    Response r = resource.eliminarRelacion(1L, 99L);

    assertEquals(404, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("no existe"));
  }

  @Test
  void eliminarRelacion_500_error() {
    when(servicioService.eliminarRelacion(7L, 8L)).thenThrow(new RuntimeException("fail"));

    Response r = resource.eliminarRelacion(7L, 8L);

    assertEquals(500, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("Error al eliminar relación"));
  }
}
