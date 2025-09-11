package com.unis.resource;

import com.unis.model.Agenda;
import com.unis.service.AgendaService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendaResourceTest {

  @Mock
  AgendaService agendaService;

  @InjectMocks
  AgendaResource resource; // cubre el ctor por defecto

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void obtenerAgendasPorDoctor_retornaListaDelServicio() {
    Long idDoctor = 7L;
    var a1 = new Agenda();
    var a2 = new Agenda();
    List<Agenda> esperado = List.of(a1, a2);

    when(agendaService.obtenerAgendasPorDoctor(idDoctor)).thenReturn(esperado);

    List<Agenda> result = resource.obtenerAgendasPorDoctor(idDoctor);

    assertSame(esperado, result);
    verify(agendaService, times(1)).obtenerAgendasPorDoctor(idDoctor);
    verifyNoMoreInteractions(agendaService);
  }

  @Test
  void crearAgenda_delegaAlServicio_yRetorna201ConEntidad() {
    var agenda = new Agenda();

    Response resp = resource.crearAgenda(agenda);

    // verificación de delegación
    verify(agendaService, times(1)).crearAgenda(same(agenda));
    verifyNoMoreInteractions(agendaService);

    // verificación de respuesta
    assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
    assertSame(agenda, resp.getEntity());
  }

  @Test
  void actualizarAgenda_delegaAlServicio_yRetorna200ConMensaje() {
    Long id = 10L;
    var actualizada = new Agenda();

    Response resp = resource.actualizarAgenda(id, actualizada);

    verify(agendaService, times(1)).actualizarAgenda(same(id), same(actualizada));
    verifyNoMoreInteractions(agendaService);

    assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    assertEquals("Agenda actualizada con éxito", resp.getEntity());
  }
}
