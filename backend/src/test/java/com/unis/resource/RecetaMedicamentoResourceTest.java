package com.unis.resource;

import com.unis.model.RecetaMedicamento;
import com.unis.service.RecetaMedicamentoService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecetaMedicamentoResourceTest {

  @Mock
  RecetaMedicamentoService recetaMedicamentoService;

  @InjectMocks
  RecetaMedicamentoResource resource; // Cubre el ctor por defecto

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void listarPorReceta_devuelveLista_yDelegaAlServicio() {
    Long idReceta = 42L;
    RecetaMedicamento rm1 = new RecetaMedicamento();
    RecetaMedicamento rm2 = new RecetaMedicamento();
    when(recetaMedicamentoService.listarPorReceta(idReceta)).thenReturn(List.of(rm1, rm2));

    List<RecetaMedicamento> res = resource.listarPorReceta(idReceta);

    assertNotNull(res);
    assertEquals(2, res.size());
    assertSame(rm1, res.get(0));
    assertSame(rm2, res.get(1));
    verify(recetaMedicamentoService, times(1)).listarPorReceta(idReceta);
    verifyNoMoreInteractions(recetaMedicamentoService);
  }

  @Test
  void agregarMedicamento_retorna201_yDelegaConElBody() {
    RecetaMedicamento body = new RecetaMedicamento();

    Response resp = resource.agregarMedicamento(body);

    assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity());
    verify(recetaMedicamentoService, times(1)).agregarMedicamentoAReceta(same(body));
    verifyNoMoreInteractions(recetaMedicamentoService);
  }

  @Test
  void eliminarMedicamento_ok_cuandoServicioRetornaTrue() {
    Long id = 7L;
    when(recetaMedicamentoService.eliminar(id)).thenReturn(true);

    Response resp = resource.eliminarMedicamento(id);

    assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity());
    verify(recetaMedicamentoService, times(1)).eliminar(id);
    verifyNoMoreInteractions(recetaMedicamentoService);
  }

  @Test
  void eliminarMedicamento_notFound_cuandoServicioRetornaFalse() {
    Long id = 99L;
    when(recetaMedicamentoService.eliminar(id)).thenReturn(false);

    Response resp = resource.eliminarMedicamento(id);

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity());
    verify(recetaMedicamentoService, times(1)).eliminar(id);
    verifyNoMoreInteractions(recetaMedicamentoService);
  }
}
