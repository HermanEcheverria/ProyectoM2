package com.unis.resource;

import com.unis.model.Usuario;
import com.unis.service.UsuarioService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioResourceTest {

  @Mock
  UsuarioService usuarioService;

  @InjectMocks
  UsuarioResource resource; // cubre el constructor por defecto

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void registrarUsuario_debeGuardarYRetornar201ConMensaje() {
    // Arrange
    var usuario = new Usuario();

    // Act
    Response response = resource.registrarUsuario(usuario);

    // Assert: delega al servicio
    verify(usuarioService, times(1)).registrarUsuario(same(usuario));
    verifyNoMoreInteractions(usuarioService);

    // Assert: respuesta
    assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());

    assertNotNull(response.getEntity());
    assertTrue(response.getEntity() instanceof Map);

    @SuppressWarnings("unchecked")
    Map<String, String> body = (Map<String, String>) response.getEntity();
    assertEquals("Usuario registrado con éxito", body.get("mensaje"));
  }
}
