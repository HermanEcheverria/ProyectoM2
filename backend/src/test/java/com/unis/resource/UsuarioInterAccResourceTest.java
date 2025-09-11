package com.unis.resource;

import com.unis.model.UsuarioInterAcc;
import com.unis.service.UsuarioInterAccService;
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

class UsuarioInterAccResourceTest {

  @Mock
  UsuarioInterAccService usuarioInterAccService;

  @InjectMocks
  UsuarioInterAccResource resource; // Cubre el constructor default

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void obtenerTodos_devuelveLista_yDelegaAlServicio() {
    UsuarioInterAcc u1 = new UsuarioInterAcc();
    UsuarioInterAcc u2 = new UsuarioInterAcc();
    when(usuarioInterAccService.getAllUsuariosInterAcc()).thenReturn(List.of(u1, u2));

    List<UsuarioInterAcc> res = resource.obtenerTodos();

    assertNotNull(res);
    assertEquals(2, res.size());
    assertSame(u1, res.get(0));
    assertSame(u2, res.get(1));
    verify(usuarioInterAccService, times(1)).getAllUsuariosInterAcc();
    verifyNoMoreInteractions(usuarioInterAccService);
  }

  @Test
  void obtenerPorId_ok_siExiste() {
    Long id = 10L;
    UsuarioInterAcc u = new UsuarioInterAcc();
    when(usuarioInterAccService.getUsuarioInterAccById(id)).thenReturn(Optional.of(u));

    Response resp = resource.obtenerPorId(id);

    assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    assertSame(u, resp.getEntity());
    verify(usuarioInterAccService, times(1)).getUsuarioInterAccById(id);
    verifyNoMoreInteractions(usuarioInterAccService);
  }

  @Test
  void obtenerPorId_notFound_siNoExiste() {
    Long id = 11L;
    when(usuarioInterAccService.getUsuarioInterAccById(id)).thenReturn(Optional.empty());

    Response resp = resource.obtenerPorId(id);

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity());
    verify(usuarioInterAccService, times(1)).getUsuarioInterAccById(id);
    verifyNoMoreInteractions(usuarioInterAccService);
  }

  @Test
  void actualizarUsuario_ok_yDelega() {
    Long id = 7L;
    UsuarioInterAcc body = new UsuarioInterAcc();

    Response resp = resource.actualizarUsuario(id, body);

    assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity());
    verify(usuarioInterAccService, times(1)).actualizarUsuarioInterAcc(id, body);
    verifyNoMoreInteractions(usuarioInterAccService);
  }

  @Test
  void eliminarUsuario_ok_yDelega() {
    Long id = 8L;

    Response resp = resource.eliminarUsuario(id);

    assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity());
    verify(usuarioInterAccService, times(1)).eliminarUsuarioInterAcc(id);
    verifyNoMoreInteractions(usuarioInterAccService);
  }
}
